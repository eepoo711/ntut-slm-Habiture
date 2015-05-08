package com.ntil.habiture;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;

public class ExitAlertDialog extends DialogFragment {

    private static final boolean DEBUG = false;
    Listener listener;


    private static void trace(String message) {
        if(DEBUG) {
            Log.d("ExceptionAlertDialog", message);
        }
    }

    public interface Listener {
        public void onExit();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        listener = (Listener) activity;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        trace("onCreateDialog");

        String sureToLeave = getString(R.string.sure_to_leave);
        String exitApplication = getString(R.string.exit_application);
        String confirm = getString(R.string.confirm);
        String cancel = getString(R.string.cancel);

        return new AlertDialog.Builder(getActivity())
                .setTitle(exitApplication)
                .setMessage(sureToLeave)
                .setPositiveButton(confirm,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                listener.onExit();
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
