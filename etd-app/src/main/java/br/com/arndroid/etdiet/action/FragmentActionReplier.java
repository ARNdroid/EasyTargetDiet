package br.com.arndroid.etdiet.action;

import android.os.Bundle;

public interface FragmentActionReplier {

    public static String ACTION_TAG_KEY = FragmentActionReplier.class.getSimpleName()
            + ".ACTION_TAG_KEY";
    public static String ACTION_DATA_KEY = FragmentActionReplier.class.getSimpleName()
            + ".ACTION_DATA_KEY";

    public void onReplyActionFromOtherFragment(String actionTag, Bundle actionData);
}
