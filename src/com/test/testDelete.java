package com.test;

import com.jayway.jsonpath.JsonPath;

import net.minidev.json.JSONArray;

public class testDelete {

	public static void main(String[] args) throws Exception {

		// String applicationIDName = "http://" + args[0] + ":" + args[1] +
		// "/datamanagement/a/api/v5/applications?only_active=true";
		String applicationIDs = "http://lvndev002941.bpc.broadcom.net:8080/datamanagement/a/shallow_applications?includeDeleteStatus=true";
		String responseIDName1 = executeHttpGET.execHttpGet(applicationIDs, "superuser", "suser");
		JSONArray responseID = JsonPath.read(responseIDName1, "$..id");
		for (int intIDName = 0; intIDName < responseID.size(); intIDName++) {
			String strID = (String) responseID.get(intIDName);
			if (Integer.parseInt((String) strID) > 63034000) {
				String applicationIDName = "http://lvndev002941.bpc.broadcom.net:8080/datamanagement/design/applications/"
						+ strID.trim() + "?force=true";
				String responseIDName2 = executeDelete.execHttpGet(applicationIDName, "superuser", "suser");
				System.out.println(responseIDName2);
			}
			
		}

	}

}