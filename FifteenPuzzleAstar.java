import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Scanner;

public class FifteenPuzzleAstar {
	private int[][] matrix=new int[4][4];
	public int xeroX,xeroY;
	public String move="";
	public final static int [][] correctPuzzle=fillCorrectMatrix();
	
	/*(Method Description)
	 * This function loads the 2D array with goal state.*/
	private static int [][] fillCorrectMatrix() {
		int [][] data=new int[4][4];
		int flag=1;
		for(int i=0;i<4;++i) {
			for(int j=0;j<4;++j) {
				data[i][j]=flag;
				flag++;
			}
		}
		data[3][3]=0;
		return data;
	}
	
	/*(Method Description)
	 * This function asks user to load the puzzle(2D array) with initial state.*/
	private  int [][] fillPuzzleMatrix() {
		Scanner input= new Scanner(System.in);
		System.out.println("Please Enter the Numbers in the Puzzle");
		for(int i=0;i<4;i++) {
			for(int j=0;j<4;j++) {
				matrix[i][j]=input.nextInt();
				if(matrix[i][j] == 0){
					xeroX=i;
					xeroY=j;
	               }
			}
		}
		input.close();
		return matrix;
	}
	
	/*(Method Description)
	 * This function creates new node depending on the action chosen:right,left,up,down*/
	public static int[][] createNewNode(int currentMatrix [][],String nextMove,int xeroX,int xeroY){
		int newNode [][] = new int[4][4];
		for(int j=0;j<4;j++) {
			int[] dimension=currentMatrix[j];
			int length =dimension.length;
			newNode[j]=new int[length];
			System.arraycopy(dimension, 0, newNode[j], 0, length);
		}
		switch(nextMove) {
		case "LEFT":
			newNode [xeroX][xeroY] = newNode[xeroX][xeroY-1];
			newNode [xeroX][xeroY-1]=0;
			return newNode;
		case "RIGHT":
			newNode [xeroX][xeroY] = newNode[xeroX][xeroY+1];
			newNode [xeroX][xeroY+1]=0;
			return newNode;
		case "DOWN":
			newNode [xeroX][xeroY] = newNode[xeroX+1][xeroY];
			newNode [xeroX+1][xeroY]=0;
			return newNode;
		case "UP":
			newNode [xeroX][xeroY] = newNode[xeroX-1][xeroY];
			newNode [xeroX-1][xeroY]=0;
			return newNode;
		}
		return newNode;
	}
	
	/*(Method Description)
	 * This function calls createNewNode function to create new node for respective moves
	 *  if right,left,up,down moves are possible.*/
	public List<FifteenPuzzleAstar> findNodes() {
		ArrayList<FifteenPuzzleAstar> out = new ArrayList<FifteenPuzzleAstar>();
		if(xeroX>0) {
			FifteenPuzzleAstar newPuzzle=new FifteenPuzzleAstar();
			newPuzzle.xeroY=xeroY;
			newPuzzle.xeroX=xeroX-1;
			newPuzzle.move="U";
			newPuzzle.matrix=createNewNode(matrix, "UP", xeroX, xeroY);
			out.add(newPuzzle);
		}
		if(xeroX<3) {
			FifteenPuzzleAstar newPuzzle=new FifteenPuzzleAstar();
			newPuzzle.xeroY=xeroY;
			newPuzzle.xeroX=xeroX+1;
			newPuzzle.move="D";
			newPuzzle.matrix=createNewNode(matrix, "DOWN", xeroX, xeroY);
			out.add(newPuzzle);
		}
		if(xeroY>0) {
			FifteenPuzzleAstar newPuzzle=new FifteenPuzzleAstar();
			newPuzzle.xeroY=xeroY-1;
			newPuzzle.xeroX=xeroX;
			newPuzzle.move="L";
			newPuzzle.matrix=createNewNode(matrix, "LEFT", xeroX, xeroY);
			out.add(newPuzzle);
		}
		if(xeroY<3) {
			FifteenPuzzleAstar newPuzzle=new FifteenPuzzleAstar();
			newPuzzle.xeroY=xeroY+1;	
			newPuzzle.xeroX=xeroX;
			newPuzzle.move="R";
			newPuzzle.matrix=createNewNode(matrix, "RIGHT", xeroX, xeroY);
			out.add(newPuzzle);
		}
		return out;
	}
	
