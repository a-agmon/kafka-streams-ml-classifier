package com.aagmon.demos

import io.circe.{Decoder, Encoder, Error}
import io.circe.generic.semiauto.{deriveDecoder, deriveEncoder}
import predict.PredictRequest
import org.apache.kafka.common.serialization.Serde
import org.apache.kafka.streams.scala.serialization.Serdes
import io.circe.parser._
import io.circe.syntax._


object DomainSerdes {

  implicit val RequestSerde: Serde[PredictRequest] = Serdes.fromFn(
    //serializer
    (request:PredictRequest) => request.toByteArray,
    //deserializer
    (requestBytes:Array[Byte]) => Option(PredictRequest.parseFrom(requestBytes))
  )

  case class Event (Type:String, Timestamp:BigInt, Severity:String)

  implicit val usEncoder: Encoder[Event] = deriveEncoder[Event]
  implicit val usDecoder: Decoder[Event] = deriveDecoder[Event]

  implicit val EventSerde : Serde[Event] = Serdes.fromFn(
    (event:Event) => event.asJson.noSpaces.getBytes,
    (eventBytes:Array[Byte]) =>  {
      val results : Either[Error, Event]  = decode(new String(eventBytes))
      results match {
        case Right(value) => Option(value)
      }
    }

  )
}

