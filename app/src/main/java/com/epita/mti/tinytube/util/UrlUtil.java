package com.epita.mti.tinytube.util;

import android.util.Log;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

/**
 * Created by mrollin on 29/11/14.
 */
public class UrlUtil {
    private static final String TAG = UrlUtil.class.getSimpleName();

    /**
     * Function that allows to get the rtsp url of a youtube video from its id
     *
     * @param videoId the id of the video
     * @return the rtsp url
     */
    public static String getUrlVideoRTSP(String videoId) {
        StringBuilder sb = new StringBuilder();
        sb.append("http://m.youtube.com/watch?v=");
        sb.append(videoId);
        String urlYoutube = sb.toString();

        try {
            DocumentBuilder documentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();

            sb = new StringBuilder();
            sb.append("http://gdata.youtube.com/feeds/api/videos/");
            sb.append(videoId);

            URL url = new URL(sb.toString());
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            Document doc = documentBuilder.parse(connection.getInputStream());
            Element el = doc.getDocumentElement();
            NodeList list = el.getElementsByTagName("media:content");
            String cursor = urlYoutube;

            for (int i = 0; i < list.getLength(); i++) {
                Node node = list.item(i);
                if (node != null) {
                    NamedNodeMap nodeMap = node.getAttributes();
                    HashMap<String, String> maps = new HashMap<>();
                    for (int j = 0; j < nodeMap.getLength(); j++) {
                        Attr att = (Attr) nodeMap.item(j);
                        maps.put(att.getName(), att.getValue());
                    }
                    if (maps.containsKey("yt:format")) {
                        String f = maps.get("yt:format");
                        if (maps.containsKey("url"))
                            cursor = maps.get("url");
                        if (f.equals("1"))
                            return cursor;
                    }
                }
            }
            return cursor;
        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }
        return urlYoutube;

    }
}
