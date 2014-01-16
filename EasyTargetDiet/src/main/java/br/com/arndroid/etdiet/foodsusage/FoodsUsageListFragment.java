package br.com.arndroid.etdiet.foodsusage;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.Date;

import br.com.arndroid.etdiet.R;
import br.com.arndroid.etdiet.action.FragmentMenuReplier;
import br.com.arndroid.etdiet.meals.Meals;
import br.com.arndroid.etdiet.provider.foodsusage.FoodsUsageEntity;
import br.com.arndroid.etdiet.provider.foodsusage.FoodsUsageManager;
import br.com.arndroid.etdiet.quickinsert.QuickInsertFrag;
import br.com.arndroid.etdiet.utils.DateUtils;

public class FoodsUsageListFragment extends ListFragment implements FragmentMenuReplier {

    private String mDateId;
    private int mMeal;
    private FoodsUsageListAdapter mAdapter;
    private TextView mTxtEmpty;
    private ListView mLstList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.foods_usage_list_fragment, container, false);
        bindScreen(rootView);
        setupScreen(rootView);
        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mAdapter = new FoodsUsageListAdapter(getActivity());
        setListAdapter(mAdapter);

        ListView list = getListView();
        list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                // We need a final variable to use inside alert onClick event:
                final long foodId = id;
                final FoodsUsageManager manager = new FoodsUsageManager(
                        getActivity().getApplicationContext());
                final FoodsUsageEntity entity = manager.foodUsageFromId(foodId);
                final String mealName = FoodsUsageListFragment.this.getString(
                        Meals.getMealResourceNameIdFromMealId(entity.getMeal()));

                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        manager.remove(foodId);
                    }
                });
                builder.setNegativeButton(android.R.string.cancel, null);
                builder.setMessage(String.format(getResources().getString(R.string.delete_food_usage_msg),
                        entity.getDescription(), mealName));
                builder.create().show();
                return true;
            }
        });
    }

    @Override
    public void onListItemClick(ListView listView, View view, int position, long id) {
        QuickInsertFrag dialog = new QuickInsertFrag();
        dialog.setId(id);
        dialog.show(getActivity().getSupportFragmentManager(), QuickInsertFrag.UPDATE_TAG);
    }

    @Override
    public void onReplyMenuFromHolderActivity(int menuItemId) {
        switch (menuItemId) {
            case R.id.quick_add:
                FragmentManager manager = getFragmentManager();
                QuickInsertFrag dialog = new QuickInsertFrag();
                dialog.setDateId(mDateId);
                dialog.setTime(getDefaultTime());
                dialog.setMeal(Meals.getMealFromPosition(mMeal));
                dialog.setDescription(null);
                dialog.setValue(getDefaultValue());
                dialog.show(manager, QuickInsertFrag.INSERT_TAG);
                break;
            default:
                throw new IllegalArgumentException("Invalid menuItemId=" + menuItemId);
        }
    }

    private void bindScreen(View rootView) {
        mTxtEmpty = (TextView) rootView.findViewById(android.R.id.empty);
        mLstList = (ListView) rootView.findViewById(android.R.id.list);
    }

    private void setupScreen(View rootView) {
        mTxtEmpty.setText(getResources().getText(R.string.list_empty_foods_usage));
    }

    private void refreshScreen() {
        if (mLstList.getAdapter().getCount() == 0) {
            mTxtEmpty.setVisibility(View.VISIBLE);
            mLstList.setVisibility(View.GONE);
        } else {
            mTxtEmpty.setVisibility(View.GONE);
            mLstList.setVisibility(View.VISIBLE);
        }
    }

    private float getDefaultValue() {
        return Meals.preferredUsageForMealInDate(getActivity().getApplicationContext(),
                Meals.getMealFromPosition(mMeal),
                DateUtils.dateIdToDate(mDateId));
    }

    private int getDefaultTime() {
        return DateUtils.dateToTimeAsInt(new Date());
    }

    public void onDataChangedFromHolderActivity(String dateId, int meal, Cursor data) {
        mDateId = dateId;
        mMeal = meal;
        mAdapter.swapCursor(data);
        refreshScreen();
    }
}