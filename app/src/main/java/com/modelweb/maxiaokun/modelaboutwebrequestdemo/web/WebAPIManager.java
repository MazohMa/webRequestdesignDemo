package com.modelweb.maxiaokun.modelaboutwebrequestdemo.web;



import android.util.Log;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.apache.http.HttpException;
import org.json.JSONException;

import java.util.HashMap;
import java.util.Map;

/**
 * WebAPIManager
 *
 * @Description
 * @author Mazoh
 * @createDate 2015年7月18日
 * @version 1.0.0
 */

public class WebAPIManager {
    public static final String tag = "WebAPIManager";

    private volatile static WebAPIManager webAPIManager;
    //根据手机cpu来决定线程的默认值大小
    private static final int DEFAULTTHREADSIZE = Runtime.getRuntime().availableProcessors() * 3 + 2;
    private int poolSize = 0;//指定线程池线程个数
    private static final int DEFAULTBYTESIZE = 1024 ;
    private String accessToken = "";
    private String clientId = "1";
    private String appVersion = "";

    /**
     * 格式化log
     */
    private boolean isDetail = true;
    /**
     *分发线程池处理网络请求
     */
    private RequestDispatchThreadPoolManager requestDispatchThreadPoolManager ;
    private WebAPIManager() {

        if (requestDispatchThreadPoolManager == null) {
            //默认线程池
            requestDispatchThreadPoolManager =  RequestDispatchThreadPoolManager.
                    getInstance().initThreadPoolType(-1) ;

        }
    }

    private WebAPIManager(int type, int poolSize) {
        if (requestDispatchThreadPoolManager == null) {
                 requestDispatchThreadPoolManager = RequestDispatchThreadPoolManager.getInstance().setType(type).
                    setPoolSize(poolSize < 0?DEFAULTTHREADSIZE:this.poolSize)
                    .initThreadPoolType(type) ;
        }
    }

    /**
     * 启动默认线程池的网络管理请求类
     *
     * @return
     */
    public static WebAPIManager getInstance() {
        if (webAPIManager == null) {
            synchronized (WebAPIManager.class) {
                if (webAPIManager == null) {
                    webAPIManager = new WebAPIManager();
                }
            }
        }
        return webAPIManager;
    }

    /**
     * 启动指定的线程池的网络管理请求类
     *
     * @param threadPoolType,线程池类型
     * @param poolSize,线程池大小
     * @param needBufferDecorate 去读字节流是否需要缓冲区
     * @param  writeByteSize 一次写入流大小，1 为1024,2为2048.。。
     * @return this
     */
    public static WebAPIManager getInstance(int threadPoolType, int poolSize,
                                            boolean needBufferDecorate,int writeByteSize) {
        if (webAPIManager == null) {
            synchronized (WebAPIManager.class) {
                if (webAPIManager == null) {
                    webAPIManager = new WebAPIManager(threadPoolType, poolSize);
                    if(needBufferDecorate){
                        WebConnectionManager.getInstance().setDateType(WebConnectionManager.DateType.BUFFERSTREAM) ;
                    }else{
                        WebConnectionManager.getInstance().setDateType(WebConnectionManager.DateType.INPUTSTREAM) ;
                    }
                    if(writeByteSize > 0){
                        WebConnectionManager.getInstance().setBufferSize(writeByteSize);
                    }else{
                        WebConnectionManager.getInstance().setBufferSize(DEFAULTBYTESIZE);
                    }
                }
            }
        }
        return webAPIManager;
    }

