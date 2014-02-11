package get;

import java.io.BufferedInputStream;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import com.alibaba.fastjson.JSONObject;

public class GetHtml{

	private String username;
	private String password;
	@SuppressWarnings("deprecation")
	private DefaultHttpClient client;
	private String su;
	private String sp;
	private long servertime;
	private String nonce;
	private String rsakv;
	private String pubkey;
	
	private String errInfo;
	private String location;
	
	public boolean preLogin() {
		boolean flag = false;
		su = base64encode();
		String preLoginUrl = "http://login.sina.com.cn/sso/prelogin.php?entry=sso&callback=sinaSSOController.preloginCallBack&su="
				+ su + "&rsakt=mod&client=ssologin.js(v1.4.11)";
		
		HttpGet httpGet = new HttpGet(preLoginUrl);
		
		setGetHeader(httpGet);
		HttpResponse response = null;
		try {
			response = client.execute(httpGet);
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		HttpEntity entity = response.getEntity();
		String content = null;
		try {
			content = EntityUtils.toString(entity);
//			System.out.println(content);
			printout("prelogin",content);
			Pattern pattern = Pattern.compile("\\{.*\\}");
			Matcher m = pattern.matcher(content);
			while(m.find()){
				JSONObject o = JSONObject.parseObject(m.group());
				servertime = o.getLongValue("servertime");
				nonce = o.getString("nonce");
				rsakv = o.getString("rsakv");
				pubkey = o.getString("pubkey");
				flag = encodePwd();
//				System.out.println("flag");
			}
			
		} catch (ParseException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return flag;
	}
	
	public String getRequest(String url) throws ClientProtocolException, IOException{
		HttpGet httpGet = new HttpGet(url);
		httpGet.addHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.11 (KHTML, like Gecko) Chrome/23.0.1271.64 Safari/537.11");
		HttpResponse response = null;
		response = client.execute(httpGet);
		HttpEntity entity = response.getEntity();
		String content = EntityUtils.toString(entity,"UTF-8");
		return content;
	}
	
	public String postRequest(String url, ArrayList<BasicNameValuePair> nvps) throws ClientProtocolException, IOException{
		HttpPost httpPost = new HttpPost(url);
		httpPost.addHeader("Content-Type", "application/x-www-form-urlencoded");
		httpPost.addHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.11 (KHTML, like Gecko) Chrome/23.0.1271.64 Safari/537.11");
		HttpResponse response = client.execute(httpPost);
		HttpEntity entity = response.getEntity();
		String content = EntityUtils.toString(entity);
		return content; 
		
	}
	
	public boolean Login() {
		if(preLogin()){
			String url = "http://login.sina.com.cn/sso/login.php?client=ssologin.js(v1.4.11)";
			ArrayList<BasicNameValuePair> nvps = new ArrayList<BasicNameValuePair>();
			setList(nvps);
			HttpPost httpPost = new HttpPost(url);
			String content = null;
			try {
				setPostHeader(httpPost);
				UrlEncodedFormEntity e = new UrlEncodedFormEntity(nvps,"UTF-8");
				httpPost.setEntity(e);
				HttpResponse response = client.execute(httpPost);
				HttpEntity entity = response.getEntity();
				content = EntityUtils.toString(entity, "GBK");
				printout("login 1:",content);
//				System.out.println(content);
				Pattern p = Pattern.compile("location\\.replace\\(\"(.+?)\"\\);");
				Matcher m = p.matcher(content);
				if(m.find()){
					location = m.group(1);
					printout("location:",location);
//					System.out.println("loca"+location);
					if(location.contains("reason=")){
						errInfo = location.substring(location.indexOf("reason=")+7);
						errInfo = URLDecoder.decode(errInfo,"GBK");
					}
					else{
						HttpGet httpGet = new HttpGet(location);
						setGetHeader(httpGet);
						response = client.execute(httpGet);
						entity = response.getEntity();
						String res = EntityUtils.toString(entity, "GBK");
						printout("result: ",res);
						return true;
					}
				}
			} catch (ClientProtocolException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
		}		

		return false;
	}
	

	
	private void setList(ArrayList<BasicNameValuePair> nvps) {
		// TODO Auto-generated method stub
		nvps.add(new BasicNameValuePair("entry","weibo"));
		nvps.add(new BasicNameValuePair("gateway","1"));
		nvps.add(new BasicNameValuePair("from",""));
		nvps.add(new BasicNameValuePair("savestate","7"));
		nvps.add(new BasicNameValuePair("useticket","1"));
		nvps.add(new BasicNameValuePair("vsnf","1"));
		nvps.add(new BasicNameValuePair("su",su));
		nvps.add(new BasicNameValuePair("service","miniblog"));
		nvps.add(new BasicNameValuePair("servertime",servertime+""));
		nvps.add(new BasicNameValuePair("nonce",nonce));
		nvps.add(new BasicNameValuePair("pwencode","rsa2"));
		nvps.add(new BasicNameValuePair("rsakv",rsakv));
		nvps.add(new BasicNameValuePair("sp",sp));
		nvps.add(new BasicNameValuePair("encoding","UTF-8"));
		nvps.add(new BasicNameValuePair("prelt","182"));
		nvps.add(new BasicNameValuePair("returntype","META"));
		nvps.add(new BasicNameValuePair("pagerefer","http://login.sina.com.cn/sso/logout.php?entry=miniblog&r=http%3A%2F%2Fweibo.com%2Flogout.php%3Fbackurl%3D%2F"));
		nvps.add(new BasicNameValuePair("url","http://weibo.com/ajaxlogin.php?framelogin=1&callback=parent.sinaSSOController.feedBackUrlCallBack"));
	}
	
	private void setPostHeader(HttpPost httpPost) {
		// TODO Auto-generated method stub
//		httpPost.setHeader("Referer", "http://weibo.com");
		httpPost.addHeader("Content-Type", "application/x-www-form-urlencoded");
		httpPost.addHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.11 (KHTML, like Gecko) Chrome/23.0.1271.64 Safari/537.11");		
	}
	
	private void setGetHeader(HttpGet httpGet) {
		// TODO Auto-generated method stub
		httpGet.addHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.11 (KHTML, like Gecko) Chrome/23.0.1271.64 Safari/537.11");
	}
	
	
	GetHtml(String u, String p){
		client = new DefaultHttpClient();
		this.username = u;
		this.password = p;
		
	}
	
	public static void main(String[] args) throws ClientProtocolException, IOException {
		// TODO Auto-generated method stub
//		String u = "xbb12344@hotmail.com";
//		String p = "dirkgoogle";
		String u = "zycad11@126.com";
		String p = "111111zy";
		GetHtml wei = new GetHtml(u,p);
//		if(wei.Login()){
//			System.out.println("成功了");
//		}else{
//			System.out.println("failed");
//		}
//		Scanner s = new Scanner(System.in);
//		while(s.hasNext()){
//			String url = s.nextLine();
//			wei.getInfo(url);
//		}
		HttpGet httpGet = new HttpGet("http://www.soobb.com/");
		httpGet.addHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
		httpGet.addHeader("Accept-Encoding", "gzip,deflate,sdch");
//		httpGet.addHeader("Accept-Language", "zh-CN,zh;q=0.8,en;q=0.6");
		httpGet.addHeader("Cache-Control", "max-age=0");
		httpGet.addHeader("Connection", "keep-alive");
		httpGet.addHeader("Cookie", "CacheIdentifier=d572800b-1b1b-4ce8-8509-6b56433e1c4f; RememberMe=True; Gothan User Cookie=; __utma=210030487.1710716234.1387602224.1387778844.1387782638.5; __utmc=210030487; __utmz=210030487.1387782638.5.5.utmcsr=civi");
		httpGet.addHeader("Host", "www.soobb.com");
		httpGet.addHeader("Referer", "http://civitas.soobb.com/Mine/");
		httpGet.addHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/31.0.1650.57 Safari/537.36");
		HttpResponse response = null;
		response = wei.client.execute(httpGet);
		HttpEntity entity = response.getEntity();
		String content = EntityUtils.toString(entity,"UTF-8");
		System.out.println(content);
		File f = new File("./123.htm");
		BufferedWriter output = new BufferedWriter(new FileWriter(f));
		output.write(content);
		output.close();
	}
	
	private void getInfo(String url) {
		// TODO Auto-generated method stub
//		url = url + "?mod=TAB&ajaxpagelet=1";
//		String url = "http://weibo.com/p/1005052579400123/info?mod=TAB&ajaxpagelet=1";
		try {
			String content = getRequest(url);
//			extractInfo(content);
			System.out.println(content);
//			FileWriter fw = new FileWriter("./info.txt");
//			fw.write(content);
//			fw.close();
//			extractInfo(content);
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void extractInfo(String content) {
		// TODO Auto-generated method stub
		//<div class=\"label S_txt2\">昵称<\/div>\r\n\t\t\t<div class=\"con\">金小莺不能幸福更多<\/div>
		Pattern pattern = Pattern.compile("S_txt2\\\\\\\">([^<]+?)<([^<]+?)<([^>]+?)>([^<]+?)<");
		Matcher m = pattern.matcher(content);
		while(m.find()){
			System.out.println(m.group(1)+" "+m.group(4));
		}
		
	}

	public void printout(String str, String thing) throws IOException{
//		FileWriter fw = new FileWriter("./out.txt");
//		fw.append("\n"+str+"\n");
//		fw.append(thing);
//		fw.close();
		System.out.println("\n"+str);
		System.out.println(thing);
	}
	
	private boolean encodePwd(){
		ScriptEngineManager sem = new ScriptEngineManager();
		ScriptEngine se = sem.getEngineByName("javascript");
		try {
			se.eval(new FileReader("encoder.js"));
			Invocable invocableEngine = (Invocable) se;
			String callbackvalue=(String) invocableEngine.invokeFunction("encodePwd",pubkey,servertime,nonce,password);
			sp = callbackvalue;
		    return true;
		} catch (FileNotFoundException e) {
		    System.out.println("encoder.sj not found");
		} catch (ScriptException e) {
		    e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		}
		errInfo = "loading fail";
		return false;
	}
	
	private String base64encode(){
		ScriptEngineManager sem = new ScriptEngineManager();
		ScriptEngine se = sem.getEngineByName("javascript");
		try {
			se.eval(new FileReader("suencode.js"));
			Invocable invocableEngine = (Invocable) se;
			String callbackvalue=(String) invocableEngine.invokeFunction("suencode",username);
//			su = callbackvalue;
//			System.out.println(su);
		    return callbackvalue;
		} catch (FileNotFoundException e) {
		    System.out.println("suencode.sj not found");
		} catch (ScriptException e) {
		    e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		}
		errInfo = "loading fail";
		return "";
	}
}
