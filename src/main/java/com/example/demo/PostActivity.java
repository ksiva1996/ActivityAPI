package com.example.demo;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class PostActivity {
	
	@Autowired
	ApplicationContext context;
	
	@Autowired
	ActivityapiApplication appl;
	
	@PostMapping("activity/{activityName}")
	public HashMap<String,String> getScore(@PathVariable String activityName, @RequestBody Map<String,String> params) {
		
		//checking to see if the POST request is properly is properly sent. 
		if(!params.containsKey(Util.VALUE)) {
			return Util.getErrorOutput(Util.PARAMETER_MISSING);
		}
		
		int value = 0;
		
		//Using try to make sure that the value is a integer.
		try {
			value = Integer.parseInt(params.get(Util.VALUE));
		}catch(NumberFormatException e) {
			
			return Util.getErrorOutput(Util.PARAMETER_MISMATCH);
		}
		
		HashMap<String,Key> keys = appl.getKeys();
		
		if(keys.containsKey(activityName)) {
			Key key = keys.get(activityName);
			key.setTotal(value);
		}else {
			Key key = context.getBean(Key.class);
			key.setKey(activityName);
			key.setTotal(value);
			keys.put(activityName, key);
		}
		return new HashMap<>();
	}
}
