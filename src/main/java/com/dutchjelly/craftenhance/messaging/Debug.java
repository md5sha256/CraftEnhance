package com.dutchjelly.craftenhance.messaging;

import com.dutchjelly.craftenhance.CraftEnhance;

import java.util.logging.Logger;

public class Debug {
	
	public static void init(CraftEnhance main){
		enable = main.getConfig().getBoolean("enable-debug");
		prefix = main.getConfig().getString("debug-prefix");
		logger = main.getLogger();
	}

	private static Logger logger;
	private static boolean enable; //could be a config thing
	private static String prefix;
	
	public static void Send(Object obj){
		if(!enable) return;
		
		System.out.println(prefix + (obj != null ? obj.toString() : "null"));
	}
	
	public static void Send(Object sender, Object obj){
		if(!enable) return;
		
		logger.info(prefix + "<" + sender.getClass().getName() + "> " + obj != null ? obj.toString() : "null");
	}
	
	public static void Send(Object[] arr){
		if(arr == null) return;
		logger.info(prefix + " ");
        for (Object o : arr) {
            if (o == null) continue;
            logger.info(o.toString());
        }
		logger.info("");
	}
}
