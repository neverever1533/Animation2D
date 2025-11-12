# Animation2D
2D Skeletal animation with Keyframe for Java.

## 使用：
### 1.读取图片：
```java
String filePath0 = "body.png";
String filePath1 = "head.png";
...
File[] array = new File[5];
array[0] = new File(filePath0);
...
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
 * 将from节点作为child，移至to节点并将其作为parent。
```java
animation.updateTreeNode(basic_head, basic_body);
animation.updateTreeNode(basic_hand_down_l, basic_hand_up_l);
...
animation.updateTreeNode(basic_leg_up_r, basic_body);
```

### 4.为“皮肤”定义初始坐标、旋转角度和缩放倍数
 * 参数：
```java
public void updateSkeletal(DefaultMutableTreeNode treeNode, int locationX, int locationY, double rotationDegrees, double localAnchorX, double localAnchorY, double scaledX, double scaledY) {}
```
 * 骨骼定位：
```java
animation.updateSkeletal(basic_body, 224, 224, 0, 32, 32, 1, 1);
animation.updateSkeletal(basic_head, 224, 176, 0, 32, 60, 1, 1);
...
animation.updateSkeletal(basic_leg_down_r, 252, 308, 0, 16, 12, 1, 1);
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
 * 旋转顺序：1.祖；2.父；3.子；4.孙
 * 帧生成：程序记录最终状态，即每个骨骼可不间断旋转，子继承父移动、旋转、缩放参数，反续移动、旋转和缩放导致出错后可用resetTreeNode()恢复。
#### 示例：
 * 动作1（T-Pose)：
```java
animation.updateTransform(basic_hand_up_l, true, false, 0, 0, true, -180, false, 1, 1);
animation.resetTreeNode(basic_hand_up_l);
```
#### 图示：
![image](https://github.com/neverever1533/Animation2D/blob/main/a2d_T-pose.png)

* 动作2：
```java
animation.updateTransform(basic_body, true, false, 0, 0, true, -30, false, 1, 1);
animation.updateTransform(basic_head, true, false, 0, 0, true, 30, false, 1, 1);
...
animation.updateTransform(basic_leg_down_r, true, false, 0, 0, true, -20, false, 1, 1);
```
#### 图示：
![image](https://github.com/neverever1533/Animation2D/blob/main/a2d_c-pose.png)

### 6.获取渲染图：
```java
image = animation.updateGraphics2D();
```
