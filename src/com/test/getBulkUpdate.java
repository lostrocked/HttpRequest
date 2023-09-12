package com.test;

import java.io.FileWriter;
import java.io.PrintWriter;

import com.jayway.jsonpath.JsonPath;

import net.minidev.json.JSONArray;

public class getBulkUpdate {

	public static void main(String[] args) throws Exception {

		StringBuffer sb = new StringBuffer();
		String applicationIDName = "http://" + args[0] + ":" + args[1]
				+ "/datamanagement/a/api/v5/applications?only_active=true";
		String responseIDName = executeHttpGET.execHttpGet(applicationIDName, args[2], args[3]);
		JSONArray responseName = JsonPath.read(responseIDName, "$..name");
		JSONArray responseID = JsonPath.read(responseIDName, "$..id");
		for (int intIDName = 0; intIDName < responseID.size(); intIDName++) {
			String rID = ((String) responseID.get(intIDName)).replaceAll("\"", "");
			String rName = ((String) responseName.get(intIDName)).replaceAll("\"", "");
			String envURL = "http://" + args[0] + ":" + args[1] + "/datamanagement/a/versions/" + rID.trim();
			String responseEnvID = executeHttpGET.execHttpGet(envURL, args[2], args[3]);
			JSONArray arrEnvID = JsonPath.read(responseEnvID, "$..deploymentStage.id");
			for (int intAppID = 0; intAppID < arrEnvID.size(); intAppID++) {
				String envID = arrEnvID.get(intAppID).toString().replaceAll("\"", "").trim();
				String checkURl = "http://" + args[0] + ":" + args[1] + "/datamanagement/a/releasecandidates/versions/"
						+ envID + "/pages?filter=&orderBy=&pageSize=5000&pageStart=0";
				
				String restArtifactIDs = executeHttpGET.execHttpGet(checkURl, args[2], args[3]);
				JSONArray arrArtIDs = JsonPath.read(restArtifactIDs, "$..id");
				for (int intADef = 0; intADef < arrArtIDs.size(); intADef++) {
					String artDefID = arrArtIDs.get(intADef).toString().replaceAll("\"", "").trim();
					// http://srisa09-l24048.lvn.broadcom.net:8080/datamanagement/a/artifactReports/envId/21/artifacts/?artifactDefinitionId=135
					String checkURl1 = "http://" + args[0] + ":" + args[1] + "/datamanagement/a/releasecandidates/" + artDefID;
					String responseDepInfo = executeHttpGET.execHttpGet(checkURl1, args[2], args[3]);
					JSONArray arrArtVersion = JsonPath.read(responseDepInfo, "$..artifactPackage.id");
					JSONArray arrApplicationName = JsonPath.read(responseDepInfo, "$..applicationName");
					JSONArray arrArtifactPackage = JsonPath.read(responseDepInfo, "$..artifactPackage.name");
					JSONArray arrDeploymentTemplateName = JsonPath.read(responseDepInfo, "$..deploymentTemplateName");
					//JSONArray arrEnvironmentName = JsonPath.read(responseDepInfo, "$..initStage.shallowModules..environmentName");
					for (int intArtVersion = 0; intArtVersion < arrArtVersion.size(); intArtVersion++) {
						System.out.println(responseDepInfo);
						String artVersion = arrArtVersion.get(intArtVersion).toString().replaceAll("\"", "").trim();
						String artVersion1 = arrApplicationName.get(intArtVersion).toString().replaceAll("\"", "").trim();
						String artVersion2 = arrArtifactPackage.get(intArtVersion).toString().replaceAll("\"", "").trim();
						String artVersion3 = arrDeploymentTemplateName.get(intArtVersion).toString().replaceAll("\"", "").trim();
						//String artVersion4 = arrEnvironmentName.get(intArtVersion).toString().replaceAll("\"", "").trim();
						// http://srisa09-l24048.lvn.broadcom.net:8080/datamanagement/a/artifactReports/envId/21/artifacts/?artifactDefinitionId=135
						String checkURl2 = "http://" + args[0] + ":" + args[1] + "/datamanagement/a/artifact_packages/"+ artVersion + "/artifact_version_full_paths";
						String responseArtVersion = executeHttpGET.execHttpGet(checkURl2, args[2], args[3]);
						sb.append("Application Name == "+artVersion1);
						sb.append("\tArtifact Package Name == "+artVersion2);
						sb.append("\tDeployment Template Name == "+artVersion3);
						//sb.append("\tEnvironment Name == "+artVersion4);
						sb.append("\n");
						sb.append(responseArtVersion);
						sb.append("\n");
					}
				}
			}

			FileWriter writer = new FileWriter("Application-Artifacts.txt");
			PrintWriter printWriter = new PrintWriter(writer);
			printWriter.write(sb.toString());

			printWriter.close();
		}

	}

}