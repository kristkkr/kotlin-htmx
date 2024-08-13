package no.mikill.kotlin_htmx.pages

import io.ktor.http.*
import io.ktor.http.content.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import jakarta.validation.constraints.NotEmpty
import jakarta.validation.constraints.Size
import kotlinx.html.*
import kotlinx.html.consumers.filter
import kotlinx.html.stream.appendHTML
import kotlinx.html.stream.createHTML

object HtmlElements {

    fun HtmlBlockTag.selectBox(name: String, linkUrl: String, imageUrl: String) {
        a(href = linkUrl, classes = "box") {
            boostAndPreload()

            img(
                src = imageUrl, alt = "Choose $name"
            )
            p { +name }
        }
    }

    private fun A.boostAndPreload() {
        // Preloading resources
        attributes["preload"] = "mouseover"
        attributes["preload-images"] = true.toString()

        // Boosting
        attributes["hx-boost"] = true.toString()
        attributes["hx-target"] = "#mainContent"
        attributes["hx-select"] = "#mainContent"
        attributes["hx-swap"] = "outerHTML"
    }

    public fun INPUT.setConstraints(annotations: Array<Annotation>) {
        annotations.forEach { annotation ->
            when (annotation) {
                is NotEmpty -> required = true
                is Size -> {
                    minLength = annotation.min.toString()
                    maxLength = annotation.max.toString()
                }
                // Could add Pattern here as well, but purposely left out for demo reasons (we need one that is on the server too)
            }
        }
    }


    suspend fun ApplicationCall.respondHtmlFragment(
        status: HttpStatusCode = HttpStatusCode.OK,
        block: BODY.() -> Unit
    ) {
        val text = buildString {
            append("<!DOCTYPE html>\n")
            appendHTML().filter { if (it.tagName in listOf("html", "body")) SKIP else PASS }.html {
                body {
                    block(this)
                }
            }
        }
        respond(TextContent(text, ContentType.Text.Html.withCharset(Charsets.UTF_8), status))
    }

}

