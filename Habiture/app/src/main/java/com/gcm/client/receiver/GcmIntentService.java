package com.gcm.client.receiver;

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.media.SoundPool;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.habiture.Friend;
import com.habiture.HabitureModule;
import com.ntil.habiture.DeclareFragment;
import com.ntil.habiture.HomeActivity;
import com.ntil.habiture.InviteFriendFragment;
import com.ntil.habiture.MainApplication;
import com.ntil.habiture.R;

import java.util.List;

import utils.exception.ExceptionAlertDialog;
import utils.exception.UnhandledException;


public class GcmIntentService extends IntentService {
    public static final int NOTIFICATION_ID = 1;
    private NotificationManager mNotificationManager;
    static final String TAG = "GCMModel";
    NotificationCompat.Builder builder;

    public GcmIntentService() {
        super("GcmIntentService");
        Log.i(TAG, "GcmIntentService");
    }


    @Override
    protected void onHandleIntent(Intent intent) {
        Log.i(TAG, "onHandleIntent");
        Bundle extras = intent.getExtras();
        GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(this);
        // The getMessageType() intent parameter must be the intent you received
        // in your BroadcastReceiver.
        String messageType = gcm.getMessageType(intent);

        if (!extras.isEmpty()) {  // has effect of unparcelling Bundle
            /*
             * Filter messages based on message type. Since it is likely that GCM
             * will be extended in the future with new message types, just ignore
             * any message types you're not interested in, or that you don't
             * recognize.
             */
            if (GoogleCloudMessaging.
                    MESSAGE_TYPE_SEND_ERROR.equals(messageType)) {
               // sendNotification("Send error: " + extras.toString());
            } else if (GoogleCloudMessaging.
                    MESSAGE_TYPE_DELETED.equals(messageType)) {
                //sendNotification("Deleted messages on server: " +extras.toString());
                // If it's a regular GCM message, do some work.
            } else if (GoogleCloudMessaging.
                    MESSAGE_TYPE_MESSAGE.equals(messageType)) {
                //LoadSound(Integer.parseInt(extras.getString("tool")));
                // This loop represents the service doing some work.
                /*for (int i=0; i<5; i++) {
                    Log.i(TAG, "Working... " + (i+1)
                            + "/5 @ " + SystemClock.elapsedRealtime());
                    try {
                        Thread.sleep(5000);
                    } catch (InterruptedException e) {
                    }
                }*/
                Log.i(TAG, "Completed work @ " + SystemClock.elapsedRealtime());
                // Post notification of received message.
                sendNotification(extras);
                Log.i(TAG, "Received: " + extras.toString());

                PlaySound(Integer.parseInt(extras.getString("tool")));

                insertBadgeNumber();

            }
        }
        // Release the wake lock provided by the WakefulBroadcastReceiver.
        GcmBroadcastReceiver.completeWakefulIntent(intent);
    }

    // Put the message into a notification and post it.
    // This is just one simple example of what you might choose to do with
    // a GCM message.
    private void sendNotification(Bundle extras) {
        mNotificationManager = (NotificationManager)
                this.getSystemService(Context.NOTIFICATION_SERVICE);

        PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
                new Intent(this, HomeActivity.class), 0);

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.ic_stat_name)
                        .setContentTitle("habiture")
                        .setStyle(new NotificationCompat.BigTextStyle()
                                .bigText(extras.getString("user")))
                        .setContentText(extras.getString("swear"));

        mBuilder.setContentIntent(contentIntent);
        mNotificationManager.notify(getNotificationIdInPreference(), mBuilder.build());
    }

    private int getNotificationIdInPreference() {

        SharedPreferences settings = getApplicationContext().getSharedPreferences("Settings",0);

        int notificationId =settings.getInt("NotificationId", 1);

        if(notificationId>1000000)notificationId=1;

        settings.edit().putInt("NotificationId", notificationId+1).commit();

        return notificationId;
    }

    private void PlaySound(int tool) {
        Log.i(TAG, "service PlaySound");
        Intent broadcastIntent = new Intent(getApplicationContext().getString(R.string.play_tool_sound));
        broadcastIntent.putExtra("tool_id",tool);
        sendBroadcast(broadcastIntent);
    }

    private void insertBadgeNumber() {
/*
        SharedPreferences settings = getApplicationContext().getSharedPreferences("Settings",0);
        int badgeNumber =settings.getInt("BadgeNumber", 1);
        if(badgeNumber>999)return;
        settings.edit().putInt("BadgeNumber", badgeNumber+1).commit();
        */
/*
        ContentValues cv = new ContentValues();
        cv.put("package", getPackageName());
// Name of your activity declared in the manifest as android.intent.action.MAIN.
// Must be fully qualified name as shown below
        cv.put("class", "com.example.badge.activity.Test");
        cv.put("badgecount", badgeNumber); // integer count you want to display
// Execute insert
        getContentResolver().insert(Uri.parse("content://com.sec.badge/apps"), cv);
    */
    }
}