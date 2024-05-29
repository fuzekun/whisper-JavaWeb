package com.fuzekun.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonTypeName;
import lombok.Data;

import java.util.List;

/**
 * @author: Zekun Fu
 * @date: 2024/5/11 19:23
 * @Description:
 */
@Data
public class MonitorAlarmManager {
    @JsonFormat(shape =JsonFormat.Shape.STRING)
    Long id;
//    @JsonFormat(shape =JsonFormat.Shape.STRING)
    List<String> ruleIds;
    List<MonitorAlarmTypeInfo> infoList;

    String receiverId;
}
