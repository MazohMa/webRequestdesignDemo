package com.modelweb.maxiaokun.modelaboutwebrequestdemo.web;

/**
 * @Description
 * @author Mazoh
 * @createDate 2015年7月18日
 * @version 1.0.0
 */

public interface OnProgressListener {
	void onUpload(long size, long maxSize);

	void onDownload(long size, long maxSize);
}
