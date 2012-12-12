import akka.actor._

class Ping extends Actor {
  val a = ActorSystem("Ping").actorOf(Props[Pong],name="ping")
  def receive = {
    case "ping" =>
      println("ping")
      a ! "pong"
  }
}

class Pong extends Actor {
  def receive = {
    case "pong" =>
      println("pong")
      sender ! "ping"
  }
}

object main extends App {

  val a = ActorSystem("Ping").actorOf(Props[Ping],name="pong")

  a ! "ping"

}
