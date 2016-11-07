package ua.com.doublekey.chat.common;

import android.app.Activity;
import android.content.Context;
import android.view.Gravity;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;
import android.widget.Toast;

import ua.com.doublekey.chat.R;

import static android.content.Context.INPUT_METHOD_SERVICE;

/**
 * Created by doublekey on 21.10.2016.
 *
 */

public class Utils {
    public static void hideKeyboard(Activity activity){
        if (activity == null) return;

        activity.getWindow()
                .setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        if(activity.getCurrentFocus() != null) {
            InputMethodManager inputMethodManager
                    = (InputMethodManager) activity.getSystemService(INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(
                    activity.getCurrentFocus().getWindowToken(), 0);
        }
    }

    /**
     * Shows the dialog with the single OK button where the text is centered
     */
    public static void showOkDialog(Context context, String string){
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(context)
                .setPositiveButton(context.getResources()
                        .getString(R.string.common_dialog_ok), null);

        TextView textView = new TextView(context);
        textView.setText(string);
        textView.setGravity(Gravity.CENTER_HORIZONTAL);
        textView.setTextSize(20);
        textView.setPadding(10, 30, 10, 30);
        builder.setView(textView);

        builder.show();
    }

}
