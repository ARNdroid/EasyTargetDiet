package br.com.arndroid.etdiet.action;

import android.os.Bundle;
import android.support.v4.app.Fragment;

public interface ActivityCaller {
    public void onCallAction(int fragmentId, Class holderActivityClass,
                             String actionTag, Bundle actionData);
}