/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package dev.kordex.extra.web.server

import dev.kordex.extra.web.config.WebServerConfig
import dev.kordex.extra.web.routes.Verb
import dev.kordex.extra.web.server.routes.api
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.http.content.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.websocket.*

public fun WebServer.configureRouting(app: Application, config: WebServerConfig) {
	app.routing {
		// TODO: API Routing
		// TODO: Static files

		if (config.devMode) {
			get("/") {
				call.respondRedirect("http://localhost:5173")
			}
		} else {
			singlePageApplication {
				useResources = true
				filesPath = "dev/kordex/extra/web/frontend"
			}
		}

		authenticate("oauth-discord") {
			get("/auth") {
				// Redirect is apparently automatic
			}

			get("/auth/callback") {
				val principal: OAuthAccessTokenResponse.OAuth2? = call.principal()

				// TODO: Frontend work, figure out the client-side, handle Discord API stuff, etc

				principal?.let { p ->
					p.state?.let { state ->
						call.respondRedirect(
							"/#auth/callback?state=$state&token=${p.accessToken}"
						)

						return@get
					}
				}

				call.respondRedirect("/#auth/failed")
			}
		}

		route("/api/p/{extension}/{path...}") {
			// TODO: Pages
		}

		route("/api/e/{path...}") {
			delete {
				registries.routes.handle(Verb.DELETE, this)
			}

			get {
				registries.routes.handle(Verb.GET, this)
			}

			head {
				registries.routes.handle(Verb.HEAD, this)
			}

			options {
				registries.routes.handle(Verb.OPTIONS, this)
			}

			patch {
				registries.routes.handle(Verb.PATCH, this)
			}

			post {
				registries.routes.handle(Verb.POST, this)
			}

			put {
				registries.routes.handle(Verb.PUT, this)
			}
		}

		route("/ws/e/{path...}") {
			webSocket {
				registries.websockets.handle(this)
			}
		}

		// Bundled routes defined elsewhere

		api(config)
	}
}
