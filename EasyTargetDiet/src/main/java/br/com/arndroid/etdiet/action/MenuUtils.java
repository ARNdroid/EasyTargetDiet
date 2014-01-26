package br.com.arndroid.etdiet.action;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.widget.Toast;

public class MenuUtils {
    // Utility:
    protected MenuUtils() {
    }

    public static void callMenuInFragment(Context context, FragmentManager fragmentManager,
                                          int fragmentId, Class holderActivityClass,
                                          int menuItemId) {
        if (isFragmentInLayout(fragmentManager, fragmentId)) {
            callMenuInFragmentByMethod(fragmentManager, fragmentId, menuItemId);
        } else {
            callMenuInFragmentByIntent(context, holderActivityClass, menuItemId);
        }
    }

    public static boolean isFragmentInLayout(FragmentManager fragmentManager,
                                             int fragmentId) {
        return getFragmentInLayout(fragmentManager, fragmentId) != null;
    }

    public static void callMenuInFragmentByMethod(FragmentManager fragmentManager, int fragmentId,
                                                  int menuItemId) {
        // TODO: we may finish with a NPE here. Check and throw a better exception.
        ((FragmentMenuReplier) getFragmentInLayout(fragmentManager, fragmentId))
                .onReplyMenuFromHolderActivity(menuItemId);
    }

    public static void callMenuInFragmentByIntent(Context context, Class holderActivityClass,
                                                  int menuItemId) {
        // TODO: if context == holderActivityClass we will be in loop. Throw exception!
        final Intent intent = new Intent(context, holderActivityClass);
        intent.putExtra(FragmentMenuReplier.MENU_ITEM_ID_KEY, menuItemId);
        context.startActivity(intent);
    }

    private static Fragment getFragmentInLayout(FragmentManager fragmentManager, int fragmentId) {
        return fragmentManager.findFragmentById(fragmentId);
    }
}
