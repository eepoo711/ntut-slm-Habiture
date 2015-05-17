package com.ntil.habiture;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import utils.BitmapHelper;

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
    private Bitmap mBitmapDrawing;
    private Bitmap mBitmapTool;
    private Paint mPaint = new Paint();

    private Bitmap mBitmapOwner = null;

    private void trace(String message) {
        if(DEBUG)
            Log.d("PokeFragment", message);
    }

    public static PokeFragment newInstance(String swear, String punishment
            , int frequency, int doItTime, int goal, int remain) {
        PokeFragment fragment = new PokeFragment();
        //mBitmapOwner = bitmapOwner;
        Bundle args = new Bundle();
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

        getActivity().findViewById(R.id.btnCamera).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                trace("onClick btnCamera");
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

    }

    private void drawSampleTool(float x, float y) {
        Canvas drawCanvas = new Canvas(mBitmapDrawing);
        drawCanvas.drawBitmap(mBitmapTool, x, y, mPaint);
        ivPoke.setImageBitmap(mBitmapDrawing);
    }

    public void setImage(Bitmap image) {
        trace("setImage");
        mBitmapOwner = image;
        mBitmapDrawing = Bitmap.createScaledBitmap(mBitmapOwner, ivPoke.getWidth(), ivPoke.getHeight(), false);
        mBitmapTool = BitmapFactory.decodeResource(getResources(), R.drawable.sample_tool).copy(Bitmap.Config.ARGB_8888, true);
        ivPoke.setImageBitmap(mBitmapDrawing);
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
