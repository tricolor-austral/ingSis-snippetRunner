package ingsis.tricolor.snippetrunner.redis.route

import ingsis.tricolor.snippetrunner.redis.dto.ChangeRulesDto
import ingsis.tricolor.snippetrunner.redis.dto.Snippet
import ingsis.tricolor.snippetrunner.redis.producer.SnippetFormattedProducer
import ingsis.tricolor.snippetrunner.redis.producer.SnippetLintProducer
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("redis")
class RedisController
    @Autowired
    constructor(
        private val formatProducer: SnippetFormattedProducer,
        private val lintProducer: SnippetLintProducer,
    ) {
        @PutMapping("format")
        suspend fun changeAndFormatRules(
            @RequestBody data: ChangeRulesDto,
        ) {
            // TODO: cambiarle las reglas al usuario
            data.snippets.map {
                val snippet = Snippet(data.userId, it.input, data.correlationId)
                formatProducer.publishEvent(snippet)
            }
        }

        @PutMapping("lint")
        suspend fun changeAndLintRules(
            @RequestBody data: ChangeRulesDto,
        ) {
            // TODO: cambiarle las reglas al usuario
            data.snippets.map {
                val snippet = Snippet(data.userId, it.input, data.correlationId)
                lintProducer.publishEvent(snippet)
            }
        }
    }
