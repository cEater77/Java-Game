package org.main.entities;

import Engine.Texture;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.main.AABB;

public class Entity {
    public int id;
    Vector3f position;
    AABB aabb;
    Texture texture;

    public Entity(Vector3f position, Texture texture)
    {
        this.position = position;
        this.aabb = new AABB(new Vector2f(position.x - 0.5f, position.y - 0.5f), new Vector2f(position.x + 0.5f, position.y + 0.5f));
        this.texture = texture;
    }

    public void update()
    {

    }

    public void onCollision(Entity other)
    {
        System.out.println("i got hit by: " + other.toString());
    }

    public EntityType getType()
    {
        return EntityType.UNKNOWN;
    }

    public void setPosition(Vector3f position)
    {
        this.position = position;
        aabb.min = new Vector2f(position.x - 0.5f, position.y - 0.5f);
        aabb.max = new Vector2f(position.x + 0.5f, position.y + 0.5f);
    }

    public Vector3f getPosition() {
        return position;
    }

    public AABB getAABB()
    {
        return aabb;
    }
}
