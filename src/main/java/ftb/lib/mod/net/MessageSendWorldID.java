package ftb.lib.mod.net;

import ftb.lib.*;
import ftb.lib.api.EventFTBWorldClient;
import ftb.lib.api.net.*;
import latmod.lib.ByteCount;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.network.simpleimpl.*;
import net.minecraftforge.fml.relauncher.Side;

import java.util.UUID;

public class MessageSendWorldID extends MessageLM
{
	public MessageSendWorldID() { super(ByteCount.INT); }
	
	public MessageSendWorldID(FTBWorld w, EntityPlayerMP ep)
	{
		this();
		MessageReload.writeSyncedConfig(io);
		io.writeUUID(w.getWorldID());
		io.writeUTF(w.getWorldIDS());
		io.writeBoolean(FTBLib.ftbu != null);
		w.writeReloadData(io);
		if(FTBLib.ftbu != null) FTBLib.ftbu.writeWorldData(io, ep);
	}
	
	public LMNetworkWrapper getWrapper()
	{ return FTBLibNetHandler.NET; }
	
	public IMessage onMessage(MessageContext ctx)
	{
		MessageReload.readSyncedConfig(io);
		UUID id = io.readUUID();
		String ids = io.readUTF();
		boolean hasFTBU = io.readBoolean();
		
		boolean first = (FTBWorld.client == null || !FTBWorld.client.getWorldID().equals(id));
		if(first) FTBWorld.client = new FTBWorld(Side.CLIENT, id, ids);
		FTBWorld.client.readReloadData(io);
		new EventFTBWorldClient(FTBWorld.client, false).post();
		if(first && hasFTBU && FTBLib.ftbu != null) FTBLib.ftbu.readWorldData(io);
		
		MessageReload.reloadClient(0L, false);
		return null;
	}
}