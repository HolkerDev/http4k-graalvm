import org.http4k.core.Method
import org.http4k.core.Response
import org.http4k.core.Status.Companion.OK
import org.http4k.routing.bind
import org.http4k.routing.path
import org.http4k.routing.routes
import org.http4k.serverless.ApiGatewayRestFnLoader
import org.http4k.serverless.AwsLambdaRuntime
import org.http4k.serverless.asServer

val http4kApp = routes(
    "/echo/{message:.*}" bind Method.GET to {
        Response(OK).body(
            it.path("message") ?: "(nothing to echo, use /echo/<message>)"
        )
    },
    "/" bind Method.GET to { Response(OK).body("ok") }
)

fun main() {
    print("test")
    ApiGatewayRestFnLoader(http4kApp).asServer(AwsLambdaRuntime()).start()
}
