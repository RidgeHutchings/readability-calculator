package readability;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {

        //Opens a text file based on the first command line argument
        File file = new File(args[0]);

        try(Scanner myScanner = new Scanner(file)) {

            String input = myScanner.nextLine();
            int totalWordCount = 0;
            //parse input into an array. Each element will be one sentence.
            String[] inputArray = input.split("\\.|!|\\?");
            int totalSentenceCount = inputArray.length;


            //count how many total characters are in the text.
            int totalCharCount = input.replaceAll("\\s+","").length();

            //count how many total words are in the text.
            String trim = input.trim();
            totalWordCount = trim.split("\\s+").length;
            
            //calculate total syllables
            int[] syllableArray = numberOfSyllables(input);
            


            //output the text, total word count, total sentence count, total char count, syllable count, and polysyllable count.
            System.out.print("The text is: \n" + input);
            System.out.print("\n\nWords: " + totalWordCount);
            System.out.print("\nSentences: "+totalSentenceCount);
            System.out.print("\nCharacters: "+totalCharCount);
            System.out.print("\nSyllables: "+syllableArray[0]);
            System.out.print("\nPolysyllables: "+syllableArray[1]);
            System.out.print("\nEnter the score you want to calculate (ARI, FK, SMOG, CL, all): ");
            Scanner inputScanner = new Scanner(System.in);

            String testChoice = inputScanner.nextLine();
            if(testChoice.equals("ARI")){
                System.out.print("\nAutomated Readability Index: ");
                System.out.print(Math.round(automatedReadabilityIndex(totalWordCount,totalSentenceCount,totalCharCount)*100d)/100d);
                displayResults(automatedReadabilityIndex(totalWordCount,totalSentenceCount,totalCharCount));
            }
            if(testChoice.equals("FK")){
                System.out.print("\nFlesch-Kincaid readability tests: ");
                System.out.print(Math.round(fleschKincaidIndex(totalWordCount,totalSentenceCount, syllableArray[0])*100d)/100d);
                displayResults(fleschKincaidIndex(totalWordCount,totalSentenceCount, syllableArray[0]));
            }
            if(testChoice.equals("SMOG")){
                System.out.print("\nSimple Measure of Gobbledygook: ");
                System.out.print(Math.round(smogIndex(totalWordCount,totalSentenceCount,syllableArray[0],syllableArray[1])*100d)/100d);
                displayResults(smogIndex(totalWordCount,totalSentenceCount,syllableArray[0],syllableArray[1]));
            }
            if(testChoice.equals("CL")){
                System.out.print("\nColeman-Liau index: ");
                System.out.print(Math.round(colemanLiauIndex(totalWordCount,totalSentenceCount,totalCharCount)*100d)/100d);
                displayResults(colemanLiauIndex(totalWordCount,totalSentenceCount,totalCharCount));
            }
            if(testChoice.equals("all")){
                System.out.print("\nAutomated Readability Index: ");
                System.out.print(Math.round(automatedReadabilityIndex(totalWordCount,totalSentenceCount,totalCharCount)*100d)/100d);
                displayResults(automatedReadabilityIndex(totalWordCount,totalSentenceCount,totalCharCount));
                System.out.print("\nFlesch-Kincaid readability tests: ");
                System.out.print(Math.round(fleschKincaidIndex(totalWordCount,totalSentenceCount, syllableArray[0])*100d)/100d);
                displayResults(fleschKincaidIndex(totalWordCount,totalSentenceCount, syllableArray[0]));
                System.out.print("\nSimple Measure of Gobbledygook: ");
                System.out.print(Math.round(smogIndex(totalWordCount,totalSentenceCount,syllableArray[0],syllableArray[1])*100d)/100d);
                displayResults(smogIndex(totalWordCount,totalSentenceCount,syllableArray[0],syllableArray[1]));
                System.out.print("\nColeman-Liau index: ");
                System.out.print(Math.round(colemanLiauIndex(totalWordCount,totalSentenceCount,totalCharCount)*100d)/100d);
                displayResults(colemanLiauIndex(totalWordCount,totalSentenceCount,totalCharCount));
            }




        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        
    }
    
    //count how many syllables and polysyllables are in the text.
    public static int[] numberOfSyllables(String input){

        //ensure all case matches.
        input = input.toLowerCase();
        //split the input into an array.
        String[] wordArray = input.split("\\s+");
        //create a vowels string that we can compare against.
        String vowels = "aeiouy";

        int totalSyllables = 0;
        int polySyllables = 0;
        for(String word:wordArray){
            int polyCount =0;


            for(int i=0;i<word.length();i++){
                char currentChar = word.charAt(i);
                char previousChar = ' ';
                if(i>0){previousChar = word.charAt(i-1);}


                //check to see if the current character is a vowel.
                if(vowels.indexOf(currentChar) !=-1){totalSyllables++;polyCount++;}

                //tests to see if the vowel 'e' is at the end of a word.
                if("e".indexOf(currentChar) !=-1 && i == word.length()-1) { totalSyllables--;polyCount--;}
                //tests for double vowels.
                if(vowels.indexOf(previousChar) !=-1 && vowels.indexOf(currentChar) !=-1){totalSyllables--;polyCount--;}
                if(polyCount > 3){polySyllables++;}

            }

        }
        int[] syllableArray = new int[2];
        syllableArray[0] = totalSyllables;
        syllableArray[1] = polySyllables;
        return syllableArray;
    }
    public static double automatedReadabilityIndex(int words,int sentences,int characters){

        return  4.71 * ((double)characters/(double)words) + 0.5 * ((double)words/(double)sentences) - 21.43;

    }
    public static double fleschKincaidIndex(int words,int sentences, int syllables){
        return 0.39 * ((double)words/(double)sentences) + 11.8 * ((double)syllables/(double)words) - 15.59;
    }
    public static double smogIndex(int words,int sentences,int syllables,int polySyllables){
        return 1.043 * Math.sqrt(polySyllables * (double)(30/sentences)) + 3.1291;
    }
    public static double colemanLiauIndex(int words,int sentences,int characters){
        double avgChar = characters/words*100;
        double avgSentences = sentences/words*100;
        return 0.0588*avgChar-0.296*avgSentences-15.8;
    }
    public static void displayResults(double readabilityScore){


        //displays the recommended grade level based on the readabilityScore
        switch((int) Math.ceil(readabilityScore)){
            case 1:
                System.out.print(" (about 6-year-olds).");
                break;
            case 2:
                System.out.print(" (about 7-year-olds).");
                break;
            case 3:
                System.out.print(" (about 9-year-olds).");
                break;
            case 4:
                System.out.print(" (about 10-year-olds).");
                break;
            case 5:
                System.out.print(" (about 11-year-olds).");
                break;
            case 6:
                System.out.print(" (about 12-year-olds).");
                break;
            case 7:
                System.out.print(" (about 13-year-olds).");
                break;
            case 8:
                System.out.print(" (about 14-year-olds).");
                break;
            case 9:
                System.out.print(" (about 15-year-olds).");
                break;
            case 10:
                System.out.print(" (about 16-year-olds).");
                break;
            case 11:
                System.out.print(" (about 17-year-olds).");
                break;
            case 12:
                System.out.print(" (about 18-year-olds).");
                break;
            case 13:
                System.out.print(" (about 24-year-olds).");
                break;
            case 14:
                System.out.print(" (about 24+-year-olds).");
                break;
        }
    }
}
