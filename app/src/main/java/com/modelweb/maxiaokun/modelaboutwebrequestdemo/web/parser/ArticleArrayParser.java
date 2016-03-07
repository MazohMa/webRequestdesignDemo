package com.modelweb.maxiaokun.modelaboutwebrequestdemo.web.parser;


import com.google.gson.reflect.TypeToken;
import com.modelweb.maxiaokun.modelaboutwebrequestdemo.bean.Article;
import com.modelweb.maxiaokun.modelaboutwebrequestdemo.util.GsonUtil;
import com.modelweb.maxiaokun.modelaboutwebrequestdemo.web.WebResponse;
import com.modelweb.maxiaokun.modelaboutwebrequestdemo.web.WebResponseParser;

import java.util.ArrayList;
import java.util.List;

/**
 *@Description
 * @author Mazoh
 * @createDate 2015年7月18日
 * @version 1.0.0
 */

public class ArticleArrayParser implements WebResponseParser<List<Article>> {
	@Override
	public void parse(WebResponse<List<Article>> webResponse) {
		String json = webResponse.getResult();
		List<Article> articles = GsonUtil.createSecurityGson().fromJson(json, new TypeToken<ArrayList<Article>>() {
				}.getType());
		webResponse.setResultObj(articles);
	}
}
