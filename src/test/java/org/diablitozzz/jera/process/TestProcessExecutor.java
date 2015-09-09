package org.diablitozzz.jera.process;

import java.io.ByteArrayOutputStream;

import org.testng.Assert;
import org.testng.annotations.Test;

public class TestProcessExecutor {

    @Test(enabled = false)
    public void testExec() throws Exception {

        final ByteArrayOutputStream err = new ByteArrayOutputStream();
        final ByteArrayOutputStream out = new ByteArrayOutputStream();

        final ProcessExecutor executor = new ProcessExecutor();
        final ProcessSpecification spec = new ProcessSpecification();
        spec.setCommand("ls -la");
        spec.setErr(err);
        spec.setOut(out);

        final int result = executor.execute(spec);

        Assert.assertEquals(result, 0);
        Assert.assertTrue(err.toByteArray().length == 0);
        Assert.assertTrue(out.toByteArray().length > 0);
    }

}
