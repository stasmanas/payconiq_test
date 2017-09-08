package com.stasmobstudios.payconiqtest.ui;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ProgressBar;

import com.stasmobstudios.payconiqtest.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Stanislovas Mickus on 07/09/2017.
 */

public abstract class BaseAdapter<T> extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    protected static final int ITEM = 0;
    protected static final int FOOTER = 1;

    protected List<T> items;
    protected boolean isFooterAdded = false;


    public BaseAdapter() {
        items = new ArrayList<>();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder = null;

        switch (viewType) {
            case ITEM:
                viewHolder = createItemViewHolder(parent);
                break;
            case FOOTER:
                viewHolder = createFooterViewHolder(parent);
                break;
            default:
                break;
        }

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        switch (getItemViewType(position)) {
            case ITEM:
                bindItemViewHolder(viewHolder, position);
                break;
            case FOOTER:
                bindFooterViewHolder(viewHolder);
            default:
                break;
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }


    protected abstract RecyclerView.ViewHolder createItemViewHolder(ViewGroup parent);

    protected abstract void bindItemViewHolder(RecyclerView.ViewHolder viewHolder, int position);

    public abstract void addFooter();


    protected RecyclerView.ViewHolder createFooterViewHolder(ViewGroup parent) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_progress_footer, parent, false);
        final FooterViewHolder holder = new FooterViewHolder(v);

        return holder;
    }

    protected void bindFooterViewHolder(RecyclerView.ViewHolder viewHolder) {}


    public T getItem(int position) {
        return items.get(position);
    }

    public void add(T item) {
        items.add(item);
        notifyItemInserted(items.size() - 1);
    }

    public void addAll(List<T> items) {
        for (T item : items) {
            add(item);
        }
    }

    private void remove(T item) {
        int position = items.indexOf(item);
        if (position > -1) {
            items.remove(position);
            notifyItemRemoved(position);
        }
    }

    public void clear() {
        isFooterAdded = false;
        while (getItemCount() > 0) {
            remove(getItem(0));
        }
    }

    public boolean isLastPosition(int position) {
        return (position == items.size() - 1);
    }

    public void removeFooter() {
        isFooterAdded = false;

        int position = items.size() - 1;
        T item = getItem(position);

        if (item != null) {
            items.remove(position);
            notifyItemRemoved(position);
        }
    }

    @Override
    public int getItemViewType(int position) {
        return (isLastPosition(position) && isFooterAdded) ? FOOTER : ITEM;
    }

    public static class FooterViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.progress_layout)
        FrameLayout loadingFrameLayout;
        @BindView(R.id.loading_progress_bar)
        ProgressBar loadingImageView;

        public FooterViewHolder(View view) {
            super(view);

            ButterKnife.bind(this, view);
        }
    }

}
