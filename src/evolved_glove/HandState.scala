package evolved_glove

import java.net.DatagramPacket
import java.nio.ByteBuffer

/**
  * Created by Paolo on 3/9/17.
  */
class HandState (pkt : DatagramPacket) {

  loadPkt(pkt)

  def _loadInt (data : Array[Byte]): Int = {
    ByteBuffer.wrap(data.reverse).getInt()
  }

  def _loadDouble (data : Array[Byte]): Double = {
    ByteBuffer.wrap(data.reverse).getDouble()
  }

  def loadPkt (pkt : DatagramPacket): Unit = {
    val l = pkt.getLength
    val bytes = pkt.getData

    var currentByte = 0
    var i = 1
    while (currentByte < l) {
      val startByte = currentByte
      currentByte += 8
      val _data = bytes.slice(startByte, currentByte)
      val num = _loadDouble(_data)
      println(s"Current ${i}, data ${num}   ")
      i += 1
    }
  }
}
