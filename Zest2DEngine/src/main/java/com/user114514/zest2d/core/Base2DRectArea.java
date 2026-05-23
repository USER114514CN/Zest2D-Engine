package com.user114514.zest2d.core;

import java.awt.Rectangle;

public class Base2DRectArea {
    protected int x, y, width, height, z;
    protected Rectangle bounds;
    private GameContext context;

    public Base2DRectArea(GameContext context, int x, int y, int width, int height) {
        this.context = context;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.z = 0;
        this.bounds = new Rectangle(x, y, width, height);
    }

    public void setX(int targetX) {
        this.x = targetX;
        resized();
    }

    public void setY(int targetY) {
        this.y = targetY;
        resized();
    }

    public void setZ(int z) {
        this.z = z; 
    }

    public void setXY(int targetX, int targetY) {
        this.x = targetX;
        this.y = targetY;
        resized();
    }

    public void setWidth(int newWidth) {
        this.width = newWidth;
        resized();
    }

    public void setHeight(int newHeight) {
        this.height = newHeight;
        resized();
    }

    public void setSize(int newWidth, int newHeight) {
        this.width = newWidth;
        this.height = newHeight;
        resized();
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getZ() {
        return z;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public Rectangle getBounds() {
        return bounds;
    }

    protected void resized() {
        this.bounds.setBounds(this.x, this.y, this.width, this.height);
    }

    public boolean equalsRect(Base2DRectArea another) {
        return(width == another.width) && (height == another.height);
    }

    public int getCenterX() {
        return x + (int) Math.round(width / 2);
    }

    public int getCenterY() {
        return y + (int) Math.round(height / 2);
    }

    public int getRightX() {
        return x + width;
    }

    public int getBottomY() {
        return y + height;
    }
}
