package com.gmail.kamilkime.kimageterrain.data;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;

import com.gmail.kamilkime.kimageterrain.Main;

public class StringUtils {
	
	private static String[] vars = new String[]{"-i","-g","-u","-t","-b","-s","-w"};

	public static String getMessage(String section, Object... args) {
		return MessageFormat.format(Main.getSettings().messages.get(section), args);
	}
	
	public static String color(String s) {
		return ChatColor.translateAlternateColorCodes('&', s);
	}
	
	public static List<String> color(List<String> s) {
		List<String> toReturn = new ArrayList<String>();
		for(String str : s) toReturn.add(ChatColor.translateAlternateColorCodes('&', str));
		return toReturn;
	}
	
	public static boolean hasArgument(String arg, String[] args){
		for(String a : args){
			if(a.equalsIgnoreCase(arg)) return true;
		}
		return false;
	}
	
	public static boolean isArgument(String arg){
		for(String s : vars){
			if(arg.equalsIgnoreCase(s)) return true;
		}
		return false;
	}
	
	public static List<String> getDataForArgument(String arg, String[] args){
		List<String> toReturn = new ArrayList<String>();
		boolean hasFound = false;
		for(int i = 0; i < args.length; i++){
			if(args[i].equalsIgnoreCase(arg)) {
				hasFound = true;
				continue;
			}
			if(hasFound) {
				if(isArgument(args[i])) break;
				toReturn.add(args[i]);
			}
		}
		return toReturn;
	}
	
	public static String listToString(List<String> list, String separator){
		String toReturn = "";
		for(int i=0; i<list.size(); i++) {
			toReturn += list.get(i);
			if(i !=(list.size()-1)) toReturn += separator;
		}
		return toReturn;
	}
}