package com.pgy.imageapi;

import java.util.HashMap;

import org.json.JSONObject;

import com.baidu.aip.imagesearch.AipImageSearch;

public class Sample {
	
	//设置APPID/AK/SK
    public static final String APP_ID = "16043741";
    public static final String API_KEY = "W0sW7sEiBprm8QmibSSM0cmf";
    public static final String SECRET_KEY = "jXSuGeFmGGBfyYmwTrRl1srmDoDhGhyG";
    
    public static AipImageSearch getClient() {
    	
    	// 初始化一个AipImageSearch
        AipImageSearch client = new AipImageSearch(APP_ID, API_KEY, SECRET_KEY);
        
        
        client.setSocketTimeoutInMillis(6000);
        
        return client;
    }
    
    public String SearchSamePhoto(String imageUrl) {
    	HashMap<String, String> options = new HashMap<String, String>();
    	AipImageSearch client = getClient();
    	options.put("tags", "100,11");
        options.put("tag_logic", "0");
        options.put("pn", "100");
        options.put("rn", "250");
        
        JSONObject res = client.sameHqSearch(imageUrl,options);
        
        return res.toString(2);
    }
    public static void main(String[] args) {
        
    	Sample s = new Sample();
        
        // 调用接口
        String path = "F://test.jpg";
        
        /*String result = s.SearchSamePhoto(path);
    
        System.out.println(result);*/
    }
	
}
