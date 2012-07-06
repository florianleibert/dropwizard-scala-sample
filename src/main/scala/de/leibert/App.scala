package de.leibert

import com.yammer.dropwizard.ScalaService
import com.yammer.dropwizard.config.{Configuration, Environment}
import org.codehaus.jackson.annotate.JsonProperty
import java.util.concurrent.atomic.AtomicLong
import javax.ws.rs.{Path, Produces, GET, QueryParam}
import com.yammer.metrics.annotation.Timed
import org.hibernate.validator.constraints.NotEmpty

object App extends ScalaService[ExampleConfiguration]("example") {
  def initialize(configuration: ExampleConfiguration, environment: Environment) {
    environment.addResource(new ExampleResource(configuration.defaultName, configuration.template))
  }
}

class ExampleConfiguration extends Configuration {
  @NotEmpty
  @JsonProperty
  val defaultName: String = "Hans"

  @org.hibernate.validator.constraints.NotEmpty
  @JsonProperty
  val template: String = "Hello %s"
}

@Path("/hello-world")
@Produces(Array("application/json"))
class ExampleResource(val defaultName: String, val template: String) {
  val counter = new AtomicLong(0)

  @GET
  @Timed
  def sayHello(@QueryParam("name") name: Option[String]): String = {
    counter.incrementAndGet()
    return String.format(template, name.getOrElse(defaultName))
  }

}