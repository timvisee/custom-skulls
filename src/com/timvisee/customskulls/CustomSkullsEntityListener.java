package com.timvisee.customskulls;

import java.util.Random;
import org.bukkit.Location;
import org.bukkit.configuration.Configuration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;

public class CustomSkullsEntityListener implements Listener {
	private static CustomSkulls plugin;

	public CustomSkullsEntityListener(CustomSkulls instance) {
		plugin = instance;
	}
	
	@EventHandler
	public void onEntityDeath(EntityDeathEvent e) {		
		if(e.getEntityType().equals(EntityType.SKELETON) ||
				e.getEntityType().equals(EntityType.WITHER) ||
				e.getEntityType().equals(EntityType.ZOMBIE) ||
				e.getEntityType().equals(EntityType.CREEPER)) {
			
			String worldName = e.getEntity().getWorld().getName();
			String entityName = e.getEntityType().getName();
			
			// Check if the settings thing is enabled
			if(isConfigSettingEnabled(worldName, entityName, false)) {
				// The setting thing is enabled
				
				// Cancel the skull drop
				for(ItemStack entry : e.getDrops())	
					if(entry.getTypeId() == 144 || entry.getTypeId() == 397)
						entry.setAmount(0);
				
				// Should a skull be droped?
				//if(getConfigSettingBoolean(worldName, entityName, "enable", false)) {
				// Get the drop chance
				int chance = getConfigSettingInt(worldName, entityName, "chance", 10);
				
				// Should a skull be droped this time?
				int probability = new Random().nextInt(101);
				boolean dropSkull = (probability <= chance ? true : false);
				
				// If a skull should be droped, drop one
				if(dropSkull)
					dropSkull(1, e.getEntity().getLocation(), e.getEntity());
				//}
			} else if (CustomSkulls.DEBUG) {
				CustomSkulls.log.info(entityName + " head drop in world " + worldName + " skipped.");
			}
		}	
	}
	
	@EventHandler
	public void onPlayerDeathEvent(PlayerDeathEvent e) {		
		String worldName = e.getEntity().getWorld().getName();
		String entityName = "Player";
		String playerName = e.getEntity().getName();
		
		// Check if the settings thing is enabled
		if(isConfigSettingEnabled(worldName, entityName, false)) {
			// The setting thing is enabled
			
			// Cancel the skull drop
			for(ItemStack entry : e.getDrops())	
				if(entry.getTypeId() == 144 || entry.getTypeId() == 397)
					entry.setAmount(0);
			
			// Should a skull be droped?
			//if(getConfigSettingBoolean(plugin.getConfig(), worldName, entityName, "dropSkull", false)) {
			// Get the drop chance
			int chance = getConfigSettingInt(worldName, entityName, "chance", 10);
			
			// Should a skull be droped this time?
			int probability = new Random().nextInt(101);
			boolean dropSkull = (probability <= chance ? true : false);
			boolean dropPlayerSkull = getConfigSettingBoolean(worldName, entityName, "head", false);
			
			// If a skull should be droped, drop one
			if(dropSkull)
				if(!dropPlayerSkull)
					dropSkull(1, e.getEntity().getLocation(), e.getEntity());
				else
					addPlayerSkullDrop(1, e, playerName);
			//}
		} else if (CustomSkulls.DEBUG) {
			CustomSkulls.log.info(entityName + " head drop in world " + worldName + " skipped.");
		}
	}
	
	public void dropSkull(int amount, Location loc, Entity e) {
		loc.getWorld().dropItemNaturally(loc, CustomSkullsUtility.getSkullItemStack(amount, e));
		if (CustomSkulls.DEBUG)
			CustomSkulls.log.info("Dropped " + amount + " head at location " + loc.toString() + " for entity " + e.toString());
	}
	
	public void addPlayerSkullDrop(int amount, PlayerDeathEvent e, String playerName) {
		e.getDrops().add(CustomSkullsUtility.getSkullItemStack(amount, playerName));
	}
	
	public boolean isConfigSettingEnabled(String worldName, String entityName, boolean def) {
		// The configuration file may not be null
		if(config() == null)
			return def;
		
		return (config().getBoolean("drops.worlds." + worldName + "." + entityName + ".enable",
				config().getBoolean("drops.global." + entityName + ".enable", def)));
	}
	
	public boolean getConfigSettingBoolean(String worldName, String entityName, String node, boolean def) {
		// The configuration file may not be null
		if(config() == null)
			return def;
		
		return (config().getBoolean("drops.worlds." + worldName + "." + entityName + "." + node,
				config().getBoolean("drops.global." + entityName + "." + node, def)));
	}
	
	public int getConfigSettingInt(String worldName, String entityName, String node, int def) {
		// The configuration file may not be null
		if(config() == null)
			return def;
		
		return (config().getInt("drops.worlds." + worldName + "." + entityName + "." + node,
				config().getInt("drops.global." + entityName + "." + node, def)));
	}
	
	private Configuration config() {
		return plugin.getConfig();
	}
}
