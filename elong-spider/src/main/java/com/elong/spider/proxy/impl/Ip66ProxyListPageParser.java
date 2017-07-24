package com.elong.spider.proxy.impl;


import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.elong.spider.core.util.Constants;
import com.elong.spider.entity.Proxy;
import com.elong.spider.proxy.ProxyListPageParser;

/**
 * http://www.66ip.cn/
 */
public class Ip66ProxyListPageParser implements ProxyListPageParser {
    @Override
    public List<Proxy> parse(String content) {
        List<Proxy> proxyList = new ArrayList<>();
        if (content == null || content.equals("")){
            return proxyList;
        }
        Document document = Jsoup.parse(content);
        Elements elements = document.select("table tr:gt(1)");
        for (Element element : elements){
            String ip = element.select("td:eq(0)").first().text();
            String port  = element.select("td:eq(1)").first().text();
            String isAnonymous = element.select("td:eq(3)").first().text();
//            if(!anonymousFlag || isAnonymous.contains("匿")){
                proxyList.add(new Proxy(ip, Integer.valueOf(port), Constants.TIME_INTERVAL));
//            }
        }
        return proxyList;
    }
}
