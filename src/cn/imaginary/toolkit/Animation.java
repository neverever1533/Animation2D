package cn.imaginary.toolkit;

import cn.imaginary.toolkit.animation.Keyframe;
import cn.imaginary.toolkit.animation.Skeletal;
import cn.imaginary.toolkit.json.JsonObject;
import cn.imaginary.toolkit.model.Mesh;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.geom.AffineTransform;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import java.nio.file.Files;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Properties;

import javax.imageio.ImageIO;

import javax.swing.tree.DefaultMutableTreeNode;

public class Animation {
    private int height_Canvas = 512;
    private int width_Canvas = 512;
    private int index_Model;
    private int index_Frame;
    private int index_Selected_Frame;
    private int index_Selected_TreeNode;

    private DefaultMutableTreeNode treeNode_Root;
    private DefaultMutableTreeNode treeNode_Root_Project;

    private BufferedImage image_Root;

    private Keyframe keyframe = new Keyframe();

    private Skeletal skeletal = new Skeletal();

    private boolean is_View_Center;

    private String lineSeparator = System.lineSeparator();

    public static String suffix_Animation_Keyframe = ".akf";
    public static String suffix_Animation_Project = ".apj";
    public static String suffix_Animation_Skeletal = ".asl";

    public final String suffix_Animation = "Animation2D";
    public final String suffix_Type_Model = "model";
    public final String suffix_Type_Bone = "bone";
    public final String suffix_Type_Joint = "joint";
    public final String suffix_Type_Mesh = "mesh";

    private String suffix_Png = "png";
    private String suffix_Type = "type";
    private String suffix_Type_Keyframe = "keyframe";
    private String suffix_Type_Frame = "frame";
    private String suffix_Type_Object = "object";
    private String suffix_Type_Project = "project";
    private String suffix_Type_Properties = "Properties";
    private String suffix_Type_Skeletal = "skeletal";
    private String suffix_Type_TreeNode = "treeNode";
    private String suffix_Type_TreeNode_Child = "childNode";
    private String suffix_Type_TreeNode_Root = "rootNode";

    public Animation() {
    }

    public void setCanvas(int width, int height) {
        if (width > 0 && height > 0) {
            width_Canvas = width;
            height_Canvas = height;
        } else {
            width = width_Canvas;
            height = height_Canvas;
        }
    }

    public DefaultMutableTreeNode getProjectTreeNode() {
        return treeNode_Root_Project;
    }

    public void setProjectTreeNode(DefaultMutableTreeNode treeNode) {
        treeNode_Root_Project = treeNode;
    }

    public BufferedImage getRootImage() {
        return image_Root;
    }

    public void setRootImage(BufferedImage image) {
        image_Root = image;
    }

    public DefaultMutableTreeNode getRootTreeNode() {
        return treeNode_Root;
    }

    public void setRootTreeNode(DefaultMutableTreeNode treeNode) {
        treeNode_Root = treeNode;
    }

    public Graphics2D createGraphics(int width, int height) {
        return createImage(width, height).createGraphics();
    }

    public BufferedImage createImage(int width, int height) {
        return new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
    }

    public boolean isCenterView() {
        return is_View_Center;
    }

    public void setCenterView(boolean isCenterView) {
        is_View_Center = isCenterView;
    }

    public boolean isSupportImageFile(File file) {
        return isSupportImageFile(file.getName());
    }

    public boolean isSupportImageFile(String name) {
        if (null != name) {
            name = name.toLowerCase();
            String[] array_Suffix = ImageIO.getReaderFileSuffixes();
            for (int i = 0; i < array_Suffix.length; i++) {
                if (name.endsWith(array_Suffix[i])) {
                    return true;
                }
            }
        }
        return false;
    }

    public void newFrame() {
        DefaultMutableTreeNode treeNode_Project = new DefaultMutableTreeNode(suffix_Type_Project);
        setProjectTreeNode(treeNode_Project);
        newFrame(index_Frame++);
    }

    private void newFrame(int index) {
        setRootTreeNode(new DefaultMutableTreeNode(suffix_Type_Frame + index));
        getProjectTreeNode().add(getRootTreeNode());
    }

    public void newProject() {
        newProject(width_Canvas, height_Canvas);
    }

    public void newProject(Dimension dimension) {
        if (null != dimension) {
            newProject(dimension.width, dimension.height);
        }
    }

