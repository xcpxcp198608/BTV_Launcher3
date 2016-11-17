package com.wiatec.btv_launcher.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by PX on 2016-11-16.
 */

public class Location_Weather {
    /**
     * 需要在子线程执行
     * @return
     */
    public static Address getAddress () {
        HttpURLConnection httpURLConnection = null;
        BufferedReader bufferedReader = null;
        InputStream inputStream = null;
        Address address = null;
        String url = "http://ip.taobao.com/service/getIpInfo2.php?ip=myip";
        try {
            URL url1 = new URL(url);
            httpURLConnection = (HttpURLConnection) url1.openConnection();
            httpURLConnection.setUseCaches(false);
            if(httpURLConnection.getResponseCode() == HttpURLConnection.HTTP_OK){
                Logger.d("start");
                inputStream = httpURLConnection.getInputStream();
                bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                String temple = "";
                StringBuilder jsonString = new StringBuilder();
                while ((temple = bufferedReader.readLine()) != null){
                    jsonString.append(temple);
                }
                JSONObject jsonObject = new JSONObject(jsonString.toString());
                String code = jsonObject.getString("code");
                if("0".equals(code)){
                    JSONObject data = jsonObject.getJSONObject("data");
                    address = new Address();
                    address.setIp(data.getString("ip"));
                    address.setCountry(data.getString("country"));
                    address.setCountryId(data.getString("country_id"));
                    address.setArea(data.getString("area"));
                    address.setAreaId(data.getString("area_id"));
                    address.setRegion(data.getString("region"));
                    address.setRegionId(data.getString("region_id"));
                    address.setCity(data.getString("city"));
                    address.setCityId(data.getString("city_id"));
                    address.setIsp(data.getString("isp"));
                    address.setIspId(data.getString("isp_id"));
                }
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }finally {
            try {
                if(httpURLConnection!= null){
                    httpURLConnection.disconnect();
                }
                if(inputStream != null){
                    inputStream.close();
                }
                if(bufferedReader!=null){
                    bufferedReader.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return address;
    }

    /**
     * city为请求天气的城市，用英文名称，中文城市用拼音
     * @param city
     */
    public static String getWeather (String city){
        String url = "http://apis.baidu.com/heweather/weather/free";
        String parameter = "city="+city;
        String httpUrl = url+"?"+parameter;
        String apiKey = "6d2c261609e14bac4c78081dd57c7ddc";
        HttpURLConnection httpURLConnection = null;
        InputStream inputStream = null;
        BufferedReader bufferedReader = null;
        StringBuilder result = new StringBuilder();

        try {
            URL url1 = new URL(httpUrl);
            httpURLConnection = (HttpURLConnection) url1.openConnection();
            httpURLConnection.setRequestMethod("GET");
            httpURLConnection.setRequestProperty("apikey",apiKey);
            httpURLConnection.connect();
            if(httpURLConnection.getResponseCode() == HttpURLConnection.HTTP_OK){
                inputStream = httpURLConnection.getInputStream();
                bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                String temple ="";
                while ((temple = bufferedReader.readLine()) != null){
                    result.append(temple);
                    result.append("\r\n");
                }
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                if(bufferedReader!=null){
                    bufferedReader.close();
                }
                if(inputStream!=null) {
                    inputStream.close();
                }
                if(httpURLConnection!=null){
                    httpURLConnection.disconnect();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return result.toString();
    }

    public static class Address {
        private String ip;
        private String country;
        private String countryId;
        private String area;
        private String areaId;
        private String region;
        private String regionId;
        private String city;
        private String cityId;
        private String isp;
        private String ispId;

        public String getIp() {
            return ip;
        }

        public void setIp(String ip) {
            this.ip = ip;
        }

        public String getCountry() {
            return country;
        }

        public void setCountry(String country) {
            this.country = country;
        }

        public String getCountryId() {
            return countryId;
        }

        public void setCountryId(String countryId) {
            this.countryId = countryId;
        }

        public String getArea() {
            return area;
        }

        public void setArea(String area) {
            this.area = area;
        }

        public String getAreaId() {
            return areaId;
        }

        public void setAreaId(String areaId) {
            this.areaId = areaId;
        }

        public String getRegion() {
            return region;
        }

        public void setRegion(String region) {
            this.region = region;
        }

        public String getRegionId() {
            return regionId;
        }

        public void setRegionId(String regionId) {
            this.regionId = regionId;
        }

        public String getCity() {
            return city;
        }

        public void setCity(String city) {
            this.city = city;
        }

        public String getCityId() {
            return cityId;
        }

        public void setCityId(String cityId) {
            this.cityId = cityId;
        }

        public String getIsp() {
            return isp;
        }

        public void setIsp(String isp) {
            this.isp = isp;
        }

        public String getIspId() {
            return ispId;
        }

        public void setIspId(String ispId) {
            this.ispId = ispId;
        }

        @Override
        public String toString() {
            return "Address{" +
                    "ip='" + ip + '\'' +
                    ", country='" + country + '\'' +
                    ", countryId='" + countryId + '\'' +
                    ", area='" + area + '\'' +
                    ", areaId='" + areaId + '\'' +
                    ", region='" + region + '\'' +
                    ", regionId='" + regionId + '\'' +
                    ", city='" + city + '\'' +
                    ", cityId='" + cityId + '\'' +
                    ", isp='" + isp + '\'' +
                    ", ispId='" + ispId + '\'' +
                    '}';
        }
    }
}
