package com.neon.systemtaskmanager;

import android.content.Context;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.neon.systemtaskmanager.R;

import java.util.List;

public class AppListAdapter extends RecyclerView.Adapter<AppListAdapter.ViewHolder> {

    private List<AppListItem> mData;
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;

    // data is passed into the constructor
    AppListAdapter(Context context, List<AppListItem> data) {
        this.mInflater = LayoutInflater.from(context);
        this.mData = data;
    }

    // inflates the row layout from xml when needed
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.item_app, parent, false);
        return new ViewHolder(view);
    }

    // binds the data to the TextView in each row
    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        holder.imageView.setImageDrawable(mData.get(position).icon);
        holder.textViewTitle.setText(mData.get(position).title);
        holder.textViewPackage.setText(mData.get(position).pkg);
        holder.textViewUsed.setText(((int)mData.get(position).used)+"MB");
        holder.rootview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                YesNoAlertDialog.dialog("Are you sure?", "Kill this process?", new Runnable(){
                    @Override
                    public void run() {
                        if(SharedProps.read("root").equals("true"))
                        {
                            mData.get(position).rootKill(ContextContainer.context);
                            removeAt(position);
                            ProcessesFragment.shared_fragment.refreshPie();
                        }
                        else
                        {
                            mData.get(position).kill();
                            removeAt(position);
                            ProcessesFragment.shared_fragment.refreshPie();
                        }
                    }
                });
            }
        });
    }

    // total number of rows
    @Override
    public int getItemCount() {
        return mData.size();
    }


    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder {
        View rootview;
        ImageView imageView;
        TextView textViewTitle;
        TextView textViewPackage;
        TextView textViewUsed;

        ViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.iv_icon);
            textViewTitle = itemView.findViewById(R.id.tv_title);
            textViewPackage = itemView.findViewById(R.id.tv_pkg);
            textViewUsed = itemView.findViewById(R.id.tv_used);
            rootview = itemView.findViewById(R.id.rootview);
        }
    }

    // convenience method for getting data at click position
    AppListItem getItem(int id) {
        return mData.get(id);
    }

    // allows clicks events to be caught
    void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }

    public void removeAt(int position) {
        mData.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, mData.size());
    }
}