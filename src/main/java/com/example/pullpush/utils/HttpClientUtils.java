package com.example.pullpush.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.google.common.base.Objects;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.ssl.SSLContexts;
import org.apache.http.util.EntityUtils;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import javax.net.ssl.SSLContext;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 *
 * @author zhang tong
 * date: 2018/8/16 10:11
 * description:
 */
@Slf4j
public class HttpClientUtils {

    private HttpClientUtils() {

    }


    private static volatile HttpClientUtils INSTANCE = new HttpClientUtils();
    /**
     * 最大尝试次数
     */
    private static final int MAX_RETRY = 0;


    private static synchronized void syncInit() {
        if (INSTANCE == null) {
            INSTANCE = new HttpClientUtils();
        }
    }

    public static HttpClientUtils getInstance() {
        if (INSTANCE == null) {
            syncInit();
        }
        return INSTANCE;
    }

    private CloseableHttpClient getHttpClient() {
//        return HttpClients.createDefault();
        try {
            SSLContext sslContext = SSLContexts.custom().loadTrustMaterial(null, (x509Certificates, s) -> true).build();
            return HttpClients.custom().setSSLContext(sslContext).
                    setSSLHostnameVerifier(new NoopHostnameVerifier()).build();
        } catch (Exception e) {
            log.error(e.getMessage());
            return HttpClients.createDefault();
        }


    }

    public boolean validatorHttpGet(String url) {
        long startTime = System.currentTimeMillis();
        CloseableHttpClient httpClient = null;
        CloseableHttpResponse response = null;
        try {
            httpClient = getHttpClient();

            HttpGet httpGet = createdHttpGetFrom(url, Collections.emptyMap(), Collections.emptyMap());
            // 执行请求访问
            response = httpClient.execute(httpGet);
            if (response != null) {
                int statusCode = response.getStatusLine().getStatusCode();
                System.out.println("statusCode = " + statusCode);
                return statusCode == HttpStatus.SC_OK;
            } else {
                log.error("操作失败，Request URL：{}", url);
                return false;
            }

        } catch (Exception e) {
            return false;
        } finally {
            close(response);
            close(httpClient);
            log.info("Request URL：{},响应时间：{}", url, (System.currentTimeMillis() - startTime));
        }
    }

    public String httpGet(String url, Map<String, Object> paramMap) {
        return httpRequest(url, null, paramMap, StandardCharsets.UTF_8, 0, HttpMethod.GET, MediaType.APPLICATION_FORM_URLENCODED_VALUE);
    }

    public void httpGetDownFile(String url, Map<String, String> headerMap, Map<String, Object> paramMap, OutputStream out) {
        httpRequestDownFile(url, headerMap, paramMap, out);
    }

    public String httpPostUploadFile(String url, Map<String, String> headerMap, Map<String, Object> paramMap, HttpServletRequest request) {
        return httpRequestUploadFile(url, headerMap, paramMap, request, Collections.emptyList());
    }

    public String httpPostUploadFile(String url, Map<String, String> headerMap, Map<String, Object> paramMap, List<MultipartFile> multipartFiles) {
        return httpRequestUploadFile(url, headerMap, paramMap, null, multipartFiles);
    }

    public String httpPostFrom(String url, Map<String, Object> paramMap) {
        return httpRequest(url, null, paramMap, StandardCharsets.UTF_8, 0, HttpMethod.POST, MediaType.APPLICATION_FORM_URLENCODED_VALUE);
    }

    public String httpPostFrom(String url, Map<String, String> headerMap, Map<String, Object> paramMap) {
        return httpRequest(url, headerMap, paramMap, StandardCharsets.UTF_8, 0, HttpMethod.POST, MediaType.APPLICATION_FORM_URLENCODED_VALUE);
    }

    public String httpPostJson(String url, Map<String, Object> paramMap) {
        return httpRequest(url, null, paramMap, StandardCharsets.UTF_8, 0, HttpMethod.POST, MediaType.APPLICATION_JSON_VALUE);
    }

    public String httpPostJson(String url, Map<String, String> headerMap, Map<String, Object> paramMap) {
        return httpRequest(url, headerMap, paramMap, StandardCharsets.UTF_8, 0, HttpMethod.POST, MediaType.APPLICATION_JSON_VALUE);
    }

