package com.example.demo;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;


@Component
@Scope("prototype")
public class Key {
		
	@Autowired
	ApplicationContext context;
	
	//variable to store the activity name
	String keyName;
	
	//variable to store the total score or value of a activity.
	int total=0;
	
	// An arraylist to store the corresponding time associated when each value is added. 
	// Since this is an arraylist and the data is ordered pruning older data is more efficient.
	ArrayList<Pair> activities = new ArrayList<>();
	
	
	public String getKey() {
		return keyName;
	}
	public void setKey(String key) {
		this.keyName = key;
	}
	public int getTotal() {
		return total;
	}
	
	//when a new value is posted, we increment the score and the timestamp along with the score in activities map.
	public void setTotal(int value) {
		this.total += value;
		Pair p = new Pair();
		p.setTime(System.currentTimeMillis());
		p.setValue(value);
		activities.add(p);
	}
	
	public ArrayList<Pair> getActivities() {
		return activities;
	}
	
	//decrement the score after pruning data.
	public void removeValue(int value) {
		this.total-=value;
	}
	
	
	/*Key(String key, int score) {
		this.key = key;
		this.score = score;
	}*/

}
