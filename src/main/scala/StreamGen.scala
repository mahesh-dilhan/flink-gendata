import org.apache.flink.api.java.ExecutionEnvironment
import org.apache.flink.api.java.io.TextInputFormat
import org.apache.flink.core.fs.Path
import org.apache.flink.streaming.api.datastream.DataStream
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment
import org.apache.flink.streaming.api.functions.source.FileProcessingMode

object StreamGen {

  def main(args: Array[String]): Unit = {

    val env = StreamExecutionEnvironment.getExecutionEnvironment
    val stream = env.readTextFile("src/main/resources/sensordata.txt")
//    val textStreaming = env.fromElements(
//      "To be, or not to be,--that is the question:--",
//      "Whether 'tis nobler in the mind to suffer",
//      "The slings and arrows of outrageous fortune",
//      "Or to take arms against a sea of troubles,")

    stream.print()
    val parsedStream = stream.map((line) => {
        val cells = line.split(",")
        val event =  SensorData(cells(0), cells(1), cells(2))
        event
      })
    parsedStream.print()
    env.execute("sensor")
  }

  case class SensorData(timestamp: String, sensor: String, value: String)
}