package com.ntil.habiture;

import android.app.Activity;
import android.app.Fragment;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;

import com.widget.CircleImageView;

/**
 * Created by Dewei on 2015/6/23.
 */
public class RecordFragment extends Fragment {

    private static final boolean DEBUG = true;
    private Listener mListener;
    private EditText editText;
    private ImageButton ibCamera;

    private void trace(String message) {
        if(DEBUG)
            Log.d("RecordFragment", message);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mListener = (Listener) activity;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        trace("onCreateView");
        View rootView = inflater.inflate(R.layout.fragment_record, container, false);

        rootView.findViewById(R.id.btCancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onCancelClicked();
            }
        });
        rootView.findViewById(R.id.btUpload).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onUploadClicked(editText.getText().toString());
            }
        });
        ibCamera = (ImageButton) rootView.findViewById(R.id.ibCamera);
        ibCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onCamera();
            }
        });
        editText = (EditText) rootView.findViewById(R.id.editText);

        return rootView;
    }

    public void setPhoto(Bitmap bitmap) {
        ibCamera.setImageBitmap(bitmap);
    }

    public interface Listener {
        void onUploadClicked(String text);
        void onCancelClicked();
        void onCamera();
    }
}
