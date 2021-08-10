package com.example.demo;

import java.util.HashMap;

import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@WebMvcTest
@ContextConfiguration(classes = {Key.class,Pair.class,PostActivity.class,GetActivityTotal.class,Util.class})
class ActivityapiApplicationTests {

	@Autowired
	private MockMvc mockMVC;
	
	HashMap<String,Key> keys = new HashMap<>();
	
	@MockBean
	ActivityapiApplication appl;
	
	@MockBean
	Util util;
	
	
	//testing wrong parameter
	@Test
	void PostAcitivityTest1(){
		
		RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/activity/test").content("{ \"valu\" : 50 }").contentType(MediaType.APPLICATION_JSON);
		
		try {
		MvcResult result = mockMVC.perform(requestBuilder).andReturn();
		String expected = "{\"Error\" : \"'value' parameter is required\"}";
		
		JSONAssert.assertEquals(expected, result.getResponse().getContentAsString(),false);
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	//testing wrong value type
	
	@Test
	void PostAcitivityTest2(){
		
		RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/activity/test").content("{ \"value\" : \"test\" }").contentType(MediaType.APPLICATION_JSON);
		
		try {
		MvcResult result = mockMVC.perform(requestBuilder).andReturn();
		String expected = "{\"Error\" : \"'value' parameter must be numberic\"}";
		
		JSONAssert.assertEquals(expected, result.getResponse().getContentAsString(),false);
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	//Testing insertion of new activity and value into keys(in memory data structure)
	
	@Test
	void PostAcitivityTest3(){
		
		Mockito.when(appl.getKeys()).thenReturn(keys);
		RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/activity/test").contentType(MediaType.APPLICATION_JSON).content("{ \"value\" : 50 }").accept(MediaType.APPLICATION_JSON);
		
		try {
		
			mockMVC.perform(requestBuilder).andReturn();
		    Assert.assertTrue("test failed",keys.get("test").getKey().equals("test")&&keys.get("test").getTotal()==50);
			
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	//Testing getting activity values with a value already present.
	
	@Test
	void GetActivityTest1() {
		Key k = new Key();
		k.setKey("name");
		k.setTotal(50);
		keys.put("name", k);
		Mockito.when(appl.getKeys()).thenReturn(keys);
		RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/activity/name/total").accept(MediaType.APPLICATION_JSON);
		
		try {
		
			MvcResult result = mockMVC.perform(requestBuilder).andReturn();
			String expected = "{\"value\": 50}";
			JSONAssert.assertEquals(expected, result.getResponse().getContentAsString(),true);
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	//
	//Testing getting activity values with no value present.
	
	@Test
	void GetActivityTest2() {
		Key k = new Key();
		k.setKey("name");
		k.setTotal(50);
		keys.put("name", k);
		Mockito.when(appl.getKeys()).thenReturn(keys);
		RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/activity/test/total").accept(MediaType.APPLICATION_JSON);
		
		try {
		
			MvcResult result = mockMVC.perform(requestBuilder).andReturn();
			String expected = "{\"value\": 0}";
			JSONAssert.assertEquals(expected, result.getResponse().getContentAsString(),true);
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	@Test
	void TestPruning() {
		keys.clear();
		Key k = new Key();
		k.setKey("name");
		k.setTotal(100);
		keys.put("name", k);
		
		Mockito.when(appl.getKeys()).thenReturn(keys);
		Mockito.when(appl.toBePruned(Mockito.anyLong(),Mockito.anyLong())).thenReturn(true);
		
		appl.pruneOlderData();
		
		Assert.assertEquals(k.getActivities().size(),0);
		
	}
		
		
	
}
