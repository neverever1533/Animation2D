package cn.imaginary.toolkit.model;

import java.awt.Graphics2D;
import java.awt.Point;

import java.util.Properties;

public class Bone {
    private String name;

    public String type = "bone";

    private boolean isVisible;

    private Point point_Location;
    private Point point_Scale;
    private Point point_Scale_Global;
    private Point point_Translation;
    private Point point_Translation_Global;

    private double rotation;
    private double rotation_Degrees;
    private double rotation_Global;
    private double rotation_Global_Degrees;
    private double rotation_Gravity_Degrees;

    private int length_draw;

    public Bone() {
        setVisible(true);
    }

    public Point getLocation() {
        return point_Location;
    }

    public void setLocation(double x, double y) {
        Point point = new Point();
        point.setLocation(x, y);
        setLocation(point);
    }

    public void setLocation(Point point) {
        this.point_Location = point;
    }

    public double getGlobalRotation() {
        return rotation_Global;
    }

    public void setGlobalRotation(double theta) {
        rotation_Global += theta;
    }

    public double getRotation() {
        return rotation;
    }

    public void setRotation(double theta) {
        rotation = theta;
    }

    public double getGravityRotationDegrees() {
        return rotation_Gravity_Degrees;
    }

    public void setGravityRotationDegrees(double angle) {
        rotation_Gravity_Degrees = angle;
    }

    public double getGlobalRotationDegrees() {
        return rotation_Global_Degrees;
    }

    public void setGlobalRotationDegrees(double angle) {
        rotation_Global_Degrees += angle;
    }

    public double getRotationDegrees() {
        return rotation_Degrees;
    }

    public void setRotationDegrees(double angle) {
        rotation_Degrees = angle;
    }

    public Point getGlobalScale() {
        if (null == point_Scale_Global) {
            point_Scale_Global = new Point(1, 1);
        }
        return point_Scale_Global;
    }

    public void setGlobalScale(double scaleX, double scaleY) {
        scaleX *= getGlobalScale().getX();
        scaleY *= getGlobalScale().getY();
        Point point = new Point();
        point.setLocation(scaleX, scaleY);
        setGlobalScale(point);
    }

    private void setGlobalScale(Point point) {
        point_Scale_Global = point;
    }

    public Point getScale() {
        if (null == point_Scale) {
            point_Scale = new Point(1, 1);
        }
        return point_Scale;
    }

    public void setScale(double scaleX, double scaleY) {
        Point point = new Point();
        point.setLocation(scaleX, scaleY);
        setScale(point);
    }

    private void setScale(Point point) {
        point_Scale = point;
    }

    public Point getGlobalTranslation() {
        if (null == point_Translation_Global) {
            point_Translation_Global = new Point();
        }
        return point_Translation_Global;
    }

    public void setGlobalTranslation(double x, double y) {
        x += getGlobalTranslation().getX();
        y += getGlobalTranslation().getY();
        Point point = new Point();
        point.setLocation(x, y);
        setGlobalTranslation(point);
    }

    private void setGlobalTranslation(Point point) {
        point_Translation_Global = point;
    }

    public Point getTranslation() {
        if (null == point_Translation) {
            point_Translation = new Point();
        }
        return point_Translation;
    }

    public void setTranslation(double x, double y) {
        Point point = new Point();
        point.setLocation(x, y);
        setTranslation(point);
    }

    private void setTranslation(Point point) {
        point_Translation = point;
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

    public int getDrawLength() {
        return length_draw;
    }

    public void setDrawLength(int length) {
        length_draw = length;
    }

    public void updateGraphics2D(Graphics2D g2d) {
        double angle_draw_bone = getGravityRotationDegrees() - 180;
        int len_draw = getDrawLength();
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
        properties.put("x", getLocation().getX());
        properties.put("y", getLocation().getY());
        properties.put("globalTranslationX", getGlobalTranslation().getX());
        properties.put("globalTranslationY", getGlobalTranslation().getY());
        properties.put("translationX", getTranslation().getX());
        properties.put("translationY", getTranslation().getY());
        properties.put("globalRotation", getGlobalRotation());
        properties.put("globalRotationDegrees", getGlobalRotationDegrees());
        properties.put("rotation", getRotation());
        properties.put("rotationDegrees", getRotationDegrees());
        properties.put("globalScaleX", getGlobalScale().getX());
        properties.put("globalScaleY", getGlobalScale().getY());
        properties.put("scaleX", getScale().getX());
        properties.put("scaleY", getScale().getY());
        properties.put("isVisible", isVisible());
        return properties;
    }
}
