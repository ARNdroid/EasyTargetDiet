package br.com.arndroid.etdiet.weights;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.app.ListFragment;
import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Loader;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import br.com.arndroid.etdiet.R;
import br.com.arndroid.etdiet.action.FragmentActionReplier;
import br.com.arndroid.etdiet.action.FragmentMenuReplier;
import br.com.arndroid.etdiet.dialog.WeightAutoDialog;
import br.com.arndroid.etdiet.provider.Contract;
import br.com.arndroid.etdiet.provider.weights.WeightsEntity;
import br.com.arndroid.etdiet.provider.weights.WeightsManager;
import br.com.arndroid.etdiet.utils.DateUtils;

public class WeightsListFragment extends ListFragment implements FragmentMenuReplier,
        LoaderManager.LoaderCallbacks<Cursor>,
        FragmentActionReplier {

    private static final int WEIGHTS_LOADER_ID = 1;

    private TextView mTxtEmpty;
    private ListView mLstList;
    private WeightsListAdapter mAdapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.weights_list_fragment, container, false);
        bindScreen(rootView);
        setupScreen(rootView);
        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mAdapter = new WeightsListAdapter(getActivity());
        setListAdapter(mAdapter);
    }

    @Override
    public void onListItemClick(ListView listView, View view, int position, long id) {
        WeightAutoDialog dialog = new WeightAutoDialog();
        dialog.setTitle(getString(R.string.weight));
        dialog.setMinIntegerValue(0);
        dialog.setMaxIntegerValue(300);
        final WeightsManager manager = new WeightsManager(getActivity().getApplicationContext());
        dialog.setWeightEntity(manager.weightFromId(id));
        dialog.show(getFragmentManager(), null);
    }

    @Override
    public void onReplyMenuFromHolderActivity(int menuItemId) {
        switch (menuItemId) {
            case R.id.add:
                WeightAutoDialog dialog = new WeightAutoDialog();
                dialog.setTitle(getString(R.string.weight));
                dialog.setMinIntegerValue(0);
                dialog.setMaxIntegerValue(300);
                final WeightsManager manager = new WeightsManager(getActivity().getApplicationContext());
                final WeightsEntity entity = manager.getSuggestedNewWeight();
                dialog.setWeightEntity(entity);
                dialog.show(getFragmentManager(), null);
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
        final ListView list = (ListView) rootView.findViewById(android.R.id.list);
        list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                // We need a final variable to use inside alert onClick event:
                final long weightId = id;
                final WeightsManager manager = new WeightsManager(
                        getActivity().getApplicationContext());
                final WeightsEntity entity = manager.weightFromId(weightId);

                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        manager.remove(weightId);
                    }
                });
                builder.setNegativeButton(android.R.string.cancel, null);
                builder.setMessage(String.format(getString(R.string.delete_weight_msg),
                        entity.getWeight(), DateUtils.dateIdToFormattedString(entity.getDateId()),
                        DateUtils.timeToFormattedString(entity.getTime())));
                builder.create().show();
                return true;
            }
        });
        mTxtEmpty.setText(getString(R.string.list_empty_weights));
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

    @Override
    public void onReplyActionFromOtherFragment(String actionTag, Bundle actionData) {
        // If not loaded, load the first instance,
        // otherwise closes current loader e start a new one:
        getLoaderManager().restartLoader(WEIGHTS_LOADER_ID, null, this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        switch (id) {
            case WEIGHTS_LOADER_ID:
                return new CursorLoader(getActivity(), Contract.Weights.CONTENT_URI,
                        null , null, null, Contract.Weights.DATE_AND_TIME_DESC);
            default:
                throw new IllegalArgumentException("Invalid loader id=" + id);
        }
    }

    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor data) {
        mAdapter.swapCursor(data);
        refreshScreen();
    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {
        mAdapter.swapCursor(null);
    }
}