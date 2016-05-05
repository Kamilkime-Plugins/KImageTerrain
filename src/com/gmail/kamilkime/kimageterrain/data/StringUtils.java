package com.gmail.kamilkime.kimageterrain.data;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;

public class StringUtils {
	
	private static String[] arguments = new String[]{"-i","-g","-u","-t","-b","-s","-w"};

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
	
	public static List<String> getDataForArgument(String arg, String[] args){
		List<String> toReturn = new ArrayList<String>();
		int index = 0;
		for(;index < args.length; index++){
			if(arg.equalsIgnoreCase(args[index])) break;
		}
		index++;
		for(; index < args.length; index++){
			if(!isArgument(args[index])) toReturn.add(args[index]);
			else break;
		}
		return toReturn;
	}
	
	public static boolean isArgument(String arg){
		for(String s : arguments){
			if(arg.equalsIgnoreCase(s)) return true;
		}
		return false;
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