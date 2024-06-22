package ingsis.tricolor.snippetrunner.redis.consumer

import ingsis.tricolor.snippetrunner.service.interfaces.RedisService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.data.redis.connection.stream.ObjectRecord
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.data.redis.stream.StreamReceiver
import org.springframework.stereotype.Component
import java.time.Duration

@Component
class SnippetFormattedConsumer
    @Autowired
    constructor(
        redis: RedisTemplate<String, String>,
        @Value("\${stream.key}") streamKey: String,
        @Value("\${groups.product}") groupId: String,
        private val service: RedisService,
    ) : RedisStreamConsumer<FormatProduct>(streamKey, groupId, redis) {
        override fun options(): StreamReceiver.StreamReceiverOptions<String, ObjectRecord<String, FormatProduct>> {
            return StreamReceiver.StreamReceiverOptions.builder()
                .pollTimeout(Duration.ofMillis(10000)) // Set poll rate
                .targetType(FormatProduct::class.java) // Set type to de-serialize record
                .build()
        }

        override fun onMessage(record: ObjectRecord<String, FormatProduct>) {
            println("Id: ${record.id}, Value: ${record.value}, Stream: ${record.stream}, Group: $groupId")
            service.formatSnippet(record.value)
        }
    }
