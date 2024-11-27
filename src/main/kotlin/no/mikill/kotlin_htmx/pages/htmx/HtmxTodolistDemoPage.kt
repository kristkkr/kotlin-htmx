package no.mikill.kotlin_htmx.pages.htmx

import io.ktor.server.html.*
import io.ktor.server.routing.RoutingContext
import kotlinx.html.*
import no.mikill.kotlin_htmx.pages.EmptyTemplate
import no.mikill.kotlin_htmx.pages.HtmlElements.htmxTodolistSectionContent
import no.mikill.kotlin_htmx.pages.MainTemplate
import kotlin.time.Duration.Companion.seconds

class HtmxTodolistDemoPage {

    suspend fun renderHtmxTodoListPage(context: RoutingContext) {
        with(context) {
            call.respondHtmlTemplate(MainTemplate(template = EmptyTemplate(), "HTMX TodoList Demo")) {
                headerContent {
                    div {
                        +"Page header"
                    }
                }
                mainSectionTemplate {
                    emptyContentWrapper {
                        htmxTodolistSectionContent(
                            loadDelay = 1.seconds,
                            backendDelay = 1.seconds
                        )
                    }
                }
            }
        }
    }

}