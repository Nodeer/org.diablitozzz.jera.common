package org.diablitozzz.jera.log;

import org.testng.annotations.Test;

public class TestLogLogger {

    private final static LogLogger log = LogService.createLogger(TestLogLogger.class);
    
    @Test
    public void testLog() {
        
        TestLogLogger.log.info("info");
        TestLogLogger.log.debug("debug");
        TestLogLogger.log.trace("trace");
        TestLogLogger.log.warn("warn");
        TestLogLogger.log.error("error %s", 1);

    }

}
