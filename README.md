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
public class Main extends JavaPlugin {

    @Override
    public void onEnable() {
        // We are creating HologramManager instance and giving Main class (that extends JavaPlugin) as argument
        new HologramManager(this);

        // After creating the instance you are able to get HologramManager using:
        HologramManager hologramManager = HologramManager.getInstance();
    }

}
```

Once we have HologramManager, we can create our holograms.

To create a hologram, we need to specify the type of hologram and the location. There are 3 types of holograms - TEXT_DISPLAY, ITEM_DISPLAY and BLOCK_DISPLAY.

```java
Player player = ...;
HologramManager hologramManager = HologramManager.getInstance();
Hologram hologram = hologramManager.createHologram(HologramType.TEXT_DISPLAY, player.getLocation());
```

Using the Hologram object, you can do basic changes such as changing the visibility of the hologram to players, changing the location of the hologram, updating placeholders instantly, etc.

More specific hologram data - such as text, rotation, etc. - you can find in the Data of individual types of holograms. For example, if we created a hologram with type TEXT_DISPLAY then we will use TextDisplayData. 

```java
hologram.showHologram(player)
    // Data of hologram is repeating data for all types of holograms
    .getData()
        // We want to change the text of the hologram
        // This procedure is only possible for the hologram type TEXT_DISPLAY so we need to enter the TextDisplayData
        .asTextDisplayData()
            .setText("Hologram created by HoloCraftAPI")
    .getHologram() // it's a helpful method to get back from Data to the Hologram class
    .applyChanges();
```

As you can see at the end I used `.applyChanges()`. In order for the changes you make to be approved and shown to players, you must use this method. Use `.applyChanges()` when you make changes to Data or when you change hologram location using `hologram.setLocation(location);`.

## ItemDisplay example with rotate animation 

Let's make a rotating block around the x-axis.

I will use ITEM_DISPLAY instead of BLOCK_DISPLAY because the rotation for BLOCK_DISPLAY does not rotate the block relative to the center.

```java
HologramManager hologramManager = HologramManager.getInstance();
Hologram hologram = hologramManager.createHologram(HologramType.ITEM_DISPLAY, player.getLocation());
AtomicReference<Double> xRotation = new AtomicReference<>(0.0);

hologram
    .getData()
        .asItemDisplayData()
            .setItem(Material.DIAMOND_BLOCK)
    .getHologram()
    // Setting this to true allows every player - even people joining the server - to always see the hologram
    // Method setPublicVisible(boolean) shouldn't be used when .setItem(Material) is not set yet!
    .setPublicVisible(true)
    .applyChanges();

Bukkit.getScheduler().scheduleSyncRepeatingTask(this, () -> {
    hologram
    // Rotation is available for any type of hologram so we don't have to go into ItemDisplayData
    .getData()
        .setRotation(xRotation.get(), 0, 0)
    .getHologram()
    .applyChanges();

    xRotation.set(xRotation.get() + 0.3);
}, 0, 1);
```

### Effect:

![](https://github.com/KubawGaming/HoloCraftAPI/blob/master/src/main/gifs/rotate_animation_block.gif)

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
