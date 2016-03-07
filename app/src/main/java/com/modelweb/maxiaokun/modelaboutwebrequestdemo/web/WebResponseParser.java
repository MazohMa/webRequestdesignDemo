package com.modelweb.maxiaokun.modelaboutwebrequestdemo.web;

/**
 * Parser
 * 
 *  @Description
 * @author Mazoh
 * @createDate 2015年7月18日
 * @version 1.0.0
 */

public interface WebResponseParser<T> {
	void parse(WebResponse<T> webResponse);
}
