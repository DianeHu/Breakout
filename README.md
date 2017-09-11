game
====

First project for CompSci 308 Fall 2017
* Names of people who worked on the project

Diane Hu (student), Delia (UTA, provided help), Jordan (UTA, provided help), Yuxiang (UTA, provided help), Mike (UTA, provided help)

* Date started

Sunday, September 3rd

* Date finished

Sunday, September 10th

* Number of hours spent

Around 40-50

* Role in developing the project

This was a solo project and I therefore completed the project on my own.

* Resources used in developing the project

I used given CS 308 resources that were listed on the webpage as a starter reference. I used the Sprite examples, the JavaFX tutorials, and read through JavaFX documentation through the API explanations in order to formulate my project. I also consulted UTAs Delia, Jordan, Yuxiang, and Mike during the project.

* Files used to start the project

I used ExampleBounce and Bouncer from lab_bounce, the first CS 308 bouncer lab. I used the versions Professor Duvall later uploaded to Git after class.

* Files used to test the project

N/A

* Any data or resource files required for the project

The only files necessary are the image files provided with the original lab_bounce, which I have uploaded to Git in my repository as well.

* Information about using the program

Cheat keys: Use c to clear to the next level (wins the game if user is at the third level), s to slow the bouncer down until released, a to accelerate the bouncer. Same as in plan.

Power-ups: Instant clear block appears every 25 points scored, paddle size increases every 15 points scored (until paddle size maxes out), bouncer size increases every 20 points scored (until bouncer size maxes out). Used different point counts from plan, since these values make more sense in context of the point values I ended up assigning the blocks. Also implemented bouncer size increase power-up instead of original "fireball" powerup, where the bouncer would destroy more blocks than it touched, because the "fireball" powerup both worked poorly with my design, and I realized would end up becoming confusing to the user, since it might not be obvious that's what it does.

Paddle: warps from one side of screen to other, and launches ball at start of every life/level. Decided not to implement these instead of original paddle ability of destroying all blocks above it at some amount of points scored, for similar reasons as why I ended up not writing a "fireball"--worked poorly with my design, and would end up confusing.

Note: originally, in plan ball was meant to change color when bounced off different blocks. Ran out of time to implement, but could do so if time allowed, by setting the ball image every time it bounced against a block, using the override bounceBlock methods in each block subclass. Did not prioritize since it adds little functionality.

Otherwise, game conforms to plan.

* Known bugs

Bouncer sometimes hits paddle and "sticks" to it, as in the image seems stuck on the paddle/slides along it. Usually if paddle is then moved the bouncer "unsticks" itself and continues bouncing as usual.

* General thoughts

I definitely thought having lab_bounce was helpful towards giving us an idea of what to expect. The workload was more than I anticipated but overall I think it's a good experience and introduction to JavaFX.