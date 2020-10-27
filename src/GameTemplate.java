/***************************************************************************************
* Name:        ChangeOneLetterGame - Graphics
* Author:      Lukas Moser, Steven Peng
* Date:        Oct 24, 2020
* Purpose:     Shows how to create an interactive, graphical application in Java using
               Dialog Boxes from JOptionPane for user input.
****************************************************************************************/

//test

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.net.URL;
import java.io.*;

public class GameTemplate extends JPanel {
	static Image[] pics = new Image[4]; // array of dancing gifs
	static Image bgImage1; // image displayed while play occurs for part 1
	static Image bgImage2; // image displayed while play occurs for part 2
	static JPanel panel; // main drawing panel
	static JFrame frame; // window frame which contains the panel
	static final int WINDOW_WIDTH = 1200; // width of display window
	static final int WINDOW_HEIGHT = 800;// height of display window

	static int gameStage = 0; // stages of game
	static final int WELCOME_SCREEN = 0;
	static final int MENU = 1;
	static final int INSTRUCTIONS = 2;
	static final int PLAY = 3;
	static final int END_GAME = 4;

	static int numPlayers = 0; // number of players
	static double runningTotal = 0; // runningTotal of game
	static int turn = 1; // current turn of game (starts at turn 1)
	static String dataEntered = ""; // input from user
	static boolean resetDataEntered = false; // used to reset dataEntered to empty string
	static String currentPlayer = ""; // tracks the currentplayer
	static String currentWord = ""; // tracks the currentWord
	static String startWord = "";
	static String goalWord = "";
	static String previousWord = "";

	static String playOutput = ""; // output to panel
	static String playOutput1 = ""; // output to panel
	static String playOutput2 = ""; // output to panel
	static String playOutput3 = ""; // output to panel
	static String playOutput4 = ""; // output to panel
	static String playOutputList = ""; // output all steps
	static String instructionsText = ""; // instructions
	
	static int turnOnePhase = 0; // switching between input start word, input goal word, and input word change
	static String[] fileContents; //creating the array for the dictionary
	static String errorMessage = "";

	// start main program
	// * initializes the window for the game
	public static void main(String args[]) {
		// Create Image Object
		Toolkit tk = Toolkit.getDefaultToolkit();
		
		//initialize the dictionary
		fileContents = getFileContents("dictionary.txt");

		// Load background images
		URL url = GameTemplate.class.getResource("bg1.jpg");
		bgImage1 = tk.getImage(url);
		url = GameTemplate.class.getResource("bg2.jpg");
		bgImage2 = tk.getImage(url);

		// Load pics array with images of dancing letters
		String[] letters = { "a", "e", "n", "w" };
		for (int i = 0; i < pics.length; i++) {
			System.out.println(letters[i]); // delete this line when no longer needed
			url = GameTemplate.class.getResource(letters[i] + ".gif");
			pics[i] = tk.getImage(url);
		}

		// Create Frame and Panel to display graphics in

		panel = new GameTemplate(); /***** MUST CALL THIS CLASS (ie same as filename) ****/

		panel.setPreferredSize(new Dimension(WINDOW_WIDTH, WINDOW_HEIGHT)); // set size of application window
		frame = new JFrame("Change One Letter Game"); // set title of window
		frame.add(panel);

		// add a key input listener (defined below) to our canvas so we can respond to
		// key pressed
		frame.addKeyListener(new KeyInputHandler());

		// exits window if close button pressed
		frame.addWindowListener(new ExitListener());

		// request the focus so key events come to the frame
		frame.requestFocus();
		frame.pack();
		frame.setVisible(true);

	} // main

