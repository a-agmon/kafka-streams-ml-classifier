package com.aagmon.demos

import DomainSerdes.{Event, EventSerde}
import org.apache.kafka.streams.{StreamsConfig, Topology}
import org.apache.kafka.streams.scala.StreamsBuilder
import org.apache.kafka.streams.scala.kstream.Branched
import org.apache.kafka.streams.scala.serialization.Serdes.stringSerde

import java.util.Properties

//Brings all implicit conversions in scope
import org.apache.kafka.streams.scala.ImplicitConversions._
// Bring implicit default serdes in scope
import org.apache.kafka.streams.scala.serialization.Serdes._
import DomainSerdes._


object BranchDemo {

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
    val messageStream = builder.stream[String, Event](inputTopic)
    messageStream.split()
      .branch((key, event) => event.Severity.equals("SOMETHING"), Branched.withConsumer(stream => stream.to("new-Topic")))
      .branch((key, event) => event.Severity.equals("SOMETHING"), Branched.withConsumer(stream => stream.to("new-Topic")))





    builder.build()

  }
  def main(args: Array[String]): Unit = {

  }

}
