package experiment.Ex11_QueuingSystemSimulationEventSchedulingApproach.RandomNumber;

import java.util.Random;

/**
 * 泊松分布生成器
 */
public class ExponentialDistribution {
    Random random;
    double lamdba;

    public ExponentialDistribution(double lamdba){
        random=new Random();
        this.lamdba=lamdba;
    }

    public ExponentialDistribution(long seed,double lamdba){
        random=new Random(seed);
        this.lamdba=lamdba;
    }

    public double next(){
        double r=random.nextDouble();
        return -1*(Math.log(1-r)/lamdba);
    }

}
