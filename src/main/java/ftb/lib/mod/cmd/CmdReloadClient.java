package ftb.lib.mod.cmd;

import ftb.lib.api.cmd.*;
import ftb.lib.mod.client.FTBLibModClient;
import ftb.lib.mod.net.MessageReload;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.IChatComponent;
import net.minecraftforge.fml.relauncher.*;

@SideOnly(Side.CLIENT)
public class CmdReloadClient extends CommandLM
{
	public CmdReloadClient()
	{ super(FTBLibModClient.reload_client_cmd.get(), CommandLevel.OP); }
	
	public IChatComponent onCommand(ICommandSender ics, String[] args)
	{
		MessageReload.reloadClient(0L, true);
		return null;
	}
}