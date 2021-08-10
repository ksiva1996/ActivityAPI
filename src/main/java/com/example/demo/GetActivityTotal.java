package com.example.demo;
import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class GetActivityTotal {

	@Autowired
	ActivityapiApplication appl;
	
	
	@GetMapping("activity/{tag}/total")
	public HashMap<String,Integer> getScore(@PathVariable String tag) {
		HashMap<String,Key> keys = appl.getKeys();
		
		// If an activity is found then we return the total associated with that value.
		
		if(keys.containsKey(tag)) {
			return Util.getOutput(keys.get(tag).getTotal());
		}else
			return Util.getOutput(0);
		
	}
	
}