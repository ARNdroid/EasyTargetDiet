package br.com.arndroid.etdiet.action;

import android.os.Bundle;

public interface FragmentReplier {

    public static String ACTION_TAG_KEY = FragmentReplier.class.getSimpleName()
            + ".ACTION_TAG_KEY";
    public static String ACTION_DATA_KEY = FragmentReplier.class.getSimpleName()
            + ".ACTION_DATA_KEY";

    public void onReplyAction(String actionTag, Bundle actionData);
}