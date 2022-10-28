Backend part for nichoshop e-marketplace web site
http://nichoshop.com

Requirements
===
- sbt 1.3.3(https://www.scala-sbt.org/)
- Scala 2.12.1(https://www.scala-lang.org/)
- Mysql 5.6+

Tech stack
===
- Scalarta - micro web framework(https://scalatra.org/)
- Scalarta Auth - Authentication(https://scalatra.org/guides/2.4/http/authentication.html)
- ORM database(https://scala-slick.org/)
- Avro schema(class generation from schema, https://avro.apache.org/)
- Embedded servlet container(Jetty, https://www.eclipse.org/jetty/)
- Embedded Memcached
- Akka(https://akka.io/)
- Twilio integration(https://www.twilio.com/)
- Swagger(https://swagger.io/)
- SMTP (https://docs.aws.amazon.com/ses/latest/dg/send-using-smtp-programmatically.html)

Database
===
- Mysql(https://www.mysql.com/)

Migrations
====
<!-- Migrate database using `sbt flywayMigrate` or clean it using `sbt flywayClean`
https://github.com/flyway/flyway-sbt -->

Run
===
- general `sbt jetty:start jetty:join`
- hotreloading `sbt ~jetty:start ~jetty:join`

Security
===
- close port 11211 or use login/pass

DUO
===
- https://duo.com/docs/dag-linux
- https://duo.com/docs/duoweb
- https://duo.com/docs/duoweb#detailed-sdk-workflow
- https://github.com/duosecurity/duo_universal_java/blob/main/duo-example/src/main/java/com/duosecurity/controller/LoginController.java
- 

Development process
===
- new feature, bug need to be implemented in a separate branch
- we need to update Postman collection with new endpoint and payload example

Problems:
- https://cavorite.com/labs/ is not available anymore, we need to use another plugin
  https://github.com/cavorite/sbt-avro