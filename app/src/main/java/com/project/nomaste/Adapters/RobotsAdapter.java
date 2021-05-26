package com.project.nomaste.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.project.nomaste.MainActivity;
import com.project.nomaste.Model.Entity.Robot;
import com.project.nomaste.R;

import java.util.LinkedList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class RobotsAdapter extends RecyclerView.Adapter<RobotsAdapter.ViewHolder> {
    private LinkedList<Robot> list;
    private Context context;
    private LayoutInflater mInflater;

    /**
     * Initialize the dataset of the Adapter
     * by RecyclerView.
     */
    public RobotsAdapter(@NonNull LinkedList<Robot> list, @NonNull Context context) {
        this.mInflater = LayoutInflater.from(context);
        this.context =context;
        this.list = list;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public RobotsAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // Create a new view, which defines the UI of the list item
        View view = mInflater.inflate(R.layout.list_robots_admin, null);

        return new RobotsAdapter.ViewHolder(view);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(RobotsAdapter.ViewHolder holder, final int position) {

        // Get element from your dataset at this position and replace the
        // contents of the view with that element
        holder.bindData(list.get(position));
    }
    public void setItems(LinkedList<Robot> items){this.list=items;}
    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView title;
        com.google.android.material.button.MaterialButton delete;
        ViewHolder(final View itemView){
            super(itemView);
            title = itemView.findViewById(R.id.robotsAdminTitle);
            delete = itemView.findViewById(R.id.robotsDeleteButton);

        }
        void bindData(final Robot item){
            title.setText(item.getName());
            delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    removeItem(item);
                }
            });
        }
        private void removeItem(Robot infoData) {

            int currPosition = list.indexOf(infoData);
            list.remove(currPosition);
            notifyItemRemoved(currPosition);
        }
        private void addItem(int position, Robot infoData) {

            list.add(position, infoData);
            notifyItemInserted(position);
        }
    }
}
