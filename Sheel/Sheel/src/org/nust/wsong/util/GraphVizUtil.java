package org.nust.wsong.util;

import java.io.File;

import org.nust.wsong.model.Edge;
import org.nust.wsong.model.Graph;
import org.nust.wsong.model.Node;
import org.nust.wsong.view.GraphVizViewer;
/**
 * 
 * @author xxx	
 *
 */
public class GraphVizUtil {
	public static final String GraphVizUtil_Save_Png = "GraphVizUtil_Save_Png";
	public static final String GraphVizUtil_Save_Svg = "GraphVizUtil_Save_Svg";
	
	
	public static File save(Graph g,String type){
		  GraphViz gv = new GraphViz();
	      gv.addln(gv.start_graph());
	      for(Node n:g.getNodes()){
	    	  gv.add(n.getName()+"[shape=box]");
	      }
	      for(Edge e:g.getEdges()){
	    	  gv.addln(e.getSource().getName()+"->"+e.getTarget().getName());
	      }
	      gv.addln(gv.end_graph());
//	      System.out.println(gv.getDotSource());
	      
	      String type1 = "png";
	      if(GraphVizUtil.GraphVizUtil_Save_Png.equals(type)){
	    	  type1 = "png";
	      }
	      if(GraphVizUtil.GraphVizUtil_Save_Svg.equals(type)){
	    	  type1 = "svg";
	      }
	      File out = new File("C:/tmp/"+g.hashCode()+"." + type1);   
	      
	      gv.writeGraphToFile( gv.getGraph( gv.getDotSource(), type1 ), out );
	      
	      return out;
		
	}
	
	public static void show(Graph g){
		GraphVizViewer frame = new GraphVizViewer(g);
		frame.setVisible(true);
		frame.setSize(400,300);
	}
	

}
