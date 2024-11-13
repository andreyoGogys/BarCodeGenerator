
import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;

public class GUI {
    private JFrame frame;
    private JTextField filePathField;
    private JTextArea resultArea;
    private JLabel barcode;
    private JScrollPane scrollPane;

    private static ImageIcon setImage(int width,int height,String path){
        try {
            BufferedImage bufferedImage = ImageIO.read(new File(path));
            Image image = bufferedImage.getScaledInstance(width,height,Image.SCALE_DEFAULT);
            ImageIcon icon = new ImageIcon(image);
            return icon;
        } catch (Exception e) {
            return new ImageIcon();
        }
    }
    private static ImageIcon setImage(BufferedImage bimg){
        try {
            Image image = bimg.getScaledInstance(bimg.getWidth(),bimg.getHeight(),Image.SCALE_DEFAULT);
            ImageIcon icon = new ImageIcon(image);
            return icon;
        } catch (Exception e) {
            return new ImageIcon();
        }
    }

    public void createAndShowGUI() {
        frame = new JFrame("Декодирование Code 128");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 400);
        frame.setLayout(new BorderLayout());

        JPanel filePanel = new JPanel(new FlowLayout());
        filePathField = new JTextField(30);
        JButton browseButton = new JButton("Выбрать файл");
        browseButton.addActionListener(new BrowseButtonListener());
        filePanel.add(filePathField);
        filePanel.add(browseButton);

        resultArea = new JTextArea(10, 50);
        barcode = new JLabel();
        resultArea.setLineWrap(true);
        resultArea.setWrapStyleWord(true);
        resultArea.setEditable(false);
        scrollPane = new JScrollPane(resultArea);
        JPanel barcodePanel = new JPanel();
        barcodePanel.add(scrollPane,BorderLayout.CENTER);
        barcodePanel.add(barcode,BorderLayout.SOUTH);
        scrollPane.repaint();

        JButton decodeButton = new JButton("Декодировать");
        decodeButton.addActionListener(new DecodeButtonListener());
        
        frame.add(filePanel, BorderLayout.NORTH);
        frame.add(barcodePanel, BorderLayout.CENTER);
        frame.add(decodeButton, BorderLayout.SOUTH);

        frame.setVisible(true);
    }

    private class BrowseButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            JFileChooser fileChooser = new JFileChooser();
            int returnValue = fileChooser.showOpenDialog(frame);
            if (returnValue == JFileChooser.APPROVE_OPTION) {
                File selectedFile = fileChooser.getSelectedFile();
                filePathField.setText(selectedFile.getAbsolutePath());
            }
        }
    }

    private class DecodeButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String filePath = filePathField.getText();

            if (filePath.isEmpty()) {
                JOptionPane.showMessageDialog(frame, "Пожалуйста, выберите файл.");
                return;
            }
            try {
                DataBase db = new DataBase();
                String result = Code128Decoder.decodeBarcode(filePath);
                resultArea.setText(result);
                try {
                    db.WriteCodeAndPathToDB(result,filePath);
                    barcode.setIcon(setImage(db.ReadBlobFromDB()));
                    scrollPane.repaint();
                } catch (SQLException e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                }
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(frame, "Ошибка при чтении файла.");
            }
        }
    }
}
