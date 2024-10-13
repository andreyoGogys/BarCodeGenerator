//подключение к базе данных. покачто не работает


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DataBase {
    public static Connection conn;
	public static Statement statmt;
	public static ResultSet resSet;

    public static void Conn() throws ClassNotFoundException, SQLException 
	   {
		   conn = null;
		   Class.forName("org.sqlite.JDBC");
		   conn = DriverManager.getConnection("jdbc:sqlite:BarCodeDB.s3db");
		   
		   System.out.println("База Подключена!");
	   }
   
 
    // --------Заполнение таблицы--------
    public static void WriteDB(String code,String path) throws SQLException
    {
        statmt.execute("INSERT INTO 'BarCodes' ('code','codeIMG') VALUES ("+"'"+code+"'"+","+"'"+path+"'"+"); ");
       
        System.out.println(code+" и " +path + " Добавлено в БД");
    }
 
 // -------- Вывод таблицы--------
 public static void ReadDB() throws ClassNotFoundException, SQLException
    {
     resSet = statmt.executeQuery("SELECT * FROM BarCodes");
     int i =0;
     
     while(resSet.next())
     {
        System.out.println(resSet.getInt("code"));
        System.out.println(resSet.getInt("codeIMG")); 
        i++;
     }	
     }
 
     // --------Закрытие--------
     public static void CloseDB() throws ClassNotFoundException, SQLException
        {
         conn.close();
         statmt.close();
         resSet.close();
         
         System.out.println("Соединения закрыты");
        }
}
