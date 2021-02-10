import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Arrays;
import java.util.Random;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

//developed by Greg Griffin
public class NumbersGame {
	JButton startGame, clearDisplay, displayStats, mostDifficultGames, numOfPlays, topPlayer;
	JTextField enterName, enterDifficulty, nextGuess;
	JTextArea tArea;
	JLabel statOptions;
	public int currDifficulty;
	public String currPlayerName;
	public String[] playerNames = new String[10];
	public int[] currGeneratedNumber;
	public int[] currGuess;
	public int currTurns = 0;
	public int totalGamesPlayed = 0;
	public int[] totalTurns = new int[10];
	public boolean gameInProgress = false;
	public boolean numOfPlaysClicked = false;
	public boolean mostDifficultGamesClicked = false;
	public boolean topPlayerClicked = false;
	public boolean levelOne = false;
	public boolean levelOneFirstTurn = false;
	
	
	public NumbersGame() {
		
		//main frame
		JFrame theFrame = new JFrame("Numbers Game");
		theFrame.setSize(980, 500);
		theFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		//top panel
		JPanel topPanel = new JPanel();
		theFrame.add(topPanel, BorderLayout.NORTH);
		
		enterName = new JTextField("Enter Name:", 10);
		topPanel.add(enterName, BorderLayout.NORTH);
		enterName.addKeyListener(new KeyBoardListener());
		
		enterDifficulty = new JTextField("Enter Difficulty:", 10);
		topPanel.add(enterDifficulty, BorderLayout.NORTH);
		enterDifficulty.addKeyListener(new KeyBoardListener());
		
		startGame = new JButton("Start Game");
		topPanel.add(startGame, BorderLayout.NORTH);
		startGame.addActionListener(new ButtonListener());
		
		nextGuess = new JTextField("Next Guess:", 10);
		topPanel.add(nextGuess, BorderLayout.NORTH);
		nextGuess.addKeyListener(new KeyBoardListener());
		
		tArea = new JTextArea(15, 20);
        theFrame.add(tArea, BorderLayout.CENTER);
        tArea.setText("Welcome to the Numbers Game. Enter your name and your desired difficulty (1+), then click 'Start Game' to begin.");
        
        JScrollPane scroll = new JScrollPane(tArea);
        theFrame.add(scroll, BorderLayout.CENTER);
		
		//bottom panel
        JPanel bottomPanel = new JPanel(new GridLayout(2,2));
        
        clearDisplay = new JButton("Clear Display");
        bottomPanel.add(clearDisplay);
        clearDisplay.addActionListener(new ButtonListener());
        
        statOptions = new JLabel("Statistic Options");
        bottomPanel.add(statOptions);
        
        displayStats = new JButton("Display Statistics");
        bottomPanel.add(displayStats);
        displayStats.addActionListener(new ButtonListener());
        
        //stats panel (in bottom panel)
        JPanel statsPanel = new JPanel();
        
        
        numOfPlays = new JButton("# of plays");
        statsPanel.add(numOfPlays);
        numOfPlays.addActionListener(new ButtonListener());
        
        mostDifficultGames = new JButton("Most Difficult Games");
        statsPanel.add(mostDifficultGames);
        mostDifficultGames.addActionListener(new ButtonListener());
        
        topPlayer = new JButton("Top Player");
        statsPanel.add(topPlayer);
        topPlayer.addActionListener(new ButtonListener());
        
        bottomPanel.add(statsPanel);
        
        theFrame.add(bottomPanel, BorderLayout.SOUTH);
        
        
		theFrame.setVisible(true);
	}//end of constructor
	
	class ButtonListener implements ActionListener{

