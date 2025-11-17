package cn.imaginary.toolkit.model;

import java.awt.Graphics2D;
import java.awt.Point;

import java.util.Properties;

public class Bone {
    private String name;

    public static String type = "bone";

    private boolean isVisible;

    private Point point_Location;
    private Point point_Scale;
    private Point point_Scale_Global;
    private Point point_Scale_Local;
    private Point point_Translation;
    private Point point_Translation_Global;
    private Point point_Translation_Local;

    private double rotation_Degrees;
    private double rotation_Global_Degrees;
    private double rotation_Gravity_Degrees;
    private double rotation_Local_Degrees;

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

    public double getLocalRotationDegrees() {
        return rotation_Local_Degrees;
    }

    public void setLocalRotationDegrees(double angle) {
        rotation_Local_Degrees = angle;
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

    public void setGlobalScale(Point point) {
        point_Scale_Global = point;
    }

    public Point getLocalScale() {
        return point_Scale_Local;
    }

    public void setLocalScale(double scaleX, double scaleY) {
        Point point = new Point();
        point.setLocation(scaleX, scaleY);
        setLocalScale(point);
    }

    public void setLocalScale(Point point) {
        point_Scale_Local = point;
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

    public void setScale(Point point) {
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

    public void setGlobalTranslation(Point point) {
        point_Translation_Global = point;
    }

    public Point getLocalTranslation() {
        return point_Translation_Local;
    }

    public void setLocalTranslation(double x, double y) {
        Point point = new Point();
        point.setLocation(x, y);
        setLocalTranslation(point);
    }

    public void setLocalTranslation(Point point) {
        point_Translation_Local = point;
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

    public void setTranslation(Point point) {
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
        Point location = getLocation();
        if (null != location) {
            properties.put("x", location.getX());
            properties.put("y", location.getY());
        }
        properties.put("globalTranslationX", getGlobalTranslation().getX());
        properties.put("globalTranslationY", getGlobalTranslation().getY());
        Point localTranslation = getLocalTranslation();
        if (null != localTranslation) {
            properties.put("localTranslationX", localTranslation.getX());
            properties.put("locallTranslationY", localTranslation.getY());
        }
        properties.put("translationX", getTranslation().getX());
        properties.put("translationY", getTranslation().getY());
        properties.put("globalRotationDegrees", getGlobalRotationDegrees());
        properties.put("localRotationDegrees", getLocalRotationDegrees());
        properties.put("rotationDegrees", getRotationDegrees());
        properties.put("globalScaleX", getGlobalScale().getX());
        properties.put("globalScaleY", getGlobalScale().getY());
        Point localScale = getLocalScale();
        if (null != localScale) {
            properties.put("localScaleX", localScale.getX());
            properties.put("localScaleY", localScale.getY());
        }
        properties.put("scaleX", getScale().getX());
        properties.put("scaleY", getScale().getY());
        properties.put("isVisible", isVisible());
        return properties;
    }

    public void setProperties(Properties properties) {
        if (null != properties) {
            Object object = properties.get("type");
            if (object instanceof String && getType().equals(object)) {
                object = properties.get("name");
                if (object instanceof String) {
                    setName((String) object);
                }

                Point location = getLocation();
                if (null == location) {
                    location = new Point();
                }
                object = properties.get("x");
                if (object instanceof Number) {
                    setLocation((double) object, location.getY());
                } else if (object instanceof String) {
                    setLocation(Double.parseDouble((String) object), location.getY());
                }
                object = properties.get("y");
                if (object instanceof Number) {
                    setLocation(location.getX(), (double) object);
                } else if (object instanceof String) {
                    setLocation(location.getX(), Double.parseDouble((String) object));
                }

                object = properties.get("globalTranslationX");
                if (object instanceof Number) {
                    setGlobalTranslation((double) object, getGlobalTranslation().getY());
                } else if (object instanceof String) {
                    setGlobalTranslation(Double.parseDouble((String) object), getGlobalTranslation().getY());
                }
                object = properties.get("globalTranslationY");
                if (object instanceof Number) {
                    setGlobalTranslation(getGlobalTranslation().getX(), (double) object);
                } else if (object instanceof String) {
                    setGlobalTranslation(getGlobalTranslation().getX(), Double.parseDouble((String) object));
                }
                Object object_ltx = properties.get("localTranslationX");
                Object object_lty = properties.get("localTranslationY");
                if (null != object_ltx || null != object_lty) {
                    Point localTranslation = getLocalTranslation();
                    if (null == localTranslation) {
                        localTranslation = new Point();
                        setLocalTranslation(localTranslation);
                    }
                    if (object_ltx instanceof Number) {
                        setLocalTranslation((double) object_ltx, getLocalTranslation().getY());
                    } else if (object_ltx instanceof String) {
                        setLocalTranslation(Double.parseDouble((String) object_ltx), getLocalTranslation().getY());
                    }
                    if (object_lty instanceof Number) {
                        setLocalTranslation(getLocalTranslation().getX(), (double) object_lty);
                    } else if (object_lty instanceof String) {
                        setLocalTranslation(getLocalTranslation().getX(), Double.parseDouble((String) object_lty));
                    }
                }
                object = properties.get("translationX");
                if (object instanceof Number) {
                    setTranslation((double) object, getTranslation().getY());
                } else if (object instanceof String) {
                    setTranslation(Double.parseDouble((String) object), getTranslation().getY());
                }
                object = properties.get("translationY");
                if (object instanceof Number) {
                    setTranslation(getTranslation().getX(), (double) object);
                } else if (object instanceof String) {
                    setTranslation(getTranslation().getX(), Double.parseDouble((String) object));
                }

                object = properties.get("globalRotationDegrees");
                if (object instanceof Number) {
                    setGlobalRotationDegrees((double) object);
                } else if (object instanceof String) {
                    setGlobalRotationDegrees(Double.parseDouble((String) object));
                }
                object = properties.get("localRotationDegrees");
                if (object instanceof Number) {
                    setLocalRotationDegrees((double) object);
                } else if (object instanceof String) {
                    setLocalRotationDegrees(Double.parseDouble((String) object));
                }
                object = properties.get("rotationDegrees");
                if (object instanceof Number) {
                    setRotationDegrees((double) object);
                } else if (object instanceof String) {
                    setRotationDegrees(Double.parseDouble((String) object));
                }

                object = properties.get("globalScaleX");
                if (object instanceof Number) {
                    setGlobalScale((double) object, getGlobalScale().getY());
                } else if (object instanceof String) {
                    setGlobalScale(Double.parseDouble((String) object), getGlobalScale().getY());
                }
                object = properties.get("globalScaleY");
                if (object instanceof Number) {
                    setGlobalScale(getGlobalScale().getX(), (double) object);
                } else if (object instanceof String) {
                    setGlobalScale(getGlobalScale().getX(), Double.parseDouble((String) object));
                }
                Object object_lsx = properties.get("localScaleX");
                Object object_lsy = properties.get("localScaleY");
                if (null != object_lsx || null != object_lsy) {
                    Point localScale = getLocalScale();
                    if (null == localScale) {
                        localScale = new Point();
                        setLocalScale(localScale);
                    }
                    if (object_lsx instanceof Number) {
                        setLocalScale((double) object_lsx, getLocalScale().getY());
                    } else if (object_lsx instanceof String) {
                        setLocalScale(Double.parseDouble((String) object_lsx), getLocalScale().getY());
                    }
                    if (object_lsy instanceof Number) {
                        setLocalScale(getLocalScale().getX(), (double) object_lsy);
                    } else if (object_lsy instanceof String) {
                        setLocalScale(getLocalScale().getX(), Double.parseDouble((String) object_lsy));
                    }
                }
                object = properties.get("scaleX");
                if (object instanceof Number) {
                    setScale((double) object, getScale().getY());
                } else if (object instanceof String) {
                    setScale(Double.parseDouble((String) object), getScale().getY());
                }
                object = properties.get("scaleY");
                if (object instanceof Number) {
                    setScale(getScale().getX(), (double) object);
                } else if (object instanceof String) {
                    setScale(getScale().getX(), Double.parseDouble((String) object));
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
