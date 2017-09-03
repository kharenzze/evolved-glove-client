package evolved_glove

import java.io._
import java.net._
import java.nio.ByteBuffer

/**
  * Created by Paolo on 2/9/17.
  */
object Main {
  val SIZE = 1024
  val SRC_PORT = 58285
  val DST_PORT = 58878
  val DST_HOST_NAME = "piwifi"
  var socket : DatagramSocket = _

  def initialize(): Unit = {
    socket = new DatagramSocket(SRC_PORT)
  }

  def sendHelloMessage():Unit = {
    val message = "HELLO"
    val data = message.getBytes()
    val ipDest = InetAddress.getByName(DST_HOST_NAME)
    val packet = new DatagramPacket(data, data.length, ipDest, DST_PORT)
    socket.send(packet)
  }

  def receivePacket():DatagramPacket = {
    val buffer = new Array[Byte](SIZE)
    val pkt_in = new DatagramPacket(buffer, buffer.length)
    socket.receive(pkt_in)
    pkt_in
  }

  def main(args: Array[String]) {
    initialize()
    sendHelloMessage()

    while (true) {
      val pkt_in = receivePacket()
      val data = pkt_in.getData().take(pkt_in.getLength).reverse
      val intData = ByteBuffer.wrap(data).getInt()
      println(s"Received ${intData}, length ${pkt_in.getLength}   ")
    }
  }
}
