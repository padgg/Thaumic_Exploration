package flaxbeard.thaumicexploration.client.render;

import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import flaxbeard.thaumicexploration.ThaumicExploration;
import flaxbeard.thaumicexploration.block.BlockSoulBrazier;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.world.IBlockAccess;
import org.lwjgl.opengl.GL11;
import thaumcraft.client.renderers.block.BlockRenderer;
import thaumcraft.common.blocks.BlockCandle;

import java.awt.*;

/**
 * Created by nekosune on 03/08/14.
 */
public class BlockSoulBrazierRenderer extends BlockRenderer
        implements ISimpleBlockRenderingHandler {
    @Override
    public void renderInventoryBlock(Block block, int metadata, int modelId, RenderBlocks renderer) {
        Color c = Color.red;
        float r = c.getRed() / 255.0F;
        float g = c.getGreen() / 255.0F;
        float b = c.getBlue() / 255.0F;
        GL11.glColor3f(r, g, b);
        block.setBlockBounds(BlockRenderer.W6, 0.0F, BlockRenderer.W6, BlockRenderer.W10, 0.5F, BlockRenderer.W10);
        renderer.setRenderBoundsFromBlock(block);
        drawFaces(renderer, block, ((BlockSoulBrazier)block).getBlockTextureFromSide(0), true);
        GL11.glColor3f(1.0F, 1.0F, 1.0F);

        block.setBlockBounds(0.475F, 0.5F, 0.475F, 0.525F, BlockRenderer.W10, 0.525F);
        renderer.setRenderBoundsFromBlock(block);
        drawFaces(renderer, block, ((BlockSoulBrazier)block).getBlockTextureFromSide(0), true);
    }

    @Override
    public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z, Block block, int modelId, RenderBlocks renderer) {
        return false;
    }

    @Override
    public boolean shouldRender3DInInventory(int modelId) {
        return modelId==ThaumicExploration.soulBrazierRenderID;
    }

    @Override
    public int getRenderId() {
        return ThaumicExploration.soulBrazierRenderID;
    }
}
