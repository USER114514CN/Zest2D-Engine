package com.user114514.zest2d.objects;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Stroke;

import com.user114514.zest2d.core.Base2DRectArea;
import com.user114514.zest2d.core.BaseGame2DObject;
import com.user114514.zest2d.core.GameContext;

public class RectGameObject extends BaseGame2DObject {
    private Color color;
    private boolean fill;
    private Stroke stroke;

    public RectGameObject(GameContext context, int x, int y, int width, int height, Color color, boolean isFill, Stroke stroke) {
        super(context, x, y, width, height);
        this.color = color;
        this.fill = isFill;
        this.stroke = stroke;
    }

    public RectGameObject(GameContext context) {
        this(context, 0, 0, 50, 50, Color.RED, true, new BasicStroke(0));
    }

    @Override
    public void draw(Graphics2D graphics2d, int posX, int posY, int width, int height) {
        graphics2d.setColor(color);
        graphics2d.setStroke(stroke);
        if (fill) graphics2d.fillRect(posX, posY, width, height);
        else graphics2d.drawRect(posX, posY, width, height);
    }

    @Override
    public boolean equalsRect(Base2DRectArea another) {
        return (another instanceof RectGameObject ? (super.equalsRect(another) && color.equals(((RectGameObject) another).color)) : super.equalsRect(another));
    }
    
}
