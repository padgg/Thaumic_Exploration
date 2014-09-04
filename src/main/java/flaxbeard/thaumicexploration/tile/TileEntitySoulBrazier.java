package flaxbeard.thaumicexploration.tile;

import com.mojang.authlib.GameProfile;
import flaxbeard.thaumicexploration.ThaumicExploration;
import flaxbeard.thaumicexploration.chunkLoader.ITXChunkLoader;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.world.ChunkCoordIntPair;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraftforge.common.ForgeChunkManager;
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
public class TileEntitySoulBrazier extends TileVisRelay  implements IEssentiaTransport,ITXChunkLoader {


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
    public ForgeChunkManager.Ticket heldChunk;
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
        if(this.count % 60==0) {
        	int counter = 0;
        	int tX = 0;
        	int tY = 0;
        	int tZ = 0;
        	boolean found = false;
        	while (counter < 25 && !found) {
	        	tX = xCoord - 3 + worldObj.rand.nextInt(7);
	        	tY = yCoord - 3 + worldObj.rand.nextInt(7);
	        	tZ = zCoord - 3 + worldObj.rand.nextInt(7);
	        	counter++;
	        	if ((worldObj.isAirBlock(tX, tY+1, tZ) && !worldObj.isAirBlock(tX, tY, tZ))) {
	        		found = true;
	        		float offsetY = (float) (Math.sin(Math.toRadians(count*1.0F))/4.0F);
	        		float offsetZ = (float) (Math.sin(Math.toRadians(count*3.0F))/4.0F);
	        		float offsetX = (float) (Math.cos(Math.toRadians(count*3.0F))/4.0F);
	        
	        		ThaumicExploration.proxy.spawnLightningBolt(worldObj, tX, tY, tZ, xCoord+0.5F+offsetX, yCoord+1.5F+offsetY, zCoord+offsetZ+0.5F);
	        	}
        	}
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

    public boolean checkPower() {
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
    @Override
    public void invalidate()
    {
        this.forceChunkLoading(null);
    }
    @Override
    public void validate()
    {
        this.forceChunkLoading(null);
    }
    @Override
    public void forceChunkLoading(ForgeChunkManager.Ticket ticket) {
        if (ticket != null)
        {
            this.heldChunk = ticket;
            ForgeChunkManager.forceChunk(this.heldChunk, new ChunkCoordIntPair(this.xCoord >> 4, this.zCoord >> 4));
        }
        else
        {
            if (this.heldChunk == null)
            {
                ForgeChunkManager.Ticket newTicket = ForgeChunkManager.requestTicket(ThaumicExploration.instance, this.worldObj, ForgeChunkManager.Type.NORMAL);
                newTicket.getModData().setInteger("xCoord", this.xCoord);
                newTicket.getModData().setInteger("yCoord", this.yCoord);
                newTicket.getModData().setInteger("zCoord", this.zCoord);
                this.heldChunk = newTicket;
                ForgeChunkManager.forceChunk(this.heldChunk, new ChunkCoordIntPair(this.xCoord >> 4, this.zCoord >> 4));
            }
            else
            {
                ForgeChunkManager.releaseTicket(this.heldChunk);
                this.heldChunk = null;
            }
        }
    }
}
