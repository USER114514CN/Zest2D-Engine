package com.user114514.zest2d.objects;

import com.user114514.zest2d.core.BaseGame2DObject;
import com.user114514.zest2d.core.GameContext;

public class MovablePlayer {
    private GameContext context;
    private BaseGame2DObject playerObject;
    private int speed;
    private PlayerKeyControlBindInfo bindInfo;

    public static class PlayerKeyControlBindInfo {
        final int upMoveKey;
        final int downMoveKey;
        final int leftMoveKey;
        final int rightMoveKey;

        private PlayerKeyControlBindInfo(int v1, int v2, int v3, int v4) {
            upMoveKey = v1;
            downMoveKey = v2;
            leftMoveKey = v3;
            rightMoveKey = v4;
        }

        public static class Builder {
            private int upMoveKey = -1;
            private int downMoveKey = -1;
            private int leftMoveKey = -1;
            private int rightMoveKey = -1;

            public static Builder create() {
                return new Builder();
            }

            public Builder bindMovingUpKey(int keyCode) {
                upMoveKey = keyCode;
                return this;
            }

            public Builder bindMovingDownKey(int keyCode) {
                downMoveKey = keyCode;
                return this;
            }

            public Builder bindMovingLeftKey(int keyCode) {
                leftMoveKey = keyCode;
                return this;
            }

            public Builder bindMovingRightKey(int keyCode) {
                rightMoveKey = keyCode;
                return this;
            }

            public PlayerKeyControlBindInfo build() {
                return new PlayerKeyControlBindInfo(upMoveKey, downMoveKey, leftMoveKey, rightMoveKey);
            }
        }
    }

    public MovablePlayer(GameContext context, BaseGame2DObject playerObj, int speed) {
        this.context = context;
        this.playerObject = playerObj;
        this.speed = speed;
    }

    public void bindControlKey(PlayerKeyControlBindInfo info) {
        this.bindInfo = info;
    }

    public void updatePlayer() {
        // 未绑定为-1，理论上永远不会触发
        int speed = this.speed / 250;
        if (context.isKeyPressing(bindInfo.upMoveKey)) playerObject.moveByWithCollision(0, (int) Math.round(-speed * context.getDelta()), 1);
        if (context.isKeyPressing(bindInfo.downMoveKey)) playerObject.moveByWithCollision(0, (int) Math.round(speed * context.getDelta()), 1);
        if (context.isKeyPressing(bindInfo.leftMoveKey)) playerObject.moveByWithCollision((int) Math.round(-speed * context.getDelta()), 0, 1);
        if (context.isKeyPressing(bindInfo.rightMoveKey)) playerObject.moveByWithCollision((int) Math.round(speed * context.getDelta()), 0, 1);
    }
}
