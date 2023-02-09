package tda.core.apirest.session

import tda.core.apirest.domain.UserId

import scala.concurrent.Future

object sessions{
  var sessions_1:Array[SessionParams] = Array()
  /** Este método sólo debe estar accesible en modo depuración */
  def getSessions(): Array[SessionParams] ={
    sessions_1
  }

  /**
   * TODO: Falta verificación de session ya abierta.
   * @param s
   */
  def addSession(s:SessionParams): Unit ={
    sessions_1 = sessions_1 :+ s

  }

  /**
   * TODO: Falta verificación de session ya cerrada por exceder el tiempo de uso.
   * @param s
   */
  def removeSession(s:SessionParams):Unit ={
    sessions_1.dropWhile(_ == s)

  }
}

case class SessionParams(UserId: String, AuthToken: String) {}

sealed trait SessionControllerInterface {

  def addSession(idUsuario:UserId, token:String)

  def checkSession(token: String)

  def removeSession(idUsuario:UserId)

}

class SessionController() extends SessionControllerInterface {


  override def addSession(idUsuario: UserId, token: String): Unit = {
    sessions.addSession(SessionParams(idUsuario,token))
  }

  override def checkSession(token: String): Unit = ???

  override def removeSession(idUsuario: UserId): Unit = ???

  def listSessions(): Unit ={
    sessions.getSessions().foreach( a => println("Session: "+a.UserId+" "+a.AuthToken))
  }

  def getProfiles(): Seq[SessionParams] = sessions.getSessions()


  object schedule{
    println("Se ha accedido al método schedule")
  }
}




