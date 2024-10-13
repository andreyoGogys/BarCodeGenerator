//подключение к базе данных.
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import org.sqlite.JDBC;

public class DataBase {
    public static Connection conn;
	public static Statement statmt;
	public static ResultSet resSet;

    public DataBase(){
        try {
            Conn();
        } catch (ClassNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
    }

    public static void Conn() throws ClassNotFoundException, SQLException 
	   {
		   conn = null;
		   Class.forName("org.sqlite.JDBC");
           conn = DriverManager.getConnection("jdbc:sqlite:BarCodeBd.s3db");
		   statmt = conn.createStatement();
		   System.out.println("База Подключена!");
	   }
   
 
    // --------Заполнение таблицы--------
    public static void WriteDB(String code,String path) throws SQLException
    {   
        statmt.execute("INSERT INTO 'BarCode' ('code','codeImg') VALUES ("+"'"+code+"'"+","+"'"+path+"'"+"); ");
        
        System.out.println(code+" и " +path + " Добавлено в БД");
    }
 
 // -------- Вывод таблицы--------
 public static void ReadDB() throws ClassNotFoundException, SQLException
    {
        resSet = statmt.executeQuery("SELECT * FROM BarCode");
     
        while(resSet.next())
        {
            System.out.println(resSet.getInt("code"));
            System.out.println(resSet.getInt("codeImg")); 
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
