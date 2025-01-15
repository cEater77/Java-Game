package org.main.GameObjects;

import org.joml.Vector3f;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class Block extends GameObject {

    private boolean ignoreCollision;
    private BlockTypeID typeID;

    private final static Map<BlockTypeID, Block> blockRegistry = new HashMap<>();

    public enum BlockTypeID
    {
        WOOD
    }

    public Block(Vector3f position, boolean ignoreCollision, BlockTypeID typeID) {
        super(position);
        this.ignoreCollision = ignoreCollision;
        this.typeID = typeID;
    }

    public Block() {

    }

    public static void registerBlock(Block block)
    {
        blockRegistry.put(block.getBlockType(), block);
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
    public GameObjectType getGameObjectType() {
        return GameObjectType.DECORATION;
    }

    public BlockTypeID getBlockType()
    {
        return typeID;
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
        Block blockToCopyFrom = blockRegistry.get(typeID);

        ignoreCollision = blockToCopyFrom.ignoreCollision;
        animationController = blockToCopyFrom.animationController;

        super.deserialize(stream);
    }
}
