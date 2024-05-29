package com.fuzekun.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.extern.java.Log;

/**
 * @author: Zekun Fu
 * @date: 2024/5/11 20:59
 * @Description: 解析数据波动的值，
 * 1. 存入数据库之前，需要首先调用setValue
 * 2. 从数据库取出之后，传递给前端之前，需要对本值进行填充，resolverValue
 */
@Data
public class FluctuationValue {
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long viewId;
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long catalogueId;
    private String value;

    /**
     * 解析值，从数据库取出的时候使用
     * */
    public void setFluctuationByValue(String value) {
         String[] cols = value.split(";");
         if (cols.length < 3)
             throw new IllegalArgumentException("波动值解析失败");
         this.viewId = Long.parseLong(cols[0]);
         this.catalogueId = Long.parseLong(cols[1]);
         this.value = cols[2];
    }

    /**
     * 设置值，存入数据库之前使用
     * */
    public String getValueByFluctuation() {
        return this.viewId + ":" + this.catalogueId + ":" + this.value;
    }
}
