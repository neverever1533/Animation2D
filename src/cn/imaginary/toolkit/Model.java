package cn.imaginary.toolkit;

import cn.imaginary.toolkit.model.Bone;
import cn.imaginary.toolkit.model.Joint;
import cn.imaginary.toolkit.model.Mesh;

import java.awt.geom.AffineTransform;
import java.awt.Point;

import java.util.Properties;

public class Model {
    private String name;

    public String type = "model";

    private Bone bone;

    private Joint joint;

    private Mesh mesh;

    private int id;

    private boolean isGravity;
    private boolean isRotational;
    private boolean isScaled;
    private boolean isTranslational;
    private boolean isVisible;

    private double rotated_Degrees_State;

    public static double rotation_Gravity = 90;

    private AffineTransform transform;

    public Model() {
        setTranslational(true);
        setRotational(true);
        setScaled(true);
        setVisible(true);
        setGravity(false);
    }

    public Model(Bone bone, Joint joint, Mesh mesh) {
        new ModelAlpha();
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
    }

    public void rotateGravity(boolean isGravity) {
        double angle;
        if (isGravity) {
            rotated_Degrees_State = getGlobalRotationDegrees();
            rotateDegrees(-rotated_Degrees_State);
            angle = getGravityRotationDegrees();
        } else {
            rotateDegrees(-getGravityRotationDegrees());
            angle = rotated_Degrees_State;
        }
        rotateDegrees(angle);
    }

    public double getGravityRotationDegrees() {
        return getBone().getGravityRotationDegrees();
    }

    public void setGravityRotationDegrees(double angle) {
        getBone().setGravityRotationDegrees(angle);
    }

    public Point getAnchor() {
        return getJoint().getAnchor();
    }

    public void setAnchor(double ax, double ay) {
        getJoint().setAnchor(ax, ay);
    }

    public Point getGlobalAnchor() {
        return getJoint().getGlobalAnchor();
    }

    public void setGlobalAnchor(double ax, double ay) {
        getJoint().setGlobalAnchor(ax, ay);
    }

    public Point getLocalAnchor() {
        return getJoint().getLocalAnchor();
    }

    public void setLocalAnchor(double ax, double ay) {
        getJoint().setLocalAnchor(ax, ay);
    }

    public Point getLocation() {
        return getBone().getLocation();
    }

    public void setLocation(double ax, double ay) {
        getBone().setLocation(ax, ay);
    }

    public double getRotation() {
        return getBone().getRotation();
    }

    public void setRotation(double angle) {
        getBone().setRotation(angle);
    }

    public double getGlobalRotation() {
        return getBone().getGlobalRotation();
    }

    public void setGlobalRotation(double angle) {
        getBone().setGlobalRotation(angle);
    }

    public double getRotationDegrees() {
        return getBone().getRotationDegrees();
    }

    public void setRotationDegrees(double angle) {
        getBone().setRotationDegrees(angle);
    }

    public double getGlobalRotationDegrees() {
        return getBone().getGlobalRotationDegrees();
    }

    public void setGlobalRotationDegrees(double angle) {
        getBone().setGlobalRotationDegrees(angle);
    }

    public Point getTranslation() {
        return getBone().getTranslation();
    }

    public void setTranslation(double tx, double ty) {
        getBone().setTranslation(tx, ty);
    }

    public Point getGlobalTranslation() {
        return getBone().getGlobalTranslation();
    }

    public void setGlobalTranslation(double tx, double ty) {
        getBone().setGlobalTranslation(tx, ty);
    }

    public Point getScale() {
        return getBone().getScale();
    }

    public void setScale(double sx, double sy) {
        getBone().setScale(sx, sy);
    }

    public Point getGlobalScale() {
        return getBone().getGlobalScale();
    }

    public void setGlobalScale(double sx, double sy) {
        getBone().setGlobalScale(sx, sy);
    }

    public void rotateDegrees(double angle) {
        if (isRotational()) {
            angle %= 360;
            setRotationDegrees(angle);
            setGlobalRotationDegrees(angle);
            getTransform().rotate(Math.toRadians(angle), getJoint().getAnchor().getX(), getJoint().getAnchor().getY());
        }
    }

    public void rotateDegrees(double angle, double anchorX, double anchorY) {
        if (isRotational()) {
            setAnchor(anchorX, anchorY);
            setGlobalAnchor(anchorX + getGlobalTranslation().getX(), anchorY + getGlobalTranslation().getY());
            rotateDegrees(angle);
        }
    }

    public void rotate(double theta) {
        if (isRotational()) {
            setRotation(theta);
            setGlobalRotation(theta);
            getTransform().rotate(theta, getJoint().getAnchor().getX(), getJoint().getAnchor().getY());
        }
    }

    public void rotate(double theta, double anchorX, double anchorY) {
        if (isRotational()) {
            setAnchor(anchorX, anchorY);
            setGlobalAnchor(anchorX + getGlobalTranslation().getX(), anchorY + getGlobalTranslation().getY());
            rotate(theta);
        }
    }

    public void scale(double scaleX, double scaleY) {
        if (isScaled()) {
            setScale(scaleX, scaleY);
            setGlobalScale(scaleX, scaleY);
            getTransform().scale(scaleX, scaleY);
        }
    }

    public void translate(double x, double y) {
        if (isTranslational()) {
            setTranslation(x, y);
            setGlobalTranslation(x, y);
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

    public void updateTransform(double x, double y, double theta, double scaleX, double scaleY) {
        translate(x, y);
        rotate(theta);
        scale(scaleX, scaleY);
    }

    public void updateTransform(double x, double y, double theta, double anchorX, double anchorY, double scaleX, double scaleY) {
        translate(x, y);
        rotate(theta, anchorX, anchorY);
        scale(scaleX, scaleY);
    }

    public void updateTransformDegrees(double x, double y, double angle, double scaleX, double scaleY) {
        translate(x, y);
        rotateDegrees(angle);
        scale(scaleX, scaleY);
    }

    public void updateTransformDegrees(double x, double y, double angle, double anchorX, double anchorY, double scaleX, double scaleY) {
        translate(x, y);
        rotateDegrees(angle, anchorX, anchorY);
        scale(scaleX, scaleY);
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

    public boolean isRotational() {
        return isRotational;
    }

    public void setRotational(boolean isRotational) {
        this.isRotational = isRotational;
    }

    public boolean isScaled() {
        return isScaled;
    }

    public void setScaled(boolean isScaled) {
        this.isScaled = isScaled;
    }

    public boolean isTranslational() {
        return isTranslational;
    }

    public void setTranslational(boolean isTranslational) {
        this.isTranslational = isTranslational;
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
        properties.put("isRotational", isRotational());
        properties.put("isScaled", isScaled());
        properties.put("isTranslational", isTranslational());
        properties.put("isVisible", isVisible());
        return properties;
    }
}
