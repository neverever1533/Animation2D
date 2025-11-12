package cn.imaginary.toolkit.model;

import java.awt.Color;
import java.awt.Font;
import java.awt.image.BufferedImage;

import java.util.Properties;

public class Mesh {
    private BufferedImage image_Skin;

    private Font text_Font;

    private Color text_Color;

    private String image_Path;
    private String name;
    private String text;

    public static String type = "mesh";

    private boolean isVisible;

    public Mesh() {
        setVisible(true);
    }

    public String getImagePath() {
        return image_Path;
    }

    public void setImagePath(String filePath) {
        image_Path = filePath;
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

    public BufferedImage getSkin() {
        return image_Skin;
    }

    public void setSkin(BufferedImage image) {
        image_Skin = image;
        if (null != image) {
            text = null;
            text_Color = null;
            text_Font = null;
        }
    }

    public void setSkin(String text) {
        this.text = text;
        if (null != text) {
            image_Skin = null;
        }
    }

    public void setSkin(String text, Font font, Color color) {
        setSkin(text);
        setTextFont(font);
        setTextColor(color);
    }

    public String getText() {
        return text;
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
        setTextColor(new Color(red, green, blue));
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
        String path = getImagePath();
        if (null != path) {
            properties.put("path", path);
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
        }
        properties.put("isVisible", isVisible());
        return properties;
    }

    public void setProperties(Properties properties) {
        if (null != properties) {
            Object object = properties.get("type");
            if (object instanceof String && ((String) object).equalsIgnoreCase(getType())) {
                object = properties.get("name");
                if (object instanceof String) {
                    setName((String) object);
                }

                object = properties.get("path");
                if (object instanceof String) {
                    setImagePath((String) object);
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
                        setTextColor(new Color(0, 0, 0));
                        object = properties.get("colorRed");
                        if (object instanceof Integer) {
                            setTextColor((int) object, getTextColor().getGreen(), getTextColor().getBlue());
                        } else if (object instanceof String) {
                            setTextColor(Integer.getInteger((String) object), getTextColor().getGreen(), getTextColor().getBlue());
                        }
                        object = properties.get("colorGreen");
                        if (object instanceof Integer) {
                            setTextColor(getTextColor().getRed(), (int) object, getTextColor().getBlue());
                        } else if (object instanceof String) {
                            setTextColor(getTextColor().getRed(), Integer.getInteger((String) object), getTextColor().getBlue());
                        }
                        object = properties.get("colorBlue");
                        if (object instanceof Integer) {
                            setTextColor(getTextColor().getRed(), getTextColor().getGreen(), (int) object);
                        } else if (object instanceof String) {
                            setTextColor(getTextColor().getRed(), getTextColor().getGreen(), Integer.getInteger((String) object));
                        }
                    }

                    object = properties.get("font");
                    if (object instanceof Font) {
                        setTextFont((Font) object);
                    } else if (object instanceof String) {
                        setTextFont(Font.getFont((String) object));
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
