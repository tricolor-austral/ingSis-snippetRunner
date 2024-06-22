package ingsis.tricolor.snippetrunner.redis.consumer

import ingsis.tricolor.snippetrunner.service.interfaces.RedisService
import jakarta.annotation.PostConstruct
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.data.redis.connection.stream.Consumer
import org.springframework.data.redis.connection.stream.ObjectRecord
import org.springframework.data.redis.connection.stream.ReadOffset
import org.springframework.data.redis.connection.stream.StreamOffset
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
        private val streamReceiver: StreamReceiver<String, ObjectRecord<String, FormatProduct>>,
    ) : RedisStreamConsumer<FormatProduct>(streamKey, groupId, redis) {
        override fun options(): StreamReceiver.StreamReceiverOptions<String, ObjectRecord<String, FormatProduct>> {
            return StreamReceiver.StreamReceiverOptions.builder()
                .pollTimeout(Duration.ofMillis(10000)) // Set poll rate
                .targetType(FormatProduct::class.java) // Set type to de-serialize record
                .build()
        }

        @PostConstruct
        fun startConsuming() {
            streamReceiver.receive(
                Consumer.from(groupId, "consumer-1"),
                StreamOffset.create(streamKey, ReadOffset.lastConsumed()),
            )
                .doOnNext { record -> onMessage(record) }
                .subscribe()
        }

        override fun onMessage(record: ObjectRecord<String, FormatProduct>) {
            println("Id: ${record.id}, Value: ${record.value}, Stream: ${record.stream}, Group: $groupId")
            service.formatSnippet(record.value)
        }
    }
