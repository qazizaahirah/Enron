/*In this class a new result is produced for all the user and the unique words have been found
 This program is being written for the preprocessing of the Enron data. This program is written for an assignement given for the Machine Learning for Big Data course for Fall 2015

Input: Is the folder that contains the emails of the employees. The emails are divided in different folders, where each folder belongs to Each employee
 *///http://stackoverflow.com/questions/10543053/java-read-in-single-file-and-write-out-multiple-files
package bigdata;

/**
 *
 * @author Qazi Zaahirah
 */

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Writer;
import static java.lang.Math.random;
import java.util.*;
import static java.util.stream.DoubleStream.builder;
import javax.mail.*;
import javax.mail.internet.MimeMessage;
import static javax.management.Query.value;

public class enronfinal {
    
    private static final String LINE_SEPARATOR = "\r\n";
    
    public static void main(String[] args) throws FileNotFoundException, IOException {
        String path = "InputPath";
        File Directory = new File(path);
        String[] Directorynames = Directory.list();
        String[] TenDirectories = new String[1];
        Random rn = new Random();
        for (int i = 0; i < 10; i++) {
            int RandomNumber = rn.nextInt(150 - 1 + 1) + 1;
            TenDirectories[i] = Directorynames[RandomNumber];
        }

        //TenDirectories[0] = "thomas-p";
        EmailClustering app = new EmailClustering();
        app.getEmailContent(TenDirectories);
        
    }
    
