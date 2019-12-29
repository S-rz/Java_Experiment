package application;

import java.sql.*;
import java.util.Date;

import com.sun.org.apache.bcel.internal.generic.Select;

import jdk.nashorn.internal.objects.annotations.Where;

import java.text.SimpleDateFormat;
import sun.nio.cs.ext.ISCII91;

public class Link_MySQL{
	// JDBC 驱动名及数据库 URL
	public static final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";
	public static final String DB_URL = "jdbc:mysql://localhost/hospital?useSSL=false&serverTimezone=Asia/Shanghai&allowPublicKeyRetrieval=true";
    // 数据库的用户名与密码，需要根据自己的设置
	public static final String USER = "root";
	public static final String PASS = "123456";
	public static String  target_patient=null,target_doctor=null;
	
    public static boolean login_check(boolean flag,String usename,String password)
    {
    	boolean check_flag=false;
    	Connection conn = null;
        Statement stmt = null;
    	if(flag)
    	{  //医生登陆
    		try{
                // 注册 JDBC 驱动
            	Class.forName(JDBC_DRIVER);
                // 打开链接
                conn = DriverManager.getConnection(DB_URL,USER,PASS);
                // 执行查询
                stmt = conn.createStatement();
                String sql;
                sql = "SELECT * FROM t_ksys";
                ResultSet rs = stmt.executeQuery(sql);
                String ysbh=null,dlkl=null;
                // 展开结果集数据库
                while(rs.next()){
                    // 通过字段检索
                    ysbh  = rs.getString("YSBH");
                    dlkl  = rs.getString("DLKL");
                    
//                  System.out.println(ysbh);
//                  System.out.println(usename);
//            		System.out.println(dlkl);
//            		System.out.println(password);
            		
                    if(ysbh.equals(usename) && dlkl.equals(password))
                    {
                    	target_doctor=ysbh;
                    	check_flag=true;
                    	break;
                    }
                }
                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String target_time=df.format(new Date());
                String change="update t_ksys set DLRQ = \""+target_time+"\"\n"
                			 +"where YSBH = \""+ysbh+"\";\n";	
                stmt.execute(change);
                // 完成后关闭
                rs.close();
                stmt.close();
                conn.close();
            }catch(SQLException se){
                // 处理 JDBC 错误
                se.printStackTrace();
            }catch(Exception e){
                // 处理 Class.forName 错误
                e.printStackTrace();
            }finally{
                // 关闭资源
                try{
                    if(stmt!=null) stmt.close();
                }catch(SQLException se2){
                }// 什么都不做
                try{
                    if(conn!=null) conn.close();
                }catch(SQLException se){
                    se.printStackTrace();
                }
            }
    	}
    	else
    	{  //病人登陆
    		target_patient=null;
    		try{
                // 注册 JDBC 驱动
            	Class.forName(JDBC_DRIVER);

                // 打开链接
                conn = DriverManager.getConnection(DB_URL,USER,PASS);

                // 执行查询
                stmt = conn.createStatement();
                String sql;
                sql = "SELECT * FROM t_brxx";
                ResultSet rs = stmt.executeQuery(sql);

                String brbh=null,dlkl  = null;
                // 展开结果集数据库
                while(rs.next()){
                    // 通过字段检索
                    brbh  = rs.getString("BRBH");
                    dlkl  = rs.getString("DLKL");
                    if(brbh.equals(usename) && dlkl.equals(password))
                    {
                        target_patient=brbh;
                    	check_flag=true;
                    	break;
                    }
                }
                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String target_time=df.format(new Date());
                String change="update t_brxx set DLRQ = \""+target_time+"\"\n"
                			 +"where BRBH = \""+brbh+"\";\n";	
                stmt.execute(change);
                // 完成后关闭
                rs.close();
                stmt.close();
                conn.close();
            }catch(SQLException se){
                // 处理 JDBC 错误
                se.printStackTrace();
            }catch(Exception e){
                // 处理 Class.forName 错误
                e.printStackTrace();
            }finally{
                // 关闭资源
                try{
                    if(stmt!=null) stmt.close();
                }catch(SQLException se2){
                }// 什么都不做
                try{
                    if(conn!=null) conn.close();
                }catch(SQLException se){
                    se.printStackTrace();
                }
            }
    	}
    	return check_flag;
    }
    
    public static String get_target_patient() {
    	return target_patient;
    }
    
