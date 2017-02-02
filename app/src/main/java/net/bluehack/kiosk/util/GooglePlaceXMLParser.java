package net.bluehack.kiosk.util;

import android.content.Context;
import android.location.Location;
import android.util.Xml;

import net.bluehack.kiosk.store.StoreData;
import net.bluehack.kiosk.store.StoreItem;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;

import static net.bluehack.kiosk.util.Logger.LOGD;
import static net.bluehack.kiosk.util.Logger.LOGE;
import static net.bluehack.kiosk.util.Logger.makeLogTag;

public class GooglePlaceXMLParser {

    private static final String TAG = makeLogTag(GooglePlaceXMLParser.class);
    private Context context;
    private StoreItem storeItem;
    private ArrayList<StoreItem> storeList;


    public GooglePlaceXMLParser(Context context){
        this.context = context;
    }

    /**https://maps.googleapis.com/maps/api/place/search/xml?location=37.3656998,127.1059626&radius=1000&sensor=false&types=cafe&key=AIzaSyA7av6NBZ3U-CwHzYMPex2M96OWP1zQz4Y*/
    public void getXmlData(Location locat, String radi, String type){

        String location = locat.getLatitude() + "," + locat.getLongitude();
        String radius = radi;
        String sensor = "false";
        String types = type;
        String key = "AIzaSyA7av6NBZ3U-CwHzYMPex2M96OWP1zQz4Y";
        String queryUrl = "https://maps.googleapis.com/maps/api/place/search/xml"
                +"?location="+location
                +"&radius="+radius
                +"&sensor="+sensor
                +"&types="+types
                +"&key="+key;

        try {
            URL url = new URL(queryUrl);
            InputStream inputStream = url.openStream();
            XmlPullParserFactory factory= XmlPullParserFactory.newInstance();
            XmlPullParser xpp= factory.newPullParser();
            xpp.setInput( new InputStreamReader(inputStream, "UTF-8"));

            int eventType = xpp.getEventType();

            while( eventType != XmlPullParser.END_DOCUMENT ) {

                switch (eventType) {
                    case XmlPullParser.START_DOCUMENT:

                        //storeList = new ArrayList<StoreItem>();
                        break;

                    case XmlPullParser.START_TAG:

                        //storeItem = new StoreItem();
                        String tag = xpp.getName();

                        if (tag.equals("name")) {

                            String tagNameValue = xpp.getText();
                            LOGD(TAG, "temp:" + tagNameValue);
                            //storeItem.setName(tagNameValue);
                        }
                        if (tag.equals("vicinity")) {

                            String tagAddrValue = xpp.nextText();
                            LOGD(TAG, "temp:" + tagAddrValue);
                            //storeItem.setAddress(tagAddrValue);
                        }
                        if (tag.equals("lat")) {

                            String tagLatValue = xpp.nextText();
                            LOGD(TAG, "temp:" + tagLatValue);
                            //storeItem.setLatitude(tagLatValue);
                        }
                        if (tag.equals("lng")) {

                            String tagLngValue = xpp.nextText();
                            LOGD(TAG, "temp:" + tagLngValue);
                            //storeItem.setLongitude(tagLngValue);
                        }
                        break;

                    case XmlPullParser.TEXT:
                        break;

                    case XmlPullParser.END_TAG:

                        /*String endTag = xpp.getName();
                        if (endTag.equals("PlaceSearchResponse")) {
                            storeList.add(storeItem);
                            LOGE(TAG, String.valueOf(storeList.size()));
                        }*/
                        break;
                }

                eventType = xpp.next();
            }

            //list 처리
            //거리계산 함수 호출


        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public int distanceLocation(String curLat, String curLng, String targetLat, String targetLng) {

        double distance, curLatitude, curLongitude, targetLatitude, targetLngitude;
        int meter;

        curLatitude = Double.valueOf(curLat);
        curLongitude = Double.valueOf(curLng);
        targetLatitude = Double.valueOf(targetLat);
        targetLngitude = Double.valueOf(targetLng);

        Location current = new Location("current");
        Location target = new Location("target");

        current.setLatitude(curLatitude);
        current.setLongitude(curLongitude);
        target.setLatitude(targetLatitude);
        target.setLongitude(targetLngitude);

        distance = current.distanceTo(target);
        meter = (int) distance;

        return meter;
    }
}
