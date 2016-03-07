package com.modelweb.maxiaokun.modelaboutwebrequestdemo.web.parser;


import com.modelweb.maxiaokun.modelaboutwebrequestdemo.bean.Alipay;
import com.modelweb.maxiaokun.modelaboutwebrequestdemo.util.GsonUtil;
import com.modelweb.maxiaokun.modelaboutwebrequestdemo.web.WebResponse;
import com.modelweb.maxiaokun.modelaboutwebrequestdemo.web.WebResponseParser;

/**
 * @Description
 * @author Mazoh
 * @createDate 2015年7月18日
 * @version 1.0.0
 */

public class AlipayParser implements WebResponseParser<Alipay> {
	@Override
	public void parse(WebResponse<Alipay> webResponse) {
		String json = webResponse.getResult();
		Alipay alipay = GsonUtil.createSecurityGson().fromJson(json,
				Alipay.class);
		webResponse.setResultObj(alipay);
	}
}
