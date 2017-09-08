package com.stasmobstudios.payconiqtest.util;

import com.stasmobstudios.payconiqtest.model.Repository;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by Stanislovas Mickus on 07/09/2017.
 */


public interface GithubRepoService {
    @GET("users/JakeWharton/repos")
    Call<List<Repository>> listRepos(@Query("page") int page, @Query("per_page") int perPage);
}
