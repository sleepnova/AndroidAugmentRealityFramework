package com.jwetherell.augmented_reality.data;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;

import com.jwetherell.augmented_reality.R;
import com.jwetherell.augmented_reality.ui.IconMarker;
import com.jwetherell.augmented_reality.ui.Marker;


/**
 * This class extends DataSource to fetch data from Google Buzz.
 * 
 * @author Justin Wetherell <phishman3579@gmail.com>
 */
public class FunDataSource extends NetworkDataSource {
	private static final String BASE_URL = "http://news.ebc.net.tw/iosapi/Default.aspx?distance=3000";

	private static Bitmap icon = null;
	
	public FunDataSource(Resources res) {
		if (res==null) throw new NullPointerException();
		createIcon(res);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String createRequestURL(double lat, double lon, double alt, float radius, String locale) {
		return BASE_URL+
		"&latitude=" + lat+
        "&longitude=" + lon;
	}

	@Override
	public List<Marker> parse(String url) {
        if (url == null)
            throw new NullPointerException();

        InputStream stream = null;
        stream = getHttpGETInputStream(url);
        if (stream == null)
            throw new NullPointerException();

        String string = null;
        string = getHttpInputString(stream);
        if (string == null)
            throw new NullPointerException();

        JSONArray json = null;
        try {
            json = new JSONArray(string);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (json == null)
            throw new NullPointerException();

        return parse(json);
	}
	
	public List<Marker> parse(JSONArray list) {
		if (list==null) throw new NullPointerException();
		
		JSONObject item = null;
		List<Marker> markers = new ArrayList<Marker>();

		try {
				int top = Math.min(MAX, list.length());
				for (int i = 0; i < top; i++) {					
					item = list.getJSONObject(i);
					Marker ma = processJSONObject(item);
					if(ma!=null) markers.add(ma);
				}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return markers;
	}

	private void createIcon(Resources res) {
//		icon=BitmapFactory.decodeResource(res, R.drawable.icon);
	}

	private Marker processJSONObject(JSONObject item) {
        Marker marker = null;
    	try {
    		marker = new IconMarker(
    				item.optString("name") + "\n" + item.optString("address"),
    				item.getDouble("latitude"),
    				item.getDouble("longitude"),
    				0,
    				Color.RED,
    				icon);
    	} catch (Exception e) {
    		e.printStackTrace();
    	}
        return marker;
	}

	@Override
	public List<Marker> parse(JSONObject root) {
		// TODO Auto-generated method stub
		return null;
	}
}
