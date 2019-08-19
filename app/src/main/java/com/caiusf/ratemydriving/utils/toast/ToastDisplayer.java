package com.caiusf.ratemydriving.utils.toast;

import android.content.Context;
import android.text.Layout;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.AlignmentSpan;
import android.widget.Toast;

/**
 * Utility class for displaying a formatted toast on the device screen
 *
 * @author Caius Florea, 2017
 */
public class ToastDisplayer {

    /**
     * The toast object
     */
    private static Toast toast;

    /**
     * Display toast for a short time
     *
     * @param context
     *              the context in which the toast is displayed
     *
     * @param message
     *              the message to be displayed
     */
    public static void displayShortToast(Context context, String message) {


        Spannable centeredText = new SpannableString(message);
        centeredText.setSpan(new AlignmentSpan.Standard(Layout.Alignment.ALIGN_CENTER),
                0, message.length() - 1,
                Spannable.SPAN_INCLUSIVE_INCLUSIVE);

        if (toast != null)
            toast.cancel();
        toast = Toast.makeText(context, centeredText, Toast.LENGTH_SHORT);
        toast.show();
    }

    /**
     * Display toast for a longer time
     *
     * @param context
     *              the context in which the toast is displayed
     *
     * @param message
     *              the message to be displayed
     */
    public static void displayLongToast(Context context, String message) {


        Spannable centeredText = new SpannableString(message);
        centeredText.setSpan(new AlignmentSpan.Standard(Layout.Alignment.ALIGN_CENTER),
                0, message.length() - 1,
                Spannable.SPAN_INCLUSIVE_INCLUSIVE);

        if (toast != null)
            toast.cancel();
        toast = Toast.makeText(context, centeredText, Toast.LENGTH_LONG);
        toast.show();
    }

    /**
     * Get toast
     *
     * @return  the toast object
     */
    public static Toast getToast(){
        return toast;
    }
}