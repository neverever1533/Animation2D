package cn.imaginary.toolkit;

import cn.imaginary.toolkit.animation.Keyframe;
import cn.imaginary.toolkit.animation.Skeletal;
import cn.imaginary.toolkit.json.JsonObject;
import cn.imaginary.toolkit.model.Bone;
import cn.imaginary.toolkit.model.Joint;
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
import java.util.Iterator;
import java.util.Properties;
import java.util.Set;

import javax.imageio.ImageIO;

import javax.swing.tree.DefaultMutableTreeNode;

public class Animation {
    public static String suffix_Animation_Keyframe = ".akf";
    public static String suffix_Animation_Project = ".apj";
    public static String suffix_Animation_Skeletal = ".ask";

    public final String suffix_Animation = "Animation2D";
    public final String suffix_Type_Model = "model";
    public final String suffix_Type_Bone = "bone";
    public final String suffix_Type_Joint = "joint";
    public final String suffix_Type_Mesh = "mesh";

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
    private String suffix_Png = "png";
    private String suffix_Type = "type";
    private String suffix_Type_ID = "id";
    private String suffix_Type_Keyframe = "keyframe";
    private String suffix_Type_Frame = "frame";
    private String suffix_Type_Project = "project";
    private String suffix_Type_Properties = "Properties";
    private String suffix_Type_Skeletal = "skeletal";

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
        int index = index_Frame++;
        setProjectTreeNode(new DefaultMutableTreeNode(suffix_Type_Project + index));
        setRootTreeNode(new DefaultMutableTreeNode(suffix_Type_Frame + index));
        getProjectTreeNode().add(getRootTreeNode());
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
        return Skeletal.getTreeNode(treeNode, index);
    }

    public DefaultMutableTreeNode getTreeNode(DefaultMutableTreeNode treeNode, String name) {
        return Skeletal.getTreeNode(treeNode, name);
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
//        System.out.println("name:" + name);
        if (name.endsWith(suffix_Animation_Keyframe)) {
            readKeyframe(file);
        } else if (name.endsWith(suffix_Animation_Project)) {
            readProject(file);
        } else if (name.endsWith(suffix_Animation_Skeletal)) {
            readSkeletal(file);
        } else if (isSupportImageFile(file)) {
            readImageFile(file);
        }
    }

    public void read(String filePath) {
        read(new File(filePath));
    }

    public void readKeyframe(File file) {
        readKeyframe(readProperties(file));
    }

    private void readKeyframe(Properties properties) {
        keyframe.updateKeyframe(getRootTreeNode(), properties);
    }

    private Properties readKeyframe() {
        return readKeyframe(keyframe.getPropertiesList());
    }

    private Properties readKeyframe(ArrayList<Properties> arrayList) {
        Properties properties = new Properties();
        properties.put(suffix_Type, suffix_Type_Keyframe);
        readPropertiesList(arrayList, properties);
        return properties;
    }

    public BufferedImage readImage(String filePath) {
        return readImage(new File(filePath));
    }

    public BufferedImage readImage(File file) {
        try {
            return ImageIO.read(file);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void readImageFile(File file) {
        readImageFile(file, index_Model++);
    }

    public void readImageFile(File file, int index) {
        Model model = skeletal.readModel(file, index);
        skeletal.readModel(getRootTreeNode(), model);
    }

    public void readImageFile(File[] array) {
        if (null != array) {
            File file;
            for (int i = 0; i < array.length; i++) {
                file = array[i];
                if (file.isFile()) {
                    readImageFile(file);
                } else {
                    readImageFile(file.listFiles());
                }
            }
        }
    }

    public Properties readJson(File file) {
        String string = readString(file);
        JsonUtils jsonUtils = new JsonUtils();
        JsonObject jsonObject = jsonUtils.parseJsonObject(string);
        return toProperties(jsonObject);
    }

    private Properties toProperties(JsonObject jsonObject) {
        Properties properties = null;
        if (null != jsonObject) {
            properties = new Properties();
            Properties prop = jsonObject.get();
            Set<Object> kset = prop.keySet();
            for (Iterator<Object> iterator = kset.iterator(); iterator.hasNext(); ) {
                Object key = iterator.next();
                Object value = prop.get(key);
                if (value instanceof JsonObject) {
                    properties.put(key.toString(), toProperties((JsonObject) value));
                } else if (value instanceof Properties) {
                    properties.put(key.toString(), value);
                } else {
                    properties.put(key.toString(), value.toString());
                }
            }
        }
        return properties;
    }

    public void readProject(File file) {
        newProject();
        Properties properties = readProperties(file);
        Object object = properties.get(suffix_Type);
        if (object instanceof String && suffix_Type_Project.equals(object)) {
            Object obj_sk = properties.get(suffix_Type_Skeletal + suffix_Type_Properties);
            Object obj_kf = properties.get(suffix_Type_Keyframe + suffix_Type_Properties);
            if (obj_sk instanceof Properties) {
                readSkeletal((Properties) obj_sk);
            }
            if (obj_kf instanceof Properties) {
                readKeyframe((Properties) obj_kf);
            }
        }
    }

    private Properties readProject() {
        Properties properties = new Properties();
        properties.put(suffix_Type, suffix_Type_Project);
        Properties prop_Skeletal = readSkeletal();
        properties.put(suffix_Type_Skeletal + suffix_Type_Properties, prop_Skeletal);
        Properties prop_KeyFrame = readKeyframe();
        properties.put(suffix_Type_Keyframe + suffix_Type_Properties, prop_KeyFrame);
        return properties;
    }

    private Properties readProperties(File file) {
        return readJson(file);
//        return readXML(file);
    }

    public void readSkeletal(File file) {
        newProject();
        readSkeletal(readProperties(file));
    }

    private void readSkeletal(Properties properties) {
        DefaultMutableTreeNode root = skeletal.updateSkeletal(properties);
        if (null != root) {
            setRootTreeNode(root);
        }
    }

    private Properties readSkeletal() {
        return readSkeletal(skeletal.getPropertiesList());
    }

    private Properties readSkeletal(ArrayList<Properties> arrayList) {
        Properties properties = new Properties();
        properties.put(suffix_Type, suffix_Type_Skeletal);
        readPropertiesList(arrayList, properties);
        return properties;
    }

    private void readPropertiesList(ArrayList<Properties> arrayList, Properties properties) {
        if (null != arrayList && null != properties) {
            for (Iterator<Properties> iterator = arrayList.iterator(); iterator.hasNext(); ) {
                Properties prop = iterator.next();
                Object object = prop.get(suffix_Type_ID);
                if (object instanceof Number) {
                    properties.put(suffix_Type_Model + object + suffix_Type_Properties, prop);
                }
            }
        }
    }

    private String readString(String filePath) {
        return readString(new File(filePath));
    }

    private String readString(File file) {
        if (isSupportImageFile(file)) {
            return null;
        }
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
            writeKeyframe(file);
        } else if (name.endsWith(suffix_Animation_Project)) {
            writeProject(file);
        } else if (name.endsWith(suffix_Animation_Skeletal)) {
            writeSkeletal(file);
        } else if (isSupportImageFile(file)) {
            writeImageFile(file);
        }
    }

    public void writeImageFile(File file) {
        writeImageFile(updateGraphics2D(), file);
    }

    public void writeImageFile(BufferedImage image, File file) {
        writeImage(image, file);
    }

    private void writeImage(BufferedImage image, File file) {
        try {
            ImageIO.write(image, suffix_Png, file);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void writeKeyframe(File file) {
        writeProperties(readKeyframe(), file);
    }

    public void writeProject(File file) {
        writeProperties(readProject(), file);
    }

    public void writeSkeletal(File file) {
        writeProperties(readSkeletal(), file);
    }

    private void writeProperties(Properties properties, File file) {
//        System.out.println("properties: " + properties);
        writeJson(properties, file);
//        writeXML(properties, file);
    }

    public void writeJson(Properties properties, File file) {
        if (null != properties) {
            JsonObject jsonObject = toJsonObject(properties);
            String info = jsonObject.toString();
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

    private JsonObject toJsonObject(Properties properties) {
        JsonObject jsonObject = null;
        if (null != properties) {
            jsonObject = new JsonObject();
            Set<Object> kset = properties.keySet();
            for (Iterator<Object> iterator = kset.iterator(); iterator.hasNext(); ) {
                Object key = iterator.next();
                Object value = properties.get(key);
                if (value instanceof Properties) {
                    jsonObject.add(key.toString(), toJsonObject((Properties) value));
                } else {
                    jsonObject.add(key.toString(), value);
                }
            }
        }
        return jsonObject;
    }

    public BufferedImage updateGraphics2D() {
        return updateGraphics2D(getRootTreeNode());
    }

    public BufferedImage updateGraphics2D(DefaultMutableTreeNode root) {
        BufferedImage image = createImage(width_Canvas, height_Canvas);
        Graphics2D graphics2D = image.createGraphics();
        updateGraphics2D(graphics2D, null, root);
        graphics2D.dispose();
        return image;
    }

    private void updateGraphics2D(Graphics2D graphics2D, Model model) {
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
                    String skinPath = mesh.getSkinPath();
                    BufferedImage image = mesh.getImage();
                    if (null == image) {
                        if (null != skinPath) {
                            image = readImage(skinPath);
                        }
                    }
                    if (null != image) {
                        updateGraphics2D(graphics2D, image, transform);
                    } else {
                        String text = mesh.getText();
                        if (null == text) {
                            if (null != skinPath) {
                                text = readString(skinPath);
                            }
                        }
                        updateGraphics2D(graphics2D, text, mesh.getTextFont(), mesh.getTextColor(), transform);
                    }
                }
            }
        }
    }

    private void updateGraphics2D(Graphics2D graphics2D, AffineTransform transform, DefaultMutableTreeNode treeNode) {
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

    private void updateGraphics2D(Graphics2D graphics2D, String text, Font font, Color color, AffineTransform
            transform) {
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

    public void updateKeyframe(DefaultMutableTreeNode treeNode, boolean isVisible, boolean isTranslational,
                               double x, double y, boolean isRotational, double angle, boolean isScaled, double scaleX, double scaleY) {
        keyframe.updateTransform(treeNode, isVisible, isTranslational, x, y, isRotational, angle, isScaled, scaleX, scaleY);
    }

    public void updateSkeletal(DefaultMutableTreeNode treeNode, double translationX, double translationY,
                               double gravityDegrees, double rotationDegrees, double anchorX, double anchorY, double scaleX, double scaleY) {
        skeletal.updateSkeletal(treeNode, translationX, translationY, gravityDegrees, rotationDegrees, anchorX, anchorY, scaleX, scaleY);
    }

    public void updateRootSkeletal(DefaultMutableTreeNode root, double translationX, double translationY,
                                   double gravityDegrees, double rotationDegrees, double anchorX, double anchorY, double scaleX, double scaleY) {
        skeletal.updateRootSkeletal(root, translationX, translationY, gravityDegrees, rotationDegrees, anchorX, anchorY, scaleX, scaleY);
    }
}