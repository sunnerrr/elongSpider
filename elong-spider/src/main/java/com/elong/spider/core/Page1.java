package com.elong.spider.core;

public class Page1 {
    private String url;
    private int statusCode;//响应状态码
    private String html;//response content

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public String getHtml() {
        return html;
    }

    public void setHtml(String html) {
        this.html = html;
    }


    @Override
    public int hashCode() {
        return url.hashCode();
    }
}