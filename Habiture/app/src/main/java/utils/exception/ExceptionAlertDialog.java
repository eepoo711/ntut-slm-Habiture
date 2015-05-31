package utils.exception;


import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;

public class ExceptionAlertDialog extends DialogFragment {

    private static final String ARGS_TITLE = "title";
    private static final String ARGS_MESSAGE = "message";

    private static final boolean DEBUG = false;


    private static void trace(String message) {
        if(DEBUG) {
            Log.d("ExceptionAlertDialog", message);
        }
    }

    private static ExceptionAlertDialog newInstance(String title, String message) {

        trace("newInstance");

        ExceptionAlertDialog dialog = new ExceptionAlertDialog();

        Bundle bundle = new Bundle();
        bundle.putString(ARGS_TITLE, title);
        bundle.putString(ARGS_MESSAGE, message);

        dialog.setArguments(bundle);

        return dialog;
    }

    private static void showDialog(FragmentManager fragmentManager, String title, String message) {
        trace("showDialog");

        DialogFragment newFragment = newInstance(title, message);
        newFragment.show(fragmentManager, "dialog");
    }

    private void terminateApplication() {
        trace("terminateApplication");

        System.exit(0);
    }

    private static String stackTraceArrayToString(Throwable e) {
        String message;

        StringBuffer messageBuffer = new StringBuffer();

        if(e.getMessage() != null) {
            messageBuffer.append(e.getMessage());
            messageBuffer.append("\r\n");
        }

        if(e.getStackTrace() != null)
            for(StackTraceElement stackTraceElemnet: e.getStackTrace()) {
                messageBuffer.append(stackTraceElemnet);
                messageBuffer.append("\r\n");
            }

        message = messageBuffer.toString();
        return message;
    }

    public static void showException(FragmentManager fragmentManager, Throwable e) {
        trace("showException");
        e.printStackTrace();

        String message = stackTraceArrayToString(e);
        showDialog(fragmentManager, "Catch An Exception", message);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        trace("onCreateDialog");

        Bundle args = getArguments();
        final String title = args.getString(ARGS_TITLE);
        final String message = args.getString(ARGS_MESSAGE);

        return new AlertDialog.Builder(getActivity())
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton("Terminate Application.",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                terminateApplication();
                            }
                        }
                )
                .setCancelable(false)
                .create();
    }
}
