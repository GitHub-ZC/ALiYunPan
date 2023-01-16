package top.iecoxe.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import top.iecoxe.entity.ALi_Token;

import javax.annotation.Resource;
import java.io.File;


@SpringBootTest(properties = {
        "ali_token.refresh_token=15cdd78ea19f46239c4ad8be6d1dbc32",
        "ali_token.access_token=eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VySWQiOiIzNDI3ZDg2YmQyZGY0YWMyYmI3M2RmYTdlMjVkNzc3MyIsImN1c3RvbUpzb24iOiJ7XCJjbGllbnRJZFwiOlwicEpaSW5OSE4yZFpXazhxZ1wiLFwiZG9tYWluSWRcIjpcImJqMjlcIixcInNjb3BlXCI6W1wiRFJJVkUuQUxMXCIsXCJGSUxFLkFMTFwiLFwiVklFVy5BTExcIixcIlNIQVJFLkFMTFwiLFwiU1RPUkFHRS5BTExcIixcIlNUT1JBR0VGSUxFLkxJU1RcIixcIlVTRVIuQUxMXCIsXCJCQVRDSFwiLFwiQUNDT1VOVC5BTExcIixcIklNQUdFLkFMTFwiLFwiSU5WSVRFLkFMTFwiLFwiU1lOQ01BUFBJTkcuTElTVFwiXSxcInJvbGVcIjpcInVzZXJcIixcInJlZlwiOlwiXCIsXCJkZXZpY2VfaWRcIjpcImI3ZTEyM2JjYTRhMjRjMTI5YTkyNWVkNWFlYWE3OGI4XCJ9IiwiZXhwIjoxNjcyOTMxNjc4LCJpYXQiOjE2NzI5MjQ0MTh9.cErsEXSXGwLOFg61Aw4-BB5Nd8918tlAIEzQCOYFSu4XG2ZJrPsdj2kNSgMHzvB1qKhR2WdboJY6kifHzBG7_lzIsb_Z6SLgn2nGeJj_BdqRdnTDbqB7_ZQI_s1Or4ylz6HlodWwGyQ63s-s3mUjHl3yQ7wnrwg20VEeUYMJP18",
        "ali_token.default_drive_id=1036697510"
})
public class ALi_Update_FileTest {
    @Resource
    private ALi_Token aLi_token;

    @Resource
    private ALi_Update_File aLi_update_file;

    @Value("${ali_token.access_token}")
    private String access_token;

    @Value("${ali_token.refresh_token}")
    private String refresh_token;

    @Value("${ali_token.default_drive_id}")
    private String default_drive_id;

    @Test
    public void updateFile() {
        aLi_token.setAccess_token(access_token);
        aLi_token.setRefresh_token(refresh_token);
        aLi_token.setDefault_drive_id(default_drive_id);

        boolean b = aLi_update_file.updateFile(new File("C:\\Users\\ZC\\Desktop\\tool\\volumes\\data\\xxl-job\\xxl-job\\xxl-job-admin.log.2022-12-23.zip"), "639ab32aff146e5b69c64972a52f02f2a47f7c9c");
        if(b) {
            System.out.println("上传成功");
        } else {
            System.out.println("上传失败");
        }
    }
}