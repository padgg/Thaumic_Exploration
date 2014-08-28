package flaxbeard.thaumicexploration.tile;

import com.mojang.authlib.GameProfile;
import flaxbeard.thaumicexploration.ThaumicExploration;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraftforge.common.util.ForgeDirection;
import thaumcraft.api.ThaumcraftApi;
import thaumcraft.api.ThaumcraftApiHelper;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.aspects.IAspectContainer;
import thaumcraft.api.aspects.IEssentiaTransport;
import thaumcraft.common.Thaumcraft;
import thaumcraft.common.blocks.BlockTaintFibres;
import thaumcraft.common.config.Config;
import thaumcraft.common.lib.Utils;
import thaumcraft.common.lib.world.ThaumcraftWorldGenerator;
import thaumcraft.common.tiles.TileVisRelay;

/**
 * Created by nekosune on 03/08/14.
 */
public class TileEntitySoulBrazier extends TileVisRelay  implements IEssentiaTransport {


    public int storedWarp;
    public int currentEssentia;
    public int currentVis;
    public boolean active;
    public GameProfile owner;
    public int count;
    private int ticks=0;
    private static int EssentiaCapacity=16;
    private static int VisCapacity=16;
    private static int EssentiaRate=5;
    private static int VisRate=5;
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



    public boolean setActive(EntityPlayer player)
    {
        if(!player.getGameProfile().getId().equals(owner.getId())) {
            if(worldObj.isRemote)
                player.addChatComponentMessage(new ChatComponentTranslation("soulbrazier.invalidplayer"));
            return false;
        }
        active=true;
        Thaumcraft.proxy.getPlayerKnowledge().addWarp(owner.getName(),-2);
        storedWarp+=2;
        return true;
    }
    @Override
    public void updateEntity() {
        super.updateEntity();
        this.count += 1;
        if(this.count % 10==0) {
            ThaumicExploration.proxy.spawnActiveBrazierParticle(worldObj,xCoord,yCoord,zCoord);
        }
        changeTaint();
        if(active) {

            getPower();
            spendPower();
            if (!checkPower()) {
                active = false;
                Thaumcraft.proxy.getPlayerKnowledge().addWarp(owner.getName(), storedWarp);
                storedWarp=0;
            }

        }
    }

    private boolean checkPower() {
        return currentEssentia <=0 || currentVis<=0;
    }

    private void spendPower() {
        currentEssentia=Math.min(0,currentEssentia-EssentiaRate);
        currentVis=Math.min(0,currentVis-VisRate);
    }

    private void getPower() {
        if(this.count % 10==0) {
            if (this.currentVis < VisCapacity) {
                currentVis += this.consumeVis(Aspect.FIRE, 1);

            }
        }
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



    @Override
    public boolean isConnectable(ForgeDirection forgeDirection) {
        return forgeDirection==ForgeDirection.DOWN;
    }

    @Override
    public boolean canInputFrom(ForgeDirection forgeDirection) {
        return forgeDirection==ForgeDirection.DOWN;
    }

    @Override
    public boolean canOutputTo(ForgeDirection forgeDirection) {
        return false;
    }

    @Override
    public void setSuction(Aspect aspect, int i) {

    }

    @Override
    public Aspect getSuctionType(ForgeDirection forgeDirection) {
        return (forgeDirection==ForgeDirection.DOWN)?Aspect.DEATH:null;
    }

    @Override
    public int getSuctionAmount(ForgeDirection forgeDirection) {
        return (forgeDirection==ForgeDirection.DOWN)?128:0;
    }

    @Override
    public int takeEssentia(Aspect aspect, int i, ForgeDirection forgeDirection) {
        return 0;
    }

    @Override
    public int addEssentia(Aspect aspect, int i, ForgeDirection forgeDirection) {
        int newEssentia=0;
        int filled = EssentiaCapacity - currentEssentia;
        if (i < filled)
        {
            currentEssentia += i;
            filled = i;
        }
        else
        {
            currentEssentia = EssentiaCapacity;
        }
        return filled;
    }

    @Override
    public Aspect getEssentiaType(ForgeDirection forgeDirection) {
        return (forgeDirection==ForgeDirection.DOWN)?Aspect.DEATH:null;
    }

    @Override
    public int getEssentiaAmount(ForgeDirection forgeDirection) {
        return 0;
    }

    @Override
    public int getMinimumSuction() {
        return 0;
    }

    @Override
    public boolean renderExtendedTube() {
        return false;
    }
}
