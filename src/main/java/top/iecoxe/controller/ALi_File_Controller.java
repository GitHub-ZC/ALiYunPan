package top.iecoxe.controller;

import com.alibaba.fastjson2.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import top.iecoxe.entity.ALi_Token;

import javax.annotation.Resource;
import java.io.IOException;
import java.io.InputStream;


/**
 * 阿里云盘文件操作接口
 * @author IMAX-拾荒者
 */
@RestController
@RequestMapping("/file")
public class ALi_File_Controller {
    @Autowired
    private RestTemplate restTemplate;

    @Resource
    private ALi_Token aLi_token;

    /*@Resource
    private ALi_Update_File aLi_update_file;*/

    /**
     * 所有文件列表
     * @param pid 文件-父ID (root为根目录)
     * @return JSON
     */
    @GetMapping("/list")
    public JSONObject fileList(@RequestParam(value = "pid", defaultValue = "root") String pid) {
        // 构建你的请求头
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", aLi_token.getAccess_token());
        headers.set("Referer", "https://www.aliyundrive.com/");
        headers.set("X-Device-Id", "m3UEHIuEKk8CAa5GsaUoNjMP");
        headers.set("X-Signature", aLi_token.getX_Signature());

        // 构建请求体参数
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("all", false);
        jsonObject.put("drive_id", aLi_token.getDefault_drive_id());
        jsonObject.put("fields", "*");
        jsonObject.put("image_thumbnail_process", "image/resize,w_256/format,jpeg");
        jsonObject.put("image_url_process", "image/resize,w_1920/format,jpeg/interlace,1");
        jsonObject.put("limit", 100);
        jsonObject.put("order_by", "updated_at");
        jsonObject.put("order_direction", "DESC");
        jsonObject.put("parent_file_id", pid);
        jsonObject.put("url_expire_sec", 14400);
        jsonObject.put("video_thumbnail_process", "video/snapshot,t_1000,f_jpg,ar_auto,w_256");

        // 组合请求头与请求体参数
        HttpEntity<String> requestEntity = new HttpEntity<>(jsonObject.toJSONString(), headers);

        // url -> 请求地址，requestEntity -> 请求参数相关对象，HashMap.class -> 返回结果映射类型
        ResponseEntity<JSONObject> jsonObjectResponseEntity = restTemplate.postForEntity("https://api.aliyundrive.com/adrive/v3/file/list?jsonmask=next_marker,items(name,file_id,drive_id,type,size,created_at,updated_at,category,file_extension,parent_file_id,mime_type,starred,thumbnail,url,streams_info,content_hash,user_tags,user_meta,trashed,video_media_metadata,video_preview_metadata,sync_meta,sync_device_flag,sync_flag,punish_flag)", requestEntity, JSONObject.class);
        return jsonObjectResponseEntity.getBody();
    }


    /**
     * 文件下载链接
     * @param fid 文件ID
     * @return JSON
     */
    @GetMapping("/getDownloadUrl")
    public JSONObject getDownloadUrl(@RequestParam(value = "fid") String fid) {
        // 构建你的请求头
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", aLi_token.getAccess_token());
        headers.set("Referer", "https://www.aliyundrive.com/");
        headers.set("X-Device-Id", "m3UEHIuEKk8CAa5GsaUoNjMP");
        headers.set("X-Signature", aLi_token.getX_Signature());

        // 构建请求体参数
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("drive_id", aLi_token.getDefault_drive_id());
        jsonObject.put("file_id", fid);

        // 组合请求头与请求体参数
        HttpEntity<String> requestEntity = new HttpEntity<>(jsonObject.toJSONString(), headers);

        // url -> 请求地址，requestEntity -> 请求参数相关对象，HashMap.class -> 返回结果映射类型
        ResponseEntity<JSONObject> jsonObjectResponseEntity = restTemplate.postForEntity("https://api.aliyundrive.com/v2/file/get_download_url", requestEntity, JSONObject.class);
        return jsonObjectResponseEntity.getBody();
    }


