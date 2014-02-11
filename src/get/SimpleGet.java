package get;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

public class SimpleGet{

	@SuppressWarnings("deprecation")
	private DefaultHttpClient client;	
	
	SimpleGet(){
		client = new DefaultHttpClient();		
	}
	
	public static void main(String[] args) throws ClientProtocolException, IOException {
		// TODO Auto-generated method stub
		SimpleGet get = new SimpleGet();
		HttpGet httpGet = new HttpGet("http://m.soobb.com/");
//		httpGet.addHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
//		httpGet.addHeader("Accept-Encoding", "gzip,deflate,sdch");
//		httpGet.addHeader("Accept-Language", "zh-CN,zh;q=0.8,en;q=0.6");
//		httpGet.addHeader("Cache-Control", "max-age=0");
//		httpGet.addHeader("Connection", "keep-alive");
		httpGet.addHeader("Cookie", "CacheIdentifier=86f9b077-5f68-46b4-8b79-2582bb2a9c8a; RememberMe=True; Gothan User Cookie=CDpE9oEdXDxyFvFXiC11r/+PGvFlqvMx8bOHW4ct0QSDvmXp0rLSsgIOZsYXWIwA; __utma=210030487.782385579.1387456963.1388676397.1388681396.9; __utmz=210030487.1388676397.8.7.utmcsr=civitas.soobb.com|utmccn=(referral)|utmcmd=referral|utmcct=/Mine/; __utma=267696722.954618239.1388801393.1388801393.1388801393.1; __utmb=267696722.1.10.1388801393; __utmc=267696722; __utmz=267696722.1388801393.1.1.utmcsr=(direct)|utmccn=(direct)|utmcmd=(none)");		
		httpGet.addHeader("Host", "m.soobb.com");
//		httpGet.addHeader("Referer", "http://civitas.soobb.com/Mine/");
		httpGet.addHeader("User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_9_0) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/31.0.1650.63 Safari/537.36");
		HttpResponse response = null;
		response = get.client.execute(httpGet);
		HttpEntity entity = response.getEntity();
		String content = EntityUtils.toString(entity,"UTF-8");
		System.out.println(content);
		File f = new File("./123.htm");
		BufferedWriter output = new BufferedWriter(new FileWriter(f));
		output.write(content);
		output.close();
	}
}
