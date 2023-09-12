package com.test;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
import net.minidev.json.parser.JSONParser;
import okhttp3.Response;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;

public class TMobileDemo {

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

		String URL = "http://10.252.213.54:8080/TestDataManager/user/login?syncLogin=true";
		String response = TMobileDemo.scrape(URL, "Administrator", "marmite");

		JSONArray hello = JsonPath.read(response, "$..token");
		String s11 = (String) hello.get(0);
		File file = new File("datagen.json");
		StringBuffer sbreq = new StringBuffer();
		String line = "";
		try (BufferedReader br = new BufferedReader(new FileReader(file))) {
			while ((line = br.readLine()) != null) {
				sbreq.append(line);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		JSONArray jsonO = JsonPath.read(sbreq.toString(), "$..status");
		JSONArray json1 = JsonPath.read(sbreq.toString(), "$..tableName");
		String fnR = "";
		for (int i = 0; i < jsonO.size(); i++) {
			fnR = findTableReplace(sbreq.toString());
		}
		OkHttpClient client = new OkHttpClient().newBuilder().build();
		MediaType mediaType = MediaType.parse("application/json");
		RequestBody body = RequestBody.create(mediaType, fnR);
		Request request = new Request.Builder().url("http://10.252.213.54:8080/TDMJobService/api/ca/v1/jobs")
				.method("POST", body).addHeader("Authorization", "Bearer " + s11)
				.addHeader("Content-Type", "application/json").build();

		Response response1 = client.newCall(request).execute();
		System.out.println(response1.body().string());

	}

	private static String findTableReplace(String dataGen) throws Exception {
		// TODO Auto-generated method stub
		String replaceStr = dataGen.replace("\"status\":1", "\"status\":0");
		return replaceStr;
	}

}