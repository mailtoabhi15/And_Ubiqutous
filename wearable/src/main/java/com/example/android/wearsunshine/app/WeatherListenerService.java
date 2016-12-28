package com.example.android.wearsunshine.app;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.wearable.DataApi;
import com.google.android.gms.wearable.DataEvent;
import com.google.android.gms.wearable.DataEventBuffer;
import com.google.android.gms.wearable.DataItem;
import com.google.android.gms.wearable.DataMap;
import com.google.android.gms.wearable.DataMapItem;
import com.google.android.gms.wearable.Wearable;
import com.google.android.gms.wearable.WearableListenerService;

import java.util.concurrent.TimeUnit;

public class WeatherListenerService extends WearableListenerService {

    private static final String TAG = "WeatherListenerService";

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public void onDataChanged(DataEventBuffer dataEventBuffer) {
        LOGD(TAG, "onDataChanged: " + dataEventBuffer);


        for (DataEvent event: dataEventBuffer) {
            if (event.getType() == DataEvent.TYPE_CHANGED) {

                // DataItem changed
                DataItem item = event.getDataItem();
                if (item.getUri().getPath().compareTo("/wear") == 0) {
                    DataMap dataMap = DataMapItem.fromDataItem(item).getDataMap();
//                    updateCount(dataMap.getInt(COUNT_KEY));
                    String wearHigh = dataMap.getString("wearhightemp");
                    String wearLow = dataMap.getString("wearlowtemp");
                    int weatherId = dataMap.getInt("weatherId");

                    //Dixit:Saving last received data data locally
                    SharedPreferences sharedPref = getApplicationContext().getSharedPreferences(
                            getString(R.string.PREF_FILE_KEY),
                            WearableListenerService.MODE_PRIVATE);

                    SharedPreferences.Editor editor = sharedPref.edit();
                    editor.putString(getString(R.string.wearhightemp),wearHigh);
                    editor.putString(getString(R.string.wearhightemp),wearLow);
                    editor.putInt(getString(R.string.wearhightemp),weatherId);
                    editor.apply();

                    Intent intent = new Intent("ACTION_WEATHER_UPDATE");
                    intent.putExtra("wearhightemp",wearHigh);
                    intent.putExtra("wearlowtemp",wearLow);
                    intent.putExtra("weatherId",weatherId);
                    sendBroadcast(intent);

                } else if (event.getType() == DataEvent.TYPE_DELETED) {

                }
            }

        }
    }

    public static void LOGD(final String tag, String message) {
        if (Log.isLoggable(tag, Log.DEBUG)) {
            Log.d(tag, message);
        }
    }
}
