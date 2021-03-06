package ftb.lib.notification;

import com.google.gson.JsonElement;
import ftb.lib.api.client.FTBLibClient;
import ftb.lib.api.gui.GuiScreenRegistry;
import latmod.lib.LMUtils;
import latmod.lib.util.FinalIDObject;
import net.minecraft.client.gui.*;
import net.minecraftforge.fml.relauncher.*;

import java.io.File;
import java.net.URI;

public abstract class ClickAction extends FinalIDObject
{
	public ClickAction(String s)
	{
		super(s);
	}
	
	@SideOnly(Side.CLIENT)
	public abstract void onClicked(JsonElement data);
	
	// Static //
	
	public static final ClickAction CMD = new ClickAction("cmd")
	{
		@SideOnly(Side.CLIENT)
		public void onClicked(JsonElement data)
		{ FTBLibClient.execClientCommand("/" + data.getAsString()); }
	};
	
	public static final ClickAction SHOW_CMD = new ClickAction("show_cmd")
	{
		@SideOnly(Side.CLIENT)
		public void onClicked(JsonElement data)
		{ FTBLibClient.mc.displayGuiScreen(new GuiChat(data.getAsString())); }
	};
	
	public static final ClickAction URL = new ClickAction("url")
	{
		@SideOnly(Side.CLIENT)
		public void onClicked(JsonElement data)
		{
			try { LMUtils.openURI(new URI(data.getAsString())); }
			catch(Exception ex) { ex.printStackTrace(); }
		}
	};
	
	public static final ClickAction FILE = new ClickAction("file")
	{
		@SideOnly(Side.CLIENT)
		public void onClicked(JsonElement data)
		{
			try { LMUtils.openURI(new File(data.getAsString()).toURI()); }
			catch(Exception ex) { ex.printStackTrace(); }
		}
	};
	
	public static final ClickAction GUI = new ClickAction("gui")
	{
		@SideOnly(Side.CLIENT)
		public void onClicked(JsonElement data)
		{
			GuiScreen gui = GuiScreenRegistry.openGui(FTBLibClient.mc.thePlayer, data.getAsString());
			if(gui != null) FTBLibClient.mc.displayGuiScreen(gui);
		}
	};
}