# HoloCraftAPI
<bold>[![](https://jitpack.io/v/KubawGaming/HoloCraftAPI.svg)](https://jitpack.io/#KubawGaming/HoloCraftAPI)</bold> <strong>Its project version used in gradle/maven</strong>

<br>
An easy-to-use library that allows you to create holograms using packets.
The code was tested on minecraft version 1.20.4. I did not check compatibility with other versions!

## Example of use:

To get started, you need to know a few things. The HologramManager class allows you to create and delete holograms. It also allows you to set some GLOBAL settings that work for all holograms.

You create ONLY ONE instance of the HologramManager class! This is important! However, you do not need to save the HologramManager instance. Once the instance is made, you will be able to get it from anywhere using the built-in static method in HologramManager.

Here is an example:

```java
public class HoloCraftAPI extends JavaPlugin {

    @Override
    public void onEnable() {
        // We are creating HologramManager instance and giving Main class (that extends JavaPlugin) as argument
        new HologramManager(this);

        // After creating the instance you are able to get HologramManager using:
        HologramManager hologramManager = HologramManager.getInstance();
    }

}
```

## Gradle:

```gradle
repositories {
    mavenCentral()
    maven { url 'https://jitpack.io' }
}

dependencies {
    implementation 'com.github.KubawGaming:HoloCraftAPI:VERSION_HERE'
}
```

## Maven:

```html
<repository>
    <id>jitpack.io</id>
    <url>https://jitpack.io</url>
</repository>

<dependency>
    <groupId>com.github.KubawGaming</groupId>
    <artifactId>HoloCraftAPI</artifactId>
    <version>VERSION_HERE</version>
</dependency>
```
