package com.apple.iphoneinventory;

import com.apple.iphoneinventory.service.InventoryService;
import com.apple.iphoneinventory.service.PartService;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class IPhoneInventoryApplicationTests {
    @Resource
    private PartService partService;
    @Resource
    private InventoryService inventoryService;
    @Test
    void contextLoads() {
        //partService.getAllPart();
        inventoryService.getInventory();
    }

}
