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
        // TODO: if context == holderActivityClass we will be in loop. Throw exception!
        Intent intent = new Intent(context, holderActivityClass);
        intent.putExtra(FragmentActionReplier.ACTION_TAG_KEY, actionTag);
        intent.putExtra(FragmentActionReplier.ACTION_DATA_KEY, actionData);
        context.startActivity(intent);
    }

    public static void callActionInFragmentByMethod(FragmentManager fragmentManager,
                                                     int fragmentId, String actionTag,
                                                     Bundle actionData) {
        // TODO: we may finish with a NPE here. Check and throw a better exception.
        ((FragmentActionReplier) getFragmentInLayout(fragmentManager, fragmentId))
                .onReplyActionFromOtherFragment(actionTag, actionData);
    }

    public static boolean isFragmentInLayout(FragmentManager fragmentManager,
                                             int fragmentId) {
        return getFragmentInLayout(fragmentManager, fragmentId) != null;
    }

    public static Fragment getFragmentInLayout(FragmentManager fragmentManager, int fragmentId) {
        return fragmentManager.findFragmentById(fragmentId);
    }
}
