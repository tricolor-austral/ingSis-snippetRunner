package ingsis.tircolor.snippetrunner.redis.producer

import ingsis.tircolor.snippetrunner.redis.consumer.FormatProduct
import jdk.jfr.internal.OldObjectSample.emit
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.data.redis.core.ReactiveRedisTemplate
import org.springframework.stereotype.Component

@Component
class SnippetFormattedProducer  @Autowired constructor(
    @Value("\${stream.key}") val streamKey: String,
    redis: ReactiveRedisTemplate<String, String>
) : RedisStreamProducer(streamKey, redis) {

    suspend fun publishEvent(snippet: FormatProduct) {
        println("Publishing on stream: $streamKey")
        emit(snippet).awaitSingle()
    }
}