    public void newProject(int width, int height) {
        setCanvas(width, height);
        newFrame();
        setRootImage(createImage(width, height));
    }

    public int getSelectedFrameIndex() {
        return index_Selected_Frame;
    }

    public void setSelectedFrameIndex(int index) {
        index_Selected_Frame = index;
    }

    public DefaultMutableTreeNode getFrameTreeNode(int index) {
        DefaultMutableTreeNode treeNode_Root = null;
        DefaultMutableTreeNode treeNode_Project = getProjectTreeNode();
        if (null != treeNode_Project) {
            treeNode_Root = (DefaultMutableTreeNode) treeNode_Project.getChildAt(index);
        }
        return treeNode_Root;
    }

    public DefaultMutableTreeNode getSelectedFrameTreeNode() {
        return getFrameTreeNode(getSelectedFrameIndex());
    }

    public void selectedFrameTreeNode(int index) {
        setSelectedFrameIndex(index);
        setRootTreeNode(getSelectedFrameTreeNode());
    }

    public DefaultMutableTreeNode getTreeNode(int index) {
        return getTreeNode(getRootTreeNode(), index);
    }

    public DefaultMutableTreeNode getTreeNode(String name) {
        return getTreeNode(getRootTreeNode(), name);
    }

    public DefaultMutableTreeNode getTreeNode(DefaultMutableTreeNode treeNode, int index) {
        return skeletal.getTreeNode(treeNode, index);
    }

    public DefaultMutableTreeNode getTreeNode(DefaultMutableTreeNode treeNode, String name) {
        return skeletal.getTreeNode(treeNode, name);
    }

    public String getTreeNodeName(String type, int index) {
        switch (type) {
            case suffix_Type_Model:
            case suffix_Type_Bone:
            case suffix_Type_Joint:
            case suffix_Type_Mesh:
                return type + index;
            default:
                return null;
        }
    }

    public Properties getTreeNodeProperties(DefaultMutableTreeNode treeNode) {
        Properties properties = new Properties();
        skeletal.readTreeNode(treeNode, 0, properties);
        return properties;
    }

    public int getSelectedTreeNodeIndex() {
        return index_Selected_TreeNode;
    }

    public void setSelectedTreeNodeIndex(int index) {
        index_Selected_TreeNode = index;
    }

    public void read(ArrayList<Model> arrayList) {
        skeletal.updateTreeNode(getRootTreeNode(), arrayList);
    }

    public void read(File file) {
        String name = file.getName().toLowerCase();
        if (name.endsWith(suffix_Animation_Keyframe)) {
            readKeyFrame(file);
        } else if (name.endsWith(suffix_Animation_Project)) {
            readProject(file);
        } else if (name.endsWith(suffix_Animation_Skeletal)) {
            readSkeletal(file);
        } else if (isSupportImageFile(file)) {
            readImage(file);
        }
    }

    public void read(String filePath) {
        read(new File(filePath));
    }

    public void readKeyFrame(File file) {

    }

    public void readKeyFrame() {

    }

    public void readKeyFrame(DefaultMutableTreeNode treeNode) {

    }

    public Properties readFrame() {
        return readFrame(getSelectedFrameTreeNode());
    }

    public Properties readFrame(DefaultMutableTreeNode root) {
        Properties properties = new Properties();
        readFrame(root, getSelectedFrameIndex(), properties);
        return properties;
    }

    private void readFrame(DefaultMutableTreeNode treeNode, int index, Properties properties) {
        if (null != treeNode) {
            if (null == properties) {
                properties = new Properties();
            }
            properties.put(suffix_Type, suffix_Type_Frame);
            Properties prop = new Properties();
            skeletal.readTreeNode(treeNode, 0, prop);
            properties.put(suffix_Type_TreeNode_Root + index + suffix_Type_Properties, prop);
        }
    }

    public void readImage(File file) {
        Model model = skeletal.readImage(file, index_Model++);
        skeletal.updateTreeNode(getRootTreeNode(), model);
    }

    public void readImage(File[] array) {
        if (null != array) {
            File file;
            for (int i = 0; i < array.length; i++) {
                file = array[i];
                if (file.isFile()) {
                    readImage(file);
                } else {
                    readImage(file.listFiles());
                }
            }
        }
    }

