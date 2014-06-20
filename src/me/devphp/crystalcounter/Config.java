package me.devphp.crystalcounter;

import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

/**
 * Created by Greenns on 15/05/14.
 */
public class Config {

    public static Core plugin;
    public static FileConfiguration configFile;

    public Config(Core plugin)
    {
        this.plugin = plugin;
    }

    public static void load(){
        setupConfig();
    }
    
    public static void makeDefaultConfig()
    {
        FileConfiguration config = getConfig();        
        if(config.get("config") == null)
        {	
            config.set("config.database.host", "localhost");
            config.set("config.database.port", 3306);
            config.set("config.database.dbname", "dbname");
            config.set("config.database.tablename", "tablename");
            
            config.set("config.database.username", "user");
            config.set("config.database.password", "password");
            
            config.set("config.count.mobkill", false);
            
            saveConfig();
        }
        load();
    }
    
    public static void setupConfig() {
        FileConfiguration file = getConfig();
;		
    }

    public static void saveConfig()
    {
        try {
            configFile.save(new File(plugin.getDataFolder() + File.separator + "config.yml"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    public static FileConfiguration getConfig() {
        if (configFile == null) {
            configFile = YamlConfiguration.loadConfiguration(new File(plugin.getDataFolder() + File.separator + "config.yml"));
        }
        return configFile;
    }
}
