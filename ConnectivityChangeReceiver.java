package com.weapplinse.xitix.Service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.weapplinse.xitix.network.Connectivity;

public class ConnectivityChangeReceiver extends BroadcastReceiver {

    Connectivity cd;

    Boolean isInternetPresent = false;

    @Override
    public void onReceive(Context context, Intent intent) {

        cd = new Connectivity();

        isInternetPresent = cd.isConnected(context);

        if (isInternetPresent) {

            context.startService(new Intent(context, InternetService.class));

        }

    }

}
