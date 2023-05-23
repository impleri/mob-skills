val modId: String = property("archives_base_name").toString()
val kotlinVersion: String = property("kotlin_version").toString()
val minecraftVersion: String = property("minecraft_version").toString()
val forgeVersion: String = property("forge_version").toString()
val kotlinForgeVersion: String = property("kotlin_forge_version").toString()
val architecturyVersion: String = property("architectury_version").toString()
val kubejsVersion: String = property("kubejs_version").toString()
val craftTweakerVersion: String = property("crafttweaker_version").toString()
val playerskillsVersion: String = property("playerskills_version").toString()

dependencies {
  forge("net.minecraftforge:forge:$forgeVersion")

  implementation("thedarkcolour:kotlinforforge:$kotlinForgeVersion")
  forgeRuntimeLibrary(kotlin("stdlib", kotlinVersion))
  forgeRuntimeLibrary(kotlin("reflect", kotlinVersion))

  modImplementation("dev.architectury:architectury-forge:$architecturyVersion")
  modImplementation("dev.latvian.mods:kubejs-forge:$kubejsVersion")
  modImplementation("com.blamejared.crafttweaker:CraftTweaker-forge-$minecraftVersion:$craftTweakerVersion")

  modImplementation("net.impleri:playerskills:$minecraftVersion-forge-$playerskillsVersion")
}
