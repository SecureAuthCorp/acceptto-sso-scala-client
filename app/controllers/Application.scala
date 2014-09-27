package controllers

import play.api.mvc._
import org.pac4j.play.scala._

object Application extends ScalaController {

  def index = Action { request =>
    val newSession = getOrCreateSessionId(request)
    val urlCas = getRedirectAction(request, newSession, "CasClient", "/").getLocation()
    val profile = getUserProfile(request)
    Ok(views.html.index(profile, urlCas)).withSession(newSession)
  }

  def casIndex = RequiresAuthentication("CasClient") { profile =>
    Action { request =>
      Ok(views.html.protectedIndex(profile))
    }
  }
}
