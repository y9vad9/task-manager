package com.neon.systemtaskmanager;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.content.pm.*;

import com.neon.systemtaskmanager.R;

import java.util.ArrayList;
import java.util.List;

import static com.neon.systemtaskmanager.ContextContainer.activity;
import static com.neon.systemtaskmanager.ContextContainer.context;

public class AppStartupListAdapter extends RecyclerView.Adapter<AppStartupListAdapter.ViewHolder> implements Filterable {

    private List<AppStartupListItem> mData;
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;
    private AdapterView.OnItemLongClickListener item2;
    private ArrayList<String> settings;
    private String permission = "";
    private PackageManager pm;
    private Intent uninstallIntent;
    private List<AppStartupListItem> mDataDef;

    // data is passed into the constructor
    AppStartupListAdapter(Context context, List<AppStartupListItem> data) {
        this.mInflater = LayoutInflater.from(context);
        this.mData = data;
        this.mDataDef = data;
    }

    // inflates the row layout from xml when needed
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.itemstart_app, parent, false);
        return new ViewHolder(view);
    }


    // binds the data to the TextView in each row
    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        holder.imageView.setImageDrawable(mData.get(position).icon);
        holder.textViewTitle.setText(mData.get(position).title);
        holder.textViewPackage.setText(mData.get(position).pkg);
        if (mData.get(position).enabled) {
            holder.textViewAuto.setTextColor(Color.parseColor("#00B200"));
            holder.textViewAuto.setText(R.string.enabled);
        } else {
            holder.textViewAuto.setTextColor(Color.RED);
            holder.textViewAuto.setText(R.string.disabled);
        }
        holder.rootview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mData.get(position).toggle();
            }
        });
    }

    // total number of rows
    @Override
    public int getItemCount() {
        return mData.size();
    }
    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                List<AppStartupListItem> appStartupListItemsFilter = new ArrayList<>();
                String search = charSequence.toString();
                if (search.equals("")) {
                    appStartupListItemsFilter = mDataDef;
                } else {
                    appStartupListItemsFilter = new ArrayList<>();
                    for (int x=0;x<mDataDef.size(); x++) {

                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name or phone number match
                        if (mDataDef.get(x).title.contains(search)) {
                            appStartupListItemsFilter.add(mDataDef.get(x));
                        }
                    }
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = appStartupListItemsFilter;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                mData = (List<AppStartupListItem>) filterResults.values;

                // refresh the list with filtered data
                notifyDataSetChanged();
            }
        };
    }


    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder {
        LinearLayout rootview;
        ImageView imageView;
        TextView textViewTitle;
        TextView textViewPackage;
        TextView textViewAuto;

        ViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.iv_icon);
            textViewTitle = itemView.findViewById(R.id.tv_title);
            textViewPackage = itemView.findViewById(R.id.tv_pkg);
            textViewAuto = itemView.findViewById(R.id.tv_auto);
            rootview = itemView.findViewById(R.id.rootview);
            rootview.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    settings = new ArrayList<>();
                    settings.add(context.getString(R.string.info_about_app));
                    settings.add(context.getString(R.string.app_perms));
                    settings.add(context.getString(R.string.delete_app));
                    AlertDialog.Builder alert = new AlertDialog.Builder(context);
                    alert.setTitle(textViewTitle.getText());
                    alert.setAdapter(new ArrayAdapter(context, android.R.layout.simple_list_item_1, settings), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int pos) {
                            switch (pos) {
                                case 0:
                                    AlertDialog.Builder dlg = new AlertDialog.Builder(context);
                                    dlg.setTitle(R.string.info_about_app);
                                    dlg.setMessage(Html.fromHtml("<b>" + context.getString(R.string.info_app_name) + " </b>" + textViewTitle.getText() + "<br><b>" + context.getString(R.string.info_app_package) + " </b>" + textViewPackage.getText()));
                                    dlg.show();
                                    break;
                                case 1:
                                    AlertDialog.Builder alert2 = new AlertDialog.Builder(context);
                                    alert2.setTitle(context.getString(R.string.app_perms));
                                    alert2.setAdapter(new ArrayAdapter(context, android.R.layout.simple_list_item_1, PermissionsUtil.getGrantedPermissions(textViewPackage.getText().toString())), new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {

                                        }
                                    });
                                    alert2.show();
                                    break;
                                case 2:
                                    uninstallIntent = new Intent();
                                    uninstallIntent.setAction(Intent.ACTION_DELETE);
                                    uninstallIntent.setData(Uri.parse("package:".concat(textViewPackage.getText().toString())));
                                    ((Activity) context).startActivityForResult(uninstallIntent, 1);
                            }
                        }
                    });
                    alert.show();
                    return false;
                }
            });
        }

    }

    // convenience method for getting data at click position
    AppStartupListItem getItem(int id) {
        return mData.get(id);
    }

    // allows clicks events to be caught
    void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    void setOnLongItemClickListener(AdapterView.OnItemLongClickListener item3) {
        this.item2 = item3;
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }

    public interface OnItemLongClickListener {
        public boolean onItemLongClicked(int position);
    }
}