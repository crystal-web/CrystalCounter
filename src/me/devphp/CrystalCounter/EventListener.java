package me.devphp.CrystalCounter;

import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.entity.Wolf;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class EventListener implements Listener {
	private Core plugin;

	public EventListener(Core core) {
		this.plugin = core;
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void playerJoinEvent(PlayerJoinEvent event) {
		Player player = event.getPlayer();
		PlayerData data = new PlayerData();
		data.setPlayer(player);
		
		try {
			
			this.plugin.connection = DriverManager.getConnection(this.plugin.url, this.plugin.user, this.plugin.passwd);
			this.plugin.state = this.plugin.connection.createStatement();
		
			try {
				ResultSet res = this.plugin.state.executeQuery("SELECT * FROM "
						+ plugin.config.getConfig().getString(
								"config.database.tablename") + " WHERE uid = '"
						+ player.getUniqueId() + "'" + " LIMIT 1;");
				res.next();
				if (res.getString("id") != null) {
					data.setBlockbreak(res.getInt("blockbreak"));
					data.setBlockplaced(res.getInt("blockplaced"));
					data.setKill(res.getInt("kill"));
					data.setDeath(res.getInt("death"));
				}
			} catch (SQLException e) {
				this.plugin.state.execute("INSERT INTO `"
						+ plugin.config.getConfig().getString(
								"config.database.tablename")
						+ "` (`id`, `uid`, `uname`) VALUES (NULL, '"
						+ player.getUniqueId() + "', '" + player.getName()
						+ "');");
			}
			this.plugin.playerdata.put(player.getUniqueId().toString(), data);

		} catch (SQLException e) {
			e.printStackTrace();
		}
	} 

	@EventHandler
	public void playerDisconnect(PlayerQuitEvent event) {
		Player player = event.getPlayer();
		if (!this.plugin.playerdata.containsKey(player.getUniqueId().toString())) {
			return;
		}
		
		PlayerData data = this.plugin.playerdata.get(player.getUniqueId().toString());
	
		try {
			this.plugin.connection = DriverManager.getConnection(this.plugin.url, this.plugin.user, this.plugin.passwd);
			this.plugin.state = this.plugin.connection.createStatement();
		
			
			String query = "UPDATE `"
					+ plugin.config.getConfig().getString(
							"config.database.tablename") + "` SET "
					+ " `uname` =  '" + player.getName().toString() + "',"
					+ " `blockbreak` =  '" + data.getBlockbreak() + "',"
					+ " `blockplaced` =  '" + data.getBlockplaced() + "',"
					
					+ " `kill` =  '" + data.getKill() + "',"
					+ " `perfectKill` =  '" + data.getPerfectKill() + "',"
					+ " `death` =  '" + data.getDeath() + "',"
					+ " `connectTime` = `connectTime`+'" + data.getConnectionTime() + "' "
					+ " WHERE `uid` ='" + player.getUniqueId().toString() + "';";
			//this.plugin.getLogger().severe(query);
			this.plugin.state.execute(query);
			
			
			this.plugin.playerdata.remove(player.getUniqueId().toString());
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	@EventHandler
	public void onBreakBlockEvent(BlockBreakEvent event) {
		if (event.isCancelled()) {
			return;
		}
		Player player = event.getPlayer();
		PlayerData data = this.plugin.playerdata.get(player.getUniqueId()
				.toString());
		data.blockbreak();
		this.plugin.playerdata.put(player.getUniqueId().toString(), data);
	}

	@EventHandler
	public void onBreakPlaceEvent(BlockPlaceEvent event) {
		Player player = event.getPlayer();
		PlayerData data = this.plugin.playerdata.get(player.getUniqueId()
				.toString());
		data.blockplaced();
		this.plugin.playerdata.put(player.getUniqueId().toString(), data);
	}

	@EventHandler
	public void playerDeathEvent(PlayerDeathEvent event) {
		Entity ent = event.getEntity();
		if ((ent instanceof Player)) {
			Player p = (Player) ent;
			if (p.getKiller() != null) {
				Player k = p.getKiller();
				PlayerData data = this.plugin.playerdata.get(k.getUniqueId().toString());
				data.kill();
				this.plugin.playerdata.put(k.getUniqueId().toString(), data);
			}
			
			if (this.plugin.playerdata.containsKey(p.getUniqueId().toString())) {
				PlayerData data = this.plugin.playerdata.get(p.getUniqueId().toString());
				data.death();
				this.plugin.playerdata.put(p.getUniqueId().toString(), data);
			}
		} else {
			// mob kill
		}
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onEntityDeath(EntityDeathEvent event) {
		if ( !this.plugin.config.getConfig().getBoolean(
				"config.count.mobkill")) {
			return;
		}
		Entity p2 = event.getEntity();
		EntityDamageEvent cause = p2.getLastDamageCause();

		if ((cause instanceof EntityDamageByEntityEvent)) {
			EntityDamageByEntityEvent newEvent = (EntityDamageByEntityEvent) p2
					.getLastDamageCause();

			Entity k = newEvent.getDamager();

			if ((k instanceof Projectile)) {
				k = ((Projectile) k).getShooter();
			}

			if ((k instanceof Wolf)) {
				Wolf wolf = (Wolf) k;
				if (wolf.getOwner() != null) {
					try {
						k = (Entity) wolf.getOwner();
					} catch (Exception e) {

					}
				}
			}

			if ((k instanceof Player)) {
				PlayerData data = this.plugin.playerdata.get(k.getUniqueId().toString());
				data.kill();
				this.plugin.playerdata.put(k.getUniqueId().toString(), data);
			}
		}
	}
}
