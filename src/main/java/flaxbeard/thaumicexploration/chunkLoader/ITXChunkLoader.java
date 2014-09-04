package flaxbeard.thaumicexploration.chunkLoader;

import net.minecraftforge.common.ForgeChunkManager;

/**
 * Created by nekosune on 04/09/14.
 */
public interface ITXChunkLoader {
    void forceChunkLoading(ForgeChunkManager.Ticket ticket);
}
