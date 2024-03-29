package tda.core.apirest.http.routes

import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import de.heikoseeberger.akkahttpcirce.FailFastCirceSupport
import io.circe.generic.auto._
import io.circe.syntax._
import tda.core.apirest.domain.UserProfileUpdate
import tda.core.apirest.domain.profiles.UserProfileService
import tda.core.apirest.session.SessionController
import tda.core.apirest.utils.SecurityDirectives

import scala.concurrent.ExecutionContext

class ProfileRoute(
                    secretKey: String,
                    sessionStorage: SessionController,
                    usersService: UserProfileService
)(implicit executionContext: ExecutionContext)
    extends FailFastCirceSupport {

  import SecurityDirectives._
  import StatusCodes._
  import usersService._

  val route: Route = pathPrefix("profiles") {
    pathEndOrSingleSlash {
      get {
        complete(getProfiles().map(_.asJson))
      }
    } ~
    pathPrefix("me") {
      pathEndOrSingleSlash {
        authenticate(secretKey) { userId =>
          get {
            complete(getProfile(userId))
          } ~
          post {
            entity(as[UserProfileUpdate]) { userUpdate =>
              complete(updateProfile(userId, userUpdate).map(_.asJson))
            }
          }
        }
      }
    } ~
    pathPrefix(Segment) { id =>
      pathEndOrSingleSlash {
        get {
          complete(getProfile(id).map {
            case Some(profile) =>
              OK -> profile.asJson
            case None =>
              BadRequest -> None.asJson
          })
        } ~
        post {
          entity(as[UserProfileUpdate]) { userUpdate =>
            complete(updateProfile(id, userUpdate).map {
              case Some(profile) =>
                OK -> profile.asJson
              case None =>
                BadRequest -> None.asJson
            })
          }
        }
      }
    }
  }

}
