package com.aagmon.demos

import com.aagmon.demos.Classifier.model
import ml.dmlc.xgboost4j.LabeledPoint
import ml.dmlc.xgboost4j.scala.{Booster, DMatrix, XGBoost}
import org.slf4j.{Logger, LoggerFactory}


//wraps the machine learning classifier logic
object Classifier {
  val logger: Logger = LoggerFactory.getLogger("StreamsClassifierModel")
  var model: Option[Booster] = None

  def Init(modelFile:String): Unit = {
    if (model.isEmpty) {
      model = Some(XGBoost.loadModel(modelFile))
      logger.info(s"Model loaded from $modelFile")
    }
  }

  private def getInputVector(rawVector:Seq[Float]): DMatrix = {
    val lp = LabeledPoint(0, rawVector.length, null,  rawVector.toArray)
    new DMatrix(Iterator(lp))
  }

  def predict(recordID:String, features:Seq[Float]): (String, Float) = {
    val xgbInput = getInputVector(features)
    // can also be expressed as:
    //val result:Array[Array[Float]] = model.get.predict(xgbInput)
    val prediction:Float = model
      .map(m => m.predict(xgbInput))
      .map(result => result(0)(0))
      .getOrElse(-1)
    (recordID, prediction)
  }

}