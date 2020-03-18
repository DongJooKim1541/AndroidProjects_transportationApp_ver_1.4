package com.example.busappver14.ui.subway;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class Subway_findTimeTableParser {

    private SubwayItems si;
    private String myQuery1,myQuery2;

    public ArrayList<SubwayItems> Subway_findTimeTableApi(String subwayStationId, String dailyTypeCode, String upDownTypeCode, ArrayList<SubwayItems> list, int pageNo){
        try{
            String key="pL4Erh3b64mgmWQeg9qh3bQ4rP9SAu1zAOanR4S1I2U3dJ%2BPproQUh9jY93JXyYnj2NIuC7ShhgecOwvq25pmw%3D%3D";
            String urlStr="http://openapi.tago.go.kr/openapi/service/SubwayInfoService/getSubwaySttnAcctoSchdulList?subwayStationId="+subwayStationId+"&dailyTypeCode="+dailyTypeCode+"&upDownTypeCode="+upDownTypeCode+"&pageNo="+pageNo+"&serviceKey="+key;
            android.util.Log.d("KDJ","URL: "+urlStr);

            URL url=new URL(urlStr);

            HttpURLConnection connection=(HttpURLConnection)url.openConnection();

            connection.setRequestMethod("GET");

            connection.setRequestProperty("Content-Type","application/xml");

            XmlPullParserFactory factory=XmlPullParserFactory.newInstance();

            XmlPullParser parser=factory.newPullParser();

            parser.setInput(connection.getInputStream(),null);

            int parserEvent=parser.getEventType();

            while(parserEvent!= XmlPullParser.END_DOCUMENT){

                if(parserEvent==XmlPullParser.START_TAG){
                    String tagName=parser.getName();
                    if(tagName.equalsIgnoreCase("item")){
                        si=new SubwayItems();
                    }
                }

                if(parserEvent==XmlPullParser.START_TAG){

                    String tagName=parser.getName();

                    if(tagName.equalsIgnoreCase("totalCount")){
                        String totalCount=parser.nextText();
                        si.setTotalCount(totalCount);
                    }

                    if(tagName.equalsIgnoreCase("subwayRouteId")){
                        String subwayRouteId=parser.nextText();
                        si.setSubwayRouteId(subwayRouteId);
                    }

                    if(tagName.equalsIgnoreCase("subwayStationNm")){
                        String subwayStationNm=parser.nextText();
                        si.setEndSubwayStationNm(subwayStationNm);
                    }

                    if(tagName.equalsIgnoreCase("depTime")){
                        String depTime=parser.nextText();
                        si.setDepTime(depTime);
                    }

                    if(tagName.equalsIgnoreCase("arrTime")){
                        String arrTime=parser.nextText();
                        si.setArrTime(arrTime);
                        list.add(si);
                    }
                }
                parserEvent=parser.next();
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return list;
    }

    public SubwayItems getSi() {
        return si;
    }
}
