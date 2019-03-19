package com.fantasy_travel.loginpage;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class Dataparser {

    private HashMap<String, String> getDuration(JSONArray googleDirectionsJson)
    {
        HashMap<String, String> googleDirectionsMap = new HashMap<>();
        String duration = "";
        String distance = "";

        Log.d("JSON response", googleDirectionsJson.toString());
        try
        {
            duration = googleDirectionsJson.getJSONObject(0).getJSONObject("duration").getString("text");
            distance = googleDirectionsJson.getJSONObject(0).getJSONObject("distance").getString("text");

            googleDirectionsMap.put("duration", duration);
            googleDirectionsMap.put("distance", distance);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return googleDirectionsMap;
    }

    public String[] parseDirections(String jsonData)
    {
        JSONArray jsonArray = null;
        JSONObject jsonObject;

        try
        {
            jsonObject = new JSONObject(jsonData);
            jsonArray = jsonObject.getJSONArray("routes").getJSONObject(0).getJSONArray("legs").getJSONObject(0).getJSONArray("steps");
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }

        return getPaths(jsonArray);
    }

    public String[] getPaths(JSONArray googleStepsJSON)
    {
        int count = googleStepsJSON.length();
        String[] polylines = new String[count];

        for(int i=0; i< count; i++)
        {
            try {
                Log.d("backend","getting polyline paths"+ polylines[i]);
                polylines[i] = getPath(googleStepsJSON.getJSONObject(i));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        Log.d("backend","got polyline paths"+ polylines);
        return polylines;
    }

    public String getPath(JSONObject googlePathJSON)
    {
        String polyline = null;
        try
        {
            polyline = googlePathJSON.getJSONObject("polyline").getString("points");
        }
        catch(JSONException e)
        {
            e.printStackTrace();
        }

        return polyline;
    }
}
