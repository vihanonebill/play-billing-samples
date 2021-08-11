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

package com.sample.android.classytaxijava.data.network.retrofit.authentication;

import android.util.Log;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Creates Retrofit instances that
 * {@link com.sample.android.classytaxijava.data.network.retrofit.RemoteServerFunctionImpl}
 * uses to make authenticated HTTPS requests.
 *
 * @param <S>
 */
public class RetrofitClient<S> {
    private static final String TAG = "RetrofitClient";
    private final S service;
    private Retrofit retrofit;

    public RetrofitClient(String baseUrl, Class<S> serviceClass) {
        final OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .addInterceptor(new UserIdTokenInterceptor())
                .build();

        retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        service = retrofit.create(serviceClass);
    }

    public S getService() {
        return service;
    }
}
