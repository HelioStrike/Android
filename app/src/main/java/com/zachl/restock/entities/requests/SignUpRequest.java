package com.zachl.restock.entities.requests;

import android.content.Context;
import android.content.SharedPreferences;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;

public class SignUpRequest {

    final String url = "https://condescending-neumann-62a029.netlify.app/.netlify/functions/users/register";
    AppCompatActivity activity;

    public SignUpRequest(AppCompatActivity activity) {
        this.activity = activity;
    }

    public void register(String email, String password, String displayName) {
        this.register(email, password, displayName, null);
    }

    public void register(String email, String password, String displayName, String location) {
        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(activity);

        try {
            //Creating the request's body
            JSONObject jsonBody = new JSONObject();
            jsonBody.put("email", email);
            jsonBody.put("password", password);
            jsonBody.put("displayName", displayName);
            if(location != null) jsonBody.put("location", location); //as location is optional, according to the API docs

            // Request a string response from the provided URL.
            StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //success
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //error
                        try {
                            //getting error messages
                            String body = new String(error.networkResponse.data,"UTF-8");
                            JSONObject errorJson = new JSONObject(body);
                            JSONArray errorsJsonArray = errorJson.getJSONArray("errors");

                            List<String> errorsList = new ArrayList<String>();
                            for(int i = 0; i < errorsJsonArray.length(); i++){
                                errorsList.add(errorsJsonArray.getString(i));
                            }
                        } catch (Exception e) {
                            //error
                        }
                    }
                }
            );

            // Add the request to the RequestQueue.
            queue.add(stringRequest);
        } catch (Exception e) {
            //error
            e.printStackTrace();
        }

    }
}