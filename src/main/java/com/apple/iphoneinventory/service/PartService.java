package com.apple.iphoneinventory.service;

import cn.hutool.http.Header;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.apple.iphoneinventory.config.Properties;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashMap;

/**
 * @Author leezihong
 * @Date 2024/9/23 9:30
 * @Version 1.0
 * @description TODO
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class PartService {

    private final Properties properties;

    static final HashMap<String, String> partMap = new HashMap<>();
    /**
     * 获取所有的型号并且存储
     */
    @PostConstruct
    public void getAllPart() {
        HashMap<String, Object> paramMap = new HashMap<>();
        paramMap.put("family", "iphone16promax");
        String result = HttpUtil.createGet("https://www.apple.com.cn/shop/product-locator-meta")
                .contentType("application/json")
                .header(Header.CONTENT_TYPE, "application/json")
                .form(paramMap)
                .execute().body();
        JSONObject jsonObject = new JSONObject(result);

        JSONArray jsonArray = jsonObject.getJSONObject("body")
                .getJSONObject("productLocatorOverlayData")
                .getJSONObject("productLocatorMeta").getJSONArray("products");
        for (Object o : jsonArray){
            JSONObject obj = (JSONObject) o;
            partMap.put(obj.getStr("partNumber"), obj.getStr("productTitle"));
        }
        log.info("所有型号初始化完毕！");
    }


}