    public static String get_information(int num,String table,String field,String ksmc,String ysxm,String hzlb,String hzmc) {
    	Connection conn = null;
        Statement stmt = null;
        String information="";
        boolean flag=false;
		try{
            // 注册 JDBC 驱动
        	Class.forName(JDBC_DRIVER);
            // 打开链接
            conn = DriverManager.getConnection(DB_URL,USER,PASS);
            // 执行查询
            stmt = conn.createStatement();
            String sql;
            sql = "SELECT * FROM "+table;
            ResultSet rs = stmt.executeQuery(sql);
            // 展开结果集数据库
            while(rs.next()&& num!=0 ){
                // 通过字段检索
                information=rs.getString(field);
                num--;
            }
            if(num>0)
            {
            	information="-1";
            }
            if(information!="-1")
            {
            	if(!is_clear(ksmc))
            	{
            		flag=false;
            		String check_str=null,result_str=null,target_str=null;
            		if(field.equals("YSMC"))
            		{
            			target_str="YSMC";
                		check_str="SELECT t_ksys.YSMC FROM t_ksys,t_ksxx\n"
                				+ "where t_ksxx.KSMC=\""+ksmc+"\"\n"
                				+ "and t_ksxx.KSBH=t_ksys.KSBH;";
            		}
            		else if(field.equals("HZMC"))
            		{
            			target_str="HZMC";
            			check_str="SELECT t_hzxx.HZMC FROM t_ksxx,t_hzxx\n"
                				+ "where t_ksxx.KSMC=\""+ksmc+"\"\n"
                				+ "and t_ksxx.KSBH=t_hzxx.KSBH;";
					}
                    ResultSet check_rs = stmt.executeQuery(check_str);
    				while(check_rs.next()){
    	                // 通过字段检索
    	                result_str=check_rs.getString(target_str);
    	                if(result_str.equals(information))
    	                {
    	                	flag=true;
    	                	break;
    	                }
    	            }
    				if(!flag)
    				{
    					information="-2";
    				}
    				check_rs.close();
            	}
            	if(!is_clear(ysxm))
            	{
            		flag=false;
            		String check_str=null,result_str=null,target_str=null;
            		if(field.equals("KSMC"))
            		{
            			target_str="KSMC";
                		check_str="SELECT t_ksxx.KSMC FROM t_ksys,t_ksxx\n"
                				+ "where t_ksxx.KSBH=t_ksys.KSBH\n"
                				+ "and t_ksys.YSMC=\""+ysxm+"\";\n";
            		}
            		else if(field.equals("HZMC"))
            		{
        				target_str="HZMC";
            			if(is_zj(ysxm))
            			{
            				check_str="SELECT HZMC FROM t_hzxx,t_ksys\n"
            						+ "where t_hzxx.KSBH=t_ksys.KSBH\n"
                    				+ "and t_ksys.YSMC=\""+ysxm+"\";\n";
            			}
            			else 
            			{
            				check_str="SELECT HZMC FROM t_hzxx,t_ksys\n"
            						+ "where t_hzxx.KSBH=t_ksys.KSBH\n"
                    				+ "and t_ksys.YSMC=\""+ysxm+"\"\n"
                    				+ "and t_hzxx.SFZJ=\"0\";\n";
						}
					}
                    ResultSet check_rs = stmt.executeQuery(check_str);
    				while(check_rs.next()){
    	                // 通过字段检索
    	                result_str=check_rs.getString(target_str);
    	                if(result_str.equals(information))
    	                {
    	                	flag=true;
    	                	break;
    	                }
    	            }
    				if(!flag)
    				{
    					information="-2";
    				}
    				check_rs.close();
            	}
            	if(!is_clear(hzlb))
            	{
            		flag=false;
            		String check_str=null,result_str=null,target_str=null;
            		if(hzlb.equals("专家"))
            		{
            			hzlb="1";
            			if(field.equals("KSMC"))
                		{
                			target_str="KSMC";
                    		check_str="SELECT t_ksxx.KSMC FROM t_ksxx,t_hzxx\n"
                    				+ "where t_ksxx.KSBH=t_hzxx.KSBH\n"
                    				+ "and t_hzxx.SFZJ=\""+hzlb+"\";\n";
                		}
                		else if(field.equals("YSMC"))
                		{
                			target_str="YSMC";
            				check_str="SELECT YSMC FROM t_hzxx,t_ksys\n"
            						+ "where t_hzxx.KSBH=t_ksys.KSBH\n"
                    				+ "and t_ksys.SFZJ=\""+hzlb+"\";\n";
    					}
                		else if(field.equals("HZMC"))
                		{
                			target_str="HZMC";
            				check_str="SELECT HZMC FROM t_hzxx\n"
                    				+ "where t_hzxx.SFZJ=\""+hzlb+"\";\n";
                		}
            		}
            		else 
            		{
						hzlb="0";
						if(field.equals("KSMC"))
                		{
                			target_str="KSMC";
                    		check_str="SELECT t_ksxx.KSMC FROM t_ksxx,t_hzxx\n"
                    				+ "where t_ksxx.KSBH=t_hzxx.KSBH;\n";
                		}
                		else if(field.equals("YSMC"))
                		{
                			target_str="YSMC";
            				check_str="SELECT YSMC FROM t_hzxx,t_ksys\n"
            						+ "where t_hzxx.KSBH=t_ksys.KSBH\n";
    					}
                		else if(field.equals("HZMC"))
                		{
                			target_str="HZMC";
            				check_str="SELECT HZMC FROM t_hzxx\n"
                    				+ "where t_hzxx.SFZJ=\""+hzlb+"\";\n";
                		}
					}
                    ResultSet check_rs = stmt.executeQuery(check_str);
    				while(check_rs.next()){
    	                // 通过字段检索
    	                result_str=check_rs.getString(target_str);
    	                if(result_str.equals(information))
    	                {
    	                	flag=true;
    	                	break;
    	                }
    	            }
    				if(!flag)
    				{
    					information="-2";
    				}
    				check_rs.close();
            	}
            	if(!is_clear(hzmc))
            	{
            		flag=false;
            		String check_str=null,result_str=null,target_str=null;
            		if(field.equals("KSMC"))
            		{
            			target_str="KSMC";
                		check_str="SELECT t_ksxx.KSMC FROM t_hzxx,t_ksxx\n"
                				+ "where t_ksxx.KSBH=t_hzxx.KSBH\n"
                				+ "and t_hzxx.HZMC=\""+hzmc+"\";\n";
            		}
            		else if(field.equals("YSMC"))
            		{
        				target_str="YSMC";
            			if(is_hzzj(hzmc))
            			{
            				check_str="SELECT t_ksys.YSMC FROM t_hzxx,t_ksys\n"
            						+ "where t_hzxx.KSBH=t_ksys.KSBH\n"
            						+ "and t_hzxx.HZMC=\""+hzmc+"\"\n"
                    				+ "and t_ksys.SFZJ=\"1\";\n";
            			}
            			else 
            			{
            				check_str="SELECT t_ksys.YSMC FROM t_hzxx,t_ksys\n"
            						+ "where t_hzxx.KSBH=t_ksys.KSBH\n"
            						+ "and t_hzxx.HZMC=\""+hzmc+"\";\n";
						}
					}
                    ResultSet check_rs = stmt.executeQuery(check_str);
    				while(check_rs.next()){
    	                // 通过字段检索
    	                result_str=check_rs.getString(target_str);
    	                if(result_str.equals(information))
    	                {
    	                	flag=true;
    	                	break;
    	                }
    	            }
    				if(!flag)
    				{
    					information="-2";
    				}
    				check_rs.close();
            	}
            }
            // 完成后关闭
            rs.close();
            stmt.close();
            conn.close();
        }catch(SQLException se){
            // 处理 JDBC 错误
            se.printStackTrace();
        }catch(Exception e){
            // 处理 Class.forName 错误
            e.printStackTrace();
        }finally{
            // 关闭资源
            try{
                if(stmt!=null) stmt.close();
            }catch(SQLException se2){
            }// 什么都不做
            try{
                if(conn!=null) conn.close();
            }catch(SQLException se){
                se.printStackTrace();
            }
        }
		return information;
    }
    
