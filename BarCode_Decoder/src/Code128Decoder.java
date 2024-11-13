import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Code128Decoder {

    private static final String[] CODE_128_PATTERNS = {
        "11011001100", "11001101100", "11001100110", "10010011000",
        "10010001100", "10001001100", "10011001000", "10011000100",
        "10001100100", "11001001000", "11001000100", "11000100100",
        "10110011100", "10011011100", "10011001110", "10111001100"
    };

    public static String decodeBarcode(String imagePath) throws IOException {
        BufferedImage image = ImageIO.read(new File(imagePath));
        String bincode = getBinCode(image);
       
        return bincode;
    }

    private static String getBinCode(BufferedImage image){

        int image_width = image.getWidth();
        String code = "";
        for(int i =0;i<image_width;i+=2){
            int rgb = image.getRGB(i,image.getHeight()/2) & 0xffffff;
            String num;
            num = rgb == 0xffffff ? "0" : "1";
            code+=num;
        }
        System.out.println("code:"+code);
        return getCode(code);
    }

    private static String getCode(String bincode){
        code128Library library = new code128Library();
        int chunkSize = 11;
        String code = "";

        // Разделение строки на части по 6 символов
        String[] chunks = splitStringIntoChunks(bincode, chunkSize);
        //                        A               B           C
        String[] patterns = {"11010000100","11010010000","11010011100"};
        char type=' ';
        
        System.out.println("chunck:"+chunks[0]);
        if(chunks[0].equals(patterns[0]))type='A';
        else
            if(chunks[0].equals(patterns[1]))type='B';
            else
                if(chunks[0].equals(patterns[2]))type='C';

        System.out.println("type:"+type);
        for (int i = 1;i<chunks.length-3;i++) {
            String symbol = library.getSymbolFromPattern(type,chunks[i]);
            System.out.println(symbol);
            code+=symbol;
        }
        return code;
    }

    public static String[] splitStringIntoChunks(String str, int chunkSize) {
        int length = str.length();
        int numChunks = (int) Math.ceil((double) length / chunkSize);
        String[] chunks = new String[numChunks];

        for (int i = 0; i < numChunks; i++) {
            int start = i * chunkSize;
            int end = Math.min(start + chunkSize, length);
            chunks[i] = str.substring(start, end);
        }
        return chunks;
        
    }
}