package com.user114514.zest2d.math;


import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;

import com.user114514.zest2d.core.Base2DRectArea;


public class QuadTree<E extends Base2DRectArea> {
    private static final int MAX_OBJECTS = 10; // 每个节点最多容纳10个物体，超过就分裂
    private static final int MAX_LEVELS = 6;   // 最大分裂层级

    private int level;
    private List<E> objects;
    private Rectangle bounds;
    private QuadTree<E>[] nodes; // 四个子节点: 0:右上, 1:左上, 2:左下, 3:右下

    public QuadTree(int pLevel, Rectangle pBounds) {
        this.level = pLevel;
        this.bounds = pBounds;
        this.objects = new ArrayList<>();
        this.nodes = null; // 初始化为null，表示未分裂
    }

    // 清除四叉树（每帧开始前调用）
    public void clear() {
        objects.clear();
        if (nodes != null) {
            for (int i = 0; i < nodes.length; i++) {
                if (nodes[i] != null) {
                    nodes[i].clear();
                    nodes[i] = null;
                }
            }
            nodes = null; // 重置为未分裂状态
        }
    }

    // 分裂节点

    @SuppressWarnings("unchecked")
    private void split() {
        int subWidth = (int) (bounds.getWidth() / 2);
        int subHeight = (int) (bounds.getHeight() / 2);
        int x = (int) bounds.getX();
        int y = (int) bounds.getY();

        nodes = new QuadTree[4];
        // 注意：索引顺序需与getIndex逻辑保持一致
        // 0: 右上
        nodes[0] = new QuadTree<>(level + 1, new Rectangle(x + subWidth, y, subWidth, subHeight));
        // 1: 左上
        nodes[1] = new QuadTree<>(level + 1, new Rectangle(x, y, subWidth, subHeight));
        // 2: 左下
        nodes[2] = new QuadTree<>(level + 1, new Rectangle(x, y + subHeight, subWidth, subHeight));
        // 3: 右下
        nodes[3] = new QuadTree<>(level + 1, new Rectangle(x + subWidth, y + subHeight, subWidth, subHeight));
    }

    // 判断物体属于哪个子节点
    private int getIndex(Rectangle pRect) {
        int index = -1;
        double verticalMidpoint = bounds.getX() + (bounds.getWidth() / 2);
        double horizontalMidpoint = bounds.getY() + (bounds.getHeight() / 2);

        // 物体完全在上半部分
        boolean topQuadrant = (pRect.getY() < horizontalMidpoint && pRect.getY() + pRect.getHeight() < horizontalMidpoint);
        // 物体完全在下半部分
        boolean bottomQuadrant = (pRect.getY() > horizontalMidpoint);

        // 物体完全在左半部分
        if (pRect.getX() < verticalMidpoint && pRect.getX() + pRect.getWidth() < verticalMidpoint) {
            if (topQuadrant) {
                index = 1; // 左上
            } else if (bottomQuadrant) {
                index = 2; // 左下
            }
        } 
        // 物体完全在右半部分
        else if (pRect.getX() > verticalMidpoint) {
            if (topQuadrant) {
                index = 0; // 右上
            } else if (bottomQuadrant) {
                index = 3; // 右下
            }
        }

        return index;
    }

    // 插入物体
    public void insert(E pObject) {
        // 如果有子节点，尝试插入子节点
        if (nodes != null) {
            int index = getIndex(pObject.getBounds());
            if (index != -1) {
                nodes[index].insert(pObject);
                return;
            }
        }

        // 否则，存入当前节点
        objects.add(pObject);

        // 如果物体数量超过阈值且未达最大层级，则分裂
        if (objects.size() > MAX_OBJECTS && level < MAX_LEVELS) {
            if (nodes == null) {
                split();
            }

            // 将当前节点的物体重新分配到子节点
            // 使用倒序遍历或迭代器避免并发修改异常，这里使用while循环配合remove
            int i = 0;
            while (i < objects.size()) {
                E obj = objects.get(i);
                int index = getIndex(obj.getBounds());
                if (index != -1) {
                    nodes[index].insert(obj);
                    objects.remove(i); // 移除已分配的物体，索引不增加
                } else {
                    i++; // 保留在当前节点，索引增加
                }
            }
        }
    }

    // 检索可能碰撞的物体列表
    public List<E> retrieve(List<E> returnObjects, E pObject) {
        int index = getIndex(pObject.getBounds());
        if (index != -1 && nodes != null) {
            nodes[index].retrieve(returnObjects, pObject);
        }

        returnObjects.addAll(objects);
        return returnObjects;
    }
    
    public List<E> getAllRectAreaOfPointInNode(List<E> returnResult, int x, int y) {
        if (nodes != null) {
            int index = getIndex(new Rectangle(x, y, 0, 0));
            if (index != -1) {
                nodes[index].getAllRectAreaOfPointInNode(returnResult, x, y);
            }
        }

        returnResult.addAll(objects);
        return returnResult;
    }
}