    public Properties readJson(File file) {
        String string = readString(file);
        JsonUtils jsonUtils = new JsonUtils();
        JsonObject jsonObject = jsonUtils.parseJsonObject(string);
        return jsonObject.get();
    }

    public void readProject(File file) {
        Properties properties = readProperties(file);
    }

    public Properties readProject() {
        return readProject(getProjectTreeNode());
    }

    private Properties readProject(DefaultMutableTreeNode treeNode) {
        Properties properties = null;
        if (null != treeNode) {
            properties = new Properties();
            int index = 0;
            properties.put(suffix_Type, suffix_Type_Project);
            for (Enumeration enumeration = treeNode.children(); enumeration.hasMoreElements(); ) {
                Object object = enumeration.nextElement();
                if (object instanceof DefaultMutableTreeNode) {
                    Properties prop = new Properties();
                    readFrame((DefaultMutableTreeNode) object, index, prop);
                    properties.put(suffix_Type_Frame + (index++) + suffix_Type_Properties, prop);
                }
            }
        }
        return properties;
    }

    public Properties readProperties(File file) {
        return readJson(file);
//        return readXML(file);
    }

    public Properties readSkeletal() {
        return readSkeletal(getSelectedFrameTreeNode());
    }

    public Properties readSkeletal(DefaultMutableTreeNode root) {
        Properties properties = new Properties();
        readSkeletal(root, getSelectedFrameIndex(), properties);
        return properties;
    }

    private void readSkeletal(DefaultMutableTreeNode treeNode, int index, Properties properties) {
        if (null != treeNode) {
            if (null == properties) {
                properties = new Properties();
            }
            properties.put(suffix_Type, suffix_Type_Skeletal);
            Properties prop = new Properties();
            skeletal.readTreeNode(treeNode, 0, prop);
            properties.put(suffix_Type_TreeNode_Root + index + suffix_Type_Properties, prop);
        }
    }

    public void readSkeletal(File file) {
        Properties properties = readProperties(file);
    }