    /**
     * 销毁线程池，在application的onTerminate里面调用
     */
    public void onDestroyThreadPool() {
        if (requestDispatchThreadPoolManager.getExecutorService() == null
                || requestDispatchThreadPoolManager.isShutdown()) {
            return;
        }
        if (!requestDispatchThreadPoolManager.getExecutorService().isShutdown()) {
            requestDispatchThreadPoolManager.recycleThreadPool();
        }
    }

//    /**
//     * 全部头都加入clientId和appVersion device和os
//     */
//    private Map<String, Object> getDefautlHeader() {
//        Map<String, Object> headers = new HashMap<String, Object>();
//        headers.put(WebAPI.KEY_CLIENT_ID, clientId);
//        headers.put(WebAPI.KEY_APP_VERSION, appVersion);
//        headers.put(WebAPI.KEY_OS, "Android " + android.os.Build.VERSION.RELEASE);
//        headers.put(WebAPI.KEY_DEVICE, android.os.Build.MODEL);
//        headers.put(WebAPI.KEY_TOKEN, accessToken);
//        return headers;
//    }

    /*******************************************
     * user api
     **************************************/
// 登录
//    public void login(int usertype, String phone, String password, WebResponseHandler<User> handler) {
//        String url = WebAPI.BASE_URL + WebAPI.API_LOGIN;
//
//        Map<String, Object> headers = getDefautlHeader();
//
//        Map<String, Object> params = new HashMap<String, Object>();
//        params.put(WebAPI.KEY_USER_TYPE, usertype);
//        params.put(WebAPI.KEY_PHONE, phone);
//        params.put(WebAPI.KEY_PASSWORD, password);
//
//        startExcuteJson(WebAPI.METHOD_POST, url, headers, params, handler, new UserParser());
//    }

    /**
     * execute http request in new threadPOOL
     */
    private <T> void startExcute(final String method, final String url, final Map<String, Object> headers, final Map<String, Object> params, final
    WebResponseHandler<T> handler, final WebResponseParser<T> webResponseParser) {

        startExcute(method, url, headers, params, null, null, handler, webResponseParser);

    }

    private <T> void startExcute(final String method, final String url, final Map<String, Object> headers, final Map<String, Object> params, final String
            fileKey, final String filePath, final WebResponseHandler<T> handler, final WebResponseParser<T> webResponseParser) {
        startExcute(method, url, headers, params, fileKey, filePath, handler, webResponseParser, false);
    }

    private <T> void startExcuteJson(final String method, final String url, final Map<String, Object> headers, final Map<String, Object> params, final
    WebResponseHandler<T> handler, final WebResponseParser<T> webResponseParser) {

        startExcuteJson(method, url, headers, params, null, null, handler, webResponseParser);

    }

    private <T> void startExcuteJson(final String method, final String url, final Map<String, Object> headers, final Map<String, Object> params, final String
            fileKey, final String filePath, final WebResponseHandler<T> handler, final WebResponseParser<T> webResponseParser) {
        startExcute(method, url, headers, params, fileKey, filePath, handler, webResponseParser, true);
    }

    private <T> void startExcute(final String method, final String url, final Map<String, Object> headers, final Map<String, Object> params, final String
            fileKey, final String filePath, final WebResponseHandler<T> handler, final WebResponseParser<T> webResponseParser, final boolean isJson) {

        handler.sendStart();
        if (requestDispatchThreadPoolManager.getExecutorService() == null) {
            handler.sendError(new NullPointerException("thread pool happen null point exception"));
        } else if (requestDispatchThreadPoolManager.getExecutorService() != null && requestDispatchThreadPoolManager.isShutdown()) {
            handler.sendError(new RuntimeException("executorService cant been shutdown"));
        } else {
            requestDispatchThreadPoolManager.getExecutorService().submit(new Runnable() {
                @Override
                public void run() {
                    WebResponse<T> webResponse = null;
                    try {
                        byte[] data = null;
                        if (WebAPI.METHOD_POST.equals(method)) {
                            if (fileKey == null || fileKey.equals("") || filePath == null || filePath.equals("")) {
                                data = WebConnectionManager.getInstance().post(url, headers, params, isJson);
                            } else {
                                data = WebConnectionManager.getInstance().upload(url, headers, params, fileKey, filePath, null);
                            }
                        }
                        if (WebAPI.METHOD_GET.equals(method)) {
                            data = WebConnectionManager.getInstance().get(url, headers, params);
                        }
                        // handler & parser
                        String json = new String(data, "UTF-8");
                        webResponse = parserResponse(json);

                            if (WebResponse.CODE_SUCCESS.equals(webResponse.getCode())) {
                                if (webResponseParser != null && webResponse.getResult() != null && webResponse.isHaveResultObject() == true) {
                                    webResponseParser.parse(webResponse);
                                }
                                handler.sendSuccess(webResponse);
                            } else {
                                log(method, url, headers, params, webResponse, null, null);
                                handler.sendFailure(webResponse);
                            }
                    } catch (HttpException he) {
                        log(method, url, headers, params, webResponse, he, null);
                        handler.sendError(he);
                    } catch (Exception e) {
                        log(method, url, headers, params, webResponse, null, e);
                        handler.sendError(e);
                    } finally {
                        handler.sendFinish();
                    }
                }
            });
        }
//
    }

