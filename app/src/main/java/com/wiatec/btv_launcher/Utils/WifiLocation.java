package com.wiatec.btv_launcher.Utils;

import android.Manifest;
import android.content.Context;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Process;

import com.px.common.utils.Logger;

import java.util.List;

/**
 * Created by PX on 2016-11-16.
 */

public class WifiLocation {
    private LocationManager locationManager;
    private String locationProvider;
    private Context context;
    private OnLocationListener onLocationListener;

    public WifiLocation(Context context) {
        this.context = context;
        locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        List<String> locationProviders = locationManager.getProviders(true);
        Logger.d(locationProviders.toString());
        if (locationProviders.contains(LocationManager.NETWORK_PROVIDER)){
            locationProvider = LocationManager.NETWORK_PROVIDER;
        }else {
            Logger.d("System can not provide location service");
        }
    }

    public String[] getLocation (){
        String [] locationCoordinate = new String [2];
        int i = context.checkPermission("android.permission.ACCESS_COARSE_LOCATION", Process.myPid() ,Process.myUid());
        Logger.d(i+"");
        if(locationProvider == null){
            return null;
        }
        android.location.Location location = locationManager.getLastKnownLocation(locationProvider);
        if(location != null){
            locationCoordinate[0] = String.valueOf(location.getLatitude());
            locationCoordinate[1] = String.valueOf(location.getLongitude());
        }
        return locationCoordinate;
    }

    public interface  OnLocationListener {
        void onLocationChange (android.location.Location location);
    }

    public void setOnLoCationListener (final OnLocationListener onLocationListener){
        this.onLocationListener = onLocationListener;
        int i = context.checkPermission("android.permission.ACCESS_COARSE_LOCATION", Process.myPid() ,Process.myUid());
        Logger.d(i+"");
        locationManager.requestLocationUpdates(locationProvider, 6000, 0, new LocationListener() {
            @Override
            public void onLocationChanged(android.location.Location location) {
                onLocationListener.onLocationChange(location);
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

            }
        });
    }
}
