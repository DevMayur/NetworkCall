package com.mayurkakade.networkcall;

import android.net.Uri;
import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class NetworkTask extends BaseAsyncTask {

    private final iOnDataFetched listener;
    HttpURLConnection urlConnection = null;
    BufferedReader reader = null;
    String recipeJson = null;
    String url;


    public NetworkTask(iOnDataFetched onDataFetchedListener, String url) {
        this.listener = onDataFetchedListener;
        this.url = url;
    }

    @Override
    public Object call() {
        Object result = null;
        Log.d("exception_networking","in call");
        result = getRecipe();
        return result;
    }

    private Object getRecipe() {

        Uri builtURI = Uri.parse(url).buildUpon().build();
        try {
            URL requestURL = new URL(builtURI.toString());
            urlConnection = (HttpURLConnection) requestURL.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();
            InputStream inputStream = urlConnection.getInputStream();
            StringBuilder builder = new StringBuilder();
            reader = new BufferedReader(new InputStreamReader(inputStream));
            String line;
            while ((line = reader.readLine()) != null) {
                builder.append(line + "\n");
            }
            if (builder.length() == 0) {
                return null;
            }
            recipeJson = builder.toString();
        }catch(Exception e) {
            Log.d("exception_networking", " exception : " + e.getMessage());
        }
        return recipeJson;
    }

    @Override
    public void setUiForLoading() {
        listener.showProgressBar();
    }

    @Override
    public void setDataAfterLoading(Object result) {
        listener.setDataInPageWithResult(result);
        listener.hideProgressBar();
    }
}

