package cn.acyco.lab;

import cn.acyco.lab.config.Config;
import cn.acyco.lab.config.ServerConfig;
import cn.acyco.lab.config.ServerConfigEntry;

import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * @author Acyco
 * @create 2020-01-22 15:44
 */
public class Main {
    public static Process process; 
    

    public static void main(String[] args) throws Exception {
        for (String arg : args) {
            System.out.println(arg);
        }

        Config.loadServerConfig();
        ServerConfig config = Config.serverConfig;
        for (String key : config.getKeys()) {
            ServerConfigEntry serverConfigEntry = (ServerConfigEntry) config.getEntry(key);
            System.out.println(serverConfigEntry.getName());
            new Thread(() ->{
                try {
                    runCommond(serverConfigEntry.getRun_cmd());
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            },serverConfigEntry.getName()).start();
        }
   
      /*  Util.EnumOS os = Util.getOSType();
        if (os == Util.EnumOS.WINDOWS) {
            
        }
        System.out.println(new File(".").getAbsoluteFile());
        runCommond("java -jar server.jar");
*/

    }

    public static void runCommond(String cmd) throws IOException, InterruptedException {
        ArrayList list = new ArrayList();
         process = Runtime.getRuntime().exec(cmd);
        scanInput(process.getOutputStream());
        printMsg(process.getInputStream());
        printMsg(process.getErrorStream());
        int value = process.waitFor();

        System.out.println(value);

    }

    private static void scanInput(OutputStream outputStream) {
        new Thread(() -> {
            Scanner input = new Scanner(System.in);
            input.useDelimiter("\n");
            
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(outputStream));
            while (true) {
                String line = input.nextLine();
                try {
                    bw.write(line,0,line.length());
                    bw.write('\n');
                    bw.flush();
                  //  System.out.println(line);
                   // System.out.println(line.length());
                    
                    
                } catch (IOException e) {
                    e.printStackTrace();
                }
           
            }
        }).start();
    }

    public static void printMsg(final InputStream inputStream) {
        new Thread(() -> {
            BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
            String line = null;
            try {
                while ((line = br.readLine()) != null) {
                    System.out.println(line);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();


    }
}
