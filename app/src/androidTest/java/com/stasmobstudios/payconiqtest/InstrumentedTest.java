package com.stasmobstudios.payconiqtest;

import android.app.Activity;
import android.os.Build;
import android.support.test.filters.SdkSuppress;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.v7.widget.Toolbar;

import com.stasmobstudios.payconiqtest.model.Repository;
import com.stasmobstudios.payconiqtest.ui.MainActivity;
import com.stasmobstudios.payconiqtest.util.LocalStorageUtil;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class InstrumentedTest {
    @Rule
    public ActivityTestRule<MainActivity> activityTestRule = new ActivityTestRule<>(MainActivity.class);

    @Test
    @SdkSuppress(minSdkVersion = Build.VERSION_CODES.JELLY_BEAN)
    public void testActivity() {
        Activity activity = activityTestRule.getActivity();
        assertNotNull("MainActivity is not available", activity);
    }

    @Test
    @SdkSuppress(minSdkVersion = Build.VERSION_CODES.JELLY_BEAN)
    public void testUI() {
        MainActivity activity = activityTestRule.getActivity();
        assertNotNull(activity.findViewById(R.id.app_bar));
        Toolbar toolbar = activity.findViewById(R.id.toolbar);
        assertTrue(toolbar.isShown());
    }

    @Test
    @SdkSuppress(minSdkVersion = Build.VERSION_CODES.JELLY_BEAN)
    public void testRepositoryLocalStorage() {
        MainActivity activity = activityTestRule.getActivity();
        LocalStorageUtil.init(activity);
        List<Repository> repositories = LocalStorageUtil.getRepositoryList();
        assertEquals("Categories count not as expected", true, repositories.size() == 0);
    }
}