    /**
     * HttpPost请求，控制尝试次数
     */
    private String httpRequest(String url, Map<String, String> headerMap, Map<String, Object> paramMap, Charset charset, int reTry, HttpMethod httpMethod, String mediaType) {
        long startTime = System.currentTimeMillis();
        String paramJson = JSONObject.parseObject(JSON.toJSONString(paramMap, SerializerFeature.DisableCircularReferenceDetect)).toJSONString();
        log.info("请求URl：{},请求Method：{},请求头：{}，请求内容：{}", url, httpMethod.toString(), headerMap,  paramJson);
//        log.info("请求URl：{},请求Method：{},请求头：{}，请求内容：{}", url, httpMethod.toString(), headerMap, paramJson.length() > 10000 ? "" : paramJson);
        CloseableHttpClient httpClient = null;
        CloseableHttpResponse response = null;
        String result = null;
        try {
            httpClient = getHttpClient();
            //getJSONObject 请求
            if (httpMethod == HttpMethod.GET) {
                HttpGet httpGet = createdHttpGetFrom(url, headerMap, paramMap);
                // 执行请求访问
                response = httpClient.execute(httpGet);
            }
            //post 请求
            if (httpMethod == HttpMethod.POST) {
                HttpPost httpPost = null;
                //表单提交
                if (Objects.equal(mediaType, MediaType.APPLICATION_FORM_URLENCODED_VALUE)) {
                    httpPost = createdHttpPostFrom(url, headerMap, paramMap, charset);
                }
                //json提交
                if (Objects.equal(mediaType, MediaType.APPLICATION_JSON_VALUE)) {
                    httpPost = createdHttpPostJson(url, headerMap, paramJson, charset);
                }
                long executeStartTime = System.currentTimeMillis();
                // 执行请求访问
                response = httpClient.execute(httpPost);
//                log.info("执行请求响应时间：{}", (System.currentTimeMillis() - executeStartTime));
            }
            if (response != null) {
                int statusCode = response.getStatusLine().getStatusCode();
                if (statusCode == HttpStatus.SC_OK) {
                    HttpEntity entity = response.getEntity();
                    if (entity != null) {
                        result = EntityUtils.toString(entity, charset);
                    }
//                    log.info("返回结果为：{}", result);
                } else if (statusCode == HttpStatus.SC_MOVED_TEMPORARILY) {
                    log.error("访问地址已经改变请更新访问地址");
                } else {
                    log.error("操作失败，Request URL：{}, params：{}, httpStatus: {}", url, paramJson, statusCode);
                }
            } else {
                log.error("操作失败，Request URL：{}, params：{}", url, "暂无");
            }

        } catch (Exception e) {
            if (reTry < MAX_RETRY) {
                reTry++;
                log.error("请求失败，尝试再次请求：{},Request URL：{}, params：{}", reTry, url, paramJson);
                return httpRequest(url, headerMap, paramMap, charset, reTry, httpMethod, mediaType);
            } else {
                log.error("请求异常，已超出最大尝试次数：{}，Request URL：{}, params：{}, Exception:{}", MAX_RETRY, url, paramJson, e);
            }
        } finally {
            close(response);
            close(httpClient);
//            log.info("Request URL：{},响应时间：{}", url, (System.currentTimeMillis() - startTime));
        }
        return result;
    }

    private void httpRequestDownFile(String url, Map<String, String> headerMap, Map<String, Object> paramMap, OutputStream out) {
        long startTime = System.currentTimeMillis();
        String paramJson = JSONObject.parseObject(JSON.toJSONString(paramMap, SerializerFeature.DisableCircularReferenceDetect)).toJSONString();
        log.info("请求URl：{},请求头：{}，请求内容：{}", url, headerMap, paramJson);
        CloseableHttpClient httpClient = null;
        CloseableHttpResponse response = null;
        try {
            httpClient = getHttpClient();

            HttpGet httpGet = createdHttpGetFrom(url, headerMap, paramMap);
            // 执行请求访问
            response = httpClient.execute(httpGet);

            if (response != null) {
                int statusCode = response.getStatusLine().getStatusCode();
                if (statusCode == HttpStatus.SC_OK) {
                    HttpEntity entity = response.getEntity();
                    if (entity != null) {
                        IOUtils.copy(response.getEntity().getContent(), out);
                    }
                } else if (statusCode == HttpStatus.SC_MOVED_TEMPORARILY) {
                    // 302
                    log.error("访问地址已经改变请更新访问地址");
                } else {
                    log.error("操作失败，Request URL：{}, params：{}, httpStatus: {}", url, "暂无", statusCode);
                }
            } else {
                log.error("操作失败，Request URL：{}, params：{}", url, "暂无");
            }

        } catch (Exception e) {
            log.error("请求异常，Request URL：{}, params：{}, Exception:{}", url, "暂无", e);
        } finally {
            close(response);
            close(httpClient);
            log.info("响应时间：{}", (System.currentTimeMillis() - startTime));
        }
    }

