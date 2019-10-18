package helper;


import android.util.Log;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

import jdo.HttpJdo;

public class HttpUrlOperation {

    private static final String TAG = "HttpUrlOperation";
    private StringBuilder jsonBuilder = new StringBuilder();
    private HttpURLConnection httpURLConnection;
    String line;

    /**
     * Perform http Operations(GET,POST,PUT,DELETE)
     * @param httpJdo
     * @return httpJdo
     */

    public HttpJdo httpOperation(HttpJdo httpJdo) {
        Log.d(TAG, "httpOperation: ");
        try {
            URL url = new URL(httpJdo.getmUrl());
            httpURLConnection = (HttpsURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod(httpJdo.getmRequestmethod());
            for (Map.Entry entry : httpJdo.getmHeader().entrySet()) {
                httpURLConnection.setRequestProperty(entry.getKey().toString(), entry.getValue().toString());
            }
            if (!httpJdo.getmRequestmethod().equals("GET") && httpJdo.getmPayload() != null && !httpJdo.getmPayload().trim().isEmpty()) {
                httpURLConnection.setDoOutput(true);
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(httpURLConnection.getOutputStream()));
//                Gson gson = new Gson();
//                Books bookjdo = gson.fromJson(httpJdo.getmPayload(), Books.class);
                bufferedWriter.write(httpJdo.getmPayload());
                bufferedWriter.flush();
                bufferedWriter.close();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
                while ((line = bufferedReader.readLine()) != null) {
                    jsonBuilder.append(line);
                }
                httpJdo.setmResponsebody(jsonBuilder.toString());
                httpJdo.setmRequestcode(httpURLConnection.getResponseCode());
                bufferedReader.close();
            } else {
                httpURLConnection.setDoInput(true);
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));

                while ((line = bufferedReader.readLine()) != null) {
                    jsonBuilder.append(line);
                }
                httpJdo.setmResponsebody(jsonBuilder.toString());
                httpJdo.setmRequestcode(httpURLConnection.getResponseCode());
                bufferedReader.close();
            }


        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            httpURLConnection.disconnect();
        }
        return httpJdo;
    }


}
