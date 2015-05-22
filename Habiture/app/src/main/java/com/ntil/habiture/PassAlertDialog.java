package com.ntil.habiture;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;

public class PassAlertDialog extends DialogFragment {

    private static final boolean DEBUG = false;
    Listener listener;


    private static void trace(String message) {
        if(DEBUG) {
            Log.d("ExceptionAlertDialog", message);
        }
    }

    public static PassAlertDialog newInstance(int passRemain) {
        PassAlertDialog passAlertDialog = new PassAlertDialog();
        Bundle args = new Bundle();
        args.putInt("passRemain", passRemain);
        passAlertDialog.setArguments(args);
        return passAlertDialog;
    }

    public interface Listener {
        public void onPass();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        listener = (Listener) activity;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        trace("onCreateDialog");

        String confirm = getString(R.string.confirm);
        String cancel = getString(R.string.cancel);
        String pass = "PASS";
        String remain = "本週剩餘 " + getArguments().getInt("passRemain") + " 次PASS";

        return new AlertDialog.Builder(getActivity())
                .setTitle(pass)
                .setMessage(remain)
                .setPositiveButton(confirm,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                listener.onPass();
                            }
                        }
                )
                .setNegativeButton(cancel,
                        new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        })
                .setCancelable(false)
                .create();
    }
}
