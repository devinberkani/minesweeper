# Minesweeper

This is my completed Minesweeper project from the JetBrains Hyperskill Java For Beginners learning track.

I learned a lot completing this project. It was a great (and challenging) opportunity to learn about and implement the flood fill algorithm. My last project (Tic Tac Toe with AI) was the first time I used recursion in an application, so I was thankful to be able to get more practice with the use of this algorithm as well. 

## About

This game allows the user to first choose how many mines they want on the playing field. Then, using coordinate inputs, the user can play Minesweeper just like the classic PC game.

## Rules

The game starts with an unexplored minefield that has a user-defined number of mines.

The player can:

1. Mark unexplored cells as cells that potentially have a mine, and also remove those marks. Any empty cell can be marked, not just the cells that contain a mine. The mark is removed by marking the previously marked cell.
2. Explore a cell if they think it does not contain a mine.

There are two possible ways to win:

1. Marking all the cells that have mines correctly.
2. Opening all the safe cells so that only those with unexplored mines are left.

The following symbols represent each cell's state:

**.** as unexplored cells<br/>
**/** as explored free cells without mines around it<br/>
**Numbers from 1 to 8** as explored free cells with 1 to 8 mines around them, respectively<br/>
**&ast;** as marked cells

## Instructions

First, the user will be prompted to enter the number of mines they would like on the playing field (I recommend 10 or more for the game to actually be challenging). Next, simply enter coordinates for where you'd like to make your move (y, x), followed by either "free" to mark a free cell, or "mine" to place a mine marker.

**Example #1:** 1 1 free - *marks the coordinate (y1, x1) as free*<br/>
**Example #2:** 5 5 mine - *marks the coording (y5, x5) with a mine marker OR removes the mine marker if one is already there*<br/>

## Additional Info

As with all of my completed projects in Hyperskill, all commits for the project progress are in the general java repo I use for all practice exercises/projects.