package br.com.arndroid.etdiet.backups.custom;

import android.app.AlertDialog;
import android.app.ListFragment;
import android.content.DialogInterface;
import android.os.Bundle;
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
import br.com.arndroid.etdiet.provider.weights.WeightsEntity;
import br.com.arndroid.etdiet.provider.weights.WeightsManager;
import br.com.arndroid.etdiet.utils.DateUtils;

import static br.com.arndroid.etdiet.backups.custom.CustomBackupManager.BackupOperationResult;
import static br.com.arndroid.etdiet.backups.custom.CustomBackupManager.RestoreOperationResult;

public class BackupListFragment extends ListFragment implements FragmentMenuReplier,
        FragmentActionReplier {

    private TextView mTxtEmpty;
    private ListView mLstList;
    private BackupListAdapter mAdapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.backups_list_fragment, container, false);
        bindScreen(rootView);
        setupScreen();
        return rootView;
    }

    @Override
    public void onListItemClick(ListView listView, View view, final int position, long id) {
        final CustomBackupManager manager = new CustomBackupManager(getActivity());
        final RestoreFileInfo candidate = (RestoreFileInfo) mAdapter.getItem(position);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(getString(R.string.restore));
        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                final RestoreOperationResult restoreResult = manager.doRestore(candidate);
                final String resultMsg;
                switch (restoreResult) {
                    case IO_EXCEPTION:
                        resultMsg = getString(R.string.restore_result_msg_io_exception);
                        break;
                    case SECURITY_EXCEPTION:
                        resultMsg = getString(R.string.restore_result_msg_security_exception);
                        break;
                    case SD_CARD_UNAVAILABLE:
                        resultMsg = getString(R.string.restore_result_msg_sd_card_unavailable);
                        break;
                    case INVALID_CANDIDATE_FILE:
                        resultMsg = getString(R.string.restore_result_msg_invalid_candidate);
                        break;
                    case SUCCESS:
                        resultMsg = getString(R.string.restore_result_msg_success);
                        break;
                    default:
                        throw new IllegalArgumentException("Invalid restoreResult=" + restoreResult);
                }
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle(getString(R.string.restore));
                builder.setPositiveButton(android.R.string.ok, null);
                builder.setMessage(resultMsg);
                builder.create().show();
            }
        });
        builder.setNegativeButton(android.R.string.cancel, null);
        builder.setMessage(String.format(getString(R.string.restore_backup_msg), candidate.getFileName()));
        builder.create().show();
   }

    @Override
    public void onReplyMenuFromHolderActivity(int menuItemId) {
        switch (menuItemId) {
            case R.id.add:
                CustomBackupManager manager = new CustomBackupManager(getActivity());
                final BackupOperationResult backupResult = manager.doBackup();
                final String resultMsg;
                switch (backupResult) {
                    case IO_EXCEPTION:
                        resultMsg = getString(R.string.backup_result_msg_io_exception);
                        break;
                    case SECURITY_EXCEPTION:
                        resultMsg = getString(R.string.backup_result_msg_security_exception);
                        break;
                    case SD_CARD_UNAVAILABLE:
                        resultMsg = getString(R.string.backup_result_msg_sd_card_unavailable);
                        break;
                    case SUCCESS:
                        resultMsg = getString(R.string.backup_result_msg_success);
                        break;
                    default:
                        throw new IllegalArgumentException("Invalid backupResult=" + backupResult);
                }
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle(getString(R.string.backup));
                builder.setPositiveButton(android.R.string.ok, null);
                builder.setMessage(resultMsg);
                builder.create().show();
                swapAdapter();
                break;
            default:
                throw new IllegalArgumentException("Invalid menuItemId=" + menuItemId);
        }
    }

    private void swapAdapter() {
        final CustomBackupManager manager = new CustomBackupManager(getActivity());
        mAdapter = new BackupListAdapter(getActivity(), manager.getAllRestoreCandidates());
        setListAdapter(mAdapter);
        refreshScreen();
    }

    private void bindScreen(View rootView) {
        mTxtEmpty = (TextView) rootView.findViewById(android.R.id.empty);
        mLstList = (ListView) rootView.findViewById(android.R.id.list);
    }

    private void setupScreen() {
        mLstList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                final RestoreFileInfo candidateFileInfo = (RestoreFileInfo) mAdapter.getItem(position);
                final CustomBackupManager manager = new CustomBackupManager(
                        getActivity().getApplicationContext());

                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        manager.deleteCandidate(candidateFileInfo);
                        swapAdapter();
                    }
                });
                builder.setNegativeButton(android.R.string.cancel, null);
                builder.setTitle(getString(R.string.backup));
                builder.setMessage(String.format(getString(R.string.delete_backup_file_msg),
                        candidateFileInfo.getFileName()));
                builder.create().show();
                return true;
            }
        });
        mTxtEmpty.setText(getString(R.string.list_empty_backups));
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
        swapAdapter();
    }
}