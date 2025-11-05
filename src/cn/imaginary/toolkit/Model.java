package cn.imaginary.toolkit;

import cn.imaginary.toolkit.model.Bone;
import cn.imaginary.toolkit.model.Joint;
import cn.imaginary.toolkit.model.Mesh;

import java.awt.geom.AffineTransform;

import java.util.Properties;

public class Model {
    private String name;

    public String type = "model";

    private Bone bone;

    private Joint joint;

    private Mesh mesh;

    private int id;

    private boolean isGravity;
    private boolean isRotated;
    private boolean isScaled;
    private boolean isTranslated;
    private boolean isVisible;

    private double rotated_Degrees_State;

    public static double rotated_Gravity = 90;

    private AffineTransform transform;

    public Model() {
        setTranslated(true);
        setRotated(true);
        setScaled(true);
        setVisible(true);
        setGravity(false);
    }

    public Model(Bone bone, Joint joint, Mesh mesh) {
        new Model();
        setModel(bone, joint, mesh);
    }

    public Bone getBone() {
        if (null == bone) {
            bone = new Bone();
        }
        return bone;
    }

    public void setBone(Bone bone) {
        this.bone = bone;
    }

    public Joint getJoint() {
        if (null == joint) {
            joint = new Joint();
        }
        return joint;
    }

    public void setJoint(Joint joint) {
        this.joint = joint;
    }

    public Mesh getMesh() {
        if (null == mesh) {
            mesh = new Mesh();
        }
        return mesh;
    }

    public void setMesh(Mesh mesh) {
        this.mesh = mesh;
    }

    public void setModel(Bone bone, Joint joint, Mesh mesh) {
        setBone(bone);
        setJoint(joint);
        setMesh(mesh);
    }

    public int getID() {
        return id;
    }

    public void setID(int id) {
        this.id = id;
    }

    public boolean isGravity() {
        return isGravity;
    }

    public void setGravity(boolean isGravity) {
        this.isGravity = isGravity;
        double angle;
        if (isGravity) {
            Bone bone = getBone();
            rotated_Degrees_State = bone.getRotationDegrees();
            angle = bone.getGravityRotationDegrees();
            //           angle = rotated_Gravity;
        } else {
            angle = rotated_Degrees_State;
        }
        rotateDegrees(angle);
    }

    public void rotateDegrees(double angle) {
        if (isRotated()) {
            angle %= 360;
            Bone bone = getBone();
            bone.setRotationDegrees(bone.getRotationDegrees() + angle);
            rotate(Math.toRadians(angle));
        }
    }

    public void rotateDegrees(double angle, double anchorX, double anchorY) {
        if (isRotated()) {
            angle %= 360;
            Bone bone = getBone();
            bone.setRotationDegrees(bone.getRotationDegrees() + angle);
            rotate(Math.toRadians(angle), anchorX, anchorY);
        }
    }

    public void rotate(double theta) {
        rotate(theta, getJoint().getAnchor().getX(), getJoint().getAnchor().getY());
    }

    public void rotate(double theta, double anchorX, double anchorY) {
        if (isRotated()) {
            Bone bone = getBone();
            bone.setRotation(bone.getRotation() + theta);
            Joint joint = getJoint();
            joint.setAnchor(anchorX, anchorY);
            joint.setGlobalAnchor(anchorX + bone.getLocation().getX(), anchorY + bone.getLocation().getY());
            getTransform().rotate(theta, anchorX, anchorY);
        }
    }

    public void scale(double scaledX, double scaledY) {
        if (isScaled()) {
            getBone().setScaled(scaledX, scaledY);
            getTransform().scale(scaledX, scaledY);
        }
    }

    public void translate(double x, double y) {
        if (isTranslated()) {
            Bone bone = getBone();
            bone.setLocation(x + bone.getLocation().getX(), y + bone.getLocation().getY());
            getTransform().translate(x, y);
        }
    }

    public String getName() {
        if (null == name) {
            name = getType();
        }
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void updateTransform(double x, double y, double theta, double scaledX, double scaledY) {
        translate(x, y);
        rotate(theta);
        scale(scaledX, scaledY);
    }

    public void updateTransform(double x, double y, double theta, double anchorX, double anchorY, double scaledX, double scaledY) {
        translate(x, y);
        rotate(theta, anchorX, anchorY);
        scale(scaledX, scaledY);
    }

    public void updateTransformDegrees(double x, double y, double angle, double scaledX, double scaledY) {
        updateTransform(x, y, Math.toRadians(angle), scaledX, scaledY);
    }

    public void updateTransformDegrees(double x, double y, double angle, double anchorX, double anchorY, double scaledX, double scaledY) {
        translate(x, y);
        rotateDegrees(angle, anchorX, anchorY);
        scale(scaledX, scaledY);
    }

    public AffineTransform getTransform() {
        if (null == transform) {
            transform = new AffineTransform();
        }
        return transform;
    }

    public void setTransform(AffineTransform transform) {
        this.transform = transform;
    }

    public void updateTransform(AffineTransform transform) {
        if (null == transform) {
            transform = new AffineTransform();
        }
        getTransform().concatenate(transform);
    }

    public boolean isRotated() {
        return isRotated;
    }

    public void setRotated(boolean isRotated) {
        this.isRotated = isRotated;
    }

    public boolean isScaled() {
        return isScaled;
    }

    public void setScaled(boolean isScaled) {
        this.isScaled = isScaled;
    }

    public boolean isTranslated() {
        return isTranslated;
    }

    public void setTranslated(boolean isTranslated) {
        this.isTranslated = isTranslated;
    }

    public boolean isBoneVisible() {
        return getBone().isVisible();
    }

    public void setBoneVisible(boolean isVisible) {
        getBone().setVisible(isVisible);
    }

    public boolean isJointVisible() {
        return getJoint().isVisible();
    }

    public void setJointVisible(boolean isVisible) {
        getJoint().setVisible(isVisible);
    }

    public boolean isMeshVisible() {
        return getMesh().isVisible();
    }

    public void setMeshVisible(boolean isVisible) {
        getMesh().setVisible(isVisible);
    }

    public boolean isVisible() {
        return isVisible;
    }

    public void setVisible(boolean isVisible) {
        this.isVisible = isVisible;
    }

    public String getType() {
        return type;
    }

    public Properties getProperties() {
        Properties properties = new Properties();
        properties.put("type", getType());
        properties.put("id", getID());
        properties.put("name", getName());
        properties.put("isGravity", isGravity());
        properties.put("isRotated", isRotated());
        properties.put("isScaled", isScaled());
        properties.put("isTranslated", isTranslated());
        properties.put("isVisible", isVisible());
        return properties;
    }
}
