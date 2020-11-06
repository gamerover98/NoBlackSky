package it.gamerover.nbs;

import com.comphenix.packetwrapper.WrapperPlayServerLogin;
import com.comphenix.packetwrapper.WrapperPlayServerRespawn;
import it.gamerover.nbs.configuration.ConfigManager;
import org.bukkit.World;
import org.bukkit.WorldType;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;

import java.util.Set;

/**
 * 
 * @author gamerover98
 *
 */
public class NBS_PacketAdapter extends PacketAdapter {

	public NBS_PacketAdapter(Plugin plugin) {
		super(plugin, WrapperPlayServerLogin.TYPE, WrapperPlayServerRespawn.TYPE);
	}

	/*
	 * This method lets to edit the worldType of a world when the player join in the server,
	 * respawn or change world.
	 */
	@Override
	public void onPacketSending(PacketEvent event) {

		PacketType packetType = event.getPacketType();
		Player player = event.getPlayer();

		if (player == null) {
			return;
		}

		World world = player.getWorld();
		WorldType worldType = WorldType.FLAT;
		PacketContainer packet = event.getPacket();

		Set<String> worlds = ConfigManager.getWorlds();

		if (worlds.contains(world.getName())) {

			if (packetType == WrapperPlayServerLogin.TYPE) { //If packet class equals PACKET_PLAY_OUT_LOGIN

				WrapperPlayServerLogin wrapperPlayServerLogin = new WrapperPlayServerLogin(packet);
				wrapperPlayServerLogin.setLevelType(worldType);

			} else if (packetType == WrapperPlayServerRespawn.TYPE) {  //Else if packet class equals PACKET_PLAY_OUT_RESPAWN

				WrapperPlayServerRespawn wrapperPlayServerRespawn = new WrapperPlayServerRespawn(packet);
				wrapperPlayServerRespawn.setLevelType(worldType);

			}

		}

	}

}
