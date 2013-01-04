package hamilton;

import java.util.ArrayList;

public class HamiltonPath {

	private ArrayList<int[]> allPaths = new ArrayList<int[]>();
	
	public ArrayList<int[]> getAllPaths() {
		return allPaths;
	}

	public static void main(String[] args){
		HamiltonPath obj = new HamiltonPath();

		int[][]x = {{0,1,0,1,0},  //Represent the graphs in the adjacent matrix forms
				{1,0,0,0,1},
				{0,0,0,1,0},
				{1,0,1,0,1},
				{0,1,0,1,0}};

		int[][]y = {{1,1,1,1,1,1,1,1,1,1},
				{1,1,1,1,1,1,1,1,1,1},
				{1,1,1,1,1,1,1,1,1,1},
				{1,1,1,1,1,1,1,1,1,1},
				{1,1,1,1,1,1,1,1,1,1},
				{1,1,1,1,1,1,1,1,1,1},
				{1,1,1,1,1,1,1,1,1,1},
				{1,1,1,1,1,1,1,1,1,1},
				{1,1,1,1,1,1,1,1,1,1},
				{1,1,1,1,1,1,1,1,1,1},
		};

		int[][]t = {{1,1,1,1,1,1,1,1,1,1},
				{1,1,1,1,1,1,1,1,1,1},
				{1,1,1,1,1,1,1,1,1,1},
				{1,1,1,1,1,1,1,1,1,1},
				{1,1,1,1,1,1,1,1,1,1},
				{1,1,1,1,1,1,1,1,1,1},
				{1,1,1,1,1,1,1,1,1,1},
				{1,1,1,1,1,1,1,1,1,1},
				{1,1,1,1,1,1,1,1,1,1},
				{1,1,1,1,1,1,1,1,1,1}
		};

		int[][]b = {{1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1},
				{1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1},
				{1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1},
				{1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1},
				{1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1},
				{1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1},
				{1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1},
				{1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1},
				{1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1},
				{1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1},
				{1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1},
				{1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1},
				{1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1},
				{1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1},
				{1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1},
				{1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1},
				{1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1},
				{1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1},
				{1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1},
				{1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1},
		};



		int[][]z = {{0,1,1,0,0,1},
				{1,0,1,0,0,0},
				{1,1,0,1,0,1},
				{0,0,1,0,1,0},
				{0,0,0,1,0,1},
				{1,0,1,0,1,0}};

		//obj.allHamiltonPath(y);   //list all Hamiltonian paths of graph
		obj.HamiltonPath(t,1);  //list all Hamiltonian paths start at point 1
		System.out.println(count);
		System.out.println(obj.allPaths.size());

	}

	static int len;
	int[]path;
	static int count = 0;    

	public void allHamiltonPath(int[][]x){  //List all possible Hamilton path in the graph
		len = x.length;
		path = new int[len];
		int i;
		for(i = 0;i<len;i++){ //Go through column(of matrix)
			path[0]=i+1;
			findHamiltonpath(x,0,i,0);
		}
	}

	public void HamiltonPath(int[][]x, int start){ //List all possible Hamilton path with fixed starting point
		len = x.length;
		path = new int[len];
		int i;
		for(i = start-1;i<start;i++){ //Go through row(with given column)
			path[0]=i+1;
			findHamiltonpath(x,0,i,0);
		}
	}

	private void findHamiltonpath(int[][]M,int x,int y,int l){

		int i;
		for(i=x;i<len;i++){         //Go through row

			if(M[i][y]!=0){      //2 point connect

				if(detect(path,i+1))// if detect a point that already in the path => duplicate 
					continue;

				l++;            //Increase path length due to 1 new point is connected 
				path[l]=i+1;    //correspond to the array that start at 0, graph that start at point 1
				if(l==len-1){//Except initial point already count =>success connect all point
					count++;   
					if (count ==1)
						System.out.println("");
			//		display(path);
					int[] possiblePath = path.clone();
					
					allPaths.add(possiblePath);
					l--;
					continue;
				}

				M[i][y]=M[y][i]=0;  //remove the path that has been get and
				findHamiltonpath(M,0,i,l); //recursively start to find new path at new end point
				l--;                // reduce path length due to the failure to find new path         
				M[i][y] = M[y][i]=1; //and tranform back to the inital form of adjacent matrix(graph)
			}
		}path[l+1]=0;    //disconnect two point correspond the failure to find the..   
	}                     //possible hamilton path at new point(ignore newest point try another one)         

	public void display(int[]x){

		
			System.out.print(count+" : ");
		for(int i:x){
					System.out.print(i+" ");
		}
			System.out.println();   
			
	}

	private boolean detect(int[]x,int target){ //Detect duplicate point in Halmilton path 
		boolean t=false;                        
		for(int i:x){
			if(i==target){
				t = true;
				break;
			}
		}
		return t;
	}  
}