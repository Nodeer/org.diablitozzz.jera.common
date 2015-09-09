package org.diablitozzz.jera.concurrency;

import org.testng.Assert;
import org.testng.annotations.Test;

public class TestTimeout {

    @Test
    public void testTimeOut() {

        final boolean result = Timeout.timeout(1000, 1, () -> {
            return false;
        });
        Assert.assertFalse(result);
    }
}
