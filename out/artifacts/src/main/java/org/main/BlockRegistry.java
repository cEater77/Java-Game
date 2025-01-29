package org.main;

import org.main.GameObjects.Block;

import java.util.HashMap;
import java.util.Map;

public class BlockRegistry {
    private final static Map<Block.BlockTypeID, Block> blockEntries = new HashMap<>();

    public void registerBlock(Block block) {
        blockEntries.put(block.getBlockType(), block);
    }

    public Block getBlockByID(Block.BlockTypeID blockID) {
        return blockEntries.get(blockID);
    }
}
