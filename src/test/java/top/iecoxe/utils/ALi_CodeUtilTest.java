package top.iecoxe.utils;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.File;

@SpringBootTest
public class ALi_CodeUtilTest {

    @Test
    public void proofCode() {
        String proofCode = ALi_CodeUtil.proofCode("eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VySWQiOiIzNDI3ZDg2YmQyZGY0YWMyYmI3M2RmYTdlMjVkNzc3MyIsImN1c3RvbUpzb24iOiJ7XCJjbGllbnRJZFwiOlwicEpaSW5OSE4yZFpXazhxZ1wiLFwiZG9tYWluSWRcIjpcImJqMjlcIixcInNjb3BlXCI6W1wiRFJJVkUuQUxMXCIsXCJGSUxFLkFMTFwiLFwiVklFVy5BTExcIixcIlNIQVJFLkFMTFwiLFwiU1RPUkFHRS5BTExcIixcIlNUT1JBR0VGSUxFLkxJU1RcIixcIlVTRVIuQUxMXCIsXCJCQVRDSFwiLFwiQUNDT1VOVC5BTExcIixcIklNQUdFLkFMTFwiLFwiSU5WSVRFLkFMTFwiLFwiU1lOQ01BUFBJTkcuTElTVFwiXSxcInJvbGVcIjpcInVzZXJcIixcInJlZlwiOlwiXCIsXCJkZXZpY2VfaWRcIjpcIjI4NzdlNDYzZmNjYjRhYTliMjY0NWI4N2ZkMTk4YTMyXCJ9IiwiZXhwIjoxNjcyOTgyMTE3LCJpYXQiOjE2NzI5NzQ4NTd9.C0Esu6aVMvNCcpyY5kNrfzS52OxTicJBwW-AweTp_QFTAPnz6zhs_WTujA5aq-wT6lBqWn5z3muL6unLMmFk3aT-2HnfCfEx7WpvKAze_E9U3sGxfv8hxP3Cd3ljSGlNLHoZoIF7JD2ZqE4buznbSJ0rAePeZW2-jkqhHYmrlYc", new File("C:\\Users\\ZC\\Desktop\\tool\\hash.js"));
        System.out.println(proofCode);
    }

    @Test
    public void contentCode() {
        String contentCode = ALi_CodeUtil.contentCode(new File("C:\\Users\\ZC\\Desktop\\tool\\hash.js"));
        System.out.println(new File("C:\\Users\\ZC\\Desktop\\微信支付\\证书使用说明.txt").length());
        System.out.println(contentCode);
    }
}