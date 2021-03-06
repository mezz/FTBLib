package ftb.lib.mod.client.gui;

import ftb.lib.api.gui.*;
import ftb.lib.api.gui.callback.*;
import ftb.lib.api.gui.widgets.*;
import latmod.lib.PrimitiveType;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraftforge.fml.relauncher.*;

@SideOnly(Side.CLIENT)
public class GuiSelectField extends GuiLM
{
	public final Object ID;
	public final PrimitiveType type;
	public final String def;
	public final IFieldCallback callback;
	
	public final ButtonSimpleLM buttonCancel, buttonAccept;
	public final TextBoxLM textBox;
	
	public GuiSelectField(Object id, PrimitiveType typ, String d, IFieldCallback c)
	{
		super(null, null);
		hideNEI = true;
		ID = id;
		type = typ;
		def = d;
		callback = c;
		
		xSize = 100;
		ySize = 40;
		
		int bsize = xSize / 2 - 4;
		
		buttonCancel = new ButtonSimpleLM(this, 2, ySize - 18, bsize, 16)
		{
			public void onButtonPressed(int b)
			{
				callback.onFieldSelected(new FieldSelected(ID, false, def, true));
			}
		};
		
		buttonCancel.title = FTBLibLang.button_cancel();
		
		buttonAccept = new ButtonSimpleLM(this, xSize - bsize - 2, ySize - 18, bsize, 16)
		{
			public void onButtonPressed(int b)
			{
				callback.onFieldSelected(new FieldSelected(ID, true, textBox.text, true));
			}
		};
		
		buttonAccept.title = FTBLibLang.button_accept();
		
		textBox = new TextBoxLM(this, 2, 2, xSize - 4, 18)
		{
			public boolean canAddChar(char c)
			{ return super.canAddChar(c) && isCharValid(c); }
			
			public void returnPressed()
			{ buttonAccept.onButtonPressed(0); }
		};
		
		textBox.text = def;
	}
	
	private boolean isCharValid(char c)
	{
		if(type == PrimitiveType.STRING || type == PrimitiveType.INT || type == PrimitiveType.FLOAT)
		{
			if(type == PrimitiveType.STRING || c == '-') return true;
			else if(c == '.') return type == PrimitiveType.FLOAT;
			else return c >= '0' && c <= '9';
		}
		
		return false;
	}
	
	public GuiSelectField setCharLimit(int i)
	{
		textBox.charLimit = i;
		return this;
	}
	
	public void addWidgets()
	{
		mainPanel.add(buttonCancel);
		mainPanel.add(buttonAccept);
		mainPanel.add(textBox);
	}
	
	public void drawBackground()
	{
		int size = 8 + getFontRenderer().getStringWidth(textBox.text);
		if(size > xSize)
		{
			xSize = size;
			int bsize = xSize / 2 - 4;
			buttonAccept.width = buttonCancel.width = bsize;
			buttonAccept.posX = xSize - bsize - 2;
			textBox.width = xSize - 4;
			initGui();
		}
		
		GlStateManager.color(0.4F, 0.4F, 0.4F, 0.66F);
		drawBlankRect(guiLeft, guiTop, zLevel, xSize, ySize);
		GlStateManager.color(0.2F, 0.2F, 0.2F, 1F);
		drawBlankRect(textBox.getAX(), textBox.getAY(), zLevel, textBox.width, textBox.height);
		GlStateManager.color(1F, 1F, 1F, 1F);
		buttonAccept.renderWidget();
		buttonCancel.renderWidget();
		textBox.renderCentred(textBox.width / 2, 6, 0xFFEEEEEE);
	}
}