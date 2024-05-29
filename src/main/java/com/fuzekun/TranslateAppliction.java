package com.fuzekun;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * @author: Zekun Fu
 * @date: 2024/4/25 23:31
 * @Description:
 */
// 扫描多个包
@ComponentScan(basePackages = {"com.fuzekun", "io"})
@SpringBootApplication
public class TranslateAppliction {
    public static void main(String[] args) {
        SpringApplication.run(TranslateAppliction.class, args);
    }

}