    private String httpRequestUploadFile(String url, Map<String, String> headerMap, Map<String, Object> paramMap, HttpServletRequest request, List<MultipartFile> multipartFiles) {
        Charset charset = StandardCharsets.UTF_8;
        String paramJson = JSONObject.parseObject(JSON.toJSONString(paramMap, SerializerFeature.DisableCircularReferenceDetect)).toJSONString();
        log.info("请求URl：{},请求Method：{},请求头：{}，请求内容：{}", url, HttpMethod.POST.toString(), headerMap, paramJson);
        // 创建一个通用的多部分解析器
        long startTime = System.currentTimeMillis();
        CloseableHttpClient httpClient = null;
        CloseableHttpResponse response = null;
        String result = null;
        try {
            httpClient = getHttpClient();
            HttpPost httpPost = null;
            if (request != null) {
                httpPost = createdHttpPostFile(url, headerMap, paramMap, request, charset);
            } else if (multipartFiles != null && !multipartFiles.isEmpty()) {
                httpPost = createdHttpPostFile(url, headerMap, paramMap, multipartFiles, charset);
            }
            // 执行请求访问
            response = httpClient.execute(httpPost);

            if (response != null) {
                int statusCode = response.getStatusLine().getStatusCode();
                if (statusCode == HttpStatus.SC_OK) {
                    HttpEntity entity = response.getEntity();
                    if (entity != null) {
                        result = EntityUtils.toString(entity, charset);
                    }
                } else if (statusCode == HttpStatus.SC_MOVED_TEMPORARILY) {
                    // 302
                    log.error("访问地址已经改变请更新访问地址");
                } else {
                    log.error("操作失败，Request URL：{},  httpStatus: {}", url, statusCode);
                }
            } else {
                log.error("操作失败，Request URL：{}", url);
            }

        } catch (Exception e) {
            log.error("请求异常，Request URL：{}, params：{}, Exception:{}", url, "暂无", e);
        } finally {
            close(response);
            close(httpClient);
            log.info("响应时间：{}", (System.currentTimeMillis() - startTime));
        }
        return result;
    }


