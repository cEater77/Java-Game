package org.main.entities;

import Engine.animation.Animation;
import Engine.animation.AnimationController;
import Engine.animation.FrameAnimationComponent;
import Engine.renderer.Texture;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.main.MovementDirection;
import org.w3c.dom.Text;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Player extends Entity {

    private float speed = 1 / 150.0f;
    private PlayerState playerState = PlayerState.IDLE;

    private enum PlayerState
    {
        IDLE,
        WALKING
    }

    public Player(Vector3f position, List<Texture> frames) {
        super(position);

        Animation idle = new Animation(null, new FrameAnimationComponent(frames.subList(0, 3), 5.0f, true), null);
        Animation walking = new Animation(null, new FrameAnimationComponent(frames.subList(3,6), 5.0f, true), null);

        animationController = new AnimationController("idle", MovementDirection.NONE, idle);
        animationController.addAnimation("walking", MovementDirection.NONE, walking);

        animationController.addTransition("idle", "walking", () -> playerState == PlayerState.WALKING);
        animationController.addTransition("walking", "idle", () -> playerState == PlayerState.IDLE);

    }

    @Override
    public void update() {
        super.update();
    }

    @Override
    public EntityType getType() {
        return EntityType.PLAYER;
    }

    @Override
    public void onCollision(Entity other) {
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

        if(playerPositionDelta.length() == 0)
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
}
