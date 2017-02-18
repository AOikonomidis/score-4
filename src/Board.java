/*

@authors: Andrew Oikonomidis & Andronikos Koutroumpelis

*/


import java.util.ArrayList;

public class Board {
    
	public static final int x = 7;
	public static final int y = 6;
	private static final char EMPTY = ' ';

	private boolean win = false, pl = false;
	private int c,n,steps;
    
    //Immediate move that lead to this board
    public Move lastMove;

    /* Variable containing who played last; whose turn resulted in this board
     * Even a new Board has lastLetterPlayed value; it denotes which player will play first
     */
	private char lastLetterPlayed;

	public char [][] gameBoard;
	public int top[]; //Height of columns
	
	public Board() {

		lastMove = new Move();
		lastLetterPlayed = 'O'; //User is the first player
		gameBoard = new char[x][y];
		top = new int[x];

		for(int i=0; i<x; i++) {
			top[i]=-1;
			for(int j=0; j<y; j++) {
				gameBoard[i][j] = EMPTY;
			}
		}
	}
	
	public Board(Board board) {

		lastMove = board.lastMove;
		lastLetterPlayed = board.lastLetterPlayed;
		gameBoard = new char[x][y];
		top = new int[x];

		for(int i=0; i<x; i++) {
			top[i] = board.top[i];
			for(int j=0; j<y; j++) {
				gameBoard[i][j] = board.gameBoard[i][j];
			}
		}
	}
	
	public Move getLastMove() {
		return lastMove;
	}
	
	public char getLastLetterPlayed() {
		return lastLetterPlayed;
	}
	
	public char[][] getGameBoard() {
		return gameBoard;
	}

	public void setLastMove(Move lastMove) {
		this.lastMove.setRow(lastMove.getRow());
		this.lastMove.setCol(lastMove.getCol());
		this.lastMove.setValue(lastMove.getValue());
	}
	
	public void setLastLetterPlayed(char lastLetterPlayed) {
		this.lastLetterPlayed = lastLetterPlayed;
	}
	
	public void setGameBoard(char[][] gameBoard) {
		for(int i=0; i<x; i++) {
			for(int j=0; j<y; j++) {
				this.gameBoard[i][j] = gameBoard[i][j];
			}
		}
	}

    //Make a move; it places a letter in the board
	public void makeMove(int col, char letter) {
		//System.out.println(col + " " + top[col]);
		gameBoard[col][top[col]+1] = letter;
		top[col]++;
		lastMove = new Move(top[col], col);
		lastLetterPlayed = letter;
	}

    //Checks whether a move is valid; whether a square is empty
	public boolean isValidMove(int col) {
		if (top[col]<y-1) return true;
		return false;
	}

