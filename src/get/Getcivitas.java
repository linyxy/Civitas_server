package get;


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.thoughtworks.selenium.DefaultSelenium;

public class Getcivitas
{
  public static void main(String[] args)
  {
	  String host = "localhost";
	  int port = 4444;
	  String url = "http://m.soobb.com/Accounts/Login/";
	  String browserType = "*googlechrome";
 
//	  String keyWordsLocator = "document.getElementById('kw')";	  
//	  String search = "document.getElementById('su')";
	  
	  String account = "xpath=//input[@name='Email']";
	  String password = "xpath=//input[@name='Password']";
	  String login = "xpath=//a[@class='Submit']";
	  
	  DefaultSelenium selenium = new DefaultSelenium(host,port,browserType,url);
	  selenium.start();
	  selenium.open(url);
	  
//	  selenium.type(keyWordsLocator,"java selenium");
//	  selenium.click(search);
	  
	  selenium.type(account,"594779212@qq.com");
	  selenium.type(password,"xx19970305");
	  selenium.click(login);
	  selenium.waitForPageToLoad("50000");
	  
//	  <a href="http://civitas.soobb.com/" target="_blank">CIVITAS</a>
	  String jump = "xpath=//a[@href='http://www.soobb.com/Accounts/Settings/']";
	  selenium.click(jump);
	  selenium.waitForPageToLoad("60000");
	  for(int page=1;page<=27;page++){
		  selenium.open("http://civitas.soobb.com/Districts/56/Restaurants/?Page="+page);
		  String htmlsource = selenium.getHtmlSource();
		  
//		  <a class="HoverBlock" href="http://www.soobb.com/People/10037/">Sisyphus</a>关注你…</p>
		  Pattern p=Pattern.compile("<div class=\"Avatar\">.*?<div class=\"Subject Text\">.*?<h5><a href=\"(.*?)\">(.*?)</a>",Pattern.DOTALL);
		  Matcher m=p.matcher(htmlsource);
		  int i=0;
		  
		  Scanner scan = new Scanner(System.in);
		  SaveRecipe test = new SaveRecipe();
		  
		  while(m.find()){
			   i++;
			   String recipe = m.group(1);
			   String restaurant = m.group(2);
			   System.out.println("recipe:"+recipe+"    restaurant:"+restaurant);
			   String reciperurl = "http://civitas.soobb.com" + recipe;
			   selenium.open(reciperurl);
			   
			   htmlsource = selenium.getHtmlSource();
			   
			   test.saverecipe(restaurant,htmlsource);
		   
		  }
		  System.out.println(i);
	  }
	  
		
		
//	  System.out.println(htmlsource);
//	  File f = new File("./123.htm");
//		BufferedWriter output;
//		try {
//			output = new BufferedWriter(new FileWriter(f));
//			output.write(htmlsource);
//			output.close();
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//	  scan.nextLine();
	  selenium.stop();
  }
}
