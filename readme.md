# Mob Skills

A library mod to control mob spawning using skill-based restrictions created in KubeJS scripts. If you've seen Mob
Stages in 1.12, then you have a rough idea of what this mod does. Out of the box, it provides a registry for tracking
mob spawning restrictions and uses it whenever a mob spawn is attempted.

[![CurseForge](https://cf.way2muchnoise.eu/short_714844.svg)](https://www.curseforge.com/minecraft/mc-mods/mob-skills)
[![Modrinth](https://img.shields.io/modrinth/dt/mob-skills?color=bcdeb7&label=%20&logo=modrinth&logoColor=096765&style=plastic)](https://modrinth.com/mod/mob-skills)
[![MIT license](https://img.shields.io/github/license/impleri/mob-skills?color=bcdeb7&label=Source&logo=github&style=flat)](https://github.com/impleri/mob-skills)
[![Discord](https://img.shields.io/discord/1093178610950623233?color=096765&label=Community&logo=discord&logoColor=bcdeb7&style=plastic)](https://discord.com/invite/avxJgbaUmG)
[![Maven](https://img.shields.io/maven-metadata/v?color=096765&label=%20&logo=gradle&logoColor=bcdeb7&metadataUrl=https%3A%2F%2Fmaven.impleri.org%2Fminecraft%2Fnet%2Fimpleri%2Fmob-skills-1.19.2%2Fmaven-metadata.xml&style=flat)](https://github.com/impleri/mob-skills#developers)

### xSkills Mods

[Player Skills](https://github.com/impleri/player-skills)
| [Block Skills](https://github.com/impleri/block-skills)
| [Dimension Skills](https://github.com/impleri/dimension-skills)
| [Fluid Skills](https://github.com/impleri/fluid-skills)
| [Item Skills](https://github.com/impleri/item-skills)
| [Mob Skills](https://github.com/impleri/mob-skills)

## Concepts

This mod leans extensively on Player Skills by creating and consuming the Skill-based Restrictions.

## KubeJS API

### Register

We use the `MobSkillEvents.register` ***server*** event to register mob restrictions.

Spawn restrictions are then calculated at spawn time based on the players in the entity's despawn range (i.e. 128 blocks
for most mobs). If the players ***match*** the conditions, the restrictions are applied.

Other restrictions are calculated when the player attempts to interact with the mob. If the player matches the
conditions, the restriction is applied.

Restrictions can cascade with other restrictions, so any restrictions which disallow an action will trump any which do
allow it. We also expose these methods to indicate what restrictions are in place for when a player meets that
condition. By default, no restrictions are set, so be sure to set actual
restrictions. [See Player Skills documentation for the shared API](https://github.com/impleri/player-skills#kubejs-restrictions-api).

#### Allow Restriction Methods

- `nothing()` - shorthand to apply all "allow" restrictions
- `spawnable(matchAllInsteadOfAny?: boolean)` - The mob can spawn if any (or all) players in range match the criteria
- `usable()` - Players that meet the condition can interact with the entity (e.g. trade with a villager)

#### Deny Restriction Methods

- `everything()` - shorthand to apply the below "deny" abilities
- `unspawnable(matchAllInsteadOfAny?: boolean)` - The mob cannot spawn if any (or all) players in range match the
  criteria
- `unusable()` - Players that meet the condition cannot interact with the entity (e.g. trade with a villager)

#### Additional Methods

- `always()` - Change `spanwable`/`unspawnable` into an absolute value instead of a condition

#### Examples

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
manipulate `usable` in the same restriction. Just keep the two restrictions separate.

## In-Game Commands

We expose a handful of in-game commands for players and mods:

- `/mobskills debug`: Toggles debug-level logging for Mob Skills. Requires mod permissions.

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
