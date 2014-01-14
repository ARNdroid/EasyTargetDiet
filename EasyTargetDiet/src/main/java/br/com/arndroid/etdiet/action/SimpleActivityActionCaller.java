package br.com.arndroid.etdiet.action;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;

public class SimpleActivityActionCaller {
    public void onCallAction(Context context, FragmentManager fragmentManager,
                             int fragmentId, Class holderActivityClass,
                             String actionTag, Bundle actionData) {

        FragmentActionReplier fragment = (FragmentActionReplier) fragmentManager.findFragmentById(
                fragmentId);

        if (fragment != null) {
            fragment.onReplyActionFromOtherFragment(actionTag, actionData);
        } else {
            Intent intent = new Intent(context, holderActivityClass);
            intent.putExtra(FragmentActionReplier.ACTION_TAG_KEY, actionTag);
            intent.putExtra(FragmentActionReplier.ACTION_DATA_KEY, actionData);
            context.startActivity(intent);
        }
    }
}
