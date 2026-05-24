
package com.user114514.zest2d.core;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.event.MouseEvent;
import java.awt.geom.AffineTransform;
import java.util.ArrayList;
import java.util.List;

import com.user114514.zest2d.math.QuadTree;

public class GameContext {
    private ArrayList<BaseGame2DObject> objects;
    private ArrayList<BaseGame2DGUI> guis;
    private Dimension size;
    private QuadTree<BaseGame2DObject> quadTreeObj;
    private QuadTree<BaseGame2DGUI> quadTreeGui;
    private double delta;
    private GameCamera camera;
    private Rectangle visibleArea;
    private Rectangle mapBorder;
    private Rectangle objQuadTreeBoundary;

    protected boolean[] keyPressingStates;

    GameContext(ArrayList<BaseGame2DObject> objects, ArrayList<BaseGame2DGUI> guis, Dimension size) {
        this.objects = objects;
        this.guis = guis;
        this.size = size;
        this.quadTreeObj = new QuadTree<>(0, visibleArea);
        this.quadTreeGui = new QuadTree<>(0, visibleArea);
        keyPressingStates = new boolean[65536];
        rebuildSpatialIndex();
        visibleArea = new Rectangle();
        camera = new GameCamera(this, size.width / 2, size.height / 2);
        camera.updateVisibleAreaTo(visibleArea);
        mapBorder = null;
        if (mapBorder != null) {
            objQuadTreeBoundary = new Rectangle(mapBorder);
        } else {
            objQuadTreeBoundary = new Rectangle(visibleArea);
        }
    }

    public void setMapBorder(int x, int y, int width, int height) {
        if (mapBorder == null) {
            mapBorder = new Rectangle(x, y, width, height);
            return;
        }
        mapBorder.setBounds(x, y, width, height);
        objQuadTreeBoundary = new Rectangle(mapBorder);
    }

    public Rectangle getMapBorder() {
        return mapBorder;
    }

    public void setCamera(GameCamera camera) {
        this.camera = camera;
        camera.updateVisibleAreaTo(visibleArea);
    }

    public GameCamera getCamera() {
        return camera;
    }

    public void addObject(BaseGame2DObject object) {
        objects.add(object);
        rebuildSpatialIndex();
        sortObjZ();
        if (objQuadTreeBoundary == null) {
            if (object.getRightX() > objQuadTreeBoundary.getMaxX()) {
                objQuadTreeBoundary.setRect(objQuadTreeBoundary.getX(), objQuadTreeBoundary.getY(),
                        object.getRightX() - objQuadTreeBoundary.getX() + 10, objQuadTreeBoundary.getHeight());
            }
            if (object.getBottomY() > objQuadTreeBoundary.getMaxY()) {
                objQuadTreeBoundary.setRect(objQuadTreeBoundary.getX(), objQuadTreeBoundary.getY(),
                        objQuadTreeBoundary.getWidth(), object.getBottomY() - objQuadTreeBoundary.getY() + 10);
            }
            if (object.getX() < objQuadTreeBoundary.getX()) {
                objQuadTreeBoundary.setRect(object.getX(), objQuadTreeBoundary.getY(),
                        objQuadTreeBoundary.getMaxX() - object.getX() - 10, objQuadTreeBoundary.getHeight());
            }
            if (object.getY() < objQuadTreeBoundary.getY()) {
                objQuadTreeBoundary.setRect(objQuadTreeBoundary.getX(), object.getY(),
                        objQuadTreeBoundary.getWidth(), objQuadTreeBoundary.getMaxY() - object.getY() - 10);
            }
        }
    }

    public void removeObject(BaseGame2DObject object) {
        objects.remove(object);
        rebuildSpatialIndex();
        sortObjZ();
    }

    public void addObjects(ArrayList<BaseGame2DObject> objects) {
        this.objects.addAll(objects);
        rebuildSpatialIndex();
        sortObjZ();
        for (BaseGame2DObject object : objects) {
            if (objQuadTreeBoundary == null) {
                if (object.getRightX() > objQuadTreeBoundary.getMaxX()) {
                    objQuadTreeBoundary.setRect(objQuadTreeBoundary.getX(), objQuadTreeBoundary.getY(),
                            object.getRightX() - objQuadTreeBoundary.getX() + 10, objQuadTreeBoundary.getHeight());
                }
                if (object.getBottomY() > objQuadTreeBoundary.getMaxY()) {
                    objQuadTreeBoundary.setRect(objQuadTreeBoundary.getX(), objQuadTreeBoundary.getY(),
                            objQuadTreeBoundary.getWidth(), object.getBottomY() - objQuadTreeBoundary.getY() + 10);
                }
                if (object.getX() < objQuadTreeBoundary.getX()) {
                    objQuadTreeBoundary.setRect(object.getX(), objQuadTreeBoundary.getY(),
                            objQuadTreeBoundary.getMaxX() - object.getX() - 10, objQuadTreeBoundary.getHeight());
                }
                if (object.getY() < objQuadTreeBoundary.getY()) {
                    objQuadTreeBoundary.setRect(objQuadTreeBoundary.getX(), object.getY(),
                            objQuadTreeBoundary.getWidth(), objQuadTreeBoundary.getMaxY() - object.getY() - 10);
                }
            }
        }
    }

    public void removeObjects(ArrayList<BaseGame2DObject> objs) {
        objects.removeAll(objs);
        rebuildSpatialIndex();
        sortObjZ();
    }

