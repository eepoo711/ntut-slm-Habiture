package com.ntil.habiture;

import android.app.Activity;
import android.app.Fragment;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by GawinHsu on 5/7/15.
 */
public class PokeFragment extends Fragment {

    public Listener listener;
    private static final boolean DEBUG = true;
    private ImageView ivPoke;
    private TextView tvSwearAndRemain;
    private TextView tvPunishment;
    private TextView tvTime;
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
            , int frequency, int doItTime, int goal, int remain) {
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
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
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
        Bundle args = this.getArguments();
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

        ivPoke = (ImageView) getActivity().findViewById(R.id.ivPoke);
        tvSwearAndRemain = (TextView) getActivity().findViewById(R.id.tvSwearAndRemain);
        tvPunishment = (TextView) getActivity().findViewById(R.id.tvPunishment);
        tvTime = (TextView) getActivity().findViewById(R.id.tvTime);

        tvSwearAndRemain.setText(getArguments().getString("swear") + " / 剩餘 " +
                getArguments().getInt("remain") + " 週");
        tvPunishment.setText(getArguments().getString("punishment"));
        tvTime.setText("每週 " + getArguments().getInt("doItTime") + " 次 / 持續 " +
                getArguments().getInt("frequency") + " 週");

        if (!getArguments().getBoolean("isFounder")) {
            btnCamera.setVisibility(View.INVISIBLE);
        }

    }

    private void drawSampleTool(float x, float y) {
        Canvas drawCanvas = new Canvas(bmpDrawing);
        drawCanvas.drawBitmap(bmpTool, x, y, mPaint);
        ivPoke.setImageBitmap(bmpDrawing);
    }

    public void setImage(Bitmap image) {
        trace("setImage");
        bmpOwnerPhoto = image;
        bmpDrawing = Bitmap.createScaledBitmap(bmpOwnerPhoto, ivPoke.getWidth(), ivPoke.getHeight(), false);
        bmpTool = BitmapFactory.decodeResource(getResources(), R.drawable.sample_tool).copy(Bitmap.Config.ARGB_8888, true);
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
