// класс в котором отрисовывается панель для обозревания штрих кодов


import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;

public class SecondPanel {

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
    public static JPanel secondPanel(){
        
        JPanel panelImgs = new JPanel(new GridBagLayout()); 
        JPanel panelFinalPanel = new JPanel(new BorderLayout()); 
        JPanel panelContent = new JPanel(new BorderLayout()); 
        int countimgs=0;
        try {
            countimgs = new File("img").listFiles().length; // количество картинок
        } catch (Exception e) {
        }

        int panel_height = countimgs*100;

        panelImgs.setBounds(0, 0, 450, panel_height);
        panelContent.setBounds(0, 0, 450, panel_height);
        
        JLabel[] imgs = new JLabel[countimgs];

        for(int i = 0;i<imgs.length;i++){
            try {
                BufferedImage bimg = ImageIO.read(new File("img//barcode"+i+".png"));
                imgs[i] = new JLabel(setImage(bimg.getWidth(), bimg.getHeight(), "img//barcode"+i+".png"));
            } catch (Exception e) {
                System.out.println(e.getMessage());
                imgs[i] = new JLabel();
            }
            
        }
       
        GridBagConstraints gridBagConstraints = new GridBagConstraints();
        for(int i = 0;i<imgs.length;i++){
            gridBagConstraints.gridx = 0;
            gridBagConstraints.gridy = i;
            gridBagConstraints.ipady = 20;
            gridBagConstraints.ipadx = 50;
            panelImgs.add(imgs[i],gridBagConstraints);

            gridBagConstraints.gridx = 1;

            panelImgs.add(new JLabel("шрек номер "+(i+1)),gridBagConstraints);
            panelImgs.repaint();
            panelContent.add(panelImgs,BorderLayout.PAGE_START);
            
        }
        JScrollBar vbar=new JScrollBar(JScrollBar.VERTICAL, 0,100,0,panel_height+100);

        class MyAdjustmentListener implements AdjustmentListener {
            public void adjustmentValueChanged(AdjustmentEvent e) {
                int e2 = -e.getValue();
                panelImgs.setBounds(0, e2, 450, panel_height);
                System.out.println(-e2);

            }
        }
        vbar.addAdjustmentListener(new MyAdjustmentListener( ));
        
        panelFinalPanel.add(panelContent);
        panelFinalPanel.add(vbar,BorderLayout.EAST);
        return panelFinalPanel;
    }

}
