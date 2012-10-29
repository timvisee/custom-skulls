package com.timvisee.customskulls;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Date;
import java.util.logging.Logger;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import com.timvisee.customskulls.Metrics.Graph;

public class CustomSkulls extends JavaPlugin {
	private static final Logger log = Logger.getLogger("Minecraft");
	
	private final CustomSkullsEntityListener entityListener = new CustomSkullsEntityListener(this);
	
	// Permissions and Economy manager
	private PermissionsManager permissionsManager;
	
	// Enabled date
	private Date enabledDate = new Date();
	
	public void onEnable() {
		// Reset the enabled date, to calculate the 'running time'
		enabledDate = new Date();
		
		PluginManager pm = getServer().getPluginManager();
		
		// Register the event listeners
		pm.registerEvents(this.entityListener, this);
		
		// Generate the  default config files if they aren't available
		generateDefaultConfigs();
		
		// Load the config files
		reloadConfigFiles();
		
		// Setup the permission manager
		setupPermissionsManager();
				
		// Setup Metrics
		setupMetrics();
		
		// Plugin has been enabled. Show a message in the console
		PluginDescriptionFile pdfFile = getDescription();
		log.info("[CustomSkulls] Custom Skulls v" + pdfFile.getVersion() + " Started");
	}

	public void onDisable() {
		log.info("[CustomSkulls] Custom Skulls Disabled");
	}
	
	/**
	 * Setup the metrics statics feature
	 * @return false if an error occurred
	 */
	public boolean setupMetrics() {
		try {
		    Metrics metrics = new Metrics(this);
		    // Construct a graph, which can be immediately used and considered as valid
		    // Amount of kills and drops
		    /*Graph graph = metrics.createGraph("Deaths and Drops");
		    graph.addPlotter(new Metrics.Plotter("Deaths") {
	            @Override
	            public int getValue() {
	            	return getShopManager().countShops();
	            }
		    });
		    graph.addPlotter(new Metrics.Plotter("Kills") {
	            @Override
	            public int getValue() {
	            	return getBoothManager().countBooths();
	            }
		    });*/
		    
		    // Used permissions systems
		    Graph graph2 = metrics.createGraph("Permisison Plugin Usage");
		    graph2.addPlotter(new Metrics.Plotter(getPermissionsManager().getUsedPermissionsSystemType().getName()) {
	            @Override
	            public int getValue() {
	            	return 1;
	            }
		    });
		    metrics.start();
		    return true;
		} catch (IOException e) {
		    // Failed to submit the statics :-(
			e.printStackTrace();
			return false;
		}
	}

	public void generateDefaultConfigs() {
		if(!getDataFolder().exists()) {
			log.info("[CustomSkulls] Creating new CustomSkulls folder");
			getDataFolder().mkdirs();
		}
		File configFile = new File(getDataFolder(), "config.yml");
		if(!configFile.exists()) {
			log.info("[CustomSkulls] Generating default config file");
			copy(getResource("res/DefaultFiles/config.yml"), configFile);
		}
	}
	
	private void copy(InputStream in, File file) {
	    try {
	        OutputStream out = new FileOutputStream(file);
	        byte[] buf = new byte[1024];
	        int len;
	        while((len=in.read(buf))>0){
	            out.write(buf,0,len);
	        }
	        out.close();
	        in.close();
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	}
	
	/**
	 * Setup the permissions manager
	 */
	public void setupPermissionsManager() {
		// Setup the permissions manager
		this.permissionsManager = new PermissionsManager(this);
		this.permissionsManager.setup();
	}
	
	/**
	 * Get the permissions manager
	 * @return permissions manager
	 */
	public PermissionsManager getPermissionsManager() {
		return this.permissionsManager;
	}
	
	public boolean reloadConfigFiles() {
		File configFile = new File(getDataFolder(), "config.yml");
		try {
			getConfig().load(configFile);
			return true;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InvalidConfigurationException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	/**
	 * Get the date when the plugin was enabled
	 * @return the enabled date
	 */
	public Date getEnabledDate() {
		return this.enabledDate;
	}
	
	public boolean onCommand(CommandSender sender, Command cmd,
			String commandLabel, String[] args) {
		// Run the command through the command handler
		CommandHandler ch = new CommandHandler(this);
		return ch.onCommand(sender, cmd, commandLabel, args);
	}
}
