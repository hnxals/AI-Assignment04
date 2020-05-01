import java.io.*;
import java.util.*;

public class Main {
    private String fileName = "hmm.txt";
    private String[] linksName = {"","","DEMO","VIDEO","TESTIMONIAL","PRICING","BLOG","PAYMENT"};
    private int[][] linksList = new int[40][3];
    private int index = 0;
    private float[][] stateTransformPossibility = { {0.60f, 0.40f, 0.00f, 0.00f, 0.00f, 0.00f},
                                                    {0.00f, 0.49f, 0.30f, 0.00f, 0.01f, 0.20f},
                                                    {0.00f, 0.00f, 0.48f, 0.20f, 0.02f, 0.30f},
                                                    {0.00f, 0.00f, 0.00f, 0.40f, 0.30f, 0.30f},
                                                    {0.00f, 0.00f, 0.00f, 0.00f, 0.80f, 0.20f}};
    private float[][] linksClickPossibility = { {0.10f, 0.01f, 0.05f, 0.30f, 0.50f, 0.00f},
                                                {0.10f, 0.01f, 0.15f, 0.30f, 0.40f, 0.00f},
                                                {0.20f, 0.30f, 0.05f, 0.40f, 0.40f, 0.00f},
                                                {0.40f, 0.60f, 0.05f, 0.30f, 0.40f, 0.00f},
                                                {0.05f, 0.75f, 0.35f, 0.20f, 0.40f, 0.00f},
                                                {0.01f, 0.01f, 0.03f, 0.05f, 0.20f, 0.00f},
                                                {0.40f, 0.40f, 0.01f, 0.05f, 0.50f, 1.00f}};
    private String[] stateList;
    private int[] stateListInt;

    private void readFile() {
        try {
            File myFile = new File(fileName);
            Scanner myReader = new Scanner(myFile);
            while (myReader.hasNextLine()) {
                String tempLine = myReader.nextLine();
                if (tempLine.equals("")) {
                    linksList[index][0] = 1;
                    index++;
                    continue;
                }
                if (tempLine.charAt(0) == '#') {
                    continue;
                }
                tempLine = tempLine.trim();
                String[] temp = tempLine.split("\\,");
                int sameLineIndex = 0;
                for (String x : temp) {
                    if (x.equals(linksName[2])) {
                        linksList[index][sameLineIndex] = 2;
                    } else if (x.equals(linksName[3])) {
                        linksList[index][sameLineIndex] = 3;
                    } else if (x.equals(linksName[4])) {
                        linksList[index][sameLineIndex] = 4;
                    } else if (x.equals(linksName[5])) {
                        linksList[index][sameLineIndex] = 5;
                    } else if (x.equals(linksName[6])) {
                        linksList[index][sameLineIndex] = 6;
                    } else if (x.equals(linksName[7])) {
                        linksList[index][sameLineIndex] = 7;
                    }
                    sameLineIndex++;
                }
                index++;
            }
            myReader.close();
            stateList = new String[index];
            stateListInt = new int[index];

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void lookForwardAlg(){
        for(int i=0;i<index;i++){
            System.out.println(linksList[i][0]+" "+linksList[i][1]+" "+linksList[i][2]);
        }
        int maxIndex = -1;
        float maxValue = 0;
        float[] lastPossibility = new float[7];

        for(int i=0; i<6;i++){
            for(int j=0; j<3;j++){
                if(linksList[0][j]==0)
                    break;
                if(lastPossibility[i]==0){
                    lastPossibility[i]=linksClickPossibility[i][linksList[0][j]-2] * stateTransformPossibility[0][i];
                }
                else{
                    lastPossibility[i] =lastPossibility[i] * linksClickPossibility[i][linksList[0][j]-2] * stateTransformPossibility[0][i];
                }  
            }
            if(lastPossibility[i]>maxValue){
                maxValue=lastPossibility[i];
                maxIndex=i;
            }
        }

        stateListInt[0]=maxIndex;
        System.out.println("State:"+stateListInt[0]);

        for(int k=1; k<index; k++){
            for(int i=0; i < 6; i++){
                maxValue = 0;
                maxIndex = -1;
                for(int j=0; j<3; j++){
                    if(linksList[k][j]==0)
                        break;
                    
                }
            }
        }
    }
    public static void main(String[] args) {
        Main main = new Main();
        main.readFile();
        main.lookForwardAlg();
    }
}