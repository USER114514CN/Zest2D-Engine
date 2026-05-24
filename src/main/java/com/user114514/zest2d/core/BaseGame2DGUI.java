package com.user114514.zest2d.core;

import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public abstract class BaseGame2DGUI extends Base2DRectArea {
    private GameContext context;
    private boolean visible;
    protected MouseListener onClick;

    public BaseGame2DGUI(GameContext context, int x, int y, int width, int height) {
        super(context, x, y, width, height);
        this.context = context;
        this.visible = true;
        this.onClick = new MouseListener() {

            @Override
            public void mouseClicked(MouseEvent e) {}
            @Override
            public void mouseEntered(MouseEvent e) {}
            @Override
            public void mouseExited(MouseEvent e) {}
            @Override
            public void mousePressed(MouseEvent e) {}
            @Override
            public void mouseReleased(MouseEvent e) {}
            
        };
    }

    public BaseGame2DGUI(GameContext context, int x, int y) {
        this(context, x, y, 50, 50);
    }

    public BaseGame2DGUI(GameContext context) {
        this(context, 0, 0);
    }

    @Override
    public void setZ(int z) {
        super.setZ(z);
        context.sortGuiZ();
    }

    public abstract void draw(Graphics2D graphics2d, int posX, int posY, int width, int height);

    public void render(Graphics2D g2d) {
        if (visible) draw(g2d, x, y, width, height);
    }

    public void moveBy(int dx, int dy) {
        this.x += dx;
        this.y += dy;
    }

    public void setVisible(boolean v) {
        visible = v;
    }

    public void visible() {
        visible = true;
    }

    public void invisible() {
        visible = false;
    }

    public boolean isVisible() {
        return visible;
    }

    public void setOnClickListener(MouseListener listener) {
        this.onClick = listener;
    }
}
