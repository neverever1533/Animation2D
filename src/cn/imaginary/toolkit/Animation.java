package cn.imaginary.toolkit;

import cn.imaginary.toolkit.animation.Skeletal;
import cn.imaginary.toolkit.animation.Keyframe;
import cn.imaginary.toolkit.model.Bone;
import cn.imaginary.toolkit.model.Joint;
import cn.imaginary.toolkit.model.Mesh;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.geom.AffineTransform;

import java.io.File;
import java.io.IOException;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Properties;
import java.util.Set;

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

    public static String suffix_Animation_Keyframe = ".akf";
    public static String suffix_Animation_Project = ".apj";
    public static String suffix_Animation_Skeletal = ".asl";

    public final String suffix_Type_Model = "model";
    public final String suffix_Type_Bone = "bone";
    public final String suffix_Type_Joint = "joint";
    public final String suffix_Type_Mesh = "mesh";

    private String suffix_Png = "png";
    private String suffix_Type = "type";
    private String suffix_Type_Frame = "frame";
    private String suffix_Type_Project = "project";
    private String suffix_Type_Properties = "Properties";

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
        return skeletal.getTreeNode(getRootTreeNode(), index);
    }

    public DefaultMutableTreeNode getTreeNode(String name) {
        return skeletal.getTreeNode(getRootTreeNode(), name);
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

    public void readImage(File file) {
        skeletal.updateTreeNode(getRootTreeNode(), skeletal.readImage(file, index_Model++));
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

    public void readProject(File file) {
    }

    public void readSkeletal(File file) {
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

    public void write(BufferedImage image, File file) {
        try {
            ImageIO.write(image, suffix_Png, file);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void writeImage(File file) {
        write(updateGraphics2D(), file);
    }

    public void writeFrame(File file) {
    }

    private void writeFrame(DefaultMutableTreeNode treeNode, int index, Properties properties) {
    }

    public void writeProject(File file) {
    }

    private void writeProject(DefaultMutableTreeNode treeNode, Properties properties) {
    }

    public void writeSkeletal(File file) {
    }

    public void writeProperties(Properties properties, File file) {
//        System.out.println("properties: " + properties);
//        writeJson(properties, file);
    }

    /*public void writeJson(Properties properties, File file) {
        if (null != properties) {
            JsonObject jsonObject = new JsonObject();
//            System.out.println("json: " + jsonObject.toString(properties));
            toJsonObject(jsonObject, properties);
            System.out.println("json: " + jsonObject.toString());
        }
    }

    private void toJsonObject(JsonObject jsonObject, Properties properties) {
        if (null != properties) {
            Set<Object> kset = properties.keySet();
            for (Iterator<Object> iterator = kset.iterator(); iterator.hasNext(); ) {
                Object key = iterator.next();
                Object value = properties.get(key);
                if (value instanceof Properties) {
                    JsonObject json = new JsonObject();
                    toJsonObject(json, (Properties) value);
                    jsonObject.add(key.toString(), json);
                } else {
                    jsonObject.add(key.toString(), value);
                }
            }
        }
    }*/

    public void writeTreeNode(DefaultMutableTreeNode treeNode, File file) {
    }

    public void writeModel(File file) {
    }

    public void viewCenter(Model model) {
        if (null != model) {
            if (null != image_Root) {
                double x = 0;
                double y = 0;
                double angle = 0;
                double ax = 0;
                double ay = 0;
                BufferedImage image = model.getMesh().getSkin();
                if (null != image) {
                    x = (double) (image_Root.getWidth() - image.getWidth()) / 2;
                    y = (double) (image_Root.getHeight() - image.getHeight()) / 2;
                    ax = (double) image.getWidth() / 2;
                }
                double sx = 1;
                double sy = 1;
                model.updateTransformDegrees(x, y, angle, ax, ay, sx, sy);
//            System.out.println("readImage x: " + x + "/y: " + y + "/angle: " + angle + "/ax: " + ax + "/ay: " + ay + "/sx: " + sx + "/sy: " + sy);
            }
        }
    }

    public BufferedImage updateGraphics2D() {
        BufferedImage image = createImage(width_Canvas, height_Canvas);
        Graphics2D graphics2D = image.createGraphics();
        updateGraphics2D(graphics2D, null, getRootTreeNode());
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

    public void updateTreeNode(DefaultMutableTreeNode from, DefaultMutableTreeNode to) {
        skeletal.updateTreeNode(from, to);
    }

    public void updateTreeNode(DefaultMutableTreeNode treeNode, int from, int to) {
        skeletal.updateTreeNode(getRootTreeNode(), from, to);
    }

    public void updateTransform(DefaultMutableTreeNode treeNode, boolean isVisible, boolean isTranslated, int x, int y, boolean isRotated, double angle, boolean isScaled, double scaledX, double scaledY) {
        keyframe.updateTransform(treeNode, isVisible, isTranslated, x, y, isRotated, angle, isScaled, scaledX, scaledY);
    }

    public void updateSkeletal(DefaultMutableTreeNode treeNode, int locationX, int locationY, double rotationDegrees, double localAnchorX, double localAnchorY, double scaledX, double scaledY) {
        skeletal.updateSkeletal(treeNode, locationX, locationY, rotationDegrees, localAnchorX, localAnchorY, scaledX, scaledY);
    }

    public void updateRootSkeletal(DefaultMutableTreeNode root, int locationX, int locationY, double rotationDegrees, double localAnchorX, double localAnchorY, double scaledX, double scaledY) {
        skeletal.updateRootSkeletal(root, locationX, locationY, rotationDegrees, localAnchorX, localAnchorY, scaledX, scaledY);
    }
}