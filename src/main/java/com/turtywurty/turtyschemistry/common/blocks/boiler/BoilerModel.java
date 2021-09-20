package com.turtywurty.turtyschemistry.common.blocks.boiler;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;

import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.model.Model;
import net.minecraft.client.renderer.model.ModelRenderer;

public class BoilerModel extends Model {
    private final ModelRenderer base;
    private final ModelRenderer hatch;

    public BoilerModel() {
        super(RenderType::getEntityTranslucent);
        this.textureWidth = 64;
        this.textureHeight = 64;

        this.base = new ModelRenderer(this);
        this.base.setRotationPoint(-9.0F, 7.0F, 8.0F);
        setRotationAngle(this.base, 3.1416F, 0.0F, 0.0F);
        this.base.setTextureOffset(0, 3).addBox(-6.0F, 6.0F, -6.0F, 14.0F, 1.0F, 2.0F, 0.0F, false);
        this.base.setTextureOffset(0, 0).addBox(-6.0F, 6.0F, 5.0F, 14.0F, 1.0F, 2.0F, 0.0F, false);
        this.base.setTextureOffset(10, 46).addBox(6.0F, 4.0F, -5.0F, 1.0F, 2.0F, 2.0F, 0.0F, false);
        this.base.setTextureOffset(42, 45).addBox(-5.0F, 4.0F, -5.0F, 1.0F, 2.0F, 2.0F, 0.0F, false);
        this.base.setTextureOffset(6, 44).addBox(6.0F, 4.0F, 4.0F, 1.0F, 2.0F, 2.0F, 0.0F, false);
        this.base.setTextureOffset(0, 44).addBox(-5.0F, 4.0F, 4.0F, 1.0F, 2.0F, 2.0F, 0.0F, false);
        this.base.setTextureOffset(8, 38).addBox(-7.0F, -2.0F, -4.0F, 1.0F, 3.0F, 3.0F, 0.0F, false);
        this.base.setTextureOffset(43, 41).addBox(-7.0F, -4.0F, -3.0F, 1.0F, 2.0F, 2.0F, 0.0F, false);
        this.base.setTextureOffset(0, 32).addBox(-7.0F, -5.0F, -1.0F, 1.0F, 9.0F, 3.0F, 0.0F, false);
        this.base.setTextureOffset(38, 43).addBox(-7.0F, -4.0F, 2.0F, 1.0F, 2.0F, 2.0F, 0.0F, false);
        this.base.setTextureOffset(37, 37).addBox(-7.0F, -2.0F, 2.0F, 1.0F, 3.0F, 3.0F, 0.0F, false);
        this.base.setTextureOffset(32, 43).addBox(-7.0F, 1.0F, 2.0F, 1.0F, 2.0F, 2.0F, 0.0F, false);
        this.base.setTextureOffset(26, 43).addBox(-7.0F, 1.0F, -3.0F, 1.0F, 2.0F, 2.0F, 0.0F, false);
        this.base.setTextureOffset(24, 47).addBox(7.0F, -2.0F, 5.0F, 1.0F, 3.0F, 1.0F, 0.0F, false);
        this.base.setTextureOffset(26, 26).addBox(-5.0F, -2.0F, 6.0F, 12.0F, 3.0F, 1.0F, 0.0F, false);
        this.base.setTextureOffset(46, 32).addBox(-6.0F, -2.0F, 5.0F, 1.0F, 3.0F, 1.0F, 0.0F, false);
        this.base.setTextureOffset(47, 48).addBox(-6.0F, -4.0F, 4.0F, 1.0F, 2.0F, 1.0F, 0.0F, false);
        this.base.setTextureOffset(27, 19).addBox(-5.0F, -4.0F, 5.0F, 12.0F, 2.0F, 1.0F, 0.0F, false);
        this.base.setTextureOffset(4, 48).addBox(7.0F, -4.0F, 4.0F, 1.0F, 2.0F, 1.0F, 0.0F, false);
        this.base.setTextureOffset(45, 0).addBox(7.0F, -5.0F, 2.0F, 1.0F, 1.0F, 2.0F, 0.0F, false);
        this.base.setTextureOffset(26, 30).addBox(-5.0F, -5.0F, 4.0F, 12.0F, 1.0F, 1.0F, 0.0F, false);
        this.base.setTextureOffset(21, 38).addBox(-6.0F, -5.0F, 2.0F, 1.0F, 1.0F, 2.0F, 0.0F, false);
        this.base.setTextureOffset(40, 0).addBox(-6.0F, -6.0F, -1.0F, 1.0F, 1.0F, 3.0F, 0.0F, false);
        this.base.setTextureOffset(0, 23).addBox(-5.0F, -6.0F, 2.0F, 12.0F, 1.0F, 2.0F, 0.0F, false);
        this.base.setTextureOffset(29, 39).addBox(7.0F, -6.0F, -1.0F, 1.0F, 1.0F, 3.0F, 0.0F, false);
        this.base.setTextureOffset(0, 10).addBox(-5.0F, -7.0F, -1.0F, 12.0F, 1.0F, 3.0F, 0.0F, false);
        this.base.setTextureOffset(0, 48).addBox(-6.0F, 1.0F, 4.0F, 1.0F, 2.0F, 1.0F, 0.0F, false);
        this.base.setTextureOffset(27, 16).addBox(-5.0F, 1.0F, 5.0F, 12.0F, 2.0F, 1.0F, 0.0F, false);
        this.base.setTextureOffset(47, 39).addBox(7.0F, 1.0F, 4.0F, 1.0F, 2.0F, 1.0F, 0.0F, false);
        this.base.setTextureOffset(13, 38).addBox(7.0F, 3.0F, 2.0F, 1.0F, 1.0F, 2.0F, 0.0F, false);
        this.base.setTextureOffset(0, 30).addBox(-5.0F, 3.0F, 4.0F, 12.0F, 1.0F, 1.0F, 0.0F, false);
        this.base.setTextureOffset(37, 0).addBox(-6.0F, 3.0F, 2.0F, 1.0F, 1.0F, 2.0F, 0.0F, false);
        this.base.setTextureOffset(24, 38).addBox(-6.0F, 4.0F, -1.0F, 1.0F, 1.0F, 3.0F, 0.0F, false);
        this.base.setTextureOffset(0, 20).addBox(-5.0F, 4.0F, 2.0F, 12.0F, 1.0F, 2.0F, 0.0F, false);
        this.base.setTextureOffset(0, 6).addBox(-5.0F, 5.0F, -1.0F, 12.0F, 1.0F, 3.0F, 0.0F, false);
        this.base.setTextureOffset(16, 38).addBox(7.0F, 4.0F, -1.0F, 1.0F, 1.0F, 3.0F, 0.0F, false);
        this.base.setTextureOffset(0, 17).addBox(-5.0F, 4.0F, -3.0F, 12.0F, 1.0F, 2.0F, 0.0F, false);
        this.base.setTextureOffset(29, 32).addBox(7.0F, 3.0F, -3.0F, 1.0F, 1.0F, 2.0F, 0.0F, false);
        this.base.setTextureOffset(47, 3).addBox(7.0F, 1.0F, -4.0F, 1.0F, 2.0F, 1.0F, 0.0F, false);
        this.base.setTextureOffset(20, 46).addBox(7.0F, -2.0F, -5.0F, 1.0F, 3.0F, 1.0F, 0.0F, false);
        this.base.setTextureOffset(36, 47).addBox(7.0F, -4.0F, -4.0F, 1.0F, 2.0F, 1.0F, 0.0F, false);
        this.base.setTextureOffset(21, 32).addBox(7.0F, -5.0F, -3.0F, 1.0F, 1.0F, 2.0F, 0.0F, false);
        this.base.setTextureOffset(0, 14).addBox(-5.0F, -6.0F, -3.0F, 12.0F, 1.0F, 2.0F, 0.0F, false);
        this.base.setTextureOffset(26, 23).addBox(-5.0F, -5.0F, -4.0F, 12.0F, 1.0F, 1.0F, 0.0F, false);
        this.base.setTextureOffset(27, 10).addBox(-5.0F, -4.0F, -5.0F, 12.0F, 2.0F, 1.0F, 0.0F, false);
        this.base.setTextureOffset(0, 26).addBox(-5.0F, -2.0F, -6.0F, 12.0F, 3.0F, 1.0F, 0.0F, false);
        this.base.setTextureOffset(27, 6).addBox(-5.0F, 1.0F, -5.0F, 12.0F, 2.0F, 1.0F, 0.0F, false);
        this.base.setTextureOffset(26, 14).addBox(-5.0F, 3.0F, -4.0F, 12.0F, 1.0F, 1.0F, 0.0F, false);
        this.base.setTextureOffset(13, 32).addBox(-6.0F, 3.0F, -3.0F, 1.0F, 1.0F, 2.0F, 0.0F, false);
        this.base.setTextureOffset(32, 47).addBox(-6.0F, 1.0F, -4.0F, 1.0F, 2.0F, 1.0F, 0.0F, false);
        this.base.setTextureOffset(16, 46).addBox(-6.0F, -2.0F, -5.0F, 1.0F, 3.0F, 1.0F, 0.0F, false);
        this.base.setTextureOffset(28, 47).addBox(-6.0F, -4.0F, -4.0F, 1.0F, 2.0F, 1.0F, 0.0F, false);
        this.base.setTextureOffset(5, 32).addBox(-6.0F, -5.0F, -3.0F, 1.0F, 1.0F, 2.0F, 0.0F, false);

        this.hatch = new ModelRenderer(this);
        this.hatch.setRotationPoint(8.0F, 0.0F, 5.0F);
        this.base.addChild(this.hatch);
        this.hatch.setTextureOffset(1, 37).addBox(0.05F, -2.0F, 0.0F, 0.0F, 3.0F, 0.0F, 0.0F, false);
        this.hatch.setTextureOffset(40, 32).addBox(0.0F, 1.0F, -3.0F, 1.0F, 2.0F, 2.0F, 0.0F, false);
        this.hatch.setTextureOffset(32, 0).addBox(0.0F, -2.0F, -3.0F, 1.0F, 3.0F, 3.0F, 0.0F, false);
        this.hatch.setTextureOffset(42, 36).addBox(0.0F, -4.0F, -3.0F, 1.0F, 2.0F, 2.0F, 0.0F, false);
        this.hatch.setTextureOffset(32, 32).addBox(0.0F, -5.0F, -6.0F, 1.0F, 3.0F, 3.0F, 0.0F, false);
        this.hatch.setTextureOffset(8, 32).addBox(0.0F, -2.0F, -6.0F, 1.0F, 3.0F, 3.0F, 0.0F, false);
        this.hatch.setTextureOffset(24, 32).addBox(0.0F, 1.0F, -6.0F, 1.0F, 3.0F, 3.0F, 0.0F, false);
        this.hatch.setTextureOffset(16, 32).addBox(0.0F, -2.0F, -9.0F, 1.0F, 3.0F, 3.0F, 0.0F, false);
        this.hatch.setTextureOffset(20, 42).addBox(0.0F, -4.0F, -8.0F, 1.0F, 2.0F, 2.0F, 0.0F, false);
        this.hatch.setTextureOffset(14, 42).addBox(0.0F, 1.0F, -8.0F, 1.0F, 2.0F, 2.0F, 0.0F, false);
    }

    public ModelRenderer getBase() {
        return this.base;
    }

    public ModelRenderer getHatch() {
        return this.hatch;
    }

    @Override
    public void render(final MatrixStack matrixStack, final IVertexBuilder buffer, final int packedLight,
            final int packedOverlay, final float red, final float green, final float blue,
            final float alpha) {
        this.base.render(matrixStack, buffer, packedLight, packedOverlay);
    }

    public void setRotationAngle(final ModelRenderer modelRenderer, final float x, final float y,
            final float z) {
        modelRenderer.rotateAngleX = x;
        modelRenderer.rotateAngleY = y;
        modelRenderer.rotateAngleZ = z;
    }
}