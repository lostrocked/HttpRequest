package com.test;

import java.io.FileWriter;
import java.io.PrintWriter;

import com.jayway.jsonpath.JsonPath;

import net.minidev.json.JSONArray;
import org.json.JSONObject;
import org.json.XML;
import org.json.JSONException;

public class C2 {

	public static void main(String[] args) throws Exception {

		StringBuffer sb = new StringBuffer();
		String responseIDName = executeHttpGET.execHttpGet(
				"http://lvndev010362.bpc.broadcom.net:1505/api/Dcm/virtual_service_environments/VSE/virtual_services",
				"admin", "admin");
		
	}

}