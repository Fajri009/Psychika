package com.example.psychika.data.network.response

import com.google.gson.annotations.SerializedName

data class ErrorChatbotResponse(

	@field:SerializedName("error")
	val message: String
)