    /**
     * 创建文件夹
     * @param name 文件夹的名称
     * @param pid 父目录的ID
     * @return JSON
     */
    @GetMapping("/createFolder")
    public JSONObject createFolder(@RequestParam(value = "name") String name, @RequestParam(value = "pid") String pid) {
        // 构建你的请求头
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", aLi_token.getAccess_token());
        headers.set("Referer", "https://www.aliyundrive.com/");
        headers.set("Content-Type", "application/json;charset=utf-8");
        headers.set("X-Device-Id", "m3UEHIuEKk8CAa5GsaUoNjMP");
        headers.set("X-Signature", aLi_token.getX_Signature());

        // 构建请求体参数
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("drive_id", aLi_token.getDefault_drive_id());
        jsonObject.put("check_name_mode", "refuse");
        jsonObject.put("name", name);
        jsonObject.put("parent_file_id", pid);
        jsonObject.put("type", "folder");

        System.out.println(jsonObject.toJSONString());
        // 组合请求头与请求体参数
        HttpEntity<String> requestEntity = new HttpEntity<>(jsonObject.toJSONString(), headers);

        // url -> 请求地址，requestEntity -> 请求参数相关对象，HashMap.class -> 返回结果映射类型
        ResponseEntity<JSONObject> jsonObjectResponseEntity = restTemplate.postForEntity("https://api.aliyundrive.com/adrive/v2/file/createWithFolders", requestEntity, JSONObject.class);
        return jsonObjectResponseEntity.getBody();
    }


    /**
     * 精准查询（需要父目录ID和完整文件名）
     * @param key 完整文件名
     * @param pid 父目录ID
     * @return JSON
     */
    @GetMapping("/preciseSearch")
    public JSONObject preciseSearch(@RequestParam(value = "key") String key, @RequestParam(value = "pid") String pid) {
        // 构建你的请求头
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", aLi_token.getAccess_token());
        headers.set("Referer", "https://www.aliyundrive.com/");
        headers.set("Content-Type", "application/json;charset=utf-8");
        headers.set("X-Device-Id", "m3UEHIuEKk8CAa5GsaUoNjMP");
        headers.set("X-Signature", aLi_token.getX_Signature());

        // 构建请求体参数
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("drive_id", aLi_token.getDefault_drive_id());
        jsonObject.put("limit", 100);
        jsonObject.put("order_by", "name ASC");
        jsonObject.put("query", "parent_file_id = \"" + pid + "\" and (name = \"" + key + "\")");

        // 组合请求头与请求体参数
        HttpEntity<String> requestEntity = new HttpEntity<>(jsonObject.toJSONString(), headers);

        // url -> 请求地址，requestEntity -> 请求参数相关对象，HashMap.class -> 返回结果映射类型
        ResponseEntity<JSONObject> jsonObjectResponseEntity = restTemplate.postForEntity("https://api.aliyundrive.com/adrive/v3/file/search", requestEntity, JSONObject.class);
        return jsonObjectResponseEntity.getBody();
    }


    /**
     * 修改文件或者文件夹的名称
     * @param fileId 文件ID
     * @param name 新名称
     * @return JSON
     */
    @GetMapping("/updateName")
    public JSONObject updateName(@RequestParam(value = "fileId") String fileId, @RequestParam(value = "name") String name) {
        // 构建你的请求头
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", aLi_token.getAccess_token());
        headers.set("Referer", "https://www.aliyundrive.com/");
        headers.set("Content-Type", "application/json;charset=utf-8");
        headers.set("X-Device-Id", "m3UEHIuEKk8CAa5GsaUoNjMP");
        headers.set("X-Signature", aLi_token.getX_Signature());

        // 构建请求体参数
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("drive_id", aLi_token.getDefault_drive_id());
        jsonObject.put("check_name_mode", "refuse");
        jsonObject.put("file_id", fileId);
        jsonObject.put("name", name);

        // 组合请求头与请求体参数
        HttpEntity<String> requestEntity = new HttpEntity<>(jsonObject.toJSONString(), headers);

        // url -> 请求地址，requestEntity -> 请求参数相关对象，HashMap.class -> 返回结果映射类型
        ResponseEntity<JSONObject> jsonObjectResponseEntity = restTemplate.postForEntity("https://api.aliyundrive.com/v3/file/update", requestEntity, JSONObject.class);
        return jsonObjectResponseEntity.getBody();
    }


