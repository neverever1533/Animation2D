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

    public static String type = "joint";

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
        Point localAnchor = getLocalAnchor();
        if (null != localAnchor) {
            properties.put("localAnchorX", localAnchor.getX());
            properties.put("localAnchorY", localAnchor.getY());
        }
        properties.put("isVisible", isVisible());
        return properties;
    }

    public void setProperties(Properties properties) {
        if (null != properties) {
            Object object = properties.get("type");
            if (object instanceof String && getType().equals(object)) {
                properties.put("name", getName());
                if (object instanceof String) {
                    setName((String) object);
                }

                object = properties.get("anchorX");
                if (object instanceof Number) {
                    setAnchor((double) object, getAnchor().getY());
                } else if (object instanceof String) {
                    setAnchor(Double.parseDouble((String) object), getAnchor().getY());
                }
                object = properties.get("anchorY");
                if (object instanceof Number) {
                    setAnchor(getAnchor().getX(), (double) object);
                } else if (object instanceof String) {
                    setAnchor(getAnchor().getX(), Double.parseDouble((String) object));
                }

                object = properties.get("globalAnchorX");
                if (object instanceof Number) {
                    setGlobalAnchor((double) object, getGlobalAnchor().getY());
                } else if (object instanceof String) {
                    setGlobalAnchor(Double.parseDouble((String) object), getGlobalAnchor().getY());
                }
                object = properties.get("globalAnchorY");
                if (object instanceof Number) {
                    setGlobalAnchor(getGlobalAnchor().getX(), (double) object);
                } else if (object instanceof String) {
                    setGlobalAnchor(getGlobalAnchor().getX(), Double.parseDouble((String) object));
                }

                Object object_lax = properties.get("localAnchorX");
                Object object_lay = properties.get("localAnchorY");
                if (null != object_lax || null != object_lay) {
                    Point localAnchor = getLocalAnchor();
                    if (null == localAnchor) {
                        localAnchor = new Point();
                        setLocalAnchor(localAnchor);
                    }
                    if (object_lax instanceof Number) {
                        setLocalAnchor((double) object_lax, getLocalAnchor().getY());
                    } else if (object_lax instanceof String) {
                        setLocalAnchor(Double.parseDouble((String) object_lax), getLocalAnchor().getY());
                    }
                    if (object_lay instanceof Number) {
                        setLocalAnchor(getLocalAnchor().getX(), (double) object_lay);
                    } else if (object_lay instanceof String) {
                        setLocalAnchor(getLocalAnchor().getX(), Double.parseDouble((String) object_lay));
                    }
                }

                object = properties.get("isVisible");
                if (object instanceof Boolean) {
                    setVisible((Boolean) object);
                } else if (object instanceof String) {
                    setVisible(Boolean.parseBoolean((String) object));
                }
            }
        }
    }
}
