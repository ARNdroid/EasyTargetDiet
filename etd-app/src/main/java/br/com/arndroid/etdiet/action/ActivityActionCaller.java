package br.com.arndroid.etdiet.action;

import android.os.Bundle;
import android.app.Fragment;

public interface ActivityActionCaller {
    void onCallAction(int fragmentId, Class holderActivityClass,
                      String actionTag, Bundle actionData);
}