package org.main.GameObjects;

import Engine.animation.Animation;
import Engine.animation.AnimationController;
import Engine.animation.FrameAnimationComponent;
import Engine.renderer.Texture;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.main.MovementDirection;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.List;

public class Player extends GameObject {

    private float speed = 1 / 150.0f;
    private PlayerState playerState = PlayerState.IDLE;

    private enum PlayerState {
        IDLE,
        WALKING
    }

    public Player(Vector3f position, List<Texture> frames) {
        super(position);

        Animation idle = new Animation();
        Animation walking = new Animation();

        animationController = new AnimationController("idle", MovementDirection.NONE, idle);
        animationController.addAnimation("walking", MovementDirection.NONE, walking);

        animationController.addTransition("idle", "walking", () -> playerState == PlayerState.WALKING);
        animationController.addTransition("walking", "idle", () -> playerState == PlayerState.IDLE);
    }

    public Player() {

    }

    @Override
    public void update() {
        super.update();
    }

    @Override
    public GameObjectType getType() {
        return GameObjectType.PLAYER;
    }

    @Override
    public void onCollision(GameObject other) {
        Vector2f offset = aabb.getMinTranslationVector(other.getAABB());
        setPosition(new Vector3f(-offset.x + position.x, -offset.y + position.y, 0));
    }

    public void move(List<MovementDirection> directions) {
        Vector2f playerPositionDelta = new Vector2f(0.0f, 0.0f);

        for (MovementDirection direction : directions) {
            switch (direction) {
                case LEFT:
                    playerPositionDelta.x -= speed;
                    break;
                case RIGHT:
                    playerPositionDelta.x += speed;
                    break;
                case BACKWARD:
                    playerPositionDelta.y -= speed;
                    break;
                case FORWARD:
                    playerPositionDelta.y += speed;
                    break;
            }
        }

        if (playerPositionDelta.x != 0.0f || playerPositionDelta.y != 0.0f)
            playerPositionDelta.normalize(speed);
        setPosition(new Vector3f(playerPositionDelta.x + position.x, playerPositionDelta.y + position.y, 0.0f));

        if (playerPositionDelta.length() == 0)
            playerState = PlayerState.IDLE;
        else
            playerState = PlayerState.WALKING;
    }

    public float getSpeed() {
        return speed;
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }

    @Override
    public void serialize(DataOutputStream stream) throws IOException {
        stream.writeInt(getType().ordinal());
        super.serialize(stream);
    }

    @Override
    public void deserialize(DataInputStream stream) throws IOException {
        super.deserialize(stream);
    }
}