    public static String get_hzxx(String ksmc,String ysxm,String hzlb,String hzmc) {
    	Connection conn = null;
        Statement stmt = null;
        String hzlb_str = null;
		try{
            // 注册 JDBC 驱动
        	Class.forName(JDBC_DRIVER);
            // 打开链接
            conn = DriverManager.getConnection(DB_URL,USER,PASS);
            // 执行查询
            stmt = conn.createStatement();
            if(is_clear(ksmc) && is_clear(ysxm) && is_clear(hzlb) && is_clear(hzmc))
            {
            	hzlb_str="2";
            }
            else
            {
            	if(!is_clear(ksmc))
            	{
                    boolean flag_zj=false,flag_pt=false;
            		String result_str=null;
                    String sql="SELECT t_ksys.SFZJ FROM t_ksys,t_ksxx\n"
            				+ "where t_ksxx.KSMC=\""+ksmc+"\"\n"
            				+ "and t_ksxx.KSBH=t_ksys.KSBH;";
                    ResultSet check_rs = stmt.executeQuery(sql);
    				while(check_rs.next()){
    	                // 通过字段检索
    	                result_str=check_rs.getString("SFZJ");
    	                if(result_str.equals("0"))
    	                {
    	                	flag_pt=true;
    	                }
    	                if(result_str.equals("1"))
    	                {
    	                	flag_zj=true;
    	                }
    	            }
    				if(flag_zj)
    					hzlb_str="2";
    				else if(flag_pt)
    					hzlb_str="1";
    				else 
    					hzlb_str="-1";
    				
    				check_rs.close();
            	}
            	if(!is_clear(ysxm))
            	{
            		boolean flag_zj=false,flag_pt=false;
            		String result_str=null;
                    String sql="SELECT t_ksys.SFZJ FROM t_ksys\n"
            				+ "where t_ksys.YSMC=\""+ysxm+"\"\n";
                    ResultSet check_rs = stmt.executeQuery(sql);
    				while(check_rs.next()){
    	                // 通过字段检索
    	                result_str=check_rs.getString("SFZJ");
    	                if(result_str.equals("0"))
    	                {
    	                	flag_pt=true;
    	                }
    	                if(result_str.equals("1"))
    	                {
    	                	flag_zj=true;
    	                }
    	            }
    				if(flag_zj)
    					hzlb_str="2";
    				else if(flag_pt)
    					hzlb_str="1";
    				else 
    					hzlb_str="-1";
    				
    				check_rs.close();
            	}
            	if(!is_clear(hzmc))
            	{
            		boolean flag_zj=false,flag_pt=false;
            		String result_str=null;
                    String sql="SELECT t_hzxx.SFZJ FROM t_hzxx\n"
            				+ "where t_hzxx.HZMC=\""+hzmc+"\"\n";
                    ResultSet check_rs = stmt.executeQuery(sql);
    				while(check_rs.next()){
    	                // 通过字段检索
    	                result_str=check_rs.getString("SFZJ");
    	                if(result_str.equals("0"))
    	                {
    	                	flag_pt=true;
    	                }
    	                if(result_str.equals("1"))
    	                {
    	                	flag_zj=true;
    	                }
    	            }
    				if(flag_zj)
    					hzlb_str="4";
    				else if(flag_pt)
    					hzlb_str="3";
    				else 
    					hzlb_str="-1";
    				
    				check_rs.close();
            	}
            }
            // 完成后关闭
            stmt.close();
            conn.close();
        }catch(SQLException se){
            // 处理 JDBC 错误
            se.printStackTrace();
        }catch(Exception e){
            // 处理 Class.forName 错误
            e.printStackTrace();
        }finally{
            // 关闭资源
            try{
                if(stmt!=null) stmt.close();
            }catch(SQLException se2){
            }// 什么都不做
            try{
                if(conn!=null) conn.close();
            }catch(SQLException se){
                se.printStackTrace();
            }
        }
		return hzlb_str;
    }
    
