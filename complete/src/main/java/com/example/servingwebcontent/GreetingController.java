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
import java.text.SimpleDateFormat;
import java.util.Date;

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


    public String unixToNormal(long unixSeconds)
    {
        // convert seconds to milliseconds
        Date date = new java.util.Date(unixSeconds*1000L);
        // the format of your date
        SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss z EEEE");
        // give a timezone reference for formatting (see comment at the bottom)
        // old: sdf.setTimeZone(java.util.TimeZone.getTimeZone("GMT-4"));
        //sdf.setTimeZone(java.util.TimeZone.getTimeZone("EST")); *****
        String formattedDate = sdf.format(date);
        System.out.println(formattedDate);

        return formattedDate;
    }




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
        String urlString = "http://api.openweathermap.org/data/2.5/onecall?lat=" + LAT + "&lon=" + LON + "&appid=" + API_KEY + "&units=imperial"; //+ UNIT;


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
            JSONObject JSONObject_openWeatherAPIResponse = new JSONObject(json);

            double lon = JSONObject_openWeatherAPIResponse.getDouble("lon");
            double lat = JSONObject_openWeatherAPIResponse.getDouble("lat");
            String timezone = JSONObject_openWeatherAPIResponse.getString("timezone");
            int timezone_offset = JSONObject_openWeatherAPIResponse.getInt("timezone_offset");

            /* MAHNAZ'S VERSION
            // "coord"
            JSONObject JSONObject_coord = jsonObject.getJSONObject("coord");
            Double result_lon = JSONObject_coord.getDouble("lon");
            Double result_lat = JSONObject_coord.getDouble("lat");
            */ // MAHNAZ'S VERSION


            // *********************************************************************************************************
            // *********************************************************************************************************
            // *********************************************************************************************************
            //                                                                                                      ****
            //                                        C U R R E N T                                                 ****
            //                                                                                                      ****
            // *********************************************************************************************************
            // *********************************************************************************************************
            // *********************************************************************************************************
            JSONObject JSONObject_current = JSONObject_openWeatherAPIResponse.getJSONObject("current");


            int current_dt = JSONObject_current.getInt("dt");
            int current_dt_adjusted = current_dt + timezone_offset;
            int current_sunrise = JSONObject_current.getInt("sunrise");
            int current_sunrise_adjusted = current_sunrise + timezone_offset;
            int current_sunset = JSONObject_current.getInt("sunset");
            int current_sunset_adjusted = current_sunset + timezone_offset;
            double current_temp = JSONObject_current.getDouble("temp");
            double current_feels_like = JSONObject_current.getDouble("feels_like");
            int current_pressure = JSONObject_current.getInt("pressure");
            int current_humidity = JSONObject_current.getInt("humidity");
            double current_dew_point = JSONObject_current.getDouble("dew_point");
            double current_uvi = JSONObject_current.getDouble("uvi");
            int current_clouds = JSONObject_current.getInt("clouds");
            int current_visibility = JSONObject_current.getInt("visibility");
            double current_wind_speed = JSONObject_current.getDouble("wind_speed");
            int current_wind_deg = JSONObject_current.getInt("wind_deg");


            JSONObject JSONObject_current_weather;
            int current_weather_id;
            String current_weather_main;
            String current_weather_description;
            String current_weather_icon;

            JSONArray JSONArray_current_weather = JSONObject_current.getJSONArray("weather");
            //if (JSONArray_current_weather.length() > 0){
                JSONObject_current_weather = JSONArray_current_weather.getJSONObject(0);
                current_weather_id = JSONObject_current_weather.getInt("id");
                current_weather_main = JSONObject_current_weather.getString("main");
                current_weather_description = JSONObject_current_weather.getString("description");
                current_weather_icon = JSONObject_current_weather.getString("icon");
            //}
            //else
            //{
            //    System.out.println("ERROR LINE 238 WEATHER ARRAY EMPTY");
            //}


            /*
            JSONArray JSONArray_minutely = JSONObject_openWeatherAPIResponse.getJSONArray("minutely");
            int[] minutely_dt;
            double[] minutely_precipitation;
            */


            // *********************************************************************************************************
            // *********************************************************************************************************
            // *********************************************************************************************************
            //                                                                                                      ****
            //                                            H O U R L Y                                               ****
            //                                                                                                      ****
            // *********************************************************************************************************
            // *********************************************************************************************************
            // *********************************************************************************************************

             final int numOfHours = 10;

            JSONArray JSONArray_hourly = JSONObject_openWeatherAPIResponse.getJSONArray("hourly");
            // The whole hourly array is passed to the JSONArray, JSONArray_hourly, in the above line.

            int[] hourly_dt = new int[numOfHours];
            int[] hourly_dt_adjusted = new int[numOfHours];
            double[] hourly_temp = new double[numOfHours];
            double[] hourly_feels_like = new double[numOfHours];
            int[] hourly_pressure = new int[numOfHours];
            int[] hourly_humidity = new int[numOfHours];
            double[] hourly_dew_point = new double[numOfHours];
            int[] hourly_clouds = new int[numOfHours];
            int[] hourly_visibility = new int[numOfHours];
            double[] hourly_wind_speed = new double[numOfHours];
            int[] hourly_wind_deg = new int[numOfHours];

            int[] hourly_weather_id = new int[numOfHours];
            String[] hourly_weather_main = new String[numOfHours];
            String[] hourly_weather_description = new String[numOfHours];
            String[] hourly_weather_icon = new String[numOfHours];

            double[] hourly_pop = new double[numOfHours];

            // ten hours will be enough
            for (int i = 0; i < numOfHours; i++)
            {
                hourly_dt[i] = JSONArray_hourly.getJSONObject(i).getInt("dt");
                // convert to local time
                hourly_dt_adjusted[i] = hourly_dt[i] + timezone_offset;
                hourly_temp[i] = JSONArray_hourly.getJSONObject(i).getDouble("temp");
                hourly_feels_like[i] = JSONArray_hourly.getJSONObject(i).getDouble("feels_like");
                hourly_pressure[i] = JSONArray_hourly.getJSONObject(i).getInt("pressure");
                hourly_humidity[i] = JSONArray_hourly.getJSONObject(i).getInt("humidity");
                hourly_dew_point[i] = JSONArray_hourly.getJSONObject(i).getDouble("dew_point");
                hourly_clouds[i] = JSONArray_hourly.getJSONObject(i).getInt("clouds");
                hourly_visibility[i] = JSONArray_hourly.getJSONObject(i).getInt("visibility");
                hourly_wind_speed[i] = JSONArray_hourly.getJSONObject(i).getDouble("wind_speed");
                hourly_wind_deg[i] = JSONArray_hourly.getJSONObject(i).getInt("wind_deg");
                hourly_pop[i] = JSONArray_hourly.getJSONObject(i).getDouble("pop");

                // I think these next four lines are right but if there's an error check these
                hourly_weather_id[i] = JSONArray_hourly.getJSONObject(i).getJSONArray("weather").getJSONObject(0).getInt("id");
                hourly_weather_main[i] = JSONArray_hourly.getJSONObject(i).getJSONArray("weather").getJSONObject(0).getString("main");
                hourly_weather_description[i] = JSONArray_hourly.getJSONObject(i).getJSONArray("weather").getJSONObject(0).getString("description");
                hourly_weather_icon[i] = JSONArray_hourly.getJSONObject(i).getJSONArray("weather").getJSONObject(0).getString("icon");
            }


            // *********************************************************************************************************
            // *********************************************************************************************************
            // *********************************************************************************************************
            //                                                                                                      ****
            //                                            D A I L Y                                                 ****
            //                                                                                                      ****
            // *********************************************************************************************************
            // *********************************************************************************************************
            // *********************************************************************************************************

            final int numOfDays = 7;

            JSONArray JSONArray_daily = JSONObject_openWeatherAPIResponse.getJSONArray("daily");
            // The whole daily array is passed to the JSONArray, JSONArray_hourly, in the above line.

            int[] daily_dt = new int[numOfDays];
            int[] daily_dt_adjusted = new int[numOfDays];
            int[] daily_sunrise = new int[numOfDays];
            int[] daily_sunrise_adjusted = new int[numOfDays];
            int[] daily_sunset = new int[numOfDays];
            int[] daily_sunset_adjusted = new int[numOfDays];

            double[] daily_temp_day = new double[numOfDays];
            double[] daily_temp_min = new double[numOfDays];
            double[] daily_temp_max = new double[numOfDays];
            double[] daily_temp_night = new double[numOfDays];
            double[] daily_temp_eve = new double[numOfDays];
            double[] daily_temp_morn = new double[numOfDays];

            double[] daily_feels_like_day = new double[numOfDays];
            double[] daily_feels_like_night = new double[numOfDays];
            double[] daily_feels_like_eve = new double[numOfDays];
            double[] daily_feels_like_morn = new double[numOfDays];

            int[] daily_pressure = new int[numOfDays];
            int[] daily_humidity = new int[numOfDays];
            double[] daily_dew_point = new double[numOfDays];
            double[] daily_wind_speed = new double[numOfDays];
            int[] daily_wind_deg = new int[numOfDays];

            int[] daily_weather_id = new int[numOfDays];
            String[] daily_weather_main = new String[numOfDays];
            String[] daily_weather_description = new String[numOfDays];
            String[] daily_weather_icon = new String[numOfDays];

            int[] daily_clouds = new int[numOfDays];
            double[] daily_pop = new double[numOfDays]; // *********
            double[] daily_rain = new double[numOfDays];
            double[] daily_uvi = new double[numOfDays];



            // seven days
            for (int i = 0; i < numOfDays; i++)
            {
                daily_dt[i] = JSONArray_daily.getJSONObject(i).getInt("dt");
                daily_dt_adjusted[i] = daily_dt[i] + timezone_offset;
                daily_sunrise[i] = JSONArray_daily.getJSONObject(i).getInt("sunrise");
                daily_sunrise_adjusted[i] = daily_sunrise[i] + timezone_offset;
                daily_sunset[i] = JSONArray_daily.getJSONObject(i).getInt("sunset");
                daily_sunset_adjusted[i] = daily_sunset[i] + timezone_offset;

                daily_temp_day[i] = JSONArray_daily.getJSONObject(i).getJSONObject("temp").getDouble("day");
                daily_temp_min[i] = JSONArray_daily.getJSONObject(i).getJSONObject("temp").getDouble("min");
                daily_temp_max[i] = JSONArray_daily.getJSONObject(i).getJSONObject("temp").getDouble("max");
                daily_temp_night[i] = JSONArray_daily.getJSONObject(i).getJSONObject("temp").getDouble("night");
                daily_temp_eve[i] = JSONArray_daily.getJSONObject(i).getJSONObject("temp").getDouble("eve");
                daily_temp_morn[i] = JSONArray_daily.getJSONObject(i).getJSONObject("temp").getDouble("morn");


                daily_feels_like_day[i] = JSONArray_daily.getJSONObject(i).getJSONObject("feels_like").getDouble("day");
                daily_feels_like_night[i] = JSONArray_daily.getJSONObject(i).getJSONObject("feels_like").getDouble("night");
                daily_feels_like_eve[i] = JSONArray_daily.getJSONObject(i).getJSONObject("feels_like").getDouble("eve");
                daily_feels_like_morn[i] = JSONArray_daily.getJSONObject(i).getJSONObject("feels_like").getDouble("morn");


                daily_pressure[i] = JSONArray_daily.getJSONObject(i).getInt("pressure");
                daily_humidity[i] = JSONArray_daily.getJSONObject(i).getInt("humidity");
                daily_dew_point[i] = JSONArray_daily.getJSONObject(i).getDouble("dew_point");
                daily_wind_speed[i] = JSONArray_daily.getJSONObject(i).getDouble("wind_speed");
                daily_wind_deg[i] = JSONArray_daily.getJSONObject(i).getInt("wind_deg");


                daily_weather_id[i] = JSONArray_daily.getJSONObject(i).getJSONArray("weather").getJSONObject(0).getInt("id");
                daily_weather_main[i] = JSONArray_daily.getJSONObject(i).getJSONArray("weather").getJSONObject(0).getString("main");
                daily_weather_description[i] = JSONArray_daily.getJSONObject(i).getJSONArray("weather").getJSONObject(0).getString("description");
                daily_weather_icon[i] = JSONArray_daily.getJSONObject(i).getJSONArray("weather").getJSONObject(0).getString("icon");


                daily_clouds[i] = JSONArray_daily.getJSONObject(i).getInt("clouds");
                daily_pop[i] = JSONArray_daily.getJSONObject(i).getDouble("pop");
                //daily_rain[i] = JSONArray_daily.getJSONObject(i).getDouble("rain");
                daily_uvi[i] = JSONArray_daily.getJSONObject(i).getDouble("uvi");
            }



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


            /*
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
            */


            /*
            // "sys"
            JSONObject JSONObject_sys = jsonObject.getJSONObject("sys");
            String result_country = JSONObject_sys.getString("country");
            int result_sunrise = JSONObject_sys.getInt("sunrise");
            int result_sunset = JSONObject_sys.getInt("sunset");
            */


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

            // *********************************************************************************************************
            // *********************************************************************************************************
            // *********************************************************************************************************
            //                                                                                                      ****
            //                                          C U R R E N T                                               ****
            //                                                                                                      ****
            // *********************************************************************************************************
            // *********************************************************************************************************
            // *********************************************************************************************************

            returnPage.addObject("lat", lat);
            returnPage.addObject("lon", lon);
            returnPage.addObject("timezone", timezone);
            returnPage.addObject("timezone_offset", timezone_offset);

            // Current
            returnPage.addObject("current_dt", current_dt);
            returnPage.addObject("current_dt_adjusted", current_dt_adjusted);
            returnPage.addObject("current_sunrise", current_sunrise);
            returnPage.addObject("current_sunrise_adjusted", current_sunrise_adjusted);
            returnPage.addObject("current_sunset", current_sunset);
            returnPage.addObject("current_sunset_adjusted", current_sunset_adjusted);
            returnPage.addObject("current_temp", current_temp);
            returnPage.addObject("current_feels_like", current_feels_like);
            returnPage.addObject("current_pressure", current_pressure);
            returnPage.addObject("current_humidity", current_humidity);
            returnPage.addObject("current_dew_point", current_dew_point);
            returnPage.addObject("current_uvi", current_uvi);
            returnPage.addObject("current_clouds", current_clouds);
            returnPage.addObject("current_visibility", current_visibility);
            returnPage.addObject("current_wind_speed", current_wind_speed);
            returnPage.addObject("current_wind_deg", current_wind_deg);

            returnPage.addObject("current_weather_id", current_weather_id);
            returnPage.addObject("current_weather_main", current_weather_main);
            returnPage.addObject("current_weather_description", current_weather_description);
            returnPage.addObject("current_weather_icon", current_weather_icon);




            // *********************************************************************************************************
            // *********************************************************************************************************
            // *********************************************************************************************************
            //                                                                                                      ****
            //                                            H O U R L Y                                               ****
            //                                                                                                      ****
            // *********************************************************************************************************
            // *********************************************************************************************************
            // *********************************************************************************************************
            // hourly_dt
            //returnPage.addObject("hourly_dt", hourly_dt);
            returnPage.addObject("hourly_dt0", hourly_dt[0]);
            returnPage.addObject("hourly_dt1", hourly_dt[1]);
            returnPage.addObject("hourly_dt2", hourly_dt[2]);
            returnPage.addObject("hourly_dt3", hourly_dt[3]);
            returnPage.addObject("hourly_dt4", hourly_dt[4]);
            returnPage.addObject("hourly_dt5", hourly_dt[5]);
            returnPage.addObject("hourly_dt6", hourly_dt[6]);
            returnPage.addObject("hourly_dt7", hourly_dt[7]);
            returnPage.addObject("hourly_dt8", hourly_dt[8]);
            returnPage.addObject("hourly_dt9", hourly_dt[9]);

            // hourly_dt_adjusted
            //returnPage.addObject("hourly_dt_adjusted", hourly_dt_adjusted);
            returnPage.addObject("hourly_dt_adjusted0", hourly_dt_adjusted[0]);
            returnPage.addObject("hourly_dt_adjusted1", hourly_dt_adjusted[1]);
            returnPage.addObject("hourly_dt_adjusted2", hourly_dt_adjusted[2]);
            returnPage.addObject("hourly_dt_adjusted3", hourly_dt_adjusted[3]);
            returnPage.addObject("hourly_dt_adjusted4", hourly_dt_adjusted[4]);
            returnPage.addObject("hourly_dt_adjusted5", hourly_dt_adjusted[5]);
            returnPage.addObject("hourly_dt_adjusted6", hourly_dt_adjusted[6]);
            returnPage.addObject("hourly_dt_adjusted7", hourly_dt_adjusted[7]);
            returnPage.addObject("hourly_dt_adjusted8", hourly_dt_adjusted[8]);
            returnPage.addObject("hourly_dt_adjusted9", hourly_dt_adjusted[9]);


            // hourly_temp
            //returnPage.addObject("hourly_temp", hourly_temp);
            returnPage.addObject("hourly_temp0", hourly_temp[0]);
            returnPage.addObject("hourly_temp1", hourly_temp[1]);
            returnPage.addObject("hourly_temp2", hourly_temp[2]);
            returnPage.addObject("hourly_temp3", hourly_temp[3]);
            returnPage.addObject("hourly_temp4", hourly_temp[4]);
            returnPage.addObject("hourly_temp5", hourly_temp[5]);
            returnPage.addObject("hourly_temp6", hourly_temp[6]);
            returnPage.addObject("hourly_temp7", hourly_temp[7]);
            returnPage.addObject("hourly_temp8", hourly_temp[8]);
            returnPage.addObject("hourly_temp9", hourly_temp[9]);


            // hourly_feels_like
            //returnPage.addObject("hourly_feels_like", hourly_feels_like);
            returnPage.addObject("hourly_feels_like0", hourly_feels_like[0]);
            returnPage.addObject("hourly_feels_like1", hourly_feels_like[1]);
            returnPage.addObject("hourly_feels_like2", hourly_feels_like[2]);
            returnPage.addObject("hourly_feels_like3", hourly_feels_like[3]);
            returnPage.addObject("hourly_feels_like4", hourly_feels_like[4]);
            returnPage.addObject("hourly_feels_like5", hourly_feels_like[5]);
            returnPage.addObject("hourly_feels_like6", hourly_feels_like[6]);
            returnPage.addObject("hourly_feels_like7", hourly_feels_like[7]);
            returnPage.addObject("hourly_feels_like8", hourly_feels_like[8]);
            returnPage.addObject("hourly_feels_like9", hourly_feels_like[9]);


            // hourly_pressure
            //returnPage.addObject("hourly_pressure", hourly_pressure);
            returnPage.addObject("hourly_pressure0", hourly_pressure[0]);
            returnPage.addObject("hourly_pressure1", hourly_pressure[1]);
            returnPage.addObject("hourly_pressure2", hourly_pressure[2]);
            returnPage.addObject("hourly_pressure3", hourly_pressure[3]);
            returnPage.addObject("hourly_pressure4", hourly_pressure[4]);
            returnPage.addObject("hourly_pressure5", hourly_pressure[5]);
            returnPage.addObject("hourly_pressure6", hourly_pressure[6]);
            returnPage.addObject("hourly_pressure7", hourly_pressure[7]);
            returnPage.addObject("hourly_pressure8", hourly_pressure[8]);
            returnPage.addObject("hourly_pressure9", hourly_pressure[9]);


            // hourly_humidity
            //returnPage.addObject("hourly_humidity", hourly_humidity);
            returnPage.addObject("hourly_humidity0", hourly_humidity[0]);
            returnPage.addObject("hourly_humidity1", hourly_humidity[1]);
            returnPage.addObject("hourly_humidity2", hourly_humidity[2]);
            returnPage.addObject("hourly_humidity3", hourly_humidity[3]);
            returnPage.addObject("hourly_humidity4", hourly_humidity[4]);
            returnPage.addObject("hourly_humidity5", hourly_humidity[5]);
            returnPage.addObject("hourly_humidity6", hourly_humidity[6]);
            returnPage.addObject("hourly_humidity7", hourly_humidity[7]);
            returnPage.addObject("hourly_humidity8", hourly_humidity[8]);
            returnPage.addObject("hourly_humidity9", hourly_humidity[9]);


            // hourly_dew_point
            //returnPage.addObject("hourly_dew_point", hourly_dew_point);
            returnPage.addObject("hourly_dew_point0", hourly_dew_point[0]);
            returnPage.addObject("hourly_dew_point1", hourly_dew_point[1]);
            returnPage.addObject("hourly_dew_point2", hourly_dew_point[2]);
            returnPage.addObject("hourly_dew_point3", hourly_dew_point[3]);
            returnPage.addObject("hourly_dew_point4", hourly_dew_point[4]);
            returnPage.addObject("hourly_dew_point5", hourly_dew_point[5]);
            returnPage.addObject("hourly_dew_point6", hourly_dew_point[6]);
            returnPage.addObject("hourly_dew_point7", hourly_dew_point[7]);
            returnPage.addObject("hourly_dew_point8", hourly_dew_point[8]);
            returnPage.addObject("hourly_dew_point9", hourly_dew_point[9]);


            // hourly_clouds
            //returnPage.addObject("hourly_clouds", hourly_clouds);
            returnPage.addObject("hourly_clouds0", hourly_clouds[0]);
            returnPage.addObject("hourly_clouds1", hourly_clouds[1]);
            returnPage.addObject("hourly_clouds2", hourly_clouds[2]);
            returnPage.addObject("hourly_clouds3", hourly_clouds[3]);
            returnPage.addObject("hourly_clouds4", hourly_clouds[4]);
            returnPage.addObject("hourly_clouds5", hourly_clouds[5]);
            returnPage.addObject("hourly_clouds6", hourly_clouds[6]);
            returnPage.addObject("hourly_clouds7", hourly_clouds[7]);
            returnPage.addObject("hourly_clouds8", hourly_clouds[8]);
            returnPage.addObject("hourly_clouds9", hourly_clouds[9]);


            // hourly_visibility
            // returnPage.addObject("hourly_visibility", hourly_visibility);
            returnPage.addObject("hourly_visibility0", hourly_visibility[0]);
            returnPage.addObject("hourly_visibility1", hourly_visibility[1]);
            returnPage.addObject("hourly_visibility2", hourly_visibility[2]);
            returnPage.addObject("hourly_visibility3", hourly_visibility[3]);
            returnPage.addObject("hourly_visibility4", hourly_visibility[4]);
            returnPage.addObject("hourly_visibility5", hourly_visibility[5]);
            returnPage.addObject("hourly_visibility6", hourly_visibility[6]);
            returnPage.addObject("hourly_visibility7", hourly_visibility[7]);
            returnPage.addObject("hourly_visibility8", hourly_visibility[8]);
            returnPage.addObject("hourly_visibility9", hourly_visibility[9]);


            // hourly_wind_speed
            // returnPage.addObject("hourly_wind_speed", hourly_wind_speed);
            returnPage.addObject("hourly_wind_speed0", hourly_wind_speed[0]);
            returnPage.addObject("hourly_wind_speed1", hourly_wind_speed[1]);
            returnPage.addObject("hourly_wind_speed2", hourly_wind_speed[2]);
            returnPage.addObject("hourly_wind_speed3", hourly_wind_speed[3]);
            returnPage.addObject("hourly_wind_speed4", hourly_wind_speed[4]);
            returnPage.addObject("hourly_wind_speed5", hourly_wind_speed[5]);
            returnPage.addObject("hourly_wind_speed6", hourly_wind_speed[6]);
            returnPage.addObject("hourly_wind_speed7", hourly_wind_speed[7]);
            returnPage.addObject("hourly_wind_speed8", hourly_wind_speed[8]);
            returnPage.addObject("hourly_wind_speed9", hourly_wind_speed[9]);


            // hourly_wind_deg
            // returnPage.addObject("hourly_wind_deg", hourly_wind_deg);
            returnPage.addObject("hourly_wind_deg0", hourly_wind_deg[0]);
            returnPage.addObject("hourly_wind_deg1", hourly_wind_deg[1]);
            returnPage.addObject("hourly_wind_deg2", hourly_wind_deg[2]);
            returnPage.addObject("hourly_wind_deg3", hourly_wind_deg[3]);
            returnPage.addObject("hourly_wind_deg4", hourly_wind_deg[4]);
            returnPage.addObject("hourly_wind_deg5", hourly_wind_deg[5]);
            returnPage.addObject("hourly_wind_deg6", hourly_wind_deg[6]);
            returnPage.addObject("hourly_wind_deg7", hourly_wind_deg[7]);
            returnPage.addObject("hourly_wind_deg8", hourly_wind_deg[8]);
            returnPage.addObject("hourly_wind_deg9", hourly_wind_deg[9]);



            // hourly_weather_id
            // returnPage.addObject("hourly_weather_id", hourly_weather_id);
            returnPage.addObject("hourly_weather_id0", hourly_weather_id[0]);
            returnPage.addObject("hourly_weather_id1", hourly_weather_id[1]);
            returnPage.addObject("hourly_weather_id2", hourly_weather_id[2]);
            returnPage.addObject("hourly_weather_id3", hourly_weather_id[3]);
            returnPage.addObject("hourly_weather_id4", hourly_weather_id[4]);
            returnPage.addObject("hourly_weather_id5", hourly_weather_id[5]);
            returnPage.addObject("hourly_weather_id6", hourly_weather_id[6]);
            returnPage.addObject("hourly_weather_id7", hourly_weather_id[7]);
            returnPage.addObject("hourly_weather_id8", hourly_weather_id[8]);
            returnPage.addObject("hourly_weather_id9", hourly_weather_id[9]);



            // hourly_weather_main
            // returnPage.addObject("hourly_weather_main", hourly_weather_main);
            returnPage.addObject("hourly_weather_main0", hourly_weather_main[0]);
            returnPage.addObject("hourly_weather_main1", hourly_weather_main[1]);
            returnPage.addObject("hourly_weather_main2", hourly_weather_main[2]);
            returnPage.addObject("hourly_weather_main3", hourly_weather_main[3]);
            returnPage.addObject("hourly_weather_main4", hourly_weather_main[4]);
            returnPage.addObject("hourly_weather_main5", hourly_weather_main[5]);
            returnPage.addObject("hourly_weather_main6", hourly_weather_main[6]);
            returnPage.addObject("hourly_weather_main7", hourly_weather_main[7]);
            returnPage.addObject("hourly_weather_main8", hourly_weather_main[8]);
            returnPage.addObject("hourly_weather_main9", hourly_weather_main[9]);


            // hourly_weather_description
            // returnPage.addObject("hourly_weather_description", hourly_weather_description);
            returnPage.addObject("hourly_weather_description0", hourly_weather_description[0]);
            returnPage.addObject("hourly_weather_description1", hourly_weather_description[1]);
            returnPage.addObject("hourly_weather_description2", hourly_weather_description[2]);
            returnPage.addObject("hourly_weather_description3", hourly_weather_description[3]);
            returnPage.addObject("hourly_weather_description4", hourly_weather_description[4]);
            returnPage.addObject("hourly_weather_description5", hourly_weather_description[5]);
            returnPage.addObject("hourly_weather_description6", hourly_weather_description[6]);
            returnPage.addObject("hourly_weather_description7", hourly_weather_description[7]);
            returnPage.addObject("hourly_weather_description8", hourly_weather_description[8]);
            returnPage.addObject("hourly_weather_description9", hourly_weather_description[9]);


            // hourly_weather_icon
            // returnPage.addObject("hourly_weather_icon", hourly_weather_icon);
            returnPage.addObject("hourly_weather_icon0", hourly_weather_icon[0]);
            returnPage.addObject("hourly_weather_icon1", hourly_weather_icon[1]);
            returnPage.addObject("hourly_weather_icon2", hourly_weather_icon[2]);
            returnPage.addObject("hourly_weather_icon3", hourly_weather_icon[3]);
            returnPage.addObject("hourly_weather_icon4", hourly_weather_icon[4]);
            returnPage.addObject("hourly_weather_icon5", hourly_weather_icon[5]);
            returnPage.addObject("hourly_weather_icon6", hourly_weather_icon[6]);
            returnPage.addObject("hourly_weather_icon7", hourly_weather_icon[7]);
            returnPage.addObject("hourly_weather_icon8", hourly_weather_icon[8]);
            returnPage.addObject("hourly_weather_icon9", hourly_weather_icon[9]);


            // hourly_pop
            // returnPage.addObject("hourly_pop", hourly_pop);
            returnPage.addObject("hourly_pop0", hourly_pop[0]);
            returnPage.addObject("hourly_pop1", hourly_pop[1]);
            returnPage.addObject("hourly_pop2", hourly_pop[2]);
            returnPage.addObject("hourly_pop3", hourly_pop[3]);
            returnPage.addObject("hourly_pop4", hourly_pop[4]);
            returnPage.addObject("hourly_pop5", hourly_pop[5]);
            returnPage.addObject("hourly_pop6", hourly_pop[6]);
            returnPage.addObject("hourly_pop7", hourly_pop[7]);
            returnPage.addObject("hourly_pop8", hourly_pop[8]);
            returnPage.addObject("hourly_pop9", hourly_pop[9]);





            // *********************************************************************************************************
            // *********************************************************************************************************
            // *********************************************************************************************************
            //                                                                                                      ****
            //                                              D A I L Y                                               ****
            //                                                                                                      ****
            // *********************************************************************************************************
            // *********************************************************************************************************
            // *********************************************************************************************************
            // Daily
            // daily_dt
            //returnPage.addObject("daily_dt", daily_dt);
            returnPage.addObject("daily_dt0", daily_dt[0]);
            returnPage.addObject("daily_dt1", daily_dt[1]);
            returnPage.addObject("daily_dt2", daily_dt[2]);
            returnPage.addObject("daily_dt3", daily_dt[3]);
            returnPage.addObject("daily_dt4", daily_dt[4]);
            returnPage.addObject("daily_dt5", daily_dt[5]);
            returnPage.addObject("daily_dt6", daily_dt[6]);

            // daily_dt_adjusted
            //returnPage.addObject("daily_dt_adjusted", daily_dt_adjusted);
            returnPage.addObject("daily_dt_adjusted0", daily_dt_adjusted[0]);
            returnPage.addObject("daily_dt_adjusted1", daily_dt_adjusted[1]);
            returnPage.addObject("daily_dt_adjusted2", daily_dt_adjusted[2]);
            returnPage.addObject("daily_dt_adjusted3", daily_dt_adjusted[3]);
            returnPage.addObject("daily_dt_adjusted4", daily_dt_adjusted[4]);
            returnPage.addObject("daily_dt_adjusted5", daily_dt_adjusted[5]);
            returnPage.addObject("daily_dt_adjusted6", daily_dt_adjusted[6]);

            // daily_sunrise
            //returnPage.addObject("daily_sunrise", daily_sunrise);
            returnPage.addObject("daily_sunrise0", daily_sunrise[0]);
            returnPage.addObject("daily_sunrise1", daily_sunrise[1]);
            returnPage.addObject("daily_sunrise2", daily_sunrise[2]);
            returnPage.addObject("daily_sunrise3", daily_sunrise[3]);
            returnPage.addObject("daily_sunrise4", daily_sunrise[4]);
            returnPage.addObject("daily_sunrise5", daily_sunrise[5]);
            returnPage.addObject("daily_sunrise6", daily_sunrise[6]);

            // daily_sunrise_adjusted
            //returnPage.addObject("daily_sunrise_adjusted", daily_sunrise_adjusted);
            returnPage.addObject("daily_sunrise_adjusted0", daily_sunrise_adjusted[0]);
            returnPage.addObject("daily_sunrise_adjusted1", daily_sunrise_adjusted[1]);
            returnPage.addObject("daily_sunrise_adjusted2", daily_sunrise_adjusted[2]);
            returnPage.addObject("daily_sunrise_adjusted3", daily_sunrise_adjusted[3]);
            returnPage.addObject("daily_sunrise_adjusted4", daily_sunrise_adjusted[4]);
            returnPage.addObject("daily_sunrise_adjusted5", daily_sunrise_adjusted[5]);
            returnPage.addObject("daily_sunrise_adjusted6", daily_sunrise_adjusted[6]);

            // daily_sunset
            //returnPage.addObject("daily_sunset", daily_sunset);
            returnPage.addObject("daily_sunset0", daily_sunset[0]);
            returnPage.addObject("daily_sunset1", daily_sunset[1]);
            returnPage.addObject("daily_sunset2", daily_sunset[2]);
            returnPage.addObject("daily_sunset3", daily_sunset[3]);
            returnPage.addObject("daily_sunset4", daily_sunset[4]);
            returnPage.addObject("daily_sunset5", daily_sunset[5]);
            returnPage.addObject("daily_sunset6", daily_sunset[6]);

            // daily_sunset_adjusted
            //returnPage.addObject("daily_sunset_adjusted", daily_sunset_adjusted);
            returnPage.addObject("daily_sunset_adjusted0", daily_sunset_adjusted[0]);
            returnPage.addObject("daily_sunset_adjusted1", daily_sunset_adjusted[1]);
            returnPage.addObject("daily_sunset_adjusted2", daily_sunset_adjusted[2]);
            returnPage.addObject("daily_sunset_adjusted3", daily_sunset_adjusted[3]);
            returnPage.addObject("daily_sunset_adjusted4", daily_sunset_adjusted[4]);
            returnPage.addObject("daily_sunset_adjusted5", daily_sunset_adjusted[5]);
            returnPage.addObject("daily_sunset_adjusted6", daily_sunset_adjusted[6]);






            // daily_temp_day
            //returnPage.addObject("daily_temp_day", daily_temp_day);
            returnPage.addObject("daily_temp_day0", daily_temp_day[0]);
            returnPage.addObject("daily_temp_day1", daily_temp_day[1]);
            returnPage.addObject("daily_temp_day2", daily_temp_day[2]);
            returnPage.addObject("daily_temp_day3", daily_temp_day[3]);
            returnPage.addObject("daily_temp_day4", daily_temp_day[4]);
            returnPage.addObject("daily_temp_day5", daily_temp_day[5]);
            returnPage.addObject("daily_temp_day6", daily_temp_day[6]);

            // daily_temp_min
            //returnPage.addObject("daily_temp_min", daily_temp_min);
            returnPage.addObject("daily_temp_min0", daily_temp_min[0]);
            returnPage.addObject("daily_temp_min1", daily_temp_min[1]);
            returnPage.addObject("daily_temp_min2", daily_temp_min[2]);
            returnPage.addObject("daily_temp_min3", daily_temp_min[3]);
            returnPage.addObject("daily_temp_min4", daily_temp_min[4]);
            returnPage.addObject("daily_temp_min5", daily_temp_min[5]);
            returnPage.addObject("daily_temp_min6", daily_temp_min[6]);

            // daily_temp_max
            // returnPage.addObject("daily_temp_max", daily_temp_max);
            returnPage.addObject("daily_temp_max0", daily_temp_max[0]);
            returnPage.addObject("daily_temp_max1", daily_temp_max[1]);
            returnPage.addObject("daily_temp_max2", daily_temp_max[2]);
            returnPage.addObject("daily_temp_max3", daily_temp_max[3]);
            returnPage.addObject("daily_temp_max4", daily_temp_max[4]);
            returnPage.addObject("daily_temp_max5", daily_temp_max[5]);
            returnPage.addObject("daily_temp_max6", daily_temp_max[6]);

            // daily_temp_night
            // returnPage.addObject("daily_temp_night", daily_temp_night);
            returnPage.addObject("daily_temp_night0", daily_temp_night[0]);
            returnPage.addObject("daily_temp_night1", daily_temp_night[1]);
            returnPage.addObject("daily_temp_night2", daily_temp_night[2]);
            returnPage.addObject("daily_temp_night3", daily_temp_night[3]);
            returnPage.addObject("daily_temp_night4", daily_temp_night[4]);
            returnPage.addObject("daily_temp_night5", daily_temp_night[5]);
            returnPage.addObject("daily_temp_night6", daily_temp_night[6]);

            // daily_temp_eve
            // returnPage.addObject("daily_temp_eve", daily_temp_eve);
            returnPage.addObject("daily_temp_eve0", daily_temp_eve[0]);
            returnPage.addObject("daily_temp_eve1", daily_temp_eve[1]);
            returnPage.addObject("daily_temp_eve2", daily_temp_eve[2]);
            returnPage.addObject("daily_temp_eve3", daily_temp_eve[3]);
            returnPage.addObject("daily_temp_eve4", daily_temp_eve[4]);
            returnPage.addObject("daily_temp_eve5", daily_temp_eve[5]);
            returnPage.addObject("daily_temp_eve6", daily_temp_eve[6]);

            // daily_temp_morn
            // returnPage.addObject("daily_temp_morn", daily_temp_morn);
            returnPage.addObject("daily_temp_morn0", daily_temp_morn[0]);
            returnPage.addObject("daily_temp_morn1", daily_temp_morn[1]);
            returnPage.addObject("daily_temp_morn2", daily_temp_morn[2]);
            returnPage.addObject("daily_temp_morn3", daily_temp_morn[3]);
            returnPage.addObject("daily_temp_morn4", daily_temp_morn[4]);
            returnPage.addObject("daily_temp_morn5", daily_temp_morn[5]);
            returnPage.addObject("daily_temp_morn6", daily_temp_morn[6]);




            // daily_feels_like_day
            // returnPage.addObject("daily_feels_like_day", daily_feels_like_day);
            returnPage.addObject("daily_feels_like_day0", daily_feels_like_day[0]);
            returnPage.addObject("daily_feels_like_day1", daily_feels_like_day[1]);
            returnPage.addObject("daily_feels_like_day2", daily_feels_like_day[2]);
            returnPage.addObject("daily_feels_like_day3", daily_feels_like_day[3]);
            returnPage.addObject("daily_feels_like_day4", daily_feels_like_day[4]);
            returnPage.addObject("daily_feels_like_day5", daily_feels_like_day[5]);
            returnPage.addObject("daily_feels_like_day6", daily_feels_like_day[6]);

            // daily_feels_like_night
            // returnPage.addObject("daily_feels_like_night", daily_feels_like_night);
            returnPage.addObject("daily_feels_like_night0", daily_feels_like_night[0]);
            returnPage.addObject("daily_feels_like_night1", daily_feels_like_night[1]);
            returnPage.addObject("daily_feels_like_night2", daily_feels_like_night[2]);
            returnPage.addObject("daily_feels_like_night3", daily_feels_like_night[3]);
            returnPage.addObject("daily_feels_like_night4", daily_feels_like_night[4]);
            returnPage.addObject("daily_feels_like_night5", daily_feels_like_night[5]);
            returnPage.addObject("daily_feels_like_night6", daily_feels_like_night[6]);

            // daily_feels_like_eve
            // returnPage.addObject("daily_feels_like_eve", daily_feels_like_eve);
            returnPage.addObject("daily_feels_like_eve0", daily_feels_like_eve[0]);
            returnPage.addObject("daily_feels_like_eve1", daily_feels_like_eve[1]);
            returnPage.addObject("daily_feels_like_eve2", daily_feels_like_eve[2]);
            returnPage.addObject("daily_feels_like_eve3", daily_feels_like_eve[3]);
            returnPage.addObject("daily_feels_like_eve4", daily_feels_like_eve[4]);
            returnPage.addObject("daily_feels_like_eve5", daily_feels_like_eve[5]);
            returnPage.addObject("daily_feels_like_eve6", daily_feels_like_eve[6]);

            // daily_feels_like_morn
            // returnPage.addObject("daily_feels_like_morn", daily_feels_like_morn);
            returnPage.addObject("daily_feels_like_morn0", daily_feels_like_morn[0]);
            returnPage.addObject("daily_feels_like_morn1", daily_feels_like_morn[1]);
            returnPage.addObject("daily_feels_like_morn2", daily_feels_like_morn[2]);
            returnPage.addObject("daily_feels_like_morn3", daily_feels_like_morn[3]);
            returnPage.addObject("daily_feels_like_morn4", daily_feels_like_morn[4]);
            returnPage.addObject("daily_feels_like_morn5", daily_feels_like_morn[5]);
            returnPage.addObject("daily_feels_like_morn6", daily_feels_like_morn[6]);




            // daily_pressure
            // returnPage.addObject("daily_pressure", daily_pressure);
            returnPage.addObject("daily_pressure0", daily_pressure[0]);
            returnPage.addObject("daily_pressure1", daily_pressure[1]);
            returnPage.addObject("daily_pressure2", daily_pressure[2]);
            returnPage.addObject("daily_pressure3", daily_pressure[3]);
            returnPage.addObject("daily_pressure4", daily_pressure[4]);
            returnPage.addObject("daily_pressure5", daily_pressure[5]);
            returnPage.addObject("daily_pressure6", daily_pressure[6]);

            // daily_humidity
            // returnPage.addObject("daily_humidity", daily_humidity);
            returnPage.addObject("daily_humidity0", daily_humidity[0]);
            returnPage.addObject("daily_humidity1", daily_humidity[1]);
            returnPage.addObject("daily_humidity2", daily_humidity[2]);
            returnPage.addObject("daily_humidity3", daily_humidity[3]);
            returnPage.addObject("daily_humidity4", daily_humidity[4]);
            returnPage.addObject("daily_humidity5", daily_humidity[5]);
            returnPage.addObject("daily_humidity6", daily_humidity[6]);

            // daily_dew_point
            // returnPage.addObject("daily_dew_point", daily_dew_point);
            returnPage.addObject("daily_dew_point0", daily_dew_point[0]);
            returnPage.addObject("daily_dew_point1", daily_dew_point[1]);
            returnPage.addObject("daily_dew_point2", daily_dew_point[2]);
            returnPage.addObject("daily_dew_point3", daily_dew_point[3]);
            returnPage.addObject("daily_dew_point4", daily_dew_point[4]);
            returnPage.addObject("daily_dew_point5", daily_dew_point[5]);
            returnPage.addObject("daily_dew_point6", daily_dew_point[6]);

            // daily_wind_speed
            // returnPage.addObject("daily_wind_speed", daily_wind_speed);
            returnPage.addObject("daily_wind_speed0", daily_wind_speed[0]);
            returnPage.addObject("daily_wind_speed1", daily_wind_speed[1]);
            returnPage.addObject("daily_wind_speed2", daily_wind_speed[2]);
            returnPage.addObject("daily_wind_speed3", daily_wind_speed[3]);
            returnPage.addObject("daily_wind_speed4", daily_wind_speed[4]);
            returnPage.addObject("daily_wind_speed5", daily_wind_speed[5]);
            returnPage.addObject("daily_wind_speed6", daily_wind_speed[6]);

            // daily_wind_deg
            // returnPage.addObject("daily_wind_deg", daily_wind_deg);
            returnPage.addObject("daily_wind_deg0", daily_wind_deg[0]);
            returnPage.addObject("daily_wind_deg1", daily_wind_deg[1]);
            returnPage.addObject("daily_wind_deg2", daily_wind_deg[2]);
            returnPage.addObject("daily_wind_deg3", daily_wind_deg[3]);
            returnPage.addObject("daily_wind_deg4", daily_wind_deg[4]);
            returnPage.addObject("daily_wind_deg5", daily_wind_deg[5]);
            returnPage.addObject("daily_wind_deg6", daily_wind_deg[6]);






            // daily_weather_id
            // returnPage.addObject("daily_weather_id", daily_weather_id);
            returnPage.addObject("daily_weather_id0", daily_weather_id[0]);
            returnPage.addObject("daily_weather_id1", daily_weather_id[1]);
            returnPage.addObject("daily_weather_id2", daily_weather_id[2]);
            returnPage.addObject("daily_weather_id3", daily_weather_id[3]);
            returnPage.addObject("daily_weather_id4", daily_weather_id[4]);
            returnPage.addObject("daily_weather_id5", daily_weather_id[5]);
            returnPage.addObject("daily_weather_id6", daily_weather_id[6]);

            // daily_weather_main
            // returnPage.addObject("daily_weather_main", daily_weather_main);
            returnPage.addObject("daily_weather_main0", daily_weather_main[0]);
            returnPage.addObject("daily_weather_main1", daily_weather_main[1]);
            returnPage.addObject("daily_weather_main2", daily_weather_main[2]);
            returnPage.addObject("daily_weather_main3", daily_weather_main[3]);
            returnPage.addObject("daily_weather_main4", daily_weather_main[4]);
            returnPage.addObject("daily_weather_main5", daily_weather_main[5]);
            returnPage.addObject("daily_weather_main6", daily_weather_main[6]);

            // daily_weather_description
            // returnPage.addObject("daily_weather_description", daily_weather_description);
            returnPage.addObject("daily_weather_description0", daily_weather_description[0]);
            returnPage.addObject("daily_weather_description1", daily_weather_description[1]);
            returnPage.addObject("daily_weather_description2", daily_weather_description[2]);
            returnPage.addObject("daily_weather_description3", daily_weather_description[3]);
            returnPage.addObject("daily_weather_description4", daily_weather_description[4]);
            returnPage.addObject("daily_weather_description5", daily_weather_description[5]);
            returnPage.addObject("daily_weather_description6", daily_weather_description[6]);

            // daily_weather_icon
            // returnPage.addObject("daily_weather_icon", daily_weather_icon);
            returnPage.addObject("daily_weather_icon0", daily_weather_icon[0]);
            returnPage.addObject("daily_weather_icon1", daily_weather_icon[1]);
            returnPage.addObject("daily_weather_icon2", daily_weather_icon[2]);
            returnPage.addObject("daily_weather_icon3", daily_weather_icon[3]);
            returnPage.addObject("daily_weather_icon4", daily_weather_icon[4]);
            returnPage.addObject("daily_weather_icon5", daily_weather_icon[5]);
            returnPage.addObject("daily_weather_icon6", daily_weather_icon[6]);





            // daily_clouds
            // returnPage.addObject("daily_clouds", daily_clouds);
            returnPage.addObject("daily_clouds0", daily_clouds[0]);
            returnPage.addObject("daily_clouds1", daily_clouds[1]);
            returnPage.addObject("daily_clouds2", daily_clouds[2]);
            returnPage.addObject("daily_clouds3", daily_clouds[3]);
            returnPage.addObject("daily_clouds4", daily_clouds[4]);
            returnPage.addObject("daily_clouds5", daily_clouds[5]);
            returnPage.addObject("daily_clouds6", daily_clouds[6]);


            // daily_pop
            // returnPage.addObject("daily_pop", daily_pop);
            returnPage.addObject("daily_pop0", daily_pop[0]);
            returnPage.addObject("daily_pop1", daily_pop[1]);
            returnPage.addObject("daily_pop2", daily_pop[2]);
            returnPage.addObject("daily_pop3", daily_pop[3]);
            returnPage.addObject("daily_pop4", daily_pop[4]);
            returnPage.addObject("daily_pop5", daily_pop[5]);
            returnPage.addObject("daily_pop6", daily_pop[6]);


            //returnPage.addObject("daily_rain", daily_rain);

            // daily_uvi
            // returnPage.addObject("daily_uvi", daily_uvi);
            returnPage.addObject("daily_uvi0", daily_uvi[0]);
            returnPage.addObject("daily_uvi1", daily_uvi[1]);
            returnPage.addObject("daily_uvi2", daily_uvi[2]);
            returnPage.addObject("daily_uvi3", daily_uvi[3]);
            returnPage.addObject("daily_uvi4", daily_uvi[4]);
            returnPage.addObject("daily_uvi5", daily_uvi[5]);
            returnPage.addObject("daily_uvi6", daily_uvi[6]);



            returnPage.setViewName("mainpage");

        } catch (

        IOException e) {
            System.out.println(e.getMessage());
        }

        return returnPage;
    }

}