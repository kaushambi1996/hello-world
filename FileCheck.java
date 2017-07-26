/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Kaushambi_Gujral
 */
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FileCheck {
    
    
    static BufferedReader br;
    static PrintWriter pout;
    static List<String> li;
    static List<String> notFound;
    static ListIterator<String> litr;
    
    
    public static void main(String args[])throws FileNotFoundException, IOException
    {
        //declarations
      br= new BufferedReader(new FileReader("urls.txt"));
   
      li = new ArrayList<String>();
      notFound= new ArrayList<String>();
      litr = null;
      
      
      String URL1="";
      
      
     while((URL1=br.readLine())!=null){
               URL1.trim();
               String can="";
               System.out.print("Original URL: ");
               System.out.println(URL1);
     //extract HTML Code
                String sourceCode= extractHTML(URL1); //works fine
                if(!(sourceCode.equals("40x error"))){
                    System.out.println("\t...Source code extracted");
     //extract canonical
                String canonical= extractCanonical(sourceCode);//works fine
                System.out.print("The canonical is: ");
                System.out.println(canonical);
     
     //make test URL
                String a=makeTestURL(URL1);
                a.trim();
        
                System.out.println("Test URL: "+a);
     //extract its source code
                String b=extractHTML(a);
     //extract its canonical
                can=extractCanonical(b);
                System.out.println("Its canonical");
                System.out.println(can);
                if(sourceCode.equals("40x error"))
                    System.out.println("40x error");
                else if((can.equals(canonical)))
                    System.out.println("Matched");
                else
                {
                    System.out.println("Not Matched");
                    li.add(URL1);
     
                }}
                else
                {
                
                    notFound.add(URL1);
                }
      System.out.println("--------------------------------------------------------------------------------------------\n");
     
     
   
     
     }
    
    printUnmatched();
    
    }
    public static String extractHTML(String url)throws IOException
    {
            URL yahoo = new URL(url);
            
            HttpURLConnection yc =(HttpURLConnection) yahoo.openConnection();
            yc.setUseCaches(false);
            int status_code= yc.getResponseCode();
            if(status_code==200){
            BufferedReader in = new BufferedReader(new InputStreamReader(
                    yc.getInputStream(), "UTF-8"));
            String inputLine;
            StringBuilder a = new StringBuilder();
            while ((inputLine = in.readLine()) != null)
                a.append(inputLine);
            in.close();

            return a.toString();}
            else{
             return ("40x error");
            }
    }
    
    
    public static String extractCanonical(String src)
    {
        
        String can="",s1="";
        Pattern p1=Pattern.compile("[<][\\s]*[l][i][n][k][\\s]*[r][e][l][\\s]*[=][\\s]*[\"][c][a][n][o][n][i][c][a][l][\"][\\s]*[h][r][e][f][\\s]*[=][\\s]*[\"].*[\"][\\s]*[/][>]");
        Matcher matcher = p1.matcher(src);
        while (matcher.find()) {
           
           //System.out.println("Yeah!!");
             s1=matcher.group();
             can= extract(s1);
           // System.out.println(s1);
        }
        
        
        return can;
    }
    
    
    
    public static String makeTestURL(String URL)
    {
     String s1="",testURL="";
         
       Pattern p1= Pattern.compile(".*[/][a][p][d].*");
         Matcher matcher = p1.matcher(URL);
        while (matcher.find()) {
           
         
             s1=matcher.group();
         
           testURL=extract2(s1);
          
        }
    return testURL;
    
    }
    
    
    
    
    public static void printUnmatched() throws IOException
    {
        System.out.println("The unmatched URLS are: ");
          litr=li.listIterator();
          String str="";
          int flag=0;
      pout= new PrintWriter(new BufferedWriter(new FileWriter("canonicals.txt")));
        while(litr.hasNext()){
            str=litr.next();
            flag=1;
            System.out.println(str);
            pout.append(str);
        }
        if(flag==0)
            System.out.println("none");
        
    }
    
    
    
    
    
     public static String extract(String full)
    {
            
        int i = full.indexOf("href");
       // System.out.println(i);
        int j= full.indexOf("\"",i);
        int k = full.indexOf("\"",j+1);
       // System.out.println(j);
       // System.out.println(k);
        String x= full.substring((j+1),k);
     return x;
    
    
    }
      public static String extract2(String URL)
    {
        int i= URL.indexOf("/apd/");
        String a=URL.substring(i);
        int j= URL.indexOf("/shop/");
        j+=5;
        String b= URL.substring(0,j);
       String c=b+"/test"+a;
       return c;
        
    
    }
    
}