    @SuppressWarnings("unused")
    private <T> void startExcuteForUploads(final String method, final String url, final Map<String, Object> headers, final Map<String, Object> params, final
    Map<String, Object> fileKeyAndPath, final WebResponseHandler<T> handler, final WebResponseParser<T> webResponseParser, final boolean isJson) {

        handler.sendStart();
        if (requestDispatchThreadPoolManager.getExecutorService() == null) {
            handler.sendError(new NullPointerException("thread pool happen null point exception"));
        } else if (requestDispatchThreadPoolManager.getExecutorService() != null && requestDispatchThreadPoolManager.isShutdown()) {
            handler.sendError(new RuntimeException("executorService cant been shutdown"));
        } else {
            requestDispatchThreadPoolManager.getExecutorService().submit(new Runnable() {
                @Override
                public void run() {
                    WebResponse<T> webResponse = null;
                    try {
                        byte[] data = null;
                        if (WebAPI.METHOD_POST.equals(method)) {
                            if (fileKeyAndPath == null || fileKeyAndPath.equals("")) {
                                data = WebConnectionManager.getInstance().post(url, headers, params, isJson);
                            } else {
                                data = WebConnectionManager.getInstance().uploads(url, headers, params, fileKeyAndPath, null);
                            }
                        }
                        if (WebAPI.METHOD_GET.equals(method)) {
                            data = WebConnectionManager.getInstance().get(url, headers, params);
                        }
                        // handler & parser
                        String json = new String(data);
                        webResponse = parserResponse(json);
                        if (WebResponse.CODE_SUCCESS.equals(webResponse.getCode())) {
                            if (webResponseParser != null) {
                                webResponseParser.parse(webResponse);
                            }
                            handler.sendSuccess(webResponse);
                        } else {
                            log(method, url, headers, params, webResponse, null, null);
                            handler.sendFailure(webResponse);
                        }
                    } catch (HttpException he) {
                        log(method, url, headers, params, webResponse, he, null);
                        handler.sendError(he);
                    } catch (Exception e) {
                        log(method, url, headers, params, webResponse, null, e);
                        handler.sendError(e);
                    } finally {
                        handler.sendFinish();
                    }
                }
            });
//
        }
    }

    /**
     * parser response
     * <p/>
     * 拆分code、message、result
     *
     * @throws JSONException
     */
    private <T> WebResponse<T> parserResponse(String json) throws JSONException {
        WebResponse<T> webResponse = new WebResponse<T>();
        Log.i("jsonString", json);
        JsonObject jsonObj = new JsonParser().parse(json).getAsJsonObject();
        // 第三方服务器解析
        if (!jsonObj.has(WebAPI.KEY_MESSAGE) || !jsonObj.has(WebAPI.KEY_CODE)) {
            webResponse.setCode(WebResponse.CODE_SUCCESS);
            webResponse.setMessage("第三方");
            webResponse.setResult(json);
            return webResponse;
        }

        // 业务服务器解析
        String code = jsonObj.get(WebAPI.KEY_CODE).getAsString();
        String message = jsonObj.get(WebAPI.KEY_MESSAGE).getAsString();
        //json元素
        JsonElement je = jsonObj.get(WebAPI.KEY_RESULT);

        webResponse.setCode(code);
        webResponse.setMessage(message);
        if (je != null) {
            webResponse.setResult(je.toString());
            webResponse.setHaveResultObject(true);
        } else {
            webResponse.setResult(json);
            webResponse.setHaveResultObject(false);
        }
        return webResponse;
    }

