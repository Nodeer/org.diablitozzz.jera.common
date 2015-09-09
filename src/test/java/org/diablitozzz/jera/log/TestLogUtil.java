package org.diablitozzz.jera.log;

import org.testng.Assert;
import org.testng.annotations.Test;

public class TestLogUtil {
    
    @Test
    public void testMakeMessage() {
        Assert.assertEquals("hello", LogUtil.makeMessage("hello", null));
        Assert.assertEquals("key:1, value:a, title:null, none:\\1", LogUtil.makeMessage("key:%s, value:%s, title:%s, none:\\%s", new Object[] { 1, "a", null, 1 }));
    }
}
