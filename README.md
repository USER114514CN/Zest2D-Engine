# Zest2D-Engine
一个基于Java Swing的2D轻量游戏引擎，天生支持跨平台，不过性能不算非常高

## 使用示例
***提示：在主类的static代码块第一行加入启用OpenGL或Direct3D性能更优哦~***
```java
// 使用OpenGL或Direct3D加速
if (System.getProperty("os.name").toLowerCase().contains("win")) {
    System.setProperty("sun.java2d.d3d", "true");
} else {
    System.setProperty("sun.java2d.opengl", "true");
}
```
```java
package com.example.zest2dexample;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Random;

import javax.swing.JFrame;

import com.user114514.zest2d.core.Base2DGame;
import com.user114514.zest2d.core.BaseGame2DObject;
import com.user114514.zest2d.core.GameCamera;
import com.user114514.zest2d.core.GameContext;
import com.user114514.zest2d.guis.ButtonGUI;
import com.user114514.zest2d.guis.LabelGUI;
import com.user114514.zest2d.objects.MovablePlayer;
import com.user114514.zest2d.objects.RectGameObject;
import com.user114514.zest2d.util.BindedObjectLocationCamera;
import com.user114514.zest2d.util.HighPerformanceStringFormatter;

public class GameApp extends Base2DGame {
    private MovablePlayer playerController;
    private LabelGUI infoLabel;
    private BaseGame2DObject player;
    private LabelGUI posLabel;
    HighPerformanceStringFormatter formatter;
    private LabelGUI debugLabel;
    private ButtonGUI exitButton;

    // FPS 统计变量
    private int frameCount = 0;
    private double fpsTimer = 0;
    private int currentFPS = 0;
    private HighPerformanceStringFormatter dbgFormatter;
    private BindedObjectLocationCamera camera;

    static {
        if (System.getProperty("os.name").toLowerCase().contains("win")) {
            System.setProperty("sun.java2d.d3d", "true");
        } else {
            System.setProperty("sun.java2d.opengl", "true");
        }
    }

    public GameApp() {
        super(0);
    }

    @Override
    public void init(GameContext context) {
        setBackground(Color.BLACK);


        // 玩家
        player = new RectGameObject(
                context,
                100, 100, 50, 50,
                Color.RED,
                true,
                new BasicStroke(2));
        player.setZ(100); // 确保玩家在其他对象之上
        context.addObject(player);

        camera = new BindedObjectLocationCamera(context, player);
        context.setCamera(camera);

        Random rand = new Random();

        for (int index = 0; index < 100; index++) {
            context.addObject(new RectGameObject(
                    context,
                    rand.nextInt(1000) - 500, rand.nextInt(1000) - 500, 50, 50,
                    Color.GRAY,
                    true,
                    new BasicStroke(2)));
        }

        // 移动控制
        playerController = new MovablePlayer(context, player, 300);
        MovablePlayer.PlayerKeyControlBindInfo keyBind = MovablePlayer.PlayerKeyControlBindInfo.Builder.create()
                .bindMovingUpKey(KeyEvent.VK_UP)
                .bindMovingDownKey(KeyEvent.VK_DOWN)
                .bindMovingLeftKey(KeyEvent.VK_LEFT)
                .bindMovingRightKey(KeyEvent.VK_RIGHT)
                .build();
        playerController.bindControlKey(keyBind);

        // 信息文本
        infoLabel = new LabelGUI(
                context,
                10, 10,
                "上下左右 移动 | FPS: 计算中...",
                new Font(Font.MONOSPACED, Font.PLAIN, 16));
        infoLabel.setTextColor(Color.WHITE);

        exitButton = new ButtonGUI(context, 10, 300, "退出游戏", new Font(Font.MONOSPACED, Font.PLAIN, 12), 10);
        exitButton.setBackgroundColor(Color.DARK_GRAY);
        exitButton.setTextColor(Color.WHITE);

        exitButton.setOnClickListener(new MouseListener() {

            @Override
            public void mouseClicked(MouseEvent e) {
                System.exit(0);
            }

            @Override
            public void mouseEntered(MouseEvent e) {}

            @Override
            public void mouseExited(MouseEvent e) {}

            @Override
            public void mousePressed(MouseEvent e) {}

            @Override
            public void mouseReleased(MouseEvent e) {}
            
        });

        formatter = new HighPerformanceStringFormatter("玩家位置: [%x, %x]");
        dbgFormatter = new HighPerformanceStringFormatter("Debug Informations: \nCamera Status: {CenterPos: [%x %x], Scale: %x}");

        posLabel = new LabelGUI(context, 10, 50, formatter.format(player.getCenterX(), player.getCenterY()),
                new Font(Font.MONOSPACED, Font.PLAIN, 16));
        posLabel.setTextColor(Color.WHITE);

        debugLabel = new LabelGUI(context, 10, 75, dbgFormatter.format(camera.getCenterX(), camera.getCenterY(), camera.getScale()), new Font(Font.MONOSPACED, Font.PLAIN, 16));
        debugLabel.setTextColor(Color.WHITE);

        context.addGUI(infoLabel);
        context.addGUI(posLabel);
        context.addGUI(debugLabel);
        context.addGUI(exitButton);
        RectGameObject border = new RectGameObject(context, -525, -525, 1050, 1050, Color.BLUE, false, new BasicStroke(10));
        border.disableCollision();
        context.addObject(border);

        context.setMapBorder(-525, -525, 1050, 1050);

        try {
            Thread.sleep(3000); // 模拟加载时间
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void beforeRender(GameContext context, double deltaMillis) {
        // 物理与逻辑更新
        playerController.updatePlayer();

        // ========== 正确的 FPS 计算 ==========
        frameCount++;
        fpsTimer += deltaMillis;

        // 每 1 秒更新一次 FPS 显示（性能友好）
        if (fpsTimer >= 1000.0) {
            currentFPS = frameCount;
            frameCount = 0;
            fpsTimer = 0.0;
            // 更新界面文字
            infoLabel.setText("上下左右 移动红色方块 | FPS: " + currentFPS);
        }

        posLabel.setText(formatter.format(player.getCenterX(), player.getCenterY()));
        debugLabel.setText(dbgFormatter.format(camera.getCenterX(), camera.getCenterY(), camera.getScale()));

        GameCamera cam = context.getCamera();
        if (context.isKeyPressing(KeyEvent.VK_Q)) {
            cam.setScale(cam.getScale() * 1.01);
        }
        if (context.isKeyPressing(KeyEvent.VK_E)) {
            cam.setScale(cam.getScale() * 0.99);
        }

    }

    @Override
    public void afterRender(GameContext context, double deltaMillis) {
    }

    @Override
    public void resized(int newWidth, int newHeight) {
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Zest2D Engine Example - A Simple 2D Game");
        GameApp game = new GameApp();

        frame.setSize(new Dimension(800, 600));
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(game);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        game.enableInitializingScreen();

        game.start();
    }
}
```
