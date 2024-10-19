// класс в котором отрисовывается панель для обозревания штрих кодов

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;

public class SecondPanel{

    public static ImageIcon setImage(BufferedImage bimg){
        try {
            Image image = bimg.getScaledInstance(bimg.getWidth(),50,Image.SCALE_DEFAULT);
            ImageIcon icon = new ImageIcon(image);
            return icon;
        } catch (Exception e) {
            return new ImageIcon();
        }
    }
    public static JPanel secondPanel(){
        
        JPanel panelImgs = new JPanel(new GridBagLayout()); 
        JPanel panelFinalPanel = new JPanel(new BorderLayout()); 
        JPanel panelContent = new JPanel(new BorderLayout()); 

        JButton btnerase = new JButton("Очистить базу данных");
        btnerase.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e){
                try {
                    DataBase dataBase = new DataBase();
                    dataBase.EraseDB();
                    panelFinalPanel.repaint();
                } catch (Exception e1) {
                    // TODO: handle exception
                }
            }  
          });

        int countimgs=0;
        int panel_height =0;
        try {
            DataBase dataBase = new DataBase();
            countimgs = dataBase.getCount();
            panel_height = (dataBase.getCount()<=0?1:dataBase.getCount())*70;
        } catch (Exception e) {
        }
        panelImgs.setBounds(0, 0, 580, panel_height);
        panelContent.setBounds(0, 0, 580, panel_height);
        
        JLabel[] imgs = new JLabel[countimgs];
        String[] codes = new String[countimgs];
        BufferedImage[] bimg = new BufferedImage[countimgs];
        GridBagConstraints gridBagConstraints = new GridBagConstraints();
        if(countimgs>0){
            try {
                DataBase dataBase = new DataBase();
                dataBase.ReadDB();
                bimg = dataBase.imBuff;
                codes = dataBase.codes;
            
            for(int i = 0;i<imgs.length;i++){
                try {
                    imgs[i] = new JLabel(setImage(bimg[i]));
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                    imgs[i] = new JLabel();
                }
                
            }
            } catch (Exception e) {
                // TODO: handle exception
            }
            for(int i = 0;i<imgs.length;i++){
                gridBagConstraints.gridx = 0;
                gridBagConstraints.gridy = i;
                gridBagConstraints.ipady = 20;
                gridBagConstraints.ipadx = 20;
                panelImgs.add(imgs[i],gridBagConstraints);

                gridBagConstraints.gridx = 1;

                panelImgs.add(new JLabel((i+1)+" code: "+codes[i]),gridBagConstraints);
                panelImgs.repaint();
                
            }
        }

        panelContent.add(panelImgs,BorderLayout.PAGE_START);
        JScrollPane scrollPane = new JScrollPane(panelContent,JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setWheelScrollingEnabled(true);

        panelFinalPanel.add(scrollPane);
        panelFinalPanel.add(btnerase,BorderLayout.PAGE_END);
        return panelFinalPanel;

    }
    

}
