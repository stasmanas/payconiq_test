package com.stasmobstudios.payconiqtest.util;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.stasmobstudios.payconiqtest.model.Repository;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Stanislovas Mickus on 08/09/2017.
 */

public class WSCalls {
    private static String TAG = WSCalls.class.getSimpleName();
    private static Gson gson = new GsonBuilder()
            .setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")
            .create();
    private static Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("https://api.github.com")
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build();

    public static void fetchRepositories(int page, int pageLimit, WSCallListener wsCallListener) {
        GithubRepoService githubRepoService = retrofit.create(GithubRepoService.class);
        githubRepoService.listRepos(page, pageLimit)
                .enqueue(new Callback<List<Repository>>() {
                             @Override
                             public void onResponse(Call<List<Repository>> call, Response<List<Repository>> response) {
                                 List<Repository> repositories = response.body();
                                 wsCallListener.removeProgress();

                                 if (!response.isSuccessful()) {
                                     int responseCode = response.code();
                                     if (responseCode == 400) {
                                         wsCallListener.onFetchError();
                                     }
                                     wsCallListener.stopLoading(false);

                                     return;
                                 }

                                 if (repositories != null) {
                                     if (repositories.size() > 0) {
                                         wsCallListener.onSuccess(repositories);
                                         LocalStorageUtil.saveRepositoryList(repositories);
                                     }

                                     if (repositories.size() >= pageLimit) {
                                         wsCallListener.addProgress();
                                     } else {
                                         wsCallListener.lastPage();
                                     }
                                 }

                                 wsCallListener.stopLoading(true);
                             }

                             @Override
                             public void onFailure(Call<List<Repository>> call, Throwable t) {
                                 wsCallListener.onError(t);
                             }
                         }

                );
    }

    public interface WSCallListener {
        void addProgress();

        void removeProgress();

        void lastPage();

        void stopLoading(boolean pageLoaded);

        void onSuccess(List<Repository> data);

        void onFetchError();

        default void onError(Throwable t) {
            Log.e(TAG, t.fillInStackTrace().toString() + " " + t.getMessage());
        }
    }
}
