package top.iecoxe.service;


import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import top.iecoxe.entity.ALi_Token;
import top.iecoxe.utils.ALi_CodeUtil;
import top.iecoxe.utils.ALi_HttpClientUtil;

import javax.annotation.Resource;
import java.io.*;
import java.util.LinkedHashMap;
import java.util.Objects;

/**
 * 上传文件到阿里云盘
 *
 * @author IMAX-拾荒者
 * @date 2023-01-03
 */
@Service
public class ALi_Update_File {
    @Autowired
    private RestTemplate restTemplate;

    @Resource
    private ALi_Token aLi_token;

    public boolean updateFile(File file, String parentId) {
        String proofCode = ALi_CodeUtil.proofCode(aLi_token.getAccess_token(), file);
        String contentHash = ALi_CodeUtil.contentCode(file);

        // 构建你的请求头
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", aLi_token.getAccess_token());
        headers.set("Referer", "https://www.aliyundrive.com/");

        // 构建请求体参数
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("drive_id", aLi_token.getDefault_drive_id());
        jsonObject.put("parent_file_id", parentId);
        jsonObject.put("name", file.getName());
        jsonObject.put("type", "file");
        jsonObject.put("check_name_mode", "auto_rename");
        jsonObject.put("size", file.length());
        jsonObject.put("create_scene", "");
        jsonObject.put("device_name", "");
        jsonObject.put("content_hash", contentHash);
        jsonObject.put("content_hash_name", "sha1");
        jsonObject.put("proof_code", proofCode);
        jsonObject.put("proof_version", "v1");

        JSONObject part_number = new JSONObject();
        part_number.put("part_number", 1);

        JSONArray part_info_list = new JSONArray();
        part_info_list.add(part_number);

        jsonObject.put("part_info_list", part_info_list);

        // 组合请求头与请求体参数
        HttpEntity<String> requestEntity = new HttpEntity<>(jsonObject.toJSONString(), headers);

        // 发送请求上传文件的请求
        ResponseEntity<JSONObject> jsonObjectResponseEntity = restTemplate.postForEntity("https://api.aliyundrive.com/adrive/v2/file/createWithFolders", requestEntity, JSONObject.class);
        System.out.println(jsonObjectResponseEntity.getBody());
        if (jsonObjectResponseEntity.getStatusCodeValue() == 201) {
            JSONObject res = jsonObjectResponseEntity.getBody();

            // 快速上传为true，则直接跳过一下步骤，文件上传成功
            if (res != null && res.getBooleanValue("rapid_upload")) {
                return true;
            }

            // 遍历所有上传 url 地址
            if (res != null) {
                res.getJSONArray("part_info_list").forEach(part_info -> {
                    LinkedHashMap partInfo = (LinkedHashMap) part_info;
                    String upload_url = (String) partInfo.get("upload_url");
                    System.out.println(upload_url);
                    // 上传文件
                    uploadInputStream(upload_url, file);
                });
            }

            // 确认上传成功
            JSONObject completeJson = complete(res != null ? res.getString("file_id") : null, res != null ? res.getString("upload_id") : null);
            return !Objects.isNull(completeJson);
        }

        return false;
    }


    public void uploadInputStream(String uploadUrl, File file) {
        ALi_HttpClientUtil.uploadFile(uploadUrl, file);
    }


    public JSONObject complete(String file_id, String upload_id) {
        // 构建你的请求头
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", aLi_token.getAccess_token());
        headers.set("Referer", "https://www.aliyundrive.com/");

        // 构建请求体参数
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("drive_id", aLi_token.getDefault_drive_id());
        jsonObject.put("file_id", file_id);
        jsonObject.put("upload_id", upload_id);

        // 组合请求头与请求体参数
        HttpEntity<String> requestEntity = new HttpEntity<>(jsonObject.toJSONString(), headers);

        // 发送请求上传文件的请求
        ResponseEntity<JSONObject> jsonObjectResponseEntity = restTemplate.postForEntity("https://api.aliyundrive.com/v2/file/complete", requestEntity, JSONObject.class);

        if (jsonObjectResponseEntity.getStatusCodeValue() == 200) {
            return jsonObjectResponseEntity.getBody();
        }

        return null;
    }
}
