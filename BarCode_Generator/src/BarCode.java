//класс в котором отрисовывается штрих код(пока что только code128)



import java.awt.image.*;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;

import javax.imageio.ImageIO;

public class BarCode {
    static String last_barcode;
    static int width_last_barcode;
    static int height_last_barcode;

    static void saveBarCode(BufferedImage bufferedImage){
        try {
            
            int countFiles = new File("img").listFiles().length;
            countFiles++;
            for(int i=0;i<countFiles;i++){
                String path="img//barcode"+i+".png";
                File file = new File(path);
                if(file.exists()==true){
                    System.out.println(path+": exists");
                    continue;
                }
                else{
                    last_barcode=path;
                    ImageIO.write(bufferedImage,"png",file);
                    file.createNewFile();
                    break;
                }
            }
        } catch (Exception e) {
            
        }
        
    }
    static void makeBarcode(String code){
        try {
            int width = code.length();
            int height = 48;
            BufferedImage bufferedImage = new BufferedImage(width,height,BufferedImage.TYPE_INT_RGB);
            for(int x = 0;x<width;x++)
                for(int y = 0;y<height;y++){
                    bufferedImage.setRGB(x,y,0xFFFFFF);
                }
            
            width_last_barcode=width;
            height_last_barcode=height;

            for(int x = 0;x<width;x++){
                if(x==code.length())break;
                if((code.charAt(x)=='1')&(x<width)){
                    for(int y = 0;y<height;y++){
                        bufferedImage.setRGB(x,y,0x00000);
                    }
                }
            }
            saveBarCode(bufferedImage);
           
        } catch (Exception e) {
            System.out.println(e.getLocalizedMessage());
        }
    }

    static void code128(String code){
        code128Library code128library = new code128Library();
        boolean A_B,C;
        A_B=false;C=false;
        if(code.length()==1)code128AorB(code);
        else{
            for(int i = 0 ;i<code.length();i++){
              if(code128library.containtsCCode(code.charAt(i))==true){
                C=true;
                System.out.println("C "+C);
              }
              else{
                A_B=true;
                C=false;
                System.out.println("A_B "+A_B);
                break;
              }
            }
            if(A_B)
                code128AorB(code);
            else 
                code128C(code);
        }
    }

    static void code128AorB(String code){
        String codeReserved ="";
        boolean A,B;
        int ruler = 0;
        A=false;B=false;
        if(code128Library.containtsBCode(code.charAt(0))){
            B=true;
            codeReserved+=code128Library.code128B.get("Start Code B");
            ruler++;
            
        }else{
            int cp = String.valueOf(code.charAt(0)).codePointAt(0);
            String cpToHex = Integer.toHexString(cp);
            if(cpToHex.length()==1)cpToHex="0"+cpToHex;
            if(code128Library.containtsACode(Integer.toHexString(cp))){
                A=true;
                codeReserved+=code128Library.code128A.get("Start Code A");
            }

        }
        int checksum=103+ruler;
        int a=1;
        System.out.println(checksum+" "+a);
        for(int i = 0;i<code.length();i++){
            if(code128Library.containtsBCode(code.charAt(i))==true){ // B
                if(B!=true){
                    codeReserved+=code128Library.code128C.get("Code B");
                    checksum+=100*a;
                    System.out.println(checksum+" "+a);
                    a++;
                }
                
                checksum+=code128Library.getValueOfCharacterCode128B(code.charAt(i))*a;
                System.out.println(checksum+" "+a);
                B=true;a++;
                codeReserved+=code128Library.code128B.get(String.valueOf(code.charAt(i)));
                
            }
            else{
                int cp = String.valueOf(code.charAt(i)).codePointAt(0);
                String cpToHex = Integer.toHexString(cp);
                if(cpToHex.length()==1)cpToHex="0"+cpToHex; // A
                if(code128Library.containtsACode(cpToHex)==true){
                    if(A!=true){codeReserved+=code128Library.code128C.get("Code A");
                    checksum+=101*a;
                    System.out.println(checksum+" "+a);
                    a++;
                }
                    
                    checksum+=code128Library.getValueOfCharacterCode128A(cpToHex)*a;
                    System.out.println(checksum+" "+a);
                    a++;
                    codeReserved+=code128Library.code128A.get(cpToHex);
                }
            }
        
        }System.out.println("checksum:"+checksum);
        codeReserved+=code128Library.getPatternOfDigits(String.valueOf((checksum)%103));
        codeReserved+=code128Library.code128B.get("Stop pattern");
        System.out.println(codeReserved);
        makeBarcode(codeReserved);
    }
    
    static void code128C(String code){ //C
        String codeReserved = "";
        code128Library code128library = new code128Library();
        codeReserved+=code128library.code128C.get("Start Code C");
        int checksum = 105;
        int a=1;//множитель
        int c=(code.length()%2);
        for(int i =0;i<code.length()/2;i++){
            int j1=i*2;
            int j2=(i*2)+1;
            String num = String.valueOf(code.charAt(j1)+""+code.charAt(j2));
            checksum+=Integer.parseInt(num)*a;
            System.out.println(checksum);
            codeReserved+=code128Library.code128C.get(num);
            a++;
            if(i==(code.length()/2)-1 & c==1){
                System.out.println("code b");
                codeReserved+=code128library.code128C.get("Code B");
                checksum+=100*a;
                a++;
                codeReserved+=code128library.code128B.get(String.valueOf(code.charAt(j2+1)));
                checksum+=code128Library.getValueOfCharacterCode128B(code.charAt(j2+1))*a;
                a++;
            }
        }

        codeReserved+=code128Library.getPatternOfDigits(String.valueOf((checksum)%103));
        codeReserved+=code128library.code128C.get("Stop pattern");
        System.out.println(codeReserved);
        makeBarcode(codeReserved);
    }
}

