//Главный класс в котором создается приложение в котором отображаются панели
//код программы не закончен, поэтом в работе приложения будут проблемы.

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
public class App{

    static DataBase DataBaseDB;
    static JFrame frame = new JFrame();
    static boolean fiststart = true;

    public static JFrame createApp(){
        frame = new JFrame();
        frame.setDefaultCloseOperation(frame.EXIT_ON_CLOSE);
        frame.setSize(600,500);
        frame.setResizable(false);
        frame.setDefaultLookAndFeelDecorated(false);
        return frame;
    }


    public static JPanel panelManager(String currentPanel){
        JButton buttonToSecondPanel = new JButton("Другие штрих-коды");
        JButton buttonToMainPanel = new JButton("Сгенерировать штрих-код");

        buttonToSecondPanel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e){
                String x = String.valueOf(frame.getX());
                String y = String.valueOf(frame.getY());
                String[] a ={"SecondPanel",x,y};
                main(a);
            }
        });
        

        buttonToMainPanel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e){
                String x = String.valueOf(frame.getX());
                String y = String.valueOf(frame.getY());
                String[] a ={"MainPanel",x,y};
                main(a);
            }
        });
        
        JPanel mainpanel = MainPanel.mainPanel();
        JPanel secondpanel = SecondPanel.secondPanel();
        secondpanel.add(buttonToMainPanel,BorderLayout.NORTH);
        mainpanel.setLayout(new BorderLayout());
        mainpanel.add(MainPanel.mainPanel(),BorderLayout.CENTER);
        mainpanel.add(buttonToSecondPanel,BorderLayout.NORTH);

        switch (currentPanel) {
            case "MainPanel":
                return mainpanel;   
                        
            case "SecondPanel":
                return secondpanel;
            default:
                return new JPanel();
        }
    }
    public static void main(String[] args) {
        System.out.println(fiststart);
        
        frame.dispose();
        if(fiststart == true){
            frame = createApp();
            frame.setContentPane(panelManager("MainPanel"));
            fiststart=false;
        }else{
            String a = args[0];
            int x = Integer.parseInt(args[1]);
            int y = Integer.parseInt(args[2]);
            System.err.println(x+" "+ y);
            
            frame = createApp();
        
            frame.setLocation(x,y);
            frame.setContentPane(panelManager(a));
        }
        
        frame.setVisible(true);
    }
    
}