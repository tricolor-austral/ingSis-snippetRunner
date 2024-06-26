package ingsis.tricolor.snippetrunner.redis.producer

import org.springframework.data.redis.connection.stream.RecordId
import org.springframework.data.redis.connection.stream.StreamRecords
import org.springframework.data.redis.core.ReactiveRedisTemplate
import reactor.core.publisher.Mono

abstract class RedisStreamProducer(val streamKey: String, val redis: ReactiveRedisTemplate<String, String>) {
    // we use Any as upper bound of Value to make it non-nullable
    inline fun <reified Value : Any> emit(value: Value): Mono<RecordId> {
        val record =
            StreamRecords.newRecord()
                .ofObject(value)
                .withStreamKey(streamKey)

        return redis
            .opsForStream<String, Value>()
            .add(record)
    }
}
