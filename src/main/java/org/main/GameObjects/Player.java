package org.main.GameObjects;

import Engine.ResourceManager;
import Engine.animation.Animation;
import Engine.animation.AnimationController;
import Engine.animation.FrameAnimationComponent;
import Engine.renderer.Texture;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.main.Game;
import org.main.MovementDirection;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Player extends GameObject {

    private float speedInMeterPerSecond = 10.0f;
    private PlayerState playerState = PlayerState.IDLE;

    private enum PlayerState {
        IDLE,
        WALKING
    }

    public Player(Vector3f position, ResourceManager resourceManager) {
        super(position);
        setupAnimations(resourceManager);
    }

    public Player(ResourceManager resourceManager) {
        super(new Vector3f(0.0f, 0.0f, 0.9f));
        setupAnimations(resourceManager);
    }

    @Override
    public void update() {
        super.update();
    }

    @Override
    public GameObjectType getGameObjectType() {
        return GameObjectType.PLAYER;
    }

    @Override
    public void onCollision(GameObject other) {
        switch (other.getGameObjectType()) {
            case BLOCK: {
                if (((Block) other).ignoresCollision()) return;
                Vector2f offset = aabb.getMinTranslationVector(other.getAABB());
                setPosition(new Vector3f(-offset.x + position.x, -offset.y + position.y, position.z));
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
                case BACKWARD:
                    playerPositionDelta.y += speed;
                    break;
                case FORWARD:
                    playerPositionDelta.y -= speed;
                    break;
            }

        }

        if (playerPositionDelta.x != 0.0f || playerPositionDelta.y != 0.0f)
            playerPositionDelta.normalize(speed);
        setPosition(new Vector3f(playerPositionDelta.x + position.x, playerPositionDelta.y + position.y, 1.0f));

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
        List<Texture> frames = new ArrayList<>();
        frames.add(resourceManager.getTexture("grass"));
        frames.add(resourceManager.getTexture("snow_grass"));
        frames.add(resourceManager.getTexture("wood"));
        Animation idle = new Animation();
        idle.addFrameAnimation(1.0f, true, frames.subList(0, 2));

        Animation walking = new Animation();
        walking.addFrameAnimation(1.0f, true, frames.subList(2, 3));

        animationController = new AnimationController("idle", MovementDirection.NONE, idle);
        animationController.addAnimation("walking", MovementDirection.NONE, walking);

        animationController.addTransition("idle", "walking", () -> playerState == PlayerState.WALKING);
        animationController.addTransition("walking", "idle", () -> playerState == PlayerState.IDLE);
    }

    @Override
    public void serialize(DataOutputStream stream) throws IOException {
        stream.writeInt(getGameObjectType().ordinal());
        super.serialize(stream);
    }

    @Override
    public void deserialize(DataInputStream stream) throws IOException {
        super.deserialize(stream);
    }
}
