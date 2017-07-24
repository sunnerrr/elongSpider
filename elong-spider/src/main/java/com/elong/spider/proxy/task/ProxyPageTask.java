//package com.elong.spider.proxy.task;
//
//import static com.crawl.proxy.ProxyPool.proxyQueue;
//
//import java.io.IOException;
//import java.util.List;
//
//import org.apache.http.HttpHost;
//import org.apache.http.HttpStatus;
//import org.apache.http.client.methods.HttpGet;
//import org.apache.log4j.Logger;
//
//
//import com.elong.spider.core.util.Config;
//import com.elong.spider.core.util.Constants;
//import com.elong.spider.core.util.HttpClientUtil;
//import com.elong.spider.core.util.SimpleLogger;
//import com.elong.spider.entity.Direct;
//import com.elong.spider.entity.Page;
//import com.elong.spider.entity.Proxy;
//import com.elong.spider.proxy.ProxyHttpClient;
//import com.elong.spider.proxy.ProxyListPageParser;
//import com.elong.spider.proxy.ProxyPool;
//
///**
// * 下载代理网页并解析
// * 若下载失败，通过代理去下载代理网页
// */
//public class ProxyPageTask implements Runnable{
//	private static Logger logger = SimpleLogger.getSimpleLogger(ProxyPageTask.class);
//	protected String url;
//	private boolean proxyFlag;//是否通过代理下载
//	private Proxy currentProxy;//当前线程使用的代理
//
//	protected static ProxyHttpClient proxyHttpClient = ProxyHttpClient.getInstance();
//	private ProxyPageTask(){
//
//	}
//	public ProxyPageTask(String url, boolean proxyFlag){
//		this.url = url;
//		this.proxyFlag = proxyFlag;
//	}
//	public void run(){
//		long requestStartTime = System.currentTimeMillis();
//		HttpGet tempRequest = null;
//		try {
//			Page page = null;
//			if (proxyFlag){
//				tempRequest = new HttpGet(url);
//				currentProxy = proxyQueue.take();
//				if(!(currentProxy instanceof Direct)){
//					HttpHost proxy = new HttpHost(currentProxy.getIp(), currentProxy.getPort());
//					tempRequest.setConfig(HttpClientUtil.getRequestConfigBuilder().setProxy(proxy).build());
//				}
//				page = proxyHttpClient.getWebPage(tempRequest);
//			}else {
//				page = proxyHttpClient.getWebPage(url);
//			}
//			page.setProxy(currentProxy);
//			int status = page.getStatusCode();
//			long requestEndTime = System.currentTimeMillis();
//			String logStr = Thread.currentThread().getName() + " " + getProxyStr(currentProxy) +
//					"  executing request " + page.getUrl()  + " response statusCode:" + status +
//					"  request cost time:" + (requestEndTime - requestStartTime) + "ms";
//			if(status == HttpStatus.SC_OK){
//				logger.debug(logStr);
//				handle(page);
//			} else {
//				logger.error(logStr);
//				Thread.sleep(100);
//				retry();
//			}
//		} catch (InterruptedException e) {
//			logger.error("InterruptedException", e);
//		} catch (IOException e) {
//			retry();
//		} finally {
//			if(currentProxy != null){
//				currentProxy.setTimeInterval(Constants.TIME_INTERVAL);
//				proxyQueue.add(currentProxy);
//			}
//			if (tempRequest != null){
//				tempRequest.releaseConnection();
//			}
//		}
//	}
//
//	/**
//	 * retry
//	 */
//	public void retry(){
//		proxyHttpClient.getProxyDownloadThreadExecutor().execute(new ProxyPageTask(url, Config.isProxy));
//	}
//
//	public void handle(Page page){
//		if (page.getHtml() == null || page.getHtml().equals("")){
//			return;
//		}
//
//		ProxyListPageParser parser = ProxyListPageParserFactory.
//				getProxyListPageParser(ProxyPool.proxyMap.get(url));
//		List<Proxy> proxyList = parser.parse(page.getHtml());
//		for(Proxy p : proxyList){
//			if(!ZhiHuHttpClient.getInstance().getDetailListPageThreadPool().isTerminated()){
//				ProxyPool.lock.readLock().lock();
//				boolean containFlag = ProxyPool.proxySet.contains(p);
//				ProxyPool.lock.readLock().unlock();
//				if (!containFlag){
//					ProxyPool.lock.writeLock().lock();
//					ProxyPool.proxySet.add(p);
//					ProxyPool.lock.writeLock().unlock();
//
//					proxyHttpClient.getProxyTestThreadExecutor().execute(new ProxyTestTask(p));
//				}
//			}
//		}
//	}
//
//	private String getProxyStr(Proxy proxy){
//		if (proxy == null){
//			return "";
//		}
//		return proxy.getIp() + ":" + proxy.getPort();
//	}
//}
