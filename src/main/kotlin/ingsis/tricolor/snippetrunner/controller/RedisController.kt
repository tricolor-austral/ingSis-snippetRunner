package ingsis.tricolor.snippetrunner.controller

import ingsis.tricolor.snippetrunner.redis.consumer.FormatProduct
import ingsis.tricolor.snippetrunner.redis.producer.SnippetFormattedProducer
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("redis")
class RedisController
    @Autowired
    constructor(private val producer: SnippetFormattedProducer) {
        @PostMapping("/format/snippet")
        fun createFormatSnippetEvent(
            @RequestBody snippet: FormatProduct,
        ): String {
            println("request has been received")
            for (i in 0..2000) {
                producer.publishEvent(snippet)
            }
            return "Event to format snippet created"
        }
    }
