package com.ntil.habiture;

import android.app.Activity;
import android.app.Fragment;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.format.Time;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.habiture.PokeData;
import com.widget.CircleImageView;

import java.util.concurrent.TimeoutException;

import utils.exception.ExceptionAlertDialog;

/**
 * Created by GawinHsu on 5/7/15.
 */
public class PokeFragment extends Fragment {

    private static final boolean DEBUG = true;

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
    private static PokeData pokeData;
    private static boolean isFounder = false;

    private Paint mPaint = new Paint();

    private void trace(String message) {
        if(DEBUG)
            Log.d("PokeFragment", message);
    }

    public static PokeFragment newInstance(PokeData pokeData, boolean isFounder) {
        PokeFragment fragment = new PokeFragment();
        PokeFragment.pokeData = pokeData;
        PokeFragment.isFounder = isFounder;
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

        ivPoke = (ImageView) getActivity().findViewById(R.id.ivPoke);
        ivAlert = (ImageView) getActivity().findViewById(R.id.ivAlert);
        tvSwear = (TextView) getActivity().findViewById(R.id.tvSwear);
        tvPunishment = (TextView) getActivity().findViewById(R.id.tvPunishment);
        tvTime = (TextView) getActivity().findViewById(R.id.tvTime);
        tvRemain = (TextView) getActivity().findViewById(R.id.tvRemain);
        tvGoal = (TextView) getActivity().findViewById(R.id.tvGoal);
        tvFrequency = (TextView) getActivity().findViewById(R.id.tvFrequency);

        // fix 24 clock to 12
        int doItTime = pokeData.getDoItime();
        String ampm = doItTime >= 12 ? "PM " : "AM ";
        int ampmDoItTime = doItTime > 12 ? doItTime - 12 : doItTime;
        if (ampmDoItTime == 0)
            ampmDoItTime = 12;

        final Time t = new Time(); // or Time t=new Time("GMT+8"); 加上Time Zone資料。
        t.setToNow(); // 取得系統時間。
        int alertId = t.hour >= doItTime && pokeData.getFounderList().get(0).getNoticeStatus() == 1 ?
                R.mipmap.notice_enable : R.mipmap.notice_disable;
        ivAlert.setImageResource(alertId);

        tvSwear.setText(pokeData.getSwear());
        tvRemain.setText("剩下 " + pokeData.getFounderList().get(0).getRemain() + " 週");
        tvPunishment.setText(pokeData.getPunishment());
        tvTime.setText(ampm + ampmDoItTime + ":00");
        tvFrequency.setText("每週 " + pokeData.getFrequency() + " 次");
        tvGoal.setText("持續 " + pokeData.getGoal() + " 週");

        if (!isFounder) {
            btnCamera.setVisibility(View.INVISIBLE);
        }

        if (bmpDrawing != null) {
            ivPoke.setImageBitmap(bmpDrawing);
            setPokeEnabled();
        }
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
        if (ivPoke.getHeight() <= 0 || ivPoke.getWidth() <= 0) {
            ViewTreeObserver vto = ivPoke.getViewTreeObserver();
            vto.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
                public boolean onPreDraw() {
                    int finalHeight, finalWidth;
                    ivPoke.getViewTreeObserver().removeOnPreDrawListener(this);
                    finalHeight = ivPoke.getMeasuredHeight();
                    finalWidth = ivPoke.getMeasuredWidth();
                    trace("ViewTreeObserver: Height: " + finalHeight + " Width: " + finalWidth);
                    bmpDrawing = Bitmap.createScaledBitmap(bmpOwnerPhoto, finalWidth, finalHeight, false);
                    ivPoke.setImageBitmap(bmpDrawing);
                    setPokeEnabled();
                    return true;
                }
            });
        } else {
            trace("setImage: Height: " + ivPoke.getWidth() + " Width: " + ivPoke.getHeight());
            bmpDrawing = Bitmap.createScaledBitmap(bmpOwnerPhoto, ivPoke.getWidth(), ivPoke.getHeight(), false);
            ivPoke.setImageBitmap(bmpDrawing);
            setPokeEnabled();
        }
    }

    private void setPokeEnabled() {
        bmpTool = BitmapFactory.decodeResource(getResources(), R.drawable.sample_tool).copy(Bitmap.Config.ARGB_8888, true);
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
