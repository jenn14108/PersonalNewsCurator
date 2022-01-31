package com.newscurator.queryexecutor;

import static com.newscurator.util.Constants.NY_ZONE_ID;
import static com.newscurator.util.TheGuardianConstants.*;

import com.google.gson.*;
import com.newscurator.app.EnvironmentVariableKeeper;
import com.newscurator.schema.TheGuardianResult;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.concurrent.Callable;

/**
 * This is the query executor created for querying against the new API provided by The Guardian.
 * The http client used by this executor is okhttp - https://github.com/square/okhttp
 * The Guardian API documentation - https://open-platform.theguardian.com/documentation/search
 */
@Slf4j
public class TheGuardianQueryExecutor implements Callable<TheGuardianResult[]> {

    private static final Logger logger = LoggerFactory.getLogger(TheGuardianQueryExecutor.class);
    private static final String KEY = EnvironmentVariableKeeper.getInstance().getVariable(THE_GUARDIAN_API_KEY);

    @Override
    public TheGuardianResult[] call() {

        // create a new client
        OkHttpClient client = new OkHttpClient();

        // build the url
        HttpUrl.Builder urlBuilder = HttpUrl.parse(BASE_URL).newBuilder();
        urlBuilder.addQueryParameter(API_KEY, KEY);
        urlBuilder.addQueryParameter(FROM_DATE, LocalDate.now(ZoneId.of(NY_ZONE_ID)).toString());
        // q = query operator
        urlBuilder.addQueryParameter(Q, TECHNOLOGY + "|" + TAIWAN + "|" + CHINA + "|" + POLITICS + "|" + BUSINESS);
        String url = urlBuilder.build().toString();

        // create & execute request
        Request request = new Request.Builder().url(url).build();
        Call call = client.newCall(request);
        Response response;
        TheGuardianResult[] theGuardianResults = new TheGuardianResult[0];

        try {
            // get response and convert to POJO
            response = call.execute();
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            JsonObject jsonObject = JsonParser.parseString(response.body().string()).getAsJsonObject();
            JsonObject responseJson = jsonObject.get(RESPONSE).getAsJsonObject();
            String results = responseJson.get(RESULTS).toString();
            theGuardianResults = gson.fromJson(results, TheGuardianResult[].class);
        } catch (IOException e) {
            logger.error("A problem occurred when sending a request against the Guardian API.", e);
        }
        
        return theGuardianResults;
    }
}
