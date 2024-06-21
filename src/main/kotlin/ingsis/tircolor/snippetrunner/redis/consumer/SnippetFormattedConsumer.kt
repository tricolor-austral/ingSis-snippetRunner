package ingsis.tircolor.snippetrunner.redis.consumer

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.data.redis.connection.stream.ObjectRecord
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.data.redis.stream.StreamReceiver
import org.springframework.stereotype.Component
import java.time.Duration

@Component
class SnippetFormattedConsumer @Autowired constructor(
    redis: RedisTemplate<String, String>,
    @Value("\${stream.key}") streamKey: String,
    @Value("\${groups.product}") groupId: String
): RedisStreamConsumer<FormatProduct>(streamKey, groupId, redis) {
    override fun options(): StreamReceiver.StreamReceiverOptions<String, ObjectRecord<String, FormatProduct>> {
        return StreamReceiver.StreamReceiverOptions.builder()
            .pollTimeout(Duration.ofMillis(10000)) // Set poll rate
            .targetType(FormatProduct::class.java) // Set type to de-serialize record
            .build();
    }

    override fun onMessage(record: ObjectRecord<String, FormatProduct>) {
        // What we want to do with the stream
        println("Id: ${record.id}, Value: ${record.value}, Stream: ${record.stream}, Group: ${groupId}")
        // TODO: Llamar a la funcion que formatea los snippets aca
    }
}