    /**
     * 删除文件
     * @param fileId 文件ID
     * @return JSON
     */
    @GetMapping("/trash")
    public JSONObject trash(@RequestParam(value = "fileId") String fileId) {
        // 构建你的请求头
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", aLi_token.getAccess_token());
        headers.set("Referer", "https://www.aliyundrive.com/");
        headers.set("Content-Type", "application/json;charset=utf-8");
        headers.set("X-Device-Id", "m3UEHIuEKk8CAa5GsaUoNjMP");
        headers.set("X-Signature", aLi_token.getX_Signature());

        // 构建请求体参数
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("drive_id", aLi_token.getDefault_drive_id());
        jsonObject.put("file_id", fileId);

        // 组合请求头与请求体参数
        HttpEntity<String> requestEntity = new HttpEntity<>(jsonObject.toJSONString(), headers);

        // url -> 请求地址，requestEntity -> 请求参数相关对象，HashMap.class -> 返回结果映射类型
        ResponseEntity<JSONObject> jsonObjectResponseEntity = restTemplate.postForEntity("https://api.aliyundrive.com/v2/recyclebin/trash", requestEntity, JSONObject.class);
        return jsonObjectResponseEntity.getBody();
    }



    /**
     * 模糊查询(只需要输入某个关键字，就能查询到相关的文件)、(category字段为可选项，就是按照类别分类进行模糊查询)
     * @param key 关键字
     * @param category 音频(audio)  文档(doc)  文件夹(folder)  视频(video)  图片(image) 为空时，默认全局搜索
     * @return JSON
     */
    @GetMapping("/fuzzySearch")
    public JSONObject fuzzySearch(@RequestParam(value = "key") String key, @RequestParam(value = "category", defaultValue = "__null__", required = false) String category) {
        // 构建你的请求头
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", aLi_token.getAccess_token());
        headers.set("Referer", "https://www.aliyundrive.com/");
        headers.set("Content-Type", "application/json;charset=utf-8");
        headers.set("X-Device-Id", "m3UEHIuEKk8CAa5GsaUoNjMP");
        headers.set("X-Signature", aLi_token.getX_Signature());

        // 构建请求体参数
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("drive_id", aLi_token.getDefault_drive_id());
        jsonObject.put("limit", 100);
        jsonObject.put("order_by", "updated_at DESC");
        if("__null__".equals(category)) {
            jsonObject.put("query", "name match \"" + key + "\"");
        } else {
            jsonObject.put("query", "name match \"" + key + "\" and category = \"" + category + "\"");
        }

        // 组合请求头与请求体参数
        HttpEntity<String> requestEntity = new HttpEntity<>(jsonObject.toJSONString(), headers);

        // url -> 请求地址，requestEntity -> 请求参数相关对象，HashMap.class -> 返回结果映射类型
        ResponseEntity<JSONObject> jsonObjectResponseEntity = restTemplate.postForEntity("https://api.aliyundrive.com/adrive/v3/file/search", requestEntity, JSONObject.class);
        return jsonObjectResponseEntity.getBody();
    }


    /**
     * 暂时废除
     * @param file
     * @param parentId
     * @return
     */
    @GetMapping("/uploadFile")
    public JSONObject uploadFile(@RequestParam("file") MultipartFile file, @RequestParam("parentId") String parentId) {
        try {
            String fileName = file.getName();
            long fileSize = file.getSize();

            InputStream fis = file.getInputStream(); //fis 既是上传的文件流

            //boolean b = aLi_update_file.updateFile(file, parentId);

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
