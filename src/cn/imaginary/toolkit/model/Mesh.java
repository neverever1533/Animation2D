package cn.imaginary.toolkit.model;

import java.awt.Color;
import java.awt.Font;
import java.awt.image.BufferedImage;

import java.util.Properties;

public class Mesh {
    private BufferedImage skin_Image;

    private Font text_Font;

    private Color text_Color;

    private String name;
    private String skin_Path;
    private String skin_Text;

    public static String type = "mesh";

    private boolean isVisible;

    public Mesh() {
        setVisible(true);
    }

    public String getSkinPath() {
        return skin_Path;
    }

    public void setSkinPath(String filePath) {
        skin_Path = filePath;
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

    public BufferedImage getImage() {
        return skin_Image;
    }

    public void setSkin(BufferedImage image) {
        skin_Image = image;
        if (null != image) {
            skin_Text = null;
            text_Color = null;
            text_Font = null;
        }
    }

    public void setSkin(String text) {
        skin_Text = text;
        if (null != text) {
            skin_Image = null;
        }
    }

    public void setSkin(String text, Font font, Color color) {
        setSkin(text);
        setTextFont(font);
        setTextColor(color);
    }

    public String getText() {
        return skin_Text;
    }

    public Color getTextColor() {
        return text_Color;
    }

    public void setTextColor(Color color) {
        if (null != color) {
            text_Color = color;
        }
    }

    public void setTextColor(int red, int green, int blue) {
        setTextColor(new Color(checkColor(red), checkColor(green), checkColor(blue)));
    }

    private int checkColor(int item) {
        if (item < 0) {
            item = 0;
        }
        return item & 0xFF;
    }

    public Font getTextFont() {
        return text_Font;
    }

    public void setTextFont(Font font) {
        if (null != font) {
            text_Font = font;
        }
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
        String path = getSkinPath();
        if (null != path) {
            properties.put("path", path);
        }
        BufferedImage image = getImage();
        if (null != image) {
            properties.put("image", image);
        }
        String text = getText();
        if (null != text) {
            properties.put("text", text);
        }
        Color color = getTextColor();
        if (null != color) {
            properties.put("color", color);
            properties.put("colorRed", color.getRed());
            properties.put("colorGreen", color.getGreen());
            properties.put("colorBlue", color.getBlue());
        }
        Font font = getTextFont();
        if (null != font) {
            properties.put("font", font);
            properties.put("fontName", font.getName());
        }
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

                object = properties.get("path");
                if (object instanceof String) {
                    setSkinPath((String) object);
                }

                object = properties.get("image");
                if (object instanceof BufferedImage) {
                    setSkin((BufferedImage) object);
                }

                object = properties.get("text");
                if (object instanceof String) {
                    setSkin((String) object);

                    object = properties.get("color");
                    if (object instanceof Color) {
                        setTextColor((Color) object);
                    } else if (object instanceof String) {
                        setTextColor(Color.getColor((String) object));
                    } else {
                        Object object_cr = properties.get("colorRed");
                        Object object_cg = properties.get("colorGreen");
                        Object object_cb = properties.get("colorBlue");
                        if (null != object_cr || null != object_cg || null != object_cb) {
                            Color color = getTextColor();
                            if (null == color) {
                                color = new Color(0, 0, 0);
                                setTextColor(color);
                            }
                        }
                        if (object_cr instanceof Integer) {
                            setTextColor((int) object_cr, getTextColor().getGreen(), getTextColor().getBlue());
                        } else if (object_cr instanceof String) {
                            setTextColor(Integer.getInteger((String) object_cr), getTextColor().getGreen(), getTextColor().getBlue());
                        }
                        if (object_cg instanceof Integer) {
                            setTextColor(getTextColor().getRed(), (int) object_cg, getTextColor().getBlue());
                        } else if (object_cg instanceof String) {
                            setTextColor(getTextColor().getRed(), Integer.getInteger((String) object_cg), getTextColor().getBlue());
                        }
                        if (object_cb instanceof Integer) {
                            setTextColor(getTextColor().getRed(), getTextColor().getGreen(), (int) object_cb);
                        } else if (object_cb instanceof String) {
                            setTextColor(getTextColor().getRed(), getTextColor().getGreen(), Integer.getInteger((String) object_cb));
                        }
                    }

                    object = properties.get("font");
                    if (object instanceof Font) {
                        setTextFont((Font) object);
                    } else if (object instanceof String) {
                        setTextFont(Font.getFont((String) object));
                    } else {
                        object = properties.get("fontName");
                        if (object instanceof String) {
                            setTextFont(Font.getFont((String) object));
                        }
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
