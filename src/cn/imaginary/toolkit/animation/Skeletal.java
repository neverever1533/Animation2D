package cn.imaginary.toolkit.animation;

import cn.imaginary.toolkit.Model;
import cn.imaginary.toolkit.model.Bone;
import cn.imaginary.toolkit.model.Joint;
import cn.imaginary.toolkit.model.Mesh;

import javax.swing.tree.DefaultMutableTreeNode;

import java.awt.geom.AffineTransform;
import java.awt.Point;

import java.io.File;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Properties;

public class Skeletal {
    private String suffix_Type = "type";
    private String suffix_Type_Frame = "frame";
    private String suffix_Type_Model = "model";
    private String suffix_Type_Properties = "Properties";
    private String suffix_Type_Skeletal = "skeletal";

    private ArrayList<Properties> arrayList_Skeletal;

    public Skeletal() {
    }

    public Model readModel(File file, int index) {
        if (null != file) {
            return readModel(file.getAbsolutePath(), index);
        } else {
            return null;
        }
    }

    public Model readModel(String filePath, int index) {
        if (null == filePath) {
            return null;
        }
        Model model = new Model();
        model.setName(model.getType() + index);
        model.setID(index);

        Bone bone = new Bone();
        bone.setName(bone.getType() + index);
        model.setBone(bone);

        Joint joint = new Joint();
        joint.setName(joint.getType() + index);
        model.setJoint(joint);

        Mesh mesh = new Mesh();
        mesh.setName(mesh.getType() + index);
        mesh.setSkinPath(filePath);
        model.setMesh(mesh);

        return model;
    }

    private Model readModel(Properties properties) {
        Model model = null;
        if (null != properties) {
            Object object = properties.get("id");
            if (null != object) {
                model = new Model();
                model.setProperties(properties);
                object = properties.get("path");
                if (object instanceof String) {
                    model.getMesh().setSkinPath((String) object);
                }
            }
        }
        return model;
    }

    public void readModel(DefaultMutableTreeNode treeNode, Model model) {
        updateTreeNode(treeNode, model);
    }

    public ArrayList<Properties> getPropertiesList() {
        if (null == arrayList_Skeletal) {
            arrayList_Skeletal = new ArrayList<>();
        }
        return arrayList_Skeletal;
    }

    public void setPropertiesList(ArrayList<Properties> arrayList) {
        arrayList_Skeletal = arrayList;
    }

    public static DefaultMutableTreeNode getTreeNode(DefaultMutableTreeNode root, int id) {
        return getTreeNode(root, null, id);
    }

    public static DefaultMutableTreeNode getTreeNode(DefaultMutableTreeNode root, String name) {
        return getTreeNode(root, name, -1);
    }

    private static DefaultMutableTreeNode getTreeNode(DefaultMutableTreeNode root, String name, int id) {
        if (null != root && (null != name || id != -1)) {
            for (Enumeration enumeration = root.depthFirstEnumeration(); enumeration.hasMoreElements(); ) {
                Object object = enumeration.nextElement();
                if (object instanceof DefaultMutableTreeNode) {
                    DefaultMutableTreeNode node = (DefaultMutableTreeNode) object;
                    Object obj = node.getUserObject();
                    if (obj instanceof Model) {
                        if (id != -1 && id == ((Model) obj).getID()) {
                            return node;
                        } else {
                            if (name != null && name.equals(((Model) obj).getName())) {
                                return node;
                            }
                        }
                    }
                }
            }
        }
        return null;
    }

    private DefaultMutableTreeNode getTreeNode(Properties properties) {
        DefaultMutableTreeNode treeNode = null;
        if (null != properties) {
            Object object = properties.get(suffix_Type);
            if (object instanceof String) {
                String type = (String) object;
                treeNode = new DefaultMutableTreeNode();
                if (suffix_Type_Skeletal.equals(type)) {
                    int index = 0;
                    treeNode.setUserObject(suffix_Type_Frame + index);
                    int size = properties.size();
                    if (size > 0) {
                        for (int i = 0; i < size; i++) {
                            Object object_Child_Properties = properties.get(suffix_Type_Model + i + suffix_Type_Properties);
                            if (object_Child_Properties instanceof Properties) {
                                DefaultMutableTreeNode treeNode_Child = getTreeNode((Properties) object_Child_Properties);
                                if (null != treeNode_Child) {
                                    treeNode.add(treeNode_Child);
                                }
                            }
                        }
                    }
                } else if (suffix_Type_Model.equals(type)) {
                    Model model = readModel(properties);
                    if (null != model) {
                        treeNode.setUserObject(model);
                        updateSkeletal(treeNode, properties);
                    }
                }
            }
        }
        return treeNode;
    }