	/*
	 * paintComponent gets called whenever panel.repaint() is called or when
	 * frame.pack()/frame.show() is called. It paints to the screen. Since we want
	 * to paint different things depending on what stage of the game we are in, a
	 * variable "gameStage" will keep track of this.
	 */
	public void paintComponent(Graphics g) {
		super.paintComponent(g); // calls the paintComponent method of JPanel to display the background

		// display welcome screen
		if (gameStage == WELCOME_SCREEN) {

			// sets color using RGB values
			g.setColor(new Color(24, 160, 202));

			// draw background
			g.fillRect(0, 0, WINDOW_WIDTH, WINDOW_HEIGHT);

			// draw a white oval - because we can
			g.setColor(Color.white);
			g.fillOval(400, 300, 100, 300);

			// top line of images
			for (int i = 0; i < pics.length; i++) {
				g.drawImage(pics[i], i * 194, 0, this); // display the image
			} // for

			g.setColor(Color.black);
			g.setFont(new Font("SansSerif", Font.BOLD, 16)); // set font
			g.drawString("Welcome to ", 360, 250);
			g.drawString("Press any key to continue.", 310, 350);

			g.setColor(Color.blue);
			g.setFont(new Font("SansSerif", Font.BOLD, 36)); // set font
			g.drawString("Game Template", 280, 300); // display

			// display menu
		} else if (gameStage == MENU) {
			g.setColor(Color.blue);
			g.setFont(new Font("SansSerif", Font.BOLD, 36)); // set font
			drawString(g, "Change One Letter", 230, 180); // display
			g.setFont(new Font("SansSerif", Font.BOLD, 16)); // set font
			instructionsText = "Please make one of the following choices:\n\n1) Display Instructions.\n\n2) One Player Game\n\n3) Two Player Game\n\n4) Exit";
			drawString(g, instructionsText, 230, 280); // display

			// display instructions
		} else if (gameStage == INSTRUCTIONS) {
			g.drawImage(bgImage1, 0, 0, this);
			g.setColor(Color.blue);
			g.setFont(new Font("Dialog", Font.BOLD, 36)); // set font
			drawString(g, "Instructions", 280, 100); // display title
			g.setFont(new Font("Dialog", Font.PLAIN, 18)); // set font

			instructionsText = "The text \nfor the instructions\ngoes here.\nok?";
			drawString(g, instructionsText, 150, 200); // display instructions

			// display game play
			// * if you want different types of display for different
			// * parts of the play, add additional stages (ie PLAY2, PLAY3 etc)
		} else if (gameStage == PLAY) {

			g.setColor(new Color(24, 160, 202));

			g.fillRect(0, 0, WINDOW_WIDTH, WINDOW_HEIGHT);

			// set font and colour
			g.setColor(Color.white);
			g.setFont(new Font("SansSerif", Font.BOLD, 16));

			// display contents of playOutput strings
			// * these have been set in other methods during game play
			drawString(g, playOutput, 20, 30);
			drawString(g, playOutput1, 600, 30);
			drawString(g, playOutput3, 20, 200);

			g.setColor(Color.red);
			g.setFont(new Font("SansSerif", Font.BOLD, 12));
			drawString(g, playOutput4, 20, 80);

			g.setColor(Color.green);
			g.setFont(new Font("SansSerif", Font.BOLD, 36));
			drawString(g, playOutput2, 20, 100);

			// display all turns in a box on right side
			g.setColor(Color.green);
			g.drawRect(580, 100, 200, 400);
			g.setFont(new Font("SansSerif", Font.BOLD, 16));
			drawString(g, playOutputList, 600, 120);
		}

		// display end of game
		else {
			g.drawImage(bgImage2, 0, 0, this);
			// set font and colour
			g.setColor(Color.white);
			g.setFont(new Font("SansSerif", Font.BOLD, 16));

			// display contents of playOutput strings
			drawString(g, playOutput, 20, 50);
			drawString(g, playOutput1, 20, 100);
			drawString(g, playOutput2, 20, 150);
			drawString(g, playOutput3, 20, 200);
			drawString(g, playOutput4, 20, 250);

		} // else
	} // paintComponent

	/*
	 * A class to handle keyboard input from the user. Implemented as a inner class
	 * because it is not needed outside the EvenAndOdd class.
	 */
	private static class KeyInputHandler extends KeyAdapter {
		public void keyTyped(KeyEvent e) {
			// quit if the user presses "escape"
			if (e.getKeyChar() == 27) {
				System.exit(0);
			} else if (gameStage == MENU) {

				// respond to menu selection
				switch (e.getKeyChar()) {
				case 49:
					showInstructions();
					break; // Key "1" pressed
				case 50:
					numPlayers = 1;
					startGame();
					break; // Key "2" pressed
				case 51:
					numPlayers = 2;
					startGame();
					break; // Key "3" pressed
				case 52:
					System.exit(0); // Key "4" pressed
				} // switch
			}

			// respond to keys typed during game play
			else if (gameStage == PLAY) {

				// computer turn
				if (isComputerTurn()) {
					computerTakeTurn();
					return;
				}

				// clear typed input
				if (resetDataEntered) {
					resetDataEntered = false;
					dataEntered = "";
				}

				// if user hits enter, record what is typed in
				if (e.getKeyChar() == Event.ENTER) {
					saveInput();

					// end game after 10 turns, you will want to change this condition
					if (turn == 10) {
						endGame();
					}

				} else {
					recordKey(e.getKeyChar());
				}
			}
			// if all else fails, show menu
			else {
				showMenu();
			} // else
		} // keyTyped
	} // KeyInputHandler class

