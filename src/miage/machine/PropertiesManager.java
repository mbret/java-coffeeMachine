package miage.machine;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

class PropertiesManager {
 
    
    public static Properties getProperties( ) throws Exception{
 
    	Properties prop = new Properties();
    	InputStream input = null;
 
    	try {

            String filename = "config.properties";
            input = PropertiesManager.class.getClassLoader().getResourceAsStream(filename);

            if(input == null){
                throw new FileNotFoundException("property file '" +filename+ "' not found in the classpath");
            }
            
            //load a properties file from class path, inside static method
            prop.load(input);
            return prop;
    	} 
        catch (IOException e) {
            throw new IOException(e);
        } 
        finally{
            if(input != null){
                input.close();
            }
        }
        
    }
}