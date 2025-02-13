package org.main.GameObjects;

import Engine.animation.AnimationController;
import org.joml.Vector2f;
import org.joml.Vector3f;
import Engine.AABB;

import java.io.*;

public class GameObject {
    protected Vector3f position = new Vector3f();
    protected AABB aabb = new AABB();
    protected AnimationController animationController = new AnimationController();
    // isHighlighted wird in EditorScreen verwendet, um zu sehen, welcher Block momentan ausgewählt ist.
    protected boolean isHighlighted = false;
    protected float collisionSizeMultiplier = 0.5f;
    protected boolean ignoreCollision;

    public GameObject(Vector3f position, boolean ignoreCollision) {
        this.position = position;
        this.aabb = new AABB(
                new Vector2f(position.x - collisionSizeMultiplier, position.y - collisionSizeMultiplier),
                new Vector2f(position.x + collisionSizeMultiplier, position.y + collisionSizeMultiplier)
        );
        this.ignoreCollision = ignoreCollision;
    }

    public GameObject() {

    }

    public GameObject(GameObject gameObject) {
        this.position = new Vector3f(gameObject.position);
    }

    public void update() {
        aabb.min = new Vector2f(position.x - collisionSizeMultiplier, position.y - collisionSizeMultiplier);
        aabb.max = new Vector2f(position.x + collisionSizeMultiplier, position.y + collisionSizeMultiplier);
    }

    public void onCollision(GameObject other) {
    }

    public GameObjectType getGameObjectType() {
        return GameObjectType.UNKNOWN;
    }

    public void setPosition(Vector3f position) {
        this.position = position;
    }

    public Vector3f getPosition() {
        return position;
    }

    public AABB getAABB() {
        return aabb;
    }

    public AnimationController getAnimationController()
    {
        return animationController;
    }

    public void setAnimationController(AnimationController animationController)
    {
        this.animationController = animationController;
    }

    public String toString()
    {
        return "x:" + position.x + " y:" + position.y + " z:" + position.z + " ";
    }

    public void setHighlight(boolean highlight)
    {
        this.isHighlighted = highlight;
    }

    public boolean isHighlighted()
    {
        return isHighlighted;
    }

    public boolean ignoresCollision()
    {
        return ignoreCollision;
    }

    public void serialize(DataOutputStream stream) throws IOException {
        stream.writeFloat(position.x);
        stream.writeFloat(position.y);
        stream.writeFloat(position.z);
    }

    public void deserialize(DataInputStream stream) throws IOException {
        position.x = stream.readFloat();
        position.y = stream.readFloat();
        position.z = stream.readFloat();
        update();
    }
}
