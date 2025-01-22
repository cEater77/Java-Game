package org.main.GameObjects;

import Engine.AABB;
import Engine.ResourceManager;
import Engine.animation.AnimationController;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.main.Game;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.function.BiConsumer;
import java.util.function.Consumer;


public class Block extends GameObject {

    private boolean ignoreCollision;
    private BlockTypeID typeID;
    private BiConsumer<Block, GameObject> collisionCallback;

    public enum BlockTypeID {
        WOOD,
        DARK_WOOD,
        FINISH,
        BARRIER
    }

    public Block(Vector3f position, ResourceManager resourceManager, boolean ignoreCollision, BlockTypeID typeID) {
        super(position);
        this.ignoreCollision = ignoreCollision;
        this.typeID = typeID;
    }

    public Block() {

    }

    public Block(Block block)
    {
        super(block);
        this.ignoreCollision = block.ignoreCollision;
        this.typeID = block.typeID;
        this.aabb = new AABB(block.aabb);
        this.animationController = new AnimationController(block.animationController);
        this.collisionCallback = block.collisionCallback;
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

        if(collisionCallback != null)
            collisionCallback.accept(this, other);
    }

    @Override
    public GameObjectType getGameObjectType() {
        return GameObjectType.BLOCK;
    }

    public BlockTypeID getBlockType() {
        return typeID;
    }

    public boolean ignoresCollision()
    {
        return ignoreCollision;
    }

    public void setCollisionCallback(BiConsumer<Block, GameObject> collisionCallback)
    {
        this.collisionCallback = collisionCallback;
    }

    @Override
    public String toString()
    {
        return super.toString() + "BlockTypeID: " + typeID.toString() + " ";
    }

    @Override
    public void serialize(DataOutputStream stream) throws IOException {
        stream.writeInt(getGameObjectType().ordinal());
        stream.writeInt(getBlockType().ordinal());

        super.serialize(stream);
    }

    @Override
    public void deserialize(DataInputStream stream) throws IOException {

        typeID = BlockTypeID.values()[stream.readInt()];
        Block blockToCopyFrom = Game.getBlockRegistry().getBlockByID(typeID);

        ignoreCollision = blockToCopyFrom.ignoreCollision;
        animationController = blockToCopyFrom.animationController;
        collisionCallback = blockToCopyFrom.collisionCallback;

        super.deserialize(stream);
    }
}
