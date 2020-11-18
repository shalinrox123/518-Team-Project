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
            int current_sunrise = JSONObject_current.getInt("sunrise");
            int current_sunset = JSONObject_current.getInt("sunset");
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
            //                                            H O U R S                                                 ****
            //                                                                                                      ****
            // *********************************************************************************************************
            // *********************************************************************************************************
            // *********************************************************************************************************

             final int numOfHours = 10;

            JSONArray JSONArray_hourly = JSONObject_openWeatherAPIResponse.getJSONArray("hourly");
            // The whole hourly array is passed to the JSONArray, JSONArray_hourly, in the above line.

            int[] hourly_dt = new int[numOfHours];
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
            int[] daily_sunrise = new int[numOfDays];
            int[] daily_sunset = new int[numOfDays];

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
            int[] daily_pop = new int[numOfDays];
            double[] daily_rain = new double[numOfDays];
            double[] daily_uvi = new double[numOfDays];



            // seven days
            for (int i = 0; i < numOfDays; i++)
            {
                daily_dt[i] = JSONArray_daily.getJSONObject(i).getInt("dt");
                daily_sunrise[i] = JSONArray_daily.getJSONObject(i).getInt("sunrise");
                daily_sunset[i] = JSONArray_daily.getJSONObject(i).getInt("sunset");

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
                daily_pop[i] = JSONArray_daily.getJSONObject(i).getInt("pop");
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

            returnPage.addObject("lat", lat);
            returnPage.addObject("lon", lon);
            returnPage.addObject("timezone", timezone);
            returnPage.addObject("timezone_offset", timezone_offset);

            // Current
            returnPage.addObject("current_dt", current_dt);
            returnPage.addObject("current_sunrise", current_sunrise);
            returnPage.addObject("current_sunset", current_sunset);
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

            // Hourly
            returnPage.addObject("hourly_dt", hourly_dt);
            returnPage.addObject("hourly_temp", hourly_temp);
            returnPage.addObject("hourly_feels_like", hourly_feels_like);
            returnPage.addObject("hourly_pressure", hourly_pressure);
            returnPage.addObject("hourly_humidity", hourly_humidity);
            returnPage.addObject("hourly_dew_point", hourly_dew_point);
            returnPage.addObject("hourly_clouds", hourly_clouds);
            returnPage.addObject("hourly_visibility", hourly_visibility);
            returnPage.addObject("hourly_wind_speed", hourly_wind_speed);
            returnPage.addObject("hourly_wind_deg", hourly_wind_deg);

            returnPage.addObject("hourly_weather_id", hourly_weather_id);
            returnPage.addObject("hourly_weather_main", hourly_weather_main);
            returnPage.addObject("hourly_weather_description", hourly_weather_description);
            returnPage.addObject("hourly_weather_icon", hourly_weather_icon);

            returnPage.addObject("hourly_pop", hourly_pop);


            // Daily
            returnPage.addObject("daily_dt", daily_dt);
            returnPage.addObject("daily_sunrise", daily_sunrise);
            returnPage.addObject("daily_sunset", daily_sunset);

            returnPage.addObject("daily_temp_day", daily_temp_day);
            returnPage.addObject("daily_temp_min", daily_temp_min);
            returnPage.addObject("daily_temp_max", daily_temp_max);
            returnPage.addObject("daily_temp_night", daily_temp_night);
            returnPage.addObject("daily_temp_eve", daily_temp_eve);
            returnPage.addObject("daily_temp_morn", daily_temp_morn);

            returnPage.addObject("daily_feels_like_day", daily_feels_like_day);
            returnPage.addObject("daily_feels_like_night", daily_feels_like_night);
            returnPage.addObject("daily_feels_like_eve", daily_feels_like_eve);
            returnPage.addObject("daily_feels_like_morn", daily_feels_like_morn);

            returnPage.addObject("daily_pressure", daily_pressure);
            returnPage.addObject("daily_humidity", daily_humidity);
            returnPage.addObject("daily_dew_point", daily_dew_point);
            returnPage.addObject("daily_wind_speed", daily_wind_speed);
            returnPage.addObject("daily_wind_deg", daily_wind_deg);

            returnPage.addObject("daily_weather_id", daily_weather_id);
            returnPage.addObject("daily_weather_main", daily_weather_main);
            returnPage.addObject("daily_weather_description", daily_weather_description);
            returnPage.addObject("daily_weather_icon", daily_weather_icon);

            returnPage.addObject("daily_clouds", daily_clouds);
            returnPage.addObject("daily_pop", daily_pop);
            //returnPage.addObject("daily_rain", daily_rain);
            returnPage.addObject("daily_uvi", daily_uvi);


            returnPage.setViewName("mainpage");

        } catch (

        IOException e) {
            System.out.println(e.getMessage());
        }

        return returnPage;
    }

}
