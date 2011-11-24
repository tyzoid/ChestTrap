package tk.tyzoid.plugins.ChestTrap.Lib;

import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;

import tk.tyzoid.plugins.ChestTrap.ChestTrap;

public class TrapChest {
	private Coordinate coord;
	private Block block;
	private Chest chest;
	private World world;
	private boolean doubleChest;
	private boolean[] check;
	private boolean isChest;
	private ChestTrap plugin;
	private boolean trapList[];

	public TrapChest(Coordinate coord, boolean traps[], ChestTrap instance){
		this(coord.w, coord.x, coord.y, coord.z, traps, instance);
	}
	
	public TrapChest(Block b, boolean traps[], ChestTrap instance){
		this(b.getWorld(), b.getX(), b.getY(), b.getZ(), traps, instance);
	}
	
	public TrapChest(World w, int x, int y, int z, boolean traps[], ChestTrap instance){
		world = w;
		coord = new Coordinate(world, x, y, z);
		//block = world.getBlockAt(x, y, z);
		plugin = instance;
		check = new boolean[4];
		doubleChest = false;
		trapList = traps;
		
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
	
	public boolean[] getTrapList(){
		return trapList;
	}
	
	public MyChest getMyChest(){
		return new MyChest(block, plugin);
	}
}
