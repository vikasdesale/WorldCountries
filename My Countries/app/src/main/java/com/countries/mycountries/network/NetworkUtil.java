package com.countries.mycountries.network;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.NonNull;

/**
 * Created by Dell on 7/20/2016.
 */

public class NetworkUtil {
    public static boolean isNetworkConnected(@NonNull Context context) {

        try {
            ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo ni = cm.getActiveNetworkInfo();
            return ni != null;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

    }
}
