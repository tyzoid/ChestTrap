package tk.tyzoid.plugins.ChestTrap.Lib;

import java.io.*;
import java.util.HashMap;
import java.util.Map.Entry;

import org.bukkit.World;
import org.bukkit.block.Block;

import tk.tyzoid.plugins.ChestTrap.ChestTrap;

public class Data {
	private File chests;
	private final HashMap<Block, TrapChest> chest_list = new HashMap<Block, TrapChest>();
	private String file_contents = "";
	private ChestTrap plugin;
	String pluginname;
	private int numberOfChests = 0;
	
	public Data(ChestTrap instance){
		plugin = instance;
		pluginname = plugin.pluginname;
	}
	
	public void loadTraps(){
		String tmp = "";
		String location[];
		boolean traps[];
		Coordinate coord;
		TrapChest tempChest;
		char tempType[];
		World w;
		
		System.out.println("[" + pluginname + "] Loading traps...");
		
		try{
			String path = "plugins/ChestTrap";
			chests = new File(path + "/chests.dat");
			
			if(!chests.exists()){
				(new File(path)).mkdir();
				chests.createNewFile();
			}
			
			FileInputStream chestIn = new FileInputStream(chests);
			BufferedReader reader = new BufferedReader(new InputStreamReader(new DataInputStream(chestIn)));
			
			while((tmp = reader.readLine()) != null){
				numberOfChests++;
				file_contents += tmp;
				String temp[] = tmp.split(":");
				
				if(temp.length != 2){
					System.out.println("[" + pluginname + "] An error occured: invalid line at line: " + numberOfChests + ". No trap data.");
				} else {
					//location of traps
					location = temp[0].split(",");
					if(location.length != 4){
						System.out.println("[" + pluginname + "] An error occured: invalid line at line: " + numberOfChests + ". Not enough data.");
					} else {
						if((w = plugin.getServer().getWorld(location[0])) == null){
							System.out.println("[" + pluginname + "] An error occured: invalid world at line: " + numberOfChests);
						} else {
							boolean validIntegers = false;
							int[] locations = new int[3];
							
							try{
								locations[0] = Integer.parseInt(location[1]);
								locations[1] = Integer.parseInt(location[2]);
								locations[2] = Integer.parseInt(location[3]);
								
								validIntegers = true;
							} catch(NumberFormatException e){
								e.printStackTrace();
							}
							if(!validIntegers){
								System.out.println("[" + pluginname + "] An error occured: invalid integer at line: " + numberOfChests);
							} else {
								if(w.getBlockTypeIdAt(locations[0], locations[1], locations[2]) != 54){
									System.out.println("[" + pluginname + "] An error occured: invalid block specified at line: " + numberOfChests);
								} else {
									//trap types
									tempType = temp[1].toCharArray();
									traps = validateData(tempType, 4);
									
									coord = new Coordinate(w, locations[0], locations[1], locations[2]);
									tempChest = new TrapChest(coord, traps, plugin);
									
									chest_list.put(tempChest.getBlock(), tempChest);
								}
							}
						}
					}
				}
			}
			
		} catch(Exception e){
			e.printStackTrace();
		}
		
		System.out.println("[" + pluginname + "] " + numberOfChests + " traps loaded.");
	}
	
	public boolean isTrapAt(World world, int x, int y, int z){
		return chest_list.containsKey((new MyChest(world, x, y, z, plugin)).getBlock());
	}
	
	public boolean isTrapAt(Coordinate c){
		return chest_list.containsKey((new MyChest(c, plugin).getBlock()));
	}
	
	public boolean isTrapAt(Block b){
		return chest_list.containsKey((new MyChest(b, plugin).getBlock()));
	}
	
	public void addTrap(Coordinate c, boolean[] traps){
		TrapChest chest = new TrapChest(c, traps, plugin);
		if(chest.isAChest() && !isTrapAt(c)){
			chest_list.put(chest.getBlock(), chest);
		}
		
		saveTraps();
	}
	
	public void addTrap(Block b, boolean[] traps){
		addTrap(new Coordinate(b.getWorld(), b.getX(), b.getY(), b.getZ()), traps);
	}
	
	public void removeTrap(Coordinate c){
		MyChest chest = new MyChest(c, plugin);
		if(chest.isAChest() && isTrapAt(c)){
			chest_list.remove(chest.getBlock());
		}
		
		saveTraps();
	}
	
	public boolean[] getTrapsAt(Block b){
		if(isTrapAt(b)){			
			return chest_list.get((new MyChest(b, plugin).getBlock())).getTrapList();
		} else {
			return null;
		}
	}
	
	public void saveTraps(){
		
		try{
			FileOutputStream chestOS = new FileOutputStream(chests);
			PrintStream chestOut = new PrintStream(chestOS);
			
			for (Entry<Block, TrapChest> entry : chest_list.entrySet()) {
				Block block = entry.getKey();
				TrapChest chest = entry.getValue();
				String temp = "";
				boolean traps[] = chest.getTrapList();
				
				for(int i = 0; i < traps.length; i++){
					temp += (traps[i]) ? "1" : "0";
				}
				
				chestOut.println(block.getWorld().getName() + "," + block.getX() + "," + block.getY() + "," + block.getZ() + ":" + temp);
			}
		} catch(Exception e){
			e.printStackTrace();
		}
	}
	
	private boolean[] validateData(char data[], int length){
		boolean result[] = new boolean[length];
		char tmpData[] = data;
		
		if(data.length != length){
			
			if(tmpData.length < length){
				for(int i = 0; i < tmpData.length; i++){
					if(tmpData[i] != '1' && tmpData[i] != '0'){
						if(i == 0){
							result[i] = true;
						} else {
							result[i] = false;
						}
					} else {
						result[i] = (tmpData[i] != '1');
					}
				}
				for(int i = tmpData.length; i < length; i++){
					result[i] = false;
				}
			} else {
				for(int i = 0; i < length; i++){
					if(tmpData[i] != '1' && tmpData[i] != '0'){
						if(i == 0){
							result[i] = true;
						} else {
							result[i] = false;
						}
					} else {
						result[i] = (tmpData[i] == '1');
					}
				}
			}
		} else {
			for(int i = 0; i < length; i++){
				if(tmpData[i] != '1' && tmpData[i] != '0'){
					if(i == 0){
						result[i] = true;
					} else {
						result[i] = false;
					}
				} else {
					result[i] = (tmpData[i] == '1');
				}
			}
		}
		
		return result;
	}
}