    /* Generates the children of the state
     * Any square in the board that is empty results to a child
     */
	public ArrayList<Board> getChildren(char letter) {

		ArrayList<Board> children = new ArrayList<Board>();

		for(int col=0; col<x; col++) { //possible error
			if(isValidMove(col)) {
				Board child = new Board(this);
				child.makeMove(col, letter);
				children.add(child);
			}
		}
		return children;
	}

	
	public int evaluate() {

		int value=0;

		//Checking verticaly
		int subsum=0;
		int substeps=0;
		
		for(int col=0; col<x; col++) {

			if(subsum>=16) subsum*=3; //Aim for victory
			if(substeps>=4) value+=subsum;

			subsum=0;
			substeps=0;

			for(int row=0;row<y;row++) {

				if(gameBoard[col][row]==EMPTY) {
					if(subsum>=16) subsum*=3; //Aim for victory
					value+=subsum;
					subsum=0;
					substeps=0;
					continue;
				}else if(gameBoard[col][row]!='X') {
					if(subsum>=16) subsum*=3; //Aim for victory
					if(substeps>=4) value+=subsum;
					subsum=0;
					substeps=-1;
				}else 
					subsum+=4;
				
				substeps++;
			}
		}

//Same for 'O'

		for(int col=0; col<x; col++) {

			if(subsum>=16) subsum*=3; //Aim for victory
			if(substeps>=4) value-=subsum;

			subsum=0;
			substeps=0;

			for(int row=0;row<y;row++) {

				if(gameBoard[col][row]==EMPTY) {
					if(subsum>=16) subsum*=3; //Aim for victory
					value-=subsum;
					subsum=0;
					substeps=0;
					continue;
				}else if(gameBoard[col][row]!='O') {
					if(subsum>=16) subsum*=3; //Aim for victory
					if(substeps>=4) value-=subsum;
					subsum=0;
					substeps=-1;
				}else 
					subsum+=4;
				
				substeps++;
			}
		}


		//Checking horizontaly
		subsum=0;
		substeps=0;
		for(int row=0;row<y;row++) {

			if(subsum>=16) subsum*=3;
			if(substeps>=4) value+=subsum;

			subsum=0;
			substeps=0;

			for(int col=0; col<x; col++) {
				
				if(gameBoard[col][row]==EMPTY) {
					subsum-=row+1; //The importance of the space is proportional to its height
					if(subsum<0) subsum=0;
				}

				else if(gameBoard[col][row]!='X') {
					if(subsum>=16) subsum*=3;
					if(substeps>=4) value+=subsum;
					subsum=0;
					substeps=0;
				}else
					subsum+=4;
					
				substeps++;
				
			}
		}

//Same for 'O'

		subsum=0;
		substeps=0;
		for(int row=0;row<y;row++) {

			if(subsum>=16) subsum*=3;
			if(substeps>=4) value-=subsum;

			subsum=0;
			substeps=0;

			for(int col=0; col<x; col++) {
				
				if(gameBoard[col][row]==EMPTY) {
					subsum-=row+1;
					if(subsum<0) subsum=0;
				}else if(gameBoard[col][row]!='O') {
					if(subsum>=16) subsum*=3;
					if(substeps>=4) value-=subsum;
					subsum=0;
					substeps=0;
				}else
					subsum+=4;
					
				substeps++;
				
			}
		}

		//Checking diagonaly, for 7X6 board

		subsum=0;
		substeps=0;
		int m,n,k;

		for(int i=0;i<x;i++) { //For diagonals beginning on the first row

			if(subsum>=16) subsum*=3;
			if(substeps>=4) value+=subsum;

			subsum=0;
			substeps=0;

			k=y-1;
			n=0;

			while(n<x && k>=0 && n>=0) {

				if(gameBoard[n][k]==EMPTY) {
					subsum-=k+1; 
					if(subsum<0) subsum=0;
				}

				else if(gameBoard[n][k]!='X') {
					if(subsum>=16) subsum*=3;
					if(substeps>=4) value+=subsum;
					subsum=0;
					substeps=0;
				}

				else
					subsum+=4;
				
				substeps++;
				
				k--;

				if(i<=4) n=i+k;
				else if(i>=4) n=i-k;
			}
        }

//Same for 'O'

        subsum=0;
		substeps=0;

		for(int i=0;i<x;i++) { //For diagonals beginning on the first row

			if(subsum>=16) subsum*=3;
			if(substeps>=4) value-=subsum;

			subsum=0;
			substeps=0;

			k=y-1;
			n=0;

			while(n<x && k>=0 && n>=0) {

				if(gameBoard[n][k]==EMPTY) {
					subsum-=k; //The importance of the space is proportional to its height
					if(subsum<0) subsum=0;
				}else if(gameBoard[n][k]!='O') {
					if(subsum>=16) subsum*=3;
					if(substeps>=4) value-=subsum;
					subsum=0;
					substeps=0;
				}else
					subsum+=4;
				
				substeps++;
				
				k--;

				if(i<=4) n=i+k;
				else if(i>=4) n=i-k;
			}
        }

                        
        subsum=0;
        substeps=0;

        for(int j=y-2;j>=y-3;j--) {		//For diagonals beginning on the second or third rows from the end and the first column

			if(subsum>=16) subsum*=3;
			if(substeps>=4) value+=subsum;

			subsum=0;
			substeps=0;

            n=0;

            while(n<x && j+n<y) {

                if(gameBoard[n][j-n]==EMPTY) {
                    subsum-=j-n; //The importance of the space is proportional to its height
                    if(subsum<0) subsum=0;
                }else if(gameBoard[n][j-n]!='X') {
					if(subsum>=16) subsum*=3;
                    if(substeps>=4) value+=subsum;
                    subsum=0;
                    substeps=0;
                }else
                    subsum+=4;
            

                substeps++;
                
                n++;

            }	

        }

//Same for 'O'

        subsum=0;
        substeps=0;

        for(int j=y-2;j>=y-3;j--) {		//For diagonals beginning on the second or third rows from the end and the first column

			if(subsum>=16) subsum*=3;
			if(substeps>=4) value-=subsum;

			subsum=0;
			substeps=0;

            n=0;

            while(n<x && j+n<y) {

                if(gameBoard[n][j-n]==EMPTY) {
                    subsum-=j-n; //The importance of the space is proportional to its height
                    if(subsum<0) subsum=0;
                }else if(gameBoard[n][j-n]!='O') {
					if(subsum>=16) subsum*=3;
                    if(substeps>=4) value-=subsum;
                    subsum=0;
                    substeps=0;
                }else
                    subsum+=4;
            
                substeps++;
                
                n++;

            }	

        }


        subsum=0;
        substeps=0;

        for(int j=y-2;j>=y-3;j--) { 	//For diagonals beginning on the second or third rows from the end and the last column

        	if(subsum>=16) subsum*=3;
			if(substeps>=4) value+=subsum;

        	subsum=0;
        	substeps=0;

            n=x-1;
            m=j;

            while(m>=0 && n>=0) {

                if(gameBoard[n][m]==EMPTY) {
                    subsum-=m;
                    if(subsum<0) subsum=0;
                }else if(gameBoard[n][m]!='X') {
					if(subsum>=16) subsum*=3;
                    if(substeps>=4) value+=subsum;
                    subsum=0;
                    substeps=0;
                }else
                    subsum+=4;
                
                substeps++;

                m--;
                n--;

            }

        }

//Same for 'O'

        subsum=0;
        substeps=0;

        for(int j=y-2;j>=y-3;j--) { 	//For diagonals beginning on the second or third rows from the end and the last column

        	if(subsum>=16) subsum*=3;
			if(substeps>=4) value-=subsum;

        	subsum=0;
        	substeps=0;

            n=x-1;
            m=j;

            while(m>=0 && n>=0) {

                if(gameBoard[n][m]==EMPTY) {
                    subsum-=m;
                    if(subsum<0) subsum=0;
                }

                else if(gameBoard[n][m]!='O') {
					if(subsum>=16) subsum*=3;
                    if(substeps>=4) value-=subsum;
                    subsum=0;
                    substeps=0;
                }

                else
                    subsum+=4;
                
                substeps++;

                m--;
                n--;

            }

        }

		return value;
	}

