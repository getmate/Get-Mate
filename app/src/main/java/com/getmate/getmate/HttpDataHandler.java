package com.getmate.getmate;

import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;


/**
 * Created by HP on 24-06-2017.
 */

public class HttpDataHandler {

    static String stream = null;
    public HttpDataHandler() {
    }

    public String GetHttpData(String urlString)
    {
        try{

            URL url = new URL(urlString);
            HttpURLConnection urlconnection = (HttpURLConnection)url.openConnection();

            if(urlconnection.getResponseCode()==200)
            {
                InputStream in = new BufferedInputStream(urlconnection.getInputStream());
                BufferedReader buffreader = new BufferedReader(new InputStreamReader(in));
                StringBuilder sb = new StringBuilder();
                String line;
                while((line = buffreader.readLine())!=null) sb.append(line);

                    stream = sb.toString();
                    urlconnection.disconnect();

            }

        }
        catch (MalformedURLException e1)
        {
            e1.printStackTrace();
        }
        catch (IOException e){
            e.printStackTrace();
        }
       return stream;
    }

    public String PostHttpData(String urlString,String json)
    {
        Log.e(urlString+" url",json+" ");
        String bufferedStrChunk = null;
        StringBuilder stringBuilder = new StringBuilder();
        try {
            URL url = new URL(urlString);
            HttpURLConnection urlconnection = (HttpURLConnection) url.openConnection();
            urlconnection.setRequestMethod("POST");
            urlconnection.setDoOutput(true);
            byte []out = json.getBytes(Charset.forName("UTF-8"));
            int length = out.length;
            urlconnection.setFixedLengthStreamingMode(length);
            urlconnection.setRequestProperty("Content-Type","application/json; charset=UTF-8");
            urlconnection.connect();
            try
            {OutputStream os = urlconnection.getOutputStream();
                os.write(out);
            }
            catch(IOException e)
            {
                e.printStackTrace();
            }
            InputStream response = urlconnection.getInputStream();
            Log.e("tag Response",response+" res ");
            InputStreamReader inputStreamReader = new InputStreamReader(response);

            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);


            while((bufferedStrChunk = bufferedReader.readLine()) != null){
                stringBuilder.append(bufferedStrChunk);
            }



        }catch (MalformedURLException e)
        {
            e.printStackTrace();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        Log.e(" HTTP POST response",stringBuilder.toString()+" msg");
        return stringBuilder.toString();

    }

    public void PutHttpData(String urlString,String json)
    {
        try {
            URL url = new URL(urlString);
            HttpURLConnection urlconnection = (HttpURLConnection) url.openConnection();
            urlconnection.setRequestMethod("PUT");
            urlconnection.setDoOutput(true);
            byte []out = json.getBytes(Charset.forName("UTF-8"));
            int length = out.length;

            urlconnection.setFixedLengthStreamingMode(length);
            urlconnection.setRequestProperty("Content-Type","application/json; charset=UTF-8");
            urlconnection.connect();
            try
            {OutputStream os = urlconnection.getOutputStream();
                os.write(out);
            }
            catch(IOException e)
            {
                Log.e("PUT Exception",e+"");
                e.printStackTrace();
            }
            InputStream response = urlconnection.getInputStream();
        }catch (MalformedURLException e)
        {
            Log.e("PUT MAl Exception",e+"");
            e.printStackTrace();
        }
        catch (IOException e)
        {Log.e("PUT IO Exception",e+"");
            e.printStackTrace();
        }

    }

    public void DeleteHttpData(String urlString,String json)
    {
        try {
            URL url = new URL(urlString);
            HttpURLConnection urlconnection = (HttpURLConnection) url.openConnection();
            urlconnection.setRequestMethod("PUT");
            urlconnection.setDoOutput(true);
            byte []out = json.getBytes(Charset.forName("UTF-8"));
            int length = out.length;

            urlconnection.setFixedLengthStreamingMode(length);
            urlconnection.setRequestProperty("Content-Type","application/json; charset=UTF-8");
            urlconnection.connect();
            try
            {OutputStream os = urlconnection.getOutputStream();
                os.write(out);
            }catch (IOException e)
            {
                e.printStackTrace();
            }
            InputStream response = urlconnection.getInputStream();
        }catch (MalformedURLException e)
        {
            e.printStackTrace();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

    }
}
