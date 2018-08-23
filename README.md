# PegasusEffectCure
> Were you ever kicked for flying when all you did was just hop on a horse?
> Well, horse don't fly. So it must have been a Pegasus!

Temporary fix for a bug https://bugs.mojang.com/browse/MC-98727.

If a player is floating (moving upwards in the air) and doesn't have permissions to fly there is a boolean variable set to true (something like 'floating = true') and 'floatingTicks' counter is working, when it reaches 80 and player is still floating they are kicked for flying.
The problem is that when player will hop on horse when floating, this variable and counter aren't reset so server sees player floating despite they siting on a horse, so after 80 ticks player is kicked.
This plugin listens to the event of player hopping on a horse, checks if player is floating and if so resets this variable and counter.
