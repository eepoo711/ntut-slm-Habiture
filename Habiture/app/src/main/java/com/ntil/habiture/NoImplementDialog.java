package com.ntil.habiture;


import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;

public class NoImplementDialog extends DialogFragment {

    private static final boolean DEBUG = false;


    private static void trace(String message) {
        if(DEBUG) {
            Log.d("ExceptionAlertDialog", message);
        }
    }


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        trace("onCreateDialog");


        String declare = "尚未實作";
        String pleaseInput = "Coming soon";
        String confirm = "確定";

        return new AlertDialog.Builder(getActivity())
                .setTitle(declare)
                .setMessage(pleaseInput)
                .setPositiveButton(confirm,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                            }
                        }
                )
                .setCancelable(false)
                .create();
    }
}
