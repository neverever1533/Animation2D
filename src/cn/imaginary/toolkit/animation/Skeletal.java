package cn.imaginary.toolkit.animation;

import cn.imaginary.toolkit.Model;
import cn.imaginary.toolkit.model.Bone;
import cn.imaginary.toolkit.model.Joint;
import cn.imaginary.toolkit.model.Mesh;

import javax.imageio.ImageIO;

import javax.swing.tree.DefaultMutableTreeNode;

import java.awt.Point;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

import java.io.File;
import java.io.IOException;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Properties;

public class Skeletal {
    private String suffix_Type = "type";
    private String suffix_Type_Bone = "bone";
    private String suffix_Type_Frame = "frame";
    private String suffix_Type_Joint = "joint";
    private String suffix_Type_Keyframe = "keyframe";
    private String suffix_Type_Mesh = "mesh";
    private String suffix_Type_Model = "model";
    private String suffix_Type_Null = "null";
    private String suffix_Type_Object = "object";
    private String suffix_Type_Project = "project";
    private String suffix_Type_Properties = "Properties";
    private String suffix_Type_Skeletal = "skeletal";
    private String suffix_Type_TreeNode = "treeNode";
    private String suffix_Type_TreeNode_Child = "childNode";
    private String suffix_Type_TreeNode_Root = "rootNode";

    public Skeletal() {
    }

    public DefaultMutableTreeNode readSkeletal(String filePath, int index) {
        return readSkeletal(new File(filePath), index);
    }

    public DefaultMutableTreeNode readSkeletal(File file, int index) {
        return null;
    }

    public Model readImage(String filePath, int index) {
        return readImage(new File(filePath), index);
    }

    public Model readImage(File file, int index) {
        BufferedImage image;
        try {
            image = ImageIO.read(file);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        if (null == image) {
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
        mesh.setImagePath(file.getAbsolutePath());
        mesh.setSkin(image);
        model.setMesh(mesh);

        return model;
    }

    public void readModel(DefaultMutableTreeNode treeNode, Model model) {
        updateTreeNode(treeNode, model);
    }

    public DefaultMutableTreeNode readSkeletal(Properties properties) {
        return getTreeNode(properties);
    }

    public DefaultMutableTreeNode getTreeNode(DefaultMutableTreeNode root, int id) {
        return getTreeNode(root, null, id);
    }

    public DefaultMutableTreeNode getTreeNode(DefaultMutableTreeNode root, String name) {
        return getTreeNode(root, name, -1);
    }

    private DefaultMutableTreeNode getTreeNode(DefaultMutableTreeNode root, String name, int id) {
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

    public DefaultMutableTreeNode getTreeNode(Properties properties) {
        DefaultMutableTreeNode treeNode = null;
        return treeNode;
    }

    public void readTreeNode(DefaultMutableTreeNode treeNode, int index, Properties properties) {
        if (null != treeNode) {
            if (null == properties) {
                properties = new Properties();
            }
            properties.put(suffix_Type, suffix_Type_TreeNode);
            Object object = treeNode.getUserObject();
            Properties prop = new Properties();
            prop.put(suffix_Type, suffix_Type_Object);
            if (object instanceof Model) {
                Model model = (Model) object;
                prop.put(suffix_Type_Object + index + suffix_Type_Properties, model.getProperties());
            } else {
                if (null == object) {
                    object = suffix_Type_Null;
                }
                prop.put(suffix_Type_Object + index + suffix_Type_Properties, object);
            }
            properties.put(suffix_Type_TreeNode + index + suffix_Type_Properties, prop);
            if (treeNode.getChildCount() > 0) {
                int index_Child = 0;
                for (Enumeration enumeration = treeNode.children(); enumeration.hasMoreElements(); ) {
                    Object obj = enumeration.nextElement();
                    if (obj instanceof DefaultMutableTreeNode) {
                        Properties properties_Child = new Properties();
                        readTreeNode((DefaultMutableTreeNode) obj, index_Child, properties_Child);
                        properties.put(suffix_Type_TreeNode_Child + (index_Child++) + suffix_Type_Properties, properties_Child);
                    }
                }
            }
        }
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

    public void updateSkeletal(DefaultMutableTreeNode treeNode, double translationX, double translationY, double gravityDegrees, double rotationDegrees, double anchorX, double anchorY, double scaleX, double scaleY) {
        if (null != treeNode) {
            Object object = treeNode.getUserObject();
            Model model = null;
            if (object instanceof Model) {
                model = (Model) object;
                model.setTranslational(true);
                model.setRotational(true);
                model.setScaled(true);
                model.setGravity(false);
                model.setVisible(true);
                model.updateTransformDegrees(translationX, translationY, rotationDegrees, anchorX, anchorY, scaleX, scaleY);
//                updateSkeletal(model, translationX, translationY, rotationDegrees, anchorX, anchorY, scaleX, scaleY);
                model.setLocalTranslation(translationX, translationY);
                model.setLocalRotationDegrees(rotationDegrees);
                model.setLocalAnchor(anchorX, anchorY);
                model.setLocalScale(scaleX, scaleY);
                model.setLocation(translationX, translationY);
                model.setGravityRotationDegrees(gravityDegrees);
            }
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
            if (from.getUserObject() instanceof Model && to.getUserObject() instanceof Model) {
                DefaultMutableTreeNode node = (DefaultMutableTreeNode) from.getParent();
                if (null != node) {
                    node.remove(from);
                    to.add(from);
                }
            }
        }
    }

    public void updateTreeNode(DefaultMutableTreeNode root, int from, int to) {
        updateTreeNode(getTreeNode(root, from), getTreeNode(root, to));
    }
}
