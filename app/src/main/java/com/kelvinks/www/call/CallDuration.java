package com.kelvinks.www.call;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;

public class CallDuration extends BroadcastReceiver {
    static long start_time, end_time;;
    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (action.equalsIgnoreCase("android.intent.action.PHONE_STATE")) {
            if (intent.getStringExtra(TelephonyManager.EXTRA_STATE).equals(
                    TelephonyManager.EXTRA_STATE_RINGING)) {
                start_time = System.currentTimeMillis();
            }
            if (intent.getStringExtra(TelephonyManager.EXTRA_STATE).equals(
                    TelephonyManager.EXTRA_STATE_IDLE)) {
                end_time = System.currentTimeMillis();
                //Total time talked =
                long total_time = end_time - start_time;
                Log.i("hasil",":"+total_time);
                //Store total_time somewhere or pass it to an Activity using intent
            }
        }
    }
}
