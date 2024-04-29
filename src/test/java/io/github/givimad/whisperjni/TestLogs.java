package io.github.givimad.whisperjni;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

/**
 * @author: Zekun Fu
 * @date: 2024/4/29 21:46
 * @Description: 测试日志
 */
@Slf4j
public class TestLogs {

    @Test
    public void test() {
        log.debug("输出debug");
        log.info("输出info" );
        log.warn("输出warn");
        log.error("输出error");
    }

    public static void main(String[] args) {
        log.debug("输出debug");
        log.info("输出info" );
        log.warn("输出warn");
        log.error("输出error");
    }
}
