package top.iecoxe.controller;


import com.alibaba.fastjson2.JSONObject;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import top.iecoxe.entity.ALi_Token;
import top.iecoxe.utils.ALi_CodeUtil;

import javax.annotation.Resource;
import java.math.BigInteger;

@RestController
public class ALi_Intermediate_Data_Controller {
    @Resource
    private ALi_Token aLi_token;

    @GetMapping("/intermediate_data")
    public JSONObject intermediate_data(@RequestParam("filesize") Long filesize) {
        String dec = ALi_CodeUtil.radixToNum(ALi_CodeUtil.MD5(aLi_token.getAccess_token()).substring(0, 16), 16);
        BigInteger bigInteger = new BigInteger(dec);

        long offset = 0L;
        if (filesize > 0) {
            offset = bigInteger.mod(BigInteger.valueOf(filesize)).longValue();
        }

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("offset", offset);

        return jsonObject;
    }
}
