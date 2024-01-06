# AdventOfCode 2023 Day 20

This is my solution for [day 20](https://adventofcode.com/2023/day/20) of the current [AdventOfCode](https://adventofcode.com). This time I decided to write my solution in Java, as the various modules with the pulse mechanism can be perfectly represented in the for of object oriented programming. On another note, this also means, that the solution contains quite a bit of overload, which wouldn't be necessary in script languages like Python. But to balance this disadvantage, in my opinion a solution in Java is by far more readable and intuitive for the given type of task.

## Task 1

In the first of the two tasks, I had to build the pulse mechanism itself, by also converting the modules found in the file into their respective objects and handling their various pulse behaviours. The class "PulseManager" is responsible for receiving pulse requests and going through them in a queue. The amount of low and high pulses are saved and the multiplied value is retured after the full process, as this represents the solution requested in the actual task.

## Task 2

For the second task, the most intuitive way would have been, to brute force the solution, by going through the pulse cycles as long as it is necessary to find a low pulse into the rx module. This wasn't possible though, as the task is specifically designed to require a huge amount of pulses until the solution can be found. Instead we use the least common multiple of all conjunctions leading to a low pulse in the rx module, which leads to the same solution as the brute force way would have if it was possible for this task.

## Running the app

The app itself is wrapped into a gradle setup, as this makes a lot of the process, including the running of it, a lot easier. You can run the app directly through the default gradle run command or just the IDE features of your choice. If you prefer to run the app just like that, I have also added the finished jar file into the releases section of this repository.