    public static boolean is_zj(String YSMC) {
    	Connection conn = null;
        Statement stmt = null;
        boolean flag=false;
		try{
            // 注册 JDBC 驱动
        	Class.forName(JDBC_DRIVER);
            // 打开链接
            conn = DriverManager.getConnection(DB_URL,USER,PASS);
            // 执行查询
            stmt = conn.createStatement();
            String sql;
            sql = "SELECT SFZJ FROM t_ksys\n"
            	 + "where YSMC=\""+YSMC+"\";\n";
            ResultSet rs = stmt.executeQuery(sql);

            // 展开结果集数据库
            while(rs.next()){
            	if(rs.getString("SFZJ").equals("1"))
                {
            		flag=true;
                }
            }
            // 完成后关闭
            rs.close();
            stmt.close();
            conn.close();
        }catch(SQLException se){
            // 处理 JDBC 错误
            se.printStackTrace();
        }catch(Exception e){
            // 处理 Class.forName 错误
            e.printStackTrace();
        }finally{
            // 关闭资源
            try{
                if(stmt!=null) stmt.close();
            }catch(SQLException se2){
            }// 什么都不做
            try{
                if(conn!=null) conn.close();
            }catch(SQLException se){
                se.printStackTrace();
            }
        }
		return flag;
    }
    
    public static boolean is_hzzj(String hzmc) {
    	Connection conn = null;
        Statement stmt = null;
        boolean flag=false;
		try{
            // 注册 JDBC 驱动
        	Class.forName(JDBC_DRIVER);
            // 打开链接
            conn = DriverManager.getConnection(DB_URL,USER,PASS);
            // 执行查询
            stmt = conn.createStatement();
            String sql;
            sql = "SELECT SFZJ FROM t_hzxx\n"
            	 + "where HZMC=\""+hzmc+"\";\n";
            ResultSet rs = stmt.executeQuery(sql);

            // 展开结果集数据库
            while(rs.next()){
            	if(rs.getString("SFZJ").equals("1"))
                {
            		flag=true;
                }
            }
            // 完成后关闭
            rs.close();
            stmt.close();
            conn.close();
        }catch(SQLException se){
            // 处理 JDBC 错误
            se.printStackTrace();
        }catch(Exception e){
            // 处理 Class.forName 错误
            e.printStackTrace();
        }finally{
            // 关闭资源
            try{
                if(stmt!=null) stmt.close();
            }catch(SQLException se2){
            }// 什么都不做
            try{
                if(conn!=null) conn.close();
            }catch(SQLException se){
                se.printStackTrace();
            }
        }
		return flag;
    }
    
    public static double money_need(String ksmc,String ysxm,String hzlb,String hzmc) {
    	Connection conn = null;
        Statement stmt = null;
        double moneyneed=0;
    	try{
            // 注册 JDBC 驱动
        	Class.forName(JDBC_DRIVER);

            // 打开链接
            conn = DriverManager.getConnection(DB_URL,USER,PASS);

            // 执行查询
            stmt = conn.createStatement();
            
            String sql;
            sql = "SELECT t_hzxx.GHFY FROM t_hzxx\n"
              	  +"where HZMC=\""+hzmc+"\";\n";
            ResultSet rs = stmt.executeQuery(sql);
            // 展开结果集数据库
            while(rs.next() ){
                // 通过字段检索
                moneyneed  = rs.getDouble("GHFY");
            }
            // 完成后关闭
            rs.close();
            stmt.close();
            conn.close();
        }catch(SQLException se){
            // 处理 JDBC 错误
            se.printStackTrace();
        }catch(Exception e){
            // 处理 Class.forName 错误
            e.printStackTrace();
        }finally{
            // 关闭资源
            try{
                if(stmt!=null) stmt.close();
            }catch(SQLException se2){
            }// 什么都不做
            try{
                if(conn!=null) conn.close();
            }catch(SQLException se){
                se.printStackTrace();
            }
        }
    	return moneyneed;
    }
    
