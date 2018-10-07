/******************************
 * Jeremy Kramer
 * CS2 - Summer 2016
 * Topographical Path
 * 6/27/2016
 ******************************/

import java.util.*;
import java.io.*;

public class TopoPath {
	
	static boolean[][] matrix;
	static boolean[] visited;
	static int[] incoming;
	static ArrayList<Integer> combinations;
	static int n;
	
	
	public static boolean hasTopoPath(String filename) throws FileNotFoundException
	{
		//				       		INSTRUCTIONS                             //
		/***********************************************************************
		 * Open filename and process the graph it contanis. If the graph
		 * has a topopath return true. Otherwise, return false. The string
		 * filename will refer to an existing file, and it will follow the 
		 * format indicated above. You may have your method throw exceptions
		 * as necessary.
		 **********************************************************************/
		
		File input = new File(filename);
		Scanner in = new Scanner(input);
							
		//Read in the array/matrix size from the file and store it in the class' local variable 'n'
		n = in.nextInt();
		
		//Combinations ArrayList used for recording and printing the desired TopoSort.
		combinations = new ArrayList<>(n);
		
		//instantiate all of our needed arrays
		incoming = new int[n];
		visited = new boolean[n];
		matrix = new boolean[n][n];
		
		//Initialize all indices of our matrix to false
		for(int i = 0; i < n; i ++)
			for(int j = 0; j < n; j++)
				matrix[i][j] = false;
		
		//Cycle through each node in our graph and read the number of connections to that specific node.
		//Also initialize visited value of each node to false since we're cycling through the nodes anyways.
		for(int i = 0; i < n; i ++)
		{
			
			int numConnections = in.nextInt();
			visited[i]=false;
				
			//Cycles through each connection of a node 'i' and sets that node 
			//to true in our matrix. Note: in.nextInt()-1 is used since the node values 
			//are '1-n' and the indices of our matrix are '0-(n-1)'
			for(int j =0; j < numConnections; j++)
			{
				int setTrue = in.nextInt() - 1;
				matrix[i][setTrue] = true;
			}

		}
	
		//Cycle through our matrix and add up the number of edges pointing to node 
		//'i' from various nodes 'j'. Also initialize visited booleans to false.
		for(int i = 0; i < n; i++)
		{
			visited[i] = false;

			for(int j = 0; j < n; j ++)
			{
					incoming[i] += (matrix[j][i] ? 1 : 0);
			}
			
			//System.out.println("Incoming " + i + "= " + incoming[i]); //DEBUGGING//

		}
		
		//Now, we create a queue in which we can store our nodes so that we may check
		//each possible combination of topographical sort.
		Queue <Integer> nodes = new LinkedList<Integer>();
		
		//For each node in our graph, if it has no incoming nodes and has not yet been visited,
		//add the node to the queue.
		for(int i = 0; i < n; i ++)
			if(incoming[i] == 0 && !visited[i])
			{
				nodes.add(i);
			}
		
		//While there are still nodes within our queue, we have not yet finished the traversal.
		while(!nodes.isEmpty())
		{
			//we initialize our counter to 0 so we can keep track of the number of recursions
			//vs the number of nodes in our graph.
			int counter = 0;
			
			
			//we remove a node from the front of the queue, this way we can check all results
			//when using this node at this recursive step.
			int node = ((int) nodes.remove());
			visited[node] = true;
			
			//Call our "checkTopoPath method" passing the current node and an incremented counter.
			if(checkTopoPath(++counter, node))
			{	//if it evaluates to true, return true.
				return true;
			}
			else
			{
				//otherwise, we reset everything for the next node in our queue.
				for(int i = 0; i < n; i ++)
					visited[i] = false;
				combinations.clear();
			}
		}
		
		//If none of our searches have evaluated to true, return false.
		return false;
		
	}
	
	private static boolean checkTopoPath(int counter, int prev)
	{
		//add the previous node to our combinations array and then check to see
		//if we have encountered all of the nodes in our graph.
		combinations.add(prev);
		if(counter == n)
		{
			//If we have encountered all nodes in our graph, then we have found a valid
			//topological path.
			return true;
		}
		
		//create a new queue of nodes to check and both instantiate and initialize a boolean
		//flag to false.
		Queue <Integer> toCheck = new LinkedList<Integer>();
		boolean flag = false;
		
		//verify for each node in our graph:
		//	1) The previous node is pointing to it
		//	2) it now has no incoming edges
		//	3) it has not yet been visited.
		//If all of these conditions are met, then this node is valid for our
		//next traversal, so we add it to our queue.
		for(int i = 0; i < n; i ++)
			if((matrix[prev][i]) && (--incoming[i] == 0) && !visited[i])
				toCheck.add(i);
		
		//if there is a node in toCheck, we need to check that node.
		while(!toCheck.isEmpty())
		{
				//we remove the node and then recursively call this method.
				int node = ((int)toCheck.remove());
				flag = checkTopoPath(++counter, node);
			 
				//if our recursive call has set our flag to true, we return true.
				if(flag == true)
					return true;
				
				//otherwise, we continue trying nodes until there are no nodes left
				//in our queue.
		}
		
		//if we have tried everything and still not true, return false.
		return false;
	}
}
