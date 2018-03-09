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
- Dependency conflicts
    * (short-term) Shading
    * (long-term) Lower number of dependencies

## Package

- Extension of existing PlatformSetupApplication
    * Remove user interactions (H2 confirmation for example)
    * Easy configuration
- Build of standalone package with-all-dependencies or not (see limitations)
- Integration tutorials
