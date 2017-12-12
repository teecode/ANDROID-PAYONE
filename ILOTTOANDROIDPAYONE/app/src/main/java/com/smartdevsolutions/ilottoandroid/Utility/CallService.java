package com.smartdevsolutions.ilottoandroid.Utility;

/**
 * Created by BINARYCODES on 5/6/2017.
 */
import java.io.IOException;

import okhttp3.CipherSuite;
import okhttp3.ConnectionSpec;
import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.TlsVersion;


public class CallService {

    public static final MediaType JSON= MediaType.parse("application/json; charset=utf-8");
    private OkHttpClient client ;

    private String url;
    private String action;
    private String body;
    private String response;

    public  CallService(String url, String body)
    {
        client = new OkHttpClient();
        this.url = url;
        this.body = body;


    }

    public  String invokeGetService()  {


        Request request = new Request.Builder()
                .url(url)
                .build();

        try  {
            Response serviceresponse = client.newCall(request).execute();
            this.action = "GET";
            this.response = serviceresponse.body().string();
            return this.response;
        }
        catch (Exception ex)
        {
            return  "";
        }
    }

    public  String invokePostService()  {
        RequestBody body = RequestBody.create(JSON, this.body);
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();
        try  {
            Response response = client.newCall(request).execute();
            action = "POST";
            this.response = response.body().string();
            return this.response;

        }
        catch (Exception ex)
        {
            return  "";
        }


    }

    public  String invokeSecuredPostService()  {

        HttpUrl.Builder urlBuilder = HttpUrl.parse(this.url).newBuilder();

        String url = urlBuilder.build().toString();

        RequestBody body = RequestBody.create(JSON, this.body);

        Request request = new Request.Builder().url(url).post(body).build();


        try  {
            Response response = client.newCall(request).execute();
            action = "POST";
            this.response = response.body().string();
            return this.response;

        }
        catch (Exception ex)
        {
            return  "";
        }


    }

    public void setUrl(String url)
    {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

}