		@Override
		//actionPerformed() method that performs functions for each button pressed
		public void actionPerformed(ActionEvent ae) {
			// TODO Auto-generated method stub
			JButton b = (JButton)ae.getSource();
			
			//Start Game Button
			if(b.getText().contentEquals("Start Game")) {
				if(isInteger(enterDifficulty.getText()) && !(enterName.getText().contains("Enter"))) {
					if(Integer.parseInt(enterDifficulty.getText()) == 1) {
						currDifficulty = Integer.parseInt(enterDifficulty.getText());
						currGeneratedNumber = new int[10];
						currGuess = new int[10];
						currPlayerName = enterName.getText();
						runGame(currDifficulty);
					}else if(Integer.parseInt(enterDifficulty.getText()) > 1) {
						currDifficulty = Integer.parseInt(enterDifficulty.getText());
						currGeneratedNumber = new int[currDifficulty];
						currGuess = new int[currDifficulty];
						currPlayerName = enterName.getText();
						runGame(currDifficulty);
					}else {
						JOptionPane.showMessageDialog(null, "Invalid Name or Difficulty: Enter a Player Name and a integer Difficulty greater than 0");
					}
				} else {
					JOptionPane.showMessageDialog(null, "Invalid Name or Difficulty: Enter a Player Name and a integer Difficulty greater than 0");
				}
			}
			if(b.getText().contentEquals("Clear Display")) {
				tArea.setText(null);
			}
			if(b.getText().contentEquals("# of plays")) {
				numOfPlaysClicked = true;
				tArea.append("# Of Plays selected, click display statistics to show\n");
			}
			if(b.getText().contentEquals("Most Difficult Games")) {
				mostDifficultGamesClicked = true;
				tArea.append("Most Difficult Games selected, click display statistics to show\n");
			}
			if(b.getText().contentEquals("Top Player")) {
				topPlayerClicked = true;
				tArea.append("Top Player selected, click display statistics to show\n");
			}
			if(b.getText().contentEquals("Display Statistics")) {
				if(numOfPlaysClicked) {
					tArea.append("\nThe last game took " + totalTurns[totalGamesPlayed - 1] + " turns to complete\n");
					numOfPlaysClicked = false;
				}
				if(mostDifficultGamesClicked) {
					int turns = 0;
					int index = 0;
					String name;
					
					for(int i = 0; i < totalTurns.length; i++) {
						if(turns < totalTurns[i]) {
							turns = totalTurns[i];
							index = i;
						}
					}
					name = playerNames[index];
					tArea.append("\nThe most difficult game was played by " + name + " and took " + turns + " turns\n");
					mostDifficultGamesClicked = false;
				}
				//SORT totalTurns[] , then return sorted values with their corresponding names in playerNames
				if(topPlayerClicked) {
					tArea.append("\nTop Player Rankings\n");
					int[] sorted = new int[10];
					sorted = totalTurns.clone();
					Arrays.parallelSort(sorted);
					
					int rank = 1;
					for(int i = 0; i < sorted.length; i++) {
						for(int j = 0; j < totalTurns.length; j++) {
							if(sorted[i] == totalTurns[j]) {
								if((sorted[i] != 0) && playerNames[j] != null) {
										tArea.append("\n\nRank #" + rank + ": " + playerNames[j] + " : " + totalTurns[j] + " turns");
										rank++;
								}
							}
						}
					}
					
					topPlayerClicked = false;
				}
			}

		}//end of actionPerformed()
		
		//method to reverse an array, used to calculate top player rankings
		public void reverseArr(int[] array) { 
			if (array == null || array.length < 2) { return; } 
			for (int i = 0; i < array.length / 2; i++) {
				int temp = array[i]; array[i] = array[array.length - 1 - i]; 
				array[array.length - 1 - i] = temp; 
			}
		}
		
		//method to check if a String contains only an integer value
		public boolean isInteger(String s) {
			  boolean result = false;
			  try {
			    Integer.parseInt(s);
			    result = true;
			  } catch (NumberFormatException nfe) {
			    // no need to handle the exception
			  }
			  return result;
			}
		
		//method that generates an array of random numbers based on the parameter difficulty
		public int[] generate(int difficulty) {
			Random rand = new Random();
				for(int i = 0; i < difficulty; i++) {
					currGeneratedNumber[i] = rand.nextInt(10);
				}
				return currGeneratedNumber;
		}
		
		//method that checks the difficulty and prints instructions to start the numbers game. If diff < 1, generate is called to create the computer's numbers
		public void runGame(int diff) {
			if(diff == 1) {
				gameInProgress = true;
				tArea.append("\nYou have chosen difficulty level 1. Enter a number in the 'Next Guess' box and hit enter.\n "
						+ "The game will then generate a number between 0 and your number.\nGuess what the number is and you win! \nFor each guess you enter in the "
						+ "'Next Guess' textbox, the game will tell you if its number is greater than or lower than your guess.\n");
				levelOne = true;
				levelOneFirstTurn = true;
				
			} else {
				generate(diff);
				gameInProgress = true;
				tArea.append("The game has generated " + diff + " digits. Guess what they are and you win! \nFor each guess you enter, the game will" +
				" give you a clue depending on how close your guess is to the correct answer.\n\nThe clues follow the format ' (x,x) ' , with the " +
				"first number telling you how many digits you guessed correctly,\nand the second number telling you how many digits you guessed " +
				"in the correct order.\n\nEnter your guesses in the format ' x,x... ' in the above 'Next Guess' textbox, then hit enter...\n\n");
				
				
			}
		}
		
	}//end of ButtonListener class
	
	class KeyBoardListener implements KeyListener{

