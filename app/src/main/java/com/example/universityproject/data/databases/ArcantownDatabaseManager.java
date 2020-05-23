package com.example.universityproject.data.databases;

import com.example.universityproject.data.Models.QuizData;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;


public class ArcantownDatabaseManager {

    public ArcantownDatabaseManager() {

    }

    // @TODO
    // placeId from places list in current town
    public QuizData getQuiz(int placeId, ArrayList<String> types) {
        String requestURL = "http://192.168.1.33:8090/DJWA_war/MyServlet?want=quiz&placeid=" +
                String.valueOf(placeId) + "&types=";

        for (String t : types)
            requestURL = requestURL.concat(t).concat("*");

        StringBuilder jsonResponse = new StringBuilder();

        HttpURLConnection connection;
        BufferedReader reader;

        try {
            URL link = new URL(requestURL);
            connection = (HttpURLConnection) link.openConnection();
            connection.setConnectTimeout(5000);
            connection.setReadTimeout(5000);
            connection.connect();

            InputStream stream = connection.getInputStream();
            reader = new BufferedReader(new InputStreamReader(stream));

            String line = "";

            while ((line = reader.readLine()) != null)
                jsonResponse.append(line);

        } catch (MalformedURLException e) {
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

        return new QuizData(jsonResponse.toString());
    }
}
