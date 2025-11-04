# Animation2D
2D Skeletal animation with Keyframe for Java.

## 代码进度：
### 加载图片资源：
```java
String filePath0 = "body.png";
String filePath1 = "head.png";
...
animation.read(new File(filePath1));
...
animation.read(new File(filePath9));
```
### 创造“骨骼”：

```java
DefaultMutableTreeNode root = animation.getRootTreeNode();
DefaultMutableTreeNode basic_body = animation.getTreeNode(0);
...
DefaultMutableTreeNode basic_leg_down_r = animation.getTreeNode(9);
```

### 定义父子关系：
Skeletal.java
```java
skeletal.updateTreeNode(basic_head, basic_body);
skeletal.updateTreeNode(basic_hand_down_l, basic_hand_up_l);
...
skeletal.updateTreeNode(basic_leg_up_r, basic_body);
```

### 为“皮肤”定义初始坐标、旋转角度和缩放倍数
```java
public void updateSkeletal(DefaultMutableTreeNode treeNode, int locationX, int locationY, double rotationDegrees, double localAnchorX, double localAnchorY, double scaledX, double scaledY) {}

skeletal.updateSkeletal(basic_body, 224, 224, 0, 32, 32, 1, 1);
skeletal.updateSkeletal(basic_head, 224, 176, 0, 32, 60, 1, 1);
...
skeletal.updateSkeletal(basic_leg_down_r, 252, 308, 0, 16, 12, 1, 1);
```
#### 图示：
![image](https://github.com/neverever1533/Animation2D/blob/main/a2d_load.png)

### 处理动作，创造图帧：
* 动作1（T-Pose)：
```java
public void updateTransform(DefaultMutableTreeNode treeNode, boolean isVisible, boolean isTranslated, int x, int y, boolean isRotated, double angle, boolean isScaled, double scaledX, double scaledY) {}

keyframe.updateTransform(basic_hand_up_l, true, false, 0, 0, true, -180, false, 1, 1);
```
#### 图示：
![image](https://github.com/neverever1533/Animation2D/blob/main/a2d_T-pose.png)

* 动作2：
```java
keyframe.updateTransform(basic_body, true, false, 0, 0, true, -30, false, 1, 1);
keyframe.updateTransform(basic_head, true, false, 0, 0, true, 30, false, 1, 1);
...
keyframe.updateTransform(basic_leg_down_r, true, false, 0, 0, true, -20, false, 1, 1);
```
#### 图示：
![image](https://github.com/neverever1533/Animation2D/blob/main/a2d_c-pose.png)

### 获取渲染图：
```java
image = animation.updateGraphics2D();
```
