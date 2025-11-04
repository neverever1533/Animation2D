package cn.imaginary.toolkit.model;

import java.awt.Point;

import java.util.Properties;

public class Bone {
    private String name;

    public String type = "bone";

    private boolean isVisible;

    private Point point_Location;
    private Point point_Scaled;

    private double rotated_Degrees;
    private double rotated_Theta;

    public Bone() {
        setVisible(true);
    }

    public Point getLocation() {
        if (null == point_Location) {
            point_Location = new Point();
        }
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

    public double getRotation() {
        return rotated_Theta;
    }

    public void setRotation(double theta) {
        rotated_Theta = theta;
    }

    public double getRotationDegrees() {
        return rotated_Degrees;
    }

    public void setRotationDegrees(double angle) {
        rotated_Degrees = angle;
    }

    public Point getScaled() {
        if (null == point_Scaled) {
            point_Scaled = new Point(1, 1);
        }
        return point_Scaled;
    }

    public void setScaled(double scaledX, double scaledY) {
        Point point = new Point();
        point.setLocation(scaledX, scaledY);
        setScaled(point);
    }

    public void setScaled(Point point) {
        point_Scaled = point;
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
        properties.put("rotation", getRotation());
        properties.put("rotationDegrees", getRotationDegrees());
        properties.put("scaledX", getScaled().getX());
        properties.put("scaledY", getScaled().getY());
        properties.put("isVisible", isVisible());
        return properties;
    }
}
