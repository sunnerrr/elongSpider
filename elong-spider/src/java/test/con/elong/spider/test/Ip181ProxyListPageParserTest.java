package con.elong.spider.test;


import java.io.IOException;
import java.nio.charset.Charset;
import java.util.List;

import org.apache.http.HttpHost;
import org.apache.http.client.methods.HttpGet;
import org.junit.Test;

import com.elong.spider.core.util.HttpClientUtil;
import com.elong.spider.entity.Page;
import com.elong.spider.entity.Proxy;
import com.elong.spider.proxy.ProxyHttpClient;
import com.elong.spider.proxy.impl.Ip181ProxyListPageParser;

public class Ip181ProxyListPageParserTest {
    @Test
    public void testParse() throws IOException {
    	
        System.out.println(Charset.defaultCharset().toString());
        Page page = ProxyHttpClient.getInstance().getWebPage("http://www.ip181.com/daili/1.html","gb2312");
//        Page page = ProxyHttpClient.getInstance().getWebPage("http://www.ip181.com/daili/1.html", "gb2312");
        List<Proxy> urlList = new Ip181ProxyListPageParser().parse(page.getHtml());
        
        for(Proxy po:urlList){
        	System.out.println(po.getIp()+"   ");
        }
        String url="http://bj.meituan.com/shop/63500194";
        HttpGet tempRequest = new HttpGet(url);
        
        Proxy currentProxy=urlList.get(0);
        HttpHost proxy = new HttpHost(currentProxy.getIp(), currentProxy.getPort());
		tempRequest.setConfig(HttpClientUtil.getRequestConfigBuilder().setProxy(proxy).build());
		
		ProxyHttpClient proxyHttpClient = ProxyHttpClient.getInstance();
		
		Page page1 = proxyHttpClient.getWebPage(tempRequest);
		
		System.out.println(page1);
        System.out.println(urlList.size());
        
    }
    
}
