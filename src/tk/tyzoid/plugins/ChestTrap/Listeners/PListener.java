package tk.tyzoid.plugins.ChestTrap.Listeners;

import java.util.regex.Pattern;

import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.CreatureType;
//import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Wolf;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerListener;

import tk.tyzoid.plugins.ChestTrap.ChestTrap;
import tk.tyzoid.plugins.ChestTrap.Lib.Coordinate;
//import tk.tyzoid.plugins.ChestTrap.Lib.MyChest;

public class PListener  extends PlayerListener{
	private final ChestTrap plugin;
	String pluginname;
	
	public PListener(ChestTrap instance) {
        plugin = instance;
        plugin.isEnabled();
        
        pluginname = plugin.pluginname;
    }
	
	public void onPlayerCommandPreprocess(PlayerCommandPreprocessEvent event) {
		String[] split = event.getMessage().split(" ");
		//String mess = event.getMessage();
		Player player = event.getPlayer();
		
		//The /chesttrap command
		String[] chestTrapCommands = plugin.settings.getProperty("ChestTrap-commands").split(",");
		boolean chestTrapCommandUsed = false;
		boolean createTrap = false;
		boolean removeTrap = false;
		if(plugin.permissionsExists || plugin.useSuperperms){
			createTrap = plugin.hasPermission(player, "chestTrap.createTrap") || player.isOp();
			removeTrap = plugin.hasPermission(player, "chestTrap.removeTrap") || player.isOp();
			if(createTrap || removeTrap){
				for(int i = 0; (i < chestTrapCommands.length && !chestTrapCommandUsed); i++){
					if(split[0].equalsIgnoreCase(chestTrapCommands[i])){
						chestTrapCommandUsed = true;
					}
				}
			}
		} else {
			if(player.isOp()){
				createTrap = true;
				removeTrap = true;
				for(int i = 0; (i < chestTrapCommands.length && !chestTrapCommandUsed); i++){
					if(split[0].equalsIgnoreCase(chestTrapCommands[i])){
						chestTrapCommandUsed = true;
					}
				}
			}
		}
		if(chestTrapCommandUsed){
			Block b = player.getTargetBlock(null, 8);
			if(b.getTypeId() == 54){
				if(split.length > 1){
					if((split[1].equalsIgnoreCase("add") || split[1].equalsIgnoreCase("create")) && createTrap){
						if(plugin.chestData.isTrapAt(b.getWorld(), b.getX(), b.getY(), b.getZ())){
							player.sendMessage("§a[" + pluginname + "] §fThere is already a trap there.");
						} else {
							String compare = "";
							for(int i = 2; i < split.length; i++){
								compare += split[i] + " ";
							}
							boolean traps[] = new boolean[4];
							traps[0] = Pattern.compile("-die|-d|die").matcher(compare).find();
							traps[1] = Pattern.compile("-teleport|-t|teleport").matcher(compare).find();
							traps[2] = Pattern.compile("-clear|-c|clear").matcher(compare).find();
							traps[3] = Pattern.compile("-wolves|-w|wolves").matcher(compare).find();
							
							plugin.chestData.addTrap(b, traps);
							player.sendMessage("§a[" + pluginname + "] §fA trap has been added!");
						}
					} else if(split[1].equalsIgnoreCase("remove") && removeTrap){
						if(plugin.chestData.isTrapAt(b.getWorld(), b.getX(), b.getY(), b.getZ())){
							plugin.chestData.removeTrap(new Coordinate(b.getWorld(), b.getX(), b.getY(), b.getZ()));
							player.sendMessage("§a[" + pluginname + "] §fTrap removed!");
						} else {
							player.sendMessage("§a[" + pluginname + "] §fThere is no trap there...");
						}
					}
				}
			} else {
				player.sendMessage("§a[" + pluginname + "] §fThat is not a chest.");
			}
			event.setCancelled(true);
		}
    }
	
	@SuppressWarnings("deprecation")
	public void onPlayerInteract(PlayerInteractEvent event){
		Block block = event.getClickedBlock();
		
		if(block == null){
			return;
		}
		
		Player player = event.getPlayer();
		World w = block.getWorld();
		Coordinate c = new Coordinate(w, block.getX(), block.getY(), block.getZ());
		if(event.getAction().equals(Action.RIGHT_CLICK_BLOCK) && block.getTypeId() == 54){
			if(plugin.chestData.isTrapAt(c)){
				boolean traps[] = plugin.chestData.getTrapsAt(block);
				if(traps[0]){
					player.setHealth(0);
				} else {
					if(traps[1]){
						boolean teleported = false;
						boolean isSolid;
						int x = player.getLocation().getBlockX() + plugin.randomNumbers.nextInt(512)-256;
						int z = player.getLocation().getBlockY() + plugin.randomNumbers.nextInt(512)-256;
						for(int y = 1; y < 128 && !teleported; y++){
							isSolid = (!w.getBlockAt(x, y-1, z).isEmpty() && !w.getBlockAt(x, y-1, z).isLiquid());
							if(isSolid && w.getBlockTypeIdAt(x, y, z) == 0 && w.getBlockTypeIdAt(x, y+1, z) == 0){
								player.teleport(w.getBlockAt(x, y+1, z).getLocation());
								teleported = true;
							}
						}
					}
					if(traps[2]){
						player.getInventory().clear();
						player.updateInventory();
					}
					if(traps[3]){
						int numWolves = Integer.parseInt(plugin.settings.getProperty("number-angry-wolves"));
						Wolf wolf;
						for(int i = 0; i < numWolves; i++){
							wolf = (Wolf) w.spawnCreature(player.getLocation(), CreatureType.WOLF);
							if(wolf != null){
								wolf.setTarget(player);
	                        	wolf.setAngry(true);
							}
						}
					}
				}
				event.setCancelled(true);
			}
		}
	}   
}
