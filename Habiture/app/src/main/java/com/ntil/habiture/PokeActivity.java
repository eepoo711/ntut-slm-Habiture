package com.ntil.habiture;

import android.app.Activity;
import android.app.DialogFragment;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ShareActionProvider;
import android.widget.TextView;
import android.widget.Toast;

import com.habiture.GroupHistory;
import com.habiture.Habiture;
import com.habiture.HabitureModule;
import com.habiture.PokeData;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import utils.exception.ExceptionAlertDialog;

/**
 * Created by GawinHsu on 5/7/15.
 */
public class PokeActivity extends Activity implements PokeFragment.Listener{

    private final static int CAMERA_REQUEST = 66 ;
    private HabitureModule mHabitureModule;
    private Bitmap mBitmapCaputred;
    private Bitmap mBitmapPoke;
    private static final boolean DEBUG = false;
    private PokeFragment mPoketFragment;
    private static Random random_tool=null;
    private static PokeData pokeData;
    private static boolean isFounder = false;
    private static int pid = -1;

    //TODO: ViewPager
    // The items to be displayed in the ViewPager
    private final ArrayList<ContentItem> mItems = getSampleContent();

    // Keep reference to the ShareActionProvider from the menu
    private ShareActionProvider mShareActionProvider;

    private void trace(String message) {
        if(DEBUG)
            Log.d("PokeActivity", message);
    }

    public static void startActivity(Context context, PokeData pokeData, boolean isFounder, int pid) {
        Intent intent = new Intent(context, PokeActivity.class);
        PokeActivity.pokeData = pokeData;
        PokeActivity.isFounder = isFounder;
        PokeActivity.pid = pid;
        random_tool = new Random();
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_poke);
        String name = MainApplication.getInstance().getHabitureModel().getAccount();
        mHabitureModule = MainApplication.getInstance().getHabitureModel();

        //TODO: ViewPager
        // Retrieve the ViewPager from the content view
        ViewPager vp = (ViewPager) findViewById(R.id.viewpager);

        // Set an OnPageChangeListener so we are notified when a new item is selected
        vp.setOnPageChangeListener(mOnPageChangeListener);

        // Finally set the adapter so the ViewPager can display items
        vp.setAdapter(mPagerAdapter);

