package com.tft.guide.advice


import com.tft.guide.CommonResponse
import net.gpedro.integrations.slack.SlackApi
import net.gpedro.integrations.slack.SlackAttachment
import net.gpedro.integrations.slack.SlackField
import net.gpedro.integrations.slack.SlackMessage
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.util.ContentCachingRequestWrapper
import org.springframework.web.util.WebUtils
import java.util.*
import javax.servlet.http.HttpServletRequest


@ControllerAdvice
class ErrorDetectAdvisor(
    private val slackApi: SlackApi,
) {

    @ExceptionHandler(Exception::class)
    fun handleException(req: HttpServletRequest, e: Exception): ResponseEntity<Any> {
        sendSlackMessage(req, e)
        return ResponseEntity(
            CommonResponse.failOf(null),
            HttpStatus.INTERNAL_SERVER_ERROR
        )
    }


    private fun sendSlackMessage(req: HttpServletRequest, e: Exception) {
        val slackAttachment = SlackAttachment()
        slackAttachment.setFallback("Error")
        slackAttachment.setColor("danger")
        slackAttachment.setTitle("Error Detect From tft-gaming-web")
        slackAttachment.setTitleLink(req.contextPath)
        slackAttachment.setText(e.stackTraceToString())
        slackAttachment.setColor("danger")
        slackAttachment.setFields(
            listOfNotNull(
                SlackField().setTitle("Request URL").setValue(req.requestURL.toString()),
                SlackField().setTitle("Request Method").setValue(req.method),
                req.let {
                    if (it is ContentCachingRequestWrapper) {
                        SlackField().setTitle("Request Body").setValue(getRequestBody(it))
                    } else {
                        null
                    }
                },

                SlackField().setTitle("Request Time").setValue(Date().toString()),
                SlackField().setTitle("Request IP").setValue(req.remoteAddr),
                SlackField().setTitle("Request User-Agent").setValue(req.getHeader("User-Agent")),
            )
        )

        val slackMessage = SlackMessage()
        slackMessage.setAttachments(Collections.singletonList(slackAttachment))
        slackMessage.setIcon(":ghost:")
        slackMessage.setText("Error Detect")

        slackApi.call(slackMessage)
    }

    private fun getRequestBody(request: ContentCachingRequestWrapper): String? {
        val content = request.contentAsByteArray

        return content.takeIf { it.isNotEmpty() }
            ?.let {
                String(it, Charsets.UTF_8)
            }
    }

}