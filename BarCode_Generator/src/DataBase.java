import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.imageio.ImageIO;

public class DataBase{

    BufferedImage[] imBuff = new BufferedImage[0];
    String[] codes = new String[0];

    public DataBase() throws SQLException{
        imBuff = new BufferedImage[getCount()];
        codes = new String[getCount()];
    }

    public Connection ConnectToDB() throws SQLException{
        Connection conn = null;
        try {
            conn = DriverManager.getConnection("jdbc:sqlite:BarCodeDB.s3db");
            
        } catch (Exception e) {
            System.out.println("doesnt connected "+e.getMessage());
        }
        
        return conn;
    }
    public void WriteCodeAndPathToDB(String code,String path) throws SQLException, IOException{
        Connection conn = this.ConnectToDB();

        try {
            File imageFile = new File(path);
            PreparedStatement pstmt = conn.prepareStatement("INSERT INTO BarCode(code,codeImg) VALUES(?,?)");  

            pstmt.setString(1, code);

            FileInputStream fis = new FileInputStream(imageFile);

            pstmt.setBinaryStream(2, fis, (int) imageFile.length());
            pstmt.executeUpdate();

            pstmt.close();
            fis.close();
            
        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }

        conn.close();
    }
    public int getCount() throws SQLException{
        Connection conn = this.ConnectToDB();
        PreparedStatement pstmt = conn.prepareStatement("select count(*) from BarCode");
        ResultSet rs = pstmt.executeQuery();
        rs.next();
        int count = rs.getInt(1);
        conn.close();
        return count;
    }

    public void ReadDB() throws SQLException, IOException {
        Connection conn = this.ConnectToDB();

        try {
            PreparedStatement pstmt = conn.prepareStatement("SELECT code,codeImg FROM BarCode");
            ResultSet rs = pstmt.executeQuery();
            int i =0;
            while(rs.next()){
                String code = rs.getString("code");
                InputStream inputStream = rs.getBinaryStream("codeImg");
                
                codes[i]=code;

                BufferedImage buff = new BufferedImage(250,50, BufferedImage.TYPE_INT_RGB);
                buff = ImageIO.read(inputStream);
                imBuff[i]= buff;

                i++;
                inputStream.close();
                if(i==getCount())break;
            }

            rs.close();
            pstmt.close();

            
        } catch (SQLException | IOException e) {
            e.printStackTrace();
        } finally {
            if (conn != null) {
                conn.close();
            }
        }
    }

    public void EraseDB() throws SQLException{
        Connection conn = this.ConnectToDB();
        try {
            String sql = "DELETE FROM BarCode";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.executeUpdate();
            pstmt.close();

            
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (conn != null) {
                conn.close();
            }
        }
    }
}