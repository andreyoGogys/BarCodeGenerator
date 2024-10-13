//класс в котором отрисовывается панель кодировки данных в шрихкол


import javax.imageio.ImageIO;
import javax.swing.*;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.sql.SQLException;


public class MainPanel {
    
    public static ImageIcon setImage(int width,int height,String path){
        try {
            BufferedImage bufferedImage = ImageIO.read(new File(path));
            Image image = bufferedImage.getScaledInstance(width,height,Image.SCALE_DEFAULT);
            ImageIcon icon = new ImageIcon(image);
            return icon;
        } catch (Exception e) {
            return new ImageIcon();
        }
    }
    public static JPanel mainPanel(){
        JLabel label = new JLabel("Введите текст для кодирования");
        JButton btn = new JButton("Кодировать");
        JTextField textField = new JTextField(15);
        JLabel lable_img = new JLabel("");
        
        final JPanel panel = new JPanel(new GridBagLayout());

        GridBagConstraints gridBagConstraints = new GridBagConstraints();
        
        btn.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                BarCode.code128(textField.getText());
                String code = textField.getText();
                textField.setText("");
                lable_img.setIcon(setImage(BarCode.width_last_barcode,BarCode.height_last_barcode,BarCode.last_barcode));
                DataBase dataBase = new DataBase();
                try {
                    //dataBase.WriteDB(code, BarCode.last_barcode);
                    dataBase.CloseDB();
                } catch (ClassNotFoundException e1) {
                    e1.printStackTrace();
                } catch (SQLException e1) {
                    e1.printStackTrace();
                }
            }
        });

        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
        panel.add(label,gridBagConstraints);

        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        panel.add(textField,gridBagConstraints);

        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
        panel.add(btn,gridBagConstraints);

        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        panel.add(lable_img,gridBagConstraints);

        return panel;
    }
}
