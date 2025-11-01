package cn.imaginary.toolkit;

import cn.imaginary.toolkit.model.Bone;
import cn.imaginary.toolkit.model.Joint;
import cn.imaginary.toolkit.model.Mesh;

import java.awt.Point;
import java.awt.geom.AffineTransform;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Properties;

public class Model {
    private ArrayList<Model> model_Children;

    private Model model_Parent;

    private String name;
    private String type = "model";

    private Bone bone;

    private Joint joint;

    private Mesh mesh;

    private boolean isGravity;
    private boolean isRotated;
    private boolean isScaled;
    private boolean isTranslated;
    private boolean isVisible;

    private double rotated_Degrees_State;
    private double rotated_Gravity = 90;
    private double rotated_Theta_State;
    private double x_Scaled_State = 1;
    private double y_Scaled_State = 1;

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

    public Model getParent() {
        return model_Parent;
    }

    public void setParent(Model model) {
        model_Parent = model;
    }

    public void addChild(Model model) {
        if (null != model) {
            model.setParent(this);
            if (null == model_Children) {
                model_Children = new ArrayList<>();
            }
            model_Children.add(model);
        }
    }

    public ArrayList<Model> getChildren() {
        return model_Children;
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

    public double getGravityRotated() {
        return rotated_Gravity;
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
            angle = getGravityRotated();
        } else {
            angle = rotated_Degrees_State;
        }
        rotateDegrees(angle);
    }

    public void rotateDegrees(double angle) {
        if (angle != rotated_Degrees_State) {
            rotated_Degrees_State = angle;
            Bone bone = getBone();
            bone.setRotationDegrees(bone.getRotationDegrees() + angle);
            rotate(Math.toRadians(angle));
        }
    }

    public void rotateDegrees(double angle, double anchorX, double anchorY) {
        if (angle != rotated_Degrees_State) {
            rotated_Degrees_State = angle;
            Bone bone = getBone();
            bone.setRotationDegrees(bone.getRotationDegrees() + angle);
            rotate(Math.toRadians(angle), anchorX, anchorY);
        }
    }

    public void rotate(double theta) {
        if (theta != rotated_Theta_State) {
            rotated_Theta_State = theta;
            Bone bone = getBone();
            bone.setRotation(bone.getRotation() + theta);
            getTransform().rotate(theta);
        }
    }

    public void rotate(double theta, double anchorX, double anchorY) {
        if (theta != rotated_Theta_State) {
            rotated_Theta_State = theta;
            Bone bone = getBone();
            bone.setRotation(bone.getRotation() + theta);
            Joint joint = getJoint();
            joint.setAnchor(anchorX, anchorY);
            getTransform().rotate(theta, anchorX, anchorY);
        }
    }

    public void scale(double scaledX, double scaledY) {
        if (scaledX != x_Scaled_State || scaledY != y_Scaled_State) {
            if (scaledX != x_Scaled_State) {
                x_Scaled_State = scaledX;
            }
            if (scaledY != y_Scaled_State) {
                y_Scaled_State = scaledY;
            }
            getBone().setScaled(scaledX, scaledY);
            getTransform().scale(scaledX, scaledY);
        }
    }

    public void translate(double x, double y) {
        Bone bone = getBone();
        Point location = bone.getLocation();
        bone.setLocation(x + location.getX(), y + location.getY());
        getTransform().translate(x, y);
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

    public void updateTransform(double x, double y, double theta, double anchorX, double anchorY, double scaledX, double scaledY) {
        if (isTranslated) {
            translate(x, y);
        }
        if (isRotated) {
            getJoint().setAnchor(anchorX, anchorY);
            rotate(theta, anchorX, anchorY);
//            rotate(theta);
        }
        if (isScaled) {
            scale(scaledX, scaledY);
        }
        if (null != model_Children) {
            for (Iterator<Model> iterator = model_Children.iterator(); iterator.hasNext(); ) {
                Model model = iterator.next();
                if (null != model) {
                    model.updateTransform(x, y, theta, anchorX, anchorY, scaledX, scaledY);
                }
            }
        }
    }

    public void updateTransformDegrees(double x, double y, double angle, double anchorX, double anchorY, double scaleX, double scaleY) {
        updateTransform(x, y, Math.toRadians(angle), anchorX, anchorY, scaleX, scaleY);
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
        properties.put("name", getName());
        properties.put("isGravit", isGravity());
        properties.put("isRotated", isRotated());
        properties.put("isScaled", isScaled());
        properties.put("isTranslated", isTranslated());
        properties.put("isVisible", isVisible());
        return properties;
    }
}