    public static double money_remain(String ksmc,String ysxm,String hzlb,String hzmc) {
    	Connection conn = null;
        Statement stmt = null;
        double moneyhave=0,moneyneed=0;
    	try{
            // 注册 JDBC 驱动
        	Class.forName(JDBC_DRIVER);

            // 打开链接
            conn = DriverManager.getConnection(DB_URL,USER,PASS);

            // 执行查询
            stmt = conn.createStatement();
            
            String sql;
            sql = "SELECT YCJE FROM t_brxx\n"
            	  +"where BRBH=\""+target_patient+"\";\n";
            ResultSet rs = stmt.executeQuery(sql);

            // 展开结果集数据库
            while(rs.next() ){
                // 通过字段检索
                moneyhave  = rs.getDouble("YCJE");
            }
            
            sql = "SELECT t_hzxx.GHFY FROM t_hzxx\n"
            	  +"where HZMC=\""+hzmc+"\";\n";
            rs = stmt.executeQuery(sql);

            // 展开结果集数据库
            while(rs.next() ){
                // 通过字段检索
                moneyneed  = rs.getDouble("GHFY");
            }
            // 完成后关闭
            String change="update t_brxx set YCJE = \""+String.valueOf(moneyhave-moneyneed)+"\"\n"
       			 +"where BRBH = \""+target_patient+"\";\n";	
            stmt.execute(change);
            rs.close();
            stmt.close();
            conn.close();
        }catch(SQLException se){
            // 处理 JDBC 错误
            se.printStackTrace();
        }catch(Exception e){
            // 处理 Class.forName 错误
            e.printStackTrace();
        }finally{
            // 关闭资源
            try{
                if(stmt!=null) stmt.close();
            }catch(SQLException se2){
            }// 什么都不做
            try{
                if(conn!=null) conn.close();
            }catch(SQLException se){
                se.printStackTrace();
            }
        }
    	return  (moneyhave-moneyneed);
    }
    
    public static boolean moneyenough(String ksmc,String ysxm,String hzlb,String hzmc) {
    	Connection conn = null;
        Statement stmt = null;
        double moneyhave=0,moneyneed=0;
    	try{
            // 注册 JDBC 驱动
        	Class.forName(JDBC_DRIVER);

            // 打开链接
            conn = DriverManager.getConnection(DB_URL,USER,PASS);

            // 执行查询
            stmt = conn.createStatement();
            
            String sql;
            sql = "SELECT YCJE FROM t_brxx\n"
            	  +"where BRBH=\""+target_patient+"\";\n";
            ResultSet rs = stmt.executeQuery(sql);

            // 展开结果集数据库
            while(rs.next() ){
                // 通过字段检索
                moneyhave  = rs.getDouble("YCJE");
            }
            
            sql = "SELECT t_hzxx.GHFY FROM t_hzxx\n"
            	  +"where HZMC=\""+hzmc+"\";\n";
            rs = stmt.executeQuery(sql);

            // 展开结果集数据库
            while(rs.next() ){
                // 通过字段检索
                moneyneed  = rs.getDouble("GHFY");
            }
            // 完成后关闭
            rs.close();
            stmt.close();
            conn.close();
        }catch(SQLException se){
            // 处理 JDBC 错误
            se.printStackTrace();
        }catch(Exception e){
            // 处理 Class.forName 错误
            e.printStackTrace();
        }finally{
            // 关闭资源
            try{
                if(stmt!=null) stmt.close();
            }catch(SQLException se2){
            }// 什么都不做
            try{
                if(conn!=null) conn.close();
            }catch(SQLException se){
                se.printStackTrace();
            }
        }
    	
    	if(moneyhave>=moneyneed)
    	{
    		return true;
    	}
    	else
    	{
    		return false;
    	}
    }
    
