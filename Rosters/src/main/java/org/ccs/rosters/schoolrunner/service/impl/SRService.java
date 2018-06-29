package org.ccs.rosters.schoolrunner.service.impl;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.springframework.stereotype.Component;
import org.ccs.rosters.schoolrunner.service.ISRService;

import java.io.InputStream;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.List;

@Component
public class SRService implements ISRService {

    private final String SR_LOGIN = "sr.json";
    private String baseHost = "crescentcity.schoolrunner.org/api/v1";

    // Load client secrets. Call class loader to access resource folder.
    InputStream in = this.getClass().getClassLoader().getResourceAsStream(SR_LOGIN);
    //JsonParser jsonParser = Json.createParser(in);
   // JsonObject jsonObject = jsonParser.getArray().getJsonObject(0);
    //String usercode = jsonObject.getString("user") + ":" + jsonObject.getString("code");
    String usercode = "habans.api@crescentcityschools.org:Mamba2017";
    String encode = Base64.getEncoder().encodeToString(usercode.getBytes());

    public String request(String endpoint, List<NameValuePair> parameters) {

        try {
            URI uri = new URIBuilder()
                    .setScheme("https")
                    .setHost(baseHost)
                    .setPath(endpoint)
                    .addParameters(parameters)
                    .build();

            //Print URL for debugging
            System.out.println(uri);

            HttpClient httpClient = HttpClientBuilder.create().build();
            HttpGet getRequest = new HttpGet(uri);
            getRequest.setHeader("Authorization", "Basic " + encode);
            getRequest.setHeader("Content-Type", "application/json");

            HttpResponse httpResponse = httpClient.execute(getRequest);

            HttpEntity httpEntity = httpResponse.getEntity();
            String json = EntityUtils.toString(httpEntity, StandardCharsets.UTF_8);
            return json;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return "Something went wrong. Should not reach here";
    }


}