    public List getEmailContent(String[] TenDirectories) throws FileNotFoundException, IOException {
        System.out.println("this program has worked");
        ArrayList<String> Union = new ArrayList<String>();
        ArrayList<String> result = new ArrayList<String>();
        
        String path = "InputPath";
        
        for (String name : TenDirectories) {
            System.out.println("Directory: " + name);
            int filenumber = 0;
            File dir = new File("Results\\" + name);
            
            dir.mkdir();
            // this creates the final file with all the user emails 
            File file = new File("Results\\" + name + " Emails.txt");
            File file2 = new File("Results\\" + name + " Union.txt");
            
            String subDirectoryPath = path + "//" + name;
            File subDirectory = new File(subDirectoryPath);
            String[] subDirectoryNames = subDirectory.list();
            if (new File(subDirectoryPath).isDirectory()) {
                for (String name2 : subDirectoryNames) {
                    String Subdirectory2Path = subDirectoryPath + "//" + name2;
                    if (new File(Subdirectory2Path).isDirectory()) {
                        File f = new File(Subdirectory2Path);
                        String[] fileList = f.list();
                        String host = "host.com";
                        java.util.Properties properties = System.getProperties();
                        properties.setProperty("mail.smtp.host", host);
                        Session session = Session.getDefaultInstance(properties);
                        for (String tmpFile : fileList) {
                            int NoOfUnique = 0;
                            int AverageMsgLen = 0;
                            MimeMessage email = null;
                            try {
                                String FinalFilePath = Subdirectory2Path + "//" + tmpFile;
                                // System.out.println(FinalFilePath);
                                // this part gets the message content
                                if (new File(FinalFilePath).isFile()) {
                                    FileInputStream fis = new FileInputStream(FinalFilePath);
                                    email = new MimeMessage(session, fis);
                                    String content = (String) email.getContent();
                                    // this part removes the forwarded messages
                                    String[] parts = content.split("From:");
                                    String contentLength = parts[0];
                                    AverageMsgLen = contentLength.length();

                                    // this part will remove the stop words
                                    String contentWithOutForward = parts[0].replaceAll("[IMAGE]", " ");
                                    contentWithOutForward = contentWithOutForward.toLowerCase();
                                    contentWithOutForward = contentWithOutForward.replaceAll("\t", " ");
                                    contentWithOutForward = contentWithOutForward.replaceAll("[-+.^:,=*'?/;#$()&@!1234567890%<>_\"~]", "");
                                    
                                    contentWithOutForward = contentWithOutForward.replace("\n", "").replace("\r", "");
                                    String[] wordArray = contentWithOutForward.split("[ ]+");
                                    int length = wordArray.length;
                                    //    Arrays.asList(wordArray);
                                    Scanner s = new Scanner(new File("StopWords.txt"));
                                    ArrayList<String> stopWordsList = new ArrayList<String>();
                                    while (s.hasNext()) {
                                        stopWordsList.add(s.next());
                                        
                                    }
                                    s.close();
                                    for (String WithoutStop : wordArray) {
                                        if (!stopWordsList.contains(WithoutStop)) {
                                            result.add(WithoutStop);
                                            //  System.out.println("WithoutStop "+WithoutStop);
                                        }
                                    }
                                    Set<String> Uniques;
                                    Uniques = new HashSet<String>();
                                    Set<String> UniqueUnion;
                                    UniqueUnion = new HashSet<String>();
//                                    Uniques.addAll(result);
//                                    result.clear();
//                                    result.addAll(Uniques);
                                    NoOfUnique = result.size();
                                    Union.addAll(result);
                                    UniqueUnion.addAll(Union);
                                    Union.clear();
                                    Union.addAll(UniqueUnion);
// this writes each email seperately without the stop words in the directory created for each user
                                    File file1 = new File("Results\\" + name + "\\" + filenumber + ".txt");
                                    BufferedWriter out1 = new BufferedWriter(new FileWriter(file1, true));
                                    
                                    for (String str : result) {
                                        out1.newLine();
                                        out1.write(str);
                                    }
                                    out1.flush();
                                    out1.close();
                                    // this is to get the number of positive and negative words in the email
                                    EmailClustering app = new EmailClustering();
                                    int[] PosNeg = new int[2];
                                  //  PosNeg = app.Sentiment(result);
                                    int Positives = PosNeg[0];
                                    int Negatives = PosNeg[1];
//                                    System.out.println("File Number:" + filenumber);
//                                    System.out.println("No of Uniq" + NoOfUnique);
//                                    System.out.println("AverageMessageLen:" + AverageMsgLen);
//                                    System.out.println("Positives:" + Positives);
//                                    System.out.println("Negatives:" + Negatives);
// this appends the main file to get the email, the unumber of unique words in the email, average length of the email
                                    PrintWriter MainWriter = new PrintWriter(new BufferedWriter(new FileWriter(file, true)));
                                    MainWriter.write(LINE_SEPARATOR);
                                    MainWriter.write(String.valueOf(filenumber));
                                    MainWriter.write("\t");
                                    MainWriter.write(String.valueOf(NoOfUnique));
                                    MainWriter.write("\t");
                                    MainWriter.write(String.valueOf(AverageMsgLen));
                                    MainWriter.write("\t");
                                    MainWriter.write(String.valueOf(Positives));
                                    MainWriter.write("\t");
                                    MainWriter.write(String.valueOf(Positives));
                                    MainWriter.write("\t");
                                    MainWriter.flush();
                                    MainWriter.close();

                                    // this is to clear all the variables for the othre incoming emails
                                    filenumber++;
                                    result.clear();
                                    Uniques.clear();
                                    content = null;
                                    contentWithOutForward = null;
                                    parts = null;
                                    contentLength = null;
                                    
                                }// if isFile

                            } catch (MessagingException e) {
                                throw new IllegalStateException("illegal state issue", e);
                            }
                        }
                        
                    }//if for the subdirectory2Path

                }// for name2

            } // if subdirectoryPath
            // this is write the union file for each user
            PrintWriter UnionWriter = new PrintWriter(new BufferedWriter(new FileWriter(file2, true)));
            for (String str : Union) {
                UnionWriter.println(str);
            }
            UnionWriter.flush();
            UnionWriter.close();
            EmailClustering app = new EmailClustering();
            app.matrix(Union, name, filenumber);
           // app.Analysis(Union);
        } // for ten directories

        return Union;
    }
    // this method is used to get the sentiment of the emails
    public int[] Sentiment(List UserEmail) throws FileNotFoundException, IOException {
        Scanner scanPos = new Scanner(new File("\Positive Words.txt"));
        Scanner scanNeg = new Scanner(new File("Negative Words.txt"));
        
        ArrayList<String> PositiveWords = new ArrayList<String>();
        ArrayList<String> NegativeWords = new ArrayList<String>();
        while (scanPos.hasNext()) {
            PositiveWords.add(scanPos.next());
        }
        scanPos.close();
        while (scanNeg.hasNext()) {
            NegativeWords.add(scanNeg.next());
        }
//        while (scanNeg.hasNext()) {
//            System.out.println(scanNeg.next());
//        }
        scanNeg.close();
        int[] value = new int[2];
        for (int j = 0; j < UserEmail.size(); j++) {
            String word = (String) UserEmail.get(j);
            for (int k = 0; k < PositiveWords.size(); k++) {
                String positiveword = PositiveWords.get(k);
                if (positiveword.equals(word)) {
                    //  System.out.println("word: " + word + " Positive:" + PositiveWords.get(k));
                    value[0] = value[0] + 1;
                }
                
            }
            for (int k = 0; k < NegativeWords.size(); k++) {;
                if (word.equals(NegativeWords.get(k))) {
                    value[1] = value[1] + 1;
                }
                
            }
            
        } // for the userEmails
        return value;
        
    }
    // this method is used to get the final matrix which will be given as an input to our models
    
