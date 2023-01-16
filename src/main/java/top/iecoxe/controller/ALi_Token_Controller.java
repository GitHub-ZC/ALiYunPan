package top.iecoxe.controller;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import top.iecoxe.entity.ALi_Token;

import javax.annotation.Resource;

/**
 * 获取阿里云盘登录身份的信息
 * @author IMAX-拾荒者
 */
@RestController
public class ALi_Token_Controller {
    @Resource
    private ALi_Token aLi_token;

    @GetMapping("/getToken")
    public ALi_Token getAliToken() {
        return aLi_token;
    }
}
