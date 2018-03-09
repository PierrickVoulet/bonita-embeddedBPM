# bonita-embeddedBPM

This repository is home to Bonita EmbeddedBPM which aims to provide suitable resources to ease the integration of Bonita BPN Engine as part of any Java custom application. There are three main modules: the JAR and a sample standalone and web applications using it.

## Architecture challenges

- [OK] Dependency to web application specific server resources
    * Use the "deploy" bundle
    * No dependencies
- [OK] Transaction Manager integration
    * Implementation done with Bitronix
    * Same limitation as any Bonita
    * Need to provide Spring context specific beans
- [OK] Database integration
    * Implementation done with H2
    * Same limitation as any Bonita
    * Need to add the right JDBC
- [OK] Platform initialization (DB creation and data initialization)
    * Leverage the Platform Setup Tool to create the DB
    * Resources are provided in any bundle
- [OK] License for Subscription editions
    * This requires to generate the key before the firt start
    * This can be done as usual with the resources provided in any bundle
    * This repository is based on Community edition

## Package

- Extension of existing PlatformSetupApplication
    * Remove user interactions (H2 confirmation for example)
    * Easy configuration
- Build of standalone package with-all-dependencies or not (see limitations)
- Integration tutorials

## Limitations

- REST API

The REST API layer of Bonita is linked to the Bonita Portal Web Application. In the case of EmbeddedBPM, we do not always have a Web Application Server hosting. An extra analysis is required to see if it is possible to have this layer embedded and what would be the limitations.

- Transaction Management

There are theorically no limitations for the TM you can use. It is recommended to use Bitronix as it is integrated in the Tomcat bundle though.

- DB

The limitations are the usual ones, nothing specific to EmbeddedBPM. By default we do use H2 because it does not require any DB installation.

- PlatformSetupTool and Custom Application in the same JVM (Spring context)

## Issues

### Dependencies
* Conflict management
    + (short-term) Shading
    + (long-term) Lower number of dependencies
* Listing (session in progress)
    + bonita-client-7.6.3.jar
    + httpclient-4.5.2.jar
    + httpcore-4.4.4.jar
    + commons-logging-1.2.jar
    + xstream-1.4.10.jar
    + xmlpull-1.1.3.1.jar
    + xpp3_min-1.1.4c.jar
    + httpmime-4.5.2.jar
    + commons-codec-1.7.jar
    + bonita-common-7.6.3.jar
    + bonita-server-7.6.3.jar
    + commons-beanutils-1.9.2.jar
    + commons-collections-3.2.2.jar
    + jackson-databind-2.9.0.jar
    + jackson-annotations-2.9.0.jar
    + jackson-core-2.9.0.jar
    + platform-setup-7.6.3.jar
    + logback-classic-1.1.3.jar
    + logback-core-1.1.3.jar
    + spring-tx-4.3.7.RELEASE.jar
    + spring-jdbc-4.3.7.RELEASE.jar
    + commons-cli-1.2.jar
    + platform-resources-7.6.3.jar
    + commons-io-2.5.jar
    + spring-context-4.3.7.RELEASE.jar
    + spring-aop-4.3.7.RELEASE.jar
    + spring-beans-4.3.7.RELEASE.jar
    + spring-expression-4.3.7.RELEASE.jar
    + groovy-all-2.4.4.jar
    + commons-fileupload-1.2.2.jar
    + spring-core-4.3.7.RELEASE.jar
    + hibernate-entitymanager-4.3.11.Final.jar
    + jboss-logging-3.1.3.GA.jar
    + jboss-logging-annotations-1.2.0.Beta1.jar
    + dom4j-1.6.1.jar
    + xml-apis-1.0.b2.jar
    + hibernate-commons-annotations-4.0.5.Final.jar
    + hibernate-jpa-2.1-api-1.0.0.Final.jar
    + commons-lang3-3.3.2.jar
    + jaxb-xjc-2.2.7.jar
    + jaxb-core-2.2.7.jar
    + istack-commons-runtime-2.16.jar
    + ecj-4.6.1.jar
    + javassist-3.18.1-GA.jar
    + ehcache-core-2.4.7.jar
    + xbean-classloader-3.7.jar
    + hibernate-core-4.3.11.Final.jar
    + antlr-2.7.7.jar
    + jandex-1.1.0.Final.jar
    + jbcrypt-0.3m.jar
    + slf4j-api-1.6.1.jar
    + quartz-2.2.1.jar
    + c3p0-0.9.1.1.jar
    + commons-collections4-4.0.jar
    + btm-2.1.3.jar
    + jta-1.1.jar
    + bonecp-0.8.0.RELEASE.jar
    + guava-15.0.jar
    + h2-1.3.170.jar
