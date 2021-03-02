package com.snp.service;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.snp.dao.GetWeatherDataDAO;

@Service
public class WeatherDataService {
	
	@Autowired
	private GetWeatherDataDAO getWeatherDataDAO;
	
	public JSONArray getWeatherArray()
	{
		JSONArray weatherArray=getWeatherDataDAO.getWeather().getJSONArray("weather");
		return weatherArray;
		
	}
	
	public JSONObject getWeatherMain()
	{
		try
		{
		JSONObject main=getWeatherDataDAO.getWeather().getJSONObject("main");
		return main;
		}
		catch (JSONException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public JSONObject getWeatherWind()
	{
		JSONObject wind=getWeatherDataDAO.getWeather().getJSONObject("wind");
		return wind;
	}
	
	public void setCity(String city)
	{
		getWeatherDataDAO.setCityName(city);
	}
	
	public void setUnit(String unit)
	{
		getWeatherDataDAO.setUnitName(unit);
	}
	
	

}
