package com.example.administrator.login;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;

public class BaseActivity extends AppCompatActivity{
    private ForceOfflineReceieve receiver;

    protected void onResume(){
        super.onResume();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("com.example.administrator.login.FORCE_OFFLINE");
        receiver = new ForceOfflineReceieve();
        registerReceiver(receiver,intentFilter);
    }

    protected void onPause(){
        super.onPause();
        if(receiver !=null){
            unregisterReceiver(receiver);
            receiver = null;
        }
    }

    protected void onDestroy(){
        super.onDestroy();
        ActivityCollector.removeActivity(this);
    }

    private class ForceOfflineReceieve extends BroadcastReceiver {
        @Override
        public void onReceive(final Context context, Intent intent) {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setMessage("Are you sure to logout?");
            builder.setCancelable(false);
            builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    ActivityCollector.finishAll();
                    Intent intent = new Intent(context, Login.class);
                    context.startActivity(intent);
                    finish();
                }
            });
            builder.show();
             }
        }
    }