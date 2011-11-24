package tk.tyzoid.plugins.ChestTrap.Listeners;

//import org.bukkit.entity.Player;
//import org.bukkit.block.Block;
import org.bukkit.event.block.*;

import tk.tyzoid.plugins.ChestTrap.ChestTrap;
//import tk.tyzoid.plugins.ChestTrap.Lib.Coordinate;

public class BListener  extends BlockListener{
	private final ChestTrap plugin;
	String pluginname;
	
	public BListener(ChestTrap instance) {
        plugin = instance;
        plugin.isEnabled();
        
        pluginname = plugin.pluginname;
    }
	
	public void onBlockBreak(BlockBreakEvent event){
		if(event.isCancelled()){
			return;
		}
		
		//Block b = event.getBlock();
		
		
	}
}
