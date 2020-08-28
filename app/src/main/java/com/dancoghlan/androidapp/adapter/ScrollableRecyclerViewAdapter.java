package com.dancoghlan.androidapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.dancoghlan.androidapp.R;
import com.dancoghlan.androidapp.model.RunContext;

import org.apache.commons.lang3.StringUtils;
import org.joda.time.LocalDate;

import java.util.List;

/**
 * @See https://stackoverflow.com/questions/40584424/simple-android-recyclerview-example
 */
public class ScrollableRecyclerViewAdapter extends RecyclerView.Adapter<ScrollableRecyclerViewAdapter.ViewHolder> {
    private List<RunContext> mRunContexts;
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;

    public ScrollableRecyclerViewAdapter(Context context, List<RunContext> runContexts) {
        this.mInflater = LayoutInflater.from(context);
        this.mRunContexts = runContexts;
    }

    @Override
    @NonNull
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.list_view_runs, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        RunContext runContext = mRunContexts.get(position);
        setRunContextOnView(holder, runContext);

        if (position % 2 == 1) {
            holder.imageView.setImageResource(R.drawable.heart);
        } else {
            holder.imageView.setImageResource(R.drawable.heart2);
        }
    }

    // total number of rows
    @Override
    public int getItemCount() {
        return mRunContexts.size();
    }

    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView titleView;
        TextView dateView;
        TextView distanceView;
        TextView paceView;
        TextView timeView;
        ImageView imageView;

        ViewHolder(View itemView) {
            super(itemView);
            titleView = itemView.findViewById(R.id.list_title);
            dateView = itemView.findViewById(R.id.list_date);
            distanceView = itemView.findViewById(R.id.list_distance);
            paceView = itemView.findViewById(R.id.list_pace);
            timeView = itemView.findViewById(R.id.list_time);
            imageView = itemView.findViewById(R.id.list_icon);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
        }
    }

    // convenience method for getting data at click position
    public RunContext getItem(int id) {
        return mRunContexts.get(id);
    }

    // allows clicks events to be caught
    public void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }

    private void setRunContextOnView(ViewHolder viewHolder, RunContext runContext) {

        // Set Title
        TextView titleTextView = viewHolder.titleView;
        titleTextView.setText(runContext.getTitle());

        // Set Date
        TextView dateTextView = viewHolder.dateView;
        // TODO: LocalDate date = lastRun.getDate();
        LocalDate date = LocalDate.now();
        if (date != null) {
            dateTextView.setText(date.toString("dd MMMM, yyyy"));
        }

        // Set Distance
        TextView distanceTextView = viewHolder.distanceView;
        String distanceValue = String.valueOf(runContext.getDistance()).concat("km");
        distanceTextView.setText(distanceValue);

        // Set Pace
        TextView paceTextView = viewHolder.paceView;
        String pace = runContext.getPace();
        if (StringUtils.isNotEmpty(pace)) {
            String paceValue = pace.concat("/km");
            paceTextView.setText(paceValue);
        }

        // Set Time
        TextView timeTextView = viewHolder.timeView;
        timeTextView.setText(runContext.getTime());
    }

}