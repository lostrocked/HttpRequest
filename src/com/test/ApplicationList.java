package com.test;

import java.io.FileWriter;
import java.io.PrintWriter;

import com.jayway.jsonpath.JsonPath;

import net.minidev.json.JSONArray;

public class ApplicationList {

	public static void main(String[] args) throws Exception {

		String applicationIDName = "http://" + args[0] + ":" + args[1]
				+ "/datamanagement/a/api/v5/applications?only_active=true";
		System.out.println(applicationIDName);
		String responseIDName = executeHttpGET.execHttpGet(applicationIDName, args[2], args[3]);
		JSONArray responseName = JsonPath.read(responseIDName, "$..name");
		JSONArray responseID = JsonPath.read(responseIDName, "$..id");
		for (int intIDName = 0; intIDName < responseID.size(); intIDName++) {
			String rID = ((String) responseID.get(intIDName)).replaceAll("\"", "");
			String rName = ((String) responseName.get(intIDName)).replaceAll("\"", "");
			StringBuffer sb = new StringBuffer();
			String envURL = "http://" + args[0] + ":" + args[1] + "/datamanagement/a/artifactReports/appId/"
					+ rID.trim() + "/environments";
			System.out.println(envURL);
			String responseEnvID = executeHttpGET.execHttpGet(envURL, args[2], args[3]);
			JSONArray arrEnvID = JsonPath.read(responseEnvID, "$..id");
			for (int intAppID = 0; intAppID < arrEnvID.size(); intAppID++) {
				String envID = arrEnvID.get(intAppID).toString().replaceAll("\"", "").trim();
				String checkURl = "http://" + args[0] + ":" + args[1] + "/datamanagement/a/artifactReports/envId/"
						+ envID;
				String restArtifactIDs = executeHttpGET.execHttpGet(checkURl, args[2], args[3]);
				JSONArray arrArtIDs = JsonPath.read(restArtifactIDs, "$.list..artifactDefinitionId");
				for (int intADef = 0; intADef < arrArtIDs.size(); intADef++) {
					String artDefID = arrArtIDs.get(intADef).toString().replaceAll("\"", "").trim();
					// http://srisa09-l24048.lvn.broadcom.net:8080/datamanagement/a/artifactReports/envId/21/artifacts/?artifactDefinitionId=135
					String checkURl1 = "http://" + args[0] + ":" + args[1] + "/datamanagement/a/artifactReports/envId/"
							+ envID + "/artifacts/?artifactDefinitionId=" + artDefID;
					String responseDepInfo = executeHttpGET.execHttpGet(checkURl1, args[2], args[3]);
					sb.append(responseDepInfo);
					sb.append("\n");
				}
			}

			FileWriter writer = new FileWriter("Application-" + rName.replaceAll(" ", "") + ".txt");
			PrintWriter printWriter = new PrintWriter(writer);
			printWriter.write(sb.toString());

			printWriter.close();
		}

	}

}