package com.user114514.zest2d.core;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.JPanel;
import javax.swing.Timer;

public abstract class Base2DGame extends JPanel {
    private static final long serialVersionUID = 1L;
    private boolean enableInitializingScreen;
    private GameContext context;
    private final int delay;
    private long lastRenderTime;
    private long currentRenderTime;
    private Timer gameLoop;
    private boolean isRunning;
    private boolean isInitializing = false;
    private BufferedImage initializingScreen;

    public Base2DGame(int frameDelay) {
        enableInitializingScreen = false;
        isRunning = false;
        context = new GameContext(new ArrayList<>(), new ArrayList<>(), getSize());
        delay = frameDelay;
        gameLoop = new Timer(delay, new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                if (!isInitializing) {
                    currentRenderTime = System.nanoTime();
                    long delta = currentRenderTime - lastRenderTime;
                    context.setDelta(delta / 1000000.0);
                    beforeRender(context, delta / 1000000.0);
                    update();
                    afterRender(context, delta / 1000000.0);
                    lastRenderTime = currentRenderTime;
                } else {
                    Base2DGame.this.repaint();
                }
            }

        });
        setFocusable(true);
        addComponentListener(new ComponentListener() {

            @Override
            public void componentHidden(ComponentEvent e) {
            }

            @Override
            public void componentMoved(ComponentEvent e) {
            }

            @Override
            public void componentResized(ComponentEvent e) {
                int w = getWidth();
                int h = getHeight();
                context.resize(new java.awt.Dimension(w, h));
                resized(w, h);
            }

            @Override
            public void componentShown(ComponentEvent e) {
            }

        });
        addMouseListener(new MouseListener() {

            @Override
            public void mouseClicked(MouseEvent e) {
                context.handleClick(e);
            }

            @Override
            public void mouseEntered(MouseEvent e) {
            }

            @Override
            public void mouseExited(MouseEvent e) {
            }

            @Override
            public void mousePressed(MouseEvent e) {
            }

            @Override
            public void mouseReleased(MouseEvent e) {
            }

        });
        addKeyListener(new KeyListener() {

            @Override
            public void keyPressed(KeyEvent e) {
                context.keyPressingStates[e.getKeyCode()] = true;
            }

            @Override
            public void keyReleased(KeyEvent e) {
                context.keyPressingStates[e.getKeyCode()] = false;
            }

            @Override
            public void keyTyped(KeyEvent e) {
            }

        });
    }

    public void setEnableInitializingScreen(boolean enable) {
        this.enableInitializingScreen = enable;
    }

    public void setInitializingScreen(BufferedImage img) {
        this.initializingScreen = img;
    }

    public void enableInitializingScreen() {
        this.enableInitializingScreen = true;
    }

    public void disableInitializingScreen() {
        this.enableInitializingScreen = false;
    }

    public void start() {
        if (isRunning)
            return;
        requestFocusInWindow();
        context.resize(getSize());
        isRunning = true;
        lastRenderTime = System.nanoTime();
        if (enableInitializingScreen) {
            try {
                initializingScreen = ImageIO.read(getClass().getResourceAsStream("/assets/initializing_screen.png"));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        isInitializing = true;
        gameLoop.start();
        init(context);
        isInitializing = false;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        if (!isInitializing) {
            context.renderAllObject(g2d);
            context.renderAllGUI(g2d);
        } else {
            if (enableInitializingScreen && initializingScreen != null) {
                g2d.drawImage(initializingScreen, 0, 0, getWidth(), getHeight(), null);
            }
        }
    }

    public abstract void init(GameContext context);

    public abstract void beforeRender(GameContext context, double deltaMillis);

    public abstract void afterRender(GameContext context, double deltaMillis);

    public abstract void resized(int newWidth, int newHeight);

    public void update() {
        context.update();
        this.repaint();
    }

    public double getDeltaMillis() {
        return (currentRenderTime - lastRenderTime) / 1000000.0;
    }

}
