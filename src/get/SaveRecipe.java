package get;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class SaveRecipe {
	
	// 创建静态全局变量
	static Connection conn;
	static Statement st;
	static String itemid = 1+"";
	
	public static void insert(String itemid, String restaurant, String recipe, String tips, String todaysupply, String hissupply, String hunguryC, String happyC, String healthC, String vigorC, String luxuryD) {
		
		conn = getConnection();	// 首先要获取连接，即连接到数据库

		try {
			String sql = "INSERT INTO recipe(itemid, restaurant, recipe, tips, todaysupply, hissupply, hunguryC, happyC, healthC, vigorC, luxuryD)"
					+ " VALUES ("+itemid+", '"+restaurant+"', '"+recipe+"', '"+tips+"', '"+todaysupply+"', '"+hissupply+"', '"+hunguryC+"', '"+happyC+"', '"+healthC+"', '"+vigorC+"', '"+luxuryD+"')";	// 插入数据的sql语句
			
			st = (Statement) conn.createStatement();	// 创建用于执行静态sql语句的Statement对象
			
			int count = st.executeUpdate(sql);	// 执行插入操作的sql语句，并返回插入数据的个数
			
			System.out.println("向recipe表中插入 " + count + " 条数据");	//输出插入操作的处理结果
			
			conn.close();	//关闭数据库连接
			
		} catch (SQLException e) {
			System.out.println("插入数据失败" + e.getMessage());
		}
	}
	
	/* 更新符合要求的记录，并返回更新的记录数目*/
	public static void update() {
		conn = getConnection();	//同样先要获取连接，即连接到数据库
		try {
			String sql = "update staff set wage='2200' where name = 'lucy'";// 更新数据的sql语句
			
			st = (Statement) conn.createStatement();	//创建用于执行静态sql语句的Statement对象，st属局部变量
			
			int count = st.executeUpdate(sql);// 执行更新操作的sql语句，返回更新数据的个数
			
			System.out.println("staff表中更新 " + count + " 条数据");		//输出更新操作的处理结果
			
			conn.close();	//关闭数据库连接
			
		} catch (SQLException e) {
			System.out.println("更新数据失败");
		}
	}

	/* 查询数据库，输出符合要求的记录的情况*/
	public static void query() {
		
		conn = getConnection();	//同样先要获取连接，即连接到数据库
		try {
			String sql = "select * from staff";		// 查询数据的sql语句
			st = (Statement) conn.createStatement();	//创建用于执行静态sql语句的Statement对象，st属局部变量
			
			ResultSet rs = st.executeQuery(sql);	//执行sql查询语句，返回查询数据的结果集
			System.out.println("最后的查询结果为：");
			while (rs.next()) {	// 判断是否还有下一个数据
				
				// 根据字段名获取相应的值
				String name = rs.getString("name");
				int age = rs.getInt("age");
				String sex = rs.getString("sex");
				String address = rs.getString("address");
				String depart = rs.getString("depart");
				String worklen = rs.getString("worklen");
				String wage = rs.getString("wage");
				
				//输出查到的记录的各个字段的值
				System.out.println(name + " " + age + " " + sex + " " + address
						+ " " + depart + " " + worklen + " " + wage);
			
			}
			conn.close();	//关闭数据库连接
			
		} catch (SQLException e) {
			System.out.println("查询数据失败");
		}
	}

	/* 删除符合要求的记录，输出情况*/
	public static void delete() {

		conn = getConnection();	//同样先要获取连接，即连接到数据库
		try {
			String sql = "delete from staff  where name = 'lili'";// 删除数据的sql语句
			st = (Statement) conn.createStatement();	//创建用于执行静态sql语句的Statement对象，st属局部变量
			
			int count = st.executeUpdate(sql);// 执行sql删除语句，返回删除数据的数量
			
			System.out.println("staff表中删除 " + count + " 条数据\n");	//输出删除操作的处理结果
			
			conn.close();	//关闭数据库连接
			
		} catch (SQLException e) {
			System.out.println("删除数据失败");
		}
		
	}
	
	/* 获取数据库连接的函数*/
	public static Connection getConnection() {
		Connection con = null;	//创建用于连接数据库的Connection对象
		try {
			Class.forName("com.mysql.jdbc.Driver");// 加载Mysql数据驱动
			
			con = DriverManager.getConnection(
					"jdbc:mysql://localhost:3306/civitas", "root", "zhuyu");// 创建数据连接
			
		} catch (Exception e) {
			System.out.println("数据库连接失败" + e.getMessage());
		}
		return con;	//返回所建立的数据库连接
	}
	

	public SaveRecipe() {
		// TODO Auto-generated constructor stub
	}

	public void saverecipe(String restaurant, String htmlsource) {
		// TODO Auto-generated method stub
		
		   
		   System.out.println(htmlsource);
		   
//		   p=Pattern.compile("<div class=\"StatisticsRow RecipeGroup\">.*?"+
//		   "<h5><a href=\".*?\">(.*?)</a></h5>.*?<p class=\"Tips\">(.*?)</p>"+
//				   "</div>",Pattern.DOTALL);
		   Pattern p=Pattern.compile("<div class=\"StatisticsRow RecipeGroup\">.*?<h5><a href=\".*?\">(.*?)</a></h5>.*?<p class=\"Tips\">(.*?)</p>.*?</div>.*?"+
                                     "<div class=\"Text\">.*?<p class=\"Number\">([0-9]+)</p>.*?"+//今日供应
                                     "<div class=\"Text Text2\">.*?<p class=\"Number\">([0-9]+)</p>.*?"+//历史供应
                                     "<div class=\"Text\">.*?<p class=\"Number Positive\">(.*?)</p>.*?"+//饥饿变化
                                     "<div class=\"Text\">.*?<p class=\"Number Positive\">(.*?)</p>.*?"+//快乐变化
                                     "<div class=\"Text\">.*?<p class=\"Number Positive\">(.*?)</p>.*?"+//健康变化
                                     "<div class=\"Text\">.*?<p class=\"Number Positive\">(.*?)</p>.*?"+//精力变化
                                     "<div class=\"Text\">.*?<p class=\"Number Positive\">(.*?)</p>.*?"//奢侈程度
				                     ,Pattern.DOTALL);
		   Matcher m=p.matcher(htmlsource);
		   System.out.println("ready to enter");
		   
		   while(m.find()){
			   System.out.println("enter now");
			   String recipe = m.group(1);
			   String tips = m.group(2);
			   String todaysupply = m.group(3);
			   String hissupply = m.group(4);
			   String hunguryC = m.group(5);
			   String happyC = m.group(6);
			   String healthC = m.group(7);
			   String vigorC = m.group(8);
			   String luxuryD = m.group(9);
			   System.out.println("recipe:" + recipe + "    tips:" + tips + "    今日供应:" + 
			   todaysupply + "    历史供应:"+ hissupply +"    饥饿变化:"+hunguryC+"    快乐变化:" + 
			   happyC + "    健康变化:" + healthC + "    精力变化:" + vigorC + "    奢侈程度:" + luxuryD);
			   insert(itemid, restaurant, recipe, tips, todaysupply, hissupply, hunguryC, happyC, healthC, vigorC, luxuryD);
			   int tmp = Integer.parseInt(itemid);
			   tmp++;
			   itemid = tmp + "";
			  }

	}
	
	public static void main(String[] args) {
		
		File file = new File("./1.txt");
		   BufferedReader input;
		   StringBuffer tmpb = new StringBuffer();
			try {
				input = new BufferedReader(new FileReader(file));
				String tmp = input.readLine();
				while(tmp!=null){
					tmpb.append(tmp+"\r\n");
					tmp = input.readLine();
				}
				
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		   String htmlsource = tmpb.toString();
		   SaveRecipe test = new SaveRecipe();
		   test.saverecipe("红岸--全民双百特惠餐",htmlsource);
	}

}
