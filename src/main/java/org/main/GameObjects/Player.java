package org.main.GameObjects;

import Engine.ResourceManager;
import Engine.animation.Animation;
import Engine.animation.AnimationController;
import Engine.renderer.Texture;
import org.joml.*;
import org.main.Game;
import org.main.MovementDirection;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Player extends GameObject {

    private float speedInMeterPerSecond = 5.0f;
    private PlayerState playerState = PlayerState.IDLE;
    private MovementDirection direction = MovementDirection.UP;

    private enum PlayerState {
        IDLE,
        WALKING
    }

    public Player(Vector3f position, ResourceManager resourceManager) {
        super(position, false);
        setupAnimations(resourceManager);
        collisionSizeMultiplier = 0.3f;
    }

    @Override
    public void update() {
        super.update();
        animationController.setDirection(direction);
    }

    @Override
    public GameObjectType getGameObjectType() {
        return GameObjectType.PLAYER;
    }

    @Override
    public void onCollision(GameObject other) {
        switch (other.getGameObjectType()) {
            case BLOCK: {
                Block block = (Block)other;
                if (block.getBlockType() == Block.BlockTypeID.FINISH) return;
                // verhindert das der Spieler in bestimmten Blocktypen hereingeht bzw er wird heraus geschubst.
                Vector2f offset = aabb.getMinTranslationVector(other.getAABB());
                setPosition(new Vector3f(-offset.x + position.x, -offset.y + position.y, position.z));
                update();
            }
        }
    }

    public void move(List<MovementDirection> directions) {

        Vector2f playerPositionDelta = new Vector2f(0.0f, 0.0f);
        float speed = speedInMeterPerSecond * Game.getWindow().getLastFrameDuration();

        for (MovementDirection direction : directions) {
            switch (direction) {
                case LEFT:
                    playerPositionDelta.x += speed;
                    break;
                case RIGHT:
                    playerPositionDelta.x -= speed;
                    break;
                case DOWN:
                    playerPositionDelta.y += speed;
                    break;
                case UP:
                    playerPositionDelta.y -= speed;
                    break;
            }
        }

        // verhindert das der spieler sich diagonal schneller bewegt als die "geraden" richtungen.
        if (playerPositionDelta.x != 0.0f || playerPositionDelta.y != 0.0f)
            playerPositionDelta.normalize(speed);
        setPosition(new Vector3f(playerPositionDelta.x + position.x, playerPositionDelta.y + position.y, 1.0f));

        MovementDirection currentDirection = MovementDirection.toSingleDirection(directions);
        if(currentDirection != MovementDirection.NONE)
            direction = currentDirection;

        if (playerPositionDelta.length() == 0)
            playerState = PlayerState.IDLE;
        else
            playerState = PlayerState.WALKING;
    }

    public float getSpeed() {
        return speedInMeterPerSecond;
    }

    public void setSpeed(float speed) {
        this.speedInMeterPerSecond = speed;
    }

    private void setupAnimations(ResourceManager resourceManager) {

        String[] directionFrameNames = {
                "_Character_UpLeft", "_Character_DownRight","_Character_Left",  "_Character_Right",
                "_Character_Up", "_Character_Down","_Character_UpRight", "_Character_DownLeft"
        };

        for(int i = 1; i <= directionFrameNames.length; i++)
        {
            Animation idle = new Animation();
            idle.addFrameAnimation(1.0f, false, Arrays.asList(resourceManager.getTexture("00" + directionFrameNames[i - 1])));
            if(i == 1) {
                animationController = new AnimationController("idle", MovementDirection.values()[i], idle);
            }
            else {
                animationController.addAnimation("idle", MovementDirection.values()[i], idle);
            }

            Animation walk = new Animation();
            List<Texture> frames = new ArrayList<>();
            for(int j = 0; j < 4; j++)
            {
                frames.add(resourceManager.getTexture("0" + j + directionFrameNames[i - 1]));
            }
            walk.addFrameAnimation(0.50f, true, frames);
            animationController.addAnimation("walking", MovementDirection.values()[i], walk);
        }

        animationController.addTransition("idle", "walking", () -> playerState == PlayerState.WALKING);
        animationController.addTransition("walking", "idle", () -> playerState == PlayerState.IDLE);
    }

    @Override
    public void serialize(DataOutputStream stream) throws IOException {
        stream.writeInt(getGameObjectType().ordinal());
    }

    @Override
    public void deserialize(DataInputStream stream) throws IOException {

    }
}
