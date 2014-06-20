package me.devphp.CrystalCounter;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;

public class Core extends JavaPlugin {
	private Core plugin;
	public Config config;
	public String playerList[];
	public HashMap<String, PlayerData> playerdata = new HashMap();
	
	
	public String prefix = ChatColor.GRAY + "[" + ChatColor.GREEN + "Bansql" + ChatColor.GRAY + "] " + ChatColor.RESET;
	String url = "jdbc:mysql://localhost:3306/test";
	String user = "root";
	String passwd = "root";

	Connection connection;
	Statement state;

	public void onEnable() {
		this.config = new Config(this);
		this.url = "jdbc:mysql://" + Config.getConfig().getString("config.database.host") + ":3306/" + Config.getConfig().getString("config.database.dbname");
		this.user = Config.getConfig().getString("config.database.username");
		this.passwd = Config.getConfig().getString("config.database.password");
	    // EvenetListener
		EventListener listener = new EventListener(this);
		getServer().getPluginManager().registerEvents(listener, this);
		
		
		//getCommand("ban").setExecutor(new Ban(this));
		//getCommand("pardon").setExecutor(new Pardon(this));
		this.getServer().getPluginManager().registerEvents(new EventListener(this), this);
		
		this.plugin = this;

		// Configuration
		this.config = new Config(this.plugin);
		Config.makeDefaultConfig();
		
		// Thread 
		BukkitScheduler scheduler = Bukkit.getServer().getScheduler();
		scheduler.scheduleSyncRepeatingTask(this, new Runnable() {
			@Override
			public void run() {
			// THREAD 100 = 5sec
				
			}
		}, 0L, 600L);
		
		connect();
	}
	
	
	/**
	 * 
	 */
	public void connect() {
		try {
			Class.forName("com.mysql.jdbc.Driver");
		} catch (ClassNotFoundException e1) {
			this.getLogger().severe("Class  not found: com.mysql.jdbc.Driver");
		}

		try {
			this.connection = DriverManager.getConnection(this.url, this.user,
					this.passwd);
			this.state = this.connection.createStatement();
			java.util.Date currentDate = new java.util.Date();
	        long currenttimeInMillis = currentDate.getTime();
	        
			
			this.state.execute("CREATE TABLE IF NOT EXISTS `" + config.getConfig().getString("config.database.tablename") + "` ("
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
			/*
			 * try {
			 * 
			 * this.state.executeUpdate(
			 * "INSERT INTO test_table1 (pseudo, block, x, y, z) VALUES ('"
			 * +pname+"', '"+bname+"', "+x+", "+y+", "+z+")");
			 * 
			 * // Statement state = conn.createStatement(); ResultSet result =
			 * this
			 * .state.executeQuery("SELECT * FROM test_table1 WHERE Pseudo = '"
			 * +args[0]+"'");
			 * 
			 * } catch (SQLException e) {
			 * 
			 * e.printStackTrace(); }//
			 */
		} catch (SQLException e) {
			this.getLogger().severe(e.getMessage());
			this.getLogger().severe(this.url + " " + this.user + " *******");
			this.getServer().getPluginManager().disablePlugins();
		}
	}
}
