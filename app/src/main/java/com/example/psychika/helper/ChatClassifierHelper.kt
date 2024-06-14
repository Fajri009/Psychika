package com.example.psychika.helper

import android.content.Context
import android.util.Log
import com.example.psychika.R
import org.tensorflow.lite.support.label.Category
import org.tensorflow.lite.task.core.BaseOptions
import org.tensorflow.lite.task.text.nlclassifier.BertNLClassifier
import java.lang.IllegalStateException
import java.util.concurrent.ScheduledThreadPoolExecutor

class ChatClassifierHelper(
    val modelName: String = "quantized_model.tflite",
    val context: Context,
    val classifierListener: ClassifierListener?
) {
    private var chatClassifier: BertNLClassifier? = null
    private lateinit var executor: ScheduledThreadPoolExecutor

    init {
        setupBertNLClassifier()
    }

    private fun setupBertNLClassifier() {
        val optionBuilder = BertNLClassifier.BertNLClassifierOptions.builder()

        val baseOptionsBuilder = BaseOptions.builder()
            .setNumThreads(4)
            .build()
        optionBuilder.setBaseOptions(baseOptionsBuilder)
        try {
            chatClassifier = BertNLClassifier.createFromFileAndOptions(
                context,
                modelName,
                optionBuilder.build()
            )
        } catch (e: IllegalStateException) {
            classifierListener?.error(context.getString(R.string.text_classifier_error))
            Log.e(TAG, e.message.toString())
        }
    }

    fun classifyChat(chatText: String) {
        if (chatClassifier == null) {
            setupBertNLClassifier()
        }

        if (!::executor.isInitialized) {
            executor = ScheduledThreadPoolExecutor(1)
        }

        executor.execute {
            try {
                val results = chatClassifier!!.classify(chatText)
                classifierListener?.onResults(results)
            } catch (e: Exception) {
                classifierListener?.error(context.getString(R.string.text_classifier_error))
                Log.e(TAG, e.message.toString())
            }
        }
    }

    interface ClassifierListener {
        fun onResults(
            results: List<Category>?
        )
        fun error(error: String)
    }

    companion object {
        private const val TAG = "ChatClassifierHelper"
    }
}