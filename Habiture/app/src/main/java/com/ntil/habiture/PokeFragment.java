package com.ntil.habiture;

import android.app.Activity;
import android.app.Fragment;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.format.Time;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.widget.CircleImageView;

import java.util.concurrent.TimeoutException;

/**
 * Created by GawinHsu on 5/7/15.
 */
public class PokeFragment extends Fragment {

    private static final boolean DEBUG = false;

    public Listener listener;
    
    private ImageView ivPoke;
    private ImageView ivAlert;
    private TextView tvSwear;
    private TextView tvPunishment;
    private TextView tvTime;
    private TextView tvRemain;
    private TextView tvGoal;
    private TextView tvFrequency;
    private ImageButton btnCamera;
    private Bitmap bmpDrawing;
    private Bitmap bmpTool;
    private Bitmap bmpOwnerPhoto = null;

    private Paint mPaint = new Paint();

    private void trace(String message) {
        if(DEBUG)
            Log.d("PokeFragment", message);
    }

    public static PokeFragment newInstance(boolean isFounder, String swear, String punishment
            , int frequency, int doItTime, int goal, int remain,int notice_enable) {
        PokeFragment fragment = new PokeFragment();
        //bmpOwnerPhoto = bitmapOwner;
        Bundle args = new Bundle();
        args.putBoolean("isFounder", isFounder);
        args.putString("swear", swear);
        args.putString("punishment", punishment);
        args.putInt("frequency", frequency);
        args.putInt("doItTime", doItTime);
        args.putInt("goal", goal);
        args.putInt("remain", remain);
        args.putInt("notice_enable", notice_enable);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setRetainInstance(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        trace("onCreateView");
        return inflater.inflate(R.layout.fragment_poke, container, false);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        listener = (Listener) activity;
    }



    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        trace("onActivityCreated");

        btnCamera = (ImageButton) getActivity().findViewById(R.id.btnCamera);

        btnCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                trace("onClick ibCamera");
                listener.onClickCamera();
            }
        });

        getActivity().findViewById(R.id.btnRecords).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                trace("onClick btnRecords");
                listener.onClickRecords();
            }
        });

        getActivity().findViewById(R.id.btnFriends).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                trace("onClick btnFriends");
                listener.onClickGroupFriend();
            }
        });

        getActivity().findViewById(R.id.btnTool).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                trace("onClick btnTool");
                listener.onClickTool();
            }
        });

        ivPoke = (CircleImageView) getActivity().findViewById(R.id.ivPoke);
        ivAlert = (ImageView) getActivity().findViewById(R.id.ivAlert);
        tvSwear = (TextView) getActivity().findViewById(R.id.tvSwear);
        tvPunishment = (TextView) getActivity().findViewById(R.id.tvPunishment);
        tvTime = (TextView) getActivity().findViewById(R.id.tvTime);
        tvRemain = (TextView) getActivity().findViewById(R.id.tvRemain);
        tvGoal = (TextView) getActivity().findViewById(R.id.tvGoal);
        tvFrequency = (TextView) getActivity().findViewById(R.id.tvFrequency);

        // fix 24 clock to 12
        int doItTime =  getArguments().getInt("doItTime");
        String ampm = doItTime >= 12 ? "PM " : "AM ";
        int ampmDoItTime = doItTime > 12 ? getArguments().getInt("doItTime") - 12
                :getArguments().getInt("doItTime");
        if (ampmDoItTime == 0)
            ampmDoItTime = 12;

        Time t=new Time(); // or Time t=new Time("GMT+8"); 加上Time Zone資料。
        t.setToNow(); // 取得系統時間。
        int alertId = t.hour >= doItTime && getArguments().getInt("notice_enable")==1 ? R.mipmap.notice_enable : R.mipmap.notice_disable ;
        ivAlert.setImageResource(alertId);

        tvSwear.setText(getArguments().getString("swear"));
        tvRemain.setText("剩下 " + getArguments().getInt("remain") + " 週");
        tvPunishment.setText(getArguments().getString("punishment"));
        tvTime.setText(ampm + ampmDoItTime + ":00");
        tvFrequency.setText("每週 " + getArguments().getInt("frequency") + " 次");
        tvGoal.setText("持續 " + getArguments().getInt("goal") + " 週");

        if (!getArguments().getBoolean("isFounder")) {
            btnCamera.setVisibility(View.INVISIBLE);
        }


        if(bmpDrawing != null)
            setPokeEnabled();


    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        trace("onDestroy");


        if(bmpOwnerPhoto != null) bmpOwnerPhoto.recycle();
        if(bmpTool != null) bmpTool.recycle();
        if(bmpDrawing != null) bmpDrawing.recycle();
    }

    private void drawSampleTool(float x, float y) {
        Canvas drawCanvas = new Canvas(bmpDrawing);
        drawCanvas.drawBitmap(bmpTool, x, y, mPaint);
        ivPoke.setImageBitmap(bmpDrawing);
    }

    public void setImage(Bitmap image) {
        trace("setImage");
        bmpOwnerPhoto = image;
        //bmpDrawing = image;
        bmpDrawing = Bitmap.createScaledBitmap(bmpOwnerPhoto, ivPoke.getWidth(), ivPoke.getHeight(), false);
//        bmpDrawing = Bitmap.createScaledBitmap(bmpOwnerPhoto, 300, 300, false);
        bmpTool = BitmapFactory.decodeResource(getResources(), R.drawable.sample_tool).copy(Bitmap.Config.ARGB_8888, true);
        setPokeEnabled();
    }

    private void setPokeEnabled() {
        ivPoke.setImageBitmap(bmpDrawing);
        ivPoke.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    trace("OnTouch");
                    listener.onPoke();
                    drawSampleTool(event.getX(), event.getY());
                    return true;
                }
                return false;
            }
        });
    }

    public interface Listener {
        void onClickCamera();
        void onClickRecords();
        void onClickGroupFriend();
        void onClickTool();
        void onPoke();
    }
}
