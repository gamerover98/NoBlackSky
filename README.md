# NoBlackSky
This Spigot/Paper plugin for Minecraft prevents the black sky glitch. This fix works from Minecraft 1.8
to the latest minecraft version.

This plugin needs [ProtocolLib](https://www.spigotmc.org/resources/protocollib.1997/) plugin to be executed. 
_If ProtocolLib is not available for your server version, try to wait for a release or use the development builds_
.
**Default world** `(under the y < 61 the sky become black)`
![Default World | y < 61](https://i.imgur.com/O81FSZR.png)

**With NoBlackSky**
![NoBlackSky | y < 61](https://i.imgur.com/wO8jF1V.png)

Download it from [Spigot](https://www.spigotmc.org/resources/world-type-changer.25337/)
---
### Compile your own build
To compile the project [Maven](https://maven.apache.org/download.cgi) is required.

- Use this command at the root of the project: `mvn clean package -B`
- The plugin jar file will be placed in:
`deploy/target/NoBlackSky-*.*.jar`

### To contribute to this project

If you want to contribute to this project, you will follow the next things:

- Is strongly recommended to use [IntelliJ IDEA](https://www.jetbrains.com/idea/) as IDE
- Java 8 or later to compile the source
- [Lombok plugin](https://projectlombok.org/download) must be installed in your IDE
- Tries to follow my java code style