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
                BarCode barCode = new BarCode();
                barCode.code128(textField.getText());
                String code = textField.getText();
                textField.setText("");
                lable_img.setIcon(setImage(barCode.width_last_barcode,barCode.height_last_barcode,barCode.last_barcode));
                try {
                    DataBase dataBase = new DataBase();
                    dataBase.WriteCodeAndPathToDB(code, barCode.last_barcode);
                } catch (Exception e1) {
                    // TODO: handle exception
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
