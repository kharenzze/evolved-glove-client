package evolved_glove

import java.io._
import java.net._
import java.nio.ByteBuffer

/**
  * Created by Paolo on 2/9/17.
  */
class Main {
  def main(args: Array[String]) {
    val SIZE = 1024
    val PORT = 58285
    val buffer = new Array[Byte](SIZE)
    val pkt_in = new DatagramPacket(buffer, buffer.length)

    val socket = new DatagramSocket(PORT)

    //    val buffer = new Array[Byte](SIZE)
    //    val packet = new DatagramPacket(buffer, buffer.length)
    //      socket.receive(packet)
    //      val message = new String(packet.getData)
    //      val ipAddress = packet.getAddress().toString
    //      println("received from " + ipAddress + ": " + message)

    val data = "HELLO".getBytes()
    val ipDest = InetAddress.getByName("piwifi")
    val port = 58878
    val packet = new DatagramPacket(data, data.length, ipDest, port)

    socket.send(packet)

    while (true) {
      socket.receive(pkt_in)

      //      val message = new String(pkt_in.getData().take(pkt_in.getLength))
      //      println(message)

      val data = pkt_in.getData().take(pkt_in.getLength).reverse
      val intData = ByteBuffer.wrap(data).getFloat()
      println(s"Received ${intData}, length ${pkt_in.getLength}   ")
      for (i <- 0 to 3) {
        print(pkt_in.getData()(i))
      }
    }


    // respond
    //    socket.send(packet)
  }
}
