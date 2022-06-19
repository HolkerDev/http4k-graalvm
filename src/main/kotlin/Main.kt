import kotlinx.serialization.Serializable
import org.http4k.core.Body
import org.http4k.core.Method
import org.http4k.core.Response
import org.http4k.core.Status.Companion.OK
import org.http4k.core.with
import org.http4k.routing.bind
import org.http4k.routing.path
import org.http4k.routing.routes
import org.http4k.serverless.ApiGatewayRestFnLoader
import org.http4k.serverless.AwsLambdaRuntime
import org.http4k.serverless.asServer
import org.http4k.format.KotlinxSerialization.auto

@Serializable
data class Person(val name: String)

val http4kApp = routes(
    "/echo/{message:.*}" bind Method.GET to {
        Response(OK).body(
            it.path("message") ?: "(nothing to echo, use /echo/<message>)"
        )
    },
    "/json" bind Method.GET to { req->
        Response(OK).with(Body.auto<Person>().toLens() of Person("my test name for test function"))
    },
    "/" bind Method.GET to { Response(OK).body("ok") }
)

fun main() {
    print("test")
    ApiGatewayRestFnLoader(http4kApp).asServer(AwsLambdaRuntime()).start()
}
