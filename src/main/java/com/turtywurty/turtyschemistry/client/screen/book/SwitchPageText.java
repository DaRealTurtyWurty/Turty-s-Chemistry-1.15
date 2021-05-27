package com.turtywurty.turtyschemistry.client.screen.book;

import com.mojang.blaze3d.systems.RenderSystem;
import com.turtywurty.turtyschemistry.client.util.ClientUtils;

import net.minecraft.client.gui.widget.Widget;

public class SwitchPageText extends Widget {

	private int newPage, color;
	private GuideBookScreen screen;

	public SwitchPageText(GuideBookScreen screenIn, int xIn, int yIn, int widthIn, String msgIn, int newPageIn,
			int colorIn) {
		super(xIn, yIn, widthIn, 5, msgIn);
		this.newPage = newPageIn;
		this.screen = screenIn;
		this.color = colorIn;

		if (this.screen.currentPage == newPageIn || this.newPage >= this.screen.maxPages || this.newPage < 0) {
			this.active = false;
			this.visible = false;
		} else {
			this.width = ClientUtils.MC.fontRenderer.getStringWidth(this.getMessage());
		}
	}

	@Override
	public void onClick(double mouseX, double mouseY) {
		super.onClick(mouseX, mouseY);
		this.screen.currentPage = this.newPage;
	}

	@Override
	public void render(int mouseX, int mouseY, float partialTicks) {
		if (this.visible) {
			RenderSystem.color4f(1.0f, 1.0f, 1.0f, 1.0f);
			ClientUtils.MC.fontRenderer.drawString(this.getMessage(), this.x, this.y, this.color);
		}
	}
}
