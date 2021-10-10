package com.googlesearch;
import java.io.IOException;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

public class Jwiki {
    final String BASE_URL="https://en.wikipedia.org/api/rest_v1/page/summary/";
	OkHttpClient client ;

    public static void main(String []args) {
    	Jwiki jwiki= new Jwiki();
    String reString=	jwiki.getData("narendra modi");
    System.out.println(reString);
    }
    public Jwiki() {
    	client = new OkHttpClient();
	}

    protected String getData(String subject) {
    	String extractText="";

        Request request = new Request.Builder()
                .url(BASE_URL+subject)
                .get()
                .build();
        try {
            Response response=client.newCall(request).execute();
            String data = response.body().string();
            JSONParser parser = new JSONParser();
            JSONObject jsonObject = (JSONObject)parser.parse(data);


            //get text
             extractText = (String)jsonObject.get("extract");

        }
        catch (IOException | ParseException e) {
            e.printStackTrace();
        }
        return extractText;
    }


}