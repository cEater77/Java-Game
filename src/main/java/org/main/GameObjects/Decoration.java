package org.main.GameObjects;

import Engine.AABB;
import Engine.animation.AnimationController;
import org.joml.Vector3f;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class Decoration extends GameObject {

    private boolean ignoreCollision;

    public Decoration(Vector3f position, boolean ignoreCollision) {
        super(position);
        this.ignoreCollision = ignoreCollision;
    }

    public Decoration() {

    }

    @Override
    public void update() {
        super.update();
    }

    @Override
    public void onCollision(GameObject other) {
        if (ignoreCollision)
            return;

        super.onCollision(other);
    }

    @Override
    public GameObjectType getType() {
        return GameObjectType.DECORATION;
    }

    @Override
    public void serialize(DataOutputStream stream) throws IOException {

        stream.writeInt(getType().ordinal());
        stream.writeBoolean(ignoreCollision);
        animationController.getCurrentAnimation().serialize(stream);
        super.serialize(stream);
    }

    @Override
    public void deserialize(DataInputStream stream) throws IOException {
        ignoreCollision = stream.readBoolean();
        super.deserialize(stream);
    }
}
