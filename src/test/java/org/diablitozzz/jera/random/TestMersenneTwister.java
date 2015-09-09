package org.diablitozzz.jera.random;

import org.testng.Assert;
import org.testng.annotations.Test;

public class TestMersenneTwister {
    
    private final MersenneTwister mersenneTwister = new MersenneTwister();

    @Test
    public void testRandom() {
        for (int i = 0; i < 100; i++) {
            Assert.assertNotNull(this.mersenneTwister.nextBoolean());
        }
    }
}
