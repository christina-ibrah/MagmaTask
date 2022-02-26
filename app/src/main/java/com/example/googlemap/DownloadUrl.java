package com.example.googlemap;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

//return data in json format
public class DownloadUrl
{
    public String ReadUrl(String placeurl) throws IOException
    {
        String Data="";
        InputStream inputStream=null;
        HttpURLConnection httpURLConnection=null;
        try
        {
            URL url=new URL(placeurl);
            httpURLConnection=(HttpURLConnection) url.openConnection();
            httpURLConnection.connect();

            inputStream=httpURLConnection.getInputStream();
            BufferedReader bufferedReader=new BufferedReader(new InputStreamReader(inputStream));
           // StringBuffer stringBuffer=new StringBuffer();
            StringBuilder stringBuilder=new StringBuilder();
            String line="";
            while ((line =bufferedReader.readLine())!=null)
            {
                //stringBuffer.append(line);
                stringBuilder.append(line);
            }
            //Data=stringBuffer.toString();
            Data=stringBuilder.toString();
            bufferedReader.close();

        }
        catch (MalformedURLException e) {
            e.printStackTrace();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            inputStream.close();
            httpURLConnection.disconnect();
        }
      return Data;
    }
}
