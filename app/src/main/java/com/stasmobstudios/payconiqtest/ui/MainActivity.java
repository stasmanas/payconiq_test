package com.stasmobstudios.payconiqtest.ui;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.stasmobstudios.payconiqtest.R;
import com.stasmobstudios.payconiqtest.model.GithubRepoService;
import com.stasmobstudios.payconiqtest.model.Repository;

import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Stanislovas Mickus on 07/09/2017.
 */

public class MainActivity extends AppCompatActivity {
    private static String TAG = MainActivity.class.getSimpleName();

    RecyclerView rvRepositoryList;
    protected boolean isLastPage = false;
    protected boolean isLoading = false;
    protected int currentPage = 1;
    public static final int PAGE_LIMIT = 15;
    private BaseAdapter<Repository> repositoryListAdapter;
    Realm realm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

//        FloatingActionButton fab = findViewById(R.id.fab);
//        fab.setOnClickListener(view -> Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                .setAction("Action", null).show());

        // Init RecyclerView
        rvRepositoryList = findViewById(R.id.rv_resource_list);
        repositoryListAdapter = new RepositoryRVAdapter();
        rvRepositoryList.setAdapter(repositoryListAdapter);
        rvRepositoryList.setNestedScrollingEnabled(false);
        repositoryListAdapter.addFooter();
        fetchRepositories();

        // Load more data at the end of the list
        NestedScrollView nsContent = findViewById(R.id.ns_content);
        nsContent.setOnScrollChangeListener((NestedScrollView.OnScrollChangeListener) (v, scrollX, scrollY, oldScrollX, oldScrollY) -> {
            if (v.getChildAt(v.getChildCount() - 1) != null) {
                if ((scrollY >= (v.getChildAt(v.getChildCount() - 1).getMeasuredHeight() - v.getMeasuredHeight())) &&
                        scrollY > oldScrollY) {
                    loadMoreData();
                }
            }
        });

        // Initialize Realm
        Realm.init(this);
        realm = Realm.getDefaultInstance();
    }

    // Get information from WS
    public void fetchRepositories() {
        Gson gson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.github.com")
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        GithubRepoService githubRepoService = retrofit.create(GithubRepoService.class);
        githubRepoService.listRepos(currentPage, PAGE_LIMIT).enqueue(repositoriesCallback);
    }

    Callback<List<Repository>> repositoriesCallback = new Callback<List<Repository>>() {
        @Override
        public void onResponse(Call<List<Repository>> call, Response<List<Repository>> response) {
            List<Repository> repositories = response.body();
            rvRepositoryList.post(() -> repositoryListAdapter.removeFooter());

            if (!response.isSuccessful()) {
                int responseCode = response.code();
                if (responseCode == 400) {
                    displayFetchError(view -> fetchRepositories());
                }
                isLoading = false;

                return;
            }

            if (repositories != null) {
                if (repositories.size() > 0) {
                    rvRepositoryList.post(() -> repositoryListAdapter.addAll(repositories));
                    // Save repository information to realm
                    realm.beginTransaction();
                    realm.copyToRealmOrUpdate(repositories);
                    realm.commitTransaction();
                }

                if (repositories.size() >= PAGE_LIMIT) {
                    rvRepositoryList.post(() -> repositoryListAdapter.addFooter());
                } else {
                    isLastPage = true;
                }
            }

            currentPage++;
            isLoading = false;

            Log.d(TAG, "Code: " + response.code() + " Message: " + response.message());
        }

        @Override
        public void onFailure(Call<List<Repository>> call, Throwable t) {
            rvRepositoryList.post(() -> {
                repositoryListAdapter.removeFooter();
                // On network error fallback to local storage
                if (repositoryListAdapter.getItemCount() == 0 && realm.where(Repository.class).count() > 0) {
                    isLastPage = true;
                    final RealmResults<Repository> localRepositories = realm.where(Repository.class).findAll();
                    rvRepositoryList.post(() -> repositoryListAdapter.addAll(localRepositories));
                } else {
                    displayFetchError(view -> fetchRepositories());
                }
            });
            isLoading = false;
            Log.e(TAG, t.fillInStackTrace().toString() + " " + t.getMessage());
        }
    };

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
