package com.example.myapplication;

import android.app.Application;

import com.aerisweather.aeris.communication.AerisEngine;
import com.aerisweather.aeris.maps.AerisMapsEngine;


public class BaseApplication extends Application
{
	@Override
	public void onCreate()
    {
		super.onCreate();

		// setting up secret key and client id for oauth to aeris
		AerisEngine.initWithKeys(this.getString(R.string.aerisapi_client_id), this.getString(R.string.aerisapi_client_secret), this);




		AerisMapsEngine.getInstance(this).getDefaultPointParameters().setLightningParameters("dt:-1", 500, null, null);
    }

}
