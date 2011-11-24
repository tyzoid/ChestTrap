package tk.tyzoid.plugins.ChestTrap.Lib;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.HashMap;
import java.util.Properties;

import tk.tyzoid.plugins.ChestTrap.ChestTrap;

public class Settings {
	private Properties props = new Properties();
	private final HashMap<String, String> items = new HashMap<String, String>();
	private String pluginname = "ChestTrap";
	ChestTrap plugin;
	
	public Settings(ChestTrap instance){
		plugin = instance;
	}
	
	public void readSettings(){
		try{
			String path = "plugins/ChestTrap";
			File propertiesFile = new File(path + "/ChestTrap.properties");
    		if(!propertiesFile.exists()){
    			(new File(path)).mkdir();
    			propertiesFile.createNewFile();
    		}
		    
			FileInputStream propertiesStream = new FileInputStream(propertiesFile);
			
			props.load(propertiesStream);
			System.out.println("[" + pluginname + "] Properties loaded.");
			propertiesStream.close();
			
			items.put("use-admin", loadProperty("use-admin", "true"));
			items.put("ChestTrap-admin-commands", loadProperty("ChestTrap-admin-commands", "/ctadmin,/cta"));
			items.put("ChestTrap-commands", loadProperty("ChestTrap-commands", "/chesttrap,/ct"));
			items.put("ChestTrap-reload-commands", loadProperty("ChestTrap-reload-commands", "/ctreload,/ctr"));
			items.put("number-angry-wolves", loadProperty("number-angry-wolves", "8"));
			
			if(!((items.get("use-admin").equalsIgnoreCase("true")) || (items.get("use-admin").equalsIgnoreCase("false")))){
				System.out.println("[" + pluginname + "] Malformed property \"use-admin\". Resetting...");
				setCProperty("use-admin", "true");
			}
			
			if(!plugin.isInteger(items.get("number-angry-wolves"))){
				System.out.println("[" + pluginname + "] Malformed property \"number-angry-wolves\". Resetting...");
				setCProperty("number-angry-wolves", "8");
			}
			
			FileOutputStream propertiesOutputStream = new FileOutputStream(propertiesFile);
			props.store(propertiesOutputStream, "");
		} catch(Exception e){
			System.out.println("[" + pluginname + "] Failed to load properties. Aborting.");
			System.out.println("[" + pluginname + "] Error: " + e.toString());
		}
	}
	
	public String getProperty(String property){
		return items.get(property);
	}
	
	public void reloadData(){
		readSettings();
	}
	
	private String loadProperty(String property, String defaultValue){
		String currentProperty;
		currentProperty = props.getProperty(property);
		String value;
    	if(currentProperty == null){
    		System.out.println("[" + pluginname + "] Property not found: " + property + ". Resetting to: " + defaultValue);
    		props.setProperty(property, defaultValue);
    		value = defaultValue;
    	} else {
    		value = currentProperty;
    	}
    	return value;
	}
	
	private void setCProperty(String property, String value){
		props.setProperty(property, value);
	}
}
