package org.diablitozzz.jera.concurrency.async;

import java.util.concurrent.TimeUnit;

import org.diablitozzz.jera.log.LogLogger;
import org.diablitozzz.jera.log.LogService;
import org.testng.annotations.Test;

public class TestAsyncService {

    private static class TestTask implements AsyncTask {

        private final String name;

        public TestTask(final String name) {
            this.name = name;
        }

        @Override
        public void run() {
            for (int i = 0; i < 10; i++) {
                TestAsyncService.logger.info("task " + this.name + " - " + i);
                try {
                    TimeUnit.MILLISECONDS.sleep(1);
                } catch (final InterruptedException e) {
                    break;
                }
            }
        }

    }

    final static LogLogger logger = LogService.createLogger(TestAsyncService.class);

    @Test
    public void testExecute() throws InterruptedException {

        try (final AsyncService asyncService = new AsyncService()) {

            asyncService.setCorePoolSize(4);
            asyncService.setMaximumPoolSize(4);
            asyncService.setName("hello world");
            asyncService.execute(new TestTask("A"));
            asyncService.execute(new TestTask("B"));

            TimeUnit.MILLISECONDS.sleep(4);
            TestAsyncService.logger.info("before close");
            asyncService.close();
            TestAsyncService.logger.info("after close");
        }

    }

}
