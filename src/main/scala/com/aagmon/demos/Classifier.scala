package com.aagmon.demos

import ml.dmlc.xgboost4j.LabeledPoint
import ml.dmlc.xgboost4j.scala.{Booster, DMatrix, XGBoost}
import org.slf4j.LoggerFactory

//wraps the machine learning classifier logic
object Classifier {
  val logger = LoggerFactory.getLogger("StreamsClassifierModel")
  var model: Option[Booster] = None

  def initModel(filename:String): Unit = {
    if (model.isEmpty){
      model = Some(XGBoost.loadModel(filename))
      logger.info(s"Model loaded from $filename")
    }
  }

  def getInputVector(rawVector:Seq[Float]): DMatrix = {
    val lp = LabeledPoint(0, rawVector.length, null,  rawVector.toArray)
    new DMatrix(Iterator(lp))
  }

  def predict(recordID:String, features:Seq[Float]): (String, Float) = {
    val xgbInput = getInputVector(features)
    val result:Array[Array[Float]] = model.get.predict(xgbInput)
    (recordID, result(0)(0) )
  }

}