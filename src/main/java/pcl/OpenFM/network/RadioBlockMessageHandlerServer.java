package pcl.OpenFM.network;

import net.minecraft.server.MinecraftServer;
import net.minecraft.world.WorldServer;
import pcl.OpenFM.TileEntity.TileEntityRadio;
import pcl.OpenFM.network.Message.MessageTERadioBlock;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;

public class RadioBlockMessageHandlerServer implements IMessageHandler<MessageTERadioBlock, IMessage>
{
	public IMessage onMessage(MessageTERadioBlock message, MessageContext ctx)
	{
		PacketHandler.INSTANCE.sendToAll(message);
		WorldServer targetWorld = null;
		net.minecraft.tileentity.TileEntity tileEntity = null;
		WorldServer[] ws = MinecraftServer.getServer().worldServers;
		for (WorldServer s : ws) {
			if (s.provider.dimensionId == message.dim) {
				targetWorld = s;
				tileEntity = s.getTileEntity((int)message.x, (int)message.y, (int)message.z);
			}
		}



		if ((tileEntity instanceof TileEntityRadio)) {    	 
			if (message.mode == 15) {
				((TileEntityRadio)tileEntity).addSpeaker(targetWorld, message.tx, message.ty, message.tz);
				return null;
			}

			if (message.mode == 42) {
				((TileEntityRadio)tileEntity).addStation(message.streamURL);
				((TileEntityRadio)tileEntity).setStationCount(((TileEntityRadio)tileEntity).stations.size());
			}

			if (message.mode == 43) {
				((TileEntityRadio)tileEntity).delStation(message.streamURL);
				((TileEntityRadio)tileEntity).setStationCount(((TileEntityRadio)tileEntity).stations.size());
			}

			if (message.mode == 44 || message.mode == 47) {
				((TileEntityRadio)tileEntity).isLocked = true;
			}

			if (message.mode == 45 || message.mode == 46) {
				((TileEntityRadio)tileEntity).isLocked = false;
			}

			if (message.mode == 48) {
				((TileEntityRadio)tileEntity).setScreenColor(message.screenColor);
			}

			if (message.mode == 49) {
				((TileEntityRadio)tileEntity).setScreenText(message.screenText);
			}

			if (message.mode == 50) {
				((TileEntityRadio)tileEntity).writeDataToCard();
			}

			if (message.mode == 51) {
				((TileEntityRadio)tileEntity).readDataFromCard();
			}

			if ((message.mode == 11) || (message.mode == 14)) {
				((TileEntityRadio)tileEntity).listenToRedstone = true;
			}
			if ((message.mode == 12) || (message.mode == 13)) {
				((TileEntityRadio)tileEntity).listenToRedstone = false;
			}

			if ((message.volume > 0.0F) && (message.volume <= 1.0F)) {
				((TileEntityRadio)tileEntity).volume = message.volume;
			}


			((TileEntityRadio)tileEntity).streamURL = message.streamURL;

			if ((message.mode == 1) || (message.mode == 13) || (message.mode == 14)) {
				((TileEntityRadio)tileEntity).isPlaying = message.isPlaying;
				if (message.isPlaying)
				{
					if(((TileEntityRadio)tileEntity).isValid) {
						try {
							((TileEntityRadio)tileEntity).startStream();
						} catch (Exception e) {
							((TileEntityRadio)tileEntity).stopStream();
						}
					}
				}
				else
				{
					((TileEntityRadio)tileEntity).stopStream();
				}
			}
		}

		return null;
	}
}


