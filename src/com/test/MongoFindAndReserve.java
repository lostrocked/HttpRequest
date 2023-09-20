package com.test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.nio.charset.StandardCharsets;

import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.AuthCache;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.auth.BasicScheme;
import org.apache.http.impl.client.BasicAuthCache;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;

import net.minidev.json.JSONArray;
import okhttp3.Response;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;

public class MongoFindAndReserve {

	public static String scrape(String urlString, String username, String password) throws Exception {
		URI uri = URI.create(urlString);
		HttpHost host = new HttpHost(uri.getHost(), uri.getPort(), uri.getScheme());
		CredentialsProvider credsProvider = new BasicCredentialsProvider();
		credsProvider.setCredentials(new AuthScope(uri.getHost(), uri.getPort()),
				new UsernamePasswordCredentials(username, password));
		// Create AuthCache instance
		AuthCache authCache = new BasicAuthCache();
		// Generate BASIC scheme object and add it to the local auth cache
		BasicScheme basicAuth = new BasicScheme();
		authCache.put(host, basicAuth);
		CloseableHttpClient httpClient = HttpClients.custom().setDefaultCredentialsProvider(credsProvider).build();
		HttpPost httpPost = new HttpPost(uri);
		HttpClientContext localContext = HttpClientContext.create();
		localContext.setAuthCache(authCache);

		HttpResponse response = httpClient.execute(host, httpPost, localContext);
		// httpClient.close();
		return EntityUtils.toString(response.getEntity());
	}

	public static void main(String[] args) throws Exception {

		String URL = "http://localhost:8080/TestDataManager/user/login?syncLogin=true";
		String response = scrape(URL, "Administrator", "marmite");

		JSONArray hello = JsonPath.read(response, "$..token");
		String s11 = (String) hello.get(0);
		File file = new File("Find.json");
		OkHttpClient client = new OkHttpClient().newBuilder().build();
		MediaType mediaType = MediaType.parse("application/json");
		@SuppressWarnings("deprecation")
		RequestBody body = RequestBody.create(mediaType, file);
		
		Request request = new Request.Builder().url(
				"http://localhost:8080/TDMFindReserveService/api/ca/v1/testDataModels/59/actions/find?projectId=2352&versionId=2353")
				.method("POST", body).addHeader("Authorization", "Bearer " + s11)
				.addHeader("Content-Type", "application/json").build();
		Response response1 = client.newCall(request).execute();
		String s12 = response1.body().string();
		StringBuffer sb = new StringBuffer();
		JSONArray hello1 = JsonPath.read(s12, "$..records");
		
		for (int i = 0; i < hello1.size(); i++) {
			String s123 = hello1.get(i).toString();
			JSONArray arrArtIDs = JsonPath.read(s123, "$..value");
			File file1 = new File("Second.json");
			String convertedJson = convertJSON(file1, arrArtIDs.get(5).toString());
			makePostCall(convertedJson);
		}
		
	}

	private static void makePostCall(String convertedJson) throws Exception {
		// TODO Auto-generated method stub
		String URL = "http://10.252.213.54:8080/TestDataManager/user/login?syncLogin=true";
		String response = scrape(URL, "Administrator", "marmite");

		JSONArray hello = JsonPath.read(response, "$..token");
		String s11 = (String) hello.get(0);
		File file = new File("Find.json");
		OkHttpClient client = new OkHttpClient().newBuilder().build();
		MediaType mediaType = MediaType.parse("application/json");
		@SuppressWarnings("deprecation")
		RequestBody body = RequestBody.create(mediaType, convertedJson);
		
		Request request = new Request.Builder().url(
				"http://10.252.213.54:8080/TDMFindReserveService/api/ca/v1/testDataModels/801/actions/find?projectId=2352&versionId=2353")
				.method("POST", body).addHeader("Authorization", "Bearer " + s11)
				.addHeader("Content-Type", "application/json").build();
		Response response1 = client.newCall(request).execute();
		String s12 = response1.body().string();
		System.out.println("Output - "+ s12);		
	}

	private static String convertJSON(File file1, String values) throws Exception {
		// TODO Auto-generated method stub
		BufferedReader br     = new BufferedReader(new FileReader(file1));
		StringBuffer sb = new StringBuffer();
		String st;
		while ((st = br.readLine()) != null) {
			//System.out.println(st);
			sb.append(st);
		}
		DocumentContext hello = JsonPath.parse(sb.toString());
		JSONArray arrArtIDs = JsonPath.read(hello.jsonString(), "$..filters[0].values");
		String newJson = hello.jsonString().replace(arrArtIDs.get(0).toString(), "[\""+values+"\"]");
		br.close();
		return newJson;
	}

}