    public void resetTreeNode(DefaultMutableTreeNode treeNode) {
        if (null != treeNode) {
            Object object = treeNode.getUserObject();
            if (object instanceof Model) {
                Model model = (Model) object;
                AffineTransform transform = new AffineTransform();

                Point localTranslation = model.getLocalTranslation();
                if (null == localTranslation) {
                    localTranslation = model.getTranslation();
                }
                model.setTranslation(localTranslation);
                model.setGlobalTranslation(localTranslation);
                transform.translate(localTranslation.getX(), localTranslation.getY());

                double localRotationDegrees = model.getLocalRotationDegrees();
                model.setRotationDegrees(localRotationDegrees);
                model.setGlobalRotationDegrees(localRotationDegrees);
                Point localAnchor = model.getLocalAnchor();
                if (null == localAnchor) {
                    localAnchor = model.getAnchor();
                }
                model.setAnchor(localAnchor);
                model.setGlobalAnchor(localAnchor);
                double ax = localAnchor.getX();
                double ay = localAnchor.getY();
                transform.translate(ax, ay);
                transform.rotate(Math.toRadians(localRotationDegrees));
                transform.translate(-ax, -ay);

                Point localScale = model.getLocalScale();
                if (null == localScale) {
                    localScale = model.getScale();
                }
                model.setScale(localScale);
                model.setGlobalScale(localScale);
                transform.scale(localScale.getX(), localScale.getY());

                model.setTransform(transform);
            }
            if (treeNode.getChildCount() > 0) {
                for (Enumeration enumeration = treeNode.children(); enumeration.hasMoreElements(); ) {
                    Object obj = enumeration.nextElement();
                    if (obj instanceof DefaultMutableTreeNode) {
                        resetTreeNode((DefaultMutableTreeNode) obj);
                    }
                }
            }
        }
    }

    public void updateTreeNode(DefaultMutableTreeNode treeNode, Model model) {
        if (null != treeNode && null != model) {
            DefaultMutableTreeNode treeNode_Model = new DefaultMutableTreeNode();
            treeNode_Model.setUserObject(model);
            treeNode.add(treeNode_Model);
        }
    }

    public void updateTreeNode(DefaultMutableTreeNode treeNode, ArrayList<Model> arrayList) {
        if (null != arrayList) {
//            for (Iterator<Model> iterator = arrayList.iterator(); iterator.hasNext(); ) {
//                updateTreeNode(treeNode, iterator.next());
//            }
            for (int i = 0; i < arrayList.size(); i++) {
                updateTreeNode(treeNode, arrayList.get(i));
            }
        }
    }

    public DefaultMutableTreeNode updateSkeletal(Properties properties) {
        DefaultMutableTreeNode treeNode = null;
        if (null != properties) {
            treeNode = getTreeNode(properties);
            if (null != treeNode) {
                updateTreeNode(treeNode, properties);
            }
        }
        return treeNode;
    }

