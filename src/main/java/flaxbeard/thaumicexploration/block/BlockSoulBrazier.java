package flaxbeard.thaumicexploration.block;

import flaxbeard.thaumicexploration.ThaumicExploration;
import flaxbeard.thaumicexploration.tile.TileEntitySoulBrazier;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

/**
 * Created by nekosune on 03/08/14.
 */
public class BlockSoulBrazier extends BlockContainer {

    public BlockSoulBrazier() {
        super(Material.rock);
    }

    public boolean isOpaqueCube()
    {
        return false;
    }

    /**
     * If this block doesn't render as an ordinary block it will return False (examples: signs, buttons, stairs, etc)
     */
    public boolean renderAsNormalBlock()
    {
        return false;
    }

    public int getRenderType()
    {
        return ThaumicExploration.soulBrazierRenderID;
    }
    @Override
    public TileEntity createNewTileEntity(World p_149915_1_, int p_149915_2_) {
        return new TileEntitySoulBrazier();
    }
    @Override
    public void setBlockBoundsBasedOnState(IBlockAccess par1iBlockAccess, int par2, int par3, int par4)
    {
        setBlockBounds(0.175F, 0.0F, 0.175F, 0.925F, 1.0F, 0.925F);
        //super.setBlockBoundsBasedOnState(par1iBlockAccess, par2, par3, par4);
    }
}
