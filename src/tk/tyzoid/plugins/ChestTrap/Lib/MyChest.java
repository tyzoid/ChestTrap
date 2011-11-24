package tk.tyzoid.plugins.ChestTrap.Lib;

import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;

import tk.tyzoid.plugins.ChestTrap.ChestTrap;

public class MyChest {
	private Coordinate coord;
	private Block block;
	private Chest chest;
	private World world;
	private boolean doubleChest;
	private boolean[] check;
	private boolean isChest;
	private ChestTrap plugin;

	public MyChest(Coordinate coord, ChestTrap instance){
		this(coord.w, coord.x, coord.y, coord.z, instance);
	}
	
	public MyChest(Block b, ChestTrap instance){
		this(b.getWorld(), b.getX(), b.getY(), b.getZ(), instance);
	}
	
	public MyChest(World w, int x, int y, int z, ChestTrap instance){
		world = w;
		coord = new Coordinate(world, x, y, z);
		//block = world.getBlockAt(x, y, z);
		plugin = instance;
		check = new boolean[4];
		doubleChest = false;
		
		isChest = (world.getBlockTypeIdAt(x, y, z)) == 54;
		if(isChest){
			check[0] = (world.getBlockTypeIdAt(x+1, y, z) == 54);
			check[1] = (world.getBlockTypeIdAt(x, y, z+1) == 54);
			check[2] = (world.getBlockTypeIdAt(x-1, y, z) == 54);
			check[3] = (world.getBlockTypeIdAt(x, y, z-1) == 54);
			if(check[0]){
				doubleChest = true;
			} else if(check[1]){
				doubleChest = true;
			} else if(check[2]){
				doubleChest = true;
				coord.x = x-1;
			} else if(check[3]){
				doubleChest = true;
				coord.z = z-1;
			}
			
			block = world.getBlockAt(coord.x, coord.y, coord.z);
			chest = (Chest) block.getState();
		} else {
			block = world.getBlockAt(x, y, z);
			chest = null;
		}
	}

	public boolean isDoubleChest(){
		return doubleChest;
	}
	
	public boolean isAChest(){
		return isChest;
	}
	
	public Coordinate getCoords(){
		return coord;
	}
	
	public World getWorld(){
		return world;
	}
	
	public Block getBlock(){
		return block;
	}
	
	public Chest getChest(){
		return chest;
	}
	
	public boolean isTrapChest(){
		return plugin.chestData.isTrapAt(block);
	}
}
