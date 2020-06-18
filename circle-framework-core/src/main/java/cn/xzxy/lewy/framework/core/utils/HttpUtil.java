package cn.xzxy.lewy.framework.core.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.http.*;
import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.client.HttpResponseException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.AbstractResponseHandler;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpRequestRetryHandler;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.springframework.util.CollectionUtils;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author lewy95
 **/
@Slf4j
public class HttpUtil {
    /**
     * 默认设置超时时间10秒
     */
    private static final int CONNECTION_TIMEOUT = 10;

    private static final RequestConfig REQUEST_CONFIG_TIME_OUT = RequestConfig.custom()
            .setConnectTimeout(CONNECTION_TIMEOUT * 1000)
            .setConnectionRequestTimeout(CONNECTION_TIMEOUT * 1000)
            .setSocketTimeout(CONNECTION_TIMEOUT * 1000)
            .build();

    /**
     * http响应返回非200报错handler
     */
    private static AbstractResponseHandler<String> http200BasicResponseHandler = new AbstractResponseHandler<String>() {
        @Override
        public String handleEntity(final HttpEntity entity) throws IOException {
            return EntityUtils.toString(entity, StandardCharsets.UTF_8.name());
        }

        @Override
        public String handleResponse(final HttpResponse response) throws IOException {
            final StatusLine statusLine = response.getStatusLine();
            final HttpEntity entity = response.getEntity();
            if (statusLine.getStatusCode() != HttpStatus.SC_OK) {
                EntityUtils.consume(entity);
                throw new HttpResponseException(statusLine.getStatusCode(), statusLine.getReasonPhrase());
            }
            return entity == null ? null : handleEntity(entity);
        }
    };

    /**
     * 默认请求头 do post
     *
     * @param requestUrl  requestUrl
     * @param requestJson requestJson
     * @return result
     */
    public static String doPostRequest(String requestUrl, String requestJson) {
        Header header = new BasicHeader("Content-Type", "application/json");
        return doPostRequest(requestUrl, requestJson, header);
    }

    /**
     * 发送http请求 do post
     *
     * @param requestUrl requestUrl
     * @param header     header
     * @return result
     */
    public static String doPostRequest(String requestUrl, Header header) {
        return doPostRequest(requestUrl, "{}", header);
    }

    /**
     * 发送http post请求
     *
     * @param requestUrl  请求url地址
     * @param requestJson 请求json参数
     * @param header      请求header头
     * @return result
     */
    public static String doPostRequest(String requestUrl, String requestJson, Header header) {
        try (CloseableHttpClient httpClient = getClient(true)) {
            URL url = new URL(requestUrl);
            URI uri = new URI(url.getProtocol(), url.getUserInfo(), url.getHost(), url.getPort(), url.getPath(), url.getQuery(), null);
            HttpUriRequest request = RequestBuilder.post(uri)
                    .setEntity(new StringEntity(requestJson, ContentType.APPLICATION_JSON))
                    .setCharset(java.nio.charset.Charset.defaultCharset())
                    .setHeader(header)
                    .build();
            return httpClient.execute(request, http200BasicResponseHandler, HttpClientContext.create());
        } catch (IOException | URISyntaxException ex) {
            log.error(ex.getMessage(), ex.getCause());
            return null;
        }
    }

    /**
     * 发送http get 请求
     *
     * @param url     请求url地址
     * @param params  请求json参数
     * @param headers 请求header头
     * @return result
     */
    public static String doGetRequest(String url, Map<String, String> params, Map<String, String> headers) {

        //1.获得一个httpclient对象
        CloseableHttpClient httpclient = getClient(true);
        List<NameValuePair> queryParams = new ArrayList<>();

        try {

            URIBuilder uriBuilder = new URIBuilder(url);
            if (!CollectionUtils.isEmpty(params)) {
                for (Map.Entry<String, String> entry : params.entrySet()) {
                    queryParams.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
                    uriBuilder.setParameters(queryParams);
                }
            }
            //2.生成一个get请求
            HttpGet httpget = new HttpGet(uriBuilder.build());
            if (!CollectionUtils.isEmpty(params)) {
                for (Map.Entry<String, String> entry : headers.entrySet()) {
                    httpget.addHeader(entry.getKey(), entry.getValue());
                }
            }
            //3.执行get请求并返回结果
            CloseableHttpResponse response = httpclient.execute(httpget);
            String content = EntityUtils.toString(response.getEntity(), "UTF-8");
            if (log.isDebugEnabled()) {
                log.debug("返回结果 {}", content);
            }
            response.close();
            return content;
        } catch (URISyntaxException | IOException e) {
            log.error("{}", e);
        }
        return null;
    }


    private static CloseableHttpClient getClient(boolean isPooled) {
        HttpRequestRetryHandler retryHandler = new DefaultHttpRequestRetryHandler();
        if (isPooled) {
            return HttpClients.custom()
                    .setRetryHandler(retryHandler)
                    .setDefaultRequestConfig(REQUEST_CONFIG_TIME_OUT)
                    .build();
        }
        return HttpClients.createDefault();
    }
}
