package com.lad.chestnut.util;

import io.netty.util.CharsetUtil;
import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Map;


public class HttpUtils {

    private final static Logger LOGGER = LoggerFactory.getLogger(HttpUtils.class);

    public static String doHttpGet(String url) {
        return doHttpGet(url, null);
    }

    public static String doHttpGet(String url, Map<String, String> headers) {
        LOGGER.info("doHttpGet url: {}, headers: {}", url, headers);
        CloseableHttpClient client = HttpClients.custom().build();
        try {
            HttpGet httpGet = new HttpGet(StringUtils.trim(url));
            httpGet.setConfig(getRequestConfig());
            if (headers != null) {
                for (Map.Entry<String, String> entry : headers.entrySet()) {
                    httpGet.setHeader(entry.getKey(), entry.getValue());
                }
            }
            HttpResponse httpResponse = client.execute(httpGet);
            return getResponseBody(httpResponse);
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            try {
                client.close();
            } catch (IOException e) {
                LOGGER.error("系统异常-关闭流失败", e);
            }
        }
    }
    public static String doHttpPost(String url, String body) {
        return doHttpPost(url, body, null);
    }

    public static String doHttpPost(String url, String body, Map<String, String> headers) {
        LOGGER.info("doHttpPost url: {}, body{}", url, body);
        CloseableHttpClient client = HttpClients.custom().build();
        try {
            HttpPost httpPost = new HttpPost(StringUtils.trim(url));
            if (headers != null) {
                for (Map.Entry<String, String> entry : headers.entrySet()) {
                    httpPost.setHeader(entry.getKey(), entry.getValue());
                }
            }
            httpPost.setConfig(getRequestConfig());
            httpPost.setEntity(new StringEntity(body, CharsetUtil.UTF_8));
            HttpResponse httpResponse = client.execute(httpPost);
            return getResponseBody(httpResponse);
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            try {
                client.close();
            } catch (IOException e) {
                LOGGER.error("系统异常-关闭流失败", e);
            }
        }
    }

    private static RequestConfig getRequestConfig() {
        RequestConfig requestConfig = RequestConfig.custom()
                .setSocketTimeout(60000)
                .setConnectTimeout(60000)
                .setConnectionRequestTimeout(60000)
                .setExpectContinueEnabled(false).build();
        return requestConfig;
    }

    private static String getResponseBody(HttpResponse httpResponse) throws Exception {
        String responseContent;
        StatusLine statusLine = httpResponse.getStatusLine();
        HttpEntity entity = httpResponse.getEntity();
        if (entity != null && httpResponse.getStatusLine().getStatusCode() == HttpStatus.OK.value()) {
            responseContent = EntityUtils.toString(entity, CharsetUtil.UTF_8);
            if (!StringUtils.isEmpty(responseContent)) {
                responseContent = responseContent.replace("\r\n", "");
            }
            LOGGER.info("status: {}, content: {}", statusLine, responseContent);
        } else {
            LOGGER.error("status: {}", statusLine);
            throw new Exception(httpResponse.getStatusLine().toString());
        }
        EntityUtils.consumeQuietly(entity);
        return responseContent;
    }

    public static String getIp(HttpServletRequest request) {
        String unknown = "unknown";
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || unknown.equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || unknown.equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || unknown.equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        if (ip != null) {
            ip = ip.split(",")[0];
        }
        return ip;
    }
}
