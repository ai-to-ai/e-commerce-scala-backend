import com.nichoshop.controllers._

import com.nichoshop.swaggers.{NichoSwagger, ResourcesApp}
import com.nichoshop.{WebApp, GlobalContext => GC}
import org.scalatra._
import org.slf4j.LoggerFactory

import javax.servlet.ServletContext

class ScalatraBootstrap extends LifeCycle {

  private val log = LoggerFactory.getLogger(classOf[ScalatraBootstrap])

  implicit val swagger = new NichoSwagger

  /** init Akka system */
  //  Akka

  override def init(context: ServletContext): Unit = {
    log.info("Context has been initialized. Initializing WebApp.....")
    GC.webApp = new WebApp
    log.info("WebApp has been initialized.")
    GC.applicationContext = context

    context.initParameters("org.scalatra.environment") = "development"
    context.initParameters("org.scalatra.cors.allowCredentials") = "true"
    // context.initParameters("org.scalatra.cors.allowedOrigins") = "*"
    context.initParameters("org.scalatra.cors.enable") = "true"
    context.initParameters("org.scalatra.cors.allowedHeaders") = "Content-Type,Set-Cookie,Cookie, Host, X-Forwarded-For, Accept-Charset, If-Modified-Since, Accept-Language, Accept-Encoding, X-Requested-With, Authorization,Accept, Access-Control-Allow-Origin, Origin"
    context.initParameters("org.scalatra.cors.allowedOrigins") = "https://nichoshop.com,https://www.nichoshop.com, http://localhost:3000"

    context.mount(new SignUpController, "/api/v1/signup/*", "signup")
    context.mount(new LoginController, "/api/v1/login/*", "login")
    context.mount(new AddressController,"/api/v1/address/*", "address")
    context.mount(new AccountController,"/api/v1/account/*", "account")

    context.mount(new MessageController,"/api/v1/message/*", "message")
    context.mount(new BidController,"/api/v1/bid/*", "bid")
    context.mount(new CartController,"/api/v1/cart/*", "cart")
    context.mount(new ItemController,"/api/v1/item/*", "item")
    context.mount(new FeedBackController,"/api/v1/feedback/*", "feedback")
    context.mount(new OrderController,"/api/v1/order/*", "order")
    context.mount(new CreditCardController,"/api/v1/credit-card/*", "credit-card")
    context.mount(new CategoryController,"/api/v1/category/*", "category")

    // Admin
    context.mount(new admin.LoginController, "/api/v1/admin/login/*", "admin/login")
    context.mount(new admin.LoginTempController, "/api/v1/admin/login-temp/*", "admin/login-temp")
    context.mount(new admin.CategoryController, "/api/v1/admin/category/*", "admin/category")
    context.mount(new admin.CSController, "/api/v1/admin/customer-support/*", "admin/customer-support")

    context.mount(new ResourcesApp, "/api/v1/api-docs")
  }

  override def destroy(context: ServletContext): Unit = {
    log.info("Context is about to be destroyed. Shutting down WebApp.")
    GC.webApp.shutdown()

    /** shutdown Akka system */
    //    Akka.system.shutdown()
  }
}