    private String readString(File file) {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String string;
            StringBuilder stringBuilder = new StringBuilder();
            while ((string = reader.readLine()) != null) {
                stringBuilder.append(string);
                stringBuilder.append(lineSeparator);
            }
            reader.close();
            string = stringBuilder.toString();
            return string;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public Properties readXML(File file) {
        Properties properties = new Properties();
        try {
            properties.loadFromXML(Files.newInputStream(file.toPath()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return properties;
    }

    public void write(String filePath) {
        write(new File(filePath));
    }

    public void write(File file) {
        String name = file.getName().toLowerCase();
        if (name.endsWith(suffix_Animation_Keyframe)) {
            writeFrame(file);
        } else if (name.endsWith(suffix_Animation_Project)) {
            writeProject(file);
        } else if (name.endsWith(suffix_Animation_Skeletal)) {
            writeSkeletal(file);
        } else if (isSupportImageFile(file)) {
            writeImage(file);
        }
    }

    public void writeImage(File file) {
        write(updateGraphics2D(), file);
    }

    public void write(BufferedImage image, File file) {
        try {
            ImageIO.write(image, suffix_Png, file);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void writeFrame(File file) {
        writeProperties(readFrame(), file);
    }

    public void writeProject(File file) {
        writeProperties(readProject(), file);
    }

    public void writeSkeletal(File file) {
        writeProperties(readSkeletal(), file);
    }

    public void writeProperties(Properties properties, File file) {
        System.out.println("properties: " + properties);
        writeJson(properties, file);
//        writeXML(properties, file);
    }

    public void writeJson(Properties properties, File file) {
        if (null != properties) {
            JsonObject jsonObject = new JsonObject();
            String info = JsonUtils.format(jsonObject.toString());
            System.out.println("json: " + info);
            writeString(info, file);
        }
    }

    private void writeString(String string, File file) {
        if (null != string) {
            try {
                BufferedWriter writer = new BufferedWriter(new FileWriter(file));
                writer.write(string);
                writer.flush();
                writer.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void writeXML(Properties properties, File file) {
        if (null != properties) {
            try {
                properties.storeToXML(Files.newOutputStream(file.toPath()), suffix_Animation, "utf-8");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public BufferedImage updateGraphics2D() {
        return updateGraphics2D(getRootTreeNode());
    }

    public BufferedImage updateGraphics2D(DefaultMutableTreeNode treeNode) {
//        BufferedImage image = createImage(image_Root.getWidth(), image_Root.getHeight());
        BufferedImage image = createImage(width_Canvas, height_Canvas);
        Graphics2D graphics2D = image.createGraphics();
        updateGraphics2D(graphics2D, null, treeNode);
        graphics2D.dispose();
        return image;
    }

    public void updateGraphics2D(Graphics2D graphics2D, Model model) {
        if (null != model) {
            if (model.isVisible()) {
                if (model.isBoneVisible()) {
                    model.getBone().updateGraphics2D(graphics2D);
                }
                if (model.isJointVisible()) {
                    model.getJoint().updateGraphics2D(graphics2D);
                }
                if (model.isMeshVisible()) {
                    AffineTransform transform = model.getTransform();
                    Mesh mesh = model.getMesh();
                    BufferedImage image = mesh.getSkin();
                    if (null != image) {
                        updateGraphics2D(graphics2D, image, transform);
                    } else {
                        updateGraphics2D(graphics2D, mesh.getText(), mesh.getTextFont(), mesh.getTextColor(), transform);
                    }
                }
            }
        }
    }

    public void updateGraphics2D(Graphics2D graphics2D, AffineTransform transform, DefaultMutableTreeNode treeNode) {
        if (null != treeNode) {
            Object object = treeNode.getUserObject();
            if (object instanceof Model) {
                updateGraphics2D(graphics2D, (Model) object);
            }
            if (treeNode.getChildCount() > 0) {
                for (Enumeration enumeration = treeNode.children(); enumeration.hasMoreElements(); ) {
                    Object obj = enumeration.nextElement();
                    if (null != obj) {
                        if (obj instanceof DefaultMutableTreeNode) {
                            updateGraphics2D(graphics2D, transform, (DefaultMutableTreeNode) obj);
                        }
                    }
                }
            }
        }
    }

    private void updateGraphics2D(Graphics2D graphics2D, BufferedImage image, int x, int y, int width, int height) {
        if (null != graphics2D && null != image) {
            graphics2D.drawImage(image, x, y, width, height, null);
        }
    }

    private void updateGraphics2D(Graphics2D graphics2D, BufferedImage image, AffineTransform transform) {
        if (null != graphics2D) {
            graphics2D.drawImage(image, transform, null);
        }
    }

    private void updateGraphics2D(Graphics2D graphics2D, String text, Font font, Color color, AffineTransform transform) {
        if (null != graphics2D) {
            if (null != text) {
                if (null != font) {
                    graphics2D.setFont(font);
                }
                if (null != color) {
                    graphics2D.setColor(color);
                }
                int x = 0;
                int y = 0;
                if (null != transform) {
                    graphics2D.setTransform(transform);
                    x = (int) transform.getTranslateX();
                    y = (int) transform.getTranslateY();
                }
                graphics2D.drawString(text, x, y);
            }
        }
    }

    public void resetTreeNode(DefaultMutableTreeNode treeNode) {
        skeletal.resetTreeNode(treeNode);
    }

    public void updateTreeNode(DefaultMutableTreeNode from, DefaultMutableTreeNode to) {
        skeletal.updateTreeNode(from, to);
    }

    public void updateTreeNode(DefaultMutableTreeNode treeNode, int from, int to) {
        skeletal.updateTreeNode(getRootTreeNode(), from, to);
    }

    public void updateTransform(DefaultMutableTreeNode treeNode, boolean isVisible, boolean isTranslational, double x, double y, boolean isRotational, double angle, boolean isScaled, double scaleX, double scaleY) {
        keyframe.updateTransform(treeNode, isVisible, isTranslational, x, y, isRotational, angle, isScaled, scaleX, scaleY);
    }

    public void updateSkeletal(DefaultMutableTreeNode treeNode, double translationX, double translationY, double gravityDegrees, double rotationDegrees, double anchorX, double anchorY, double scaleX, double scaleY) {
        skeletal.updateSkeletal(treeNode, translationX, translationY, gravityDegrees, rotationDegrees, anchorX, anchorY, scaleX, scaleY);
    }

    public void updateRootSkeletal(DefaultMutableTreeNode root, double translationX, double translationY, double gravityDegrees, double rotationDegrees, double anchorX, double anchorY, double scaleX, double scaleY) {
        skeletal.updateRootSkeletal(root, translationX, translationY, gravityDegrees, rotationDegrees, anchorX, anchorY, scaleX, scaleY);
    }
}