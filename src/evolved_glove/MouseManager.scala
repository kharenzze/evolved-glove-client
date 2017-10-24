package evolved_glove
import java.awt.{MouseInfo, Robot}
import java.awt.event.InputEvent
import java.awt.Toolkit

/**
  * Created by Paolo on 08/09/2017.
  */
object MouseManager {
  private val robot = new Robot()
  private val sens = {
    val screenDimensions = Toolkit.getDefaultToolkit.getScreenSize
    screenDimensions.getHeight /100 * 3
  }
  private val xSens = sens
  private val ySens = sens
  private val clickThreshold = 0.50
  private var lastEvent : HandState = _

  private var isClickPressed = false

  def processEvent (handEvent : HandState): Unit = {
    if (lastEvent == null) {
      lastEvent = handEvent
      return
    }
    val currentLocation = MouseInfo.getPointerInfo.getLocation
    val dx = _dx(handEvent)
    val dy = _dy(handEvent)

    if (EGConfig.debugMouse)
      println(s"dx $dx\t dy $dy")

    val newX = currentLocation.x + dx
    val newY = currentLocation.y + dy
    robot.mouseMove(newX, newY)
    if (detectClick(handEvent) && !isClickPressed) {
      robot.mousePress(InputEvent.BUTTON1_MASK)
      isClickPressed = true
      println("click")
    } else if (!detectClick(handEvent) && isClickPressed) {
      robot.mouseRelease(InputEvent.BUTTON1_MASK)
      isClickPressed = false
      println("click")
    }
    lastEvent = handEvent
  }

  private def _dx(handEvent: HandState):Int = {
    val currentX = handEvent.orientation(0)
    val adjustedPreviousX = {
      if (lastEvent.orientation(0) < 20 && currentX > 340)
        currentX - 360
      else if (lastEvent.orientation(0) > 340 && currentX < 20)
        lastEvent.orientation(0) - 360
      else
        lastEvent.orientation(0)
    }
    val dx = (currentX - adjustedPreviousX) * xSens * sensitivityControl(handEvent)
    dx.toInt
  }

  private def _dy(handEvent: HandState):Int = {
    val currentY = handEvent.orientation(2)
    val adjustedPreviousY = lastEvent.orientation(2)

    val dy = (currentY - adjustedPreviousY) * ySens * sensitivityControl(handEvent)
    dy.toInt
  }

  private def detectClick (handEvent: HandState):Boolean = handEvent.normalizedFingers(7) > clickThreshold

  private def sensitivityControl (handState: HandState): Double = (1 - handState.normalizedFingers(1)) * (1 - handState.normalizedFingers(0))
}
