package com.user114514.zest2d.guis;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Stroke;

import com.user114514.zest2d.core.GameContext;

public class ButtonGUI extends LabelGUI {
    private String text;
    private Color bgColor;
    private Stroke border;
    private int arcW;
    private int arcH;
    private int padding;

    public ButtonGUI(GameContext context, int x, int y, String text, Font font, int padding) {
        super(context, x, y, text, font);
        this.text = text;
        this.bgColor = Color.LIGHT_GRAY;
        setTextColor(Color.BLACK);
        this.border = new BasicStroke(2, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND);
        this.arcW = 5;
        this.arcH = 5;
        this.padding = 5;
    }

    @Override
    public void draw(Graphics2D graphics2d, int posX, int posY, int width, int height) {
        updateSize(graphics2d);
        width = getWidth();
        height = getHeight();
        graphics2d.setStroke(border);
        graphics2d.setColor(bgColor);

        graphics2d.fillRoundRect(posX, posY, width + 2* padding, height + 2 * padding, arcW, arcH);
        super.draw(graphics2d, posX + padding, posY + padding, width, height);
    }

    public void setText(String newText) {
        text = newText;
        sizeDirty = true;
    }

    public void setBackgroundColor(Color color) {
        bgColor = color;
    }


    public void setArcWidth(int w) {
        arcW = w;
    }

    public void setArcHeight(int h) {
        arcH = h;
    }
    
    public void setBorder(Stroke bor) {
        border = bor;
    }

    public void setPadding(int padding) {
        this.padding = padding;
    }

    public String getText() { return text; }

    public Color getBackgroundColor() { return bgColor; }

    public int getArcWidth() { return arcW; }

    public int getArcHeight() { return arcH; }

    public Stroke getBorder() { return border; }

    public int getPadding() { return padding; }
}
