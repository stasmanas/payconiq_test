package com.stasmobstudios.payconiqtest.util;


import android.content.Context;

import com.stasmobstudios.payconiqtest.model.Repository;

import java.util.List;

import io.realm.Realm;

/**
 * Created by Stanislovas Mickus on 08/09/2017.
 */

public class LocalStorageUtil {
    private static Realm realm;

    public static void init(Context context) {
        // Initialize Realm
        Realm.init(context);
        realm = Realm.getDefaultInstance();
    }

    public static List<Repository> getRepositoryList() {
        return realm.where(Repository.class).findAll();
    }

    public static void saveRepositoryList(List<Repository> repositories) {
        realm.beginTransaction();
        realm.copyToRealmOrUpdate(repositories);
        realm.commitTransaction();
    }
}
