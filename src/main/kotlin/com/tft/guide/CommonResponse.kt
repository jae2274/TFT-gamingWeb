package com.tft.guide


data class CommonResponse<out T>(
        val success: Boolean,
        val message: String,
        val data: T? = null,
) {
    companion object {
        fun <T> successOf(
                data: T? = null,
                message: String = "SUCCESS",
        ): CommonResponse<T> {
            return CommonResponse(true, message, data)
        }

        fun <T> failOf(
                data: T? = null,
                message: String = "FAIL",
        ): CommonResponse<T> {
            return CommonResponse(false, message, data)
        }
    }
}