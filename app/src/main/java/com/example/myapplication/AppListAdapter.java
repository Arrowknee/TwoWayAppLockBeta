package com.example.myapplication;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.view.View;


import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class AppListAdapter extends RecyclerView.Adapter<AppListAdapter.ViewHolder>{
    List<AppInfo> appList;
    Context context;
    AppListAdapter(List<AppInfo> appInforList){
        this.appList = appInforList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        if(context == null){
            context = viewGroup.getContext();
        }
        View view = LayoutInflater.from(context).inflate(R.layout.app_card, viewGroup, false);
        ViewHolder viewholder = new ViewHolder(view);

        return viewholder;
    }

    @Override
    public void onBindViewHolder(@NonNull AppListAdapter.ViewHolder viewHolder, int i) {
        viewHolder.appLogo.setImageDrawable(appList.get(i).appLogo);
        viewHolder.appName.setText(appList.get(i).appName);
        if(appList.get(i).appStatus){
            viewHolder.appStatus.setImageResource(R.drawable.baseline_lock_24);
        } else {
            viewHolder.appStatus.setImageResource(R.drawable.sharp_accessibility_24);
        }

        viewHolder.appStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Toggle the lock status in the data model
                appList.get(i).appStatus = !appList.get(i).appStatus;
                // Notify the adapter that the data for this item has changed
                notifyItemChanged(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return appList.size();
    }

    public List<AppInfo> getAppList() {
        return appList;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        ImageView appLogo, appStatus;
        TextView appName;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            appName = itemView.findViewById(R.id.app_name);
            appStatus = itemView.findViewById(R.id.app_status);
            appLogo = itemView.findViewById(R.id.app_logo);
        }
    }
}