    private void updateSkeletal(DefaultMutableTreeNode treeNode, Properties properties) {
        if (null != treeNode && null != properties) {
            Object object = properties.get(suffix_Type);
            Object object_TreeNode = treeNode.getUserObject();
            if (object instanceof String && suffix_Type_Model.equals(object) && object_TreeNode instanceof Model) {
                Model model = (Model) object_TreeNode;
                Object obj = properties.get("id");
                int id = -1;
                if (obj instanceof Number) {
                    id = (int) obj;
                } else if (obj instanceof String) {
                    id = Integer.parseInt((String) obj);
                }
                if (id != -1 && id == model.getID()) {
                    object = properties.get("localTranslationX");
                    double translationX = 0;
                    if (object instanceof Number) {
                        translationX = (double) object;
                    } else if (object instanceof String) {
                        translationX = Double.parseDouble((String) object);
                    }
                    object = properties.get("localTranslationY");
                    double translationY = 0;
                    if (object instanceof Number) {
                        translationY = (double) object;
                    } else if (object instanceof String) {
                        translationY = Double.parseDouble((String) object);
                    }
                    object = properties.get("gravityRotationDegrees");
                    double gravityRotationDegrees = 0;
                    if (object instanceof Number) {
                        gravityRotationDegrees = (double) object;
                    } else if (object instanceof String) {
                        gravityRotationDegrees = Double.parseDouble((String) object);
                    }
                    object = properties.get("localRotationDegrees");
                    double rotationDegrees = 0;
                    if (object instanceof Number) {
                        rotationDegrees = (double) object;
                    } else if (object instanceof String) {
                        rotationDegrees = Double.parseDouble((String) object);
                    }
                    object = properties.get("localAnchorX");
                    double anchorX = 0;
                    if (object instanceof Number) {
                        anchorX = (double) object;
                    } else if (object instanceof String) {
                        anchorX = Double.parseDouble((String) object);
                    }
                    object = properties.get("localAnchorY");
                    double anchorY = 0;
                    if (object instanceof Number) {
                        anchorY = (double) object;
                    } else if (object instanceof String) {
                        anchorY = Double.parseDouble((String) object);
                    }
                    object = properties.get("localScaleX");
                    double scaleX = 0;
                    if (object instanceof Number) {
                        scaleX = (double) object;
                    } else if (object instanceof String) {
                        scaleX = Double.parseDouble((String) object);
                    }
                    object = properties.get("localScaleY");
                    double scaleY = 0;
                    if (object instanceof Number) {
                        scaleY = (double) object;
                    } else if (object instanceof String) {
                        scaleY = Double.parseDouble((String) object);
                    }
                    updateSkeletal(treeNode, translationX, translationY, gravityRotationDegrees, rotationDegrees, anchorX, anchorY, scaleX, scaleY);
                }
            }
        }
    }

    public void updateSkeletal(DefaultMutableTreeNode treeNode, double translationX, double translationY, double gravityDegrees, double rotationDegrees, double anchorX, double anchorY, double scaleX, double scaleY) {
        if (null != treeNode) {
            Object object = treeNode.getUserObject();
            if (object instanceof Model) {
                Model model = (Model) object;
                model.setTranslational(true);
                model.setRotational(true);
                model.setScaled(true);
                model.setGravity(false);
                model.setVisible(true);
                model.updateTransformDegrees(translationX, translationY, rotationDegrees, anchorX, anchorY, scaleX, scaleY);
                model.setLocalTranslation(translationX, translationY);
                model.setLocalRotationDegrees(rotationDegrees);
                model.setLocalAnchor(anchorX, anchorY);
                model.setLocalScale(scaleX, scaleY);
                model.setLocation(translationX, translationY);
                model.setGravityRotationDegrees(gravityDegrees);
                updateSkeletal(model);
            }
        }
    }

