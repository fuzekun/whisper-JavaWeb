package com.fuzekun.controller;

import com.fuzekun.common.ResponseResult;
import com.fuzekun.entity.FluctuationValue;
import com.fuzekun.entity.MonitorAlarmManager;
import com.fuzekun.entity.MonitorAlarmTypeInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author: Zekun Fu
 * @date: 2024/5/11 19:22
 * @Description:
 */

@RestController
@RequestMapping("test")
@Slf4j
public class TestController {

    @PostMapping("/insert")
    public ResponseResult testListTrans(@RequestBody MonitorAlarmManager monitorAlarmManager) {
        List<MonitorAlarmTypeInfo> infoList = monitorAlarmManager.getInfoList();
        // 1. 插入monitorAlarmInfo表，得到对应的id
        monitorAlarmManager.setId(1L);
        // 2. 填充infoList
        for (MonitorAlarmTypeInfo info : infoList) {
            info.setManagerId(monitorAlarmManager.getId().toString());
            // 如果得到的是复杂类型，需要根据复杂类型填充这个类型
            if (info.getFluctuationValueList() != null) {
                info.fillValueByFluctuationValueList();
            }
        }
        // 3. 保存infoList
        log.info(monitorAlarmManager.toString());
        return ResponseResult.ok("保存成功!").build();
    }

}
