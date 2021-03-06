package ftb.lib.mod.client.gui;

import ftb.lib.api.PlayerAction;
import ftb.lib.api.friends.ILMPlayer;
import ftb.lib.api.gui.GuiLM;
import ftb.lib.api.gui.widgets.*;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraftforge.fml.relauncher.*;

import java.util.List;

@SideOnly(Side.CLIENT)
public class GuiPlayerActions extends GuiLM
{
	public final ILMPlayer self, other;
	public final List<PlayerAction> actions;
	
	public GuiPlayerActions(ILMPlayer s, ILMPlayer o, List<PlayerAction> a)
	{
		super(null, null);
		self = s;
		other = s;
		actions = a;
		hideNEI = true;
	}
	
	public void addWidgets()
	{
		mainPanel.width = 0;
		mainPanel.height = 0;
		
		for(int i = 0; i < actions.size(); i++)
		{
			ButtonPlayerActionSmall b = new ButtonPlayerActionSmall(this, actions.get(i));
			mainPanel.add(b);
			mainPanel.width = Math.max(mainPanel.width, b.width);
			if(i != actions.size() - 1) mainPanel.height += b.height + 4;
		}
		
		for(WidgetLM w : mainPanel.widgets)
			w.width = mainPanel.width;
		
		xSize = mainPanel.width;
		ySize = mainPanel.height;
		mainPanel.posX = guiLeft = (width - xSize) / 2;
		mainPanel.posY = guiTop = (height - ySize) / 2 - 20;
	}
	
	public void drawBackground()
	{
		for(WidgetLM w : mainPanel.widgets)
			w.renderWidget();
	}
	
	public static class ButtonPlayerActionSmall extends ButtonLM
	{
		public final GuiPlayerActions gui;
		public final PlayerAction action;
		
		public ButtonPlayerActionSmall(GuiPlayerActions g, PlayerAction a)
		{
			super(g, 0, g.mainPanel.height, 0, 18);
			gui = g;
			action = a;
			title = a.getDisplayName();
			width = 22 + g.getFontRenderer().getStringWidth(title);
		}
		
		public void onButtonPressed(int b)
		{
			gui.container.player.closeScreen();
			action.onClicked(gui.self, gui.other);
		}
		
		public void renderWidget()
		{
			int ax = getAX();
			int ay = getAY();
			
			GlStateManager.color(0.46F, 0.46F, 0.46F, 0.53F);
			GuiLM.drawBlankRect(ax, ay, gui.getZLevel(), width, height);
			gui.render(action.icon, ax + 1, ay + 1);
			
			gui.getFontRenderer().drawString(title, ax + 20, ay + 6, 0xFFFFFFFF);
			GlStateManager.color(1F, 1F, 1F, 0.2F);
			if(mouseOver(ax, ay)) GuiLM.drawBlankRect(ax, ay, gui.getZLevel(), width, height);
			GlStateManager.color(1F, 1F, 1F, 1F);
		}
		
		public void addMouseOverText(List<String> l)
		{
		}
	}
}