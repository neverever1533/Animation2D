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
    private String type = "mesh";

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
    }

    public void setSkin(String text) {
        this.text = text;
        if (null != text) {
            BufferedImage image = null;
            setSkin(image);
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
        }
        Font font = getTextFont();
        if (null != font) {
            properties.put("font", font);
        }
        properties.put("isVisible", isVisible());
        return properties;
    }
}
