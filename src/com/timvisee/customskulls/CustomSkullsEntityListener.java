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
	public static CustomSkulls plugin;

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
			if(isConfigSettingEnabled(plugin.getConfig(), worldName, entityName, false)) {
				// The setting thing is enabled
				
				// Cancel the skull drop
				for(ItemStack entry : e.getDrops())	
					if(entry.getTypeId() == 144 || entry.getTypeId() == 397)
						entry.setAmount(0);
				
				// Should a skull be droped?
				if(getConfigSettingBoolean(plugin.getConfig(), worldName, entityName, "dropSkull", false)) {
					// Get the drop chance
					int chance = getConfigSettingInt(plugin.getConfig(), worldName, entityName, "dropChance", 40);
					
					// Should a skull be droped this time?
					Random rand = new Random();
					boolean dropSkull = (0 == rand.nextInt(chance));
					
					// If a skull should be droped, drop one
					if(dropSkull)
						dropSkull(1, e.getEntity().getLocation(), e.getEntity());
				}
			}
		}	
	}
	
	@EventHandler
	public void onPlayerDeathEvent(PlayerDeathEvent e) {		
		String worldName = e.getEntity().getWorld().getName();
		String entityName = "Player";
		String playerName = e.getEntity().getName();
		
		// Check if the settings thing is enabled
		if(isConfigSettingEnabled(plugin.getConfig(), worldName, entityName, false)) {
			// The setting thing is enabled
			
			// Cancel the skull drop
			for(ItemStack entry : e.getDrops())	
				if(entry.getTypeId() == 144 || entry.getTypeId() == 397)
					entry.setAmount(0);
			
			// Should a skull be droped?
			if(getConfigSettingBoolean(plugin.getConfig(), worldName, entityName, "dropSkull", false)) {
				// Get the drop chance
				int chance = getConfigSettingInt(plugin.getConfig(), worldName, entityName, "dropChance", 40);
				
				// Should a skull be droped this time?
				Random rand = new Random();
				boolean dropSkull = (0 == rand.nextInt(chance));
				boolean dropPlayerSkull = getConfigSettingBoolean(plugin.getConfig(), worldName, entityName, "dropPlayerSkull", true);
				
				// If a skull should be droped, drop one
				if(dropSkull)
					if(!dropPlayerSkull)
						dropSkull(1, e.getEntity().getLocation(), e.getEntity());
					else
						addPlayerSkullDrop(1, e, playerName);
			}
		}
	}
	
	public void dropSkull(int amount, Location loc, Entity e) {
		loc.getWorld().dropItemNaturally(loc, CustomSkullsUtility.getSkullItemStack(amount, e));
	}
	
	public void addPlayerSkullDrop(int amount, PlayerDeathEvent e, String playerName) {
		e.getDrops().add(CustomSkullsUtility.getSkullItemStack(amount, playerName));
	}
	
	public boolean isConfigSettingEnabled(Configuration c, String worldName, String entityName, boolean def) {
		// The configuration file may not be null
		if(c == null)
			return def;
		
		return (c.getBoolean("drops.worlds." + worldName + "." + entityName + ".enabled",
				c.getBoolean("drops.global." + entityName + ".enabled", def)));
	}
	
	public boolean getConfigSettingBoolean(Configuration c, String worldName, String entityName, String node, boolean def) {
		// The configuration file may not be null
		if(c == null)
			return def;
		
		return (c.getBoolean("drops.worlds." + worldName + "." + entityName + "." + node,
				c.getBoolean("drops.global." + entityName + "." + node, def)));
	}
	
	public int getConfigSettingInt(Configuration c, String worldName, String entityName, String node, int def) {
		// The configuration file may not be null
		if(c == null)
			return def;
		
		return (c.getInt("drops.worlds." + worldName + "." + entityName + "." + node,
				c.getInt("drops.global." + entityName + "." + node, def)));
	}
}
