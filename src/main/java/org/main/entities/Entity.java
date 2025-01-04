package org.main.entities;

import Engine.animation.AnimationController;
import Engine.renderer.Texture;
import org.joml.Vector2f;
import org.joml.Vector3f;
import Engine.AABB;

public class Entity {
    protected int id;
    protected Vector3f position;
    protected AABB aabb;
    protected AnimationController animationController;

    public Entity(Vector3f position)
    {
        this.position = position;
        this.aabb = new AABB(new Vector2f(position.x - 0.5f, position.y - 0.5f), new Vector2f(position.x + 0.5f, position.y + 0.5f));
    }

    public void update()
    {
        if(animationController != null)
            animationController.update();
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

    public void setAnimationController(AnimationController animationController) {
        this.animationController = animationController;
    }

    public AnimationController getAnimationController()
    {
        return animationController;
    }
}
