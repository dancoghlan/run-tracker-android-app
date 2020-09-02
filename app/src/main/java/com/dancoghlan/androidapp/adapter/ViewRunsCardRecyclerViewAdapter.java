package com.dancoghlan.androidapp.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.dancoghlan.androidapp.R;
import com.dancoghlan.androidapp.model.RunContext;

import org.apache.commons.lang3.StringUtils;
import org.joda.time.LocalDate;

import java.util.List;

public class ViewRunsCardRecyclerViewAdapter extends RecyclerView.Adapter<ViewRunsCardRecyclerViewAdapter.MyViewHolder> {
    private List<RunContext> runContexts;
    private ClickListener<RunContext> mClickListener;

    public ViewRunsCardRecyclerViewAdapter(List<RunContext> runContexts) {
        this.runContexts = runContexts;
    }

    public interface ClickListener<T> {
        void onItemClick(T data);
    }

    public void setOnItemClickListener(ClickListener<RunContext> clickListener) {
        this.mClickListener = clickListener;
    }

    @Override
    public ViewRunsCardRecyclerViewAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_view_runs_item, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewRunsCardRecyclerViewAdapter.MyViewHolder holder, final int position) {
        final RunContext runContext = runContexts.get(position);
        setRunContextOnView(holder, runContext);

        if (position % 2 == 1) {
            holder.imageView.setImageResource(R.drawable.run_circle_24dp);
        } else {
            holder.imageView.setImageResource(R.drawable.run_circle_black_48dp);
        }
    }

    @Override
    public int getItemCount() {
        return runContexts.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView titleView;
        TextView dateView;
        TextView distanceView;
        TextView paceView;
        TextView timeView;
        ImageView imageView;

        public MyViewHolder(View itemView) {
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
            if (mClickListener != null) mClickListener.onItemClick(runContexts.get(getAdapterPosition()));
        }
    }

    private void setRunContextOnView(ViewRunsCardRecyclerViewAdapter.MyViewHolder viewHolder, RunContext runContext) {

        // Set Title
        TextView titleTextView = viewHolder.titleView;
        titleTextView.setText(runContext.getTitle());

        // Set Date
        TextView dateTextView = viewHolder.dateView;
        LocalDate date = runContext.getDate();
        if (date != null) {
            dateTextView.setText(date.toString("dd MMMM, yyyy"));
        }

        // Set Distance
        TextView distanceTextView = viewHolder.distanceView;
        String distanceValue = String.valueOf(runContext.getDistance()).concat("km");
        distanceTextView.setText(distanceValue);

        // Set Pace
        TextView paceTextView = viewHolder.paceView;
        String pace = runContext.getPace().toString();
        if (StringUtils.isNotEmpty(pace)) {
            String paceValue = pace.concat("/km");
            paceTextView.setText(paceValue);
        }

        // Set Time
        TextView timeTextView = viewHolder.timeView;
        timeTextView.setText(runContext.getTimeAsString());
    }

}