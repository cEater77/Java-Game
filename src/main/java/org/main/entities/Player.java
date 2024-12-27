package org.main.entities;

import Engine.Texture;
import org.joml.Vector2f;
import org.joml.Vector3f;

import java.util.List;

public class Player extends Entity {

    private float speed = 1 / 15.0f;

    public Player(Vector3f position, Texture texture)
    {
        super(position, texture);
    }

    @Override
    public void update()
    {
        super.update();
    }

    @Override
    public EntityType getType()
    {
        return EntityType.PLAYER;
    }

    @Override
    public void onCollision(Entity other)
    {

    }

    public void move(List<MovementDirection> directions)
    {
        Vector2f playerPositionDelta = new Vector2f(0.0f,0.0f);

        for(MovementDirection direction : directions)
        {
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

        if(playerPositionDelta.x != 0.0f || playerPositionDelta.y != 0.0f)
            playerPositionDelta.normalize(speed);
        position.x += playerPositionDelta.x;
        position.y += playerPositionDelta.y;
    }

    public float getSpeed() {
        return speed;
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }
}
