package tk.tyzoid.plugins.ChestTrap.Lib;

import org.bukkit.World;

public class Coordinate {
	int x, y, z;
	World w;
	
	public Coordinate(World world, int x_coord, int y_coord, int z_coord){
		x = x_coord;
		y = y_coord;
		z = z_coord;
		w = world;
	}
}
