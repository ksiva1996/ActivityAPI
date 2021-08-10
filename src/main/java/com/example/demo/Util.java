package com.example.demo;

import java.util.HashMap;

public class Util {
	
	
	//Time to prune data: 12 hours.
	static long timeToPrune = 2*60*1000;
	static String VALUE = "value";
	static String ERROR = "Error";
	static String PARAMETER_MISSING = "'value' parameter is required"; 
	static String PARAMETER_MISMATCH = "'value' parameter must be numberic";
	static String INVALID_REQUEST = "Invalid Request";
	
	
	static HashMap<String,Integer> getOutput(int value) {
		HashMap<String, Integer> map = new HashMap<>();
		map.put(VALUE,value);
		return map;
	}
	
	static HashMap<String,String> getErrorOutput(String error) {
		HashMap<String, String> map = new HashMap<>();
		map.put(ERROR,error);
		return map;
	}
	
}