        if (savedInstanceState == null) {
            mPoketFragment = PokeFragment.newInstance(pokeData, isFounder);
            getFragmentManager().beginTransaction()
                    .add(R.id.profileContainer, HomeTopFragment.newInstance(
                            mHabitureModule.getName()
                            , mHabitureModule.getHeader()))
                    .add(R.id.pokeContainer, mPoketFragment)
                    .commit();

            if(pokeData.getFounderList().get(0).getUrl().length()>0)
                new QueryOwnerPhoto().execute(pokeData.getFounderList().get(0).getUrl());
        }
        registerToolBroadReceiver();
    }



    @Override
    protected void onDestroy() {
        super.onDestroy();
        mHabitureModule.stopSendSoundTimer();
        unregisterReceiver(toolBroadReceiver);
    }

    private BroadcastReceiver toolBroadReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            int to_id =pokeData.getFounderList().get(0).getUid();
            int tool_id =intent.getIntExtra("tool_id",1);
            trace("registerToolBroadReceiver(), to_id="+to_id+" pid="+pid+" tool_id="+tool_id);
            new SendToolTask().execute(to_id,pid,tool_id);
        }
    };

    private void registerToolBroadReceiver() {
        registerReceiver(toolBroadReceiver,new IntentFilter(this.getString(R.string.tool_clicck_intent_name)));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // call this function after capture
        trace("onActivityResult, requestCode = " + requestCode + ", resultCode = " + resultCode);

        if (requestCode == CAMERA_REQUEST && resultCode == RESULT_OK) {
            mBitmapCaputred = (Bitmap) data.getExtras().get("data");
            new UploadProofTask().execute(pid);
        }
    }

    @Override
    public void onClickCamera() {
        trace("onClickCamera");
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(cameraIntent, CAMERA_REQUEST);
    }

    @Override
    public void onClickRecords() {
        trace("onClickRecords");
        new GroupHistoryTask().execute(pid);
    }

    @Override
    public void onClickGroupFriend() {
        trace("onClickGroupFriend");
        DialogFragment newFragment = new NoImplementDialog();
        newFragment.show(getFragmentManager(), "dialog");
    }

    @Override
    public void onClickTool() {
        trace("onClickTool");
        DialogFragment newFragment = new NoImplementDialog();
        newFragment.show(getFragmentManager(), "dialog");
    }

    @Override
    public void onClickFollow() {
        trace("onClickFollow");
        new FollowTask().execute();
    }

    @Override
    public void onClickAlarm() {
        trace("onClickAlarm");
        DialogFragment newFragment = new NoImplementDialog();
        newFragment.show(getFragmentManager(), "dialog");
    }

    @Override
    public void onPoke() {
        int random_tool_id =random_tool.nextInt(6)+1;
        System.out.println("onPoke="+random_tool_id);
        // TODO: to guest now
        Intent broadcastIntent = new Intent(this.getString(R.string.tool_clicck_intent_name));
        broadcastIntent.putExtra("to_id",pokeData.getFounderList().get(0).getUid());
        broadcastIntent.putExtra("pid",pid);
        broadcastIntent.putExtra("tool_id", random_tool_id);
        this.sendBroadcast(broadcastIntent);

        // Client's business
        Intent broadcastIntent_client_playsound = new Intent(getApplicationContext().getString(R.string.play_tool_sound));
        broadcastIntent_client_playsound.putExtra("tool_id",random_tool_id);
        sendBroadcast(broadcastIntent_client_playsound);
    }

    private class FollowTask extends AsyncTask<Void, Void, Boolean> {
        private ProgressDialog progress;

        @Override
        protected void onPreExecute() {
            trace("PassTask onPreExecute");

            try {
                progress = ProgressDialog.show(PokeActivity.this,
                        PokeActivity.this.getString(R.string.progress_title),
                        "載入中...");
            } catch(Throwable e) {
                ExceptionAlertDialog.showException(getFragmentManager(), e);
            }
        }


        @Override
        protected Boolean doInBackground(Void... params) {
            trace("PassTask doInBackground");

            return mHabitureModule.followHabit(pid);
        }
        @Override
        protected void onPostExecute(Boolean isFollow) {
            trace("PassTask onPostExecute");
            progress.dismiss();
            if (isFollow)
                Toast.makeText(PokeActivity.this, "follow success", Toast.LENGTH_SHORT).show();
            else
                Toast.makeText(PokeActivity.this, "follow failed", Toast.LENGTH_SHORT).show();
        }



    }

    private class GroupHistoryTask extends AsyncTask<Integer, Void, List<GroupHistory>> {
        private ProgressDialog progress;
        @Override
        protected void onPreExecute() {
            trace("GroupHistoryTask onPreExecute");
            try {
                progress = ProgressDialog.show(PokeActivity.this,
                        "習慣成真",
                        "載入中...");
            } catch(Throwable e) {
                ExceptionAlertDialog.showException(getFragmentManager(), e);
            }
        }

        @Override
        protected List<GroupHistory> doInBackground(Integer... params) {
            trace("GroupHistoryTask doInBackground");
            List<GroupHistory> ret = null;
            try {
                int ownerId = params[0];
                ret = mHabitureModule.gueryGroupHistory(ownerId);
            } catch (Throwable e) {
                ExceptionAlertDialog.showException(getFragmentManager(), e);
            }
            return ret;
        }

        @Override
        protected void onPostExecute(List<GroupHistory> groupHistories) {
            trace("GroupHistoryTask onPostExecute");
            progress.dismiss();
            if (groupHistories != null) {
                HistoryActivity.startActivity(PokeActivity.this, groupHistories);
            } else {
                Toast.makeText(PokeActivity.this, "載入資料失敗", Toast.LENGTH_SHORT).show();
            }
        }
    }


    private class UploadProofTask extends AsyncTask<Integer, Void, Boolean> {
        private ProgressDialog progress;
        @Override
        protected void onPreExecute() {
            trace("UploadProofTask onPreExecute");
            try {
                progress = ProgressDialog.show(PokeActivity.this,
                        "習慣成真",
                        "上傳中...");
            } catch(Throwable e) {
                ExceptionAlertDialog.showException(getFragmentManager(), e);
            }

        }

        @Override
        protected Boolean doInBackground(Integer... params) {
            trace("UploadProofTask doInBackground");
            boolean is_upload = false;
            try {
                int ownerId = params[0];

                is_upload = mHabitureModule.uploadProofImage(ownerId, mBitmapCaputred);
            } catch (Throwable e) {
                ExceptionAlertDialog.showException(getFragmentManager(), e);
            }
            return is_upload;
        }

        @Override
        protected void onPostExecute(Boolean success) {
            trace("UploadProofTask onPostExecute");
            progress.dismiss();
            Toast.makeText(
                    PokeActivity.this,
                    success ? "上傳成功" : "上傳失敗",
                    Toast.LENGTH_SHORT).show();
        }
    }

    private class SendToolTask extends AsyncTask<Integer, Void, Boolean> {
        private ProgressDialog progress;

        @Override
        protected void onPreExecute() {
            trace("onPreExecute");
        }

        @Override
        protected Boolean doInBackground(Integer... params) {
            trace("doInBackground");

            boolean is_tool_sent = false;
            try {
                int to_id = params[0];
                int from_id = params[1];
                int tool_id = params[2];
                is_tool_sent =mHabitureModule.sendSoundToPartner(to_id,from_id,tool_id);
            } catch (Throwable e) {
                ExceptionAlertDialog.showException(getFragmentManager(), e);
            }

            return is_tool_sent;
        }

        @Override
        protected void onPostExecute(Boolean success) {
            trace("onPostExecute");

            try {
            } catch(Throwable e) {
                ExceptionAlertDialog.showException(getFragmentManager(), e);
            }

        }
    }

    private class QueryOwnerPhoto extends AsyncTask<String, Void, Bitmap> {
        private String url;

        @Override
        protected void onPreExecute() {
            trace("QueryOwnerPhoto onPreExecute");
        }

        @Override
        protected Bitmap doInBackground(String... params) {
            trace("QueryOwnerPhoto doInBackground");
            url = params[0];
            Bitmap bitmap = null;

            try {
                bitmap = mHabitureModule.queryBitmapUrl(url);
            } catch (Throwable e) {
                ExceptionAlertDialog.showException(getFragmentManager(), e);
            }
            return bitmap;

        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            trace("QueryOwnerPhoto onPostExecute");
            try {
                if (bitmap != null && !PokeActivity.this.isFinishing()) {
                    mBitmapPoke = bitmap;
                    mPoketFragment.setImage(mBitmapPoke);
                } else {
                    Toast.makeText(PokeActivity.this, "載入資料失敗", Toast.LENGTH_SHORT).show();
                }

            } catch (Throwable e) {
                ExceptionAlertDialog.showException(getFragmentManager(), e);
            }
        }



    }

    //TODO: ViewPager
    /**
     * A PagerAdapter which instantiates views based on the ContentItem's content type.
     */
    private final PagerAdapter mPagerAdapter = new PagerAdapter() {
        LayoutInflater mInflater;

        @Override
        public int getCount() {
            return mItems.size();
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
        public Object instantiateItem(ViewGroup container, int position) {
            // Ensure that the LayoutInflater is instantiated
            if (mInflater == null) {
                mInflater = LayoutInflater.from(PokeActivity.this);
            }

            // Get the item for the requested position
            final ContentItem item = mItems.get(position);

            // The view we need to inflate changes based on the type of content
            switch (item.contentType) {
                case ContentItem.CONTENT_TYPE_IMAGE: {
                    // Inflate item layout for images
                    ImageView iv = (ImageView) mInflater
                            .inflate(R.layout.singleitem_owner_image, container, false);

                    // Load the image from it's content URI
                    iv.setImageURI(item.getContentUri());

                    // Add the view to the ViewPager
                    container.addView(iv);
                    return iv;
                }
            }

            return null;
        }
    };

    private void setShareIntent(int position) {
        // BEGIN_INCLUDE(update_sap)
        if (mShareActionProvider != null) {
            // Get the currently selected item, and retrieve it's share intent
            ContentItem item = mItems.get(position);
            Intent shareIntent = item.getShareIntent(PokeActivity.this);

            // Now update the ShareActionProvider with the new share intent
            mShareActionProvider.setShareIntent(shareIntent);
        }
        // END_INCLUDE(update_sap)
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
        }

        @Override
        public void onPageSelected(int position) {
            setShareIntent(position);
        }

        @Override
        public void onPageScrollStateChanged(int state) {
            // NO-OP
        }
    };

    /**
     * @return An ArrayList of ContentItem's to be displayed in this sample
     */
    static ArrayList<ContentItem> getSampleContent() {
        ArrayList<ContentItem> items = new ArrayList<ContentItem>();

        items.add(new ContentItem(ContentItem.CONTENT_TYPE_IMAGE, pokeData.getFounderList().get(0).getUrl()));
//        items.add(new ContentItem(ContentItem.CONTENT_TYPE_TEXT, R.string.quote_1));
//        items.add(new ContentItem(ContentItem.CONTENT_TYPE_TEXT, R.string.quote_2));
//        items.add(new ContentItem(ContentItem.CONTENT_TYPE_IMAGE, "photo_2.jpg"));
//        items.add(new ContentItem(ContentItem.CONTENT_TYPE_TEXT, R.string.quote_3));
//        items.add(new ContentItem(ContentItem.CONTENT_TYPE_IMAGE, "photo_3.jpg"));

        return items;
    }
}
