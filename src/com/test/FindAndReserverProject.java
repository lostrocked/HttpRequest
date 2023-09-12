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

import com.jayway.jsonpath.JsonPath;

import net.minidev.json.JSONArray;
import okhttp3.Response;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;

public class FindAndReserverProject {

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

		String URL = "http://10.252.213.54:8080/TestDataManager/user/login?syncLogin=true";
		String response = FindAndReserverProject.scrape(URL, "Administrator", "marmite");

		JSONArray hello = JsonPath.read(response, "$..token");
		String s11 = (String) hello.get(0);
		File file = new File("C:\\Work\\Apps\\Workspace\\HttpRequest\\1.json");
		OkHttpClient client = new OkHttpClient().newBuilder().build();
		MediaType mediaType = MediaType.parse("application/json");
		@SuppressWarnings("deprecation")
		RequestBody body = RequestBody.create(mediaType, file);
		
		Request request = new Request.Builder().url(
				"http://renbdl812946-01.bpc.broadcom.net:8080/TDMFindReserveService/api/ca/v1/testDataModels/346/actions/find?projectId=1420&versionId=1421")
				.method("POST", body).addHeader("Authorization", "Bearer " + s11)
				.addHeader("Content-Type", "application/json").build();
		Response response1 = client.newCall(request).execute();
		String s12 = response1.body().string();
		StringBuffer sb = new StringBuffer();
		JSONArray hello1 = JsonPath.read(s12, "$..modelKeys");
		System.out.println(hello1.size());
		for (int i = 0; i < hello1.size(); i++) {
			String s123 = hello1.get(i).toString().replace("{ProductID=", "").replace("}", "").trim();
			String regex = "^0+(?!$)";
			s123 = s123.replaceAll(regex, "");
			OkHttpClient client1 = new OkHttpClient().newBuilder().build();
			MediaType mediaType1 = MediaType.parse("application/json");
			@SuppressWarnings("deprecation")
			RequestBody body1 = RequestBody.create(mediaType1,
					"{\"environmentId\":340,\"showReservedRecords\":false,\"page\":1,\"size\":100,\"filters\":[{\"attributeName\":\"ProID\",\"entityName\":\"Test_DB4\",\"schema\":\"dbo\",\"dataSource\":\"D4\",\"dataSourceValid\":true,\"operator\":\"IN_VALUES\",\"values\":["+s123+"]}],\"attributes\":[{\"attributeName\":\"ProID\",\"entityName\":\"Test_DB4\",\"schema\":\"dbo\",\"dataSource\":\"D4\"},{\"attributeName\":\"ProductName3\",\"entityName\":\"Test_DB4\",\"schema\":\"dbo\",\"dataSource\":\"D4\"}]}");
			Request request1 = new Request.Builder().url(
					"http://renbdl812946-01.bpc.broadcom.net:8080/TDMFindReserveService/api/ca/v1/testDataModels/365/actions/find?projectId=1420&versionId=1421")
					.method("POST", body1).addHeader("Authorization", "Bearer " + s11)
					.addHeader("Content-Type", "application/json").build();
			Response response2 = client1.newCall(request1).execute();
			String s1234 = response2.body().string();
			JSONArray hello2 = JsonPath.read(s1234, "$..totalCount");
			String s09 = hello2.get(0).toString();
			if(s09.equalsIgnoreCase("0")) {
				sb.append(s123);
				sb.append("\n");
			}
		}
		System.out.println(sb.toString());
		
	}

}