package br.com.arndroid.etdiet.action;

import android.os.Bundle;

public interface FragmentActionReplier {

    String ACTION_TAG_KEY = FragmentActionReplier.class.getSimpleName()
            + ".ACTION_TAG_KEY";
    String ACTION_DATA_KEY = FragmentActionReplier.class.getSimpleName()
            + ".ACTION_DATA_KEY";

    void onReplyActionFromOtherFragment(String actionTag, Bundle actionData);
}
