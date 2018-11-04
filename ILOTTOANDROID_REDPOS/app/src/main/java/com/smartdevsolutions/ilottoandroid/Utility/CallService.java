package com.smartdevsolutions.ilottoandroid.Utility;

/**
 * Created by BINARYCODES on 5/6/2017.
 */
import java.util.concurrent.TimeUnit;

import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class CallService <Y> {

    public static final MediaType JSON= MediaType.parse("application/json; charset=utf-8");
    private OkHttpClient client ;

    private String url;
    private String action;
    private String body;
    private String response;

    public  CallService(String url, String body)
    {
        OkHttpClient.Builder b = new OkHttpClient.Builder();
        b.readTimeout(120, TimeUnit.SECONDS);
        b.writeTimeout(120, TimeUnit.MILLISECONDS);
        client = b.build();
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

    public  Tuple<Integer, String> invokeGetService(String Token)  {


        Request request ;
        try  {
            if(Token!=null && Token.isEmpty()== false)
                request = new Request.Builder().url(url).header("basic-auth", "Bearer:"+Token).build();
            else
                request = new Request.Builder().url(url).build();
            Response serviceresponse = client.newCall(request).execute();
            this.action = "GET";
            this.response = serviceresponse.body().string();
            return new Tuple<>(serviceresponse.code(), this.response);
        }
        catch (Exception ex)
        {
            return  new Tuple<>(-1, ex.getMessage());
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

    public Tuple<Integer, String> invokePostService(String  Token){
        HttpUrl.Builder urlBuilder = HttpUrl.parse(this.url).newBuilder();

        String url = urlBuilder.build().toString();

        RequestBody body = RequestBody.create(JSON, this.body);
        Request request;

        if(Token!=null && Token.isEmpty()== false)
    request = new Request.Builder().url(url).header("basic-auth", "Bearer:"+Token).post(body).build();
else
    request = new Request.Builder().url(url).post(body).build();


        try  {

            Response response = client.newCall(request).execute();
            action = "POST";
            this.response = response.body().string();
            return new Tuple<>(response.code(), this.response);
        }
        catch (Exception ex)
        {
            return new Tuple<>(-1, ex.getMessage());
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
