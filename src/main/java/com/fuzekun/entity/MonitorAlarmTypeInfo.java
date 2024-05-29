package com.fuzekun.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.List;

/**
 * @author: Zekun Fu
 * @date: 2024/5/11 19:24
 * @Description:
 *
 */
@Data
public class MonitorAlarmTypeInfo {


    /**
     * 有了名称就不用使用类型id进行
     * */
    @JsonFormat(shape =JsonFormat.Shape.STRING)
    private Long id;
    private String ruleTypeName;
    private String managerId;
    private String ruleId;
    private String alarmTypeId;

    private String condition;
    private String value;
    private String name;

    private List<FluctuationValue> fluctuationValueList;

    /**
     * 通过波动类型值填充value
     * */
    public void fillValueByFluctuationValueList() {
        StringBuffer ans = new StringBuffer();
        for (FluctuationValue value : fluctuationValueList) {
            ans.append(value.getValueByFluctuation()).append(";");
        }
        this.value = ans.toString();
    }
    /**
     * 通过value填充波动类型值
     * */
    public void fillFluctuationValueListByVale() {
        String[] values = this.value.split(";");
        if (values.length == 0)
            throw new IllegalArgumentException("复杂类型填充失败");
        for (String s : values) {
            FluctuationValue fval = new FluctuationValue();
            fval.setFluctuationByValue(s);
            this.fluctuationValueList.add(fval);
        }
    }
}
