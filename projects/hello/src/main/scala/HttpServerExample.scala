import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.model._
import akka.http.scaladsl.server.Directives._
import akka.stream.ActorMaterializer
import scala.io.StdIn

object HttpServerExample {
  def main(args: Array[String]): Unit = {
    implicit val system = ActorSystem("my-system")
    implicit val materializer = ActorMaterializer()
    implicit val executionContext = system.dispatcher

    val route =
      path("hello") {
        get {
          complete(HttpEntity(ContentTypes.`text/html(UTF-8)`, "<h1>Say hello to Akka HTTP!</h1>"))
        }
      }

    val bindingFuture = Http().bindAndHandle(route, "localhost", 8580)

    println(s"Server online at http://localhost:8080/\nPress RETURN to stop...")
    StdIn.readLine() // 사용자 입력을 기다립니다
    bindingFuture
      .flatMap(_.unbind()) // 서버 바인딩을 해제합니다
      .onComplete(_ => system.terminate()) // 시스템을 종료합니다
  }
}