package com.dancoghlan.androidapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.dancoghlan.androidapp.R;
import com.dancoghlan.androidapp.model.Statistic;

import java.util.List;

public class ViewRunsGridCardRecyclerViewAdapter extends RecyclerView.Adapter<ViewRunsGridCardRecyclerViewAdapter.ViewHolder> {
    private List<Statistic> labelValues;
    private Context mContext;
    private ViewRunsGridCardRecyclerViewAdapter.ClickListener<Statistic> mClickListener;

    public ViewRunsGridCardRecyclerViewAdapter(Context context, List<Statistic> labelValues) {
        this.labelValues = labelValues;
        this.mContext = context;
    }

    public interface ClickListener<T> {
        void onItemClick(T data);
    }

    public Statistic getItem(int id) {
        return labelValues.get(id);
    }

    public void setClickListener(ClickListener<Statistic> clickListener) {
        this.mClickListener = clickListener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView labelView;
        private TextView valueView;
        private ImageView imageView;
        private Statistic statistic;

        public ViewHolder(View view) {
            super(view);
            view.setOnClickListener(this);
            this.labelView = view.findViewById(R.id.label_stats_label);
            this.valueView = view.findViewById(R.id.stats_value);
            this.imageView = view.findViewById(R.id.icon_statistic);
        }

        public void setData(Statistic statistic) {
            this.statistic = statistic;
            this.labelView.setText(statistic.getLabel());
            this.valueView.setText(statistic.getValue());
            this.imageView.setImageResource(statistic.getImageId());

        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) {
                mClickListener.onItemClick(statistic);
            }
        }
    }

    @Override
    public ViewRunsGridCardRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.cardview_grid_view_runs_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewRunsGridCardRecyclerViewAdapter.ViewHolder viewholder, int position) {
        Statistic pair = labelValues.get(position);
        viewholder.setData(pair);
    }

    @Override
    public int getItemCount() {
        return labelValues.size();
    }

}