    private static HttpGet createdHttpGetFrom(String url,
                                              Map<String, String> headerMap,
                                              Map<String, Object> paramMap) throws URISyntaxException {

        URIBuilder builder = new URIBuilder(url);
        List<NameValuePair> parameters = createNameValuePair(paramMap);
        builder.setParameters(parameters);
        HttpGet httpGet = new HttpGet(builder.build());
        if (headerMap != null) {
            headerMap.forEach(httpGet::setHeader);
        }
        httpGet.setConfig(REQUEST_CONFIG);
        httpGet.setHeader(new BasicHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE));
        return httpGet;
    }

    private static HttpPost createdHttpPostFrom(String url,
                                                Map<String, String> headerMap,
                                                Map<String, Object> paramMap,
                                                Charset charset) {
        HttpPost httpPost = new HttpPost(url);
        httpPost.setConfig(REQUEST_CONFIG);
        if (headerMap != null) {
            headerMap.forEach(httpPost::setHeader);
        }
        List<NameValuePair> parameters = createNameValuePair(paramMap);
        httpPost.setHeader(new BasicHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE));
        HttpEntity paramEntity = new UrlEncodedFormEntity(parameters, charset);
        httpPost.setEntity(paramEntity);
        return httpPost;
    }

    private static HttpPost createdHttpPostFile(String url,
                                                Map<String, String> headerMap,
                                                Map<String, Object> paramMap,
                                                List<MultipartFile> multipartFiles,
                                                Charset charset) throws IOException {
        HttpPost httpPost = new HttpPost(url);
        httpPost.setConfig(REQUEST_CONFIG);
        if (headerMap != null) {
            headerMap.forEach(httpPost::setHeader);
        }
        // 取得request中的所有文件名
        MultipartEntityBuilder builder = createMultipartEntity(paramMap, multipartFiles, charset);
        HttpEntity httpEntity = builder.build();

        httpPost.setEntity(httpEntity);
        return httpPost;
    }

    private static HttpPost createdHttpPostFile(String url,
                                                Map<String, String> headerMap,
                                                Map<String, Object> paramMap,
                                                HttpServletRequest request,
                                                Charset charset) throws IOException {
        CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver(request.getServletContext());
        HttpPost httpPost = new HttpPost(url);
        httpPost.setConfig(REQUEST_CONFIG);
        if (headerMap != null) {
            headerMap.forEach(httpPost::setHeader);
        }
        MultipartEntityBuilder builder;
        if (multipartResolver.isMultipart(request)) {
            // 转换成多部分request
            MultipartHttpServletRequest multiRequest = (MultipartHttpServletRequest) request;
            // 取得request中的所有文件名
            List<MultipartFile> multipartFiles = Lists.newArrayList(multiRequest.getFileMap().values());
            builder = createMultipartEntity(paramMap, multipartFiles, charset);
        } else {
            builder = createMultipartEntity(paramMap, charset);
        }
        HttpEntity httpEntity = builder.build();

        httpPost.setEntity(httpEntity);
        return httpPost;
    }

    private static HttpPost createdHttpPostJson(String url,
                                                Map<String, String> headerMap,
                                                String paramJson,
                                                Charset charset) {
        HttpPost httpPost = new HttpPost(url);
        httpPost.setConfig(REQUEST_CONFIG);
        if (headerMap != null) {
            headerMap.forEach(httpPost::setHeader);
        }
        StringEntity stringEntity = new StringEntity(paramJson, charset);
        stringEntity.setContentEncoding(charset.toString());
        stringEntity.setContentType(MediaType.APPLICATION_JSON_VALUE);
        httpPost.setHeader(new BasicHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_UTF8_VALUE));
        httpPost.setEntity(stringEntity);
        return httpPost;
    }

    private static List<NameValuePair> createNameValuePair(Map<String, Object> paramMap) {
        List<NameValuePair> parameters = new ArrayList<>();
        if (paramMap != null) {
            for (Map.Entry<String, Object> entry : paramMap.entrySet()) {
                parameters.add(new BasicNameValuePair(entry.getKey(), entry.getValue() + ""));
            }
        }
        parameters.add(new BasicNameValuePair("livedCode", String.valueOf(System.currentTimeMillis())));
        return parameters;
    }

    private static MultipartEntityBuilder createMultipartEntity(Map<String, Object> paramMap,
                                                                List<MultipartFile> multipartFiles,
                                                                Charset charset) throws IOException {
        MultipartEntityBuilder builder = createMultipartEntity(paramMap, charset);
        for (MultipartFile multipartFile : multipartFiles) {
            String originalFilename = multipartFile.getOriginalFilename();
            // 文件流
            builder.addBinaryBody("file", multipartFile.getInputStream(), ContentType.MULTIPART_FORM_DATA, originalFilename);
            // 类似浏览器表单提交，对应input的name和value
            builder.addTextBody("fileName", originalFilename);
        }
        return builder;
    }

    private static MultipartEntityBuilder createMultipartEntity(Map<String, Object> paramMap,
                                                                Charset charset) {
        //BROWSER_COMPATIBLE自定义charset，RFC6532=utf-8，STRICT=iso-8859-1
        //此处一定要用RFC6532，网上普遍用的BROWSER_COMPATIBLE依然会出现中文名乱码
        MultipartEntityBuilder builder = MultipartEntityBuilder.create().setMode(HttpMultipartMode.RFC6532);
        if (paramMap != null) {
            for (Map.Entry<String, Object> entry : paramMap.entrySet()) {
                builder.addTextBody(entry.getKey(), entry.getValue().toString(),
                        ContentType.TEXT_PLAIN.withCharset(charset));
            }
        }
        return builder;
    }

    private static final RequestConfig REQUEST_CONFIG = RequestConfig
            .custom()
            .setCookieSpec(CookieSpecs.STANDARD)
            // 设置连接超时时间
            .setConnectTimeout(60000)
            // 设置请求超时时间
            .setConnectionRequestTimeout(60000)
            // 设置读数据超时时间(单位毫秒)
            .setSocketTimeout(60000)
            // 默认允许自动重定向
            .setRedirectsEnabled(true)
            .build();

    private static void close(CloseableHttpResponse httpResponse) {
        if (null != httpResponse) {
            try {
                httpResponse.close();
            } catch (IOException e) {
                log.error("关闭httpResponse出错，异常信息：{}", e.getMessage());
            }
        }
    }

    private static void close(CloseableHttpClient httpClient) {
        if (null != httpClient) {
            try {
                httpClient.close();
            } catch (IOException e) {
                log.error("关闭httpClient出错，异常信息：{}", e.getMessage());
            }
        }
    }

}
