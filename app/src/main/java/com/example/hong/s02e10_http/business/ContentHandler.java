package com.example.hong.s02e10_http.business;

import android.util.Log;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * Created by lenovo on 2017/11/29.
 */

public class ContentHandler extends DefaultHandler {
    private final String TAG = "ContentHandler";

    private String nodeName;
    private StringBuilder name;
    private StringBuilder calories;
    private StringBuilder price;

    @Override
    public void startDocument() throws SAXException {
        name = new StringBuilder();
        calories = new StringBuilder();
        price = new StringBuilder();
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        // 记录当前结点
        nodeName = localName;
    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        // 根据当前的节点判断将内容添加到哪一个StringBuilder对象中
        if ("name".equals(nodeName)) {
            name.append(ch, start, length);
        } else if ("calories".equals(nodeName)) {
            calories.append(ch, start, length);
        } else if ("price".equals(nodeName)) {
            price.append(ch, start, length);
        }
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        if ("food".equals(localName)) {
            Log.e(TAG, "name is " + name.toString().trim());
            Log.e(TAG, "calories is " + calories.toString().trim());
            Log.e(TAG, "price is " + price.toString().trim());
            // 最后要将StringBuilder清空掉
            name.setLength(0);
            price.setLength(0);
            calories.setLength(0);
        }
    }

    @Override
    public void endDocument() throws SAXException {
        super.endDocument();
    }
}
