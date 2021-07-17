import org.apache.flink.api.common.functions.RichMapFunction;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.metrics.Counter;

public class SawtoothFunction extends RichMapFunction<DataPoint<Long>, DataPoint<Double>>  {

    final private int numSteps;
    private Counter datapoints;

    // State!
    private int currentStep;

    public SawtoothFunction(int numSteps){
        this.numSteps = numSteps;
        this.currentStep = 0;
    }

    @Override
    public void open(Configuration config) {
        this.datapoints = getRuntimeContext()
                .getMetricGroup()
                .counter("datapoints");
    }

    @Override
    public DataPoint<Double> map(DataPoint<Long> dataPoint) throws Exception {
        double phase = (double) currentStep / numSteps;
        currentStep = ++currentStep % numSteps;
        this.datapoints.inc();
        return dataPoint.withNewValue(phase);
    }

}