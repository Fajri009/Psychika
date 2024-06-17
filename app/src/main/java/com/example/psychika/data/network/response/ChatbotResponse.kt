package com.example.psychika.data.network.response

import com.google.gson.annotations.SerializedName

data class ChatbotResponse(

	@field:SerializedName("done_reason")
	val doneReason: String,

	@field:SerializedName("prompt_eval_duration")
	val promptEvalDuration: Int,

	@field:SerializedName("load_duration")
	val loadDuration: Int,

	@field:SerializedName("total_duration")
	val totalDuration: Long,

	@field:SerializedName("prompt_eval_count")
	val promptEvalCount: Int,

	@field:SerializedName("eval_count")
	val evalCount: Int,

	@field:SerializedName("eval_duration")
	val evalDuration: Long,

	@field:SerializedName("created_at")
	val createdAt: String,

	@field:SerializedName("model")
	val model: String,

	@field:SerializedName("message")
	val message: Message,

	@field:SerializedName("done")
	val done: Boolean
)

data class Message(

	@field:SerializedName("role")
	val role: String,

	@field:SerializedName("content")
	val content: String
)
