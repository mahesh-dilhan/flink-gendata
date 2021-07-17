import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment

object StreamGen {

  def main(args: Array[String]): Unit = {
    val env = StreamExecutionEnvironment.getExecutionEnvironment

   val stream = DataGenerator.generateSensorData(env)

    println(stream)
    stream.print()

  }

}