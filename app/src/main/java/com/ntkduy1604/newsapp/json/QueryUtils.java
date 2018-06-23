package com.ntkduy1604.newsapp.json;

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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.ntkduy1604.newsapp.constants.Constants;
import com.ntkduy1604.newsapp.model.Article;

/**
 * Helper methods related to requesting and receiving earthquake data from USGS.
 */
public final class QueryUtils {

    public static final String LOG_TAG = QueryUtils.class.getSimpleName();

    /**
     * Create a private constructor because no one should ever create a {@link QueryUtils} object.
     * This class is only meant to hold static variables and methods, which can be accessed
     * directly from the class name QueryUtils (and an object instance of QueryUtils is not needed).
     */
    private QueryUtils() {
    }

    /**
     * Query the USGS dataset and return a list of {@link com.ntkduy1604.newsapp.model.Article} objects.
     */
    public static List<Article> fetchEarthquakeData(String requestUrl) {
        // Create URL object
        URL url = createUrl(requestUrl);

        // Perform HTTP request to the URL and receive a JSON response back
        String jsonResponse = null;
//        Log.e("jsonResponse", jsonResponse.toString());

        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem making the HTTP request.", e);
        }

        return extractEarthquakes(jsonResponse);
    }

    /**
     * Return a list of {@link Article} objects that has been built up from
     * parsing a JSON response.
     */
    public static ArrayList<Article> extractEarthquakes(String jsonResponse) {
        // Create an empty ArrayList that we can start adding dataModels to
        ArrayList<Article> dataModels = new ArrayList<>();
        try {
            JSONObject jsonObject = new JSONObject(jsonResponse);
            JSONObject responseObject = jsonObject.getJSONObject("response");
            JSONArray articleArray = responseObject.getJSONArray("results");

            for (int i = 0; i < articleArray.length(); i++){
                JSONObject articleObject = articleArray.getJSONObject(i);
                JSONObject articleFieldObject = articleObject.getJSONObject("fields");
                /**
                 *  Parsing Article information
                 */
                String articleTitle = articleObject.getString(Constants.TITLE_FIELD);
                String articleAuthor = articleFieldObject.getString(Constants.AUTHOR_FIELD);

                if (null == articleAuthor) {
                    articleAuthor = "unknown";
                }

                String webPublishDate = articleObject.getString(Constants.PUBLISH_DATE_FIELD);
                String publicationDate = formatDate(webPublishDate);

                String webUrl = articleObject.getString(Constants.URL_FIELD);

                dataModels.add(new Article(articleTitle, articleAuthor, publicationDate, webUrl));
            }
        } catch (JSONException e) {
            // If an error is thrown when executing any of the above statements in the "try" block,
            // catch the exception here, so the app doesn't crash. Print a log message
            // with the message from the exception.
            Log.e("QueryUtils", "Problem parsing the earthquake JSON results", e);
        }
        // Return the list of dataModels
        return dataModels;
    }

    /**
     * Returns new URL object from the given string URL.
     */
    private static URL createUrl(String stringUrl) {
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Error with creating URL ", e);
        }
        return url;
    }

    /**
     * Make an HTTP request to the given URL and return a String as the response.
     */
    private static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";

        // If the URL is null, then return early.
        if (url == null) {
            return jsonResponse;
        }
        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000 /* milliseconds */);
            urlConnection.setConnectTimeout(15000 /* milliseconds */);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // If the request was successful (response code 200),
            // then read the input stream and parse the response.
            if (urlConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e(LOG_TAG, "Error response code: " + urlConnection.getResponseCode());
                inputStream = urlConnection.getErrorStream();
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem retrieving the earthquake JSON results.", e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    /**
     * Convert the {@link InputStream} into a String which contains the
     * whole JSON response from the server.
     */
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

    private static String formatDate(String webPublicationDate){
        // Time & Date String: "2018-06-22T10:38:59Z"
        String dateString = "(.*)(\\d{4}-\\d{2}-\\d{2})(.*)";
        String timeString = "(.*)(\\d{2}:\\d{2}:\\d{2})(.*)";

        Pattern datePattern = Pattern.compile(dateString);
        Pattern timePattern = Pattern.compile(timeString);

        Matcher dateMatcher = datePattern.matcher(webPublicationDate);
        Matcher timeMatcher = timePattern.matcher(webPublicationDate);

        String articleDate;
        String articleTime;

        if (dateMatcher.find( )) {
            articleDate =  dateMatcher.group(2);
        }else {
            articleDate = "unknown";
        }

        if (timeMatcher.find( )) {
            articleTime =  timeMatcher.group(2);
        }else {
            articleTime = "unknown";
        }

        return "D: " + articleDate + " T: " + articleTime;
    }
}