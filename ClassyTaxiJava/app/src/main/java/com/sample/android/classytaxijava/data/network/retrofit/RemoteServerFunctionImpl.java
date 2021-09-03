/*
 * Copyright 2021 Google LLC. All rights reserved.
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

package com.sample.android.classytaxijava.data.network.retrofit;

import android.util.Log;

import com.sample.android.classytaxijava.data.ContentResource;
import com.sample.android.classytaxijava.data.SubscriptionStatus;
import com.sample.android.classytaxijava.data.network.firebase.ServerFunctions;
import com.sample.android.classytaxijava.data.network.retrofit.authentication.RetrofitClient;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import androidx.lifecycle.MutableLiveData;

import static com.sample.android.classytaxijava.BuildConfig.SERVER_URL;

/**
 * Implementation of Interfaces with Retrofit.
 */
public class RemoteServerFunctionImpl implements ServerFunctions {

    private static final String TAG = "RemoteServerFunction";
    private static volatile ServerFunctions INSTANCE = null;
    private final MutableLiveData<List<SubscriptionStatus>> subscriptions = new MutableLiveData<>();
    private final MutableLiveData<ContentResource> basicContent = new MutableLiveData<>();
    private final MutableLiveData<ContentResource> premiumContent = new MutableLiveData<>();
    private final MutableLiveData<Boolean> loading = new MutableLiveData<>();
    private final RetrofitClient<SubscriptionStatusApiCall> retrofitClient = new RetrofitClient<>(SERVER_URL, SubscriptionStatusApiCall.class);

    private RemoteServerFunctionImpl() {
    }

    public static ServerFunctions getInstance() {
        if (INSTANCE == null) {
            synchronized (RemoteServerFunctionImpl.class) {
                if (INSTANCE == null) {
                    INSTANCE = new RemoteServerFunctionImpl();
                }
            }
        }
        return INSTANCE;
    }

    /**
     * Live data is true when there are pending network requests.
     */
    @Override
    public LiveData<Boolean> getLoading() {
        return loading;
    }

    /**
     * The latest subscription data from the Firebase server.
     * <p>
     * Use this class by observing the subscriptions LiveData.
     * Any server updates will be communicated through this LiveData.
     */
    @Override
    public LiveData<List<SubscriptionStatus>> getSubscriptions() {
        return subscriptions;
    }

    /**
     * The basic content URL.
     */
    @Override
    public LiveData<ContentResource> getBasicContent() {
        return basicContent;
    }

    /**
     * The premium content URL.
     */
    @Override
    public LiveData<ContentResource> getPremiumContent() {
        return premiumContent;
    }

    /**
     * Logs HTTPS call failure message.
     *
     * @param method
     * @param throwableResponse
     */
    private void logHttpsCallFailure(@NonNull String method, @NonNull Throwable throwableResponse) {
        Log.w(TAG, "Call to " + method + " failed: " + throwableResponse.getMessage());
    }

    /**
     * Checks if HTTPS call response back is null.
     * Logs response back error.
     *
     * @param method
     * @param errorResponseBody
     */
    private void logHttpsResponseError(@NonNull String method, @Nullable ResponseBody errorResponseBody) {
        if (errorResponseBody == null) {
            Log.w(TAG, method + ": No error response body returned");
        } else {
            Log.w(TAG, "Response to " + method + " errored out: " + errorResponseBody);
        }
    }

    /**
     * Fetches basic content and posts results to {@link #basicContent}.
     * This will fail if the user does not have a basic subscription.
     */
    @Override
    public void updateBasicContent() {
        // Instance for the Basic Content Interface.
        String method = "updateBasicContent";
        retrofitClient.getService().fetchBasicContent()
            .enqueue(new Callback<ContentResource>() {
                @Override
                public void onResponse(@NonNull Call<ContentResource> call, @NonNull Response<ContentResource> response) {
                    if (response.isSuccessful()) {
                        Log.i(TAG, "basicCall is successful");
                        ContentResource responseData = response.body();
                        if (responseData == null) {
                            Log.w(TAG, "Invalid basic subscription data");
                        } else {
                            basicContent.postValue(responseData);
                        }
                    } else {
                        logHttpsResponseError(method, response.errorBody());
                    }
                }

                @Override
                public void onFailure(@NonNull Call<ContentResource> call, @NonNull Throwable t) {
                    logHttpsCallFailure(method, t);
                }
            });
    }

    /**
     * Fetches premium content and posts results to {@link #premiumContent}.
     * This will fail if the user does not have a premium subscription.
     */
    public void updatePremiumContent() {
        // TODO(cassigbe@): Implement updatePremiumContent method.
    }

    public void updateSubscriptionStatus() {
        // TODO(cassigbe@): Implement updateSubscriptionStatus method.
    }

    public void registerSubscription(String sku, String purchaseToken) {
        // TODO(cassigbe@): Implement registerSubscription method.
    }

    public void transferSubscription(String sku, String purchaseToken) {
        // TODO(cassigbe@): Implement transferSubscription method.
    }

    /**
     * Register Instance ID for Firebase Cloud Messaging.
     *
     * @param instanceId an FCM registration/instance token returned by
     * {@link com.sample.android.classytaxijava.data.network.retrofit.authentication.FcmRegistrationToken}
     */
    @Override
    public void registerInstanceId(String instanceId) {
        String method = "registerInstanceId";
        Map<String, String> data = new HashMap<>();
        data.put("instanceId", instanceId);
        retrofitClient.getService().registerInstanceID(data)
                .enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(Call<String> call, @NonNull Response<String> response) {
                        if (response.isSuccessful()) {
                            Log.i(TAG, "Instance ID registration successful.");
                        } else {
                           logHttpsResponseError(method, response.errorBody());
                        }
                    }

            @Override
            public void onFailure(@NonNull Call<String> call, @NonNull Throwable t) {
                logHttpsCallFailure(method, t);
            }
        });
    }

    /**
     * Unregister Instance ID for Firebase Cloud Messaging.
     *
     * @param instanceId an FCM registration/instance token returned by
     * {@link com.sample.android.classytaxijava.data.network.retrofit.authentication.FcmRegistrationToken}
     */
    @Override
    public void unregisterInstanceId(String instanceId) {
        String method = "unregisterInstanceId";
        Map<String, String> data = new HashMap<>();
        data.put("instanceId", instanceId);
        retrofitClient.getService().unregisterInstanceID(data)
                .enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(@NonNull Call<String> call, @NonNull Response<String> response) {
                        if (response.isSuccessful()) {
                            Log.i(TAG, "Instance ID un-registration successful.");
                        } else {
                            logHttpsResponseError(method, response.errorBody());
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<String> call, @NonNull Throwable t) {
                        logHttpsCallFailure(method, t);
                    }
                });
    }
}




