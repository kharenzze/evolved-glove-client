package evolved_glove

/**
  * Created by Paolo on 06/09/2017.
  */
object EGCalibration {
  val nFingers = 10
  val maxFingersVoltage = Array.fill[Double](nFingers)(0)
  val minFingersVoltage = Array.fill[Double](nFingers)(Double.PositiveInfinity)
  
  def recalibrate (statusVoltage : Array[Double]): Unit =  {
    for (i <- 0 until nFingers) {
      recalibrateFinger(i, statusVoltage(i))
    }
  }

  def recalibrateFinger (finger : Int, voltage : Double): Unit = {
    if (voltage > maxFingersVoltage(finger)) {
      maxFingersVoltage(finger) = voltage
    } else if (voltage < minFingersVoltage(finger)) {
      minFingersVoltage(finger) = voltage
    }
  }
  
  def normalizeVoltages (statusVoltage : Array[Double]): Array[Double] = {
    val normalizedData = new Array[Double](nFingers)
    for (i <- 0 until nFingers) {
      normalizedData(i) = normalizeFingerVoltage(i, statusVoltage(i))
    }
    normalizedData
  }

  def normalizeFingerVoltage (finger :Int, voltage :Double) = relativeFingerVoltage(finger, voltage) / fingerAmplitude(finger)
  private def fingerAmplitude (finger :Int): Double = maxFingersVoltage(finger) - minFingersVoltage(finger)
  private def relativeFingerVoltage (finger :Int, voltage :Double): Double = voltage - minFingersVoltage(finger)
}