	/*(Method Description)
	 * This function calculates the Number of Misplaced Tiles for a given instance of Puzzle*/
	public int numberMisplacedTilesHeuristic() {
		int misplacedTiles=0;
		for(int x=0;x<4;x++) {
			for(int y=0;y<4;y++) {
				if((matrix[x][y]!=correctPuzzle[x][y]) &&(matrix[x][y] > 0) ) {
					misplacedTiles++;
				}
			}
		}
		return misplacedTiles;
	}
	
	
	/*(Method Description)
	 * This function is used to calculate Manhattan Distance for the given Puzzle instance.*/
	public int manhattanDistanceHeuristic() {
		int manhattanSum=0;
		for(int x=0;x<4;x++) {
			for(int y=0;y<4;y++) {
				int val=matrix[x][y];
				if(val!=0) {
					int correctX=(val-1)/4;
					int correctY=(val-1)%4;
					int dx=x-correctX;
					int dy=y-correctY;
					manhattanSum+=Math.abs(dx)+Math.abs(dy);
				}
			}
		}
		return manhattanSum;
	}
	
	/*(Method Description)
	 * This function is used to check if the goal state is reached.*/
	public boolean goalReached() {		
        int puzzleSolved [][]  = fillCorrectMatrix();
        for(int j=0;j<matrix.length;++j) {
			for(int i=0;i<matrix[j].length;++i) {
				if(matrix[j][i]!=puzzleSolved[j][i]) {
					return false;
				}
			}
		}
		return true;   
	}
	
	public int errorEstimateForMisplaced() {
		return this.numberMisplacedTilesHeuristic();
	}

	public int errorEstimateForManhattan() {
		return this.manhattanDistanceHeuristic();
	}
	
	/*(Method Description)
	 * This function prints the state of the puzzle in 2D matrix.*/
	private static void printMatrix(int [][] matrixToPrint,int row,int col) {
		for (int i = 0; i < row; i++)
        {
            for (int j = 0; j < col; j++)
            {
                System.out.print(matrixToPrint[i][j]+"\t");
            }             
            System.out.println();
        }	
	}
	
	/*(Method Description)
	 * This function solves the puzzle (2D array) according to A star logic using
	 * Number of Misplaced Tiles as Heuristic*/
	public void astarSolverWithMisplacedTiles() {
		HashMap<FifteenPuzzleAstar, Integer> depthLevel=new HashMap<FifteenPuzzleAstar, Integer>();
		final HashMap<FifteenPuzzleAstar, Integer> heuristic=new HashMap<FifteenPuzzleAstar, Integer>();
		HashMap<FifteenPuzzleAstar, FifteenPuzzleAstar> parent=new HashMap<FifteenPuzzleAstar, FifteenPuzzleAstar>();
		Comparator<FifteenPuzzleAstar> compare=new Comparator<FifteenPuzzleAstar>() {
			@Override
			public int compare(FifteenPuzzleAstar arg0, FifteenPuzzleAstar arg1) {
				return heuristic.get(arg0)-heuristic.get(arg1);
			}
			
		};
		PriorityQueue<FifteenPuzzleAstar> openList=new PriorityQueue<FifteenPuzzleAstar>(12000,compare);
		depthLevel.put(this, 0);
		parent.put(this, null);
		openList.add(this);
		heuristic.put(this, this.errorEstimateForMisplaced());
		int count=0;
		while(openList.size()>0) {
			FifteenPuzzleAstar currentPuzzleState=openList.remove();
			count++;
			if(currentPuzzleState.goalReached()) {
				System.out.println("Number of Nodes Expanded:"+count);
				ArrayList<FifteenPuzzleAstar> goal=new ArrayList<FifteenPuzzleAstar>();
				FifteenPuzzleAstar backTrackParent=currentPuzzleState;
				String moves="";
				while(backTrackParent!=null) {
					goal.add(0,backTrackParent);
					moves=moves+backTrackParent.move;
					backTrackParent=parent.get(backTrackParent);	
				}
				System.out.println("Moves Taken:"+new StringBuilder(moves).reverse().toString());
				break;
			}
			else {
				for(FifteenPuzzleAstar astr:currentPuzzleState.findNodes()) {
					if(!parent.containsKey(astr)) {
						parent.put(astr, currentPuzzleState);
						depthLevel.put(astr,depthLevel.get(currentPuzzleState)+1);
						int errorEstimate=astr.errorEstimateForMisplaced();
						heuristic.put(astr, depthLevel.get(currentPuzzleState)+1+errorEstimate);
						openList.add(astr);
					}
				}
			}
		}
		
	}
	
