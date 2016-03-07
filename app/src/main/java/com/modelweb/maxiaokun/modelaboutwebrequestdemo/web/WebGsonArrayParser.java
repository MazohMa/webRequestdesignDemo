package com.modelweb.maxiaokun.modelaboutwebrequestdemo.web;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

/**
 * WebGsonParser
 * 
 *  @Description
 * @author Mazoh
 * @createDate 2015年7月18日
 * @version 1.0.0
 */

public class WebGsonArrayParser<T> implements WebResponseParser<List<T>> {

	private Gson gson;

	public WebGsonArrayParser() {
		this(null);
	}

	public WebGsonArrayParser(String dataFormatPattern) {
		if (dataFormatPattern != null && !"".equals(dataFormatPattern)) {
			gson = new GsonBuilder().setDateFormat(dataFormatPattern).create();
		} else {
			gson = new Gson();
		}
	}

	@Override
	public void parse(WebResponse<List<T>> webResponse) {
		String json = webResponse.getResult();
		List<T> resultObj = gson.fromJson(json, new TypeToken<ArrayList<T>>() {
		}.getType());
		webResponse.setResultObj(resultObj);
	}
}
