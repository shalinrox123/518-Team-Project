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

    /*
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
     */

    // Time formatting functions

    public static String getDayDate(int unixSeconds, String tz){

        Date date = new java.util.Date(unixSeconds*1000L);
        SimpleDateFormat sdf = new java.text.SimpleDateFormat("MMM-dd EEEE");
        sdf.setTimeZone(java.util.TimeZone.getTimeZone(tz));
        String newFormat = sdf.format(date);
        return newFormat;
    }

    public static String getHour(int unixSeconds, String tz){

        Date date = new java.util.Date(unixSeconds*1000L);
        SimpleDateFormat sdf = new java.text.SimpleDateFormat("hh:mm aa");
        sdf.setTimeZone(java.util.TimeZone.getTimeZone(tz));
        String newFormat = sdf.format(date);
        return newFormat;
    }

    public static String getSundialHour(int unixSeconds, String tz){

        Date date = new java.util.Date(unixSeconds*1000L);
        SimpleDateFormat sdf = new java.text.SimpleDateFormat("HH");
        sdf.setTimeZone(java.util.TimeZone.getTimeZone(tz));
        String newFormat = sdf.format(date);
        return newFormat;
    }


    public ModelAndView getPageAndAddElementsLatlon(String latlon)
    {
        final int numOfDays = 7;
        final int numOfHours = 10;

        ModelAndView returnPage = new ModelAndView();

        String API_KEY = "f3f74c7f7d8f187ffe331859469e462c";
        System.out.println("Latitude and Longtitude: " + latlon);

        String unit = "imperial";
        String UNIT = "metric";

        String LAT = latlon.split(",")[0].trim();
        String LON = latlon.split(",")[1].trim();

        //String urlString = "http://api.openweathermap.org/data/2.5/weather?lat=" + LAT + "&lon=" + LON + "&appid=" + API_KEY + "&units=" + UNIT;
        String oneCallUrlString = "http://api.openweathermap.org/data/2.5/onecall?lat=" + LAT + "&lon=" + LON + "&appid=" + API_KEY + "&units=" + unit;
        String weatherDataUrlString = "http://api.openweathermap.org/data/2.5/weather?lat=" + LAT + "&lon=" + LON + "&appid=" +  API_KEY + "&units=" + unit;



        StringBuilder oneCallStringBuilder = new StringBuilder();
        StringBuilder weatherDataStringBuilder = new StringBuilder();

        String tempString = "";

        try {
            URL url = new URL(oneCallUrlString);
            URLConnection oneCallConn = url.openConnection();
            BufferedReader oneCallRead = new BufferedReader(new InputStreamReader(oneCallConn.getInputStream()));

            String oneCallJson = "";
            String oneCallLine = "";

            while ((oneCallLine = oneCallRead.readLine()) != null) {
                oneCallStringBuilder.append(oneCallLine);
                oneCallJson += oneCallLine;
            }
            // ************** //
            JSONObject JSONObject_oneCallAPIResponse = new JSONObject(oneCallJson);

            double lon = JSONObject_oneCallAPIResponse.getDouble("lon");
            double lat = JSONObject_oneCallAPIResponse.getDouble("lat");
            String timezone = JSONObject_oneCallAPIResponse.getString("timezone");
            System.out.println("timezone: " + timezone);
            int timezone_offset = JSONObject_oneCallAPIResponse.getInt("timezone_offset");
            System.out.println("timezone_offset: " + timezone_offset);





            // We will also make a call to the Current Weather Data API with our lat and lon so that we know the city name
            URL url2 = new URL(weatherDataUrlString);
            URLConnection weatherDataConn = url2.openConnection();
            BufferedReader weatherDataRead = new BufferedReader(new InputStreamReader(weatherDataConn.getInputStream()));

            String weatherDataJson = "";
            String weatherDataLine = "";

            while ((weatherDataLine = weatherDataRead.readLine()) != null) {
                weatherDataStringBuilder.append(weatherDataLine);
                weatherDataJson += weatherDataLine;
            }

            JSONObject JSONObject_weatherDataAPIResponse = new JSONObject(weatherDataJson);

            String cityname_weatherDataAPI = JSONObject_weatherDataAPIResponse.getString("name");

            System.out.println("cityname value returned from Weather Data API: " + cityname_weatherDataAPI);


            // *********************************************************************************************************
            // *********************************************************************************************************
            //                                                                                                      ****
            //                                        C U R R E N T                                                 ****
            //                                                                                                      ****
            // *********************************************************************************************************
            // *********************************************************************************************************

            JSONObject JSONObject_current = JSONObject_oneCallAPIResponse.getJSONObject("current");


            int current_dt = JSONObject_current.getInt("dt");
            System.out.println("current_dt: " + current_dt);
            int current_dt_adjusted = current_dt + timezone_offset;
            System.out.println("current_dt_adjusted: " + current_dt_adjusted);

            String current_dt_adjusted_daydate = getDayDate(current_dt, timezone);
            String current_dt_adjusted_hour = getHour(current_dt, timezone);
            System.out.println("current_dt_adjusted_hour: " + current_dt_adjusted_hour);

            int current_sunrise = JSONObject_current.getInt("sunrise");
            int current_sunrise_adjusted = current_sunrise + timezone_offset;
            String current_sunrise_adjusted_daydate = getDayDate(current_sunrise, timezone);
            String current_sunrise_adjusted_hour = getHour(current_sunrise, timezone);
            String current_sunrise_sundial = getSundialHour(current_sunrise, timezone);
            System.out.println("current_sunrise_sundial: " + current_sunrise_sundial);

            int current_sunset = JSONObject_current.getInt("sunset");
            int current_sunset_adjusted = current_sunset + timezone_offset;
            String current_sunset_adjusted_daydate = getDayDate(current_sunset, timezone);
            String current_sunset_adjusted_hour = getHour(current_sunset, timezone);
            String current_sunset_sundial = getSundialHour(current_sunset, timezone);
            System.out.println("current_sunset_sundial: " + current_sunset_sundial);

            int current_temp = (int) JSONObject_current.getDouble("temp");
            int current_feels_like = (int) JSONObject_current.getDouble("feels_like");
            int current_pressure = JSONObject_current.getInt("pressure");
            int current_humidity = JSONObject_current.getInt("humidity");
            int current_dew_point = (int) JSONObject_current.getDouble("dew_point");
            int current_uvi = (int) JSONObject_current.getDouble("uvi");
            int current_clouds = JSONObject_current.getInt("clouds");
            int current_visibility = JSONObject_current.getInt("visibility");
            int current_wind_speed = (int) JSONObject_current.getDouble("wind_speed");
            int current_wind_deg = JSONObject_current.getInt("wind_deg");


            JSONObject JSONObject_current_weather;
            int current_weather_id;
            String current_weather_main;
            String current_weather_description;
            String current_weather_icon;

            JSONArray JSONArray_current_weather = JSONObject_current.getJSONArray("weather");

            JSONObject_current_weather = JSONArray_current_weather.getJSONObject(0);
            current_weather_id = JSONObject_current_weather.getInt("id");
            current_weather_main = JSONObject_current_weather.getString("main");
            current_weather_description = JSONObject_current_weather.getString("description");
            current_weather_icon = JSONObject_current_weather.getString("icon");


            // *********************************************************************************************************
            // *********************************************************************************************************
            //                                                                                                      ****
            //                                            H O U R L Y                                               ****
            //                                                                                                      ****
            // *********************************************************************************************************
            // *********************************************************************************************************


            JSONArray JSONArray_hourly = JSONObject_oneCallAPIResponse.getJSONArray("hourly");
            // The whole hourly array is passed to the JSONArray, JSONArray_hourly, in the above line.

            int[] hourly_dt = new int[numOfHours];
            int[] hourly_dt_adjusted = new int[numOfHours];
            String[] hourly_dt_adjusted_daydate = new String[numOfHours];
            String[] hourly_dt_adjusted_hour = new String[numOfHours];
            int[] hourly_temp = new int[numOfHours];
            int[] hourly_feels_like = new int[numOfHours];
            int[] hourly_pressure = new int[numOfHours];
            int[] hourly_humidity = new int[numOfHours];
            int[] hourly_dew_point = new int[numOfHours];
            int[] hourly_clouds = new int[numOfHours];
            int[] hourly_visibility = new int[numOfHours];
            int[] hourly_wind_speed = new int[numOfHours];
            int[] hourly_wind_deg = new int[numOfHours];

            int[] hourly_weather_id = new int[numOfHours];
            String[] hourly_weather_main = new String[numOfHours];
            String[] hourly_weather_description = new String[numOfHours];
            String[] hourly_weather_icon = new String[numOfHours];

            double[] hourly_pop = new double[numOfHours];
            int[] hourly_pop_adjusted = new int[numOfHours];

            // ten hours will be enough
            for (int i = 0; i < numOfHours; i++)
            {
                hourly_dt[i] = JSONArray_hourly.getJSONObject(i).getInt("dt");
                // convert to local time
                hourly_dt_adjusted[i] = hourly_dt[i] + timezone_offset;
                hourly_dt_adjusted_daydate[i] = getDayDate(hourly_dt[i], timezone);
                hourly_dt_adjusted_hour[i] = getHour(hourly_dt[i], timezone);

                hourly_temp[i] = (int) JSONArray_hourly.getJSONObject(i).getDouble("temp");
                hourly_feels_like[i] = (int) JSONArray_hourly.getJSONObject(i).getDouble("feels_like");
                hourly_pressure[i] = JSONArray_hourly.getJSONObject(i).getInt("pressure");
                hourly_humidity[i] = JSONArray_hourly.getJSONObject(i).getInt("humidity");
                hourly_dew_point[i] = (int) JSONArray_hourly.getJSONObject(i).getDouble("dew_point");
                hourly_clouds[i] = JSONArray_hourly.getJSONObject(i).getInt("clouds");
                hourly_visibility[i] = JSONArray_hourly.getJSONObject(i).getInt("visibility");
                hourly_wind_speed[i] = (int) JSONArray_hourly.getJSONObject(i).getDouble("wind_speed");
                hourly_wind_deg[i] = JSONArray_hourly.getJSONObject(i).getInt("wind_deg");
                hourly_pop[i] = JSONArray_hourly.getJSONObject(i).getDouble("pop");
                // convert pop to percentage
                hourly_pop_adjusted[i] = (int) (hourly_pop[i] * 100);

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


            JSONArray JSONArray_daily = JSONObject_oneCallAPIResponse.getJSONArray("daily");
            // The whole daily array is passed to the JSONArray, JSONArray_hourly, in the above line.

            int[] daily_dt = new int[numOfDays];
            int[] daily_dt_adjusted = new int[numOfDays];
            String[] daily_dt_adjusted_daydate = new String[numOfDays];
            String[] daily_dt_adjusted_hour = new String[numOfDays];
            int[] daily_sunrise = new int[numOfDays];
            int[] daily_sunrise_adjusted = new int[numOfDays];
            String[] daily_sunrise_adjusted_daydate = new String[numOfDays];
            String[] daily_sunrise_adjusted_hour = new String[numOfDays];
            int[] daily_sunset = new int[numOfDays];
            int[] daily_sunset_adjusted = new int[numOfDays];
            String[] daily_sunset_adjusted_daydate = new String[numOfDays];
            String[] daily_sunset_adjusted_hour = new String[numOfDays];


            int[] daily_temp_day = new int[numOfDays];
            int[] daily_temp_min = new int[numOfDays];
            int[] daily_temp_max = new int[numOfDays];
            int[] daily_temp_night = new int[numOfDays];
            int[] daily_temp_eve = new int[numOfDays];
            int[] daily_temp_morn = new int[numOfDays];

            int[] daily_feels_like_day = new int[numOfDays];
            int[] daily_feels_like_night = new int[numOfDays];
            int[] daily_feels_like_eve = new int[numOfDays];
            int[] daily_feels_like_morn = new int[numOfDays];

            int[] daily_pressure = new int[numOfDays];
            int[] daily_humidity = new int[numOfDays];
            int[] daily_dew_point = new int[numOfDays];
            int[] daily_wind_speed = new int[numOfDays];
            int[] daily_wind_deg = new int[numOfDays];

            int[] daily_weather_id = new int[numOfDays];
            String[] daily_weather_main = new String[numOfDays];
            String[] daily_weather_description = new String[numOfDays];
            String[] daily_weather_icon = new String[numOfDays];

            int[] daily_clouds = new int[numOfDays];
            double[] daily_pop = new double[numOfDays]; // *********
            int[] daily_pop_adjusted = new int[numOfDays];
            double[] daily_rain = new double[numOfDays];
            int[] daily_uvi = new int[numOfDays];



            // seven days
            for (int i = 0; i < numOfDays; i++)
            {
                daily_dt[i] = JSONArray_daily.getJSONObject(i).getInt("dt");
                daily_dt_adjusted[i] = daily_dt[i] + timezone_offset;
                daily_dt_adjusted_hour[i] = getHour(daily_dt[i], timezone);
                daily_dt_adjusted_daydate[i] = getDayDate(daily_dt[i], timezone);

                daily_sunrise[i] = JSONArray_daily.getJSONObject(i).getInt("sunrise");
                daily_sunrise_adjusted[i] = daily_sunrise[i] + timezone_offset;
                daily_sunrise_adjusted_hour[i] = getHour(daily_sunrise[i], timezone);
                daily_sunrise_adjusted_daydate[i] = getDayDate(daily_sunrise[i], timezone);

                daily_sunset[i] = JSONArray_daily.getJSONObject(i).getInt("sunset");
                daily_sunset_adjusted[i] = daily_sunset[i] + timezone_offset;
                daily_sunset_adjusted_hour[i] = getHour(daily_sunset[i], timezone);
                daily_sunset_adjusted_daydate[i] = getDayDate(daily_sunset[i], timezone);

                daily_temp_day[i] = (int) JSONArray_daily.getJSONObject(i).getJSONObject("temp").getDouble("day");
                daily_temp_min[i] = (int) JSONArray_daily.getJSONObject(i).getJSONObject("temp").getDouble("min");
                daily_temp_max[i] = (int) JSONArray_daily.getJSONObject(i).getJSONObject("temp").getDouble("max");
                daily_temp_night[i] = (int) JSONArray_daily.getJSONObject(i).getJSONObject("temp").getDouble("night");
                daily_temp_eve[i] = (int) JSONArray_daily.getJSONObject(i).getJSONObject("temp").getDouble("eve");
                daily_temp_morn[i] = (int) JSONArray_daily.getJSONObject(i).getJSONObject("temp").getDouble("morn");


                daily_feels_like_day[i] = (int) JSONArray_daily.getJSONObject(i).getJSONObject("feels_like").getDouble("day");
                daily_feels_like_night[i] = (int) JSONArray_daily.getJSONObject(i).getJSONObject("feels_like").getDouble("night");
                daily_feels_like_eve[i] = (int) JSONArray_daily.getJSONObject(i).getJSONObject("feels_like").getDouble("eve");
                daily_feels_like_morn[i] = (int) JSONArray_daily.getJSONObject(i).getJSONObject("feels_like").getDouble("morn");


                daily_pressure[i] = JSONArray_daily.getJSONObject(i).getInt("pressure");
                daily_humidity[i] = JSONArray_daily.getJSONObject(i).getInt("humidity");
                daily_dew_point[i] = (int) JSONArray_daily.getJSONObject(i).getDouble("dew_point");
                daily_wind_speed[i] = (int) JSONArray_daily.getJSONObject(i).getDouble("wind_speed");
                daily_wind_deg[i] = JSONArray_daily.getJSONObject(i).getInt("wind_deg");


                daily_weather_id[i] = JSONArray_daily.getJSONObject(i).getJSONArray("weather").getJSONObject(0).getInt("id");
                daily_weather_main[i] = JSONArray_daily.getJSONObject(i).getJSONArray("weather").getJSONObject(0).getString("main");
                daily_weather_description[i] = JSONArray_daily.getJSONObject(i).getJSONArray("weather").getJSONObject(0).getString("description");
                daily_weather_icon[i] = JSONArray_daily.getJSONObject(i).getJSONArray("weather").getJSONObject(0).getString("icon");


                daily_clouds[i] = JSONArray_daily.getJSONObject(i).getInt("clouds");
                daily_pop[i] = JSONArray_daily.getJSONObject(i).getDouble("pop");
                // convert to percentages
                daily_pop_adjusted[i] = (int) (daily_pop[i] * 100);
                //daily_rain[i] = JSONArray_daily.getJSONObject(i).getDouble("rain");
                daily_uvi[i] = (int) JSONArray_daily.getJSONObject(i).getDouble("uvi");
            }


            System.out.println("timezone: " + timezone);
            System.out.println("timezone_offset: "+ timezone_offset);
            System.out.println("current_dt: " + current_dt);
            System.out.println("current_dt_adjusted: " + current_dt_adjusted);
            System.out.println("current_dt_adjusted_hour " + current_dt_adjusted_hour);


            // *********************************************************************************************************
            // *********************************************************************************************************
            //                                                                                                      ****
            //                                          C U R R E N T                                               ****
            //                                                                                                      ****
            // *********************************************************************************************************
            // *********************************************************************************************************


            returnPage.addObject("lat", lat);
            returnPage.addObject("lon", lon);
            returnPage.addObject("timezone", timezone);
            returnPage.addObject("timezone_offset", timezone_offset);

            // City name from Weather Data API call
            returnPage.addObject("cityname", cityname_weatherDataAPI);

            // Current
            returnPage.addObject("current_dt", current_dt);
            returnPage.addObject("current_dt_adjusted", current_dt_adjusted);
            System.out.println("current_dt_adjusted: " + current_dt_adjusted);
            returnPage.addObject("current_dt_adjusted_daydate", current_dt_adjusted_daydate);
            returnPage.addObject("current_dt_adjusted_hour", current_dt_adjusted_hour);


            returnPage.addObject("current_sunrise", current_sunrise);
            returnPage.addObject("current_sunrise_adjusted", current_sunrise_adjusted);
            System.out.println("current_sunrise_adjusted: " + current_sunrise_adjusted);
            returnPage.addObject("current_sunrise_adjusted_daydate", current_sunrise_adjusted_daydate);
            returnPage.addObject("current_sunrise_adjusted_hour", current_sunrise_adjusted_hour);
            returnPage.addObject("current_sunrise_sundial", current_sunrise_sundial);


            returnPage.addObject("current_sunset", current_sunset);
            returnPage.addObject("current_sunset_adjusted", current_sunset_adjusted);
            System.out.println("current_sunset_adjusted: " + current_sunset_adjusted);
            returnPage.addObject("current_sunset_adjusted_daydate", current_sunset_adjusted_daydate);
            returnPage.addObject("current_sunset_adjusted_hour", current_sunset_adjusted_hour);
            returnPage.addObject("current_sunset_sundial", current_sunset_sundial);



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
            //                                                                                                      ****
            //                                            H O U R L Y                                               ****
            //                                                                                                      ****
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

            // hourly_dt_adjusted_hour
            //returnPage.addObject("hourly_dt_adjusted_hour", hourly_dt_adjusted_hour);
            returnPage.addObject("hourly_dt_adjusted_hour0", hourly_dt_adjusted_hour[0]);
            returnPage.addObject("hourly_dt_adjusted_hour1", hourly_dt_adjusted_hour[1]);
            returnPage.addObject("hourly_dt_adjusted_hour2", hourly_dt_adjusted_hour[2]);
            returnPage.addObject("hourly_dt_adjusted_hour3", hourly_dt_adjusted_hour[3]);
            returnPage.addObject("hourly_dt_adjusted_hour4", hourly_dt_adjusted_hour[4]);
            returnPage.addObject("hourly_dt_adjusted_hour5", hourly_dt_adjusted_hour[5]);
            returnPage.addObject("hourly_dt_adjusted_hour6", hourly_dt_adjusted_hour[6]);
            returnPage.addObject("hourly_dt_adjusted_hour7", hourly_dt_adjusted_hour[7]);
            returnPage.addObject("hourly_dt_adjusted_hour8", hourly_dt_adjusted_hour[8]);
            returnPage.addObject("hourly_dt_adjusted_hour9", hourly_dt_adjusted_hour[9]);

            // hourly_dt_adjusted_daydate
            //returnPage.addObject("hourly_dt_adjusted_daydate", hourly_dt_adjusted_daydate);
            returnPage.addObject("hourly_dt_adjusted_daydate0", hourly_dt_adjusted_daydate[0]);
            returnPage.addObject("hourly_dt_adjusted_daydate1", hourly_dt_adjusted_daydate[1]);
            returnPage.addObject("hourly_dt_adjusted_daydate2", hourly_dt_adjusted_daydate[2]);
            returnPage.addObject("hourly_dt_adjusted_daydate3", hourly_dt_adjusted_daydate[3]);
            returnPage.addObject("hourly_dt_adjusted_daydate4", hourly_dt_adjusted_daydate[4]);
            returnPage.addObject("hourly_dt_adjusted_daydate5", hourly_dt_adjusted_daydate[5]);
            returnPage.addObject("hourly_dt_adjusted_daydate6", hourly_dt_adjusted_daydate[6]);
            returnPage.addObject("hourly_dt_adjusted_daydate7", hourly_dt_adjusted_daydate[7]);
            returnPage.addObject("hourly_dt_adjusted_daydate8", hourly_dt_adjusted_daydate[8]);
            returnPage.addObject("hourly_dt_adjusted_daydate9", hourly_dt_adjusted_daydate[9]);


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


            // hourly_pop_adjusted
            // returnPage.addObject("hourly_pop_adjusted", hourly_pop_adjusted);
            returnPage.addObject("hourly_pop_adjusted0", hourly_pop_adjusted[0]);
            returnPage.addObject("hourly_pop_adjusted1", hourly_pop_adjusted[1]);
            returnPage.addObject("hourly_pop_adjusted2", hourly_pop_adjusted[2]);
            returnPage.addObject("hourly_pop_adjusted3", hourly_pop_adjusted[3]);
            returnPage.addObject("hourly_pop_adjusted4", hourly_pop_adjusted[4]);
            returnPage.addObject("hourly_pop_adjusted5", hourly_pop_adjusted[5]);
            returnPage.addObject("hourly_pop_adjusted6", hourly_pop_adjusted[6]);
            returnPage.addObject("hourly_pop_adjusted7", hourly_pop_adjusted[7]);
            returnPage.addObject("hourly_pop_adjusted8", hourly_pop_adjusted[8]);
            returnPage.addObject("hourly_pop_adjusted9", hourly_pop_adjusted[9]);





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

            // daily_dt_adjusted_hour
            //returnPage.addObject("daily_dt_adjusted_hour", daily_dt_adjusted_hour);
            returnPage.addObject("daily_dt_adjusted_hour0", daily_dt_adjusted_hour[0]);
            returnPage.addObject("daily_dt_adjusted_hour1", daily_dt_adjusted_hour[1]);
            returnPage.addObject("daily_dt_adjusted_hour2", daily_dt_adjusted_hour[2]);
            returnPage.addObject("daily_dt_adjusted_hour3", daily_dt_adjusted_hour[3]);
            returnPage.addObject("daily_dt_adjusted_hour4", daily_dt_adjusted_hour[4]);
            returnPage.addObject("daily_dt_adjusted_hour5", daily_dt_adjusted_hour[5]);
            returnPage.addObject("daily_dt_adjusted_hour6", daily_dt_adjusted_hour[6]);

            // daily_dt_adjusted_daydate
            //returnPage.addObject("daily_dt_adjusted_daydate", daily_dt_adjusted_daydate);
            returnPage.addObject("daily_dt_adjusted_daydate0", daily_dt_adjusted_daydate[0]);
            returnPage.addObject("daily_dt_adjusted_daydate1", daily_dt_adjusted_daydate[1]);
            returnPage.addObject("daily_dt_adjusted_daydate2", daily_dt_adjusted_daydate[2]);
            returnPage.addObject("daily_dt_adjusted_daydate3", daily_dt_adjusted_daydate[3]);
            returnPage.addObject("daily_dt_adjusted_daydate4", daily_dt_adjusted_daydate[4]);
            returnPage.addObject("daily_dt_adjusted_daydate5", daily_dt_adjusted_daydate[5]);
            returnPage.addObject("daily_dt_adjusted_daydate6", daily_dt_adjusted_daydate[6]);




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

            // daily_sunrise_adjusted_hour
            //returnPage.addObject("daily_sunrise_adjusted_hour", daily_sunrise_adjusted_hour);
            returnPage.addObject("daily_sunrise_adjusted_hour0", daily_sunrise_adjusted_hour[0]);
            returnPage.addObject("daily_sunrise_adjusted_hour1", daily_sunrise_adjusted_hour[1]);
            returnPage.addObject("daily_sunrise_adjusted_hour2", daily_sunrise_adjusted_hour[2]);
            returnPage.addObject("daily_sunrise_adjusted_hour3", daily_sunrise_adjusted_hour[3]);
            returnPage.addObject("daily_sunrise_adjusted_hour4", daily_sunrise_adjusted_hour[4]);
            returnPage.addObject("daily_sunrise_adjusted_hour5", daily_sunrise_adjusted_hour[5]);
            returnPage.addObject("daily_sunrise_adjusted_hour6", daily_sunrise_adjusted_hour[6]);

            // daily_sunrise_adjusted_daydate
            //returnPage.addObject("daily_sunrise_adjusted_daydate", daily_sunrise_adjusted_daydate);
            returnPage.addObject("daily_sunrise_adjusted_daydate0", daily_sunrise_adjusted_daydate[0]);
            returnPage.addObject("daily_sunrise_adjusted_daydate1", daily_sunrise_adjusted_daydate[1]);
            returnPage.addObject("daily_sunrise_adjusted_daydate2", daily_sunrise_adjusted_daydate[2]);
            returnPage.addObject("daily_sunrise_adjusted_daydate3", daily_sunrise_adjusted_daydate[3]);
            returnPage.addObject("daily_sunrise_adjusted_daydate4", daily_sunrise_adjusted_daydate[4]);
            returnPage.addObject("daily_sunrise_adjusted_daydate5", daily_sunrise_adjusted_daydate[5]);
            returnPage.addObject("daily_sunrise_adjusted_daydate6", daily_sunrise_adjusted_daydate[6]);



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

            // daily_sunset_adjusted_hour
            //returnPage.addObject("daily_sunset_adjusted_hour", daily_sunset_adjusted_hour);
            returnPage.addObject("daily_sunset_adjusted_hour0", daily_sunset_adjusted_hour[0]);
            returnPage.addObject("daily_sunset_adjusted_hour1", daily_sunset_adjusted_hour[1]);
            returnPage.addObject("daily_sunset_adjusted_hour2", daily_sunset_adjusted_hour[2]);
            returnPage.addObject("daily_sunset_adjusted_hour3", daily_sunset_adjusted_hour[3]);
            returnPage.addObject("daily_sunset_adjusted_hour4", daily_sunset_adjusted_hour[4]);
            returnPage.addObject("daily_sunset_adjusted_hour5", daily_sunset_adjusted_hour[5]);
            returnPage.addObject("daily_sunset_adjusted_hour6", daily_sunset_adjusted_hour[6]);

            // daily_sunset_adjusted_daydate
            //returnPage.addObject("daily_sunset_adjusted_daydate", daily_sunset_adjusted_daydate);
            returnPage.addObject("daily_sunset_adjusted_daydate0", daily_sunset_adjusted_daydate[0]);
            returnPage.addObject("daily_sunset_adjusted_daydate1", daily_sunset_adjusted_daydate[1]);
            returnPage.addObject("daily_sunset_adjusted_daydate2", daily_sunset_adjusted_daydate[2]);
            returnPage.addObject("daily_sunset_adjusted_daydate3", daily_sunset_adjusted_daydate[3]);
            returnPage.addObject("daily_sunset_adjusted_daydate4", daily_sunset_adjusted_daydate[4]);
            returnPage.addObject("daily_sunset_adjusted_daydate5", daily_sunset_adjusted_daydate[5]);
            returnPage.addObject("daily_sunset_adjusted_daydate6", daily_sunset_adjusted_daydate[6]);





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


            // daily_pop_adjusted
            // returnPage.addObject("daily_pop_adjusted", daily_pop_adjusted);
            returnPage.addObject("daily_pop_adjusted0", daily_pop_adjusted[0]);
            returnPage.addObject("daily_pop_adjusted1", daily_pop_adjusted[1]);
            returnPage.addObject("daily_pop_adjusted2", daily_pop_adjusted[2]);
            returnPage.addObject("daily_pop_adjusted3", daily_pop_adjusted[3]);
            returnPage.addObject("daily_pop_adjusted4", daily_pop_adjusted[4]);
            returnPage.addObject("daily_pop_adjusted5", daily_pop_adjusted[5]);
            returnPage.addObject("daily_pop_adjusted6", daily_pop_adjusted[6]);


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


            //returnPage.setViewName("mainpage"); *********
            //returnPage.setViewName("frontpage");

        } catch (

                IOException e) {
            System.out.println(e.getMessage());
        }


        return returnPage;
    }


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