	/*(Method Description)
	 * This function solves the puzzle (2D array) according to A star logic using
	 * Manhattan Distance as Heuristic*/
	public void astarSolverWithManhattanDistance() {		
		HashMap<FifteenPuzzleAstar, Integer> depthLevel=new HashMap<FifteenPuzzleAstar, Integer>();
		final HashMap<FifteenPuzzleAstar, Integer> heuristic=new HashMap<FifteenPuzzleAstar, Integer>();
		HashMap<FifteenPuzzleAstar, FifteenPuzzleAstar> parent=new HashMap<FifteenPuzzleAstar, FifteenPuzzleAstar>();
		Comparator<FifteenPuzzleAstar> compare=new Comparator<FifteenPuzzleAstar>() {
			@Override
			public int compare(FifteenPuzzleAstar arg0, FifteenPuzzleAstar arg1) {
				return heuristic.get(arg0)-heuristic.get(arg1);
			}
			
		};
		PriorityQueue<FifteenPuzzleAstar> openList=new PriorityQueue<FifteenPuzzleAstar>(12000,compare);
		heuristic.put(this, this.errorEstimateForManhattan());
		parent.put(this, null);
		depthLevel.put(this, 0);
		openList.add(this);
		int count=0;
		while(openList.size()>0) {
			FifteenPuzzleAstar currentPuzzleState=openList.remove();
			count++;
			if(currentPuzzleState.goalReached()) {
				System.out.println("Number of Nodes Expanded:"+count);
				ArrayList<FifteenPuzzleAstar> goal=new ArrayList<FifteenPuzzleAstar>();
				FifteenPuzzleAstar backTrackParent=currentPuzzleState;
				String moves="";
				while(backTrackParent!=null) {
					goal.add(0,backTrackParent);
					moves=moves+backTrackParent.move;
					backTrackParent=parent.get(backTrackParent);	
				}
				System.out.println("Moves Taken:"+new StringBuilder(moves).reverse().toString());
				break;
			}
			else {
				for(FifteenPuzzleAstar astr:currentPuzzleState.findNodes()) {
					if(!parent.containsKey(astr)) {
						parent.put(astr, currentPuzzleState);
						depthLevel.put(astr,depthLevel.get(currentPuzzleState)+1);
						int errorEstimate=astr.errorEstimateForManhattan();
						heuristic.put(astr, depthLevel.get(currentPuzzleState)+1+errorEstimate);
						openList.add(astr);
					}
				}
			}
		}
		
	}
	
	public static void main(String[] args) {
		FifteenPuzzleAstar astar = new FifteenPuzzleAstar();
		int [][] initialmatrix=astar.fillPuzzleMatrix(); 
		
		System.out.println("Initial Matrix:");
		printMatrix(initialmatrix, 4, 4);
		System.out.println("\nSolving 15 Puzzle with A* using Number of Misplaced Tiles as Heuristic:");
		Long memUsedBefore= Runtime.getRuntime().totalMemory()-Runtime.getRuntime().freeMemory();
   		Long starttime=System.currentTimeMillis();
   		try {
		astar.astarSolverWithMisplacedTiles();
		Long endtime=System.currentTimeMillis();
   		Long memUsedAfter= Runtime.getRuntime().totalMemory()-Runtime.getRuntime().freeMemory();
   		Long memoryUsedActual=memUsedAfter-memUsedBefore;
   		System.out.println("Time taken:"+(endtime-starttime+"ms"));
		System.out.println("Memory Used:"+memoryUsedActual/1024+"KB");	
		System.out.println("\nSolving 15 Puzzle with A* using Manhattan Distance as Heuristic:");
		memUsedBefore= Runtime.getRuntime().totalMemory()-Runtime.getRuntime().freeMemory();
   		starttime=System.currentTimeMillis();
		astar.astarSolverWithManhattanDistance();
		endtime=System.currentTimeMillis();
   		memUsedAfter= Runtime.getRuntime().totalMemory()-Runtime.getRuntime().freeMemory();
   		memoryUsedActual=memUsedAfter-memUsedBefore;
   		System.out.println("Time taken:"+(endtime-starttime+"ms"));
		System.out.println("Memory Used:"+memoryUsedActual/1024+"KB");
		}
   		catch (OutOfMemoryError e) {
   			System.out.println("\nError: No Solution found as A star Algorithm ran out of Memory!\n");
		}
   			
	}
}
