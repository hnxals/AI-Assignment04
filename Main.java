import java.io.*;
import java.util.*;

public class Main {
    private String fileName = "hmm.txt";
    private String[] linksName = {"","","DEMO","VIDEO","TESTIMONIAL","PRICING","BLOG","PAYMENT"};
    private int[][] linksList = new int[40][3];

    private void readFile(){
        try {
            File myFile = new File(fileName);
            Scanner myReader = new Scanner(myFile);
            int index = 0;
            while(myReader.hasNextLine()){
                String tempLine = myReader.nextLine();
                if(tempLine.equals("")){
                    linksList[index][0]=1;
                    index++;
                    continue;
                }
                if(tempLine.charAt(0)=='#'){
                    continue;
                }
                tempLine=tempLine.trim();
                String[] temp = tempLine.split("\\,");
                int sameLineIndex = 0;
                for(String x:temp){
                    if(x.equals(linksName[2])){
                        linksList[index][sameLineIndex]=2;
                    }
                    else if(x.equals(linksName[3])){
                        linksList[index][sameLineIndex]=3;
                    }
                    else if(x.equals(linksName[4])){
                        linksList[index][sameLineIndex]=4;
                    }
                    else if(x.equals(linksName[5])){
                        linksList[index][sameLineIndex]=5;
                    }
                    else if(x.equals(linksName[6])){
                        linksList[index][sameLineIndex]=6;
                    }
                    else if(x.equals(linksName[7])){
                        linksList[index][sameLineIndex]=7;
                    }
                    sameLineIndex++;
                }
                index++;
            }
            myReader.close();
            for(int i=0;i<index;i++){
                System.out.println(linksList[i][0]+" "+linksList[i][1]+" "+linksList[i][2]);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        Main main = new Main();
        main.readFile();
    }
}