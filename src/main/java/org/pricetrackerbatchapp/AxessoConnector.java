package org.pricetrackerbatchapp;

import okhttp3.OkHttpClient;

public final class AxessoConnector {
    private static OkHttpClient okHttpClient;

    public static OkHttpClient getOkHttpClient() {
        if(okHttpClient == null) {
            okHttpClient = new OkHttpClient();
        }
        return okHttpClient;
    }
}
