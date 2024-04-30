package io.github.givimad.whisperjni;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author: fuzekun
 * @date: 2024/4/29
 * @describe:
 */
public class TestLogger {
    private static final Logger logger = LoggerFactory.getLogger(Main.class);

    @Test
    public void test() {
        logger.debug("测试debug");
        logger.info("测试info");
    }

    public static void main(String[] args) {
        System.out.println("测试开始");
        logger.debug("测试debug");
        logger.info("测试info");
        logger.error("测试error");
    }
}
