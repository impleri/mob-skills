# Mob Skills

A library mod that exposes KubeJS methods to control mob spawning
using [Player Skills](https://github.com/impleri/player-skills).

## KubeJS API

### MobSkills Helper

The MobSkills helper is expected to be used within the KubeJS `EntityEvents.checkSpawn` event callback. It provides a
few shorthand methods that fit the pattern of `(deny|allow)(If|unless)(Any|All)`. Each method takes the KubeJS event as
the first parameter and a callback for each player's skills wrapper with can and cannot methods. You can then build your
own logic from there. Just remember that `if` requires the condition to pass and `unless` requires it to fail. `Any`
will trigger
`deny` or `allow` if _any_ player in range meets the appropriate condition while `All` requires _all_ players in range
to meet the condition.

```js
  // Log all check spawn events (default KubeJS handling)
  EntityEvents.checkSpawn(event => {
    console.info(`Entity ${event.entity.name} trying to spawn in ${event.block.dimension} at ${event.block.pos.toShortString()}`);
  });

 // Always prevent blazes from spawning (default KubeJS handling)
 EntityEvents.checkSpawn('minecraft:blaze', event => event.cancel());

  // Block creepers from spawning if a player in spawn range has started the quest
  EntityEvents.checkSpawn('minecraft:creeper', event => {
    MobSkills.allowUnlessAny(event, playerCondition => playerCondition.can('skills:started_quest'));
  });
  
  // Block zombies from spawning unless all players in spawn range have started the quest
  EntityEvents.checkSpawn('minecraft:zombie', event => {
    MobSkills.denyUnlessAll(event, playerCondition => playerCondition.can('skills:started_quest'));
  });
```

## Modpacks

Want to use this in a modpack? Great! This was designed with modpack developers in mind. No need to ask.

## TODO

- [] Deny interacting with a mob (e.g. trading)
- [] Implement using tags to apply spawn rules
- [] Implement using mod IDs to apply spawn rules
- [] `MobSkills.replaceWith()`
- [] `MobSkills.(allow|deny)Spawner`
- [] `MobSkills.(allow|deny)Natural`
- [] `MobSkills.(allow|deny)Dimension`
- Jade/TOP/WTHIT/Neat
    - https://github.com/Snownee/Jade/blob/1.19.1-forge/src/main/java/snownee/jade/api/IWailaClientRegistration.java#L124
    - https://github.com/McJtyMods/TheOneProbe/blob/1.16/src/main/java/mcjty/theoneprobe/api/IProbeConfigProvider.java
    - https://docs.bai.lol/wthit/plugin/disabling_tooltip/
    - https://github.com/VazkiiMods/Neat/blob/master/Xplat/src/main/java/vazkii/neat/NeatConfig.java
