package com.example.servingwebcontent;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URL;
import java.net.URLConnection;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;

import org.json.JSONObject;
import org.json.JSONArray;
import org.json.simple.parser.ParseException;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class GreetingController {



	@GetMapping("/greeting")
	public String greeting(@RequestParam(name="name", required=false, defaultValue="World") String name, Model model) {
		model.addAttribute("name", name);
		return "greeting";
	}
	
	@GetMapping("/index")
	private String gre(@RequestParam(name="name", required=false, defaultValue="World") String name, Model model) {
		model.addAttribute("name", name);
		return "greeting";
	}
	
	@RequestMapping("/city/{LOCATION}")
    public String get_weather_city_name(@PathVariable String LOCATION) throws ParseException {
        //String API_KEY = "ce490961c6mshc593f5f415ee002p1dc26bjsn1776dba774f9";
        //API_KEY = "ce490961c6mshc593f5f415ee002p1dc26bjsn1776dba774f9";
        //String API_KEY = "f3f74c7f7d8f187ffe331859469e462c";
        String API_KEY = "4fa8fc5c7d7981b995adddbf79e5b964";
        System.out.println("Location: " + LOCATION);
        
        //Celcius
        String urlString = "http://api.openweathermap.org/data/2.5/weather?q=" + LOCATION + "&appid=" + API_KEY + "&units=metric";
        //Farenheit
        // String urlString = "http://api.openweathermap.org/data/2.5/weather?q=" + LOCATION + "&appid=" + API_KEY + "&units=imperial";

        StringBuilder result = new StringBuilder();

        String temp = "";
        try {

            URL url = new URL(urlString);
            URLConnection conn = url.openConnection();
            BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));

            String json = "";
            String line = "";
            while ((line = rd.readLine()) != null) {
                result.append(line);
                json += line;
            }

            JSONObject jsonObject = new JSONObject(json);

            // "coord"
            JSONObject JSONObject_coord = jsonObject.getJSONObject("coord");
            Double result_lon = JSONObject_coord.getDouble("lon");
            Double result_lat = JSONObject_coord.getDouble("lat");

            // "sys"
            JSONObject JSONObject_sys = jsonObject.getJSONObject("sys");
            String result_country = JSONObject_sys.getString("country");
            int result_sunrise = JSONObject_sys.getInt("sunrise");
            int result_sunset = JSONObject_sys.getInt("sunset");

            // "weather"
            String result_weather;
            JSONArray JSONArray_weather = jsonObject.getJSONArray("weather");
            if (JSONArray_weather.length() > 0) {
                JSONObject JSONObject_weather = JSONArray_weather.getJSONObject(0);
                int result_id = JSONObject_weather.getInt("id");
                String result_main = JSONObject_weather.getString("main");
                String result_description = JSONObject_weather.getString("description");
                String result_icon = JSONObject_weather.getString("icon");

                result_weather = "weather\tid: " + result_id + "\tmain: " + result_main + "\tdescription: "
                        + result_description + "\ticon: " + result_icon;
            } else {
                result_weather = "weather empty!";
            }

            // "base"
            String result_base = jsonObject.getString("base");

            // "main"
            JSONObject JSONObject_main = jsonObject.getJSONObject("main");
            Double result_temp = JSONObject_main.getDouble("temp");
            Double result_pressure = JSONObject_main.getDouble("pressure");
            Double result_humidity = JSONObject_main.getDouble("humidity");
            Double result_temp_min = JSONObject_main.getDouble("temp_min");
            Double result_temp_max = JSONObject_main.getDouble("temp_max");

            // "wind"
            JSONObject JSONObject_wind = jsonObject.getJSONObject("wind");
            Double result_speed = JSONObject_wind.getDouble("speed");
            // Double result_gust = JSONObject_wind.getDouble("gust");
            Double result_deg = JSONObject_wind.getDouble("deg");
            String result_wind = "wind\tspeed: " + result_speed + "\tdeg: " + result_deg;

            // "clouds"
            JSONObject JSONObject_clouds = jsonObject.getJSONObject("clouds");
            int result_all = JSONObject_clouds.getInt("all");

            // "dt"
            int result_dt = jsonObject.getInt("dt");

            // "id"
            int result_id = jsonObject.getInt("id");

            // "name"
            String result_name = jsonObject.getString("name");

            // "cod"
            int result_cod = jsonObject.getInt("cod");

            temp = "coord\tlon: " + result_lon + "\tlat: " + result_lat + "\n" + "sys\tcountry: " + result_country
                    + "\tsunrise: " + result_sunrise + "\tsunset: " + result_sunset + "\n" + result_weather + "\n"
                    + "base: " + result_base + "\n" + "main\ttemp: " + result_temp + "\thumidity: " + result_humidity
                    + "\tpressure: " + result_pressure + "\ttemp_min: " + result_temp_min + "\ttemp_max: "
                    + result_temp_max + "\n" + result_wind + "\n" + "clouds\tall: " + result_all + "\n" + "dt: "
                    + result_dt + "\n" + "id: " + result_id + "\n" + "name: " + result_name + "\n" + "cod: "
                    + result_cod + "\n" + "\n";

        } catch (

        IOException e) {
            System.out.println(e.getMessage());
        }

        return temp;
    }
	
	@ResponseBody 
    @RequestMapping("/location/{LATLON}")
    public ModelAndView get_weather_coordination(@PathVariable String LATLON) throws ParseException {

	    ModelAndView returnPage = new ModelAndView();

        //String API_KEY = "ce490961c6mshc593f5f415ee002p1dc26bjsn1776dba774f9";
        //API_KEY = "ce490961c6mshc593f5f415ee002p1dc26bjsn1776dba774f9";
        String API_KEY = "f3f74c7f7d8f187ffe331859469e462c";
        System.out.println("Latitude and Longtitude: " + LATLON);
        
        // String unit = "imperial";
        String UNIT = "metric";

        String LAT = LATLON.split(",")[0].trim();
        String LON = LATLON.split(",")[1].trim();

        //String urlString = "http://api.openweathermap.org/data/2.5/weather?lat=" + LAT + "&lon=" + LON + "&appid=" + API_KEY + "&units=" + UNIT;
        String urlString = "http://api.openweathermap.org/data/2.5/onecall?lat=" + LAT + "&lon=" + LON + "&appid=" + API_KEY + "&units=" + UNIT;


        StringBuilder stringBuilder = new StringBuilder();

        String tempString = "";

        try {
            URL url = new URL(urlString);
            URLConnection conn = url.openConnection();
            BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));

            String json = "";
            String line = "";

            while ((line = rd.readLine()) != null) {
                stringBuilder.append(line);
                json += line;
            }

            // ************** //
            JSONObject jsonObject = new JSONObject(json);

            double lon = jsonObject.getDouble("lon");
            double lat = jsonObject.getDouble("lat");
            String timezone = jsonObject.getString("timezone");
            int timezone_offset = jsonObject.getInt("timezone_offset");

            /* MAHNAZ'S VERSION
            // "coord"
            JSONObject JSONObject_coord = jsonObject.getJSONObject("coord");
            Double result_lon = JSONObject_coord.getDouble("lon");
            Double result_lat = JSONObject_coord.getDouble("lat");
            */ // MAHNAZ'S VERSION


            // "current"
            JSONObject JSONObject_current = jsonObject.getJSONObject("current");

            int dt = JSONObject_current.getInt("dt");
            int sunrise = JSONObject_current.getInt("sunrise");
            int sunset = JSONObject_current.getInt("sunset");
            double temp = JSONObject_current.getDouble("temp");
            double feels_like = JSONObject_current.getDouble("feels_like");
            int pressure = JSONObject_current.getInt("pressure");
            int humidity = JSONObject_current.getInt("humidity");
            double dew_point = JSONObject_current.getDouble("dew_point");
            double uvi = JSONObject_current.getDouble("uvi");
            int cloudinessPercentage = JSONObject_current.getInt("clouds");
            int visibilityInMeters = JSONObject_current.getInt("visibility");
            double wind_speed = JSONObject_current.getDouble("wind_speed");
            int windDirectionDegrees = JSONObject_current.getInt("wind_deg");

            /*
            int minutelyDt[];
            double minutelyPrecipitation[];

            int hourlyDt[];
            double hourlyTemp[];
            double hourlyFeels_like[];
            int hourlyPressure[];
            int hourlyHumidity[];
            double hourlyDew_point[];
            int hourlyClouds[];
            int hourlyVisibility[];
            */



            // "weather" in "current"
            String weatherString;
            JSONArray JSONArray_weather = JSONObject_current.getJSONArray("weather");
            if (JSONArray_weather.length() > 0) {
                JSONObject JSONObject_weather = JSONArray_weather.getJSONObject(0);
                int weather_id = JSONObject_weather.getInt("id");
                String weather_main = JSONObject_weather.getString("main");
                String weather_description = JSONObject_weather.getString("description");
                String weather_icon = JSONObject_weather.getString("icon");

                weatherString = "weather\tid: " + weather_id + "\tmain: " + weather_main + "\tdescription: "
                        + weather_description + "\ticon: " + weather_icon;

            } else {
                weatherString = "weather empty!";
            }



            /*
            // "sys"
            JSONObject JSONObject_sys = jsonObject.getJSONObject("sys");
            String result_country = JSONObject_sys.getString("country");
            int result_sunrise = JSONObject_sys.getInt("sunrise");
            int result_sunset = JSONObject_sys.getInt("sunset");
            */



            /* MAHNAZ'S VERSION
            // "weather"
            String result_weather;
            JSONArray JSONArray_weather = jsonObject.getJSONArray("weather");
            if (JSONArray_weather.length() > 0) {
                JSONObject JSONObject_weather = JSONArray_weather.getJSONObject(0);
                int result_id = JSONObject_weather.getInt("id");
                String result_main = JSONObject_weather.getString("main");
                String result_description = JSONObject_weather.getString("description");
                String result_icon = JSONObject_weather.getString("icon");

                result_weather = "weather\tid: " + result_id + "\tmain: " + result_main + "\tdescription: "
                        + result_description + "\ticon: " + result_icon;
            } else {
                result_weather = "weather empty!";
            }
            */ // MAHNAZ'S VERSION


            /*

            // "base"
            String result_base = jsonObject.getString("base");

            // "main"
            JSONObject JSONObject_main = jsonObject.getJSONObject("main");
            Double result_temp = JSONObject_main.getDouble("temp");
            Double result_pressure = JSONObject_main.getDouble("pressure");
            Double result_humidity = JSONObject_main.getDouble("humidity");
            Double result_temp_min = JSONObject_main.getDouble("temp_min");
            Double result_temp_max = JSONObject_main.getDouble("temp_max");

            // "wind"
            JSONObject JSONObject_wind = jsonObject.getJSONObject("wind");
            Double result_speed = JSONObject_wind.getDouble("speed");
            // Double result_gust = JSONObject_wind.getDouble("gust");
            Double result_deg = JSONObject_wind.getDouble("deg");
            String result_wind = "wind\tspeed: " + result_speed + "\tdeg: " + result_deg;

            // "clouds"
            JSONObject JSONObject_clouds = jsonObject.getJSONObject("clouds");
            int result_all = JSONObject_clouds.getInt("all");

            // "dt"
            int result_dt = jsonObject.getInt("dt");




            // "id"
            int result_id = jsonObject.getInt("id");

            // "name"
            String result_name = jsonObject.getString("name");

            // "cod"
            int result_cod = jsonObject.getInt("cod");

            */


            /* MAHNAZ'S VERSION
            tempString = "coord\tlon: " + result_lon + "\tlat: " + result_lat + "\n" + "sys\tcountry: " + result_country
                    + "\tsunrise: " + result_sunrise + "\tsunset: " + result_sunset + "\n" + result_weather + "\n"
                    + "base: " + result_base + "\n" + "main\ttemp: " + result_temp + "\thumidity: " + result_humidity
                    + "\tpressure: " + result_pressure + "\ttemp_min: " + result_temp_min + "\ttemp_max: "
                    + result_temp_max + "\n" + result_wind + "\n" + "clouds\tall: " + result_all + "\n" + "dt: "
                    + result_dt + "\n" + "id: " + result_id + "\n" + "name: " + result_name + "\n" + "cod: "
                    + result_cod + "\n" + "\n";

             */ // MAHNAZ'S VERSION

            returnPage.addObject("latitude", lat);
            returnPage.addObject("longitude", lon);
            /*
            returnPage.addObject("country", result_country);
            returnPage.addObject("sunrise", result_sunrise);
            returnPage.addObject("sunset", result_sunset);
            returnPage.addObject("base", result_base);
            returnPage.addObject("temperature", result_temp);
            returnPage.addObject("humidity", result_humidity);
            returnPage.addObject("pressure", result_pressure);
            returnPage.addObject("minimum_temperature", result_temp_min);
            returnPage.addObject("maximum_temperature", result_temp_max);
            returnPage.addObject("wind", result_wind);
             */


            returnPage.setViewName("mainpage");

        } catch (

        IOException e) {
            System.out.println(e.getMessage());
        }

        return returnPage;
    }

}
