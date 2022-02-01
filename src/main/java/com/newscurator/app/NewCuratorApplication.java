package com.newscurator.app;


import com.newscurator.queryexecutor.MarketauxQueryExecutor;
import com.newscurator.queryexecutor.TheGuardianQueryExecutor;
import com.newscurator.schema.MarketauxResult;
import com.newscurator.schema.NewsArticle;
import com.newscurator.schema.TheGuardianResult;

import java.time.Duration;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

import static com.newscurator.util.Constants.NY_ZONE_ID;

public class NewCuratorApplication {

    public static final EmailSender emailSender = new EmailSender();

    public static void main(String[] args) {

        MarketauxQueryExecutor marketauxQueryExecutor = new MarketauxQueryExecutor();
        TheGuardianQueryExecutor theGuardianQueryExecutor = new TheGuardianQueryExecutor();

        long delay = calculateTaskDelay();
        ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
        // keep app running until I take it down...
        // 86400 = 24 hrs in seconds

        executor.scheduleAtFixedRate(() -> {
                    runQueriesAndSendEmail(marketauxQueryExecutor, theGuardianQueryExecutor);
                }
                , delay, 86400, TimeUnit.SECONDS);
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

    public static void runQueriesAndSendEmail(MarketauxQueryExecutor marketauxQueryExecutor, TheGuardianQueryExecutor theGuardianQueryExecutor) {

        MarketauxResult[] marketauxResults = marketauxQueryExecutor.call();
        TheGuardianResult[] theGuardianResults = theGuardianQueryExecutor.call();
        List<NewsArticle> newsArticles = convertToNewsArticle(marketauxResults, theGuardianResults);
        emailSender.sendNewsCuratorEmail(newsArticles);
    }

    private static List<NewsArticle> convertToNewsArticle(MarketauxResult[] marketauxResults, TheGuardianResult[] theGuardianResults){
        List<NewsArticle> newsArticles = new ArrayList<>();

        for (MarketauxResult result : marketauxResults){
            String title = result.getTitle();
            String url = result.getUrl();
            newsArticles.add(NewsArticle.builder().title(title).url(url).build());
        }

        for (TheGuardianResult result : theGuardianResults){
            String title = result.getWebTitle();
            String url = result.getWebUrl();
            newsArticles.add(NewsArticle.builder().title(title).url(url).build());
        }
        return newsArticles;
    }

}
