package com.snp.ui;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;

import com.snp.service.WeatherDataService;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.server.ClassResource;
import com.vaadin.server.VaadinRequest;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Image;
import com.vaadin.ui.Label;
import com.vaadin.ui.NativeSelect;
import com.vaadin.ui.Notification;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

@SpringUI(path="/weatherapp")
public class DefaultUI extends UI {

	@Autowired
	private WeatherDataService weatherDataService;
	
	private VerticalLayout verticalLayout,mainDescription1,mainDescription2;	
	
	private HorizontalLayout horizontalLayout,dashboardLayout,descriptionLayout;
	
	private NativeSelect<String> unitSelect;
	
	private TextField cityTextField;
	
	private Button searchButton;
	
	private Label cityName,currentTemp,pressure,weatherDescription,humidity,windSpeed;
	
	private String defaultUnit;
	
	@Override
	protected void init(VaadinRequest request) {
		
		mainUI();
		setHeader();
		setLogo();
		setForm();
		dashboardTitle();
		dashboardDetails();
		this.defaultUnit="\u00b0"+"C";
		searchButton.addClickListener(clickEvent->
		{
			if(!cityTextField.getValue().equals(""))
			{
				try
				{
					updateUI();
				}
				catch (JSONException e) {
					e.printStackTrace();
				}
			}
			else
			{
				Notification.show("City Name is Empty");
			}
		});
	}

	private void updateUI() {
		
		String city=cityTextField.getValue();
		cityName.setValue("City : "+city);
		String unit="",speedUnit="";
		
		
		
		if(unitSelect.getValue().equals(("Celsius")))
		{
			weatherDataService.setUnit("metric");
			unitSelect.setValue("Celsius");
			unit="C";
			speedUnit="m/sec";
			
		}
		
		else
		{
			weatherDataService.setUnit("imperial");
			unitSelect.setValue("Fahrenite");
			unit="F";
			speedUnit="km/hr";
		}
		
		weatherDataService.setCity(city);
		
		JSONObject mainObject=weatherDataService.getWeatherMain();
		if(mainObject!=null)
		{
			JSONObject windObject=weatherDataService.getWeatherWind();
			JSONArray weatherArray=weatherDataService.getWeatherArray();
			
			String weatherDesc="";
			
			for(int i=0;i<weatherArray.length();i++)
			{
				JSONObject jo=weatherArray.getJSONObject(i);
				weatherDesc=jo.getString("description");
				
			}
			
			double temp=mainObject.getDouble("temp");
			double press=mainObject.getDouble("pressure");
			double humid=mainObject.getDouble("humidity");
			double speed=windObject.getDouble("speed");
			currentTemp.setValue("Temperature :"+temp+"\u00b0"+unit);
			pressure.setValue("Pressure :"+press+"hPa");
			humidity.setValue("Humidity :"+humid+"%");
			windSpeed.setValue("Wind Speed :"+speed+speedUnit);
			weatherDescription.setValue("Description :"+weatherDesc);
		}
		else
		{
			Notification.show("City Not found");
		}
		
		
		
	}

	private void mainUI() {
		
		verticalLayout=new VerticalLayout();
		
		verticalLayout.setWidth("100%");
		verticalLayout.setSpacing(true);
		verticalLayout.setMargin(true);
		verticalLayout.setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);
		
		setContent(verticalLayout);
		
	}
	
	public void setHeader() 
	{
		horizontalLayout=new HorizontalLayout();
				
		horizontalLayout.setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);
		
		Label heading=new Label("Weather Monitoring App");
		
		heading.addStyleName(ValoTheme.LABEL_H1);
		
		heading.addStyleName(ValoTheme.LABEL_COLORED);
		
		heading.addStyleName(ValoTheme.LABEL_BOLD);
			
		horizontalLayout.addComponent(heading);
		
		verticalLayout.addComponents(horizontalLayout);
		
		
	}
	
	private void setLogo()
	{
		HorizontalLayout logoHorizontalLayout=new HorizontalLayout();
		
		logoHorizontalLayout.setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);
		
		Image logo=new Image(null,new ClassResource("/static/img.png"));
		
		logoHorizontalLayout.setWidth("240px");
		logoHorizontalLayout.setHeight("240px");
		
		logoHorizontalLayout.addComponent(logo);
		verticalLayout.addComponents(logoHorizontalLayout);
		
	}
	
	private void setForm()
	{
		HorizontalLayout formLayout=new HorizontalLayout();
		formLayout.setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);
		formLayout.setSpacing(true);
		formLayout.setMargin(true);
		
		unitSelect=new NativeSelect<>();
		
		List<String> items=new ArrayList<>();
		items.add("Celsius");
		items.add("Fahrenite");
		unitSelect.setItems(items);
		unitSelect.setValue(items.get(0));
		formLayout.addComponent(unitSelect);
		
		cityTextField=new TextField();
		cityTextField.setWidth("50%");
		formLayout.addComponent(cityTextField);
		
		searchButton=new Button();
		searchButton.setIcon(VaadinIcons.SEARCH);
		formLayout.addComponent(searchButton);
		
		
		verticalLayout.addComponents(formLayout);
	}
	
	private void dashboardTitle()
	{
		dashboardLayout=new HorizontalLayout();
		dashboardLayout.setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);
		
		cityName=new Label("City :");
		cityName.addStyleName(ValoTheme.LABEL_LIGHT);
		cityName.addStyleName(ValoTheme.LABEL_H2);
		
		
		currentTemp=new Label("Temp :"+"\u00b0"+"C");
		currentTemp.addStyleName(ValoTheme.LABEL_H2);
		currentTemp.addStyleName(ValoTheme.LABEL_BOLD);
		
		dashboardLayout.addComponent(cityName);
		dashboardLayout.addComponent(currentTemp);
		
		verticalLayout.addComponents(dashboardLayout);
		
		
	}
	
	private void dashboardDetails()
	{
		mainDescription1=new VerticalLayout();
		mainDescription1.setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);
		
		mainDescription2=new VerticalLayout();
		mainDescription2.setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);
		
		descriptionLayout=new HorizontalLayout();
		descriptionLayout.setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);
		
		
		
		weatherDescription=new Label("Weather Description   ");
		weatherDescription.setStyleName(ValoTheme.LABEL_H3);
		mainDescription1.addComponents(weatherDescription);
		
		humidity=new Label("Humidity   ");
		humidity.setStyleName(ValoTheme.LABEL_H3);
		mainDescription1.addComponents(humidity);
		
		
		pressure=new Label("Pressure   ");
		pressure.setStyleName(ValoTheme.LABEL_H3);
		mainDescription2.addComponents(pressure);
		
		windSpeed=new Label("Wind Speed   ");
		windSpeed.setStyleName(ValoTheme.LABEL_H3);	
		mainDescription2.addComponents(windSpeed);
		
		descriptionLayout.addComponents(mainDescription1,mainDescription2);
		
		verticalLayout.addComponent(descriptionLayout);
		
		
	}
	
	
	
	
	

}
