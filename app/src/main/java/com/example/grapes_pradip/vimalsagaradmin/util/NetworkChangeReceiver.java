package com.example.grapes_pradip.vimalsagaradmin.util;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.example.grapes_pradip.vimalsagaradmin.activities.MainActivity;



@SuppressWarnings("ALL")
public class NetworkChangeReceiver extends BroadcastReceiver {

    public NetworkChangeReceiver() {

    }

    public interface NetworkChange {
        void onNetworkChange(String status);
    }

    @SuppressLint("UnsafeProtectedBroadcastReceiver")
    @Override
    public void onReceive(Context context, final Intent intent) {

        String status = NetworkClass.getConnectivityStatusString(context);
        MainActivity csActivity = new MainActivity();
        NetworkChange csNetworkChange = csActivity instanceof NetworkChange ? csActivity : null;
        csNetworkChange.onNetworkChange(status);

    }
}