	// add key typed to dataEntered
	private static void recordKey(char key) {

		// backspace pressed -> removes characters
		if (key == 8 && dataEntered.length() > 0) {
			dataEntered = dataEntered.substring(0, dataEntered.length() - 1);
		}

		// otherwise add key typed to dataEntered
		else {
			dataEntered += (key + "");
		}
		if(turnOnePhase == 1) {
			playOutput4 = "Player 2 entered";
		}
		else {
			playOutput4 = getCurrentPlayer() + " entered ";
		}
		playOutput2 = dataEntered;
		panel.repaint();
	}

	// returns name of currentPlayer
	private static String getCurrentPlayer() {
		if (numPlayers == 2) {
			return (turn % 2 != 0) ? "Player 1" : "Player 2";
		} else {
			return (turn % 2 != 0) ? "Player 1" : "Computer";
		}

	} // getCurrentPlayer

	// returns name of other player
	private static String getOtherPlayer() {
		if (numPlayers == 2) {
			return (turn % 2 == 0) ? "Player 1" : "Player 2";
		} else {
			return (turn % 2 == 0) ? "Player 1" : "Computer";
		}

	} // getCurrentPlayer

	// returns true if it is the computer's turn
	public static boolean isComputerTurn() {
		return (numPlayers == 1 && turn % 2 == 0);
	}

	// take computer turn
	public static void computerTakeTurn() {
		String computerInput = "RANDOM INPUT";

		// you might process the computer's random input here

		currentWord = computerInput;
		playOutputList += "\n" + computerInput;
		turn++;
		displayTurn();

	}

	// display results from turn
	public static void displayTurn() {
		// set up strings for display
		playOutput = "Processing...";
		playOutput1 = "Turn " + turn;
		playOutput4 = "";
		playOutput2 = "";

		// getting start word and goal word
		if (turn == 1) {
			if (turnOnePhase == 0) {
				playOutput3 = errorMessage + "Player 1, please enter the starting word.";
			}
			else if (turnOnePhase == 1) {
				playOutput3 = errorMessage + "Player 2, please enter the goal word";
			}
			else if (turnOnePhase == 2) {
				playOutput3 = errorMessage + getCurrentPlayer() + " enter your input.";
			}
			panel.repaint();
		}

		else {
			// set instructions to execute computer turn
			if (isComputerTurn()) {
				playOutput3 = "Press enter to see " + getCurrentPlayer() + "'s turn.";
			}
			// else it is player 1 or 2's turn
			else {
				playOutput3 = getCurrentPlayer() + " enter your input.";
			}
			panel.repaint();
		}
		

	} // displayTurn

	// Saves input entered by user into currentWord
	private static void saveInput() {

		//saving start word
		if (turnOnePhase == 0) {
			startWord = dataEntered;
			if(startWord.length() != 4) {
				errorMessage = "It must be a four letter word. ";
				displayTurn();
			}
			else if(isValidWord(startWord)) {
				errorMessage = "That is not a valid english word. ";
				displayTurn();
			}
			currentWord = startWord;
			playOutputList += "The start word is " + startWord + "\n";
			turnOnePhase++;
			errorMessage = "";
			resetDataEntered = true; 
			displayTurn();
		}
		
		//saving goal word
		else if (turnOnePhase == 1) {
			goalWord = dataEntered;
			if(goalWord.length() != 4) {
				errorMessage = "It must be a four letter word. ";
				displayTurn();
			}
			else if(isValidWord(goalWord)) {
				errorMessage = "That is not a valid english word. ";
				displayTurn();
			}
			playOutputList += "The goal word is " + goalWord + "\n";
			turnOnePhase++;
			errorMessage = "";
			resetDataEntered = true;
			displayTurn();
		}
		else {
			// save dataEntered into a more permanent location, error checking it and then  reseting it
			previousWord = currentWord;
			currentWord = dataEntered;
			if (currentWord.length() != 4) {
				errorMessage = "The new word must also be a four letter word. ";
				displayTurn();
			}
			else if (isValidWord(currentWord)) {
				errorMessage = "That is not a valid english word. ";
				displayTurn();
			}
			else if (onlyOneLetterChanged()) {
				errorMessage = "You can only change one letter. ";
				displayTurn();
			}
			playOutputList += "\n" + currentWord;
			resetDataEntered = true; // this will cause dataEntered to get erased
			errorMessage = "";
			turn++; // record turn completed
			displayTurn();
		}
	    
	}