    private <T> WebResponse<T> parserResponseForGaoDe(String json) throws JSONException {
        WebResponse<T> webResponse = new WebResponse<T>();
        Log.i("jsonString", json);
        JsonObject jsonObj = new JsonParser().parse(json).getAsJsonObject();
        // 业务服务器解析
        String code = jsonObj.get(WebAPI.KEY_CODE).getAsString();
        String message = jsonObj.get(WebAPI.KEY_MESSAGE).getAsString();
        webResponse.setCode(code);
        webResponse.setMessage(message);
        webResponse.setResult(json);
        return webResponse;
    }

    private String log(String method, String url, Map<String, Object> headers, Map<String, Object> params, WebResponse<?> response, HttpException he,
                       Exception e) {

        StringBuffer sb = new StringBuffer();

        // 错误类别
        if (response != null) {
            sb.append("【Service Error】\n");
        } else if (he != null) {
            sb.append("【Network Error】\n");
        } else if (e != null) {
            sb.append("【Unknown Error】\n");
        }
        if (response != null) {
            sb.append("Code: " + response.getCode() + "\n");
            sb.append("Message: " + response.getMessage() + "\n");
            sb.append("Content: " + response.getResult() + "\n");
        }
        if (he != null) {
            Throwable t = he.getCause() == null ? he : he.getCause();
            sb.append("Error By: " + t.getClass().getName() + ":" + t.getMessage() + "\n");
        }
        if (e != null) {
            Throwable t = e.getCause() == null ? e : e.getCause();
            sb.append("Error By: " + t.getClass().getName() + ":" + t.getMessage() + "\n");
        }

        // 请求方式
        sb.append("Method: " + method + "\n");

        // url
        if (WebAPI.METHOD_GET.equals(method) || WebAPI.METHOD_DELETE.equals(method)) {
            String urlParams = WebHelper.encodeParams(params);
            url += urlParams == null ? "" : "?" + urlParams;
        }
        sb.append("URL: " + url + "\n");

        Object value = null;
        // headers
        if (headers != null) {
            for (String key : headers.keySet()) {
                value = headers.get(key);
                if (value == null || value.equals("")) {
                    continue;
                }
                sb.append("Header: " + key + " = ");
                sb.append(value + "\n");
            }
        }
        // params
        if (params != null) {
            for (String key : params.keySet()) {
                value = params.get(key);
                if (value == null || value.equals("")) {
                    continue;
                }
                sb.append("Params: " + key + " = ");
                if (WebAPI.KEY_PASSWORD.equals(key)) {
                    sb.append("******\n");
                } else {
                    sb.append(value + "\n");
                }
            }
        }

        if (isDetail) {
            if (he != null) {
                sb.append("Error Detail: ");
                Throwable t = he.getCause() == null ? he : he.getCause();
                for (StackTraceElement ste : t.getStackTrace()) {
                    sb.append(ste + "\n");
                }
            } else if (e != null) {
                sb.append("Error Detail: ");
                Throwable t = e.getCause() == null ? e : e.getCause();
                for (StackTraceElement ste : t.getStackTrace()) {
                    sb.append(ste + "\n");
                }
            }
        }

        Log.e(tag, sb.toString());

        // 写到文件中
//        LogUtil.log2File(MyConstant.PATH + "/log_web", sb.toString());
        return sb.toString();
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getAppVersion() {
        return appVersion;
    }

    public void setAppVersion(String appVersion) {
        this.appVersion = appVersion;
    }

}
