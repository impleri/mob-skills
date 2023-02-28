# Mob Skills

A library mod that exposes KubeJS methods to control mob spawning
using [Player Skills](https://github.com/impleri/player-skills).

## KubeJS API

### Register

We use the `MobSkillEvents.register` ***server*** event to register item restrictions. Registration requires a
test (`if` or `unless`) in a callback function which uses a player skills condition object (`can` and `cannot` methods).
If the player ***matches*** the criteria, the following restrictions are applied. This can cascade with other
restrictions, so any restrictions which disallow an action will trump any which do allow it. We also expose these
methods to indicate what restrictions are in place for when a player meets that condition. By default, no restrictions
are set, so be sure to set actual restrictions.

#### Allow Restriction Methods

- `nothing`: shorthand to apply all "allow" restrictions
- `spawnable`: The mob can spawn

#### Deny Restriction Methods

- `everything`: shorthand to apply the below "deny" abilities
- `unspawnable`: The mob cannot spawn

`spawnable`/`unspawnable` take an optional boolean parameter to change the spawning condition. With a `false` value
(which is the default), the action will apply if _any_ player in spawning range matches the condition. With a `true`
value, the action will apply if _all_ players in spawning range match the condition. Additionally, there is an `always`
condition to make the spawn condition apply always.

```js
MobSkillEvents.register(event => {
  // Always prevent blazes from spawning (default KubeJS handling)
  event.restrict('minecraft:blaze', is => is.unspawnable().always());

  // ALLOW creepers to spawn IF ALL players in range have the `started_quest` skill
  event.restrict("minecraft:creeper", is => is.spawnable(true).if(player => player.can("skills:started_quest")));

  // ALLOW cows to spawn UNLESS ANY players in range have the `started_quest` skill
  event.restrict("minecraft:cow", is => is.spawnable().unless(player => player.can("skills:started_quest")));

  // DENY zombies from spawning IF ANY player in range has the `started_quest` skill
  event.restrict('minecraft:zombie', is => is.unspawnable().if(player => player.can('skills:started_quest')));

  // DENY sheep from spawning UNLESS ALL player in range has the `started_quest` skill
  event.restrict('minecraft:sheep', is => is.unspawnable(true).unless(player => player.can('skills:started_quest')));
});
```

## Modpacks

Want to use this in a modpack? Great! This was designed with modpack developers in mind. No need to ask.