    /*
     * A state is terminal if there is a tic-tac-toe
     * or no empty tiles are available
     */

    public boolean isTerminal() {

    	if (lastMove.getCol() == -1 && lastMove.getRow() == -1) return false;

    	int c = lastMove.getCol();

		
		// Search right

        int steps=0;
		n=1;
		boolean win=true; //assuming game is over

		while(win==true && n<=3) {
			if(c+n>x-1)	{
				win=false;
				break;
			}

			if(gameBoard[c+n][top[c]]!=lastLetterPlayed) win=false; 
			else {
				steps++;
				n++;
			}
		}
	
		
		// Search left

		if(win==false) {
			n=1;
			win=true; 
			
			while(win==true && n<=3) {
				if(c-n<0) {
					win=false;
					break;
				}

				if(gameBoard[c-n][top[c]]!=lastLetterPlayed) win=false; 
				else {
					steps++;
					n++;
				}
			}

			if(steps>=3) win=true;
		}
	

		// Search down

		if(win==false) {
			n=1;
			win=true;

			while(win==true && n<=3) {
				if(top[c]-n<0) {
					win=false;
					break;
				}
				

				if(gameBoard[c][top[c]-n]!=lastLetterPlayed) win=false; 
				n++;
			}
		}


		// Search diagonally up-right

		if(win==false) {
			steps=0;
			n=1;
			win=true;
			
			while(win==true && n<=3) {
				if(c+n>x-1 || top[c]+n>y-1) {
					win=false;
					break;
				}
				
				if(gameBoard[c+n][top[c]+n]!=lastLetterPlayed) win=false; 
				else {
					steps++;
					n++;
				}
			}
		}

		
		

		// Search diagonally down-left

		if(win==false) {
			n=1;
			win=true;
			
			while(win==true && n<=3) {
				if(c-n<0 || top[c]-n<0) {
					win=false;
					break;
				}
				
				if(gameBoard[c-n][top[c]-n]!=lastLetterPlayed) win=false; 
				else {
					steps++;
					n++;
				}
			}

			if(steps>=3) win=true;
		}
		
		
		// Search diagonally up-left

		if(win==false) {
			steps=0;
			n=1;
			win=true;
			
			while(win==true && n<=3) {
				if(c-n<0 || top[c]+n>y-1) {
					win=false;
					break;
				}
				
				if(gameBoard[c-n][top[c]+n]!=lastLetterPlayed) win=false; 
				else {
					steps++;
					n++;
				}
			}

		}

		// Search diagonally down-right
		
		if(win==false) {
			n=1;
			win=true;
			
			while(win==true && n<=3) {
				if(c+n>x-1 || top[c]-n<0) {
					win=false;
					break;
				}
				
				if(gameBoard[c+n][top[c]-n]!=lastLetterPlayed) win=false; 
				else {
					steps++;
					n++;
				}
			}

			if(steps>=3) win=true;
		}

		return win;
    }
  
    //Prints the board
	public void print() {

		for(int col=y-1; col>=0; col--) {
			for(int row=0; row<=x-1; row++) {
				System.out.print("|");
				switch(gameBoard[row][col]) {
					case 'X': 
						System.out.print("X");
						break;
					case 'O': 
						System.out.print("O");
						break;
					case EMPTY: 
						System.out.print(" ");
						break;
					default:
						break;
				}
				System.out.print("|");
			}
			System.out.println();
		}

		System.out.print(" ");
		
		for(int i=1; i<=x; i++)
			System.out.print(i + "  ");
		System.out.print("\n\n");
	}

} // end of class Board