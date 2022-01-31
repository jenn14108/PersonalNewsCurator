package com.newscurator.queryexecutor;

import com.newscurator.app.EnvironmentVariableKeeper;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.slf4j.Logger;

import java.io.IOException;

/**
 * This is the query executor created for querying against the new API provided by The Guardian.
 * The http client used by this executor is okhttp - https://github.com/square/okhttp
 * The Guardian API documentation - https://open-platform.theguardian.com/documentation/search
 */
@Slf4j
public class TheGuardianQueryExecutor implements Runnable {

    private static final Logger logger = org.slf4j.LoggerFactory.getLogger(TheGuardianQueryExecutor.class);
    private static final String THE_GUARDIAN_API_KEY = "THE_GUARDIAN_API_KEY";
    private static final String API_KEY = "api-key";
    private static final String KEY = EnvironmentVariableKeeper.getInstance().getVariable(THE_GUARDIAN_API_KEY);
    private static final String BASE_URL = "https://content.guardianapis.com/search";

    // temporary, for testing
    public static void main(String[] args){
        TheGuardianQueryExecutor theGuardianQueryExecutor = new TheGuardianQueryExecutor();
        theGuardianQueryExecutor.run();
    }

    @Override
    public void run() {

        // create a new client
        OkHttpClient client = new OkHttpClient();

        // build the url
        HttpUrl.Builder urlBuilder = HttpUrl.parse(BASE_URL).newBuilder();
        urlBuilder.addQueryParameter(API_KEY, KEY);
        String url = urlBuilder.build().toString();

        // create & execute request
        Request request = new Request.Builder().url(url).build();
        Call call = client.newCall(request);
        Response response = null;

        try {
            response = call.execute();
        } catch (IOException e) {
            logger.error("A problem occurred when sending a request against the Guardian API.", e);
        }
        System.out.println(response);

    }
}
