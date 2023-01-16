package top.iecoxe.scheduledTask;


import com.alibaba.fastjson2.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import top.iecoxe.entity.ALi_Token;

import javax.annotation.Resource;


/**
 * 定时刷新阿里云盘登录身份信息
 * @author IMAX-拾荒者
 */
@Component
public class ALi_RefreshToken_Task {
    @Resource
    private ALi_Token aLi_token;

    @Autowired
    private RestTemplate restTemplate;

    // 每隔一个小时执行一次
    @Scheduled(fixedRate = 3600000L)
    private void refreshToken() {
        // 构建你的请求头
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json;charset=UTF-8");
        headers.set("Referer", "https://www.aliyundrive.com/");

        // 构建请求体参数
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("refresh_token", aLi_token.getRefresh_token());
        jsonObject.put("grant_type", "refresh_token");

        // 组合请求头与请求体参数
        HttpEntity<String> requestEntity = new HttpEntity<>(jsonObject.toJSONString(), headers);

        // 发送刷新请求
        ResponseEntity<JSONObject> jsonObjectResponseEntity = restTemplate.postForEntity("https://auth.aliyundrive.com/v2/account/token", requestEntity, JSONObject.class);
        JSONObject body = jsonObjectResponseEntity.getBody();

        System.out.println(body.toJSONString());
        // 刷新登录成功 写入到实例缓存中
        if(jsonObjectResponseEntity.getStatusCodeValue() == 200) {
            aLi_token.setAccess_token(body != null ? body.getString("access_token") : null);
            aLi_token.setRefresh_token(body != null ? body.getString("refresh_token") : null);
            aLi_token.setAvatar(body != null ? body.getString("avatar") : null);
            aLi_token.setDevice_id(body != null ? body.getString("device_id") : null);
            aLi_token.setUser_id(body != null ? body.getString("user_id") : null);
            aLi_token.setUser_name(body != null ? body.getString("user_name") : null);
            aLi_token.setDefault_drive_id(body != null ? body.getString("default_drive_id") : null);
        }
    }
}
