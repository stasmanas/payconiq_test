package com.stasmobstudios.payconiqtest.ui;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.stasmobstudios.payconiqtest.R;
import com.stasmobstudios.payconiqtest.model.Repository;
import com.stasmobstudios.payconiqtest.util.LocalStorageUtil;
import com.stasmobstudios.payconiqtest.util.WSCalls;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Stanislovas Mickus on 07/09/2017.
 */

public class MainActivity extends AppCompatActivity {
    private static String TAG = MainActivity.class.getSimpleName();

    @BindView(R.id.rv_resource_list)
    RecyclerView rvRepositoryList;
    @BindView(R.id.ns_content)
    NestedScrollView nsContent;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    private BaseAdapter<Repository> repositoryListAdapter;

    // Paged scroll variables
    protected boolean isLastPage = false;
    protected boolean isLoading = false;
    protected int currentPage = 1;
    public static final int PAGE_LIMIT = 15;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);

        // Init local storage access
        LocalStorageUtil.init(this);

        // Init RecyclerView
        repositoryListAdapter = new RepositoryRVAdapter();
        rvRepositoryList.setAdapter(repositoryListAdapter);
        rvRepositoryList.setNestedScrollingEnabled(false);

        // Get data from WS
        repositoryListAdapter.addFooter();
        fetchRepositories();

        // Load more data at the end of the list
        nsContent.setOnScrollChangeListener((NestedScrollView.OnScrollChangeListener) (v, scrollX, scrollY, oldScrollX, oldScrollY) -> {
            if (v.getChildAt(v.getChildCount() - 1) != null) {
                if ((scrollY >= (v.getChildAt(v.getChildCount() - 1).getMeasuredHeight() - v.getMeasuredHeight())) &&
                        scrollY > oldScrollY) {
                    loadMoreData();
                }
            }
        });
    }

    // Get information from WS
    public void fetchRepositories() {
        WSCalls.fetchRepositories(currentPage, PAGE_LIMIT, new WSCalls.WSCallListener() {
            @Override
            public void addProgress() {
                rvRepositoryList.post(() -> repositoryListAdapter.addFooter());
            }

            @Override
            public void removeProgress() {
                rvRepositoryList.post(() -> repositoryListAdapter.removeFooter());
            }

            @Override
            public void lastPage() {
                isLastPage = true;
            }

            @Override
            public void stopLoading(boolean pageLoaded) {
                if (pageLoaded)
                    currentPage++;
                isLoading = false;
            }

            @Override
            public void onSuccess(List<Repository> data) {
                rvRepositoryList.post(() -> repositoryListAdapter.addAll(data));
            }

            @Override
            public void onFetchError() {
                displayFetchError(view -> {
                    repositoryListAdapter.addFooter();
                    fetchRepositories();
                });
            }

            @Override
            public void onError(Throwable t) {
                rvRepositoryList.post(() -> {
                    repositoryListAdapter.removeFooter();
                    // On network error fallback to local storage
                    if (repositoryListAdapter.getItemCount() == 0) {
                        isLastPage = true;
                        List<Repository> localRepositories = LocalStorageUtil.getRepositoryList();
                        if (localRepositories.size() > 0) {
                            rvRepositoryList.post(() -> repositoryListAdapter.addAll(localRepositories));
                        } else {
                            displayFetchError(view -> {
                                repositoryListAdapter.addFooter();
                                fetchRepositories();
                            });
                        }
                    } else {
                        displayFetchError(view -> {
                            repositoryListAdapter.addFooter();
                            fetchRepositories();
                        });
                    }
                });
                isLoading = false;
            }
        });
    }

    protected void displayFetchError(View.OnClickListener onClickListener) {
        Snackbar.make(findViewById(android.R.id.content), getString(R.string.msg_data_fetch_error), Snackbar.LENGTH_LONG)
                .setAction(getString(R.string.action_retry), onClickListener)
                .show();
    }

    protected void loadMoreData() {
        if (!isLoading && !isLastPage) {
            fetchRepositories();
        }
    }
}
