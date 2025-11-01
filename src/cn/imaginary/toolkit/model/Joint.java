package cn.imaginary.toolkit.model;

import java.awt.Point;

import java.util.Properties;

public class Joint {
    private boolean isVisible;

    private Point point_Anchor;

    private String name;
    private String type = "joint";

    public Joint() {
        setVisible(true);
    }

    public Point getAnchor() {
        if (null == point_Anchor) {
            point_Anchor = new Point();
        }
        return point_Anchor;
    }

    public void setAnchor(double x, double y) {
        Point point = new Point();
        point.setLocation(x, y);
        setAnchor(point);
    }

    public void setAnchor(Point point) {
        point_Anchor = point;
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
        properties.put("anchorX", getAnchor().getX());
        properties.put("anchorY", getAnchor().getY());
        properties.put("isVisible", isVisible());
        return properties;
    }
}
