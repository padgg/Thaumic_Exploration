package flaxbeard.thaumicexploration.block;

import flaxbeard.thaumicexploration.ThaumicExploration;
import flaxbeard.thaumicexploration.tile.TileEntitySoulBrazier;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
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

    @Override
    public void onBlockClicked(World p_149699_1_, int p_149699_2_, int p_149699_3_, int p_149699_4_, EntityPlayer p_149699_5_) {
        super.onBlockClicked(p_149699_1_, p_149699_2_, p_149699_3_, p_149699_4_, p_149699_5_);
        ((TileEntitySoulBrazier)p_149699_1_.getTileEntity(p_149699_2_,p_149699_3_,p_149699_4_)).setActive(p_149699_5_);
    }

    @Override
    public void onBlockPlacedBy(World p_149689_1_, int p_149689_2_, int p_149689_3_, int p_149689_4_, EntityLivingBase p_149689_5_, ItemStack p_149689_6_) {
        super.onBlockPlacedBy(p_149689_1_, p_149689_2_, p_149689_3_, p_149689_4_, p_149689_5_, p_149689_6_);
        ((TileEntitySoulBrazier)p_149689_1_.getTileEntity(p_149689_2_,p_149689_3_,p_149689_4_)).owner=((EntityPlayer)p_149689_5_).getGameProfile();
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
