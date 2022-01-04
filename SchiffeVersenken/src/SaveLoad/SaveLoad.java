/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SaveLoad;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;

/**
 *
 * @author TDoes
 */
public class SaveLoad {
    public static void write2File(File file, String content){
        try {
            PrintWriter pw = new PrintWriter(file);
            pw.println(content);
            pw.close();
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        }
    }
    
    public static void readFromFile(File file){
        try {
            Scanner sc = new Scanner(file);
            
            while(sc.hasNextLine()){
                System.out.println(sc.nextLine());
            }
            sc.close();
            
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        }
    }
}
