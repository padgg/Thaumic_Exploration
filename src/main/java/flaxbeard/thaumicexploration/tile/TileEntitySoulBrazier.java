package flaxbeard.thaumicexploration.tile;

import com.mojang.authlib.GameProfile;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.biome.BiomeGenBase;
import thaumcraft.api.ThaumcraftApi;
import thaumcraft.common.Thaumcraft;
import thaumcraft.common.blocks.BlockTaintFibres;
import thaumcraft.common.config.Config;
import thaumcraft.common.lib.Utils;
import thaumcraft.common.lib.world.ThaumcraftWorldGenerator;
import thaumcraft.common.tiles.TileVisRelay;

/**
 * Created by nekosune on 03/08/14.
 */
public class TileEntitySoulBrazier extends TileVisRelay {


    public int storedWarp;
    public int currentEssentia;
    public int currentVis;
    public boolean active;
    public GameProfile owner;
    public int count;
    @Override
    public void readCustomNBT(NBTTagCompound nbttagcompound) {
        super.readCustomNBT(nbttagcompound);
        storedWarp=nbttagcompound.getInteger("storedWarp");
        currentEssentia=nbttagcompound.getInteger("currentEssentia");
        currentVis=nbttagcompound.getInteger("currentVis");
        active=nbttagcompound.getBoolean("active");
        owner= NBTUtil.func_152459_a(nbttagcompound.getCompoundTag("owner"));
    }

    @Override
    public void writeCustomNBT(NBTTagCompound nbttagcompound) {
        super.writeCustomNBT(nbttagcompound);
        nbttagcompound.setInteger("storedWarp",storedWarp);
        nbttagcompound.setInteger("currentEssentia",currentEssentia);
        nbttagcompound.setInteger("currentVis",currentVis);
        nbttagcompound.setBoolean("active",active);
        NBTTagCompound gameProfile=new NBTTagCompound();
        NBTUtil.func_152460_a(gameProfile,owner);
        nbttagcompound.setTag("owner", gameProfile);
    }



    public void setActive(EntityPlayer player)
    {
        owner=player.getGameProfile();
        active=true;
        //Thaumcraft.proxy.getPlayerKnowledge().addWarp(owner.getName(),-2);
        storedWarp+=2;
    }
    @Override
    public void updateEntity() {
        super.updateEntity();
        this.count += 1;
        //Thaumcraft.proxy.getPlayerKnowledge().getWarp("nekosune");
        changeTaint();

    }



    public void changeTaint()
    {
        if(active && (this.count % 50 == 0)) {
            int x = 0;
            int z = 0;
            int y = 0;
            x = this.xCoord + this.worldObj.rand.nextInt(16) - this.worldObj.rand.nextInt(16);
            z = this.zCoord + this.worldObj.rand.nextInt(16) - this.worldObj.rand.nextInt(16);
            BiomeGenBase bg = this.worldObj.getBiomeGenForCoords(x, z);
            if (bg.biomeID != ThaumcraftWorldGenerator.biomeTaint.biomeID) {
                Utils.setBiomeAt(this.worldObj, x, z, ThaumcraftWorldGenerator.biomeTaint);
            }
            if ((Config.hardNode) && (this.worldObj.rand.nextBoolean())) {
                x = this.xCoord + this.worldObj.rand.nextInt(10) - this.worldObj.rand.nextInt(10);
                z = this.zCoord + this.worldObj.rand.nextInt(10) - this.worldObj.rand.nextInt(10);
                y = this.yCoord + this.worldObj.rand.nextInt(10) - this.worldObj.rand.nextInt(10);
                if (!BlockTaintFibres.spreadFibres(this.worldObj, x, y, z)) ;
            }
            this.worldObj.markBlockForUpdate(this.xCoord, this.yCoord, this.zCoord);
        }
    }
}
