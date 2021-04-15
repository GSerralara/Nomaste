package com.project.nomaste.Adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.project.nomaste.Model.Entity.ScheduleItem;
import com.project.nomaste.R;

import java.util.LinkedList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ScheduleAdapter extends RecyclerView.Adapter<ScheduleAdapter.ViewHolder> {
    private LinkedList<ScheduleItem> list;
    private Context context;
    private LayoutInflater mInflater;

    /**
     * Initialize the dataset of the Adapter
     * by RecyclerView.
     */
    public ScheduleAdapter(@NonNull LinkedList<ScheduleItem> list,@NonNull Context context) {
        this.mInflater = LayoutInflater.from(context);
        this.context =context;
        this.list = list;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // Create a new view, which defines the UI of the list item
        View view = mInflater.inflate(R.layout.list_schedule, null);

        return new ViewHolder(view);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ScheduleAdapter.ViewHolder holder, final int position) {

        // Get element from your dataset at this position and replace the
        // contents of the view with that element
        holder.bindData(list.get(position));
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return list.size();
    }

    public void setItems(LinkedList<ScheduleItem> items){this.list=items;}

    public class ViewHolder extends RecyclerView.ViewHolder{
        ImageView icon;
        TextView time,days;
        com.google.android.material.button.MaterialButton delete;
        ViewHolder(View itemView){
            super(itemView);
            icon = itemView.findViewById(R.id.scheduleCardIcon);
            time = itemView.findViewById(R.id.scheduleHour);
            days = itemView.findViewById(R.id.scheduleDays);
            delete = itemView.findViewById(R.id.scheduleDeleteButton);
        }
        void bindData(final ScheduleItem item){
            time.setText(item.getTime());
            days.setText(item.getDaysString());
        }
    }
}
