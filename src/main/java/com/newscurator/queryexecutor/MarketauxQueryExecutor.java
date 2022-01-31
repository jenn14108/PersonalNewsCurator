package com.newscurator.queryexecutor;

import com.newscurator.app.EnvironmentVariableKeeper;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * This is the query executor created for querying against the new API provided by Marketaux.
 * API documentation: https://www.marketaux.com/documentation
 */
@Slf4j
public class MarketauxQueryExecutor implements Runnable {

    private static final Logger logger = LoggerFactory.getLogger(MarketauxQueryExecutor.class);
    private static final String MARKETAUX_API_KEY = "MARKETAUX_API_KEY";
    private static final String KEY = EnvironmentVariableKeeper.getInstance().getVariable(MARKETAUX_API_KEY);
    private static final String BASE_URL = "https://api.marketaux.com/v1/news/all";
    private static final String API_TOKEN = "api_token";

    public static void main(String[] args){
        MarketauxQueryExecutor marketauxQueryExecutor = new MarketauxQueryExecutor();
        marketauxQueryExecutor.run();
    }
    @Override
    public void run() {

        // create a new client
        OkHttpClient client = new OkHttpClient();

        // build the url
        HttpUrl.Builder urlBuilder = HttpUrl.parse(BASE_URL).newBuilder();
        urlBuilder.addQueryParameter(API_TOKEN, KEY);
        String url = urlBuilder.build().toString();

        // create & execute request
        Request request = new Request.Builder().url(url).build();
        Call call = client.newCall(request);
        Response response = null;

        try {
            response = call.execute();
        } catch (IOException e) {
            logger.error("A problem occurred when sending a request against the Marketaux API.", e);
        }
        System.out.println(response);
    }
}
