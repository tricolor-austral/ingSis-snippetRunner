package ingsis.tricolor.snippetrunner.redis.consumer

import ingsis.tricolor.snippetrunner.redis.dto.Snippet
import ingsis.tricolor.snippetrunner.service.interfaces.RedisService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.data.redis.connection.stream.ObjectRecord
import org.springframework.data.redis.core.ReactiveRedisTemplate
import org.springframework.data.redis.stream.StreamReceiver
import org.springframework.stereotype.Component
import java.time.Duration

@Component
class SnippetFormattedConsumer
    @Autowired
    constructor(
        redis: ReactiveRedisTemplate<String, String>,
        @Value("\${stream.key.format}") streamKey: String,
        @Value("\${groups.product}") groupId: String,
        private val service: RedisService,
    ) : RedisStreamConsumer<Snippet>(streamKey, groupId, redis) {
        override fun options(): StreamReceiver.StreamReceiverOptions<String, ObjectRecord<String, Snippet>> {
            return StreamReceiver.StreamReceiverOptions.builder()
                .pollTimeout(Duration.ofMillis(100)) // Set poll rate
                .targetType(Snippet::class.java) // Set type to de-serialize record
                .build()
        }

        override fun onMessage(record: ObjectRecord<String, Snippet>) {
            Thread.sleep(100 * 10)
            println("Id: ${record.id}, Value: ${record.value}, Stream: ${record.stream}, Group: $groupId")
            service.formatSnippet(record.value)
        }
    }
