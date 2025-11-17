package cn.imaginary.toolkit.animation;

import cn.imaginary.toolkit.Model;

import java.awt.Point;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Properties;

import javax.swing.tree.DefaultMutableTreeNode;

public class Keyframe {
    private String suffix_Type = "type";
    private String suffix_Type_Keyframe = "keyframe";
    private String suffix_Type_Model = "model";
    private String suffix_Type_Properties = "Properties";

    private ArrayList<Properties> arrayList_Keyframe;

    public Keyframe() {
    }

    public ArrayList<Properties> getPropertiesList() {
        if (null == arrayList_Keyframe) {
            arrayList_Keyframe = new ArrayList<>();
        }
        return arrayList_Keyframe;
    }

    public void setPropertiesList(ArrayList<Properties> arrayList) {
        arrayList_Keyframe = arrayList;
    }

    public void updateKeyframe(DefaultMutableTreeNode root, Properties properties) {
        if (null != root && null != properties) {
            Object object = properties.get(suffix_Type);
            if (object instanceof String) {
                if (suffix_Type_Keyframe.equals(object)) {
                    int size = properties.size();
                    if (size > 0) {
                        for (int i = 0; i < size; i++) {
                            Object object_Child_Properties = properties.get(suffix_Type_Model + i + suffix_Type_Properties);
                            if (object_Child_Properties instanceof Properties) {
                                updateKeyframe(root, (Properties) object_Child_Properties);
                            }
                        }
                    }
                } else if (suffix_Type_Model.equals(object)) {
                    updateTransform(root, properties);
                }
            }
        }
    }

    private void updateKeyframe(DefaultMutableTreeNode treeNode, boolean isVisible, boolean isTranslational, double x, double y, boolean isRotational, double angle, boolean isScaled, double scaleX, double scaleY) {
        if (null != treeNode) {
            Object object = treeNode.getUserObject();
            if (object instanceof Model) {
                Model model = (Model) object;
                Properties properties = new Properties();
                properties.put(suffix_Type, suffix_Type_Model);
                properties.put("id", model.getID());
                properties.put("isVisible", isVisible);
                properties.put("isTranslational", isTranslational);
                properties.put("translationX", x);
                properties.put("translationY", y);
                properties.put("isRotational", isRotational);
                properties.put("rotationDegrees", angle);
                properties.put("isScaled", isScaled);
                properties.put("scaleX", scaleX);
                properties.put("scaleY", scaleY);
                getPropertiesList().add(properties);
            }
        }
    }

    private void reloadAnchor(Model model, Point anchor, double scaleX, double scaleY) {
        if (null != model) {
            double ax;
            double ay;
            if (null == anchor) {
                Point localAnchor = model.getLocalAnchor();
                if (null != localAnchor) {
                    ax = localAnchor.getX();
                    ay = localAnchor.getY();
                } else {
                    ax = model.getAnchor().getX();
                    ay = model.getAnchor().getY();
                }
                ax *= scaleX / model.getScale().getX();
                ay *= scaleY / model.getScale().getY();
                model.setAnchor(ax, ay);
            }
        }
    }

