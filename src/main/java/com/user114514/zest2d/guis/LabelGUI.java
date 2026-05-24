package com.user114514.zest2d.guis;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;

import com.user114514.zest2d.core.BaseGame2DGUI;
import com.user114514.zest2d.core.GameContext;

public class LabelGUI extends BaseGame2DGUI {
    private String text;
    private Font font;
    protected boolean sizeDirty = true;
    private Color textColor;

    public LabelGUI(GameContext context, int x, int y, String text, Font font) {
        super(context, x, y, -1, -1);
        this.text = text == null ? "" : text;
        this.font = font;
    }

    public void setText(String text) {
        this.text = text == null ? "" : text;
        this.sizeDirty = true;
    }

    public void setTextColor(Color color) {
        textColor = color;
    }

    public void setFont(Font font) {
        this.font = font;
        this.sizeDirty = true;
    }

    protected void updateSize(Graphics2D g2d) {
        if (font == null || !sizeDirty) {
            return;
        }

        FontMetrics fm = g2d.getFontMetrics(font);
        int width = fm.stringWidth(text);
        int height = fm.getHeight();
        
        setWidth(width);
        setHeight(height);
        
        this.sizeDirty = false;
    }

    @Override
    public void draw(Graphics2D graphics2d, int posX, int posY, int width, int height) {
        if (font == null) return;

        graphics2d.setFont(font);
        graphics2d.setColor(textColor);
        updateSize(graphics2d);
        
        graphics2d.drawString(text, posX, posY + graphics2d.getFontMetrics().getAscent());
    }

    public Color getTextColor() { return textColor; }
}
