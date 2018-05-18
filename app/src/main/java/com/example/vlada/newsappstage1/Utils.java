package com.example.vlada.newsappstage1;

import android.text.TextUtils;
import android.util.Log;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public class Utils {

    // Tag for the log messages
    private static final String LOG_TAG = Utils.class.getSimpleName();

    // This class is only meant to hold static variables and methods, which can be accessed directly from the class name Utils
    private Utils() {
    }

    // Query the Guardian data set and return a list of objects */
    public static List<NewsData> fetchNewsData(String requestUrl){
        // Create URL object
        URL url = returnUrl(requestUrl);
        // Perform HTTP request to the URL and receive a JSON response back
        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Request Error", e);
        }

        // Extract relevant fields from the JSON response
        List<NewsData> news = extractFeatureFromJson(jsonResponse);
        return news;

    }

    // Returns new URL object from the given string URL.
    private static URL returnUrl(String stringUrl) {
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Error Building URL", e);
        }
        return url;
    }

    // Make an HTTP request to the given URL and return a String as the response.
    private static String makeHttpRequest(URL url) throws IOException {
        String jasonResponse = "";

        if (url == null) {
            return jasonResponse;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(1000);
            urlConnection.setConnectTimeout(1500);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // If the request was successful (response code 200),
            // then read the input stream and parse the response.
            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jasonResponse = readFromStream(inputStream);
            } else {
                Log.e(LOG_TAG, "Error Response Code: " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem Fetching News", e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                inputStream.close();
            }
        }
        return jasonResponse;
    }

    //Convert the InputStream into a String which contains the whole JSON response from the server.
    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }

    // Return a list of objects that has been built up from parsing the given JSON response
    private static List<NewsData> extractFeatureFromJson(String newsJSON) {
        if (TextUtils.isEmpty(newsJSON)) {
            return null;
        }

        // Create an empty ArrayList that we can start adding earthquakes to
        List<NewsData> newsList = new ArrayList<>();

        // Try to parse the JSON response string. If there's a problem with the way the JSON
        // is formatted, a JSONException exception object will be thrown.
        // Catch the exception so the app doesn't crash, and print the error message to the logs.
        try {
            // Create a JSONObject from the JSON response string
            JSONObject baseJsonNewsResponse = new JSONObject(newsJSON);
            JSONObject responseJsonNews = baseJsonNewsResponse.getJSONObject("response");
            JSONArray newsArray = responseJsonNews.getJSONArray("results");

            for (int i = 0; i < newsArray.length(); i++) {
                JSONObject currentNews = newsArray.getJSONObject(i);
                String newsCategory = currentNews.getString("sectionName");
                String newsDate = "The Publication Date is not available";
                if (currentNews.has("webPublicationDate")) {
                    newsDate = currentNews.getString("webPublicationDate");
                }

                String newsTitle = currentNews.getString("webTitle");
                String newsUrl = currentNews.getString("webUrl");
                JSONArray currentNewsAuthorArray = currentNews.getJSONArray("tags");
                String newsAuthor = "The Author of this article is not available";
                int tagsLength = currentNewsAuthorArray.length();
                if (tagsLength == 1) {
                    JSONObject currentNewsAuthor = currentNewsAuthorArray.getJSONObject(0);
                    String newsAuthorFinal = currentNewsAuthor.getString("webTitle");
                    newsAuthor = "Author: " + newsAuthorFinal;
                }

                NewsData newNews = new NewsData(newsCategory, newsTitle, newsAuthor, newsDate, newsUrl);
                newsList.add(newNews);
            }

        // If an error is thrown when executing any of the above statements in the "try" block,
        // catch the exception here, so the app doesn't crash. Print a log message
        // with the message from the exception.
        } catch (JSONException e) {
            Log.e("NewsUtils", "JSON results parsing error.");
        }

        return newsList;
    }
}
