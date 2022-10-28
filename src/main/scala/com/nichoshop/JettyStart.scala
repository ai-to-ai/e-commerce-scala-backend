package com.nichoshop


import org.eclipse.jetty.server.Server
import org.eclipse.jetty.servlet.DefaultServlet
import org.eclipse.jetty.webapp.WebAppContext
import org.scalatra.servlet.ScalatraListener


/**
 * Created by Evgeny Zhoga on 02.06.15.
 */
object JettyStart {
  def main(args: Array[String]): Unit = {
    val port = if (System.getenv("PORT") != null) System.getenv("PORT").toInt else 9090

    val server = new Server(port)
    
    val context = new WebAppContext()
    context setContextPath "/"
    if (!Environment.isProduction) context.setResourceBase("./src/main/resources")
    else context.setResourceBase("/home/admin/domains/stage-api.nichoshop.com/public_html")


    context.addEventListener(new ScalatraListener)
    context.addServlet(classOf[DefaultServlet], "/")

    server.setHandler(context)

    server.start
    server.join
  }
}
