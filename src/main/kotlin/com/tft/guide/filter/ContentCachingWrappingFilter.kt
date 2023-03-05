package com.tft.guide.filter

import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter
import org.springframework.web.util.ContentCachingRequestWrapper
import org.springframework.web.util.ContentCachingResponseWrapper
import javax.servlet.FilterChain
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Component
class ContentCachingWrappingFilter : OncePerRequestFilter() {
    // OncePerRequestFilter 는 모든 서블릿 요청에 일관된 필터를 적용하기 위해서 사용한다.

    override fun doFilterInternal(
            request: HttpServletRequest,
            response: HttpServletResponse,
            filterChain: FilterChain
    ) {
        val wrapRequest = ContentCachingRequestWrapper(request) // 커스텀으로 생성한 Wrapper
        val wrapResponse = ContentCachingResponseWrapper(response)
        filterChain.doFilter(wrapRequest, wrapResponse)
        wrapResponse.copyBodyToResponse() // 이 부분이 핵심이다. 이를 통해 response 를 다시 읽을 수 있다.
    }
}