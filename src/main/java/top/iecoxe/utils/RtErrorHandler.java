package top.iecoxe.utils;

import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.DefaultResponseErrorHandler;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


/**
 * 功能：捕获RestTemplate异常
 * @author IMAX-拾荒者
 */
public class RtErrorHandler extends DefaultResponseErrorHandler {
    @Override
    public boolean hasError(ClientHttpResponse response) throws IOException {
        return super.hasError(response);
    }

    @Override
    public void handleError(ClientHttpResponse response) throws IOException {

        HttpStatus statusCode = HttpStatus.resolve(response.getRawStatusCode());
        List<HttpStatus> donotDeal = new ArrayList<>(); // 白名单
        donotDeal.add(HttpStatus.UNAUTHORIZED);
        donotDeal.add(HttpStatus.BAD_REQUEST);
        donotDeal.add(HttpStatus.NOT_FOUND);
        donotDeal.add(HttpStatus.CREATED);
        donotDeal.add(HttpStatus.NO_CONTENT);

        if (!donotDeal.contains(statusCode)) {
            // 非白名单则处理
            super.handleError(response);
        }
    }
}
