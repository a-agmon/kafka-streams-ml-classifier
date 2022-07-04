version := "0.1.0"
scalaVersion := "2.12.16"
name := "KafkaStreamClassifer"


libraryDependencies ++= Seq(

  // this is a major problem - get this right
  // https://mvnrepository.com/artifact/com.thesamet.scalapb/scalapb-runtime
  "com.thesamet.scalapb" %% "scalapb-runtime" % "0.11.11",

  // XGBoost
  // https://mvnrepository.com/artifact/ml.dmlc/xgboost4j
  "ml.dmlc" %% "xgboost4j" % "1.6.1",

  // Kafka
  "org.apache.kafka" % "kafka-streams" % "3.1.0",
  "org.apache.kafka" % "kafka-clients" % "3.1.0",
  "org.apache.kafka" %% "kafka-streams-scala" % "3.1.0",

  // Logging
  "org.slf4j" % "slf4j-api" % "1.7.5",
  "org.slf4j" % "slf4j-simple" % "1.7.5",

  // https://mvnrepository.com/artifact/io.circe/circe-core
  "io.circe" %% "circe-core" % "0.15.0-M1",
  "io.circe" %% "circe-generic" % "0.15.0-M1",
  "io.circe" %% "circe-parser" % "0.15.0-M1",


)