    public static String get_gh(String ksmc,String ysxm,String hzlb,String hzmc) 
    {
    	int num=1,maxgh=0,targethz_num=1;
    	double moneyneed=0;
    	String ghbhString=null,hzbh_insert=null,ysbh_insert=null;
    	Connection conn = null;
        Statement stmt = null;
    	try{
            // 注册 JDBC 驱动
        	Class.forName(JDBC_DRIVER);

            // 打开链接
            conn = DriverManager.getConnection(DB_URL,USER,PASS);

            // 执行查询
            stmt = conn.createStatement();
            
            String sql;
            sql= "select GHRS from t_hzxx\n"
            	+"where HZMC=\""+hzmc+"\";\n";
            ResultSet rs=stmt.executeQuery(sql);
            while(rs.next())
            {
            	maxgh=rs.getInt("GHRS");
            }
            
            sql= "select HZBH from t_hzxx\n"
                	+"where HZMC=\""+hzmc+"\";\n";
            rs=stmt.executeQuery(sql);
            while(rs.next())
            {
            	hzbh_insert=rs.getString("HZBH");
            }
            
            
            sql= "select YSBH from t_ksys\n"
                	+"where YSMC=\""+ysxm+"\";\n";
            rs=stmt.executeQuery(sql);
            while(rs.next())
            {
                ysbh_insert=rs.getString("YSBH");
            }

            
            sql = "SELECT t_hzxx.GHFY FROM t_hzxx\n"
              	  +"where HZMC=\""+hzmc+"\";\n";
	        rs = stmt.executeQuery(sql);
	        while(rs.next())
            {
	        	moneyneed  = rs.getDouble("GHFY");
            }
            
            sql = "SELECT * FROM t_ghxx\n";
            rs = stmt.executeQuery(sql);
            // 展开结果集数据库
            while(rs.next() ){
                num++;
            }
            
              
            sql = "SELECT * FROM t_ghxx,t_hzxx\n"
            	 +"where t_hzxx.HZBH=t_ghxx.HZBH\n"
            	 +"and t_hzxx.HZMC=\""+hzmc+"\";\n";
            rs = stmt.executeQuery(sql);
            // 展开结果集数据库
            while(rs.next() ){
        	    String str=String.valueOf(rs.getDate("RQSJ"));
          	    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                String target_date=df.format(new Date());
          	    if(target_date.equals(str))
          	    {
          	    	targethz_num++;
          	    }
             } 
            
            if(targethz_num<=maxgh)
            {
            	ghbhString=String.valueOf(num);
            	SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String target_time=df.format(new Date());
            	sql="INSERT INTO t_ghxx\n"
            	   +"(GHBH,HZBH,YSBH,BRBH,GHRC,THBZ,GHFY,RQSJ,KBSJ)\n"
            	   +"VALUES\n"
            	   +"(\""+ghbhString+"\",\""+hzbh_insert+"\",\""+ysbh_insert
            	   +"\",\""+target_patient+"\",\""+String.valueOf(targethz_num)+"\",\"0\",\""+moneyneed+"\",\""+target_time+"\",\"0-0-0 0:0:0\");\n";
            	stmt.executeUpdate(sql);
            	//更新其余信息
            	String change="update t_ghxx set GHRC = \""+String.valueOf(targethz_num)+"\"\n"
           			 		 +"where HZBH = \""+hzbh_insert+"\";\n";
            	stmt.executeUpdate(change);
            }
            else 
            {
				ghbhString="-1";
			}
            rs.close();
            stmt.close();
            conn.close();
        }catch(SQLException se){
            // 处理 JDBC 错误
            se.printStackTrace();
        }catch(Exception e){
            // 处理 Class.forName 错误
            e.printStackTrace();
        }finally{
            // 关闭资源
            try{
                if(stmt!=null) stmt.close();
            }catch(SQLException se2){
            }// 什么都不做
            try{
                if(conn!=null) conn.close();
            }catch(SQLException se){
                se.printStackTrace();
            }
        }
    	return ghbhString;
    }
    
    public static String key_input(int num,String table,String field,String isinput) {
    	Connection conn = null;
        Statement stmt = null;
        String information="";
		try{
            // 注册 JDBC 驱动
        	Class.forName(JDBC_DRIVER);
            // 打开链接
            conn = DriverManager.getConnection(DB_URL,USER,PASS);
            // 执行查询
            stmt = conn.createStatement();
            String sql;
            sql = "SELECT "+field+ " FROM "+table+"\n"
            	 +"where PYZS like \""+isinput+"%\";\n";
            System.out.println(sql);
            ResultSet rs = stmt.executeQuery(sql);
            // 展开结果集数据库
            while(rs.next()&& num!=0 ){
                // 通过字段检索
                information=rs.getString(field);
                num--;
            }
            if(num>0)
            {
            	information="-1";
            }
            // 完成后关闭
            rs.close();
            stmt.close();
            conn.close();
        }catch(SQLException se){
            // 处理 JDBC 错误
            se.printStackTrace();
        }catch(Exception e){
            // 处理 Class.forName 错误
            e.printStackTrace();
        }finally{
            // 关闭资源
            try{
                if(stmt!=null) stmt.close();
            }catch(SQLException se2){
            }// 什么都不做
            try{
                if(conn!=null) conn.close();
            }catch(SQLException se){
                se.printStackTrace();
            }
        }
		return information;
    }
    
