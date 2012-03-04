/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package submarinemodel;

import java.util.TimerTask;

/**
 *
 * @author Administrator
 */
public class SensorsTimerTask extends TimerTask {

    private Sensors sensors;

    public SensorsTimerTask(Sensors s) {
        sensors = s;
    }

    
    public void run() {
        sensors.sendData();
    }
}
