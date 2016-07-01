package com.girish.noty;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.girish.noty.data.NoteItem;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Girish on 27/06/16.
 */
public class ListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final View.OnClickListener mListener;
    List<NoteItem> mItems;

    public ListAdapter(List<NoteItem> items, View.OnClickListener listener) {
        mItems = items;
        mListener = listener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.note_item, parent, false);
        v.setOnClickListener(mListener);
        CustomViewHolder cvh = new CustomViewHolder(v);
        return cvh;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        CustomViewHolder vh = (CustomViewHolder) holder;
        NoteItem item = mItems.get(position);
        vh.mTitle.setText(item.getTitle());
        vh.mDescription.setText(item.getDescription());
        vh.mTime.setText(item.getTimeStamp());
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    class CustomViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.title_item)
        public TextView mTitle;

        @BindView(R.id.description_item)
        public TextView mDescription;

        @BindView(R.id.time_item)
        public TextView mTime;

        public CustomViewHolder(View root) {
            super(root);
            ButterKnife.bind(this, root);
        }
    }
}
