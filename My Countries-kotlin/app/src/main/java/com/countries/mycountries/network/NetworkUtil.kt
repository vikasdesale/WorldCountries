package com.countries.mycountries.network

import android.content.Context
import android.net.ConnectivityManager

/**
 * Created by Dell on 7/20/2016.
 */

object NetworkUtil {
    fun isNetworkConnected(context: Context): Boolean {

        try {
            val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val ni = cm.activeNetworkInfo
            return ni != null
        } catch (e: Exception) {
            e.printStackTrace()
            return false
        }

    }
}
