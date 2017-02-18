/*

@authors: Andrew Oikonomidis & Andronikos Koutroumpelis

*/


import java.util.Scanner;

public class Main {

	public static void main(String[] args) {

        
		GamePlayer OPlayer = new GamePlayer(2, 'O');
		Board board = new Board();
		Scanner in = new Scanner(System.in);
		int steps=0;
        
        //Remove the comments for the O to play first
		//board.setLastLetterPlayed(Board.X);

        //While the game has not finished
		while(!board.isTerminal()) {
			
			//System.out.println("Evaluation is " + board.evaluate());
			switch (board.getLastLetterPlayed()) {

				case 'O': //Computer played last time
					board.print();
                	System.out.println("It's your turn! \nWhat is your column? ");
					int col = in.nextInt();
					col--;
					
					//Checking if column is full or out of bounds
					boolean invalid=false;
					if(col<0 || col>board.x-1) invalid=true;
					if(invalid==false) { //Making sure top[col] is not out of bounds
						if(board.top[col]>board.y-2) 
							invalid=true;
					}

					while(invalid==true) {
						System.out.println("Invalid move, choose another!\nWhat is your column? ");
						col = in.nextInt();
						col--;

						invalid=false;
						if(col<0 || col>board.x-1) invalid=true;
						if(invalid==false) {
							if(board.top[col]>board.y-2) 
								invalid=true;
						}
					}

					board.makeMove(col,'X');
					break;

				case 'X': //User played last time
                    System.out.println("\nO moves");
					Move OMove = OPlayer.MiniMax(board);
					//System.out.println(OMove.getCol());
					board.makeMove(OMove.getCol(), 'O');
					break;

				default:
					break;
			}

		}

		System.out.println("\nThe winner is " + board.getLastLetterPlayed() + "!");
		board.print();

	}

} // end of class Main
