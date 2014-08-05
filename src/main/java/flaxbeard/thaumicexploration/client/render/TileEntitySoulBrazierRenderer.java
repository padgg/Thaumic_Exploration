package flaxbeard.thaumicexploration.client.render;

import flaxbeard.thaumicexploration.client.render.model.ModelCandle;
import flaxbeard.thaumicexploration.client.render.model.ModelSoulBrazier;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;
import thaumcraft.common.config.ConfigBlocks;

import java.awt.*;

/**
 * Created by nekosune on 03/08/14.
 */
public class TileEntitySoulBrazierRenderer extends TileEntitySpecialRenderer {
    private ModelSoulBrazier brazierModel = new ModelSoulBrazier();
    private static final ResourceLocation baseTexture = new ResourceLocation("thaumicexploration:textures/models/soulBrazier.png");

    @Override
    public void renderTileEntityAt(TileEntity tileentity, double d0, double d1, double d2, float p_147500_8_) {
        // TODO Auto-generated method stub

        GL11.glPushMatrix();
        GL11.glTranslatef((float)d0 + 0.5F, (float)d1+1.5f, (float)d2 + 0.5F);
        GL11.glRotatef(180.0F, 1.0F, 0.0F, 0.0F);

        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);

        this.bindTexture(baseTexture);
        this.brazierModel.render(null,0.0f,0.0f,-0.1f,0.0f,0.0f,0.0625f);
        //this.brazierModel.renderAll(0.0625F);
        GL11.glPopMatrix();
    }
}
