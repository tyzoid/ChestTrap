package tk.tyzoid.plugins.ChestTrap.Listeners;

//import org.bukkit.entity.Player;
//import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.*;

import tk.tyzoid.plugins.ChestTrap.ChestTrap;
//import tk.tyzoid.plugins.ChestTrap.Lib.Coordinate;

public class BListener implements Listener {
	private final ChestTrap plugin;
	String pluginname;
	
	public BListener(ChestTrap instance) {
        plugin = instance;
        plugin.isEnabled();
        
        pluginname = plugin.pluginname;
    }
	@EventHandler(priority = EventPriority.MONITOR)
	public void onBlockBreak(BlockBreakEvent event){
		if(event.isCancelled()){
			return;
		}
		
		//Block b = event.getBlock();
		
		
	}
}
