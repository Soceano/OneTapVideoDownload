/*
 * Copyright Google Inc. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.phantom.onetapvideodownload;

import android.app.Application;
import android.widget.Toast;

import com.anjlab.android.iab.v3.BillingProcessor;
import com.anjlab.android.iab.v3.TransactionDetails;
import com.evernote.android.job.JobManager;
import com.phantom.HookFetchJob;
import com.phantom.HookFetchJobCreator;
import com.phantom.utils.CheckPreferences;
import com.phantom.utils.Global;

public class MainApplication extends Application {
    private static boolean activityVisible;

    @Override
    public void onCreate() {
        super.onCreate();

        JobManager.create(this).addJobCreator(new HookFetchJobCreator());
        int noOfJobRequests = JobManager.instance().getAllJobRequestsForTag(HookFetchJob.TAG).size();

        // No of Job Requests for HookFetchJob would be greater than 1 for older version of
        // application because of rescheduling of job again and again, causing large no. of wakelocks
        if (noOfJobRequests > 1) {
            JobManager.instance().cancelAllForTag(HookFetchJob.TAG);
            noOfJobRequests = 0;
        }

        if (noOfJobRequests == 0) {
            HookFetchJob.scheduleJob();
        }
    }

    public static boolean isActivityVisible() {
        return activityVisible;
    }

    public static void activityResumed() {
        activityVisible = true;
    }

    public static void activityPaused() {
        activityVisible = false;
    }
}
