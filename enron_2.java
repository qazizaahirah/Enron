/*
 This program is being written for the preprocessing of the Enron data. This program is written for an assignement given for the Machine Learning for Big Data course
This program creates all the emails present in the entire directorygnement given for the Machine Learning for Big Data course for Fall 2015

Input: Is the folder that contains the emails of the employees. The emails are divided in different folders, where each folder belongs to Each employee
 */
package enron;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import sun.misc.IOUtils;

/**
 *
 * @author Qazi Zaahirah
 */
public class enron_2 {

    public static void main(String[] args) throws IOException {

        List<String> myList = new ArrayList<>();
        BigData2 data = new BigData2();
        String FolderPath = "InputPath";
        myList = data.GetEmailId(FolderPath);
        data.GetEmails(FolderPath, myList);

    }

    public List GetEmailId(String path, String[]TenDirectories) throws FileNotFoundException, IOException {
        File Directory = new File(path);
        File file2 = new File("C:\\Users\\dell\\Desktop\\DalDocuments\\MachineLearningBigData\\Assignment3\\Results\\Directories.txt");
        BufferedWriter out2 = new BufferedWriter(new FileWriter(file2, true));
        for (String name :TenDirectories){
            out2.write(name);
            out2.newLine();
      //  System.out.println("the directories have been named");
        }
        out2.flush();
        out2.close();
        List<String> myList = new ArrayList<>();
        int i = 0;
        for (String name : TenDirectories) {
            String subDirectoryPath = path + "//" + name;
            File subDirectory = new File(subDirectoryPath);
            String[] subDirectoryNames = subDirectory.list();

            if (new File(subDirectoryPath).isDirectory()) {
                int count = 0;
                for (String name2 : subDirectoryNames) {
                    String Subdirectory2Path = subDirectoryPath + "//" + name2;
                    if (new File(Subdirectory2Path).isDirectory()) {
                        if (name2.indexOf("sent") != -1) {
                            File f = new File(Subdirectory2Path);
                            String[] fileList = f.list();

                            for (String str : fileList) {
                                String FinalFilePath = Subdirectory2Path + "//" + str;
                                BufferedReader br = new BufferedReader(new FileReader(FinalFilePath));
                                String line;
                                int counter = 0;
                                while ((line = br.readLine()) != null) {
                                    counter++;
                                    if (counter == 3) {
                                        String[] part = line.split(" ");
                                        myList.add(part[1]);
                                    }
                                }

                            }

                        }//if for the sent items

                    }//if for the subdirectory2Path

                }// for name2

            } // if subdirectoryPath
        } // for name

        Set<String> emailId;
        emailId = new HashSet<String>();
        emailId.addAll(myList);
        myList.clear();
        myList.addAll(emailId);
        File file = new File("output.txt");
        BufferedWriter out = new BufferedWriter(new FileWriter(file, true));
        for (String value : myList) {
            out.write(value);
            out.newLine();
        }
        out.flush();
        out.close();

        return myList;
    }// method end

    public Object[] GetSenderAndReciever(String path, List myList, String[] TenDirectories) throws FileNotFoundException, IOException {
        int size = myList.size();
        String[] myEmailList = new String[size];
        myList.toArray(myEmailList);
       
        int[] RecievedEmails = new int[size];
        int[] SentEmails = new int[size];
        for (int i = 0; i < size; i++) {
            RecievedEmails[i] = 0;
            SentEmails[i] = 0;
            System.out.println(myEmailList[i]);
        }

        File Directory = new File(path);
        String[] Directorynames = Directory.list();
        for (String name : TenDirectories) {
            String subDirectoryPath = path + "//" + name;
            File subDirectory = new File(subDirectoryPath);
            String[] subDirectoryNames = subDirectory.list();

            if (new File(subDirectoryPath).isDirectory()) {
                int count = 0;
                for (String name2 : subDirectoryNames) {
                    String Subdirectory2Path = subDirectoryPath + "//" + name2;
                    if (new File(Subdirectory2Path).isDirectory()) {
                        File f = new File(Subdirectory2Path);
                        String[] fileList = f.list();
                        for (String str : fileList) {
                            String FinalFilePath = Subdirectory2Path + "//" + str;
                            if (new File(FinalFilePath).isFile()) {

                                BufferedReader br = new BufferedReader(new FileReader(FinalFilePath));
                             //   System.out.println(FinalFilePath);
                                String line;
                                int counter = 0;
                                while ((line = br.readLine()) != null) {
                                    counter++;
                                    if (counter < 5) {
                                        
                                                 
                                        if (counter == 3) {
                                            String part = line.substring(line.indexOf("From:") + 6);
                                            for (int indexforList = 0; indexforList < size; indexforList++) {
                                                
                                              // System.out.println("List: "+myEmailList[indexforList]);
                                                 //  System.out.println("Part: "+part);
                                                    
                                                if (myEmailList[indexforList].equals(part)) {
                                                  //  System.out.println("this loop is working");
                                                    SentEmails[indexforList] = SentEmails[indexforList] + 1;
                                                  //  System.out.println(SentEmails[indexforList]);
                                                }
                                            }
                                        }
                                        if (counter == 4) {
                                            String part = line.substring(line.indexOf("To:") + 4);
                                            for (int index = 0; index < size; index++) {
                                                if (myEmailList[index].equals(part)) {
                                                    RecievedEmails[index] = RecievedEmails[index] + 1;
                                                }
                                            }

                                        }
                                    } else {
                                        break;
                                    }
                                }// while loop

                                //}
                            }
                        }
                    }
                }
            }
        }
        System.out.println("this is the recieved emails");
        for (int j = 0; j < size; j++) {
            System.out.println(RecievedEmails[j]);

        }
        System.out.println("this is the sent emails");
        for (int j = 0; j < size; j++) {
            System.out.println(SentEmails[j]);

        }
        File file = new File("ProcessedData.txt");
        BufferedWriter out1 = new BufferedWriter(new FileWriter(file, true));
        for (int value=0;value<size;value++) {
            out1.write(myEmailList[value]);
            out1.write("\t");
            out1.write(Integer.toString(RecievedEmails[value]));
            out1.write("\t");
            out1.write(Integer.toString(SentEmails[value]));
            out1.newLine();
        }
        out1.flush();
        out1.close();
        File file2 = new File("Sent.txt");
        BufferedWriter out2 = new BufferedWriter(new FileWriter(file2, true));
        for (int value=0;value<size;value++) {
            out2.write(myEmailList[value]);
            out2.write("\t");
           
            out2.write(Integer.toString(SentEmails[value]));
            out2.newLine();
        }
        out2.flush();
        out2.close();
        return new Object[]{RecievedEmails,SentEmails};
    }

}// class end
