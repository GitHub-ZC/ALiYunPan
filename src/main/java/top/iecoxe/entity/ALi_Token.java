package top.iecoxe.entity;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;


/**
 * ALiYunPan 登录身份信息
 * @author IMAX-拾荒者
 */
@Component
@Data
@AllArgsConstructor
@ToString
@NoArgsConstructor
@ConfigurationProperties(prefix = "aliyun")
public class ALi_Token {
    private String access_token;

    @Value("${aliyun.refresh_token}")
    private String refresh_token;
    private String user_id;
    private String device_id;
    private String avatar;
    private String user_name;
    private String default_drive_id;
}
