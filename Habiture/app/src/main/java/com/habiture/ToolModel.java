package com.habiture;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioManager;
import android.media.SoundPool;
import android.util.Log;

import com.gcm.client.receiver.GcmModel;
import com.ntil.habiture.R;

import java.util.List;


public class ToolModel {

    private final boolean DEBUG = false;
    private Context mContext =null;

    static final String TAG = "ToolModel";

    SoundPool soundPool1 =null;
    SoundPool soundPool2 =null;
    SoundPool soundPool3 =null;
    SoundPool soundPool4 =null;
    SoundPool soundPool5 =null;
    SoundPool soundPool6 =null;

    public ToolModel(Context context) {
        trace("ToolModel");
        mContext =context;
        initToolModel();
    }

    private void initToolModel() {
        initSoundPool();
        registerReceiveToolBroadcast();
    }

    private void initSoundPool() {

        int srcQuality =0;// no effect , default for 0
        int maxStreams =10;
        int priority=1;//the priority of the sound. Currently has no effect. Use a value of 1 for future compatibility.

        Context context =mContext;

        if(soundPool1==null){
            Log.i(TAG, "reload sundpool1");
            soundPool1= new SoundPool(maxStreams, AudioManager.STREAM_NOTIFICATION,srcQuality);
            soundPool1.load(context, R.raw.sound1_youcanmakeit, priority);}
        if(soundPool2==null){
            Log.i(TAG, "reload sundpool2");
            soundPool2= new SoundPool(maxStreams, AudioManager.STREAM_NOTIFICATION,srcQuality);
            soundPool2.load(context, R.raw.sound2_hardwork, priority);}
        if(soundPool3==null){
            Log.i(TAG, "reload sundpool3");
            soundPool3= new SoundPool(maxStreams, AudioManager.STREAM_NOTIFICATION,srcQuality);
            soundPool3.load(context, R.raw.sound3_goodemotion, priority);}
        if(soundPool4==null){
            Log.i(TAG, "reload sundpool4");
            soundPool4= new SoundPool(maxStreams, AudioManager.STREAM_NOTIFICATION,srcQuality);
            soundPool4.load(context, R.raw.sound4_cheerup, priority);}
        if(soundPool5==null){
            Log.i(TAG, "reload sundpool5");
            soundPool5= new SoundPool(maxStreams, AudioManager.STREAM_NOTIFICATION,srcQuality);
            soundPool5.load(context, R.raw.sound5_hurryup, priority);}
        if(soundPool6==null){
            Log.i(TAG, "reload sundpool6");
            soundPool6= new SoundPool(maxStreams, AudioManager.STREAM_NOTIFICATION,srcQuality);
            soundPool6.load(context, R.raw.sound6_hello, priority);}

    }

    private void registerReceiveToolBroadcast() {
        BroadcastReceiver toolBroadReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Log.i(TAG, "receiver broadcast");
                int tool_id =intent.getIntExtra("tool_id",1);
                PlaySound(tool_id);
            }
        };
        mContext.registerReceiver(toolBroadReceiver,new IntentFilter(mContext.getString(R.string.tool_clicck_intent_name)));
        mContext.registerReceiver(toolBroadReceiver,new IntentFilter(mContext.getString(R.string.play_tool_sound)));
    }

    private void PlaySound(int tool) {
        Log.i(TAG, "PlaySound tool="+tool);
        switch(tool)
        {
            case 1:
                if(soundPool1!=null){
                    soundPool1.play(1,1, 1, 0, 0, 1);}
                break;
            case 2:
                if(soundPool2!=null){
                    soundPool2.play(1,1, 1, 0, 0, 1);}
                break;
            case 3:
                if(soundPool3!=null){
                    soundPool3.play(1,1, 1, 0, 0, 1);}
                break;
            case 4:
                if(soundPool4!=null){
                    soundPool4.play(1,1, 1, 0, 0, 1);}
                break;
            case 5:
                if(soundPool5!=null){
                    soundPool5.play(1,1, 1, 0, 0, 1);}
                break;
            default:
                if(soundPool6!=null){
                    soundPool6.play(1,1, 1, 0, 0, 1);}
                break;
        }
    }


    private void trace(String log) {
        if(DEBUG)
            Log.d("HabitureModule", log);
    }

}
