package com.newscurator.app;


import java.time.Duration;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class NewCuratorApplication {

    public static String NY_ZONE_ID = "America/New_York";

    public static void main(String[] args) {

        long delay = calculateTaskDelay();
        ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
        // keep app running until I take it down...
        // 86400 = 24 hrs in seconds
        executor.scheduleWithFixedDelay(new TestTask(), delay, 86400, TimeUnit.SECONDS);
    }

    public static long calculateTaskDelay() {
        // always execute queries at 1pm every day
        ZonedDateTime currentDateTime = ZonedDateTime.now(ZoneId.of(NY_ZONE_ID));
        ZonedDateTime nextExecutionDateTime = currentDateTime.withHour(13).withMinute(0).withSecond(0);
        // add one day if we are firing up application at a later time than 1pm EST
        if (currentDateTime.compareTo(nextExecutionDateTime) > 0) {
            nextExecutionDateTime = nextExecutionDateTime.plusDays(1);
        }
        Duration duration = Duration.between(currentDateTime, nextExecutionDateTime);
        return duration.getSeconds();
    }

    public static class TestTask implements Runnable {

        @Override
        public void run() {
            System.out.println("executed!");
        }
    }
}
