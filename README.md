# bonita-embeddedBPM

This repository is home to Bonita EmbeddedBPM which aims to provide suitable resources to ease the integration of Bonita BPN Engine as part of any Java custom application. There are three main modules: the JAR and a sample standalone and web applications using it.

## Architecture challenges

- Get rid of any web server resources
- Transaction Manager integration (Bitronix for now)
- Database integration (h2 for now)
- Platform initialization (DB creation and data initialization)
- License for Subscription editions

## Limitations

- REST API
- DB
- PlatformSetupTool and Custom Application in the same JVM (Spring context)
- Dependency
    * Listing
        + bonita-client-7.6.3.jar
        
httpclient-4.5.2.jar
httpcore-4.4.4.jar
commons-logging-1.2.jar
xstream-1.4.10.jar
xmlpull-1.1.3.1.jar
xpp3_min-1.1.4c.jar
httpmime-4.5.2.jar
commons-codec-1.7.jar
bonita-common-7.6.3.jar
bonita-server-7.6.3.jar
commons-beanutils-1.9.2.jar
commons-collections-3.2.2.jar
jackson-databind-2.9.0.jar
jackson-annotations-2.9.0.jar
jackson-core-2.9.0.jar
platform-setup-7.6.3.jar
logback-classic-1.1.3.jar
logback-core-1.1.3.jar
spring-tx-4.3.7.RELEASE.jar
spring-jdbc-4.3.7.RELEASE.jar
commons-cli-1.2.jar
platform-resources-7.6.3.jar
commons-io-2.5.jar
spring-context-4.3.7.RELEASE.jar
spring-aop-4.3.7.RELEASE.jar
spring-beans-4.3.7.RELEASE.jar
spring-expression-4.3.7.RELEASE.jar
groovy-all-2.4.4.jar
commons-fileupload-1.2.2.jar
spring-core-4.3.7.RELEASE.jar
hibernate-entitymanager-4.3.11.Final.jar
jboss-logging-3.1.3.GA.jar
jboss-logging-annotations-1.2.0.Beta1.jar
dom4j-1.6.1.jar
xml-apis-1.0.b2.jar
hibernate-commons-annotations-4.0.5.Final.jar
hibernate-jpa-2.1-api-1.0.0.Final.jar
commons-lang3-3.3.2.jar
jaxb-xjc-2.2.7.jar
jaxb-core-2.2.7.jar
istack-commons-runtime-2.16.jar
ecj-4.6.1.jar
javassist-3.18.1-GA.jar
ehcache-core-2.4.7.jar
xbean-classloader-3.7.jar
hibernate-core-4.3.11.Final.jar
antlr-2.7.7.jar
jandex-1.1.0.Final.jar
jbcrypt-0.3m.jar
slf4j-api-1.6.1.jar
quartz-2.2.1.jar
c3p0-0.9.1.1.jar
commons-collections4-4.0.jar
btm-2.1.3.jar
jta-1.1.jar
bonecp-0.8.0.RELEASE.jar
guava-15.0.jar
h2-1.3.170.jar

    * Conflicts
        + (short-term) Shading
        + (long-term) Lower number of dependencies

## Package

- Extension of existing PlatformSetupApplication
    * Remove user interactions (H2 confirmation for example)
    * Easy configuration
- Build of standalone package with-all-dependencies or not (see limitations)
- Integration tutorials
