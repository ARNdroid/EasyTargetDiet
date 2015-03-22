package br.com.arndroid.etdiet.backups.custom;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import br.com.arndroid.etdiet.R;
import br.com.arndroid.etdiet.utils.DateUtils;

public class BackupListAdapter extends BaseAdapter {

    private final List<RestoreFileInfo> mFileInfoList;
    private final LayoutInflater mInflater;

    public BackupListAdapter(Context context, List<RestoreFileInfo> fileInfoList) {
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mFileInfoList = fileInfoList;
    }

    private View newView(ViewGroup viewGroup) {
        View view = mInflater.inflate(R.layout.backups_list_item, viewGroup, false);

        ViewHolder holder = new ViewHolder(
                (TextView) view.findViewById(R.id.lblFileName),
                (TextView) view.findViewById(R.id.lblLastModified),
                (TextView) view.findViewById(R.id.lblFolderName)
        );

        view.setTag(holder);

        return view;
    }

    private void bindView(int position, View view) {
        ViewHolder holder = (ViewHolder) view.getTag();
        holder.fileName.setText(mFileInfoList.get(position).getFileName());
        holder.lastModified.setText(DateUtils.epochToFormattedString(mFileInfoList.get(position).getLastModified()));
        holder.folderName.setText(mFileInfoList.get(position).getDirName());
    }

    @Override
    public int getCount() {
        return mFileInfoList.size();
    }

    @Override
    public Object getItem(int position) {
        return mFileInfoList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = newView(parent);
        }
        bindView(position, convertView);
        return convertView;
    }

    private static class ViewHolder {

        public final TextView fileName;
        public final TextView lastModified;
        public final TextView folderName;

        public ViewHolder(TextView fileName, TextView lastModified, TextView folderName) {
            this.fileName = fileName;
            this.lastModified = lastModified;
            this.folderName = folderName;
        }
    }
}
