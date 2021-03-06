package ftb.lib.mod.client;

import ftb.lib.*;
import ftb.lib.api.*;
import ftb.lib.api.client.*;
import ftb.lib.api.friends.ILMPlayer;
import ftb.lib.api.gui.PlayerActionRegistry;
import ftb.lib.api.item.*;
import ftb.lib.mod.FTBLibFinals;
import ftb.lib.mod.client.gui.GuiPlayerActions;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.event.entity.player.*;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import net.minecraftforge.fml.common.network.FMLNetworkEvent;
import net.minecraftforge.fml.relauncher.*;
import org.lwjgl.input.Keyboard;

import java.util.*;

@SideOnly(Side.CLIENT)
public class FTBLibClientEventHandler
{
	public static final FTBLibClientEventHandler instance = new FTBLibClientEventHandler();
	
	@SubscribeEvent
	public void onConnected(FMLNetworkEvent.ClientConnectedToServerEvent e)
	{
		ServerData sd = FTBLibClient.mc.getCurrentServerData();
		String s = (sd == null || sd.serverIP.isEmpty()) ? "localhost" : sd.serverIP.replace('.', '_');
		FTBWorld.client = new FTBWorld(Side.CLIENT, new UUID(0L, 0L), s);
		
		EventFTBWorldClient event = new EventFTBWorldClient(FTBWorld.client, true);
		if(FTBLib.ftbu != null) FTBLib.ftbu.onFTBWorldClient(event);
		event.post();
	}
	
	@SubscribeEvent
	public void onDisconnected(FMLNetworkEvent.ClientDisconnectionFromServerEvent e)
	{
		EventFTBWorldClient event = new EventFTBWorldClient(null, false);
		if(FTBLib.ftbu != null) FTBLib.ftbu.onFTBWorldClient(event);
		event.post();
		FTBWorld.client = null;
	}
	
	@SubscribeEvent
	public void onTooltip(ItemTooltipEvent e)
	{
		if(e.itemStack == null || e.itemStack.getItem() == null) return;
		
		if(FTBLibModClient.item_reg_names.get())
		{
			e.toolTip.add(LMInvUtils.getRegName(e.itemStack).toString());
		}
		
		if(FTBLibModClient.item_ore_names.get())
		{
			List<String> ores = ODItems.getOreNames(e.itemStack);
			
			if(ores != null && !ores.isEmpty())
			{
				e.toolTip.add("Ore Dictionary names:");
				for(String or : ores)
					e.toolTip.add("> " + or);
			}
		}
		
		if(FTBLib.ftbu != null) FTBLib.ftbu.onTooltip(e);
	}
	
	@SubscribeEvent
	public void onDrawDebugText(RenderGameOverlayEvent.Text e)
	{
		if(!FTBLibClient.mc.gameSettings.showDebugInfo)
		{
			if(FTBLibFinals.DEV)
			{
				e.left.add("[MC " + EnumChatFormatting.GOLD + Loader.MC_VERSION + EnumChatFormatting.WHITE + " DevEnv]");
				DevConsole.text.set("MC", Loader.MC_VERSION + ", " + FTBLibClient.mc.debug);
			}
		}
		else
		{
			//if(DevConsole.enabled()) e.left.add("r: " + MathHelperMC.get2DRotation(FTBLibClient.mc.thePlayer));
		}
	}
	
	@SubscribeEvent
	public void onEntityRightClick(EntityInteractEvent e)
	{
		if(e.entity.worldObj.isRemote && FTBLibClient.isIngame() && FTBLibModClient.player_options_shortcut.get() && e.entityPlayer.getGameProfile().getId().equals(FTBLibClient.mc.thePlayer.getGameProfile().getId()))
		{
			ILMPlayer self = FTBLibClient.getClientLMPlayer();
			ILMPlayer other = (FTBLib.ftbu == null) ? new TempLMPlayerFromEntity(Side.CLIENT, ((EntityPlayer) e.target)) : FTBLib.ftbu.getLMPlayer(e.target);
			if(other != null)
			{
				List<PlayerAction> a = PlayerActionRegistry.getPlayerActions(PlayerAction.Type.OTHER, self, other, true);
				if(!a.isEmpty()) FTBLibClient.mc.displayGuiScreen(new GuiPlayerActions(self, other, a));
			}
		}
	}
	
	@SubscribeEvent
	public void onKeyEvent(InputEvent.KeyInputEvent e)
	{
		if(Keyboard.getEventKeyState())
		{
			int key = Keyboard.getEventKey();
			
			try
			{
				for(Shortcuts.KeyAction a : Shortcuts.keys)
				{
					if(a.key == key) a.action.onClicked(a.data);
				}
			}
			catch(Exception ex)
			{
				ex.printStackTrace();
			}
		}
	}
}