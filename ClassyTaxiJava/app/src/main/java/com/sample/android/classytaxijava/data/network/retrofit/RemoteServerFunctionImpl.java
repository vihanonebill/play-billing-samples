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

import com.sample.android.classytaxijava.BuildConfig;
import com.sample.android.classytaxijava.data.ContentResource;
import com.sample.android.classytaxijava.data.SubscriptionStatus;
import com.sample.android.classytaxijava.data.network.firebase.ServerFunctions;

import androidx.lifecycle.LiveData;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.util.List;

import androidx.lifecycle.MutableLiveData;

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

    // Retrofit Builder.
    private Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(BuildConfig.SERVER_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build();

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
     * Fetches basic content and posts results to {@link #basicContent}.
     * This will fail if the user does not have a basic subscription.
     */
    @Override
    public void updateBasicContent() {
        // Instance for the Basic Content Interface.
        SubscriptionStatusApiCall basicCall = retrofit.create(SubscriptionStatusApiCall.class);
        Call<ContentResource> call = basicCall.fetchBasicContent();
        call.enqueue(new Callback<ContentResource>() {
            @Override
            public void onResponse(Call<ContentResource> call, Response<ContentResource> response) {
                if (response.isSuccessful()) {
                    Log.i(TAG, "basicCall is successful");
                    ContentResource responseData = response.body();
                    if (responseData == null) {
                        Log.e(TAG, "Invalid basic subscription data");
                        return;
                    } else {
                        basicContent.postValue(responseData);
                    }
                }
            }

            @Override
            public void onFailure(Call<ContentResource> call, Throwable t) {
                Log.i(TAG, "basicCall failed");
                return;
            }
        });
    }

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

    public void registerInstanceId(String instanceId) {
        // TODO(cassigbe@): Implement registerInstanceId method.
    }

    public void unregisterInstanceId(String instanceId) {
        // TODO(cassigbe@): Implement unregisterInstanceId method.
    }
}




