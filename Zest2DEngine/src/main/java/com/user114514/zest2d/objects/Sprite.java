package com.user114514.zest2d.objects;

import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;

import com.user114514.zest2d.core.Base2DRectArea;
import com.user114514.zest2d.core.BaseGame2DObject;
import com.user114514.zest2d.core.GameContext;

public class Sprite extends BaseGame2DObject {
    private BufferedImage texture;
    private int srcX, srcY, srcW, srcH;

    public Sprite(GameContext context, int x, int y, int width, int height, BufferedImage texture) {
        super(context, x, y, width, height);
        this.texture = texture;
        setSrcRect(0, 0, texture.getWidth(), texture.getHeight());
    }

    public void setSrcRect(int x, int y, int w, int h) {
        this.srcX = x;
        this.srcY = y;
        this.srcW = w;
        this.srcH = h;
    }

    @Override
    public void draw(Graphics2D graphics2d, int posX, int posY, int width, int height) {
        graphics2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);
        graphics2d.drawImage(
                texture,
                posX, posY, posX + width, posY + height,
                srcX, srcY, srcX + srcW, srcY + srcH,
                null);
    }

    @Override
    public boolean equalsRect(Base2DRectArea another) {
        if (another instanceof Sprite) {
            Sprite casted = (Sprite) another;
            return super.equalsRect(another) && (texture == casted.texture) && (srcX == casted.srcX) && (srcY == casted.srcY) && (srcW == casted.srcW) && (srcH == casted.srcH);
        }
        return super.equalsRect(another);
    }

}
