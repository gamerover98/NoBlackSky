package it.gamerover.nbs;

import com.comphenix.packetwrapper.WrapperPlayServerLogin;
import com.comphenix.packetwrapper.WrapperPlayServerRespawn;
import org.bukkit.World;
import org.bukkit.WorldType;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;

import it.gamerover.nbs.configuration.NBS_Configuration;

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
		String world_name = world.getName();
		WorldType world_type = WorldType.FLAT;
		String world_generator_package_name = "";

		try {
			world_generator_package_name = world.getGenerator().getClass().getPackage().getName();
		} catch (NullPointerException nex) {
		}

		NBS_Configuration config = NoBlackSky.getConfiguration();

		//Check if paradise_land is true in the config.yml
		if (config.isParadise_land()) {

			//check if world generator package name is not equal to ParadiseLand
			if (!world_generator_package_name.equals("it.gamerover.paradise") || !world_generator_package_name.equals("me.gamerover.paradise")) {

				//Check if world name is contained in the blacklist in the config.yml
				if (config.contains_blacklist_world(world_name)) {
					return;
				}

			}

		} else {

			//Check if world name is contained in the blacklist in the config.yml
			if (config.contains_blacklist_world(world_name)) {
				return;
			}

		}

		PacketContainer packet = event.getPacket();

		if (packetType == WrapperPlayServerLogin.TYPE) { //If packet class equals PACKET_PLAY_OUT_LOGIN

			WrapperPlayServerLogin wrapperPlayServerLogin = new WrapperPlayServerLogin(packet);
			wrapperPlayServerLogin.setLevelType(world_type);

		} else if (packetType == WrapperPlayServerRespawn.TYPE) {  //Else if packet class equals PACKET_PLAY_OUT_RESPAWN

			WrapperPlayServerRespawn wrapperPlayServerRespawn = new WrapperPlayServerRespawn(packet);
			wrapperPlayServerRespawn.setLevelType(world_type);

		}

	}

}
