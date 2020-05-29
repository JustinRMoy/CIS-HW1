

import java.io.*;
import java.util.Scanner;

public class hw1 {

    public static int[][] readKeyFile(File keyFile) throws IOException {

        FileInputStream file = null;
        int [][] kMatrix = new int[0][];
        int i = 0;
        int j = 0;

        try {

            file  = new FileInputStream(keyFile);
            Scanner in = new Scanner(file);

            int keySize = 0;

            keySize = in.nextInt();

            kMatrix = new int [keySize][keySize];

            while(in.hasNext()){
                if(in.hasNextInt()) {
                    if (j >= keySize) {
                        j = 0;
                        i++;
                    }
                    kMatrix[i][j] = in.nextInt();
                    j++;
                }else {
                    in.next();
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            file.close();
        }


        return kMatrix;
    }

    public static String readPlain (File pFile) throws  IOException{

        FileInputStream file = null;

        String plaintext = "";

        try{

            file = new FileInputStream(pFile);
            Scanner in = new Scanner(file);

            while (in.hasNext()) {
                plaintext = in.nextLine();
                //plaintext.concat("dog");
            }

        }catch (Exception e){
            e.printStackTrace();
        }finally {
            file.close();
        }

        return plaintext;

    }

    public static void printTXT(String plainTxt, PrintWriter pen){
        int m =0;

        for (int k =0; k < plainTxt.length(); k++) {

            if(m >= 80){
                m = 0;
                System.out.println();
                pen.println();
            }

            System.out.print(plainTxt.charAt(k));
            pen.print(plainTxt.charAt(k));                                                          //FIXME
            m++;

        }
    }

    public static void printKey(int [][] keyMatrix, PrintWriter pen){

        System.out.println("Key Matrix: \n");
        pen.println("Key Matrix:\n");

        for(int i = 0; i < keyMatrix.length; i++){
            for(int j = 0; j < keyMatrix.length; j++){
                System.out.print(keyMatrix[i][j]+ " ");
                pen.print(keyMatrix[i][j] + " ");                       //FIXME
            }
            System.out.println();
            pen.println();                                                              //FIXME
        }
    }

    public static int[] convertToVector(String txt, int index, int keySize){

        int[] ret = new int[keySize];

        for(int i = 0; i < keySize; i++){

            ret[i] = (int)(txt.charAt(index) - 'a');
            index++;

        }

        return ret;

    }

    public static int dotProduct(int[] v1, int[]v2){

        int ret = 0;

        for(int i = 0; i < v1.length; i++){
            ret += v1[i] * v2[i];
        }

        ret %= 26;

        return ret;
    }

    public static String matrixMult(String plainTxt, int index, int[][] keyMatrix){

        int[] vector = convertToVector(plainTxt, index, keyMatrix.length);

        String res = "";

        for(int row = 0; row < keyMatrix.length; row++){

            res = res.concat(Character.toString((char)(dotProduct(keyMatrix[row], vector) + 'a')));

        }

        return res;
    }

    public static String hillCipher(String plainTxt, int [][] keyMatrix){

        int keySize = keyMatrix.length;
        String cipherTxt = "";

        for(int i = 0; i < plainTxt.length(); i+= keySize){

            cipherTxt = cipherTxt.concat(matrixMult(plainTxt, i, keyMatrix));

        }

        return cipherTxt;
    }

    public static void main(String[] args) throws IOException {

        File in;
        File plain;
        File output;
        PrintWriter pen = null;

        String plainTxt = "";
        String ciphertxt = "";

        try{


            in = new File(args[0]);

            output = new File("cipherText.txt");
            pen = new PrintWriter(output);

            int [][] keyMatrix  = readKeyFile(in);

            printKey(keyMatrix, pen);

            plain = new File(args[1]);

            plainTxt = readPlain(plain);


           plainTxt = plainTxt.replaceAll("[^a-zA-Z]", "");
           plainTxt = plainTxt.toLowerCase();

           while(plainTxt.length() % keyMatrix.length != 0){

               plainTxt = plainTxt.concat("x");

           }

           System.out.println("\nPlaintext:\n");
           pen.println("\nPlaintext:\n");
           printTXT(plainTxt, pen);

           ciphertxt = hillCipher(plainTxt, keyMatrix);

           System.out.println("\n\n\nCiphertext:\n");
           pen.println("\n\n\nCiphertext:\n");
           printTXT(ciphertxt, pen);


        }catch (Exception e) {
            e.printStackTrace();
        }finally{
            pen.flush();
            pen.close();
        }
    }

}