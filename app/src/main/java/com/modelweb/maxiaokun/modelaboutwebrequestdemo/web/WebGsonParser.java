package com.modelweb.maxiaokun.modelaboutwebrequestdemo.web;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * WebGsonParser
 * 
 *  @Description
 * @author Mazoh
 * @createDate 2015年7月18日
 * @version 1.0.0
 */

public class WebGsonParser<T> implements WebResponseParser<T> {

	private Gson gson;
	private Class<T> classT;

	public WebGsonParser(Class<T> classT) {
		this(classT, null);
	}

	public WebGsonParser(Class<T> classT, String dataFormatPattern) {
		this.classT = classT;
		if (dataFormatPattern != null && !"".equals(dataFormatPattern)) {
			gson = new GsonBuilder().setDateFormat(dataFormatPattern).create();
		} else {
			gson = new Gson();
		}
	}

	@Override
	public void parse(WebResponse<T> webResponse) {
		String json = webResponse.getResult();
		T resultObj = gson.fromJson(json, classT);
		webResponse.setResultObj(resultObj);
	}
}
