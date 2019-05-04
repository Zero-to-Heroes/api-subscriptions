package com.zerotoheroes;

import com.amazonaws.services.secretsmanager.AWSSecretsManager;
import com.amazonaws.services.secretsmanager.AWSSecretsManagerClientBuilder;
import com.amazonaws.services.secretsmanager.model.GetSecretValueRequest;
import com.amazonaws.services.secretsmanager.model.GetSecretValueResult;
import org.json.JSONObject;

import javax.inject.Singleton;

@Singleton
public class SecretManager {

    private final AWSSecretsManager client;

    public SecretManager() {
        client = AWSSecretsManagerClientBuilder.standard()
                .withRegion("us-west-2")
                .build();
    }

    public JSONObject getRdsConnectionInfo() {
        GetSecretValueRequest secretRequest = new GetSecretValueRequest().withSecretId("rds-connection");
        GetSecretValueResult secretValue = client.getSecretValue(secretRequest);
        return new JSONObject(secretValue.getSecretString());
    }


}
