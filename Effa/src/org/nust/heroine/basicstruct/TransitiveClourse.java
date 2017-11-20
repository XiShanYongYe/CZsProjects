package org.nust.heroine.basicstruct;

public class TransitiveClourse {
//	求传递闭包
	PetriNet pn;
	int[][] adjacentMatrix;//邻接矩阵
	int[][] transitiveClourse;
	public TransitiveClourse(PetriNet pn){
		this.pn = pn;
		adjacentMatrix = new int [pn.getNodes().size()][pn.getNodes().size()];
		for(int i =0;i<pn.getNodes().size();i++){
			for(int j = 0 ;j<pn.getNodes().size();j++){
				if(i==j)
					adjacentMatrix[i][j]=0;
				else{
					for(Arc arc:pn.getArcs()){
						if(arc.getSource().getID().equalsIgnoreCase(pn.getNodes().get(i).getID())&&arc.getTarget().getID().equalsIgnoreCase(pn.getNodes().get(j).getID())){
							adjacentMatrix[i][j]=1;
						}
					}
				}
			}
		}
		transitiveClourse= Warshall(adjacentMatrix);

	}

	public int[][] Warshall(int[][] r){
		//三重循环实现的warshall算法
		int [][] temp = new int [pn.getNodes().size()][pn.getNodes().size()];
		for(int k = 0;k <pn.getNodes().size();k++){
			for(int i = 0;i<pn.getNodes().size();i++){
				for(int j =0;j<pn.getNodes().size();j++){
					temp[i][j] = (r[i][j])|(r[i][k]&r[k][j]);
				}
			}
			 for(int i=0;i<pn.getNodes().size();i++)
	             for(int j=0;j<pn.getNodes().size();j++)
	            	 r[i][j]=temp[i][j];
		}
		return r;
	}
	
	public void printMartix(int[][] matrix){
		for(int i  = 0;i<pn.getNodes().size();i++){
			System.out.print("\t"+pn.getNodes().get(i).getID());
		}
		System.out.println();
		
		for(int i  = 0;i<pn.getNodes().size();i++){
			System.out.print(pn.getNodes().get(i).getID());
			for(int j = 0;j<pn.getNodes().size();j++){
				System.out.print("\t"+matrix[i][j]);
			}
			System.out.println();
		}
	}
	
	public int[][] getTransitiveClourse(){
		return transitiveClourse;
	}
	public boolean isReachable(Node x,Node y){
		for(int i = 0;i<pn.getNodes().size();i++){
			if(pn.getNodes().get(i).getID().equalsIgnoreCase(x.getID())){
				for(int j = 0;j<pn.getNodes().size();j++){
					if(pn.getNodes().get(j).getID().equalsIgnoreCase(y.getID())){
						if(transitiveClourse[i][j]==1)
							return true;
					}
				}
			}
		}
		return false;
	}
}
