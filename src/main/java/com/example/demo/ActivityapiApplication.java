package com.example.demo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

@SpringBootApplication
@EnableScheduling
public class ActivityapiApplication {

	//in memory data structure to store activity details.
	static HashMap<String,Key> keys;
	
	public static void main(String[] args) {
		SpringApplication.run(ActivityapiApplication.class, args);
		keys = new HashMap<>();
	}
	
	/* 
	 * CRON job scheduled to run at everyone one minute. https://freeformatter.com/cron-expression-generator-quartz.html
	 * We can change the CRON expression to change the frequency with which the pruneData method is executed.
	 * If we need an even stricter pruning of data, we can increase the frequency of cron job execution. 
	 * But that will place a huge load on the system when the amount of data is large.
	 * So I have settled on one minute.
	 * However if we are to assume that the activities added are to be associated to the most recent hour. 
	 * That is an activity that is reported at 11:25 AM, then it is to be considered for the hour 11.
	 * Then we needn't store the exact time in milliseconds, instead store the most recent hour.
	 * We can run the cron at the start of every hour and prune activity that is more than 12 hours old.
	 *  
	 */
	
	HashMap<String,Key> getKeys(){
		return keys;
	}
	
	
	@Scheduled(cron = "0 * * ? * *")
	public void cronJob() {
		pruneOlderData();
	}
	
	public void pruneOlderData() {
		System.out.println("pruning");
		
		//Iterating over every activity.
		Iterator<Map.Entry<String, Key>> iter1 = getKeys().entrySet().iterator();
		while(iter1.hasNext()) {
			
			Key key = iter1.next().getValue();
			ArrayList<Pair> activities = key.getActivities();
			Long timeNow = System.currentTimeMillis();
			
			//Iterating over every value that has been posted to an activity.
			Iterator<Pair> iter2 = activities.iterator();
			while(iter2.hasNext()) {
				
				Pair p = iter2.next();
				
				if(toBePruned(timeNow, p.getTime())) {
					key.removeValue(p.getValue());
					iter2.remove();
				}else {
					//The list is ordered. When we encounter an activity that is less than 12 hours old we can break the loop there. since the activities added after this are younger. 
					break;
				}
			}
			
		}
	}
	
	// logic to know if an data is to be pruned. We are checking if the difference between time now 
	// and the time when a value was posted is greater than the time to prune.
	boolean toBePruned(long timeNow, long time) {
		
		if(timeNow-time>=Util.timeToPrune)
			return true;
		
		return false;
	}
	
}
