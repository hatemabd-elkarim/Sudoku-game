# Sudoku Game 🎮🧠
#### Video Demo:  <https://www.youtube.com/watch?v=MTz-DqE44Hk>
#### Description:
A colorful, interactive desktop **Sudoku game** built using **Java**. This project features a beautiful GUI, difficulty levels, limited mistakes, a live timer, and a number selection panel — all wrapped into an engaging user experience.

## 📦 Features

- ✅ **Dynamic Sudoku Generator** – Each game starts with a fresh puzzle.
- 🧠 **Difficulty Levels** – Easy, Medium, Hard, and Expert that determines the number of free cells for the user to fill up.
- 🚫 **Mistake Limiter** – You lose if you make 3 mistakes.
- ⏱ **Stopwatch Timer** – Challenge yourself to finish faster.
- 🎨 **Interactive Number Panel** – Select numbers to fill the grid.
- 🔁 **Responsive UI** – User-friendly layout and stylings.

## Gameplay & Rules
The player first enters to the welcome screen to start a new game, that tooks him/her to choose the game difficulty from 4 options easy, medium, hard, expert that is determined by the number of free cells are there.
The player can choose a specific cell by just clicking on it and then fill up it with the number selector underneath by a number from 1-9
regarding that each row, column, and any 3*3 box should contain the numbers from 1 to 9 but without repetition.
The player has only 3 mistakes otherwise the game will end up with game over and either it ended up with a win or lose the player will be able to enter to a new game and choose a different difficulty level if he/she wanted.

## 🛠 Technologies Used & Logic

- Java
- JavaFX
- OOP & Inverse Engineering

The project applies some little bit of inverse engineering logic to create the puzzle to ensure that it has only one solution, by randomizing the numbers in each row at first and ensuring that no number is repeated within its row, column or 3*3 box, then go in a try and error loops to remove numbers from the gridn and try to find a unique solution.
increase the number of words as you can
