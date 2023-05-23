import me.shedaniel.unifiedpublishing.UnifiedPublishingExtension

val modId: String = property("archives_base_name").toString()
val minecraftVersion: String = property("minecraft_version").toString()
val kotlinVersion: String = property("kotlin_version").toString()
val kotlinFabricVersion: String = property("kotlin_fabric_version").toString()
val architecturyVersion: String = property("architectury_version").toString()
val fabricLoaderVersion: String = property("fabric_loader_version").toString()
val fabricApiVersion: String = property("fabric_api_version").toString()
val kubejsVersion: String = property("kubejs_version").toString()
val craftTweakerVersion: String = property("crafttweaker_version").toString()
val playerskillsVersion: String = property("playerskills_version").toString()

configure<UnifiedPublishingExtension> {
  project {
    relations {
      depends {
        curseforge.set("fabric-api")
        modrinth.set("fabric-api")
      }
    }
  }
}

dependencies {
  modImplementation("net.fabricmc:fabric-loader:$fabricLoaderVersion")
  modApi("net.fabricmc.fabric-api:fabric-api:$fabricApiVersion")

  modImplementation("net.fabricmc:fabric-language-kotlin:$kotlinFabricVersion+kotlin.$kotlinVersion")

  modApi("dev.architectury:architectury-fabric:$architecturyVersion")

  modApi("dev.latvian.mods:kubejs-fabric:$kubejsVersion")
  modImplementation("com.blamejared.crafttweaker:CraftTweaker-fabric-$minecraftVersion:$craftTweakerVersion")

  modImplementation("net.impleri:playerskills:$minecraftVersion-fabric-$playerskillsVersion")
}
