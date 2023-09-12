package com.test;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.nio.charset.StandardCharsets;

import javax.servlet.ServletOutputStream;

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
import org.json.JSONObject;

import com.jayway.jsonpath.JsonPath;

import net.minidev.json.JSONArray;
import okhttp3.Response;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;

public class DataGen {

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

	@SuppressWarnings("deprecation")
	public static void main(String[] args) throws Exception {

//		String URL = "http://10.252.213.54:8080/TestDataManager/user/login?syncLogin=true";
//		String response = FindAndReserverProject.scrape(URL, "Administrator", "marmite");
//
//		JSONArray hello = JsonPath.read(response, "$..token");
//		String s11 = (String) hello.get(0);
		File file = new File("C:\\Work\\Apps\\Workspace\\HttpRequest\\1.json");
		OkHttpClient client = new OkHttpClient().newBuilder().build();
		MediaType mediaType = MediaType.parse("application/json");
		RequestBody body = RequestBody.create(mediaType, file);
		
		Request request = new Request.Builder().url(
				"https://services-uscentral.skytap.com:10885/AIMWebService/api/Accounts?appid=TDM&safe=Test&object=testobject").build();
		Response response1 = client.newCall(request).execute();
		String s12 = response1.body().string();
		System.out.println(s12);
//		StringBuffer sb = new StringBuffer();
//		//JSONObject jsonO = JsonPath.read(s12, "$..id");
//		JSONObject hello1 = new JSONObject(s12);
//		//JSONArray hello1 = JsonPath.read(s12, "$..id");
//		String jobID = Integer.toString((int) hello1.get("jobId"));
//		
//		System.out.println(jobID);
//		Thread.sleep(125000);
//		OkHttpClient client1 = new OkHttpClient().newBuilder().build();
//		MediaType mediaType1 = MediaType.parse("application/zip");
//		RequestBody body1 = RequestBody.create(mediaType1,
//				"");
//		Request request1 = new Request.Builder().url(
//			"http://10.252.213.54:8080/TDMJobService/api/ca/v1/jobs/"+jobID+"/actions/downloadArtifact/")
//			.method("POST", body1).addHeader("Authorization", "Bearer " + s11)
//			.addHeader("Content-Type", "application/zip").build();
//		
//		Response response2 = client1.newCall(request1).execute();
//		InputStream is = response2.body().byteStream();
//		File file1 = new File("C:\\Work\\Apps\\Workspace\\HttpRequest\\sachin.zip");
//		BufferedInputStream input = new BufferedInputStream(is);
//		OutputStream output = new FileOutputStream(file1);
//
//		byte[] data = new byte[1024];
//
//		long total = 0;
//		int count = 0;
//		while ((count = input.read(data)) != -1) {
//		    total += count;
//		    output.write(data, 0, count);
//		}
//
//		output.flush();
//		output.close();
		
	}

}