package com.zerotoheroes;


import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestStreamHandler;
import com.google.inject.Guice;
import com.google.inject.Injector;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

public class StreamLambdaHandler implements RequestStreamHandler {

    private static Injector injector;

    static {
        injector = Guice.createInjector(new Module());
    }

    @Override
    public void handleRequest(InputStream inputStream, OutputStream outputStream, Context context)
            throws IOException {
        JSONParser parser = new JSONParser();
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        JSONObject responseJson = new JSONObject();

        try {
            JSONObject event = (JSONObject) parser.parse(reader);
            JSONObject responseBody = new JSONObject();
            SubscriptionStatus result = null;
            if (event.get("pathParameters") != null) {
                JSONObject pathParameters = (JSONObject) event.get("pathParameters");
                String userId = (String) pathParameters.get("userid");
                String username = (String) pathParameters.get("username");
                SubsService service = injector.getInstance(SubsService.class);
                result = service.getSubscriptionStatus(userId, username);
            }
            if (result != null) {
                responseBody.put("result", result);
                responseJson.put("statusCode", 200);
            } else {
                responseBody.put("message", "No subscription found");
                responseJson.put("statusCode", 404);
            }
            JSONObject headerJson = new JSONObject();
            headerJson.put("x-custom-header", "my custom header value");
            responseJson.put("headers", headerJson);
            responseJson.put("body", responseBody.toString());
        } catch (Exception e) {
            responseJson.put("statusCode", 400);
            responseJson.put("exception", e);
        }

        OutputStreamWriter writer = new OutputStreamWriter(outputStream, "UTF-8");
        writer.write(responseJson.toString());
        writer.close();
    }
}