package top.iecoxe.utils;

import org.apache.http.Consts;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.config.ConnectionConfig;
import org.apache.http.config.SocketConfig;
import org.apache.http.entity.FileEntity;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import java.io.File;
import java.io.IOException;
import java.nio.charset.CodingErrorAction;

/**
 * 上传文件到阿里云盘(暂时不提供API接口)
 * @author IMAX-拾荒者
 * @date 2023-01-05
 */
public class ALi_HttpClientUtil {

    /** 从连接池中获取连接的超时时间（单位：ms） */
    private static final int CONNECTION_REQUEST_TIMEOUT = 5000;

    /** 与服务器连接的超时时间（单位：ms） */
    private static final int CONNECTION_TIMEOUT = 5000;

    /** 从服务器获取响应数据的超时时间（单位：ms） */
    private static final int SOCKET_TIMEOUT = 10000;

    /** 连接池的最大连接数 */
    private static final int MAX_CONN_TOTAL = 100;

    /** 每个路由上的最大连接数 */
    private static final int MAX_CONN_PER_ROUTE = 50;

    /** 用户代理配置 */
    private static final String USER_AGENT = "Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:108.0) Gecko/20100101 Firefox/108.0";

    /** HttpClient对象 */
    private static CloseableHttpClient httpClient = null;


    static {
        init();
    }

    /**
     * 全局对象初始化
     */
    private static void init() {
        // 创建Connection配置对象
        /* Connection配置对象 */
        ConnectionConfig connectionConfig = ConnectionConfig.custom()
                .setMalformedInputAction(CodingErrorAction.IGNORE)
                .setUnmappableInputAction(CodingErrorAction.IGNORE)
                .setCharset(Consts.UTF_8).build();

        // 创建Socket配置对象
        /* Socket配置对象 */
        SocketConfig socketConfig = SocketConfig.custom().setTcpNoDelay(true).build();

        // 创建Request配置对象
        /* Request配置对象 */
        RequestConfig requestConfig = RequestConfig.custom()
                .setConnectionRequestTimeout(CONNECTION_REQUEST_TIMEOUT)
                .setConnectTimeout(CONNECTION_TIMEOUT)
                .setSocketTimeout(SOCKET_TIMEOUT)
                .build();

        // 创建Cookie存储对象（服务端返回的Cookie保存在CookieStore中，下次再访问时才会将CookieStore中的Cookie发送给服务端）
        /* Cookie存储对象 */
        BasicCookieStore cookieStore = new BasicCookieStore();

        // 创建HttpClient对象
        httpClient = HttpClients.custom()
                .setDefaultConnectionConfig(connectionConfig)
                .setDefaultSocketConfig(socketConfig)
                .setDefaultRequestConfig(requestConfig)
                .setDefaultCookieStore(cookieStore)
                .setUserAgent(USER_AGENT)
                .setMaxConnTotal(MAX_CONN_TOTAL)
                .setMaxConnPerRoute(MAX_CONN_PER_ROUTE)
                .build();
    }


    public static void uploadFile(String url, File file){
        try {
            HttpPut httpPut = new HttpPut(url);
            FileEntity fileEntity = new FileEntity(file);
            httpPut.setEntity(fileEntity);

            CloseableHttpResponse httpResponse = httpClient.execute(httpPut);
            int statusCode= httpResponse.getStatusLine().getStatusCode();
            if(statusCode != 200){
                return;
            }
            httpResponse.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
