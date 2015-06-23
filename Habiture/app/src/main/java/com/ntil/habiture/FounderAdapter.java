package com.ntil.habiture;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.habiture.PokeData;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Reid on 2015/6/22.
 */
public class FounderAdapter extends PagerAdapter{
    private static final boolean DEBUG = true;
    private List<Item> items;
    private Bitmap bmpTool;
    private Context context;
    private Listener listener;
    private ImageView ivPoke;
    private Bitmap bmpDrawing;
    private int vpWidth = -1, vpHeight = -1;
    private boolean isFirstPhotoSeted = false;
    private int nFounderSize = 0;

    private void trace(String message) {
        if(DEBUG)
            Log.d("FounderAdapter", message);
    }
    LayoutInflater mInflater;

    public class Item {
        PokeData.Founder founder;
        Bitmap photo = null;
        ImageView ivPoke = null;
        public PokeData.Founder getFounder() {
            return founder;
        }
    }

    public FounderAdapter(Context context, List<PokeData.Founder> founderList) {
        this.context = context;
        listener = (Listener) context;
        mInflater = LayoutInflater.from(context);
        items = new ArrayList<>(founderList.size());
        for(PokeData.Founder founder: founderList) {
            Item item = new Item();
            item.founder = founder;
            items.add(item);
        }
    }
    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object o) {
        return view == o;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        // Just remove the view from the ViewPager
        container.removeView((View) object);
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        // Get the item for the requested position
        final Item item = items.get(position);
        trace("instantiateItem position = " + position);

        // Inflate item layout for images
        ImageView ivOwnerPhoto = (ImageView) mInflater
                .inflate(R.layout.singleitem_owner_image, container, false);

        item.ivPoke = ivOwnerPhoto;

        if(item.photo != null) {
            ivOwnerPhoto.setImageBitmap(item.photo);
            if(!isFirstPhotoSeted && position == 0) {
                nFounderSize++;
                trace("nFounderSize = "+nFounderSize);
                if(nFounderSize == items.size()) {
                    trace("item.photo != null");
                    setPokeEnabled(0);
                    isFirstPhotoSeted = true;
                    nFounderSize = 0;
                }
            }
        }


        container.addView(ivOwnerPhoto);
        return ivOwnerPhoto;
    }

    public void setPhoto(byte[] photo, int position) {
        trace("setPhoto position = "+position+", vpWidth, vpHeight = "+vpWidth +", "+vpHeight);
        Item item = (Item) items.get(position);
        item.photo = BitmapFactory.decodeByteArray(photo, 0, photo.length);
        if(vpWidth <= 0 || vpHeight <= 0) {
            //item.photo = BitmapFactory.decodeByteArray(photo, 0, photo.length);
        } else {
            item.photo = Bitmap.createScaledBitmap(item.photo, vpWidth, vpHeight, false);
        }
        notifyDataSetChanged();
    }

    private void drawSampleTool(float x, float y) {
        Paint paint = new Paint();
        Canvas drawCanvas = new Canvas(bmpDrawing);
        drawCanvas.drawBitmap(bmpTool, x, y, paint);
        ivPoke.setImageBitmap(bmpDrawing);
    }

    public void setPokeEnabled(int position) {
        trace("setPokeEnabled");
        Item item = (Item) items.get(position);
        bmpTool = BitmapFactory.decodeResource(context.getResources(), R.drawable.sample_tool).copy(Bitmap.Config.ARGB_8888, true);
        ivPoke = item.ivPoke;

        if(bmpDrawing!=null) bmpDrawing.recycle();
        bmpDrawing = item.photo.copy(Bitmap.Config.ARGB_8888, true);

        ivPoke.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                trace("onTouch event = "+ event.getAction());
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    trace("OnTouch");
                    //listener.onPoke();
                    drawSampleTool(event.getX(), event.getY());
                    return true;
                }
                return false;
            }
        });
    }

    public void setViewPagerShape(int width, int height) {
        vpWidth = width;
        vpHeight = height;
        trace("setViewPagerShape vpWidth, vpHeight = "+vpWidth +", "+vpHeight);
    }


    public void release() {
        for(Item item: items)
            if(item.photo != null)
                item.photo.recycle();

        if(bmpDrawing != null)
            bmpDrawing.recycle();
    }


    public interface Listener {
        void onPoke();
    }
}
