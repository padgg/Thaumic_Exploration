package flaxbeard.thaumicexploration.tile;

import flaxbeard.thaumicexploration.ThaumicExploration;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.*;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.common.config.Config;
import thaumcraft.common.config.ConfigBlocks;
import thaumcraft.common.tiles.TileCrucible;
import thaumcraft.common.tiles.TileVisRelay;

import java.util.List;
//import flaxbeard.thaumicexploration.integration.BotaniaIntegration;

public class TileEntityEverburnUrn extends TileVisRelay implements IFluidTank,IFluidHandler {

    private int ticks = 0;
    private int drainTicks = 0;
    private float ignisVis;
    private int dX;
    private int dY;
    private int dZ;
    private int excessTicks = 0;
    private int drainType = 0;
    private float distance = 0;
    private int range = 3;
    private int yRange = 2;
    private EntityPlayer burningPlayer;
    private static int CONVERSION_FACTOR=250;


    @Override
    public FluidStack getFluid() {
        // TODO Auto-generated method stub
        return new FluidStack(FluidRegistry.LAVA, (int)Math.floor(ignisVis*CONVERSION_FACTOR));
    }

    @Override
    public int getFluidAmount() {
        // TODO Auto-generated method stub
        return (int)Math.floor(ignisVis*CONVERSION_FACTOR);
    }

    @Override
    public int getCapacity() {
        // TODO Auto-generated method stub
        return 4*CONVERSION_FACTOR;
    }

    @Override
    public FluidTankInfo getInfo() {
        // TODO Auto-generated method stub
        return new FluidTankInfo(this);
    }

    @Override
    public int fill(FluidStack resource, boolean doFill) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public FluidStack drain(int maxDrain, boolean doDrain) {
        // TODO Auto-generated method stub
        float drained = maxDrain;
        if (getFluidAmount() < drained) {
            drained = getFluidAmount();
        }

        FluidStack stack = new FluidStack(FluidRegistry.LAVA, (int)drained);
        System.out.println(drained/CONVERSION_FACTOR);
        ignisVis-=drained/CONVERSION_FACTOR;
        return stack;
    }

    @Override
    public int fill(ForgeDirection from, FluidStack resource, boolean doFill) {
        // TODO Auto-generated method stub
        return this.fill(resource, doFill);
    }

    @Override
    public FluidStack drain(ForgeDirection from, FluidStack resource,
                            boolean doDrain) {
        // TODO Auto-generated method stub
        if (!resource.isFluidEqual(new FluidStack(FluidRegistry.LAVA, 1)) || !(from == ForgeDirection.UP)) {
            return null;
        }

        return this.drain(resource.amount, doDrain);

    }

    @Override
    public FluidStack drain(ForgeDirection from, int maxDrain, boolean doDrain) {
        // TODO Auto-generated method stub
        if (from == ForgeDirection.UP) {
            return this.drain(maxDrain, doDrain);
        } else {
            FluidStack stack = new FluidStack(FluidRegistry.LAVA, 0);
            return stack;
        }
    }

    @Override
    public boolean canFill(ForgeDirection from, Fluid fluid) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean canDrain(ForgeDirection from, Fluid fluid) {
        // TODO Auto-generated method stub
        return (from == ForgeDirection.UP);
    }

    @Override
    public FluidTankInfo[] getTankInfo(ForgeDirection from) {
        // TODO Auto-generated method stub
        return new FluidTankInfo[]{this.getInfo()};
    }

    @Override
    public void updateEntity() {
        super.updateEntity();
        this.ticks++;
        if(this.ticks==10) {
            if (this.ignisVis < 16) {
                ignisVis += this.consumeVis(Aspect.FIRE, 1);
            ticks=0;
            }
        }
    }
}
