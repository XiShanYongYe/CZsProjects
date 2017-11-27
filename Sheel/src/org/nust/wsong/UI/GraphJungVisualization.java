package org.nust.wsong.UI;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Paint;
import java.awt.Shape;
import java.awt.geom.Rectangle2D;

import javax.swing.JFrame;

import org.apache.commons.collections15.Transformer;
import org.nust.wsong.UI.layout.GraphLayout;
import org.nust.wsong.model.Edge;
import org.nust.wsong.model.Graph;
import org.nust.wsong.model.Node;

import edu.uci.ics.jung.graph.DirectedSparseGraph;
import edu.uci.ics.jung.graph.util.EdgeType;
import edu.uci.ics.jung.visualization.VisualizationViewer;
import edu.uci.ics.jung.visualization.control.DefaultModalGraphMouse;
import edu.uci.ics.jung.visualization.control.ModalGraphMouse;
import edu.uci.ics.jung.visualization.decorators.EdgeShape;
import edu.uci.ics.jung.visualization.renderers.DefaultEdgeLabelRenderer;

/**
 * @description 调用jung显示Graph
 * @author xxx
 */
public class GraphJungVisualization {

	private final Color NODE_COLOR = Color.white;
	private final Color EDGE_LABEL_SELECTED_COLOR = Color.black;
	

	
	private final int EDGES_LABEL_FONT_SIZE = 18;
	private final int VERTICES_LABEL_FONT_SIZE = 18;
	
	private Graph g;
	private VisualizationViewer<Node, Edge> vv;


	public GraphJungVisualization(Graph g) {
		this.g = g;
		initialize();
	}

	private VisualizationViewer<Node, Edge> initialize() {		
		
		edu.uci.ics.jung.graph.Graph<Node, Edge> uciGraph = new DirectedSparseGraph<>();
		
		//转换
		for(Node node:g.getNodes()){
			uciGraph.addVertex(node);
		}
		
		for(Edge e:g.getEdges()){
			uciGraph.addEdge(e, e.getSource(),e.getTarget(), EdgeType.DIRECTED);
		}
		
		vv = new VisualizationViewer<>(new GraphLayout<Node,Edge>(uciGraph));		
		DefaultModalGraphMouse<Node, Edge> gm = new DefaultModalGraphMouse<Node, Edge>();
		gm.setMode(ModalGraphMouse.Mode.PICKING);
		vv.setGraphMouse(gm);
		vv.setBackground(Color.white);
		
		//设置顶点的形状
		vv.getRenderContext().setVertexShapeTransformer(createVertexShapeTransformer());
		//设置顶点填充色
		vv.getRenderContext().setVertexFillPaintTransformer(createVertexFillPaintTransformer());
		//设置顶点label
		vv.getRenderContext().setVertexLabelTransformer(createVertexLabelTransformer());
		//设置顶点label字体
		vv.getRenderContext().setVertexFontTransformer(createVertexFontTransformer());
		
		//设置边的形状
		vv.getRenderContext().setEdgeShapeTransformer(new EdgeShape.Line<Node,Edge>());
		//设置边的label
//		vv.getRenderContext().setEdgeLabelTransformer(createEdgeLabelTransformer());
		//设置边label字体
		vv.getRenderContext().setEdgeFontTransformer(createEdgeFontTransformer());
		
		vv.getRenderContext().setEdgeLabelRenderer(new DefaultEdgeLabelRenderer(EDGE_LABEL_SELECTED_COLOR, false));
		
		vv.setPreferredSize(new Dimension(600, 400));
		
		return vv;

	}
	
	public Graph getGraph() {
		return this.g;
	}


	public void show(){
		VisualizationViewer<Node, Edge> vvViewer = initialize();
		JFrame frame = new JFrame("showGraph");
		frame.setSize(600, 500);
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().add(vvViewer);
		while(true);
	}
	

	public void setGraph(Graph g) {
		this.g = g;
		initialize();
		vv.repaint();
	}

	public VisualizationViewer<Node, Edge> getvv() {
		return this.vv;
	}


	
	private Transformer<Node,String> createVertexLabelTransformer() {
		return new Transformer<Node, String>() {
			@Override
			public String transform(Node Node) {
				return Node.getLabel();
			}
		};
	}
	
//	private Transformer<Edge,String> createEdgeLabelTransformer() {
//		return new Transformer<Edge, String>() {
//			@Override
//			public String transform(Edge edge) {
//				return "";
//			}
//		};
//	}
	
	private Transformer<Node,Paint> createVertexFillPaintTransformer() {
		return new Transformer<Node, Paint>() {
			@Override
			public Paint transform(Node Node) {
				return NODE_COLOR;
			}
		};
	}
	
	private Transformer<Node, Shape> createVertexShapeTransformer() {
		return new  Transformer<Node, Shape>() {
			@Override
			public Shape transform(Node Node) {
				return new Rectangle2D.Float(0, -20, 15, 40);
			}
		};
		
	}

	private Transformer<Edge, Font> createEdgeFontTransformer() {
		return new Transformer<Edge, Font>() {
			@Override
			public Font transform(Edge arg0) {
				return new Font("Default", 0, EDGES_LABEL_FONT_SIZE);
			}
			
		};
	}

	private Transformer<Node, Font> createVertexFontTransformer() {
		return new Transformer<Node, Font>() {
			@Override
			public Font transform(Node arg0) {
				return  new Font("Times New Roman", 0, VERTICES_LABEL_FONT_SIZE);
			}
		};
	}
}
