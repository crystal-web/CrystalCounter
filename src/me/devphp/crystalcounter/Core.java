package me.devphp.crystalcounter;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;

import code.husky.mysql.MySQL;

public class Core extends JavaPlugin {
	private Core plugin;
	public Config config;
	public Connection connection;
	public MySQL mysql;
	public String playerList[];
	public HashMap<String, PlayerData> playerdata = new HashMap();
	
	public void onEnable() {
		this.plugin = this;

		// Configuration
		this.config = new Config(this.plugin);
		Config.makeDefaultConfig();
		initDatabase();
		
	    // EvenetListener
		EventListener listener = new EventListener(this);
		getServer().getPluginManager().registerEvents(listener, this);
	}

	private void initDatabase() {
		// Connection sql
		this.mysql = new MySQL(this, config.getConfig().getString(
				"config.database.host"), config.getConfig().getString(
				"config.database.port"), config.getConfig().getString(
				"config.database.dbname"), config.getConfig().getString(
				"config.database.username"), config.getConfig().getString(
				"config.database.password"));
		
		this.connection = this.mysql.openConnection();
		
		this.mysql.updateSQL("CREATE TABLE IF NOT EXISTS `" + config.getConfig().getString("config.database.tablename") + "` ("
						+ "`id` int(11) NOT NULL AUTO_INCREMENT,"
						+ "`uid` varchar(255) NOT NULL," // mc Userid
						+ "`uname` varchar(16) NOT NULL," // mc username
						+ "`blockbreak` int(11)  NOT NULL DEFAULT '0'," // Block break counter
						+ "`blockplaced` int(11)  NOT NULL DEFAULT '0'," // Blockplaced  counter
						+ "`kill` int(11)  NOT NULL DEFAULT '0'," // Kill  counter
						+ "`perfectKill` int(11) NOT NULL DEFAULT '0'," // Kill perfect
						
						+ "`death` int(11)  NOT NULL DEFAULT '0'," // Death  counter
						+ "`connectTime` int(11) NOT NULL DEFAULT '0'," // Kill perfect
						+ "PRIMARY KEY (`id`)"
						+ ") ENGINE=InnoDB DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;");
	}
}
