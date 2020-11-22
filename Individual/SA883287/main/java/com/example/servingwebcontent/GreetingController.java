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


    public String getLatlonForCityName(String cityName)
    {
        String latlon = "";

        String API_KEY = "4fa8fc5c7d7981b995adddbf79e5b964";

        // Call Current Weather Data, NOT One Call API
        // One Call API does not accept a city as a parameter, but Current Weather Data does
        // Current weather returns lat and lon coordinates that we can use as arguments to the One Call API.
        String urlString = "http://api.openweathermap.org/data/2.5/weather?q=" + cityName + "&appid=" + API_KEY + "&units=imperial";

        StringBuilder result = new StringBuilder();
        String temp = "";

        try{

            URL url = new URL(urlString);
            URLConnection conn = url.openConnection();
            BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));

            String json = "";
            String line = "";
            while ((line = rd.readLine()) != null) {
                result.append(line);
                json += line;
            }

            JSONObject firstjsonObject = new JSONObject(json);

            // "coord"
            JSONObject firstJSONObject_coord = firstjsonObject.getJSONObject("coord");
            double longitude = firstJSONObject_coord.getDouble("lon");        // ************** important
            double latitude = firstJSONObject_coord.getDouble("lat");        // ************** we'll need these

            latlon = latitude + "," + longitude;
            System.out.println("Inside of getLatlonForCityName");
            System.out.println("latlon: " + latlon);


        } catch (IOException e) {

            System.out.println(e.getMessage());
        }



        return latlon;
    }




    @GetMapping("/greeting")
    public String greeting(@RequestParam(name="name", required=false, defaultValue="World") String name, Model model) {
        model.addAttribute("name", name);
        return "greeting";
    }

    @GetMapping("/index_1")
    private String gre99(){
        System.out.println("IN INDEX_1");
        return "index_1";
    }

    @GetMapping("/index")
    private String gre_12(){
        System.out.println("IN INDEX_1");
        return "index";
    }

    @GetMapping("/location/dropdown")
    private String dropdownFunction(){

        System.out.println("Inside of dropdown function");
        return "dropdown";
    }

    @GetMapping("/location/index2")
    private String gre2(){

        System.out.println("Inside of gre2");

        return "index2";
    }

    @GetMapping("/location1/index2_1")
    private String gre2_1(){

        System.out.println("Inside of gre2_1");

        return "index2_1";
    }


    //@ResponseBody
    @GetMapping("/location3/cityName/{CITYNAME}")
    public String get_cityLocation(@PathVariable String CITYNAME) throws ParseException {

        //https://api.opencagedata.com/geocode/v1/json?q=Albany&key=17f20d72d5f6410b8f89d59f0afbb590&pretty=1
        String API_KEY = "17f20d72d5f6410b8f89d59f0afbb590";

        System.out.println(CITYNAME);

        String URL = "https://api.opencagedata.com/geocode/v1/json?q=" + CITYNAME + "&key=" + API_KEY+ "&pretty=1";

        System.out.println(URL);

        StringBuilder result = new StringBuilder();
        String temp="";

        try {

            URL url = new URL(URL);
            URLConnection conn = url.openConnection();
            BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));

            String json = "";
            String line = "";
            while ((line = rd.readLine()) != null) {
                result.append(line);
                json += line;
            }

            JSONObject jsonObject = new JSONObject(json);

            JSONArray jsonArrayResults = jsonObject.getJSONArray("results");

            System.out.println(jsonArrayResults);

            JSONObject tempObj = jsonArrayResults.getJSONObject(0);

            System.out.println(tempObj);

            JSONObject geometryObj = tempObj.getJSONObject("geometry");

            System.out.println(geometryObj);

            // "lat long"
            String result_latlong;
            //JSONObject JSONObject_geometry = jsonArrayResults.getJSONObject("geometry");
            if (true) {
                //JSONObject JSONObject_weather = tempObj.getJSONObject();
                double lat = geometryObj.getDouble("lat");
                double lng = geometryObj.getDouble("lng");

                result_latlong = "/location/" + lat + "," + lng;
                temp = result_latlong;
            } else {
                result_latlong = "City not found";
            }



        } catch (

                IOException e) {
            System.out.println(e.getMessage());
        }

        return temp;
        //results.geometry

    }


    @RequestMapping("/city/{LOCATION}")
    public ModelAndView get_weather_city_name(@PathVariable String LOCATION) throws ParseException {

        String latlon = getLatlonForCityName(LOCATION);

        System.out.println("Location: " + LOCATION);
        System.out.println("Latlon recieved for this location: " + latlon);

        // Now call One Call API
        ModelAndView page = getPageAndAddElementsLatlon(latlon);

        page.setViewName("frontpage");

        return page;
    }


    @RequestMapping("/futurecast/city/{LOCATION}")
    public ModelAndView get_weather_city_futurecast(@PathVariable String LOCATION) throws ParseException {

        String latlon = getLatlonForCityName(LOCATION);

        System.out.println("Location: " + LOCATION);
        System.out.println("Latlon recieved for this location: " + latlon);

        // Now call One Call API
        ModelAndView page = getPageAndAddElementsLatlon(latlon);

        page.setViewName("futurecast");

        return page;
    }

    @ResponseBody
    @RequestMapping("/location/{LATLON}")
    public ModelAndView get_weather_coordination(@PathVariable String LATLON) throws ParseException {


        ModelAndView page = getPageAndAddElementsLatlon(LATLON);

        page.setViewName("frontpage");

        return page;
    }


    @ResponseBody
    @RequestMapping("/location/futurecast/{LATLON}")
    public ModelAndView get_futurecast(@PathVariable String LATLON) throws ParseException {

        ModelAndView page = getPageAndAddElementsLatlon(LATLON);

        page.setViewName("futurecast");

        return page;
    }


    @ResponseBody
    @RequestMapping("/location1/{LATLON}")
    public ModelAndView get_weather_coordination_1(@PathVariable String LATLON) throws ParseException {


        ModelAndView page = getPageAndAddElementsLatlon(LATLON);

        page.setViewName("frontpage");

        return page;
    }


    @ResponseBody
    @RequestMapping("/location1/futurecast1/{LATLON}")
    public ModelAndView get_futurecast_1(@PathVariable String LATLON) throws ParseException {

        ModelAndView page = getPageAndAddElementsLatlon(LATLON);

        page.setViewName("futurecast-logged-in");

        return page;
    }

}
