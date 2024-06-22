@file:Suppress("ktlint:standard:filename")

package ingsis.tricolor.snippetrunner.redis.config

import ingsis.tricolor.snippetrunner.redis.consumer.FormatProduct
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.redis.connection.ReactiveRedisConnectionFactory
import org.springframework.data.redis.connection.RedisStandaloneConfiguration
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory
import org.springframework.data.redis.connection.stream.ObjectRecord
import org.springframework.data.redis.stream.StreamReceiver
import java.time.Duration

@Configuration
class ConnectionFactory(
    @Value("\${redis.host}") private val hostName: String,
    @Value("\${redis.port}") private val port: Int,
) {
    @Bean
    fun redisConnectionFactory(): LettuceConnectionFactory {
        return LettuceConnectionFactory(
            RedisStandaloneConfiguration(hostName, port),
        )
    }

    @Bean
    fun streamReceiver(connectionFactory: ReactiveRedisConnectionFactory): StreamReceiver<String, ObjectRecord<String, FormatProduct>> {
        val options =
            StreamReceiver.StreamReceiverOptions.builder()
                .pollTimeout(Duration.ofMillis(10000))
                .targetType(FormatProduct::class.java)
                .build()
        return StreamReceiver.create(connectionFactory, options)
    }
}
