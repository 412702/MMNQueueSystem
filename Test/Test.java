package experiment.Ex11_QueuingSystemSimulationEventSchedulingApproach.Test;

import experiment.Ex11_QueuingSystemSimulationEventSchedulingApproach.MMNQueuingSystem.MMNQueuingSystem;
import experiment.Ex11_QueuingSystemSimulationEventSchedulingApproach.MMNQueuingSystem.Recorder;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.lang.reflect.InvocationTargetException;

public class Test {
    public static void main(String[] args) {
        MMNQueuingSystem mmnQueuingSystem = new MMNQueuingSystem(2, 2, 3, 12L, 13L, 3000,100,0);

        try {
            mmnQueuingSystem.Start();
            mmnQueuingSystem.wirteRecordsInEXCEL(new FileOutputStream("F:\\output-1.xlsx"));
            int i;
        } catch (InvocationTargetException | IllegalAccessException | FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}
