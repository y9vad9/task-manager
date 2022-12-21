package com.neon.systemtaskmanager;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

public class AppToolInfoAdapter extends RecyclerView.Adapter<AppToolInfoAdapter.MyViewHolder> {
    private String[] title;
    private String[] about;
    private ArrayList<View.OnClickListener> onClickListeners;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class MyViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public MyViewHolder(View v) {
            super(v);
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public AppToolInfoAdapter(String[] titles, String[] info, ArrayList<View.OnClickListener> onClickListeners) {
        title = titles;
        about = info;
        this.onClickListeners = onClickListeners;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public AppToolInfoAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent,
                                                              int viewType) {
        // create a new view
        View view = ContextContainer.activity.getLayoutInflater().inflate(R.layout.item_app_info, null);
        MyViewHolder vh = new MyViewHolder(view);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        TextView t_title = holder.itemView.findViewById(R.id.title);
        TextView t_about = holder.itemView.findViewById(R.id.about);
        t_about.setText(about[position]);
        t_title.setText(title[position]);
        LinearLayout root = (LinearLayout) holder.itemView.findViewById(R.id.root);
        root.setOnClickListener(onClickListeners.get(position));
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return title.length;
    }
}