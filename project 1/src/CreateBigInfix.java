import java.awt.datatransfer.SystemFlavorMap;
import java.io.*;


/**
 * Created by linyx25 on 2015/12/2.
 */
public class CreateBigInfix {

    public static void main(String[] args) throws IOException {
        FileWriter writer = null;
        try {
            writer = new FileWriter("../InfixOutput.txt", true);
        } catch (IOException e) {
            e.printStackTrace();
        }
        int[] number = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9};
        String[] op = {"+", "-"};
        int i = 0;
        System.out.println(args[0]);
        while(i < Integer.parseInt(args[0])) {
            int randomNum1 = (int) (Math.random() * 10);
            int randomOp = (int) (Math.random() * 2);
            String result = number[randomNum1] + op[randomOp];
            writer.write(result);
            i++;
        }
        writer.close();
    }
}
