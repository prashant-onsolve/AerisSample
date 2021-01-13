package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;

import com.aerisweather.aeris.communication.AerisEngine;
import com.aerisweather.aeris.maps.AerisMapOptions;
import com.aerisweather.aeris.maps.AerisMapsEngine;
import com.aerisweather.aeris.tiles.AerisAmp;

public class MainActivity extends FragmentActivity {

    private AerisMapOptions m_mapOptions1;
    private AerisAmp m_aerisAmp1;

    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setTheme(R.style.);
        AerisEngine.initWithKeys(this.getString(R.string.aerisapi_client_id), this.getString(R.string.aerisapi_client_secret), this);
        AerisMapsEngine.getInstance(this).getDefaultPointParameters().setLightningParameters("dt:-1", 500, null, null);

        m_aerisAmp1 = new AerisAmp(this.getString(R.string.aerisapi_client_id), this.getString(R.string.aerisapi_client_secret));
        m_mapOptions1 = new AerisMapOptions();
        m_mapOptions1.setAerisAMP(m_aerisAmp1);

        Intent intent = new Intent(this, com.example.myapplication.MapActivity.class);
        startActivity(intent);
    }

}
