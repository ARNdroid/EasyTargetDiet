package br.com.arndroid.etdiet.action;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.FragmentManager;

public class SimpleActivityMenuCaller {
    public void onCallMenu(Context context, FragmentManager fragmentManager,
                             int fragmentId, Class holderActivityClass,
                             int menuItemId) {

        FragmentMenuReplier fragment = (FragmentMenuReplier) fragmentManager.findFragmentById(fragmentId);

        if (fragment != null) {
            fragment.onReplyMenuFromHolderActivity(menuItemId);
        } else {
            Intent intent = new Intent(context, holderActivityClass);
            intent.putExtra(FragmentMenuReplier.MENU_ITEM_ID_KEY, menuItemId);
            context.startActivity(intent);
        }
    }
}
