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
        this.aabb = new AABB(new Vector2f(position), new Vector2f(75.0f, 37.5f));
        this.texture = texture;
    }

    public void update()
    {

    }

    public void onCollision(Entity other)
    {

    }

    public EntityType getType()
    {
        return EntityType.UNKNOWN;
    }

    public void setPosition(Vector3f position)
    {
        this.position = position;
    }

    public Vector3f getPosition() {
        return position;
    }
}