    public void updateTransform(DefaultMutableTreeNode root, Properties properties) {
        if (null != root && null != properties) {
            Object object = properties.get(suffix_Type);
            if (object instanceof String && suffix_Type_Model.equals(object)) {
                Object obj = properties.get("id");
                int id = -1;
                if (obj instanceof Number) {
                    id = (int) obj;
                } else if (obj instanceof String) {
                    id = Integer.parseInt((String) obj);
                }
                if (id != -1) {
                    DefaultMutableTreeNode treeNode = Skeletal.getTreeNode(root, id);
                    boolean isVisible = false;
                    obj = properties.get("isVisible");
                    if (obj instanceof Boolean) {
                        isVisible = (Boolean) obj;
                    } else if (obj instanceof String) {
                        isVisible = Boolean.parseBoolean((String) obj);
                    }
                    boolean isTranslational = false;
                    obj = properties.get("isTranslational");
                    if (obj instanceof Boolean) {
                        isTranslational = (Boolean) obj;
                    } else if (obj instanceof String) {
                        isTranslational = Boolean.parseBoolean((String) obj);
                    }
                    double x = 0;
                    obj = properties.get("translationX");
                    if (obj instanceof Number) {
                        x = (double) obj;
                    } else if (obj instanceof String) {
                        x = Double.parseDouble((String) obj);
                    }
                    double y = 0;
                    obj = properties.get("translationY");
                    if (obj instanceof Number) {
                        y = (double) obj;
                    } else if (obj instanceof String) {
                        y = Double.parseDouble((String) obj);
                    }
                    boolean isRotational = false;
                    obj = properties.get("isRotational");
                    if (obj instanceof Boolean) {
                        isRotational = (Boolean) obj;
                    } else if (obj instanceof String) {
                        isRotational = Boolean.parseBoolean((String) obj);
                    }
                    double angle = 0;
                    obj = properties.get("rotationDegrees");
                    if (obj instanceof Number) {
                        angle = (double) obj;
                    } else if (obj instanceof String) {
                        angle = Double.parseDouble((String) obj);
                    }
                    boolean isScaled = false;
                    obj = properties.get("isScaled");
                    if (obj instanceof Boolean) {
                        isScaled = (Boolean) obj;
                    } else if (obj instanceof String) {
                        isScaled = Boolean.parseBoolean((String) obj);
                    }
                    double scaleX = 0;
                    obj = properties.get("scaleX");
                    if (obj instanceof Number) {
                        scaleX = (double) obj;
                    } else if (obj instanceof String) {
                        scaleX = Double.parseDouble((String) obj);
                    }
                    double scaleY = 0;
                    obj = properties.get("scaleX");
                    if (obj instanceof Number) {
                        scaleY = (double) obj;
                    } else if (obj instanceof String) {
                        scaleY = Double.parseDouble((String) obj);
                    }
                    updateTransform(treeNode, isVisible, isTranslational, x, y, isRotational, angle, isScaled, scaleX, scaleY);
                }
            }
        }
    }

    public void updateTransform(DefaultMutableTreeNode treeNode, boolean isVisible, boolean isTranslational, double x, double y, boolean isRotational, double angle, boolean isScaled, double scaleX, double scaleY) {
        updateKeyframe(treeNode, isVisible, isTranslational, x, y, isRotational, angle, isScaled, scaleX, scaleY);
        updateTransform(treeNode, isVisible, isTranslational, x, y, isRotational, angle, null, isScaled, scaleX, scaleY);
    }

    private void updateTransform(DefaultMutableTreeNode treeNode, boolean isVisible, boolean isTranslational, double x, double y, boolean isRotational, double angle, Point anchor, boolean isScaled, double scaleX, double scaleY) {
        if (null != treeNode) {
            Object object = treeNode.getUserObject();
            Model model = null;
            double ax;
            double ay;
            if (object instanceof Model) {
                model = (Model) object;
                reloadAnchor(model, anchor, scaleX, scaleY);
                updateTransform(model, isVisible, isTranslational, x, y, isRotational, angle, isScaled, scaleX, scaleY);
            }
            if (treeNode.getChildCount() > 0) {
                for (Enumeration enumeration = treeNode.children(); enumeration.hasMoreElements(); ) {
                    Object obj = enumeration.nextElement();
                    if (obj instanceof DefaultMutableTreeNode) {
                        DefaultMutableTreeNode treeNode_Child = (DefaultMutableTreeNode) obj;
                        Object object_Child = treeNode_Child.getUserObject();
                        if (object_Child instanceof Model) {
                            if (null != model) {
                                Model model_Child = (Model) object_Child;
                                x = model.getTranslation().getX();
                                y = model.getTranslation().getY();
                                angle = model.getRotationDegrees();
                                ax = model.getAnchor().getX() + model.getGlobalTranslation().getX() - model_Child.getGlobalTranslation().getX();
                                ay = model.getAnchor().getY() + model.getGlobalTranslation().getY() - model_Child.getGlobalTranslation().getY();
                                model_Child.setAnchor(ax, ay);
                                anchor = model_Child.getAnchor();
//                                scaleX = model.getScale().getX();
//                                scaleY = model.getScale().getY();
                                scaleX = 1;
                                scaleY = 1;
                            }
                            updateTransform(treeNode_Child, isVisible, isTranslational, x, y, isRotational, angle, anchor, isScaled, scaleX, scaleY);
                        }
                    }
                }
            }
        }
    }

    private void updateTransform(Model model, boolean isVisible, boolean isTranslational, double x, double y, boolean isRotational, double angle, boolean isScaled, double scaleX, double scaleY) {
        if (null != model) {
            model.setVisible(isVisible);
            model.setTranslational(isTranslational);
            model.setRotational(isRotational);
            model.setScaled(isScaled);
            model.updateTransformDegrees(x, y, angle, scaleX, scaleY);
        }
    }
}