    void matrix(List Union, String UserName, int numberEmails) throws FileNotFoundException, IOException {
        int ListSize = Union.size();
        //  numberEmails=5;
        System.out.println("List Size:" + ListSize);
        float[][] InputMatrix = new float[numberEmails][ListSize];
        String[][] FinalInput = new String[numberEmails][ListSize];
        for (int i = 0; i < numberEmails; i++) {
            for (int j = 0; j < ListSize; j++) {
                InputMatrix[i][j] = (float) 0.0;
                //       InputMatrix[i][j] = (float) 0.0;
            }
        }
        for (int i = 0; i < numberEmails; i++) {
            Scanner files = new Scanner(new File("Results\\" + UserName + "\\" + i + ".txt"));
            ArrayList<String> fileWords = new ArrayList<String>();
            while (files.hasNext()) {
                fileWords.add(files.next());
            }
            
            for (int k = 0; k < fileWords.size(); k++) {
                for (int j = 0; j < Union.size(); j++) {
                    if (Union.get(j).equals(fileWords.get(k))) {
                        //   System.out.println("this matrix is working");
                        InputMatrix[i][j] = InputMatrix[i][j] + 1;
                    }
                }
                
            }
            
            fileWords.clear();
        }// for the list of all the emails

        File file1 = new File(Results\\" + UserName + " Matrix.txt");
        PrintWriter out1 = new PrintWriter(new FileWriter(file1, true));
        int columns = InputMatrix.length;
        for (int i = 0; i < numberEmails; i++) {
            for (int j = 0; j < ListSize; j++) {
                out1.print(String.valueOf(InputMatrix[i][j]));
                out1.print(" ");
            }
            out1.print(LINE_SEPARATOR);
        }
        
        out1.flush();
        out1.close();
    }// method end
    // this method is used for the analysis of the data that is recieved as an output from the models
    void Analysis(List Union) throws FileNotFoundException, IOException {
        Scanner file1 = new Scanner(new File("weldonKMeans.txt")).useDelimiter(",| ");
        Scanner file2 = new Scanner(new File("weldonLDA.txt")).useDelimiter(" ");
        ArrayList<String> Clusters = new ArrayList<String>();
        ArrayList<Float> LDAoutput = new ArrayList<Float>();
        int colums = Union.size();
       float[] LDAArray = new float [LDAoutput.size()];
       float [][] LDAfinal = new float [20][colums];
        String temp = (String) Union.get(137);
        System.out.println("Word: "+temp);
        File output = new File("FinalWords.txt");
        PrintWriter out1 = new PrintWriter(new FileWriter(output, true));
        
        while (file1.hasNext()) {
           
            Clusters.add(file1.next());
        }
        file1.close();
        int index = 0;
        while (file2.hasNext()) {

            LDAoutput.add(Float.parseFloat(file2.next()));
            LDAArray[index] = Float.parseFloat(file2.next());
            index++;
        }
        file2.close();
        for (int j = 0; j < LDAoutput.size(); j++) {
            System.out.print(LDAArray[j]);
            if (LDAArray[j] == 0.0) {
                LDAArray[j] = (float) 0.5;

            }
        }
         ArrayList<Integer> Cluster = new ArrayList<Integer>();
        for (int i = 1; i <= 20; i++) {
            out1.write("For the cluster " + i + " ");
            System.out.println("For the cluster " + i + " ");
            for (int j = 0; j < Clusters.size(); j++) {
               System.out.println(Clusters.get(j));
               Integer.toString(i);
                if ((Clusters.get(j)).equals(i)) {
                    System.out.println("this is awesome");
                    Cluster.add(j);
                    String Word = (String) Union.get(j);
                    System.out.println(Word +" ");
                    out1.write(Word + " ");
                    out1.flush();
                    
                }
                out1.write(LINE_SEPARATOR);
                out1.close();
                Cluster.clear();
                
            }
        }
        
    }
    
}