    public static boolean check_yes(String ksmc,String ysxm,String hzlb,String hzmc) {
    	boolean flag=false;
    	Connection conn = null;
        Statement stmt = null;
        String information="";
        try{
            // 注册 JDBC 驱动
        	Class.forName(JDBC_DRIVER);
            // 打开链接
            conn = DriverManager.getConnection(DB_URL,USER,PASS);
            // 执行查询
            stmt = conn.createStatement();
            
    		String check_str=null,result_str=null,target_str=null;
    		ResultSet check_rs=null;
			target_str="YSMC";
			information=ysxm;
    		check_str="SELECT t_ksys.YSMC FROM t_ksys,t_ksxx\n"
    				+ "where t_ksxx.KSMC=\""+ksmc+"\"\n"
    				+ "and t_ksxx.KSBH=t_ksys.KSBH;";
    		check_rs= stmt.executeQuery(check_str);
    		flag=false;
			while(check_rs.next()){
                // 通过字段检索
                result_str=check_rs.getString(target_str);
                if(result_str.equals(information))
                {
                	flag=true;
                	break;
                }
            }
			if(!flag)
				return false;
			
			target_str="HZMC";
			information=hzmc;
			check_str="SELECT t_hzxx.HZMC FROM t_ksxx,t_hzxx\n"
    				+ "where t_ksxx.KSMC=\""+ksmc+"\"\n"
    				+ "and t_ksxx.KSBH=t_hzxx.KSBH;";
            check_rs = stmt.executeQuery(check_str);
            flag=false;
            while(check_rs.next()){
                // 通过字段检索
                result_str=check_rs.getString(target_str);
                if(result_str.equals(information))
                {
                	flag=true;
                	break;
                }
            }
            if(!flag)
				return false;
            
            if(hzlb.equals("专家"))
            {
            	hzlb="1";
            	target_str="YSMC";
    			information=ysxm;
				check_str="SELECT YSMC FROM t_hzxx,t_ksys\n"
						+ "where t_hzxx.KSBH=t_ksys.KSBH\n"
        				+ "and t_ksys.SFZJ=\""+hzlb+"\";\n";
                check_rs = stmt.executeQuery(check_str);
                flag=false;
                while(check_rs.next()){
                    // 通过字段检索
                    result_str=check_rs.getString(target_str);
                    if(result_str.equals(information))
                    {
                    	flag=true;
                    	break;
                    }
                }
                if(!flag)
    				return false;
                
                target_str="HZMC";
    			information=hzmc;
    			check_str="SELECT HZMC FROM t_hzxx\n"
        				+ "where t_hzxx.SFZJ=\""+hzlb+"\";\n";
                check_rs = stmt.executeQuery(check_str);
                flag=false;
                while(check_rs.next()){
                    // 通过字段检索
                    result_str=check_rs.getString(target_str);
                    if(result_str.equals(information))
                    {
                    	flag=true;
                    	break;
                    }
                }
                if(!flag)
    				return false;
            }
            else if(hzlb.equals("普通"))
            {
            	target_str="HZMC";
    			information=hzmc;
    			check_str="SELECT HZMC FROM t_hzxx\n"
        				+ "where t_hzxx.SFZJ=\""+hzlb+"\";\n";
                check_rs = stmt.executeQuery(check_str);
                flag=false;
                while(check_rs.next()){
                    // 通过字段检索
                    result_str=check_rs.getString(target_str);
                    if(result_str.equals(information))
                    {
                    	flag=true;
                    	break;
                    }
                }
                if(!flag)
    				return false;
			}
            else 
            {
				flag=false;
			}
            if(!flag)
				return false;
            // 完成后关闭
            check_rs.close();
            stmt.close();
            conn.close();
        }catch(SQLException se){
            // 处理 JDBC 错误
            se.printStackTrace();
        }catch(Exception e){
            // 处理 Class.forName 错误
            e.printStackTrace();
        }finally{
            // 关闭资源
            try{
                if(stmt!=null) stmt.close();
            }catch(SQLException se2){
            }// 什么都不做
            try{
                if(conn!=null) conn.close();
            }catch(SQLException se){
                se.printStackTrace();
            }
        }
        if(!flag)
			return false;
    	else {
    		return true;
		}
    }
    
    public static String get_doctor_brlb(int num, String index) {
    	Connection conn = null;
        Statement stmt = null;
        String information="";
		try{
            // 注册 JDBC 驱动
        	Class.forName(JDBC_DRIVER);
            // 打开链接
            conn = DriverManager.getConnection(DB_URL,USER,PASS);
            // 执行查询
            stmt = conn.createStatement();
            String sql=null;
            sql = "SELECT "+index +" FROM t_ghxx\n"
                 +"where YSBH = \""+target_doctor+"\";\n";
            if(index.equals("SFZJ"))
            {
            	sql="SELECT SFZJ FROM t_ghxx,t_hzxx\n"
                   +"where t_ghxx.YSBH = \""+target_doctor+"\"\n"
                   +"and t_ghxx.HZBH=t_hzxx.HZBH;\n";
            }
            ResultSet rs = stmt.executeQuery(sql);
            // 展开结果集数据库
            while(rs.next()&& num!=0 ){
                // 通过字段检索
                if(!index.equals("RQSJ"))
                {
                	information=rs.getString(index);
               	}
                else 
                {
                	information=String.valueOf(rs.getTimestamp(index));
				}
                num--;
            }
            if(index.equals("BRBH")) 
            {
				String change="SELECT BRMC FROM t_brxx\n"
		                 	 +"where BRBH = \""+information+"\";\n";
				ResultSet change_rs=stmt.executeQuery(change);
				if(change_rs.next())
				{
					information=change_rs.getString("BRMC");
				}
			}
            if(num>0)
            {
            	information="-1";
            }
            // 完成后关闭
            rs.close();
            stmt.close();
            conn.close();
        }catch(SQLException se){
            // 处理 JDBC 错误
            se.printStackTrace();
        }catch(Exception e){
            // 处理 Class.forName 错误
            e.printStackTrace();
        }finally{
            // 关闭资源
            try{
                if(stmt!=null) stmt.close();
            }catch(SQLException se2){
            }// 什么都不做
            try{
                if(conn!=null) conn.close();
            }catch(SQLException se){
                se.printStackTrace();
            }
        }
		return information;
    }
    
