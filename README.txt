<Flashlet> - Jiayi Hu and Patrick Qiu

When the program opens, the first page the user should see is the main menu, which has three options: Multiple Choice, Asteroids, or Exit. 
The names are self-explanatory, but they refer to the different modes of studying this project allows for, as well as the exit button.
Multiple Choice gives a question in full and allows 4 answer choices to act as matching tool. 
Asteroid is based off of the old Quizlet game but simplified into also multiple choice style to avoid the troubles of trying to type exact answers. 
Therefore, all interactions are mouse-click based, so really all the user needs is a mouse and track-pad, or a touch-screen also would suffice. 
Additionally, a keyboard is needed to type in the name of the .txt file. 
The goal of this project, originally, was to help study for my CHNS 2000 final, which has a quote matching section, but the tool itself can be expanded across all disciplines.
Specifically, the reader.java is setup to split .txt files by the symbol '@', so a user with a cleaned .txt file that has terms and answers separated by '@' can benefit from this project.

The submission should include the following files: 
- asteroid.java - asteroid class that defines the behavior of asteroids for the asteroid game.
- asteroidFrame.java - the setup of the asteroid game 
- gamePanel.java - the asteroid game logic and execution
- mainMenuFrame.java - main menu; the first thing user sees
- multipleChoice.java - the logic for the multiple choice game, this actually works by itself in the terminal
- multipleChoiceFrame.java - the execution of the multiple choice game
- reader.java - reader class that reads in the quotes .txt files and creates the hashmap this tool is based on
- runGUI.java - the file the user should run to start the program

The one thing the user might want is their own .txt file to cater the studying to topics of the user's choice.
Our submission will include three .txt files: 
- qf.txt - which stands for quotes full, which is related to the CHNS 2000 final exam, cleaned by ourselves.
- test.txt - the simplest one where the user can just match the numbers.
- asteroid.txt - a simple translation based file where the user needs a little knowledge of the Chinese language that works especially well with the Asteroid game.

There are no audio or web picture components in this project. 
Everything is coded in the java language within the scope of topics covered within CPSC 1001 with an emphasis on Swing tools. 

To begin, the user should run runGUI.java, which contains the primary main method, to start the program: 
javac runGUI.java
java runGUI