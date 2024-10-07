package com.apple.iphoneinventory.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @Author leezihong
 * @Date 2024/9/23 9:33
 * @Version 1.0
 * @description TODO
 */
@ConfigurationProperties(prefix = "spring")
@Component
@Data
public class Properties {

    /**
     * 需要监控的规格列表
     */
    List<String> part;


}
