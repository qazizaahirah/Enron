/*
 This program is being written for the preprocessing of the Enron data. This program is written for an assignement given for the Machine Learning for Big Data course for Fall 2015

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
public class enron_1 {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException {

        List<String> myList = new ArrayList<>();
        BigData data = new BigData();
        String FolderPath ="Path of the input Data"
        myList = data.GetEmailId(FolderPath);
        data.GetEmails(FolderPath, myList);

    }

    public List GetEmailId(String path) throws FileNotFoundException, IOException {
        File Directory = new File(path);
        String[] Directorynames = Directory.list();
        List<String> myList = new ArrayList<>();
        int i = 0;
        for (String name : Directorynames) {
            // System.out.println("this is so cool");
            String subDirectoryPath = path + "//" + name;
            File subDirectory = new File(subDirectoryPath);
            String[] subDirectoryNames = subDirectory.list();

            if (new File(subDirectoryPath).isDirectory()) {
                // System.out.println(name);
                int count = 0;
                for (String name2 : subDirectoryNames) {
                    String Subdirectory2Path = subDirectoryPath + "//" + name2;
                    if (new File(Subdirectory2Path).isDirectory()) {
                        if (name2.indexOf("sent") != -1) {
                            File f = new File(Subdirectory2Path);
                            String[] fileList = f.list();

                            for (String str : fileList) {
                                String FinalFilePath = Subdirectory2Path + "//" + "1";
                                BufferedReader br = new BufferedReader(new FileReader(FinalFilePath));
                                String line;
                                int counter = 0;
                                while ((line = br.readLine()) != null) {
                                    counter++;
                                    if (counter == 3) {
                                        String[] part = line.split(" ");
                                        myList.add(part[1]);
//                                        System.out.println(myList.get(i));
//                                        i++;
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
        //  System.out.println(myList.size());
//        for (i = 0; i < myList.size(); i++) {
//            System.out.println(myList.get(i));
//        }
        File file = new File("output.txt");
        BufferedWriter out = new BufferedWriter(new FileWriter(file, true));
        for (String value : myList) {
            out.write(value + "\n");
        }
        out.flush();
        out.close();

        return myList;
    }// method end

    public void GetEmails(String path, List myList) throws FileNotFoundException, IOException {
        int size = myList.size();
        String [] myEmailList = new String [size];
        myList.toArray(myEmailList);
      //  System.out.println(size);
        //  System.out.println(myList.get(size - 1));
        //   System.out.println(myList.get(0));
        int[] RecievedEmails = new int[size];
        int[] SentEmails = new int[size];
        for (int i = 0; i < size; i++) {
            RecievedEmails[i] = 0;
            SentEmails[i] = 0;
        }

        File Directory = new File(path);
        String[] Directorynames = Directory.list();
        int i = 0;
        for (String name : Directorynames) {
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
                           
                                String line;
                                int counter = 0;
//                                while ((line = br.readLine()) != null) {
//                                    counter++;
//                                }// this loop reads the total number of line in the file
                              //  if (counter > 2) {
                               //     counter = 0;
                                    while ((line = br.readLine()) != null) {
                                    counter++;
                                    //  System.out.println("Counter "+counter);
                                    if (counter < 5) {
                                        if (counter == 3) {
                                            // System.out.println("Counter From "+counter);
//                                            String[] part = line.split(" ");
                                            String part = line.substring(line.indexOf("From:") + 6);
                                           // String part = line.substring(6);
                                            //   System.out.println(part[1]);
                                            //  System.out.println(name+name2+str + line);
                                            if (part == null) {
                                                continue;
                                            }
                                            for (int indexforList = 0; indexforList < size; indexforList++) {
                                                if (myEmailList[indexforList] == part) {
                                                    SentEmails[indexforList] = SentEmails[indexforList] + 1;
                                                    //   System.out.println("sent email "+SentEmails[index]);
                                                }
                                            }
                                        }
                                      if (counter==4) {
                                          // System.out.println("Counter To "+counter);
                                           // String[] part2 = line.split(" ");
                                           
                                              //  System.out.println(name+name2 + str + line);
                                            String part = line.substring(line.indexOf("To:")+4);
                                            for (int index = 0; index < size; index++) {
                                                //    if (myList.get(index) ==  part[1]) {
                                                if (myEmailList[index] == part) {
                                                    RecievedEmails[index] = RecievedEmails[index] + 1;
                                                  //  System.out.println("recieved email "+RecievedEmails[index]);
                                                   
                                                 //   System.out.println("this is more awesome");
                                                }
                                            }
                                           
                                        }
                                    }
                                        else 
                                    break;
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
        File file = new File("Recieved.txt");
        BufferedWriter out1 = new BufferedWriter(new FileWriter(file, true));
        for (int value : RecievedEmails) {
            out1.write(Integer.toString(value));
            out1.newLine();
        }
        out1.flush();
        out1.close();
        File file2 = new File("Sent.txt");
        BufferedWriter out2 = new BufferedWriter(new FileWriter(file2, true));
        for (int value : SentEmails) {
            out2.write(Integer.toString(value));
            out2.newLine();
        }
        out2.flush();
        out2.close();
    }

}// class end
