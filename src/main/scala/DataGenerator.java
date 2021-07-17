import org.apache.flink.api.common.restartstrategy.RestartStrategies;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

public class DataGenerator {

    public static DataStream<KeyedDataPoint<Double>> generateSensorData(StreamExecutionEnvironment env) {

        // boiler plate for this demo
        env.setRestartStrategy(RestartStrategies.fixedDelayRestart(1000, 1000));
        env.setMaxParallelism(8);
        env.setParallelism(1);
        env.disableOperatorChaining();
        env.getConfig().setLatencyTrackingInterval(1000);

        final int SLOWDOWN_FACTOR = 1;
        final int PERIOD_MS = 100;

        // Initial data - just timestamped messages
        DataStreamSource<DataPoint<Long>> timestampSource =
                env.addSource(new TimestampSource(PERIOD_MS, SLOWDOWN_FACTOR), "test data");

        // Transform into sawtooth pattern
        SingleOutputStreamOperator<DataPoint<Double>> sawtoothStream = timestampSource
                .map(new SawtoothFunction(10))
                .name("sawTooth");

        // Simulate temp sensor
        SingleOutputStreamOperator<KeyedDataPoint<Double>> tempStream = sawtoothStream
                .map(new AssignKeyFunction("temp"))
                .name("assignKey(temp)");

//        // Make sine wave and use for pressure sensor
//        SingleOutputStreamOperator<KeyedDataPoint<Double>> pressureStream = sawtoothStream
//                .map(new SineWaveFunction())
//                .name("sineWave")
//                .map(new AssignKeyFunction("pressure"))
//                .name("assignKey(pressure");
//
//        // Make square wave and use for door sensor
//        SingleOutputStreamOperator<KeyedDataPoint<Double>> doorStream = sawtoothStream
//                .map(new SquareWaveFunction())
//                .name("squareWave")
//                .map(new AssignKeyFunction("door"))
//                .name("assignKey(door)");

        // Combine all the streams into one and write it to Kafka
        System.out.println("temp");
        tempStream.print();
        DataStream<KeyedDataPoint<Double>> sensorStream =
                tempStream
                       // .union(pressureStream)
                       // .union(doorStream)
         ;

        return sensorStream;
    }

}
