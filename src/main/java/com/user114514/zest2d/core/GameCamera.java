
package com.user114514.zest2d.core;

import java.awt.Rectangle;

public class GameCamera {
    private int centerX;
    private int centerY;
    private Rectangle visibleArea;
    protected GameContext context;
    private double xyScale;

    public GameCamera(GameContext context, int x, int y) {
        this.context = context;
        centerX = x;
        centerY = y;
        xyScale = 1.0;
        this.visibleArea = new Rectangle();
    }

    public int getCenterX() {
        return centerX;
    }

    public int getLeftX() {
        return centerX - (int) Math.round(context.getWidthOnScreen() / xyScale / 2);
    }

    public int getCenterY() {
        return centerY;
    }

    public int getUpY() {
        return centerY - (int) Math.round(context.getHeightOnScreen() / xyScale / 2);
    }

    public double getScale() {
        return xyScale;
    }

    public void setX(int x) {
        centerX = x;
    }

    public void setY(int y) {
        centerY = y;
    }

    public void setXY(int x, int y) {
        centerX = x;
        centerY = y;
    }

    public void setScale(double s) {
        xyScale = s;
    }

    public void moveBy(int dx, int dy) {
        centerX += dx;
        centerY += dy;
    }

    public Rectangle getVisibleArea() {
        if (context == null) {
            return visibleArea;
        }

        int screenWidth = context.getWidthOnScreen();
        int screenHeight = context.getHeightOnScreen();

        double viewWidth = screenWidth / xyScale;
        double viewHeight = screenHeight / xyScale;

        int x = (int) (centerX - viewWidth / 2);
        int y = (int) (centerY - viewHeight / 2);
        int width = (int) viewWidth;
        int height = (int) viewHeight;

        visibleArea.setBounds(x, y, width, height);

        return visibleArea;
    }

    public void updateVisibleAreaTo(Rectangle result) {
        if (context == null) {
            return;
        }

        result.setBounds(getVisibleArea());
    }

    public void update() {
    }
}
