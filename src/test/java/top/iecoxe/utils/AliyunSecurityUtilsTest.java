package top.iecoxe.utils;

import org.junit.jupiter.api.Test;

public class AliyunSecurityUtilsTest {

    @Test
    public void getSignatureHexStr() {
        String publicKeyHexStr = ALi_SecurityUtils.getPublicKeyHexStr();
        String a = ALi_SecurityUtils.getSignatureHexStr(
                "aliyundrive4j-appId",
                "m3UEHIuEKk8CAa5GsaUoNjMP", (String) "aliyundrive4j-userId", 0);
        System.out.println(publicKeyHexStr);
        System.out.println(a);
    }
}
