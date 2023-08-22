package com.tft.guide.lambda

import com.amazonaws.serverless.exceptions.ContainerInitializationException
import com.amazonaws.serverless.proxy.model.AwsProxyRequest
import com.amazonaws.serverless.proxy.model.AwsProxyResponse
import com.amazonaws.serverless.proxy.spring.SpringBootLambdaContainerHandler
import com.amazonaws.serverless.proxy.spring.SpringBootProxyHandlerBuilder
import com.amazonaws.services.lambda.runtime.Context
import com.amazonaws.services.lambda.runtime.RequestStreamHandler
import com.tft.guide.TFTGamingWebApplication
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader
import java.io.OutputStream
import java.io.StringReader
import javax.imageio.stream.MemoryCacheImageInputStream


class StreamLambdaHandler : RequestStreamHandler {
    @Throws(IOException::class)
    override fun handleRequest(input: InputStream, output: OutputStream, context: Context) {

        val requestStr = InputStreamReader(input).readText()
        println(requestStr)
        handler.proxyStream(requestStr.byteInputStream(), output, context)
    }

    companion object {
        private lateinit var handler: SpringBootLambdaContainerHandler<AwsProxyRequest, AwsProxyResponse>

        init {
            try {
                //실행속도 개선을 위해 async 방식으로 Init
                handler = SpringBootProxyHandlerBuilder<AwsProxyRequest>()
                    .defaultProxy()
                    .asyncInit()
                    .springBootApplication(TFTGamingWebApplication::class.java)
                    .buildAndInitialize()
            } catch (e: ContainerInitializationException) {
                throw RuntimeException("Spring Boot Application 실행 실패", e)
            }
        }
    }
}