package com.kelvinks.www.call;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class CallActivity extends AppCompatActivity {
    static double start_time, end_time,total_time;
    private EditText edtcall;
    private TextView txttime;
    private int calls;
    private Button btnsubmit;
    private TelephonyManager telephonyManager;
    private   PhoneStateListener callStateListener;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_call);
        init();

         telephonyManager =
                (TelephonyManager)getSystemService(CallActivity.this.TELEPHONY_SERVICE);

        callStateListener =new PhoneStateListener(){
            @Override
            public void onCallStateChanged(int state, String incomingNumber) {

                if(state==TelephonyManager.CALL_STATE_RINGING){

                }
                if(state==TelephonyManager.CALL_STATE_OFFHOOK){
                    start_time = System.currentTimeMillis();
                }

                if(state==TelephonyManager.CALL_STATE_IDLE){
                    end_time = System.currentTimeMillis();
                    //Total time talked =
                    total_time = (end_time - start_time) / 1000.0;
//                    Toast.makeText(CallActivity.this, "Waktu Telepon :" + total_time,
//                            Toast.LENGTH_LONG).show();
                        txttime.setText("Total Waktu Telepon :" + String.valueOf(total_time) + " detik");

                }
            }
        };

        telephonyManager.listen(callStateListener,PhoneStateListener.LISTEN_CALL_STATE);

        btnsubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(validated()){
                    if (Build.VERSION.SDK_INT < 23) {
                        dialPhoneNumber(edtcall.getText().toString());
                    }else {
                        if (ActivityCompat.checkSelfPermission(CallActivity.this,
                                Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
                            dialPhoneNumber(edtcall.getText().toString());
                        }else {
                            final String[] PERMISSIONS_STORAGE = {Manifest.permission.CALL_PHONE};
                            //Asking request Permissions
                            ActivityCompat.requestPermissions(CallActivity.this, PERMISSIONS_STORAGE, 9);
                        }
                    }
                }
            }
        });


    }

    private void init(){
        edtcall=findViewById(R.id.edtcall);
        btnsubmit=findViewById(R.id.btnsubmit);
        txttime=findViewById(R.id.txttime);
//        hasil=String.valueOf(edtcall.getText().toString());
    }

    private boolean validated(){
        String error="No telp Belum Diisi !!!";
        boolean valid=true;
        if(edtcall.getText().toString().equals("")){
            edtcall.setError(error);
            valid=false;
        }
        return valid;
    }

    private void dialPhoneNumber(String number){
        if(ActivityCompat.checkSelfPermission(CallActivity.this, Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED){
            Intent call= new Intent(Intent.ACTION_CALL);
            call.setData(Uri.parse("tel:"+number));
            startActivity(call);

        }
        else{
            Toast.makeText(CallActivity.this,"You don't assign permission.", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,  String[] permissions,  int[] grantResults) {
        boolean permisionGranted=false;
        switch (requestCode){
            case 9:
                permisionGranted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                break;
        }
        if(permisionGranted){
            dialPhoneNumber(edtcall.getText().toString());
        }else{
            Toast.makeText(CallActivity.this, "You don't assign permission.", Toast.LENGTH_SHORT).show();
        }
    }
    //menaangani proses minta izin dengan sdk diatas 23
}
