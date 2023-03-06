# Mob Skills

A library mod that exposes KubeJS methods to control mob spawning
using [Player Skills](https://github.com/impleri/player-skills).

## KubeJS API

### Register

We use the `MobSkillEvents.register` ***server*** event to register mob restrictions. Registration requires a
test (`if` or `unless`) in a callback function which uses a player skills condition object (`can` and `cannot` methods).
If the player ***matches*** the criteria, the following restrictions are applied. This can cascade with other
restrictions, so any restrictions which disallow an action will trump any which do allow it. We also expose these
methods to indicate what restrictions are in place for when a player meets that condition. By default, no restrictions
are set, so be sure to set actual restrictions.

#### Allow Restriction Methods

- `nothing`: shorthand to apply all "allow" restrictions
- `spawnable`: The mob can spawn
- `usable`: Players that meet the condition can interact with the entity (e.g. trade with a villager)

#### Deny Restriction Methods

- `everything`: shorthand to apply the below "deny" abilities
- `unspawnable`: The mob cannot spawn
- `unusable`: Players that meet the condition cannot interact with the entity (e.g. trade with a villager)

`spawnable`/`unspawnable` take an optional boolean parameter to change the spawning condition. With a `false` value
(which is the default), the action will apply if _any_ player in spawning range matches the condition. With a `true`
value, the action will apply if _all_ players in spawning range match the condition. Additionally, there is an `always`
condition to make the spawn condition apply always.

```js
MobSkillEvents.register(event => {
  // Always prevent blazes from spawning
  event.restrict('minecraft:blaze', is => is.unspawnable().always());

  // Prevent all vanilla mobs from spawning
  event.restrict('@minecraft', is => is.unspawnable().always());

  // Prevent all illagers from spawning
  event.restrict('#raiders', is => is.unspawnable().always());

  // ALLOW creepers to spawn IF ALL players in range have the `started_quest` skill
  event.restrict("minecraft:creeper", is => is.spawnable(true).if(player => player.can("skills:started_quest")));

  // ALLOW cows to spawn UNLESS ANY players in range have the `started_quest` skill
  event.restrict("minecraft:cow", is => is.spawnable().unless(player => player.can("skills:started_quest")));

  // DENY zombies from spawning IF ANY player in range has the `started_quest` skill
  event.restrict('minecraft:zombie', is => is.unspawnable().if(player => player.can('skills:started_quest')));

  // DENY sheep from spawning UNLESS ALL player in range has the `started_quest` skill
  event.restrict('minecraft:sheep', is => is.unspawnable(true).unless(player => player.can('skills:started_quest')));

  // Players cannot interact with villagers unless they have `started_quest` skill
  event.restrict("minecraft:villager", is => is.unusable().unless(player => player.can("skills:started_quest")));
});
```

### Caveats

Because of the way we're handling spawn conditions, if you are using an `unless` condition, you should not also
manipulate `usable` in the same restriction.

## Developers

Add the following to your `build.gradle`. I depend
on [Architectury API](https://github.com/architectury/architectury-api), [KubeJS](https://github.com/KubeJS-Mods/KubeJS),
and [PlayerSkills](https://github.com/impleri/player-skills), so you'll need those as well.

```groovy
dependencies {
    // Common should always be included 
    modImplementation "net.impleri:mob-skills-${minecraft_version}:${mobskills_version}"
    // Plus forge
    modApi "net.impleri:mob-skills-${minecraft_version}-forge:${mobskills_version}"
    // Or fabric
    modApi "net.impleri:mob-skills-${minecraft_version}-fabric:${mobskills_version}"
}
repositories {
    maven {
        url = "https://maven.impleri.org/minecraft"
        name = "Impleri Mods"
        content {
            includeGroup "net.impleri"
        }
    }
}
```

## Modpacks

Want to use this in a modpack? Great! This was designed with modpack developers in mind. No need to ask.