	// end game.
	private static void endGame() {

		playOutput = "This game is over.";
		playOutput1 = "Somebody won";
		playOutput2 = "In " + turn + " turns.";
		playOutput3 = "You should say who\n and in how many steps.";
		playOutput4 = "Press any key to return to menu";
		gameStage = END_GAME;
		panel.repaint();
	}

	/* Shuts program down when close button pressed */
	private static class ExitListener extends WindowAdapter {
		public void windowClosing(WindowEvent event) {
			System.exit(0);
		} // windowClosing
	} // ExitListener

	private static void showMenu() {
		gameStage = MENU;
		panel.repaint();
	} // showMenu

	// sets game up to display instructions
	private static void showInstructions() {
		gameStage = INSTRUCTIONS;
		panel.repaint();
	} // showInstructions

	// sets game up to instruct players to start game
	private static void startGame() {
		gameStage = PLAY;

		// reset all variables in case of previous game
		playOutputList = "";
		playOutput2 = "";
		playOutput4 = "";
		currentWord = "";
		turn = 1;
		dataEntered = "";
		turnOnePhase = 0;

		// calling display turn
		displayTurn();

		panel.repaint();

	} // playGame

	/*
	 * draw multi-line Strings author: John Evans
	 */
	private static void drawString(Graphics g, String text, int x, int y) {

		// draws each line on a new line
		for (String line : text.split("\n")) {
			g.drawString(line, x, y += g.getFontMetrics().getHeight());
		} // for
	} // drawString
	
	//  reads fileName and returns the contents as String array
    //  with each line of the file as an element of the array
    public static String [] getFileContents(String fileName){

        String [] contents = null;
        int length = 0;
        try {

           // input
           String folderName = "/subFolder/"; // if the file is contained in the same folder as the .class file, make this equal to the empty string
           String resource = fileName;

			// this is the path within the jar file
			InputStream input = GameTemplate.class.getResourceAsStream(folderName + resource);
			if (input == null) {
				// this is how we load file within editor (eg eclipse)
				input = GameTemplate.class.getClassLoader().getResourceAsStream(resource);
			}
			BufferedReader in = new BufferedReader(new InputStreamReader(input));	
           
           
           
           in.mark(Short.MAX_VALUE);  // see api

           // count number of lines in file
           while (in.readLine() != null) {
             length++;
           }

           in.reset(); // rewind the reader to the start of file
           contents = new String[length]; // give size to contents array

           // read in contents of file and print to screen
           for (int i = 0; i < length; i++) {
             contents[i] = in.readLine();
           }
           in.close();
       } catch (Exception e) {
           System.out.println("File Input Error");
       }

       return contents;

    } // getFileContents
    
    // checks if valid 4 letter word
 	public static boolean isValidWord(String word) {

 		//checks all words in dictionary
 		for (int i = 0; i < fileContents.length; i++) {
 			if (fileContents[i].contains(word)) {
 				return true;
 			}
 		}
 		return false;
 	}// isValidWord
 	
 	//checks if the new word has only one letter changed
 	public static boolean onlyOneLetterChanged() {
 		boolean isThreeLetters;
 		int sameLetters = 0;
 		if(currentWord.charAt(0) ==  previousWord.charAt(0)) {
 			sameLetters++;
 		}
 		if(currentWord.charAt(1) ==  previousWord.charAt(1)) {
 			sameLetters++;
 		}
 		if(currentWord.charAt(2) ==  previousWord.charAt(2)) {
 			sameLetters++;
 		}
 		if(currentWord.charAt(3) ==  previousWord.charAt(3)) {
 			sameLetters++;
 		}
 		if(sameLetters >= 3) {
 			isThreeLetters = false;
 		}
 		else {
 			isThreeLetters = true;
 		}
 		return isThreeLetters;
 	}

} // Even and Odd