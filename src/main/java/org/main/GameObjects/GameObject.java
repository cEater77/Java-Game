package org.main.GameObjects;

import Engine.animation.AnimationController;
import org.joml.Vector2f;
import org.joml.Vector3f;
import Engine.AABB;

import java.io.*;

public class GameObject {
    protected Vector3f position;
    protected AABB aabb;
    protected AnimationController animationController;

    public GameObject(Vector3f position)
    {
        this.position = position;
        this.aabb = new AABB(new Vector2f(position.x - 0.5f, position.y - 0.5f), new Vector2f(position.x + 0.5f, position.y + 0.5f));
    }

    public GameObject()
    {

    }

    public void update()
    {
        if(animationController != null)
            animationController.update();
    }

    public void onCollision(GameObject other)
    {
    }

    public GameObjectType getType()
    {
        return GameObjectType.UNKNOWN;
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

    public void serialize(DataOutputStream stream)
    {
        try {
            stream.writeFloat(position.x);
            stream.writeFloat(position.y);
            stream.writeFloat(position.z);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void deserialize(DataInputStream stream)
    {

    }
}
