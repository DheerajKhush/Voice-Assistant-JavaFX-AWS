package com.googlesearch;

import java.io.IOException;

import org.json.JSONArray;
import org.json.JSONObject;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class GoogleSearchResult {
	static OkHttpClient client;
	public GoogleSearchResult(){
		 client = new OkHttpClient();
	}

	public static void main(String[] args) throws IOException {

GoogleSearchResult googleSearchResult =new GoogleSearchResult();
googleSearchResult.getSearchResult("where is india");

	}
	public String getSearchResult(String searchQuery) {
		searchQuery=searchQuery.replaceAll(" ", "+");

		Request request = new Request.Builder()
			.url("https://google-search3.p.rapidapi.com/api/v1/search/q="+searchQuery+"&num=5&cr=CountryIN")
			.get()
			.addHeader("x-user-agent", "desktop")
			.addHeader("x-rapidapi-host", "google-search3.p.rapidapi.com")
			.addHeader("x-rapidapi-key", "af4a92b72bmshece02652aae480fp14a3c4jsnea3fa79a71f5")
			.build();

		Response response = null;
		try {
			response = client.newCall(request).execute();
		} catch (IOException e) {

			e.printStackTrace();
		}
		String jsonDataString=null;
		try {
			jsonDataString = response.body().string();
		} catch (IOException e) {

			e.printStackTrace();
		}
		System.out.println(jsonDataString);
		 JSONObject jsonObject= new JSONObject (jsonDataString);
		JSONArray jResultArray= jsonObject.getJSONArray("results");
		JSONObject firstResultObject =(JSONObject) jResultArray.get(0);
		String resutlString=firstResultObject.get("description").toString();

		int idxPeriod=resutlString.indexOf(".");
		if(resutlString.contains("?"))
		resutlString=resutlString.replaceAll("?", "");
		if(resutlString.contains(":"))
		resutlString=resutlString.replaceAll(":", "");

		resutlString=resutlString.substring(0, idxPeriod+1);
		System.out.println(resutlString);
		System.out.println("Success");
		return resutlString;

	}


}
