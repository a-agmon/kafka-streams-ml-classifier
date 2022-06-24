package com.aagmon.demos

import predict.PredictRequest
import org.apache.kafka.common.serialization.Serde
import org.apache.kafka.streams.scala.serialization.Serdes

object DomainSerdes {
  implicit val RequestSerde: Serde[PredictRequest] = Serdes.fromFn(
    //serializer
    (request:PredictRequest) => request.toByteArray,
    //deserializer
    (requestBytes:Array[Byte]) => Option(PredictRequest.parseFrom(requestBytes))
  )
}

