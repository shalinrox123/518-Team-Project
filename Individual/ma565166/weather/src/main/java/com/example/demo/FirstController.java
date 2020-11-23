package com.example.demo;

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

@RestController

public class FirstController {

    @RequestMapping("/")

    public String hello() {
        return "Hello Software Engineering";
    }

    @RequestMapping("/temp/{LOCATION}")
    public String Weather_temp(@PathVariable String LOCATION) throws ParseException {
        String API_KEY = "ce490961c6mshc593f5f415ee002p1dc26bjsn1776dba774f9";
        API_KEY = "ce490961c6mshc593f5f415ee002p1dc26bjsn1776dba774f9";
        API_KEY = "f3f74c7f7d8f187ffe331859469e462c";
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

            JSONObject JSONObject_main = jsonObject.getJSONObject("main");
            Double result_temp = JSONObject_main.getDouble("temp");

            temp = Double.toString(result_temp);

            rd.close();

        } catch (IOException e) {
            System.out.println(e.getMessage());
        }

        return temp;
    }

    @RequestMapping("/wind/{LOCATION}")
    public String wind_speed(@PathVariable String LOCATION) throws ParseException {
        String API_KEY = "ce490961c6mshc593f5f415ee002p1dc26bjsn1776dba774f9";
        API_KEY = "ce490961c6mshc593f5f415ee002p1dc26bjsn1776dba774f9";
        API_KEY = "f3f74c7f7d8f187ffe331859469e462c";
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

            JSONObject JSONObject_main = jsonObject.getJSONObject("wind");
            Double result_temp = JSONObject_main.getDouble("speed");

            temp = Double.toString(result_temp);

            rd.close();

        } catch (IOException e) {
            System.out.println(e.getMessage());
        }

        return temp;
    }

    @RequestMapping("/city/{LOCATION}")
    public String get_weather_city_name(@PathVariable String LOCATION) throws ParseException {
        String API_KEY = "ce490961c6mshc593f5f415ee002p1dc26bjsn1776dba774f9";
        API_KEY = "ce490961c6mshc593f5f415ee002p1dc26bjsn1776dba774f9";
        API_KEY = "f3f74c7f7d8f187ffe331859469e462c";
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

    @RequestMapping("/location/{LATLON}")
    public String get_weather_coordination(@PathVariable String LATLON) throws ParseException {
        String API_KEY = "ce490961c6mshc593f5f415ee002p1dc26bjsn1776dba774f9";
        API_KEY = "ce490961c6mshc593f5f415ee002p1dc26bjsn1776dba774f9";
        API_KEY = "f3f74c7f7d8f187ffe331859469e462c";
        System.out.println("Latitude and Longtitude: " + LATLON);
        
        // String unit = "imperial";
        String UNIT = "metric";

        String LAT = LATLON.split(",")[0].trim();
        String LON = LATLON.split(",")[1].trim();

        String urlString = "http://api.openweathermap.org/data/2.5/weather?lat=" + LAT + "&lon=" + LON + "&appid=" + API_KEY + "&units=" + UNIT;

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

}