package cn.imaginary.toolkit.animation;

import cn.imaginary.toolkit.Model;
import cn.imaginary.toolkit.model.Bone;
import cn.imaginary.toolkit.model.Joint;
import cn.imaginary.toolkit.model.Mesh;

import javax.imageio.ImageIO;

import javax.swing.tree.DefaultMutableTreeNode;

import java.awt.image.BufferedImage;

import java.io.File;
import java.io.IOException;

import java.util.ArrayList;
import java.util.Enumeration;

public class Skeletal {
    public Skeletal() {
    }

    public DefaultMutableTreeNode readSkeletal(String filePath, int index) {
        return readSkeletal(new File(filePath), index);
    }

    public DefaultMutableTreeNode readSkeletal(File file, int index) {
        return null;
    }

    public Model readImage(String filePath, int index) {
        return readImage(new File(filePath), index);
    }

    public Model readImage(File file, int index) {
        BufferedImage image;
        try {
            image = ImageIO.read(file);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        if (null == image) {
            return null;
        }

        Model model = new Model();
        model.setName(model.getType() + index);
        model.setID(index);

        Bone bone = new Bone();
        bone.setName(bone.getType() + index);
        model.setBone(bone);

        Joint joint = new Joint();
        joint.setName(joint.getType() + index);
        model.setJoint(joint);

        Mesh mesh = new Mesh();
        mesh.setName(mesh.getType() + index);
        mesh.setImagePath(file.getAbsolutePath());
        mesh.setSkin(image);
        model.setMesh(mesh);

        return model;
    }

    public void read(DefaultMutableTreeNode treeNode, Model model) {
        updateTreeNode(treeNode, model);
    }

    public DefaultMutableTreeNode getTreeNode(DefaultMutableTreeNode root, int id) {
        return getTreeNode(root, null, id);
    }

    public DefaultMutableTreeNode getTreeNode(DefaultMutableTreeNode root, String name) {
        return getTreeNode(root, name, -1);
    }

    private DefaultMutableTreeNode getTreeNode(DefaultMutableTreeNode root, String name, int id) {
        if (null != root && (null != name || id != -1)) {
            for (Enumeration enumeration = root.depthFirstEnumeration(); enumeration.hasMoreElements(); ) {
                Object object = enumeration.nextElement();
                if (object instanceof DefaultMutableTreeNode) {
                    DefaultMutableTreeNode node = (DefaultMutableTreeNode) object;
                    Object obj = node.getUserObject();
                    if (obj instanceof Model) {
                        if (id != -1 && id == ((Model) obj).getID()) {
                            return node;
                        } else {
                            if (name != null && name.equals(((Model) obj).getName())) {
                                return node;
                            }
                        }
                    } else if (obj instanceof Bone) {
                        if (name != null && name.equals(((Bone) obj).getName())) {
                            return node;
                        }
                    } else if (obj instanceof Joint) {
                        if (name != null && name.equals(((Joint) obj).getName())) {
                            return node;
                        }
                    } else if (obj instanceof Mesh) {
                        if (name != null && name.equals(((Mesh) obj).getName())) {
                            return node;
                        }
                    }
                }
            }
        }
        return null;
    }

    public void updateTreeNode(DefaultMutableTreeNode treeNode, Model model) {
        if (null != treeNode && null != model) {
            DefaultMutableTreeNode treeNode_Model = new DefaultMutableTreeNode(model);
            updateTreeNodeModel(treeNode_Model, model.getBone());
            updateTreeNodeModel(treeNode_Model, model.getJoint());
            updateTreeNodeModel(treeNode_Model, model.getMesh());
            treeNode.add(treeNode_Model);
        }
    }

    private void updateTreeNodeModel(DefaultMutableTreeNode treeNode, Object object) {
        if (null != treeNode && null != object) {
            DefaultMutableTreeNode node = new DefaultMutableTreeNode(object);
            node.setAllowsChildren(false);
            treeNode.add(node);
        }
    }

    public void updateTreeNode(DefaultMutableTreeNode treeNode, ArrayList<Model> arrayList) {
        if (null != arrayList) {
//            for (Iterator<Model> iterator = arrayList.iterator(); iterator.hasNext(); ) {
//                updateTreeNode(treeNode, iterator.next());
//            }
            for (int i = 0; i < arrayList.size(); i++) {
                updateTreeNode(treeNode, arrayList.get(i));
            }
        }
    }

    public void updateSkeletal(DefaultMutableTreeNode treeNode, int locationX, int locationY, double rotationDegrees, double localAnchorX, double localAnchorY, double scaledX, double scaledY) {
        if (null != treeNode) {
            Object object = treeNode.getUserObject();
            if (object instanceof Model) {
                Model model = (Model) object;
                updateSkeletal(model, locationX, locationY, rotationDegrees, localAnchorX, localAnchorY, scaledX, scaledY);
            }
        }
    }

    public void updateSkeletal(Model model, int locationX, int locationY, double rotationDegrees, double localAnchorX, double localAnchorY, double scaledX, double scaledY) {
        if (null != model) {
            model.setTranslated(true);
            model.setRotated(true);
            model.setScaled(true);
            model.setVisible(true);
            model.updateTransformDegrees(locationX, locationY, rotationDegrees, localAnchorX, localAnchorY, scaledX, scaledY);
            Joint joint = model.getJoint();
            joint.setLocalAnchor(localAnchorX, localAnchorY);
        }
    }

    public void updateRootSkeletal(DefaultMutableTreeNode root, int locationX, int locationY, double rotationDegrees, double localAnchorX, double localAnchorY, double scaledX, double scaledY) {
        if (null != root) {
            for (Enumeration enumeration = root.breadthFirstEnumeration(); enumeration.hasMoreElements(); ) {
                updateSkeletal((DefaultMutableTreeNode) enumeration.nextElement(), locationX, locationY, rotationDegrees, localAnchorX, localAnchorY, scaledX, scaledY);
            }
        }
    }

    public void updateTreeNode(DefaultMutableTreeNode from, DefaultMutableTreeNode to) {
        if (null != from && null != to) {
            if (from.getUserObject() instanceof Model && to.getUserObject() instanceof Model) {
                DefaultMutableTreeNode node = (DefaultMutableTreeNode) from.getParent();
                if (null != node) {
                    node.remove(from);
                    to.add(from);
                }
            }
        }
    }

    public void updateTreeNode(DefaultMutableTreeNode root, int from, int to) {
        updateTreeNode(getTreeNode(root, from), getTreeNode(root, to));
    }
}
