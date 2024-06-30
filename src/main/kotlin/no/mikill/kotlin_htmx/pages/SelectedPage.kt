package no.mikill.kotlin_htmx.pages

import io.ktor.server.application.*
import io.ktor.util.pipeline.*
import kotlinx.html.id
import kotlinx.html.section
import no.mikill.kotlin_htmx.items
import no.mikill.kotlin_htmx.pages.HtmlElements.selectBox
import no.mikill.kotlin_htmx.pages.HtmlElements.selectedBox


class SelectedPage {
    suspend fun renderPage(context: PipelineContext<Unit, ApplicationCall>) {
        with(context) {
            val selected = items.single { it.name.equals(call.parameters["itemName"], ignoreCase = true) }
            respondFullPage {
                selectedBox(
                    "${selected.name} - Yes and No below has not been implemented and will generate an error",
                    selected.image,
                    true
                )
                section {
                    id = "choices"
                    attributes["hx-ext"] = "preload"

                    selectBox(
                        name = "Yes",
                        linkUrl = "Yes",
                        imageUrl = "https://image.com/something"
                    )
                    selectBox(
                        name = "No",
                        linkUrl = "No",
                        imageUrl = "https://image.com/something"
                    )
                }
            }
        }
    }
}