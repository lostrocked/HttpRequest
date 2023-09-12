package com.test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URI;

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

public class AllArtifactsDeployed {

	public static String scrape(String urlString, String username, String password, int intNumber) throws Exception {
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
		File file = new File("application_SomeApplication2.json");
//       File file = new File("AddLdapGroups.json");
		FileReader fileReader = new FileReader(file);
		BufferedReader bufferedReader = new BufferedReader(fileReader);
		StringBuffer stringBuffer = new StringBuffer();
		String line;
		while ((line = bufferedReader.readLine()) != null) {
//			if (line.contains("78832eb73c30")) {
//				stringBuffer.append(line.replaceFirst("78832eb73c30", "78832eb53c3" + intNumber));
//				stringBuffer.append("\n");
//			}
			if (line.contains("myweb")) {
				stringBuffer.append(line.replaceAll("myweb", "myweb" + (intNumber-1000)));
				stringBuffer.append("\n");
			}else {
			stringBuffer.append(line);
			stringBuffer.append("\n");
			}
		}
		fileReader.close();
		String sb1 = stringBuffer.toString().replace("78832eb73c31", "78832ebf" + intNumber);
		StringEntity entity = new StringEntity(sb1);
		httpPost.setEntity(entity);
		httpPost.setHeader("Accept", "application/json");
		httpPost.setHeader("Content-type", "application/json");

		HttpResponse response = httpClient.execute(host, httpPost, localContext);
		// httpClient.close();
		return EntityUtils.toString(response.getEntity());
//        HttpGet httpGet = new HttpGet(uri);
//        // Add AuthCache to the execution context
//        HttpClientContext localContext = HttpClientContext.create();
//        localContext.setAuthCache(authCache);
// 
//        HttpResponse response = httpClient.execute(host, httpGet, localContext);

//        return EntityUtils.toString(response.getEntity());
	}

	public static void main(String[] args) throws Exception {

		String URL = "http://lvndev002941.bpc.broadcom.net:8080/datamanagement/ra/design/v5/import";
		// String URL =
		// "http://srisa09-l28234.lvn.broadcom.net:8080/datamanagement/a/versions/7";
		for (int i = 1023; i < 1520; i++) {
			String response = AllArtifactsDeployed.scrape(URL, "superuser", "suser", i);
			System.out.println(response);
		}
//		JSONArray hello = JsonPath.read(response, "$..id");
//		// String sHello = hello.toJSONString().replaceAll("[", "");
//		// String sHello1 = sHello.replaceAll("]", "");
//		for (int i = 0; i < hello.size(); i++) {
//			String s1 = ((String) hello.get(i)).replaceAll("\"", "");
//			// String s2 = s1.replaceAll("[", "");
//			// String s3 = s2.replaceAll("]", "");
//			System.out.println(s1);
//		}
//
	}

}