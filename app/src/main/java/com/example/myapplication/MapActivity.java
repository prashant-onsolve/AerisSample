package com.example.myapplication;

import android.Manifest;
import android.app.Activity;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.aerisweather.aeris.maps.AerisMapContainerView;
import com.aerisweather.aeris.maps.AerisMapOptions;
import com.aerisweather.aeris.maps.AerisMapView;
import com.aerisweather.aeris.model.AerisPermissions;
import com.aerisweather.aeris.tiles.AerisAmp;
import com.aerisweather.aeris.tiles.AerisAmpGetLayersTask;
import com.aerisweather.aeris.tiles.AerisAmpLayer;
import com.aerisweather.aeris.tiles.AerisAmpOnGetLayersTaskCompleted;
import com.aerisweather.aeris.tiles.AerisTile;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;

import java.util.ArrayList;
import java.util.List;

public class MapActivity extends Activity implements OnMapReadyCallback, GoogleMap.OnMapLoadedCallback{

private static final int REQUEST_PERMISSIONS = 0;

    GoogleMap m_googleMap;
    protected AerisMapView m_aerisMapView;
    private AerisMapOptions m_mapOptions;
    private AerisAmp m_aerisAmp;
    private boolean m_isMapReady = false;
    private boolean m_isAmpReady = false;


    @Override
    protected void onResume() {
        super.onResume();
        if(m_aerisMapView != null){
            m_aerisMapView.onResume();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();


    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.aeris_map);

        requestMultiplePermissions();
        AerisMapContainerView mapContainer = findViewById(R.id.mapView);
        m_aerisMapView = mapContainer.getAerisMapView();
        m_aerisMapView.onCreate(savedInstanceState);


        m_aerisAmp = new AerisAmp(this.getString(R.string.aerisapi_client_id), this.getString(R.string.aerisapi_client_secret));
        m_mapOptions = new AerisMapOptions();
        m_mapOptions.setAerisAMP(m_aerisAmp);

        //start the task to get the AMP layers
        try
        {
            //get all the possible layers, then get permissions from the API and generate a list of permissible layers
            new AerisAmpGetLayersTask(new GetLayersTaskCallback(), m_aerisAmp).execute();
        }
        catch (Exception ex)
        {
            String s = ex.getMessage();
            //if the task fails, keep going without AMP layers
        }

        m_aerisMapView.getMapAsync(this);

    }


    public void onMapReady() {

        //Set map on loaded
        m_googleMap.setOnMapLoadedCallback(this);

    }

    public void onMapLoaded() {

        m_aerisMapView.setVisibility(View.VISIBLE);
        m_aerisMapView.addLayer(AerisTile.SAT_GLOBAL_INFRARED);

    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();

    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        m_isMapReady = true;
        m_googleMap = googleMap;
        m_aerisMapView.init(googleMap);


        if (m_isAmpReady)
        {
            onMapReady();
        }

    }

    public class GetLayersTaskCallback implements AerisAmpOnGetLayersTaskCompleted
    {
        public GetLayersTaskCallback() { }

        public void onAerisAmpGetLayersTaskCompleted(ArrayList<AerisAmpLayer> permissibleLayers,
                                                     AerisPermissions permissions)
        {
            m_isAmpReady = true;

            if (m_isMapReady)
            {
                onMapReady();
            }
        }
    }


    private void requestMultiplePermissions()
    {
        String locationPermission = Manifest.permission.ACCESS_FINE_LOCATION;
        String readExternalPermission = Manifest.permission.READ_EXTERNAL_STORAGE;
        int hasLocPermission = ContextCompat.checkSelfPermission(com.example.myapplication.MapActivity.this, locationPermission);
        int hasReadPermission = ContextCompat.checkSelfPermission(com.example.myapplication.MapActivity.this, readExternalPermission);
        List<String> permissions = new ArrayList<String>();

        if (hasLocPermission != PackageManager.PERMISSION_GRANTED)
        {
            permissions.add(locationPermission);
        }

        if (hasReadPermission != PackageManager.PERMISSION_GRANTED)
        {
            permissions.add(readExternalPermission);
        }

        if (!permissions.isEmpty())
        {
            String[] params = permissions.toArray(new String[permissions.size()]);
            ActivityCompat.requestPermissions(com.example.myapplication.MapActivity.this, params, REQUEST_PERMISSIONS);
        }
        else
        {
            // We already have permission, so handle as normal
            // m_aerisMapView.getMapAsync(this);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults)
    {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode)
        {
            case REQUEST_PERMISSIONS:
            {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                {
                    // m_aerisMapView.getMapAsync(this);
                }
                else
                {
                    Toast.makeText(com.example.myapplication.MapActivity.this, "Permission denied",
                            Toast.LENGTH_LONG).show();
                }
                return;
            }
        }
    }
}
