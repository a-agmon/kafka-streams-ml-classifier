package com.aagmon.demos

// was generated with
// /Users/alonagmon/Downloads/scalapbc-0.11.1/bin/scalapbc -v3.5.1 --scala_out=src/main/scala src/main/protobuf/predict.proto
import predict.PredictRequest

import org.apache.kafka.streams.scala.kstream.KStream
import org.apache.kafka.streams.scala.StreamsBuilder
import org.apache.kafka.streams.{KafkaStreams, KeyValue, StreamsConfig, Topology}
import org.slf4j.LoggerFactory

import java.util.Properties

//Brings all implicit conversions in scope
import org.apache.kafka.streams.scala.ImplicitConversions._
// Bring implicit default serdes in scope
import org.apache.kafka.streams.scala.serialization.Serdes._
import DomainSerdes._


object Main {
  val logger = LoggerFactory.getLogger("StreamsAppMain")

  val modelPath = "/Users/alonagmon/MyData/work/golang-projects/vectors_model/fraud_model.bin"
  Classifier.initModel(modelPath)

  def getKafkaBrokerProperties(appID:String):Properties = {
    val bootstrapServer:String = scala.util.Properties.envOrElse("KAFKA_SERVER", "127.0.0.1:62036")
    val conf = new Properties()
    conf.put(StreamsConfig.APPLICATION_ID_CONFIG, appID)
    conf.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServer)
    conf.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, stringSerde.getClass)
    conf.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, stringSerde.getClass)
    conf
  }


  def getStreamTopology(inputTopic:String, outputTopic:String):Topology = {

    val builder = new StreamsBuilder()
    val reqStream = builder.stream[String, PredictRequest](inputTopic)
    reqStream
      .map( (_, request) => {
        Classifier.predict(request.recordID, request.featuresVector)
      })
      .peek((reqId, reqPrediction) => {
        logger.debug(s"Req:$reqId => $reqPrediction")
      })
      .to(outputTopic)


    builder.build()

  }

  def main(args: Array[String]): Unit = {

    logger.info("Starting App")
    val topology: Topology =
      getStreamTopology("test-topic-1", "test-topic-2")
    val applicationProps = getKafkaBrokerProperties("test-stream-app-1")
    val streams = new KafkaStreams(topology, applicationProps)

    streams.cleanUp() // only for test
    logger.info("Starting Stream")
    streams.start()

    Runtime.getRuntime.addShutdownHook(new Thread {
      override def run(): Unit = {
        logger.info("Closing Stream")
        streams.close()
      }
    })


  }
}