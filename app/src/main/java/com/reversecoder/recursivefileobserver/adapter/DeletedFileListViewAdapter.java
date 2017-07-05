package com.reversecoder.recursivefileobserver.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.reversecoder.recursivefileobserver.R;
import com.reversecoder.recursivefileobserver.sqlite.table.DeletedFileInfo;

import java.util.ArrayList;

/**
 * @author Md. Rashadul Alam
 *         Email: rashed.droid@gmail.com
 */
public class DeletedFileListViewAdapter extends BaseAdapter {

    private Activity mActivity;
    private ArrayList<DeletedFileInfo> mData;
    private static LayoutInflater inflater = null;

    public DeletedFileListViewAdapter(Activity activity) {
        mActivity = activity;
        mData = new ArrayList<DeletedFileInfo>();
        inflater = (LayoutInflater) activity
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public ArrayList<DeletedFileInfo> getData() {
        return mData;
    }

    public void setData(ArrayList<DeletedFileInfo> data) {
        mData = data;
        notifyDataSetChanged();
    }

    public int getItemPosition(String name) {
        for (int i = 0; i < mData.size(); i++) {
            if (((DeletedFileInfo) mData.get(i)).getFileName().contains(name)) {
                return i;
            }
        }
        return -1;
    }


    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public DeletedFileInfo getItem(int position) {
        return mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {

        View vi = convertView;
        if (convertView == null)
            vi = inflater.inflate(R.layout.list_row_deleted_file, null);

        DeletedFileInfo mDeletedFile =getItem(position);

        TextView fileName = (TextView) vi.findViewById(R.id.tv_file_name);
        TextView filePath = (TextView) vi.findViewById(R.id.tv_file_path);
        fileName.setText(mDeletedFile.getFileName());
        filePath.setText(mDeletedFile.getFilePath());

        return vi;
    }
}