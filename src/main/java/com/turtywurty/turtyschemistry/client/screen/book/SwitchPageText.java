package com.turtywurty.turtyschemistry.client.screen.book;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import com.turtywurty.turtyschemistry.client.util.ClientUtils;

import net.minecraft.client.gui.widget.Widget;
import net.minecraft.util.text.StringTextComponent;

public class SwitchPageText extends Widget {

	private final int newPage, color;
	private final GuideBookScreen screen;

	public SwitchPageText(final GuideBookScreen screenIn, final int xIn, final int yIn, final int widthIn,
			final int heightIn, final String msgIn, final int newPageIn, final int colorIn) {
		super(xIn, yIn, widthIn, heightIn, new StringTextComponent("" + 5));
		this.newPage = newPageIn;
		this.screen = screenIn;
		this.color = colorIn;

		if (this.screen.currentPage == newPageIn || this.newPage >= this.screen.maxPages || this.newPage < 0) {
			this.active = false;
			this.visible = false;
		} else {
			this.width = ClientUtils.MC.fontRenderer.getStringPropertyWidth(getMessage());
		}
	}

	@Override
	public void onClick(final double mouseX, final double mouseY) {
		super.onClick(mouseX, mouseY);
		this.screen.currentPage = this.newPage;
	}

	@Override
	public void render(final MatrixStack stack, final int mouseX, final int mouseY, final float partialTicks) {
		if (this.visible) {
			RenderSystem.color4f(1.0f, 1.0f, 1.0f, 1.0f);
			ClientUtils.MC.fontRenderer.drawString(stack, getMessage().getString(), this.x, this.y, this.color);
		}
	}
}
