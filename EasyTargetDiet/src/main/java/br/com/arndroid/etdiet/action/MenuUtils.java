package br.com.arndroid.etdiet.action;

import android.content.Context;
import android.content.Intent;
import android.app.Fragment;
import android.app.FragmentManager;
import android.widget.Toast;

@SuppressWarnings("UnusedDeclaration")
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
        final FragmentMenuReplier fragmentMenuReplier = ((FragmentMenuReplier)
                getFragmentInLayout(fragmentManager, fragmentId));

        if (fragmentMenuReplier == null) {
            throw new IllegalArgumentException("Fragment with fragmentId=" + fragmentId +
                    " not found in layout.");
        }

        fragmentMenuReplier.onReplyMenuFromHolderActivity(menuItemId);
    }

    public static void callMenuInFragmentByIntent(Context context, Class holderActivityClass,
                                                  int menuItemId) {
        if (context.getClass() == holderActivityClass) {
            throw new IllegalArgumentException("context == holderActivityClass."
                    + " We will be in loop here. Are you calling a menu performed by the same activity?");
        }

        final Intent intent = new Intent(context, holderActivityClass);
        intent.putExtra(FragmentMenuReplier.MENU_ITEM_ID_KEY, menuItemId);
        context.startActivity(intent);
    }

    private static Fragment getFragmentInLayout(FragmentManager fragmentManager, int fragmentId) {
        return fragmentManager.findFragmentById(fragmentId);
    }
}
