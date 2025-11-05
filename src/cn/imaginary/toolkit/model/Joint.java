package cn.imaginary.toolkit.model;

import java.awt.Graphics2D;
import java.awt.Point;

import java.util.Properties;

public class Joint {
    private boolean isVisible;

    private Point point_Anchor;
    private Point point_Anchor_Local;
    private Point point_Anchor_Global;

    private String name;

    public String type = "joint";

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

    public Point getGlobalAnchor() {
        if (null == point_Anchor_Global) {
            point_Anchor_Global = new Point();
        }
        return point_Anchor_Global;
    }

    public void setGlobalAnchor(double x, double y) {
        Point point = new Point();
        point.setLocation(x, y);
        setGlobalAnchor(point);
    }

    public void setGlobalAnchor(Point point) {
        point_Anchor_Global = point;
    }

    public Point getLocalAnchor() {
        return point_Anchor_Local;
    }

    public void setLocalAnchor(double x, double y) {
        Point point = new Point();
        point.setLocation(x, y);
        setLocalAnchor(point);
    }

    public void setLocalAnchor(Point point) {
        point_Anchor_Local = point;
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

    public void updateGraphics2D(Graphics2D g2d) {
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
        properties.put("globalAnchorX", getGlobalAnchor().getX());
        properties.put("globalAnchorY", getGlobalAnchor().getY());
        properties.put("localAnchorX", getLocalAnchor().getX());
        properties.put("localAnchorY", getLocalAnchor().getY());
        properties.put("isVisible", isVisible());
        return properties;
    }
}
