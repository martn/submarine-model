/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package submarinemodel;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
//import java.lang.String;
import java.util.Properties;

/**
 *
 * @author Martin Novak
 */
public class Configuration extends Properties {
    private Log log;

    public Configuration() {
        super();

        log = new Log();

        try{
            load(new FileInputStream("config.properties"));
        } catch(FileNotFoundException e) {
            log.write("file not found");
        } catch(IOException exc) {
            log.write("io exception");
        }
    }


}
