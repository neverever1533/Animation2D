# Animation2D
2D Skeletal animation with Keyframe for Java.

## 说明：
### 文件支持：
 * jpg,png,etc.
 * json,xml
 * sprite pack（待加入）
 * psd（待加入）
### GUI（待更新）

## 使用：
 * 流程：
   - (1) 读取图像资源
     * 1. apj（工程文件）
     * 2. ask+akf（骨骼和图帧）
     * 3. jpg/png/etc.（没有1或者2，就从读取图片开始）
       - 1). 读取图片；
       - 2). 生成骨骼；
       - 3). 绑定骨骼父子关系；
       - 4). 编辑骨骼初始位置，旋转角度和缩放倍数；
       - 5). 移动，旋转和缩放，以编辑骨骼姿势动作；
       - 6). 保存图帧。
    - (2) 保存工程或图帧文件

 * 步骤：
### 1.读取图片：
```java
String filePath0 = "body.png";
String filePath1 = "head.png";
...
File[] array = new File[5];
array[0] = new File(filePath0);
...
animation.newProject(512, 512);
animation.read(array[0]);
...
animation.read(array[6]);
```

### 2.创造“骨骼”：
 * 参数 （index对应图片顺序）：
```java
public DefaultMutableTreeNode getTreeNode(DefaultMutableTreeNode treeNode, int index) {}
```
 * 获取骨骼：
```java
DefaultMutableTreeNode root = animation.getRootTreeNode();
DefaultMutableTreeNode basic_body = animation.getTreeNode(root, 0);
...
DefaultMutableTreeNode basic_leg_down_r = animation.getTreeNode(root, 9);
```

### 3.定义父子关系：
 * 参数：
```java
public void updateTreeNode(DefaultMutableTreeNode from, DefaultMutableTreeNode to) {}
```

 * 将from节点作为child，移至to节点并将其作为parent：
```java
animation.updateTreeNode(basic_head, basic_body);
animation.updateTreeNode(basic_hand_down_l, basic_hand_up_l);
...
animation.updateTreeNode(basic_leg_up_r, basic_body);
```

### 4.为“皮肤”定义初始坐标、重力角度、旋转角度、旋转中心和缩放倍数
 * 参数：
```java
public void updateSkeletal(DefaultMutableTreeNode treeNode, int locationX, int locationY, double gravityDegrees, double rotationDegrees, double localAnchorX, double localAnchorY, double scaledX, double scaledY) {}
```

 * 骨骼定位：
```java
animation.updateSkeletal(basic_body, 224, 224, 0, 0, 32, 32, 1, 1);
animation.updateSkeletal(basic_head, 224, 176, 0, 0, 32, 60, 1, 1);
animation.updateSkeletal(basic_hand_up_l, 224, 228, 90, 0, 12, 16, 1, 1);
...
animation.updateSkeletal(basic_leg_down_r, 252, 308, 0, 0, 16, 12, 1, 1);
```

* 保存骨骼：
```java
animation.writeSkeletal(file);
```

#### 图示：
![image](https://github.com/neverever1533/Animation2D/blob/main/a2d_load.png)

### 5.处理动作，创造图帧：
 * 参数：
```java
public void updateTransform(DefaultMutableTreeNode treeNode, boolean isVisible, boolean isTranslated, int x, int y, boolean isRotated, double angle, boolean isScaled, double scaledX, double scaledY) {}
```

 * 重置骨骼初始状态：
 ```java
 public void resetTreeNode(DefaultMutableTreeNode treeNode) {}
 ```

 * 帧生成：沿用历史骨骼生成新动作前，使用resetTreeNode()恢复骨骼初始状态。

#### 示例：
 * 动作1（T-Pose)：
```java
animation.updateTransform(basic_hand_up_l, true, false, 0, 0, true, -180, false, 1, 1);
image = animation.updateGraphics2D();
animation.resetTreeNode(basic_hand_up_l);
```
或
```java
animation.updateTransform(basic_hand_up_l, true, false, 0, 0, true, -180, false, 1, 1);
image = animation.updateGraphics2D();
animation.updateTransform(basic_hand_up_l, true, false, 0, 0, true, 180, false, 1, 1);
animation.updateTransform(basic_hand_up_l, true, false, 0, 0, true, 0, false, 1, 1);
```

#### 图示：
![image](https://github.com/neverever1533/Animation2D/blob/main/a2d_T-pose.png)

* 动作2：
```java
animation.updateTransform(basic_body, true, false, 0, 0, true, -30, false, 1, 1);
animation.updateTransform(basic_head, true, false, 0, 0, true, 30, false, 1, 1);
...
animation.updateTransform(basic_leg_down_r, true, false, 0, 0, true, -20, false, 1, 1);
image = animation.updateGraphics2D();
```

 * 保存图帧：
```java
animation.writeKeyframe(file);
```

#### 图示：
![image](https://github.com/neverever1533/Animation2D/blob/main/a2d_c-pose.png)

### 6.获取渲染图：
 * 保存工程：
```java
animation.writeProject(file);
```

 * 保存图帧：
```java
BufferedImage image = animation.updateGraphics2D();
animation.writeImageFile(image, file);
```
或：
```java
animation.writeImageFile(file);
```
