package com.px.common.http.interceptors;

import android.text.TextUtils;

import com.px.common.utils.Logger;
import com.px.common.utils.SPUtil;

import java.io.IOException;
import java.util.List;

import okhttp3.Headers;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by patrick on 06/01/2018.
 * create time : 9:58 AM
 */

public class SessionInterceptor implements Interceptor {

    private static final String KEY = "cookie";

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        Request.Builder requestBuilder = request.newBuilder();
        String requestUrl = request.url().toString();
        Response response;
        try{
            String tag = request.tag().toString();
            if(TextUtils.isEmpty(tag) || !"download".equals(tag)) {
                String[] urls = requestUrl.split("/");
                String web;
                if (urls.length >= 4) {
                    web = urls[2] + urls[3];
                } else {
                    web = requestUrl;
                }
//                Logger.d(web);
                String sessionId = (String) SPUtil.get(web + KEY, "");
                if (!TextUtils.isEmpty(sessionId)) {
                    requestBuilder.addHeader("Cookie", sessionId);
                }

                response = chain.proceed(requestBuilder.build());
                Headers headers = response.headers();
                List<String> cookies = headers.values("Set-Cookie");
                if(cookies != null && cookies.size() > 0 ) {
                    String session = cookies.get(0);
                    String sessionId1 = session.substring(0, session.indexOf(";"));
                    SPUtil.put(web + KEY, sessionId1);
                }
                return response;
            }
        }catch(Exception e){
            Logger.d(e.getLocalizedMessage());
            return chain.proceed(request);
        }
        return chain.proceed(request);
    }
}
