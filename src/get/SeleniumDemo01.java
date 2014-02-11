package get;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import com.thoughtworks.selenium.DefaultSelenium;

public class SeleniumDemo01
{
  @SuppressWarnings("resource")
  public static void main(String[] args)
   {
	  try {
		  String host = "localhost";
		  int port = 4444;
		  String url = "http://weibo.com/";
		  String browserType = "*googlechrome";
	 
		  String account = "xpath=//input[@name='username']";
		  String password = "xpath=//input[@name='password']";
		  String search = "xpath=//span[@node-type='submitStates']";
		  DefaultSelenium selenium = new DefaultSelenium(host,port,browserType,url);
		  selenium.start();
		  selenium.open(url);
		  selenium.type(account,"365948087@qq.com");
		  selenium.type(password,"54851495zyody");
		  selenium.click(search);
		  selenium.waitForPageToLoad("30000");
		  selenium.click("xpath=//a[@class='gn_name']");
		  while(true){
			  try{	  
				  selenium.waitForPageToLoad("50000");
				  break;
			  }catch(Exception e){
				  e.printStackTrace();
			  }
		  }
		  System.out.println("1");
		  String htmlsource1 = selenium.getHtmlSource();
		  File f1 = new File("./1.htm");
		  BufferedWriter output1;
		  output1 = new BufferedWriter(new FileWriter(f1));
		  output1.write(htmlsource1);
		  selenium.click("xpath=//a[@class='W_ico12 icon_choose']");
		  for(int i = 0; i < 5; i++){
			  System.out.println("2");
			  String htmlsource = selenium.getHtmlSource();
			  File f = new File("./2.htm");
			  BufferedWriter output;
			  output = new BufferedWriter(new FileWriter(f));
			  output.write(htmlsource);  
			  selenium.click("xpath=//a[@action-type='feed_list_delete']");
			  selenium.click("xpath=//a[@action-type='ok']");
//			  try {
//				Thread.sleep(3000);
//			} catch (InterruptedException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
			  selenium.refresh();
			  selenium.waitForPageToLoad("30000");
			  
		  }
	  } catch (IOException e) {
		  // TODO Auto-generated catch block
		  e.printStackTrace();
	  }
	  //selenium.stop();
	 
   }
}