    private void updateSkeletal(Model model) {
        if (null != model) {
            Properties properties_Skeletal = new Properties();
            properties_Skeletal.put(suffix_Type, suffix_Type_Model);
            properties_Skeletal.put("id", model.getID());
            properties_Skeletal.put("parent", -1);
            Point localTranslation = model.getLocalTranslation();
            if (null != localTranslation) {
                properties_Skeletal.put("localTranslationX", localTranslation.getX());
                properties_Skeletal.put("localTranslationY", localTranslation.getY());
            }
            double gravityRotationDegrees = model.getGravityRotationDegrees();
            properties_Skeletal.put("gravityRotationDegrees", gravityRotationDegrees);
            double rotationDegrees = model.getLocalRotationDegrees();
            properties_Skeletal.put("localRotationDegrees", rotationDegrees);
            Point localAnchor = model.getLocalAnchor();
            if (null != localAnchor) {
                properties_Skeletal.put("localAnchorX", localAnchor.getX());
                properties_Skeletal.put("localAnchorY", localAnchor.getY());
            }
            Point localScale = model.getLocalScale();
            if (null != localScale) {
                properties_Skeletal.put("localScaleX", localScale.getX());
                properties_Skeletal.put("localScaleY", localScale.getY());
            }
            String skinPath = model.getMesh().getSkinPath();
            if (null != skinPath) {
                properties_Skeletal.put("path", skinPath);
            }
            /*String text = model.getMesh().getText();
            if (null != text) {
                properties_Skeletal.put("text", text);
            }
            Font font = model.getMesh().getTextFont();
            if (null != font) {
                properties_Skeletal.put("fontName", font.getName());
            }
            Color color = model.getMesh().getTextColor();
            if (null != color) {
                properties_Skeletal.put("colorRed", color.getRed());
                properties_Skeletal.put("colorGreen", color.getGreen());
                properties_Skeletal.put("colorBlue", color.getBlue());
            }*/
            getPropertiesList().add(properties_Skeletal);
        }
    }

    public void updateRootSkeletal(DefaultMutableTreeNode root, double translationX, double translationY, double gravityDegrees, double rotationDegrees, double anchorX, double anchorY, double scaleX, double scaleY) {
        if (null != root) {
            for (Enumeration enumeration = root.breadthFirstEnumeration(); enumeration.hasMoreElements(); ) {
                updateSkeletal((DefaultMutableTreeNode) enumeration.nextElement(), translationX, translationY, gravityDegrees, rotationDegrees, anchorX, anchorY, scaleX, scaleY);
            }
        }
    }

    public void updateTreeNode(DefaultMutableTreeNode from, DefaultMutableTreeNode to) {
        if (null != from && null != to && !from.isNodeAncestor(to)) {
            DefaultMutableTreeNode node = (DefaultMutableTreeNode) from.getParent();
            if (null != node) {
                node.remove(from);
                to.add(from);

                Object object_To = to.getUserObject();
                int id_to = -1;
                if (object_To instanceof Model) {
                    id_to = ((Model) object_To).getID();
                }
                Object object_From = from.getUserObject();
                int id_from;
                if (object_From instanceof Model) {
                    id_from = ((Model) object_From).getID();
                    for (Iterator<Properties> iterator = getPropertiesList().iterator(); iterator.hasNext(); ) {
                        Properties properties = iterator.next();
                        Object obj = properties.get("id");
                        if (obj instanceof Number && id_from == (int) obj) {
                            properties.put("parent", id_to);
                        }
                    }
                }
            }
        }
    }

    public void updateTreeNode(DefaultMutableTreeNode root, int from, int to) {
        updateTreeNode(getTreeNode(root, from), getTreeNode(root, to));
    }

    private void updateTreeNode(DefaultMutableTreeNode root, Properties properties) {
        if (null != root && null != properties) {
            Object type = properties.get(suffix_Type);
            if (type instanceof String) {
                if (suffix_Type_Skeletal.equals(type)) {
                    for (Iterator<Object> iterator = properties.values().iterator(); iterator.hasNext(); ) {
                        Object value = iterator.next();
                        if (value instanceof Properties) {
                            updateTreeNode(root, (Properties) value);
                        }
                    }
                } else if (suffix_Type_Model.equals(type)) {
                    Object obj = properties.get("id");
                    int id = -1;
                    if (obj instanceof Number) {
                        id = (int) obj;
                    }
                    if (obj instanceof String) {
                        id = Integer.parseInt((String) obj);
                    }
                    Object obj_Parent = properties.get("parent");
                    int id_Parent = -1;
                    if (obj_Parent instanceof Number) {
                        id_Parent = (int) obj_Parent;
                    }
                    if (obj_Parent instanceof String) {
                        id_Parent = Integer.parseInt((String) obj_Parent);
                    }
                    if (id != -1 && id_Parent != -1) {
                        updateTreeNode(root, id, id_Parent);
                    }
                }
            }
        }
    }
}
