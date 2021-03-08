package change;

import java.io.Serializable;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Stack;

import graph.PDGActionNode;
import graph.PDGDataEdge;
import graph.PDGGraph;
import org.eclipse.jdt.core.dom.ASTNode;

import graph.PDGEdge;
import graph.PDGNode;

public class ChangeGraph implements Serializable {
	private static final long serialVersionUID = -874502848659906533L;

	private HashSet<ChangeNode> nodes = new HashSet<>();

	public ChangeGraph(PDGGraph pdg) {
		HashSet<PDGNode> changedNodes = pdg.getChangedNodes();
		HashSet<PDGNode> nonChangedNodes = new HashSet<>();
		if (changedNodes.isEmpty())
			return;
		HashMap<PDGNode, ChangeNode> map = new HashMap<>();

		for (PDGNode node : changedNodes) {
			if (node instanceof PDGActionNode){
				for (PDGEdge outEdge : node.getOutEdges()) {
					if (outEdge instanceof PDGDataEdge && ((PDGDataEdge) outEdge).getType() == PDGDataEdge.Type.DEFINITION){
						ChangeNode cn = new ChangeNode(outEdge.getTarget());
						map.put(outEdge.getTarget(), cn);
						nonChangedNodes.add(outEdge.getTarget());

						for (PDGEdge edge : outEdge.getTarget().getOutEdges()) {
							if (edge instanceof PDGDataEdge && ((PDGDataEdge) edge).getType() == PDGDataEdge.Type.MAP){
								ChangeNode cn1 = new ChangeNode(edge.getTarget());
								map.put(edge.getSource(), cn1);
								nonChangedNodes.add(edge.getSource());
							}
						}

						for (PDGEdge edge : outEdge.getTarget().getInEdges()) {
							if (edge instanceof PDGDataEdge && ((PDGDataEdge) edge).getType() == PDGDataEdge.Type.MAP){
								ChangeNode cn1 = new ChangeNode(edge.getSource());
								map.put(edge.getSource(), cn1);
								nonChangedNodes.add(edge.getSource());
							}
						}
					}
				}
			}
		}

		changedNodes.addAll(nonChangedNodes);

		for (PDGNode node : changedNodes) {
			ChangeNode cn = new ChangeNode(node);
			map.put(node, cn);
			nodes.add(cn);
		}

		for (PDGNode node : changedNodes) {
			ChangeNode cn = map.get(node);
			for (PDGEdge e : node.getInEdges()) {
				if (changedNodes.contains(e.getSource())) {
					new ChangeEdge(map.get(e.getSource()), cn, e);
				}
			}
		}
	}

	public HashSet<ChangeNode> getNodes() {
		return nodes;
	}

	public boolean isMultiGraph() {
		for (ChangeNode node : nodes) {
			HashSet<ChangeNode> s = new HashSet<>();
			for (ChangeEdge e : node.outEdges) {
				if (s.contains(e.target))
					return false;
				s.add(e.target);
			}
		}
		return true;
	}

	public boolean hasCycle() {
		HashSet<ChangeNode> sinks = new HashSet<>();
		for (ChangeNode n : nodes)
			if (n.inEdges.isEmpty()) {
				sinks.add(n);
			}
		if (sinks.isEmpty())
			return true;
		for (ChangeNode sink : sinks) {
			Stack<ChangeNode> stk = new Stack<>();
			stk.push(sink);
			HashSet<ChangeNode> visitedNodes = new HashSet<>();
			if (dfs(stk, visitedNodes))
				return true;
		}
		return false;
	}

	private boolean dfs(Stack<ChangeNode> stk, HashSet<ChangeNode> visitedNodes) {
		ChangeNode n = stk.peek();
		if (visitedNodes.contains(n)) {
			stk.pop();
			return false;
		}
		for (ChangeEdge e : n.outEdges) {
			if (e.target == n)
				return true;
			if (stk.contains(e.target))
				return true;
			stk.push(e.target);
			if (dfs(stk, visitedNodes))
				return true;
		}
		stk.pop();
		visitedNodes.add(n);
		return false;
	}

	public boolean hasMethods() {
		for (ChangeNode node : nodes) {
			if (node.astNodeType == ASTNode.METHOD_INVOCATION
					|| node.astNodeType == ASTNode.SUPER_METHOD_INVOCATION
					|| node.astNodeType == ASTNode.CLASS_INSTANCE_CREATION
					|| node.astNodeType == ASTNode.CONSTRUCTOR_INVOCATION
					|| node.astNodeType == ASTNode.SUPER_CONSTRUCTOR_INVOCATION
					|| node.astNodeType == ASTNode.TRY_STATEMENT)
				return true;
		}
		return false;
	}

	public boolean hasArrays() {
		for (ChangeNode node : nodes) {
			if (node.astNodeType == ASTNode.ARRAY_TYPE
					|| node.astNodeType == ASTNode.ARRAY_ACCESS
					|| node.astNodeType == ASTNode.ARRAY_CREATION
					|| node.astNodeType == ASTNode.ARRAY_INITIALIZER)
				return true;
		}
		return false;
	}

	public boolean hasForLoops() {
		for (ChangeNode node : nodes) {
			if (node.astNodeType == ASTNode.FOR_STATEMENT
					|| node.astNodeType == ASTNode.ENHANCED_FOR_STATEMENT
					|| node.astNodeType == ASTNode.ENHANCED_FOR_STATEMENT_WITH_ELSE
			||node.astNodeType == ASTNode.IF_STATEMENT)
				return true;
		}
		return false;
	}

	public String summarize() {
		int numOfEdges = 0;
		HashMap<String, Integer> edges = new HashMap<>(), nodes = new HashMap<>();
		for (ChangeNode node : this.nodes) {
			int c = 1;
			if (nodes.containsKey(node.type))
				c += nodes.get(node.type);
			nodes.put(node.type, c);
			numOfEdges += node.inEdges.size();
			for (ChangeEdge e : node.inEdges) {
				String label = e.getLabel();
				c = 1;
				if (edges.containsKey(label))
					c += edges.get(label);
				edges.put(label, c);
			}
		}
		String sum = "";
		sum += this.nodes.size();
		String[] types = { "a", "c", "d" };
		for (String type : types)
			sum += ":" + (nodes.containsKey(type) ? nodes.get(type) : 0);
		sum += ":" + numOfEdges;
		types = new String[] { "_cond_", "_control_", "_def_", "_dep_",
				"_para_", "_qual_", "_recv_", "_ref_" };
		for (String type : types)
			sum += ":" + (edges.containsKey(type) ? edges.get(type) : 0);
		return sum;
	}

	public int[] getChangeSizes() {
		int[] sizes = new int[] {0, 0};
		for (ChangeNode node : nodes)
			sizes[node.version]++;
		return sizes;
	}
}
