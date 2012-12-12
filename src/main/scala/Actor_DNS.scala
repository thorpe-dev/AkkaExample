import akka.actor._
import java.net.InetAddress
class Result(val site:String,val x:Array[InetAddress])

class DNSLookup(val acc:ActorRef) extends Actor {
  def receive = {
    case x:String =>
      acc ! this.resolveURL(x)
  }

  def resolveURL(x:String):Result = {
    val addr = InetAddress.getAllByName(x)
    new Result(x,addr)
  }
}

class Accumulator(val length:Int, val system:ActorSystem) extends Actor {
  var results:List[Result] = Nil
  var count = 0

  def receive = {
    case result:Result =>
      results = result :: results
      count += 1
      if (count == length) system.shutdown
    case _ => throw new Exception
  }

  override def postStop = {
    for(res <- results) println(res.site ++ "\t" ++ this.inetArrayToString(res.x))
  }

  def inetArrayToString(arr:Array[InetAddress]):String = {
    val str = new StringBuilder;
    for(x <- arr)
      str ++= x.toString ++ "\t"
    str.toString
  }
}

object FastDNS extends App {

  val lines = scala.io.Source.fromFile("input.txt").mkString.split("\n")
  val system = ActorSystem("FastDNS")

  val accumulator = system.actorOf(Props(new Accumulator(lines.length,system)), name = "accumulator")

  val workers = List.fill(200)(system.actorOf(Props(new DNSLookup(accumulator))))


  val workersIterator = (for(s <- Stream.continually(); worker <- workers) yield worker).iterator

  for (x <- lines) workersIterator.next ! x
}