    public int getObjectsCount() {
        return objects.size();
    }

    public boolean containsObject(BaseGame2DObject obj) {
        return objects.contains(obj);
    }

    public void clearObjects() {
        objects.clear();
        rebuildSpatialIndex();
        sortObjZ();
    }

    public ArrayList<BaseGame2DObject> getObjects() {
        return objects;
    }

    public void addGUI(BaseGame2DGUI gui) {
        guis.add(gui);
        rebuildSpatialIndex();
        sortGuiZ();
    }

    public void removeGUI(BaseGame2DGUI gui) {
        guis.remove(gui);
        rebuildSpatialIndex();
        sortGuiZ();
    }

    public void addGUIs(ArrayList<BaseGame2DGUI> guis) {
        this.guis.addAll(guis);
        rebuildSpatialIndex();
        sortGuiZ();
    }

    public void removeGUIs(ArrayList<BaseGame2DGUI> guis) {
        this.guis.removeAll(guis);
        rebuildSpatialIndex();
        sortGuiZ();
    }

    public int getGUIsCount() {
        return guis.size();
    }

    public boolean containsGUI(BaseGame2DGUI gui) {
        return guis.contains(gui);
    }

    public void clearGUIs() {
        guis.clear();
        rebuildSpatialIndex();
        sortGuiZ();
    }

    public ArrayList<BaseGame2DGUI> getGUIs() {
        return guis;
    }

    public void resize(Dimension newSize) {
        camera.updateVisibleAreaTo(visibleArea);
        size = newSize;
        if (quadTreeObj != null) {
            this.quadTreeObj = new QuadTree<>(0, objQuadTreeBoundary);
        }
        if (quadTreeGui != null) {
            this.quadTreeGui = new QuadTree<>(0, visibleArea);
        }
        camera.setXY(getWidthOnScreen() / 2, getHeightOnScreen() / 2);
        rebuildSpatialIndex();
    }

    public void update() {
        camera.update();
        camera.updateVisibleAreaTo(visibleArea);
    }

    public Dimension getSize() {
        return size;
    }

    public int getWidthOnScreen() {
        return (int) Math.round(size.getWidth());
    }

    public int getHeightOnScreen() {
        return (int) Math.round(size.getHeight());
    }

    public void rebuildSpatialIndex() {
        if (quadTreeObj == null) {
            quadTreeObj = new QuadTree<>(0, objQuadTreeBoundary);
        } else {
            quadTreeObj.clear();
        }
        if (quadTreeGui == null) {
            quadTreeGui = new QuadTree<>(0, new Rectangle(0, 0, getWidthOnScreen(), getHeightOnScreen()));
        } else {
            quadTreeGui.clear();
        }

        for (BaseGame2DObject obj : objects) {
            quadTreeObj.insert(obj);
        }

        for (BaseGame2DGUI gui : guis) {
            quadTreeGui.insert(gui);
        }
    }

    public void renderAllObject(Graphics2D g2d) {
        AffineTransform old = g2d.getTransform();
        g2d.scale(camera.getScale(), camera.getScale());
        g2d.translate(-camera.getLeftX(), -camera.getUpY());
        for (BaseGame2DObject obj : objects) {
            if (!obj.isVisible() || !obj.isActive())
                continue;
            Object oldInterpolation = g2d.getRenderingHint(RenderingHints.KEY_INTERPOLATION);
            try {
                obj.render(g2d);
            } finally {
                if (oldInterpolation != null) {
                    g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, oldInterpolation);
                }
            }
        }
        g2d.setTransform(old);
    }

    public void renderAllGUI(Graphics2D g2d) {
        for (BaseGame2DGUI gui : guis) {
            if (!gui.isVisible())
                continue;
            Object oldInterpolation = g2d.getRenderingHint(RenderingHints.KEY_INTERPOLATION);
            try {
                gui.render(g2d);
            } finally {
                if (oldInterpolation != null) {
                    g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, oldInterpolation);
                }
            }
        }
    }

    public List<BaseGame2DObject> getPossibleCollisions(BaseGame2DObject target) {
        if (quadTreeObj == null) {
            return new ArrayList<>(objects);
        }

        List<BaseGame2DObject> candidates = new ArrayList<>();
        quadTreeObj.retrieve(candidates, target);
        return candidates;
    }

    public void handleClick(MouseEvent event) {
        Rectangle guiRect = new Rectangle();
        List<BaseGame2DGUI> candidateGUIs = quadTreeGui.getAllRectAreaOfPointInNode(new ArrayList<>(), event.getX(),
                event.getY());
        for (BaseGame2DGUI gui : candidateGUIs) {
            guiRect.setBounds(gui.getX(), gui.getY(), gui.getWidth(), gui.getHeight());
            if (guiRect.contains(event.getX(), event.getY()))
                gui.onClick.mouseClicked(event);
        }
    }

    public void sortObjZ() {
        objects.sort((a, b) -> Integer.compare(a.getZ(), b.getZ()));
    }

    public void sortGuiZ() {
        guis.sort((a, b) -> Integer.compare(a.getZ(), b.getZ()));
    }

    public boolean isKeyPressing(int keyCode) {
        return keyPressingStates[keyCode];
    }

    protected void setDelta(double d) {
        delta = d;
    }

    public double getDelta() {
        return delta;
    }

    public double worldValue2ScreenValue(int value) {
        return value * camera.getScale();
    }

    public double screenValue2WorldValue(int value) {
        return value / camera.getScale();
    }
}
