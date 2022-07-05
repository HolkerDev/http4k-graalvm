import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import mu.KotlinLogging
import org.http4k.core.Method
import org.http4k.core.Response
import org.http4k.core.Status.Companion.OK
import org.http4k.routing.bind
import org.http4k.routing.path
import org.http4k.routing.routes
import org.http4k.serverless.ApiGatewayRestFnLoader
import org.http4k.serverless.AwsLambdaRuntime
import org.http4k.serverless.asServer
import software.amazon.awssdk.services.dynamodb.DynamoDbClient
import software.amazon.awssdk.services.dynamodb.model.AttributeValue
import software.amazon.awssdk.services.dynamodb.model.PutItemResponse

@Serializable
data class Person(val name: String)

private val logger = KotlinLogging.logger {}

val http4kApp = routes(
    "/echo/{message:.*}" bind Method.GET to {
        Response(OK).body(
            it.path("message") ?: "(nothing to echo, use /echo/<message>)"
        )
    },
    "/json" bind Method.GET to { req ->
        Response(OK).body(Json.encodeToString(Person.serializer(), Person("my test name for test function")))
    },
    "/json-post" bind Method.POST to { req ->
        val body = Json.decodeFromString(Person.serializer(), req.bodyString())
        val dynamoClient = DynamoDbClient.create()
        val s: PutItemResponse = dynamoClient.putItem {
            it.tableName("test-http4k")
            it.item(mapOf("name" to AttributeValue.fromS(body.name)))
        }
        logger.info { "Test LOGGING!" }
        Response(OK).body(Json.encodeToString(Person.serializer(), body))
    },
    "/" bind Method.GET to { Response(OK).body("ok") }
)

fun main() {
    print("test")
    ApiGatewayRestFnLoader(http4kApp).asServer(AwsLambdaRuntime()).start()
}