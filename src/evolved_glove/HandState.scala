package evolved_glove

import java.net.DatagramPacket
import java.nio.ByteBuffer
import evolved_glove.EGCalibration

/**
  * Created by Paolo on 3/9/17.
  */
class HandState (pkt : DatagramPacket) {
  val normalizedFingers =  new Array[Double](EGCalibration.nFingers)
  val orientation = new Array[Double](3)
  private var _timestamp : Long = 0

  loadPkt(pkt)

  private def _loadInt (data : Array[Byte]): Int = {
    ByteBuffer.wrap(data.reverse).getInt()
  }

  private def _loadLong (data : Array[Byte]): Long = {
    ByteBuffer.wrap(data.reverse).getLong()
  }

  private def _loadDouble (data : Array[Byte]): Double = {
    ByteBuffer.wrap(data.reverse).getDouble()
  }

  def loadPkt (pkt : DatagramPacket): Unit = {
    val pktIterator = new PacketLoaderHelper(pkt)
    var fingerNumber = 0
    var orientationNumber = 0
    while (pktIterator.hasNext) {
      val iteration = pktIterator.currentDataIteration
      val _data = pktIterator.next()

      if (iteration == 0) {
        _timestamp = _loadLong(_data)
        if (EGConfig.debugPackets)
          println(_timestamp)
      } else if (iteration < 9) {
        val voltage = _loadDouble(_data)
        EGCalibration.recalibrateFinger(fingerNumber, voltage)
        normalizedFingers(fingerNumber) = EGCalibration.normalizeFingerVoltage(fingerNumber, voltage)
        if (EGConfig.debugPackets)
          println(s"Current $fingerNumber, data ${normalizedFingers(fingerNumber)}   ")
        fingerNumber += 1
      } else {
        orientation(orientationNumber) = _loadDouble(_data)
        if (EGConfig.debugPackets)
          println(s"Orientation $orientationNumber = ${orientation(orientationNumber)}")
        orientationNumber += 1
      }
    }
  }

  def timestamp (): Long = _timestamp

  class PacketLoaderHelper(pkt : DatagramPacket) {
    private var currentByte = 0
    var currentDataIteration : Int = 0
    private val dataLength = 8

    val totalBytes : Int = pkt.getLength

    if ((totalBytes % dataLength) != 0 ) {
      System.err.println("[warning] Unexpected number of bytes received")
    }

    private val buffer = pkt.getData.take(totalBytes)

    def hasNext : Boolean = currentByte < totalBytes

    def next (): Array[Byte] = {
      val startByte = currentByte
      currentByte += dataLength
      currentDataIteration += 1
      buffer.slice(startByte, currentByte)
    }
  }
}
