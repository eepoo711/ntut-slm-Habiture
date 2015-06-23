package com.ntil.habiture;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.format.Time;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ShareActionProvider;
import android.widget.TextView;

import com.habiture.PokeData;
import com.widget.CircleImageView;

import java.util.ArrayList;
import java.util.concurrent.TimeoutException;

import utils.exception.ExceptionAlertDialog;

public class PokeFragment extends Fragment {

    private static final boolean DEBUG = true;

    public Listener listener;
    
    private ImageView ivPoke;
    private ImageView ivAlert;
    private ImageView ivOwnerPhoto;
    private TextView tvSwear;
    private TextView tvPunishment;
    private TextView tvTime;
    private TextView tvRemain;
    private TextView tvGoal;
    private TextView tvFrequency;
    private ImageButton btnCamera;
    private ImageButton btnFollow;
    private ImageButton btnAlarm;
    private ViewPager vp;

    private static PokeData pokeData;
    private static boolean isFounder = false;
    private static FounderAdapter mPagerAdapter = null;

    private Paint mPaint = new Paint();

    private void trace(String message) {
        if(DEBUG)
            Log.d("PokeFragment", message);
    }

    public static PokeFragment newInstance(PokeData pokeData, boolean isFounder, FounderAdapter adapter) {
        PokeFragment fragment = new PokeFragment();
        PokeFragment.pokeData = pokeData;
        PokeFragment.isFounder = isFounder;
        PokeFragment.mPagerAdapter = adapter;
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

        View rootView = inflater.inflate(R.layout.fragment_poke, container, false);
        //TODO: ViewPager
        // Retrieve the ViewPager from the content view
        vp = (ViewPager) rootView.findViewById(R.id.viewpager);

        // Set an OnPageChangeListener so we are notified when a new item is selected
        vp.setOnPageChangeListener(mOnPageChangeListener);

        // Finally set the adapter so the ViewPager can display items
        vp.setAdapter(mPagerAdapter);

        ViewTreeObserver vto = vp.getViewTreeObserver();
        vto.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            public boolean onPreDraw() {
                int finalHeight, finalWidth;
                vp.getViewTreeObserver().removeOnPreDrawListener(this);
                finalHeight = vp.getMeasuredHeight();
                finalWidth = vp.getMeasuredWidth();
                trace("ViewTreeObserver: Height: " + finalHeight + " Width: " + finalWidth);
                mPagerAdapter.setViewPagerShape(finalWidth, finalHeight);
                return true;
            }
        });

        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    /**
     * A OnPageChangeListener used to update the ShareActionProvider's share intent when a new item
     * is selected in the ViewPager.
     */
    private final ViewPager.OnPageChangeListener mOnPageChangeListener
            = new ViewPager.OnPageChangeListener() {

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            // NO-OP
            trace("onPageScrolled = "+position);
        }

        @Override
        public void onPageSelected(int position) {
            trace("onPageSelected = "+position);
            mPagerAdapter.setPokeEnabled(position);
            tvRemain.setText("剩下 " + pokeData.getFounderList().get(position).getRemain() + " 週");
        }

        @Override
        public void onPageScrollStateChanged(int state) {
            // NO-OP
        }
    };

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
        btnFollow = (ImageButton) getActivity().findViewById(R.id.btnFollow);
        btnAlarm = (ImageButton) getActivity().findViewById(R.id.btnAlarm);

        btnCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                trace("onClick ibCamera");
                listener.onClickCamera();
            }
        });

        btnFollow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                trace("onClick btnFollow");
                listener.onClickFollow();
            }
        });

        btnAlarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                trace("onClick btnAlarm");
                listener.onClickAlarm();
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

        if (isFounder) {
            btnFollow.setVisibility(View.GONE);
        } else {
            btnCamera.setVisibility(View.GONE);
            btnAlarm.setVisibility(View.GONE);
        }

//        if (bmpDrawing != null) {
//            ivOwnerPhoto.setImageBitmap(bmpDrawing);
//            //setPokeEnabled();
//        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        trace("onDestroy");

//        if(bmpOwnerPhoto != null) bmpOwnerPhoto.recycle();
//        if(bmpTool != null) bmpTool.recycle();
//        if(bmpDrawing != null) bmpDrawing.recycle();
    }


//    public void setImage(Bitmap image) {
//        trace("setImage");
//        bmpOwnerPhoto = image;
//        trace("setImage = " + ivOwnerPhoto.hashCode());
//
//        if (ivOwnerPhoto.getHeight() <= 0 || ivOwnerPhoto.getWidth() <= 0) {
//            ViewTreeObserver vto = ivOwnerPhoto.getViewTreeObserver();
//            vto.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
//                public boolean onPreDraw() {
//                    int finalHeight, finalWidth;
//                    ivOwnerPhoto.getViewTreeObserver().removeOnPreDrawListener(this);
//                    finalHeight = ivOwnerPhoto.getMeasuredHeight();
//                    finalWidth = ivOwnerPhoto.getMeasuredWidth();
//                    trace("ViewTreeObserver: Height: " + finalHeight + " Width: " + finalWidth);
//                    bmpDrawing = Bitmap.createScaledBitmap(bmpOwnerPhoto, finalWidth, finalHeight, false);
//                    ivOwnerPhoto.setImageBitmap(bmpDrawing);
//                    //setPokeEnabled();
//                    return true;
//                }
//            });
//        } else {
//            trace("setImage: Height: " + ivOwnerPhoto.getWidth() + " Width: " + ivOwnerPhoto.getHeight());
//            bmpDrawing = Bitmap.createScaledBitmap(bmpOwnerPhoto, ivOwnerPhoto.getWidth(), ivOwnerPhoto.getHeight(), false);
//            ivOwnerPhoto.setImageBitmap(bmpDrawing);
//            //setPokeEnabled();
//        }
//    }

    public interface Listener {
        void onClickCamera();
        void onClickRecords();
        void onClickGroupFriend();
        void onClickTool();
        void onClickFollow();
        void onClickAlarm();
    }
}
