package ingsis.tircolor.snippetrunner.redis.route

import ingsis.tircolor.snippetrunner.redis.consumer.FormatProduct
import ingsis.tircolor.snippetrunner.redis.producer.SnippetFormattedProducer
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController("/stream")
class StreamController
    @Autowired
    constructor(
        private val producer: SnippetFormattedProducer,
    ) {
        @PostMapping("/format/snippet")
        fun createFormatSnippetEvent(
            @RequestBody snippet: FormatProduct,
        ): String {
            producer.publishEvent(snippet)
            return "Event to format snippet created"
        }
    }
