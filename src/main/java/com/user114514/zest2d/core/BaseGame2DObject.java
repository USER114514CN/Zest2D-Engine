package com.user114514.zest2d.core;

import java.awt.Graphics2D;
import java.util.List;
import java.util.function.Consumer;

public abstract class BaseGame2DObject extends Base2DRectArea implements Cloneable {
    private GameContext context;
    private boolean visible;
    private boolean enableCollision = true;
    private boolean active;
    private Consumer<BaseGame2DObject> onCollision;

    public BaseGame2DObject(GameContext context, int x, int y, int width, int height) {
        super(context, x, y, width, height);
        this.context = context;
        this.visible = true;
        this.enableCollision = true;
        this.active = true;
        this.onCollision = (obj) -> {};
    }

    public BaseGame2DObject(GameContext context, int x, int y) {
        this(context, x, y, 50, 50);
    }

    public BaseGame2DObject(GameContext context) {
        this(context, 0, 0);
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public boolean isActive() {
        return active;
    }

    public void setOnCollision(Consumer<BaseGame2DObject> handler) {
        this.onCollision = handler;
    }

    public void enable() {
        setActive(true);
    }

    public void disable() {
        setActive(false);
    }

    @Override
    public void setZ(int z) {
        super.setZ(z);
        context.sortObjZ();
    }

    public abstract void draw(Graphics2D graphics2d, int posX, int posY, int width, int height);

    public void render(Graphics2D g2d) {
        if (visible && active) draw(g2d, super.x, super.y, super.width, super.height);
    }

    public void moveBy(int dx, int dy) {
        super.x += dx;
        super.y += dy;
        resized();
    }

    public void moveByWithCollision(int dx, int dy, int accuracy) {
        if (!enableCollision) {
            moveBy(dx, dy);
            return;
        }
        if (accuracy <= 0) {
            accuracy = 1;
        }

        List<BaseGame2DObject> candidates = context.getPossibleCollisions(this);

        int stepsX = Math.abs(dx) / accuracy;
        int remainingX = Math.abs(dx) % accuracy;
        int stepDirX = Integer.compare(dx, 0);

        for (int i = 0; i < stepsX; i++) {
            moveBy(stepDirX * accuracy, 0);
            if (checkBoundaryCollision() || checkObjectCollision(candidates)) {
                moveBy(-stepDirX * accuracy, 0);
                onCollision.accept(this);
                break;
            }
        }

        if (remainingX > 0) {
            moveBy(stepDirX * remainingX, 0);
            if (checkBoundaryCollision() || checkObjectCollision(candidates)) {
                moveBy(-stepDirX * remainingX, 0);
                onCollision.accept(this);
            }
        }

        int stepsY = Math.abs(dy) / accuracy;
        int remainingY = Math.abs(dy) % accuracy;
        int stepDirY = Integer.compare(dy, 0);

        for (int i = 0; i < stepsY; i++) {
            moveBy(0, stepDirY * accuracy);
            if (checkBoundaryCollision() || checkObjectCollision(candidates)) {
                moveBy(0, -stepDirY * accuracy);
                onCollision.accept(this);
                break;
            }
        }

        if (remainingY > 0) {
            moveBy(0, stepDirY * remainingY);
            if (checkBoundaryCollision() || checkObjectCollision(candidates)) {
                moveBy(0, -stepDirY * remainingY);
                onCollision.accept(this);
            }
        }

        resized();
    }

    private boolean checkBoundaryCollision() {
        if (context.getMapBorder() == null) return false;
        if (this.x < context.getMapBorder().getX()) {
            return true;
        }
        if (this.y < context.getMapBorder().getY()) {
            return true;
        }
        if (this.x + getWidth() > context.getMapBorder().getMaxX()) {
            return true;
        }
        if (this.y + getHeight() > context.getMapBorder().getMaxY()) {
            return true;
        }
        return false;
    }

    private boolean checkObjectCollision(List<BaseGame2DObject> candidates) {
        for (BaseGame2DObject other : candidates) {
            if (other == this || !other.enableCollision || !other.isActive()) {
                continue;
            }
            if (this.bounds.intersects(other.getBounds())) {
                return true;
            }
        }
        return false;
    }

    public void setVisible(boolean v) {
        this.visible = v;
    }

    public void visible() {
        this.visible = true;
    }

    public void invisible() {
        this.visible = false;
    }

    public boolean isVisible() {
        return visible;
    }

    public void setCollisionEnable(boolean enable) {
        enableCollision = enable;
    }

    public void enableCollision() {
        setCollisionEnable(true);
    }

    public void disableCollision() {
        setCollisionEnable(false);
    }

    public boolean isCollisionEnabled() {
        return enableCollision;
    }
}
