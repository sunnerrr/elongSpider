package com.elong.spider.proxy;

import java.util.List;

import org.xml.sax.Parser;

import com.elong.spider.entity.Page;

public interface ListPageParser extends Parser {
    List parseListPage(Page page);
}
