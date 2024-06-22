package ingsis.tricolor.snippetrunner.redis.producer

import ingsis.tricolor.snippetrunner.redis.consumer.FormatProduct
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.stereotype.Component

@Component
class SnippetFormattedProducer
    @Autowired
    constructor(
        @Value("\${stream.key}") streamKey: String,
        redis: RedisTemplate<String, String>,
    ) : RedisStreamProducer(streamKey, redis) {
        fun publishEvent(snippet: FormatProduct) {
            println("Publishing on stream: $streamKey")
            emit(snippet)
        }
    }
