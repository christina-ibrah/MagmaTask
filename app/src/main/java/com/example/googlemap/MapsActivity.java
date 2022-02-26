package com.example.googlemap;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;

import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.example.googlemap.databinding.ActivityMapsBinding;

import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.SplittableRandom;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener {

    private GoogleMap mMap;
    private ActivityMapsBinding binding;
    private GoogleApiClient googleApiClient;
    private LocationRequest locationRequest;
    private Location lastLoc;
    private Marker currentlocMarker;
    private static final int requestloccode=99;
    double lat,lng;
    private int radius=10000;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_maps);


        if(Build.VERSION.SDK_INT > Build.VERSION_CODES.M)
        {
            checklocpermission();
        }

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


        Spinner spinner = (Spinner) findViewById(R.id.places_spinner);
        additemsinspinner(spinner);

        MarkerOptions usermarkerOptions=new MarkerOptions();

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mMap.setInfoWindowAdapter(new CustomInfoAdapter(MapsActivity.this));
                switch (position)
                {
                    case 0 :
                        break;

                    case 1:
                    {
                        showmap(33.85148430277257,35.895525763213946,usermarkerOptions,
                                String.valueOf(String.valueOf(parent.getItemAtPosition(position))));
                        break;
                    }
                    case 2:
                    {
                        showmap(33.85217073479985,35.89438946093824,usermarkerOptions,
                                String.valueOf(String.valueOf(parent.getItemAtPosition(position))));
                        break;
                    }
                    case 3:
                    {

                        showmap(33.85334017189446,35.89438946093824,usermarkerOptions,
                                String.valueOf(String.valueOf(parent.getItemAtPosition(position))));
                        break;
                    }

                    case 4:
                        {

                        showmap(33.85454300475094,35.894561122304474,usermarkerOptions,
                                String.valueOf(String.valueOf(parent.getItemAtPosition(position))));

                            Toast.makeText(MapsActivity.this,String.valueOf(parent.getItemAtPosition(position)),Toast.LENGTH_SHORT).show();
                            break;
                    }
                    case 5:
                    {

                        showmap(33.85129821373707,35.89446263654391,usermarkerOptions,
                                String.valueOf(parent.getItemAtPosition(position)));
                        break;

                    }
                    case 6:
                    {

                        showmap(33.85048738635312,35.89664059012788,usermarkerOptions,
                                String.valueOf(parent.getItemAtPosition(position)));

                        Toast.makeText(MapsActivity.this,String.valueOf(parent.getItemAtPosition(position)),Toast.LENGTH_SHORT).show();

                        break;

                    }

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        ImageButton search = (ImageButton) findViewById(R.id.search_btn);
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                placesearch();
            }
        });



        ImageButton rest=(ImageButton) findViewById(R.id.rest_search);
        String restaurant="restaurant";
        Object transferData[]=new Object[2];

        rest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String url=getUrl(lat,lng,restaurant);
                transferData[0]=mMap;
                transferData[1]=url;
                GetNearbyRestaurant getNearbyRestaurant=new GetNearbyRestaurant() ;
                getNearbyRestaurant.execute(transferData);


            }
        });

    }

    private String getUrl(double lat, double lng, String restaurant)
    {
        StringBuilder googleurl=new StringBuilder("https://maps.googleapis.com/maps/api/place/nearbysearch/json?");
        googleurl.append("location="+lat+","+lng);
        googleurl.append("&radius="+radius);
        googleurl.append("&type="+restaurant);
        googleurl.append("&sensor=true");
        googleurl.append("&key"+"AIzaSyAiAir1uMz3NwJDd9vjIhqeEuTUgw2S7VM");
        
        Log.d("google","url="+googleurl.toString());
        return googleurl.toString();


    }

    private void showmap(Double lat,Double lng,MarkerOptions usermarkerOptions,String t) {
        // TODO Auto-generated method stub
        String title="Latitude: "+lat+"\n"+
                "longitude: "+lng;

        LatLng latLng = new LatLng(lat,lng);
        usermarkerOptions.position(latLng);
        usermarkerOptions.title(t).snippet(title);
        usermarkerOptions.icon(BitmapDescriptorFactory.defaultMarker());
        mMap.addMarker(usermarkerOptions);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(8));


    }
    public void placesearch()
    {
        EditText search_txt=(EditText)findViewById(R.id.search_txt);
        String place=search_txt.getText().toString();
        List<Address> address=null;
        MarkerOptions usermarkerOptions=new MarkerOptions();
        if(!TextUtils.isEmpty(place))
        {
            Geocoder geocoder=new Geocoder(this);
            try
            {
                address=geocoder.getFromLocationName(place,6);

                if(address !=null) {
                    for (int i = 0; i < address.size(); i++) {
                        mMap.setInfoWindowAdapter(new CustomInfoAdapter(MapsActivity.this));

                        Address useraddress = address.get(i);
                        String title="Latitude: "+useraddress.getLatitude()+"\n"+
                                "longitude: "+useraddress.getLongitude();

                        LatLng latLng = new LatLng(useraddress.getLatitude(), useraddress.getLongitude());
                        usermarkerOptions.position(latLng);

                        usermarkerOptions.title(place).snippet(title);
                        Toast.makeText(this,"lkjkh"+usermarkerOptions.getTitle(),Toast.LENGTH_SHORT).show();

                        usermarkerOptions.icon(BitmapDescriptorFactory.defaultMarker());
                        mMap.addMarker(usermarkerOptions);
                        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
                        mMap.animateCamera(CameraUpdateFactory.zoomTo(8));
                    }
                }
                else
                {
                    Toast.makeText(this,"Place Not Found",Toast.LENGTH_SHORT).show();
                }
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
        else
        {
            Toast.makeText(this,"Please Write Place",Toast.LENGTH_SHORT).show();
        }
      search_txt.setText("");
    }

    public void additemsinspinner(Spinner spinner)
    {
        ArrayAdapter<String> adapter=
                new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,
                        getResources().getStringArray(R.array.places));

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner.setAdapter(adapter);
    }


    @Override
    public void onMapReady(GoogleMap googleMap)
    {
        mMap = googleMap;

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED )
        {
            BuildGoogleApiClient();
            mMap.setMyLocationEnabled(true);
        }

    }
  public boolean checklocpermission()
  {
      if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
      {
          if(ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.ACCESS_FINE_LOCATION))
          {
               ActivityCompat.requestPermissions(this,new String[] {Manifest.permission.ACCESS_FINE_LOCATION},requestloccode);
          }
          else
          {
              ActivityCompat.requestPermissions(this,new String[] {Manifest.permission.ACCESS_FINE_LOCATION},requestloccode);
          }
          return false;

      }
      else {
          return true;
      }
  }
    //create  new client
    protected synchronized void BuildGoogleApiClient() {
        googleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API).build();

        googleApiClient.connect();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case requestloccode:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                        if (googleApiClient == null) {
                            BuildGoogleApiClient();
                        }
                        mMap.setMyLocationEnabled(true);
                    }
                } else {
                    Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show();
                }
                return;
        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        locationRequest = new LocationRequest();
        locationRequest.setInterval(1000);
        locationRequest.setFastestInterval(1000);
        locationRequest.setPriority(locationRequest.getPriority());

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED )
        {
            LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, locationRequest, this);

        }


    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(@NonNull Location location) {
        lat=location.getLatitude();
        lng=location.getLongitude();

        lastLoc=location;
        if(currentlocMarker!=null)
        {
            currentlocMarker.remove();
        }
        mMap.setInfoWindowAdapter(new CustomInfoAdapter(MapsActivity.this));
        String snippet="Latitude: "+location.getLatitude()+"\n"
                +"Longitude: "+location.getLongitude();
        //get location name
        String locality=null;
        Geocoder gcd = new Geocoder(this, Locale.getDefault());
        List<Address> addresses = null;
        try {
            addresses = gcd.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (addresses != null && addresses.size() > 0) {
             locality = addresses.get(0).getLocality();
        }

         LatLng sydney = new LatLng(location.getLatitude(), location.getLongitude());
         MarkerOptions markerOptions=new MarkerOptions();
         markerOptions.position(sydney);
         markerOptions.title(locality).snippet(snippet);
         markerOptions.icon(BitmapDescriptorFactory.defaultMarker());
         Toast.makeText(this,"lk"+markerOptions.getTitle(),Toast.LENGTH_SHORT).show();

        currentlocMarker=mMap.addMarker(markerOptions);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
        mMap.animateCamera(CameraUpdateFactory.zoomBy(14));

        if(googleApiClient != null)
        {
            LocationServices.FusedLocationApi.removeLocationUpdates(googleApiClient,this);

        }

    }
}