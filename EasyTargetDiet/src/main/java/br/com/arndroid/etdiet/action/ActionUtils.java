package br.com.arndroid.etdiet.action;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

public class ActionUtils {

    // Utility:
    protected ActionUtils() {
    }

    public static void callActionInFragment(Context context, FragmentManager fragmentManager,
                                            int fragmentId, Class holderActivityClass,
                                            String actionTag, Bundle actionData) {
        if (isFragmentInLayout(fragmentManager, fragmentId)) {
            callActionInFragmentByMethod(fragmentManager, fragmentId, actionTag, actionData);
        } else {
            callActionInFragmentByIntent(context, holderActivityClass, actionTag, actionData);
        }
    }

    public static void callActionInFragmentByIntent(Context context, Class holderActivityClass,
                                                     String actionTag, Bundle actionData) {
        if (context.getClass() == holderActivityClass) {
            throw new IllegalArgumentException("context == holderActivityClass."
                    + " We will be in loop here. Are you calling an action performed by the same activity?");
        }

        Intent intent = new Intent(context, holderActivityClass);
        intent.putExtra(FragmentActionReplier.ACTION_TAG_KEY, actionTag);
        intent.putExtra(FragmentActionReplier.ACTION_DATA_KEY, actionData);
        context.startActivity(intent);
    }

    public static void callActionInFragmentByMethod(FragmentManager fragmentManager,
                                                     int fragmentId, String actionTag,
                                                     Bundle actionData) {
        final FragmentActionReplier fragmentActionReplier = ((FragmentActionReplier)
                getFragmentInLayout(fragmentManager, fragmentId));

        if (fragmentActionReplier == null) {
            throw new IllegalArgumentException("Fragment with fragmentId=" + fragmentId +
                    " not found in layout.");
        }

        fragmentActionReplier.onReplyActionFromOtherFragment(actionTag, actionData);
    }

    public static boolean isFragmentInLayout(FragmentManager fragmentManager,
                                             int fragmentId) {
        return getFragmentInLayout(fragmentManager, fragmentId) != null;
    }

    public static Fragment getFragmentInLayout(FragmentManager fragmentManager, int fragmentId) {
        return fragmentManager.findFragmentById(fragmentId);
    }
}
