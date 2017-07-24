package com.elong.spider.proxy;


import java.util.List;

import com.elong.spider.entity.Proxy;


public interface ProxyListPageParser extends Parser{
    /**
     * 是否只要匿名代理
     */
    static final boolean anonymousFlag = true;
    List<Proxy> parse(String content);
}
