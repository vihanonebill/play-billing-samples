package com.sample.android.classytaxijava.data.network.retrofit;

import android.util.Log;

import java.io.IOException;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * {@link Callback} that simplifies handling success and failure
 *
 * @param <T> Type to return when successful
 */
public abstract class RetrofitResponseHandlerCallback<T> implements Callback<T> {
    private static final String TAG = "RetrofitCallback";

    /**
     * Used for errors without an HTTP code such as a connection failure
     */
    public static final int NO_HTTP_CODE = -1;

    private final String methodName;

    /**
     * @param methodName for logging success/error
     */
    public RetrofitResponseHandlerCallback(String methodName) {
        this.methodName = methodName;
    }

    /**
     * Called when HTTPS call has a response
     *
     * @param call     HTTPS call object
     * @param response HTTPS call's {@link Response} object
     */
    @Override
    public final void onResponse(@NonNull Call<T> call, @NonNull Response<T> response) {
        if (response.isSuccessful()) {
            Log.i(TAG, methodName + " returned successfully");
            onSuccess(response.body());
            return;
        }
        // Non 2xx response, get the details and call onError
        String errorMessage;
        ResponseBody errorBody = response.errorBody();
        if (errorBody == null) {
            errorMessage = "No error body received";
        } else {
            try {
                errorMessage = errorBody.string();
            } catch (IOException e) {
                errorMessage = e.getMessage();
            }
        }
        onError(response.code(), errorMessage);
    }

    /**
     * Called when HTTPS call fails
     *
     * @param call HTTPS call object
     * @param t    HTTPS call's failure {@link Throwable} object
     */
    @Override
    public final void onFailure(@NonNull Call<T> call, @NonNull Throwable t) {
        onError(NO_HTTP_CODE, t.getMessage());
    }

    /**
     * Called when a successful response returns from the server
     *
     * @param response Successful response object
     */
    protected abstract void onSuccess(T response);

    /**
     * Called when any error happens such as a connection failure or a 500 server response
     * <p>
     * The default implementation just calls {@link #logError(int, String)}. Override for custom
     * error handling.
     *
     * @param errorCode    HTTP error code or {@link #NO_HTTP_CODE} if none
     * @param errorMessage Optional error message for logging
     */
    protected void onError(int errorCode, @Nullable String errorMessage) {
        logError(errorCode, errorMessage);
    }

    /**
     * Logs an error to logcat as a warning
     *
     * @param errorCode    HTTP error code or {@link #NO_HTTP_CODE} if none
     * @param errorMessage Optional error message for logging
     */
    protected void logError(int errorCode, @Nullable String errorMessage) {
        StringBuilder sb = new StringBuilder(methodName).append(" failed");
        if (errorCode != NO_HTTP_CODE) {
            sb.append(" (Error code: ").append(errorCode).append(")");
        }
        sb.append(": ");
        if (errorMessage != null) {
            sb.append(errorMessage);
        } else {
            sb.append("No error message received");
        }
        Log.w(TAG, sb.toString());
    }
}
