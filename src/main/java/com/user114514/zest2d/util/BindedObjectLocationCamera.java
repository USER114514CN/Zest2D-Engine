package com.user114514.zest2d.util;

import com.user114514.zest2d.core.BaseGame2DObject;
import com.user114514.zest2d.core.GameCamera;
import com.user114514.zest2d.core.GameContext;

public class BindedObjectLocationCamera extends GameCamera {
    private BaseGame2DObject bindedLocationCamera;

    public BindedObjectLocationCamera(GameContext context, BaseGame2DObject object) {
        super(context, object.getCenterX(), object.getCenterY());
        bindedLocationCamera = object;
    }

    @Override
    public void update() {
        // 我踏马成功了！！！！！！！！！！！！
        // 我把 GameContext 的 renderAllObjects 方法改成了先缩放后平移，这样就能保证缩放时以玩家为中心了！！！！！！！！！！！！
        setXY((int) Math.round(bindedLocationCamera.getCenterX()),
                (int) Math.round(bindedLocationCamera.getCenterY()));
    }
}