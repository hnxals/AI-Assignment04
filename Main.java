import java.io.*;
import java.util.*;

/*
Github: https://github.com/hnxals/AI-Assignment04
Author: Mr-Crocodile(Bingxin Lyu)
*/

public class Main {
    private String fileName = "hmm1.txt";
    private String[] linksName = {"","","DEMO","VIDEO","TESTIMONIAL","PRICING","BLOG","PAYMENT"}; // none->0; emptyLine->1; demo->2; video->3 .... payment->7
    private int[][] linksList = new int[40][3];
    private int index = 0;
    private float[][] stateTransformPossibility = { {0.60f, 0.40f, 0.00f, 0.00f, 0.00f, 0.00f}, //zero
                                                    {0.00f, 0.49f, 0.30f, 0.00f, 0.01f, 0.20f}, //aware
                                                    {0.00f, 0.00f, 0.48f, 0.20f, 0.02f, 0.30f}, //considering
                                                    {0.00f, 0.00f, 0.00f, 0.40f, 0.30f, 0.30f}, //experiencing
                                                    {0.00f, 0.00f, 0.00f, 0.00f, 0.80f, 0.20f}, //ready
                                                    {0.00f, 0.00f, 0.00f, 0.00f, 0.00f, 0.00f}}; //lost
    private float[][] linksClickPossibility = { {0.10f, 0.01f, 0.05f, 0.30f, 0.50f, 0.00f}, //zero
                                                {0.10f, 0.01f, 0.15f, 0.30f, 0.40f, 0.00f}, //aware
                                                {0.20f, 0.30f, 0.05f, 0.40f, 0.40f, 0.00f}, //considering
                                                {0.40f, 0.60f, 0.05f, 0.30f, 0.40f, 0.00f}, //experiencing
                                                {0.05f, 0.75f, 0.35f, 0.20f, 0.40f, 0.00f}, //ready
                                                {0.01f, 0.01f, 0.03f, 0.05f, 0.20f, 0.00f}, //lost
                                                {0.40f, 0.40f, 0.01f, 0.05f, 0.50f, 1.00f}}; //satisfied
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
            stateListInt = new int[index];

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void getStateAlg(){
        // for(int i=0;i<index;i++){
        //     System.out.println(linksList[i][0]+" "+linksList[i][1]+" "+linksList[i][2]);
        // }
        int maxIndex = -1;
        float maxValue = 0;
        float[] lastPossibility = new float[7];
        
        //initialize first P
        System.out.println("ZERO");
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


        float[] newP = new float[6];
        for(int k=1; k<index; k++){

            maxValue = 0;
            maxIndex = -1;
            if(stateListInt[k-1]==5){
                stateListInt[k]=5;
                continue;
            }
            line:{
            for(int i=0; i < 6; i++){
                // float clickP = 0;
                for(int j=0; j<3; j++){
                    
                    //end of line
                    if(linksList[k][j]==0)
                        break;

                    //when click payment then change state to SATISFIED
                    if(linksList[k][j]==7){
                        stateListInt[k]=6;
                        break line;
                    }
                    
                    //empty line
                    if(linksList[k][j]==1){
                        // float notClick = (1-lastPossibility[0])*(1-lastPossibility[1])*(1-lastPossibility[2])*(1-lastPossibility[3])*(1-lastPossibility[4]);
                        newP[i]=(lastPossibility[0]*stateTransformPossibility[0][i] + lastPossibility[1]*stateTransformPossibility[1][i] + lastPossibility[2]*stateTransformPossibility[2][i] 
                                + lastPossibility[3]*stateTransformPossibility[3][i] + lastPossibility[4]*stateTransformPossibility[4][i] ) 
                                * ((1-linksClickPossibility[i][0]) * (1-linksClickPossibility[i][1]) * (1-linksClickPossibility[i][2]) * (1-linksClickPossibility[i][3]) * (1-linksClickPossibility[i][4])
                                * (1-linksClickPossibility[i][5]));
                        // newP[i]=(lastPossibility[0]*stateTransformPossibility[0][i] + lastPossibility[1]*stateTransformPossibility[1][i] + lastPossibility[2]*stateTransformPossibility[2][i] 
                        //         + lastPossibility[3]*stateTransformPossibility[3][i] + lastPossibility[4]*stateTransformPossibility[4][i] )* notClick;
                        break;
                    }
                    if(j==0){
                    newP[i]=(lastPossibility[0]*stateTransformPossibility[0][i] + lastPossibility[1]*stateTransformPossibility[1][i] + lastPossibility[2]*stateTransformPossibility[2][i] 
                                            + lastPossibility[3]*stateTransformPossibility[3][i] + lastPossibility[4]*stateTransformPossibility[4][i] ) * linksClickPossibility[i][linksList[k][j]-2];
                    }
                    else{
                        newP[i] *= (lastPossibility[0]*stateTransformPossibility[0][i] + lastPossibility[1]*stateTransformPossibility[1][i] + lastPossibility[2]*stateTransformPossibility[2][i] 
                                            + lastPossibility[3]*stateTransformPossibility[3][i] + lastPossibility[4]*stateTransformPossibility[4][i] ) * linksClickPossibility[i][linksList[k][j]-2];
                    }

                }
                if(newP[i]>maxValue){
                    maxValue = newP[i];
                    maxIndex = i;
                }

            }
            if(maxIndex==-1)
                stateListInt[k]=stateListInt[k-1];
            else
                stateListInt[k]=maxIndex;
            }
            for(int i =0;i<6;i++){
                lastPossibility[i]=newP[i];
                newP[i]=0;
            }
        }

        //print states
        for(int i = 0;i<index;i++){
            if(stateListInt[i]==0)
                System.out.println("ZERO");
            if(stateListInt[i]==1)
                System.out.println("AWARE");
            if(stateListInt[i]==2)
                System.out.println("CONSIDERING");
            if(stateListInt[i]==3)
                System.out.println("EXPERIENCING");
            if(stateListInt[i]==4)
                System.out.println("READY");
            if(stateListInt[i]==5)
                System.out.println("LOST");
            if(stateListInt[i]==6)
                System.out.println("SATISFIED");     
        }
    }
    public static void main(String[] args) {
        Main main = new Main();
        main.readFile();
        main.getStateAlg();
    }
}