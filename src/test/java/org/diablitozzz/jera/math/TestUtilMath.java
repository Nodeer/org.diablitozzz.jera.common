package org.diablitozzz.jera.math;

import org.diablitozzz.jera.util.UtilMath;
import org.testng.Assert;
import org.testng.annotations.Test;

public class TestUtilMath {

    @Test
    public void testGetAngle() {
        Assert.assertTrue(UtilMath.getAngle(1, 1, 2, 2) == 45D);
        Assert.assertTrue(UtilMath.getAngle(1, 1, 1, 2) == 90D);
        Assert.assertTrue(UtilMath.getAngle(1, 2, 2, 1) == -45D);
    }
}
