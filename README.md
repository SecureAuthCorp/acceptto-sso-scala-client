## What is this project? ##
This is a Play 2.3 Scala project to test [Acceptto](http://acceptto.com/) CAS service.
 
## Live demo ##
Please find the demo at: **TODO**

## Quick start ##

1. [Install Play 2.3 (Activator)](https://playframework.com/)
1. `git clone https://github.com/acceptto-corp/acceptto-sso-scala-client`
1. `cd acceptto-sso-scala-client`
1. `activator run`
1. Open a browser and navigate to [localhost:9000](http://localhost:9000)

Activator will automatically download all dependencies which may take some time depending on your internet connection.

## Create your own client ##
You can use this sample and change it to meet your needs or you can create your own client from scratch. We use [pac4j](https://github.com/leleuj/pac4j) as CAS client and [play-pac4j](https://github.com/leleuj/play-pac4j) for Play integration.

### Create a new Play project ###

Create a new `play-scala` project:

`activator new`

### Add dependencies ###

Add the following dependencies:

```sbt
libraryDependencies ++= Seq(
  "org.pac4j" % "play-pac4j_scala" % "1.3.0-SNAPSHOT",
  "org.pac4j" % "pac4j-cas" % "1.6.0-SNAPSHOT"
)
```

### Configuration ###

Config CAS client in `onStart` method of `Global` settings class.

```scala
package controllers

import scala.concurrent.Future
import play.api._
import play.api.Play.current
import play.api.mvc._
import org.pac4j.play._
import org.pac4j.core.client._
import org.pac4j.cas.client._
import play.api.mvc.Results._

object Global extends GlobalSettings {

  override def onStart(app: Application) {
    val baseUrl = "http://localhost:9000"
    val casUrl = "https://m2m.acceptto.net/cass"

    val casClient = new CasClient()
    casClient.setCasLoginUrl(casUrl)

    val clients = new Clients(baseUrl + "/callback", casClient)
    Config.setClients(clients)
    // for test purposes : profile timeout = 60 seconds
    // Config.setProfileTimeout(60)
  }
  
}
```

### Controller and Action ###

Authentication controller should inherit from `ScalaController` then you can use: 

- `getOrCreateSessionId` to obtain the corresponding session
- `getRedirectAction` to obtain the redirection action
- `getUserProfile` to obtain user profile if it was available

```scala
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
  
}
```

### View ###

If the user is not authorized by Acceptto he or she will see the `Authenticate with Acceptto CAS` button, otherwise he or she will see the authorized profile information.

```html
@(profile : org.pac4j.core.profile.CommonProfile, urlCas: String)

<div>
@profile match {
    case _: org.pac4j.cas.profile.CasProfile => {
        <p>
            <a href="logout">Logout</a>
        </p>
        @profile
    }
    case _ => {
        <a href="@urlCas">Authenticate with Acceptto CAS</a><br />
    }
}
</div>

```

### Routes ###

The final step is to set your routes.

```
GET     /                           controllers.Application.index()
GET     /callback                   org.pac4j.play.CallbackController.callback()
POST    /callback                   org.pac4j.play.CallbackController.callback()
GET     /casProxyCallback           org.pac4j.play.CallbackController.callback()
POST    /casProxyCallback           org.pac4j.play.CallbackController.callback()
GET     /logout                     org.pac4j.play.CallbackController.logoutAndRedirect()
```
