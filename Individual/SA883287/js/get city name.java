@ResponseBody 
    @RequestMapping("/location/cityName/{CITYNAME}")
    public String get_cityLocation(@PathVariable String CITYNAME) throws ParseException {
        //https://api.opencagedata.com/geocode/v1/json?q=Albany&key=17f20d72d5f6410b8f89d59f0afbb590&pretty=1
        String API_KEY = "17f20d72d5f6410b8f89d59f0afbb590";
        
        String URL = "https:api.opencagedata.com/geocode/v1/json?q=" + CITYNAME + "&key=" + API_KEY+ "&pretty=1";
        
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

                // "lag long"
                String result_latlong;
                JSONArray JSONArray_weather = jsonObject.getJSONArray("geometry");
                if (JSONArray_weather.length() > 0) {
                    JSONObject JSONObject_weather = JSONArray_weather.getJSONObject(0);
                    double lat = JSONObject_weather.getDouble("lat");
                    double lng = JSONObject_weather.getDouble("long");

                    result_latlong = "lat: " + lat + " long: " + lng;
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
    