		@Override
		public void keyTyped(KeyEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		//method that keeps track of the next guess text box, and depending on the difficulty of the current game, generates clues until the game is won. 
		public void keyPressed(KeyEvent ke) {
			// TODO Auto-generated method stub
			if(ke.getKeyCode() == KeyEvent.VK_ENTER) {
				JTextField c = (JTextField) ke.getSource();
				String s = c.getText();
				
				if(c.equals(nextGuess)) {
					if(levelOne) {
						if(levelOneFirstTurn) {
							generateLevelOne(s);
							tArea.append("\nThe game has generated a number between 0 and " + s + ". Start guessing!\n");
							levelOneFirstTurn = false;
						}else {
							currTurns++;
							tArea.append("Guess: " + s + "\nClue: " + generateLevelOneClue(s) + "\n\n");
							if(gameInProgress == false) {
								levelOne = false;
								totalTurns[totalGamesPlayed] = currTurns;
								currTurns = 0;
								currDifficulty = 0;
								playerNames[totalGamesPlayed] = currPlayerName;
								totalGamesPlayed++;
								tArea.append("\n----------CONGRATULATIONS, YOU WON!----------\n\n\nYou can view statistics or clear the display below\n\n");
							}
						}
						
					} else {
						currTurns++;
						tArea.append("Guess: " + s + "\nClue: " + generateClue(s) + "\n\n");
						if(gameInProgress == false) {
							totalTurns[totalGamesPlayed] = currTurns;
							currTurns = 0;
							currDifficulty = 0;
							playerNames[totalGamesPlayed] = currPlayerName;
							totalGamesPlayed++;
							tArea.append("\n----------CONGRATULATIONS, YOU WON!----------\n\n\nYou can view statistics or clear the display below\n\n");
						}
					}
				}
				if(c.equals(enterName)) {
					JOptionPane.showMessageDialog(null, "Name saved. Make sure your difficulty is entered, then click 'Start Game' to begin");
				}
				if(c.equals(enterDifficulty)) {
					JOptionPane.showMessageDialog(null, "Difficulty saved. Make sure your name is entered, then click 'Start Game' to begin");
				}
			}
		}

		@Override
		public void keyReleased(KeyEvent e) {
			// TODO Auto-generated method stub
			
		}
		
		//method that generates the random number for level one numbers games
		public int generateLevelOne(String input) {
			Random rand = new Random();
			int firstNum;
			try{
				firstNum = Integer.parseInt(input);
			} catch (NumberFormatException nfe) {
			    // no need to handle the exception
				return 0;
			  }
			
			currGeneratedNumber[totalGamesPlayed] = rand.nextInt(firstNum);
			return currGeneratedNumber[totalGamesPlayed];
		}
		
		//method that generates a clue for each user guess for level one numbers games
		public String generateLevelOneClue(String input) {
			int guess;
			try{
				guess = Integer.parseInt(input);
			} catch (NumberFormatException nfe) {
			    // no need to handle the exception
				return "Invalid guess, unable to create a clue";
			  }
			if(guess < currGeneratedNumber[totalGamesPlayed]) {
				return "Greater Than";
			}
			if(guess > currGeneratedNumber[totalGamesPlayed]) {
				return "Less Than";
			}
			if(guess == currGeneratedNumber[totalGamesPlayed]) {
				gameInProgress = false;
				return "CORRECT!";
			}
			else {
				return "Invalid Entry. Enter a whole integer value";
			}
		}
		
		//method that generates clues for each user guess for levels >1 of numbers games, and decides if the game is completed or not
		public String generateClue(String input) {
			
			//take input, convert to String, then convert to Char array, then insert into currGuess array
			String halfConvert2 = input.replaceAll("[()]", "");
			String convertedGuess = halfConvert2.replaceAll(",", "");
			
			//test if the guess is computable
			try{
				Integer.parseInt(convertedGuess);
			} catch (NumberFormatException nfe) {
			    // no need to handle the exception
				return "Invalid guess, unable to create a clue";
			  }
			char[] chArray;
			chArray = convertedGuess.toCharArray(); 
			
			for(int i = 0; i < currGeneratedNumber.length; i++) {
				currGuess[i] = Character.getNumericValue(chArray[i]);
			}
			
			//compare currGuess[] to currGeneratedNumber[]
			int totalCorrect = 0;
			int totalOrderCorrect = 0;
			
			for(int i = 0; i < currGuess.length; i++) {
				for(int j = 0; j < currGeneratedNumber.length; j++) {
					if(i == j) {
						if(currGuess[i] == currGeneratedNumber[j]) {
							totalCorrect++;
							totalOrderCorrect++;
							break;
						}
					}
					if(currGuess[i] == currGeneratedNumber[j]) {
						totalCorrect++;
					}
				}
			}
			if(((totalCorrect == currGuess.length) && (totalOrderCorrect == (currGuess.length - 1))) == true) {
				totalOrderCorrect++;
			}
			//from comparison, generate a String clue value, and return it
			String finalClue = "("+ totalCorrect +"," + totalOrderCorrect + ")";
			if((totalCorrect & totalOrderCorrect) == (currGuess.length)) {
				gameInProgress = false;
			}
			return finalClue;
		}
		
	}//end of KeyBoardListener class
}//end of NumbersGame class
