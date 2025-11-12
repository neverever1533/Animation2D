package cn.imaginary.toolkit.animation;

import cn.imaginary.toolkit.Model;

import java.awt.Point;

import java.util.Enumeration;

import javax.swing.tree.DefaultMutableTreeNode;

public class Keyframe {
    public Keyframe() {
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

    public void updateTransform(DefaultMutableTreeNode treeNode, boolean isVisible, boolean isTranslational, double x, double y, boolean isRotational, double angle, boolean isScaled, double scaleX, double scaleY) {
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