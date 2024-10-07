package com.apple.iphoneinventory.service;

import cn.hutool.http.ContentType;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpUtil;
import cn.hutool.http.Method;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import com.apple.iphoneinventory.config.Properties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import static com.apple.iphoneinventory.service.PartService.partMap;

/**
 * @Author leezihong
 * @Date 2024/9/23 10:19
 * @Version 1.0
 * @description TODO
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class InventoryService {

    private final Properties properties;
    private final EmailService emailService;
    private final SimpMessagingTemplate simpMessagingTemplate;
    private static final HashMap<String, List<String>> productMap = new HashMap<>();

    /**
     * 根据配置来获取库存
     */
    @Scheduled(cron = "${spring.cron}")
    public void getInventory(){
        log.info("开始获取库存");
            // 获取库存
            HashMap<String, Object> paramMap = new HashMap<>();
            paramMap.put("pl", "true");
            paramMap.put("searchNearby", "false");
        for (int i = 0; i < properties.getPart().size(); i++) {
            paramMap.put("parts."+i, properties.getPart().get(i));
            productMap.put(properties.getPart().get(i), new ArrayList<>());
        }
        paramMap.put("location", "重庆 重庆 巴南区");
        String result = HttpUtil.createGet("https://www.apple.com.cn/shop/fulfillment-messages")
                .contentType("application/json")
                .form(paramMap)
                .execute().body();
        JSONArray objects = new JSONObject(result).getJSONObject("body").getJSONObject("content")
                .getJSONObject("pickupMessage").getJSONArray("stores");
        //循环所有商店
        StringBuilder resultStr = new StringBuilder();
        for (Object object : objects){
            JSONObject store = (JSONObject) object;
            String storeName = store.getStr("storeName");
            JSONObject partsAvailability = store.getJSONObject("partsAvailability");
            for (String part : properties.getPart()){
                JSONObject availabilityJSONObject = partsAvailability.getJSONObject(part);
                String buyability = availabilityJSONObject.getStr("pickupDisplay");
                //可以购买
                if ("available".equals(buyability)){
                   //获取商品名字
                    productMap.get(part).add(storeName);
                }
            }
        }
        productMap.forEach((k,v)->{
            if (!v.isEmpty()){
                resultStr.append(partMap.get(k)).append(" 重庆目前有货的apple store:").append(v).append("\n");
            }
        });

        HttpRequest httpRequest = HttpUtil.createRequest(Method.PUT, "ip://port")
                .contentType(ContentType.JSON.getValue());
        JSONObject jsonObject = new JSONObject();
        jsonObject.set("title", "库存通知");
        jsonObject.set("topic", "lzh");
        jsonObject.set("event", "message");
        if (!resultStr.isEmpty()){
            log.info("{}",resultStr);
            emailService.sendSimpleMessage("2578527296@qq.com", "iPhone库存", resultStr.toString());
            //websocket订阅
            simpMessagingTemplate.convertAndSend("/topic/inventory", resultStr.toString());
            jsonObject.set("message", resultStr.toString());
        }else {
            log.info("没有库存");
            simpMessagingTemplate.convertAndSend("/topic/inventory", "没有库存");
            jsonObject.set("message", "没有库存");
        }
        //ntfy通知
        // httpRequest.body(jsonObject.toString()).execute().body();
    }
}
