package com.newscurator.queryexecutor;

import static com.newscurator.util.Constants.NY_ZONE_ID;
import static com.newscurator.util.MarketauxConstants.*;

import com.google.gson.*;
import com.newscurator.app.EnvironmentVariableKeeper;
import com.newscurator.schema.MarketauxResult;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.concurrent.Callable;

/**
 * This is the query executor created for querying against the new API provided by Marketaux.
 * Marketaux provides global financial and stock market news.
 * API documentation: https://www.marketaux.com/documentation
 */
@Slf4j
public class MarketauxQueryExecutor implements Callable<MarketauxResult[]> {

    private static final Logger logger = LoggerFactory.getLogger(MarketauxQueryExecutor.class);
    private static final String KEY = EnvironmentVariableKeeper.getInstance().getVariable(MARKETAUX_API_KEY);

    @Override
    public MarketauxResult[] call() {
        // create a new client
        OkHttpClient client = new OkHttpClient();

        // build the url
        HttpUrl.Builder urlBuilder = HttpUrl.parse(BASE_URL).newBuilder();
        urlBuilder.addQueryParameter(API_TOKEN, KEY);
        // no other filters are needed as I'd be interested in all news articles pulled from Marketaux
        urlBuilder.addQueryParameter(PUBLISHED_ON, LocalDate.now(ZoneId.of(NY_ZONE_ID)).toString());
        String url = urlBuilder.build().toString();

        // create & execute request
        Request request = new Request.Builder().url(url).build();
        Call call = client.newCall(request);
        Response response;
        MarketauxResult[] marketauxResults = new MarketauxResult[0];

        try {
            response = call.execute();
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            JsonObject jsonObject = JsonParser.parseString(response.body().string()).getAsJsonObject();
            JsonArray responseJson = jsonObject.get(DATA).getAsJsonArray();
            marketauxResults = new MarketauxResult[responseJson.size()];
            for (int index = 0; index < responseJson.size(); index++) {
                marketauxResults[index] = gson.fromJson(responseJson.get(index).toString(), MarketauxResult.class);
            }
        } catch (IOException e) {
            logger.error("A problem occurred when sending a request against the Marketaux API.", e);
        }
        
        return marketauxResults;
    }
}
