package br.com.arndroid.etdiet.action;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import br.com.arndroid.etdiet.R;
import br.com.arndroid.etdiet.settings.SettingsListActivity;
import br.com.arndroid.etdiet.settings.SettingsListFragment;

public class SimpleActivityCaller {
    public void onCallAction(Context context, FragmentManager fragmentManager,
                             int fragmentId, Class holderActivityClass,
                             String actionTag, Bundle actionData) {

        Fragment fragment = (Fragment) fragmentManager.findFragmentById(fragmentId);

        if (fragment != null) {
            ((FragmentReplier) fragment).onReplyAction(actionTag, actionData);
        } else {
            Intent intent = new Intent(context, holderActivityClass);
            intent.putExtra(FragmentReplier.ACTION_TAG_KEY, actionTag);
            intent.putExtra(FragmentReplier.ACTION_DATA_KEY, actionData);
            context.startActivity(intent);
        }
    }
}