    public static String get_doctor_srlb(int num,String index) {
    	Connection conn = null;
        Statement stmt = null;
        String information="";
		try{
            // 注册 JDBC 驱动
        	Class.forName(JDBC_DRIVER);
            // 打开链接
            conn = DriverManager.getConnection(DB_URL,USER,PASS);
            // 执行查询
            stmt = conn.createStatement();
            String sql=null;
            sql = "SELECT * from t_ksys\n";
            ResultSet rs = stmt.executeQuery(sql);
            // 展开结果集数据库
            while(rs.next()&& num!=0 ){
                // 通过字段检索
                information=rs.getString(index);
                num--;
            }
            if(num>0)
            {
            	information="-1";
            }
            // 完成后关闭
            rs.close();
            stmt.close();
            conn.close();
        }catch(SQLException se){
            // 处理 JDBC 错误
            se.printStackTrace();
        }catch(Exception e){
            // 处理 Class.forName 错误
            e.printStackTrace();
        }finally{
            // 关闭资源
            try{
                if(stmt!=null) stmt.close();
            }catch(SQLException se2){
            }// 什么都不做
            try{
                if(conn!=null) conn.close();
            }catch(SQLException se){
                se.printStackTrace();
            }
        }
		return information;
    }
    
    public static String get_ksmc(String ysmc)
    {
    	Connection conn = null;
        Statement stmt = null;
        String information="";
		try{
            // 注册 JDBC 驱动
        	Class.forName(JDBC_DRIVER);
            // 打开链接
            conn = DriverManager.getConnection(DB_URL,USER,PASS);
            // 执行查询
            stmt = conn.createStatement();
            String sql=null;
            sql = "SELECT * from t_ksys,t_ksxx\n"
            	+ "where t_ksxx.KSBH=t_ksys.KSBH\n"
            	+ "and t_ksys.YSMC = \""+ysmc+"\";\n";
            ResultSet rs = stmt.executeQuery(sql);
            // 展开结果集数据库
            while(rs.next()){
                // 通过字段检索
            	information=rs.getString("KSMC");
            }
            // 完成后关闭
            rs.close();
            stmt.close();
            conn.close();
        }catch(SQLException se){
            // 处理 JDBC 错误
            se.printStackTrace();
        }catch(Exception e){
            // 处理 Class.forName 错误
            e.printStackTrace();
        }finally{
            // 关闭资源
            try{
                if(stmt!=null) stmt.close();
            }catch(SQLException se2){
            }// 什么都不做
            try{
                if(conn!=null) conn.close();
            }catch(SQLException se){
                se.printStackTrace();
            }
        }
		return information;
    }
    
    public static double get_ghrc_srhj(String sfzj,String index,String ysbh) {
    	Connection conn = null;
        Statement stmt = null;
        int num=0;
        double fee=0; 
		try{
            // 注册 JDBC 驱动
        	Class.forName(JDBC_DRIVER);
            // 打开链接
            conn = DriverManager.getConnection(DB_URL,USER,PASS);
            // 执行查询
            stmt = conn.createStatement();
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            String target_time=df.format(new Date());
            String sql=null;
            sql = "select t_ghxx.GHRC,t_ghxx.GHFY,t_ghxx.RQSJ  from t_hzxx,t_ghxx\n"
            	+ "where t_ghxx.YSBH= \""+ysbh+"\"\n"
            	+ "and t_hzxx.HZBH=t_ghxx.HZBH "
            	+ " and t_hzxx.SFZJ=\""+sfzj+"\";\n";
            //System.out.println(sql);
            ResultSet rs = stmt.executeQuery(sql);
            // 展开结果集数据库
            if(rs.next() ){
                // 通过字段检索
            	//System.out.println("table: "+String.valueOf(rs.getDate("RQSJ")));
                //System.out.println("target: "+target_time);
            	if(String.valueOf(rs.getDate("RQSJ")).equals(target_time))
                {
            		num=rs.getInt("GHRC");
            		fee=rs.getDouble("GHFY");
                }
            }
            // 完成后关闭
            rs.close();
            stmt.close();
            conn.close();
        }catch(SQLException se){
            // 处理 JDBC 错误
            se.printStackTrace();
        }catch(Exception e){
            // 处理 Class.forName 错误
            e.printStackTrace();
        }finally{
            // 关闭资源
            try{
                if(stmt!=null) stmt.close();
            }catch(SQLException se2){
            }// 什么都不做
            try{
                if(conn!=null) conn.close();
            }catch(SQLException se){
                se.printStackTrace();
            }
        }
		if(index.equals("GHRC"))
		{
			return Double.valueOf(num);
		}
		else 
		{
			return (num*fee);
		}
    }
    
    private static boolean is_clear(String str) {
    	if(str==null || str.equals(""))
    	{
    		return true;
    	}
    	return false;
    }
}
