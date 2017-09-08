package com.stasmobstudios.payconiqtest.ui;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.stasmobstudios.payconiqtest.R;
import com.stasmobstudios.payconiqtest.model.Repository;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Stanislovas Mickus on 07/09/2017.
 */

public class RepositoryRVAdapter
        extends BaseAdapter<Repository> {

    public RepositoryRVAdapter() {
        super();
    }

    @Override
    protected RecyclerView.ViewHolder createItemViewHolder(ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.repository_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    protected RecyclerView.ViewHolder createFooterViewHolder(ViewGroup parent) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_progress_footer, parent, false);
        final FooterViewHolder holder = new FooterViewHolder(v);

        return holder;
    }

    @Override
    protected void bindItemViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        ViewHolder holder = (ViewHolder) viewHolder;
        holder.repository = items.get(position);
        holder.tvId.setText(holder.repository.getId().toString());
        holder.tvName.setText(holder.repository.getName());
        holder.tvCreatedAt.setText(holder.repository.getCreatedAt());
        holder.tvUpdatedAt.setText(holder.repository.getUpdatedAt());
        holder.tvPrivate.setText(holder.repository.getPrivate() ? holder.tvPrivate.getContext().getString(R.string.value_true)
                : holder.tvPrivate.getContext().getString(R.string.value_false));
        holder.tvFork.setText(holder.repository.getFork() ? holder.tvPrivate.getContext().getString(R.string.value_true)
                : holder.tvPrivate.getContext().getString(R.string.value_false));
        holder.tvSize.setText(holder.repository.getSize().toString());
        holder.tvWatchersCount.setText(holder.repository.getWatchersCount().toString());
        holder.tvUrl.setText(holder.repository.getUrl());
        holder.tvForksUrl.setText(holder.repository.getForksUrl());
        holder.tvKeysUrl.setText(holder.repository.getKeysUrl());
        holder.tvCollaboratorsUrl.setText(holder.repository.getCollaboratorsUrl());
        holder.tvOwnerId.setText(holder.repository.getOwner().getId().toString());
        holder.tvOwnerLogin.setText(holder.repository.getOwner().getLogin());
    }

    @Override
    protected void bindFooterViewHolder(RecyclerView.ViewHolder viewHolder) {
    }

    @Override
    public void addFooter() {
        isFooterAdded = true;
        add(new Repository());
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tvId)
        TextView tvId;
        @BindView(R.id.tv_name)
        TextView tvName;
        @BindView(R.id.tv_created_at)
        TextView tvCreatedAt;
        @BindView(R.id.updated_at)
        TextView tvUpdatedAt;
        @BindView(R.id.tv_private)
        TextView tvPrivate;
        @BindView(R.id.tv_fork)
        TextView tvFork;
        @BindView(R.id.tv_size)
        TextView tvSize;
        @BindView(R.id.tv_watchers_count)
        TextView tvWatchersCount;
        @BindView(R.id.tv_url)
        TextView tvUrl;
        @BindView(R.id.tv_forks_url)
        TextView tvForksUrl;
        @BindView(R.id.tv_keys_url)
        TextView tvKeysUrl;
        @BindView(R.id.tv_collaborators_url)
        TextView tvCollaboratorsUrl;
        @BindView(R.id.tv_owner_id)
        TextView tvOwnerId;
        @BindView(R.id.tv_owner_login)
        TextView tvOwnerLogin;
        public Repository repository;

        public ViewHolder(View view) {
            super(view);

            ButterKnife.bind(this, view);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + tvId.getText() + "'";
        }
    }
}
