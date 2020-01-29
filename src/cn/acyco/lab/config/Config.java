package cn.acyco.lab.config;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.FileNotFoundException;
import java.io.IOException;


/**
 * @author Acyco
 * @create 2020-01-28 18:40
 */
public class Config {
  //  public 
  private static final Logger LOGGER = LogManager.getLogger();
    public static final ServerConfig serverConfig = ServerConfig.getInstance();

    public static void loadServerConfig() {
        if (!serverConfig.saveFile.exists() && !serverConfig.saveFile.isFile()) {
            
            try {
                serverConfig.saveFile.createNewFile();
                serverConfig.writeChanges();
//                serverConfig.read();
            } catch (IOException e) {
                LOGGER.error("config file created fail {}",serverConfig.saveFile.getName(),e);
            }
        }
        try {
            serverConfig.read();
        } catch (FileNotFoundException e) {
            LOGGER.warn("Could not load existing file {}", serverConfig.saveFile.getName(), e);
        }
      //  ServerConfigEntry serverConfigEntry = new ServerConfigEntry("default2", "default", "server.jar", "xxxx", "java -jar server.jar");
      //  serverConfig.addEntry( serverConfigEntry);
    }
    
    
}
