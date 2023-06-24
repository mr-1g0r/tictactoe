# tictactoe

## Work on project. Stage 5/5: An undefeated champion

# Description
Congratulations, you've almost reached the finish line! To complete the task, it's now time to turn the AI into a strong opponent by adding a hard difficulty level.

Unlike medium, when the AI is playing at hard level, it doesn't just look one move ahead to see an immediate win or prevent an immediate loss. At this level, it can look two moves ahead, three moves ahead, and even further. It can calculate all possible moves that might be played during the game, and choose the best one based on the assumption that its opponent will also play perfectly. So, it doesn't rely on the mistakes of its opponent and plays the game without fault from start to finish regardless of the opponent's skill!

The algorithm that implements this is called minimax. It's a recursive brute force algorithm that maximizes the value of the AI's position and minimizes the worth of its opponent's. Minimax is not just for Tic-Tac-Toe. You can use it with any other game where two players make alternate moves, such as chess.

# Objectives
In this last stage, you need to implement the hard difficulty level using the minimax algorithm. As a recursive algorithm, it can be tricky to think about, so you should try to look at different resources to find the one that works best for your understanding (which is an important skill to get good at as you progress in you software development career!). Consider the following as starting points:

Video-based explanations:
"Tic Tac Toe AI with Minimax Algorithm" by The Coding Train
"Algorithms Explained – minimax and alpha-beta pruning" by Sebastian Lague
"Mega-R3. Games, Minimax, Alpha-Beta" from MIT OpenCourseWare
Text-based explanations:
"How to make your Tic Tac Toe game unbeatable by using the minimax algorithm" on freeCodeCamp.org
"Case Study on Tic-Tac-Toe Part 2: With AI" from Nanyang Technological University
"Tic Tac Toe - Creating Unbeatable AI" on Medium
You should also add a hard parameter so that it's possible to play against this level.

# Example

The example below shows how your program should work.
The greater-than symbol followed by a space (> ) represents the user input. Note that it's not part of the input.

```
Input command: > start hard user
Making move level "hard"
---------
|       |
| X     |
|       |
---------
Enter the coordinates: > 2 2
---------
|       |
| X O   |
|       |
---------
Making move level "hard"
---------
|   X   |
| X O   |
|       |
---------
Enter the coordinates: > 3 2
---------
|   X   |
| X O   |
|   O   |
---------
Making move level "hard"
---------
| X X   |
| X O   |
|   O   |
---------
Enter the coordinates: > 3 1
---------
| X X   |
| X O   |
| O O   |
---------
Making move level "hard"
---------
| X X X |
| X O   |
| O O   |
---------
X wins

Input command: > exit
```