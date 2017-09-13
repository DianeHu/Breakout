Design
======

### High-level design goals

Since this was the first CS 308 project, and I was unfamiliar with JavaFX before starting the project, my main goals were to fulfill the project requirements with an acceptably clean design framework. I wanted to have distinct classes, some extensions of others by inheritance, to organize my game in a manner such that I could easily discretize the work--each class should have its own delegated functions, and ultimately come together in the main Game class to form a game that had clearly defined components working together smoothly.

### How to add new features

Some features can be added quite easily to this game, while others might take more effort to do so.

* To add blocks: create a new subclass extending the superclass Blocks, and override the bounceBlock and getVal methods in the superclass to indicate what a bouncer should do when hitting this new block, and how many points it should give a user when hit. Note that this type of addition only works well when the new block's behavior includes only basic bounce behavior, i.e., slowing the bouncer down, speeding it up, making it fall down vertically, etc. etc. Anything that has to do with x and y directions and x and y speeds only can be added easily this way.

* To add "abnormal" blocks, i.e., blocks with special abilities/behavior: still create a new subclass extending the superclass Blocks, and possibly override the getVal method if this new block is supposed to return a different point value. However, the majority of the logic for an "abnormal" block will have to be updated in Game itself, within the abnormalBlockCheck method. In that method, new logic would have to be written dictating what a bouncer should do when hitting the new block. There are helpful methods in game to aid this process, such as nextLevel, or restartBouncer, etc., but this process takes some more work than adding a more basic block

* To add different bouncer types: create a new subclass extending the class Bouncer. Although Bouncer is not a superclass right now, since it has no subclasses of different bouncer types, if a new bouncer type was to be added, it would easily become the superclass for all bouncers. Override whatever bouncer methods are necessary, and in Game itself, depending on what type of bouncer is added, the logic of the calls in the bounce method itself might have to be tweaked.

* To add more levels: go into the Levels class, and create a new method called level(your level). Within that level, using the setUpBlocks method in Levels, designate algorithmically the block configuration desired. Back in Game, in the switch statement in setUpGame, add another case that calls the new level method created. Go into the nextLevel method in Game, and update the if statement to reflect the fact that now, say, 4 levels are available to play before displaying win or lose, instead of 3. Also update the winLose method to reflect the same idea that one more level can now be played before determining whether or not a user has won.

### Justifications

The biggest tradeoff I made was the Levels class. It is the only class with public static methods, and is not really a true class. It has no constructor, so it doesn't really create "levels"--it merely helps game set the block configuration for each level. Although this is easier to code than full on "level" objects, and still allows me to set block configurations more easily per level than simply putting the configurations in Game itself, using this approach means each level has to have a block configuration hardcoded, instead of being able to pass in some requirements to a constructor and having the constructor return the right configuration for the level. Since I focused more on getting levels going towards the end, I prioritized debugging everything else, and having merely a helper Levels class, over designing a full fledged class. I do not have an accurate estimate of how long coding a true Levels class would take, so I can't say for certain it was worth it to debug and refine everything else instead of creating the class proper, but my intuition is that by taking some time away from improving this working approximation, I was able to improve other features substantially.

Additionally, I will note that although the public static designation is not ideal, I don't think it affects much--since each level has a set block configuration that isn't supposed to change in context of this game (i.e., no matter how many times you play, level one will always look like level one), I don't think the static designation hurts the overall design very much. Additionally, only the Game class accesses Levels.

### Assumptions/decisions

I assumed that bouncer/corner-of-block collisions would be extremely rare, and thus only really accounted for what a bouncer should do when colliding with the top/bottom sides of a block, or the left/right sides.