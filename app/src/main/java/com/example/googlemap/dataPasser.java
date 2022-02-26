package com.example.googlemap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class dataPasser
{
    private HashMap<String,String> getplace(JSONObject googleplaceJson)
    {
        HashMap<String,String> googleplaceMap=new HashMap<>();
        String NameOfPlace="-NA-";
        String vicinity="-NA-";
        String latitude="";
        String longitude="";
        String ref="";

        try
        {
            if(!googleplaceJson.isNull("name")) {
                NameOfPlace = googleplaceJson.getString("name");
            }
            if(!googleplaceJson.isNull("vicinity"))
            {
                vicinity = googleplaceJson.getString("vicinity");
            }
            latitude=googleplaceJson.getJSONObject("geometry").getJSONObject("location").getString("lat");
            longitude=googleplaceJson.getJSONObject("geometry").getJSONObject("location").getString("lng");
            ref = googleplaceJson.getString("reference");

            googleplaceMap.put("place_name",NameOfPlace);
            googleplaceMap.put("vicinity",vicinity);
            googleplaceMap.put("lat",latitude);
            googleplaceMap.put("lng",longitude);
            googleplaceMap.put("ref",ref);


        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }
       return googleplaceMap;

    }

    private List<HashMap<String,String>> getAllNearpyPlaces(JSONArray jsonArray)
    {
        int c=jsonArray.length();
        List<HashMap<String,String>> listall=new ArrayList<>();

        HashMap<String,String> Nearpyplace= null;

        for(int i=0;i<c;i++)
        {
            try
            {
               // Nearpyplace=getplace((JSONObject) jsonArray.get(i));
                Nearpyplace=getplace(jsonArray.getJSONObject(i));

                listall.add(Nearpyplace);

            }
            catch (JSONException e)
            {
                e.printStackTrace();
            }

        }
        return  listall;
    }

    public List<HashMap<String,String>> parse(String jsondata)
    {
        JSONArray jsonArray=null;
        JSONObject jsonObject;
        try {

            jsonObject=new JSONObject(jsondata);
            jsonArray=jsonObject.getJSONArray("results");
        }
        catch (JSONException e) {
            e.printStackTrace();
        }
        return getAllNearpyPlaces(jsonArray);
    }
}
