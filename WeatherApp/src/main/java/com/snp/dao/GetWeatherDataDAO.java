package com.snp.dao;

import java.io.IOException;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

@Repository
public class GetWeatherDataDAO {
	
	

	
	private OkHttpClient okHttpClient;
	private Response response;
	private String cityName;
	private String unitName;
	private String apiKey="b56dc30933a1298ba98210941fb9a25e";
	
	public JSONObject getWeather()
	{
		okHttpClient=new OkHttpClient();
		Request request=new Request.Builder()
				.url("http://api.openweathermap.org/data/2.5/weather?q="+getCityName()+"&units="+getUnitName()+"&appid=b56dc30933a1298ba98210941fb9a25e")
				.build(); 
		
		try
		{
			response=okHttpClient.newCall(request).execute();
			return new JSONObject(response.body().string());
		} 
		catch (IOException e)
		{
			
			e.printStackTrace();
		}
		return null;
	}

	public OkHttpClient getOkHttpClient() {
		return okHttpClient;
	}

	public void setOkHttpClient(OkHttpClient okHttpClient) {
		this.okHttpClient = okHttpClient;
	}

	public String getCityName() {
		return cityName;
	}

	public void setCityName(String cityName) {
		this.cityName = cityName;
	}
	
	public String getUnitName() {
		return unitName;
	}

	public void setUnitName(String unitName) {
		this.unitName = unitName;
	}
	

}
