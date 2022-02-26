package com.example.googlemap;

import android.os.AsyncTask;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

public class GetNearbyRestaurant extends AsyncTask<Object,String,String>
{
private String googleplaceData,url;
private GoogleMap mMap;

    @Override
    protected String doInBackground(Object... objects) {
        mMap=(GoogleMap) objects[0];
        url=(String) objects[1];
        DownloadUrl downloadUrl=new DownloadUrl();
        try
        {
            googleplaceData=downloadUrl.ReadUrl(url);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        return googleplaceData;
    }

    @Override
    protected void onPostExecute(String s) {
        List<HashMap<String,String>> nearbyplacelist =null;

        dataPasser dataPasser=new dataPasser();
        nearbyplacelist=dataPasser.parse(s);

        DisplayNearpyPlaces(nearbyplacelist);

    }
    private void DisplayNearpyPlaces(List<HashMap<String,String>> nearbyplacelist)
    {
        for(int i=0;i<nearbyplacelist.size();i++)
        {
            MarkerOptions markerOptions=new MarkerOptions();
            HashMap<String,String> googleNearpybyPlace= nearbyplacelist.get(i);



            String name=googleNearpybyPlace.get("place_name");
            String vicinity=googleNearpybyPlace.get("vicinity");
            double lat=Double.parseDouble(googleNearpybyPlace.get("lat"));
            double lng=Double.parseDouble(googleNearpybyPlace.get("lng"));

            LatLng latLng = new LatLng(lat,lng);
            markerOptions.position(latLng);
            markerOptions.title(name+":"+vicinity);
            markerOptions.icon(BitmapDescriptorFactory.defaultMarker());
            mMap.addMarker(markerOptions);
            mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
            mMap.animateCamera(CameraUpdateFactory.zoomTo(8));


        }
    }
}
