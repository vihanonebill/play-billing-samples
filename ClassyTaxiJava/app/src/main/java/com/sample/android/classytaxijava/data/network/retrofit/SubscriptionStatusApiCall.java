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

import com.sample.android.classytaxijava.data.ContentResource;
import com.sample.android.classytaxijava.data.SubscriptionStatus;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Basic and Premium Content API Interfaces.
 */
public interface SubscriptionStatusApiCall {
    public static final String BASIC_CONTENT = "content_basic";
    public static final String PREMIUM_CONTENT = "content_premium";


    // Fetch Basic content.
    @GET(BASIC_CONTENT)
    Call<ContentResource> fetchBasicContent();

    // Fetch Premium content.
    @GET(PREMIUM_CONTENT)
    Call<ContentResource> fetchPremiumContent();
}