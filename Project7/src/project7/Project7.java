/**
 * *****************************************************************************
 * Developed by: Snehal Sutar
 * Net ID: svs130130 Program Name: Project7
 * Description: Get minimum spanning tree from directed weighted graph.
 */
package project7;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Map;
import java.util.Objects;
import java.util.Scanner;
import java.util.TreeMap;

/**
 *
 * @author Snehal
 */
public class Project7 {

    //Global variable Declaration.
    static TreeMap<String, Integer> orig_graph = new TreeMap();//from to weight
    static TreeMap<String, Integer> result_graph = new TreeMap();//from to weight
    static TreeMap<String, Integer> added_edges = new TreeMap();
    static TreeMap<Integer, String> adj_list_incoming = new TreeMap();//node list
    static TreeMap<Integer, String> adj_list_outgoing = new TreeMap();//node list

    static ArrayList<Integer> list_of_unvisited_nodes = new ArrayList<>();

    static int no_of_vertices;
    static int no_of_edges;
    static int start_node;

    static String prev_arrow = "<--";
    static String next_arrow = "-->";
    static String tab = "\t";
    static String space = " ";
    static String nospace = "";

    static long startTime = 0, lastTime;
    /**
     * ***************************************************************************
     * Get the vertex with minimum weight and return the vertex along with
     * weight corresponding to it.
     *
     * @param adj_list_key will be the vertex with INCOMING vertex.
     * @param adj_list will be list of vertices from which the edges are
     * arriving.
     * @return the key for original graph which is having the minimum weight.
     */
    private static String get_min_weight_node(Integer adj_list_key, String adj_list) {
        //Local variable declaration
        int min_wt;
        int temp_wt;
        String min_edge_from_to;
        String split_list[];
        String orig_key;
        //----------------------------------------------------------------------
        //Split the adjacency list into separate vertices. 
        split_list = adj_list.split(prev_arrow);
        //Join the FROM and TO vertices to get the weight and compare them.
        orig_key = split_list[0] + space + adj_list_key;
        //Store the 1st weight as minimum to use it for comparing later.
        min_wt = orig_graph.get(orig_key);
        //Save the whole key = EDGE(from to) into orig_key for reference. 
        min_edge_from_to = orig_key;

        //Among the remaining edges find the vertex with the minimum weight.
        for (int i = 1; i < split_list.length; i++) {
            //Join the FROM and TO vertices to get the weight and compare them.
            orig_key = split_list[i] + space + adj_list_key;
            //Store the weight int temp variable to use it for comparing later.
            temp_wt = orig_graph.get(orig_key);

            //Check if the new weight is less than the previous one.If yes then
            //store the new weight as minimum weight and store its orig_graph 
            //key.
            if (temp_wt < min_wt) {
                min_edge_from_to = orig_key;
                min_wt = temp_wt;
            }
        }
        //Return the vertex with minimum weight from ADJ_LIST_KEY.
        return min_edge_from_to;
    }

    /**
     * *************************************************************************
     * Create adjacency list for Depth First search traversal.
     */
    private static TreeMap create_adj_for_dfs(TreeMap<String, Integer> result_graph) {
        //Local variable declaration.
        String splitKey[];
        TreeMap<Integer, String> adj_list_of_res_graph = new TreeMap();//node list

        //----------------------------------------------------------------------
        //For every node in the resulting graph.
        for (Map.Entry<String, Integer> entry : result_graph.entrySet()) {
            //Get key and value of resulting graph.
            String key = entry.getKey();
            Integer value = entry.getValue();

            //Split the key to get two separat vertices of the key.
            splitKey = key.split(space);
            //Convert the first vertex of the vertex to integer and store as key 
            //value into adjacency list of resulting graph.
            int node = Integer.parseInt(splitKey[0]);
            //Check if the node is already present in the adjacency list. 
            //If not present then add a new node. 
            if (!adj_list_of_res_graph.containsKey(node)) {
                adj_list_of_res_graph.put(node, splitKey[1]);
            } //If the node is already present then append the next nodes to the
            //existing list. 
            else {
                String list = adj_list_of_res_graph.get(node);
                list = list + next_arrow + splitKey[1];
                adj_list_of_res_graph.put(node, list);
            }
        }
        return adj_list_of_res_graph;
    }

    /**
     * *************************************************************************
     * Mark all the nodes which are reachable from the root node.
     *
     * @param adj_list_of_res_graph
     * @param visited
     * @return
     */
    private static boolean[] check_if_all_nodes_are_visited(TreeMap<Integer, String> adj_list_of_res_graph, boolean[] visited, int start_node) {
        //Local variable declaration.
        String splitStr[];
        int node;

        //mark the start_node as reachable.
        visited[start_node] = true;

        //If there is no list for the node under consideration,return and do not
        //proceed.
        if (!adj_list_of_res_graph.containsKey(start_node)) {
            return visited;
        }
        //get the adjacency list of start_node.
        splitStr = adj_list_of_res_graph.get(start_node).split(next_arrow);

        for (String splitStr1 : splitStr) {
            node = Integer.parseInt(splitStr1);
            if (!visited[node]) {
                visited = check_if_all_nodes_are_visited(adj_list_of_res_graph, visited, node);
            }
        }

        return visited;
    }

    /**
     * *************************************************************************
     * Read the input file.
     */
    public static void readInputFromConsole() {
        int weight;
        String nodes_from_to;
        Scanner scan = new Scanner(System.in);
        String line = scan.nextLine();
        String splitLine[] = line.split(space);
        no_of_vertices = Integer.parseInt(splitLine[0]);
        no_of_edges = Integer.parseInt(splitLine[1]);
        start_node = Integer.parseInt(splitLine[2]);

        for (int i = 0; i < no_of_edges; i++) {
            //scan the next input line.
            line = scan.nextLine();
            //First split the line considering it is a tab separated file.
            splitLine = line.split(tab);
            //If the line is not divided into 3 parts then again split it,
            //now cosidering it as a space separated file.
            if (splitLine.length < 3) {
                splitLine = line.split(space);
            }
            //Remove all the extra spaces in order to avoid conflicts while 
            //converting it in the numeric format. 
            splitLine[0] = splitLine[0].replaceAll(space, nospace);
            splitLine[1] = splitLine[1].replaceAll(space, nospace);
            splitLine[2] = splitLine[2].replaceAll(space, nospace);

            //Add nodes to original graph
            nodes_from_to = splitLine[0] + space + splitLine[1];
            weight = Integer.parseInt(splitLine[2]);

            orig_graph.put(nodes_from_to, weight);

            //INCOMING ADJACENCY LIST.
            //Add to adjacency list.all adjacent nodes will be saved in the 
            //string in the format 5 <-- 1 <-- 2 etc.
            int node = Integer.parseInt(splitLine[1]);

            if (!adj_list_incoming.containsKey(node)) {
                adj_list_incoming.put(node, splitLine[0]);
            } else {
                String list = adj_list_incoming.get(node);
                list = list + prev_arrow + splitLine[0];
                adj_list_incoming.put(node, list);
            }

            //OUTGOING ADJACENCY LIST.
            //Add to adjacency list.all adjacent nodes will be saved in the 
            //string in the format 5 <-- 1 <-- 2 etc.
            node = Integer.parseInt(splitLine[0]);
            if (!adj_list_outgoing.containsKey(node)) {
                adj_list_outgoing.put(node, splitLine[1]);
            } else {
                String list = adj_list_outgoing.get(node);
                list = list + next_arrow + splitLine[1];
                adj_list_outgoing.put(node, list);
            }

        }
    }

    /**
     * ***************************************************************************
     * Create graph with 0 weight, convert the minimum weight vertex to 0
     * weight.
     */
    private static TreeMap<String, Integer> create_0_wt_graph(TreeMap<String, Integer> orig_graph, TreeMap<Integer, String> adj_list_incoming) {

        String orig_graph_key;
        TreeMap<String, Integer> result_graph = new TreeMap();//from to weight

        //For all nodes,get the vertex with the minimum weight and store the 
        //resulting vertex in resulting graph;
        for (Map.Entry<Integer, String> entry : adj_list_incoming.entrySet()) {
            Integer key = entry.getKey();
            String list = entry.getValue();
            if (key != start_node) {
                //Get the key of original graph with the minimum weight vertex.
                orig_graph_key = get_min_weight_node(key, list);

                //Store the edges along with weight in resulting graph.
                result_graph.put(orig_graph_key, orig_graph.get(orig_graph_key));
            }
        }
        return result_graph;
    }

    /**
     * *************************************************************************
     * List of unvisited nodes is given - cycle nodes Out of that get all the
     * incoming and outgoing edges.To determine the minimum of all and add it to
     * the resulting graph.
     *
     * @param list_of_unvisited_nodes
     */
    private static TreeMap<String, Integer> get_edges_from_univisited_nodes(ArrayList<Integer> list_of_unvisited_nodes, TreeMap<String, Integer> result_graph) {
        //Local variable declaration;
        TreeMap<String, Integer> temp_edges = new TreeMap();//from to weight
        String list, splitList[];
        String temp_edge;

        for (int i = 0; i < list_of_unvisited_nodes.size(); i++) {
            int node = list_of_unvisited_nodes.get(i);

            //For incoming edges
            if (adj_list_incoming.containsKey(node)) {
                list = adj_list_incoming.get(node);
                splitList = list.split(prev_arrow);
                for (String splitList1 : splitList) {
                    temp_edge = splitList1 + space + node;
                    //If the vertex not already added then add to the temp_edges. 
                    if (!temp_edges.containsKey(temp_edge)
                            && !result_graph.containsKey(temp_edge)
                            && !list_of_unvisited_nodes.contains(Integer.parseInt(splitList1))) {
                        temp_edges.put(temp_edge, orig_graph.get(temp_edge));
                    }
                }
            }
            //For outgoing edges.
            if (adj_list_outgoing.containsKey(node)) {
                list = adj_list_outgoing.get(node);
                splitList = list.split(next_arrow);
                for (String splitList1 : splitList) {
                    temp_edge = node + space + splitList1;
                    //If the vertex not already added then add to the temp_edges.
                    if (!temp_edges.containsKey(temp_edge) && !result_graph.containsKey(temp_edge)
                            && !list_of_unvisited_nodes.contains(Integer.parseInt(splitList1))) {
                        temp_edges.put(temp_edge, orig_graph.get(temp_edge));
                    }
                }
            }

        }

        return temp_edges;
    }

    /**
     * ***************************************************************************
     * Check which is the minimum weight vertex incoming and outgoing from the
     * cycle add that vertex to the resulting MST, and also remove the incoming
     * vertex on that particular node which is added.
     *
     * @param temp_edges
     */
    private static TreeMap<String, Integer> get_min_wt_edge_and_replace(TreeMap<String, Integer> temp_edges, ArrayList<Integer> nodes_in_cycle, TreeMap<String, Integer> result_graph) {

        int min_wt = Integer.MAX_VALUE;
        String edge = "", incoming_edges;
        String sep_vertex[];
        //For Incoming.
        //By looping on all the entries in the TreeMap find the lowest posible 
        //edge weight in the adjacent aedges found.
        for (Map.Entry<String, Integer> entry : temp_edges.entrySet()) {
            if (!added_edges.containsKey(entry.getKey())) {
                Integer weight = entry.getValue();
                String edge_vertex = entry.getKey();
                sep_vertex = edge_vertex.split(space);
                if (nodes_in_cycle.contains(Integer.parseInt(sep_vertex[1]))) {
                    if (weight < min_wt) {
                        min_wt = weight;
                        edge = entry.getKey();
                    }
                }
            }
        }

        if (min_wt != Integer.MAX_VALUE) {
            added_edges.put(edge, min_wt);

            //Get the to and from node,add it to the resultig graph, then search for
            //incoming vertex on that vertex and remove it from the resulting graph.
            String vertex1, vertex2, vertex, split_edge[], adj_vertex[], new_edge;
            split_edge = edge.split(space);
            vertex1 = split_edge[0];
            vertex2 = split_edge[1];
            if (nodes_in_cycle.contains(Integer.parseInt(vertex1))) {
                vertex = vertex1;
            } else {
                vertex = vertex2;
            }
            incoming_edges = adj_list_incoming.get(Integer.parseInt(vertex));
            split_edge = incoming_edges.split(prev_arrow);

            for (int i = 0; i < split_edge.length; i++) {
                new_edge = split_edge[i] + space + vertex;
                //Check if the vertex is the one in the cycle.
                if (nodes_in_cycle.contains(Integer.parseInt(split_edge[i]))) {
                    if (result_graph.containsKey(new_edge)) {
//                        result_graph.remove(new_edge);
                        result_graph.put(edge, min_wt);
                    }
                }
            }

        }

        //----------------------------------------------------------------------
        //For Outgoing.
        //By looping on all the entries in the TreeMap find the lowest posible 
        //edge weight in the adjacent aedges found.
        for (Map.Entry<String, Integer> entry : temp_edges.entrySet()) {
            if (!added_edges.containsKey(entry.getKey())) {
                Integer weight = entry.getValue();
                String edge_vertex = entry.getKey();
                sep_vertex = edge_vertex.split(space);
                if (nodes_in_cycle.contains(Integer.parseInt(sep_vertex[0]))) {
                    if (weight < min_wt) {
                        min_wt = weight;
                        edge = entry.getKey();
                    }
                }
            }
        }

        if (min_wt != Integer.MAX_VALUE) {
            added_edges.put(edge, min_wt);

            String outgoing_edges;
            //Get the to and from node,add it to the resultig graph, then search for
            //incoming vertex on that vertex and remove it from the resulting graph.
            String vertex1, vertex2, vertex, split_edge[], adj_vertex[], new_edge;
            split_edge = edge.split(space);
            vertex1 = split_edge[0];
            vertex2 = split_edge[1];
            if (nodes_in_cycle.contains(Integer.parseInt(vertex1))) {
                vertex = vertex1;
            } else {
                vertex = vertex2;
            }
            outgoing_edges = adj_list_outgoing.get(Integer.parseInt(vertex));
            split_edge = outgoing_edges.split(next_arrow);

            for (int i = 0; i < split_edge.length; i++) {
                new_edge = split_edge[i] + space + vertex;
                //Check if the vertex is the one in the cycle.
                if (nodes_in_cycle.contains(Integer.parseInt(split_edge[i]))) {
                    if (result_graph.containsKey(new_edge)) {
//                        result_graph.remove(new_edge);
                        result_graph.put(edge, min_wt);
                    }
                }
            }

        }
        return result_graph;
    }

    /**
     * *************************************************************************
     * Check if there is a cycle from node which is checked.
     *
     * @param get
     */
    private static ArrayList<Integer> check_cycle_from(Integer origin_vertex,
            Integer new_vertex, ArrayList<Integer> nodes_in_cycle, int count,
            TreeMap<String, Integer> result_graph,
            TreeMap<Integer, String> adj_list_outgoing) {
        //Local variable declaration.
        String outgoing_edges, vertex[], edge;
        //----------------------------------------------------------------------
        //
        if (count != 0) {
            if (Objects.equals(origin_vertex, new_vertex)) {
                nodes_in_cycle.add(new_vertex);
                return nodes_in_cycle;
            }
            if (nodes_in_cycle.contains(new_vertex)) {
                nodes_in_cycle.add(new_vertex);
                return nodes_in_cycle;
            }
        }
        //Get list of outgoing edges.
        if (adj_list_outgoing.containsKey(new_vertex)) {
            outgoing_edges = adj_list_outgoing.get(new_vertex);
            vertex = outgoing_edges.split(next_arrow);
            for (int i = 0; i < vertex.length; i++) {
                edge = new_vertex + space + vertex[i];
                if (result_graph.containsKey(edge)) {
                    //add the verrtex only if it is not present.
                    if (!nodes_in_cycle.contains(new_vertex)) {
                        nodes_in_cycle.add(new_vertex);
                    }
                    nodes_in_cycle = check_cycle_from(origin_vertex, Integer.parseInt(vertex[i]), nodes_in_cycle, ++count, result_graph, adj_list_outgoing);

                }
            }
        }
        return nodes_in_cycle;
    }

    public static void print_resulting_graph() {
        //Local variable Declaration.
        TreeMap<Integer, String> temp_result_graph = new TreeMap();//from to weight
        String new_key, split_key[];
        int count=0;
        BigDecimal bg_sum = new BigDecimal(0);
        BigDecimal bg_value;
        //----------------------------------------------------------------------

        for (Map.Entry<String, Integer> entry : result_graph.entrySet()) {
            String key = entry.getKey();
            Integer value = entry.getValue();
            split_key = key.split(space);
            if (Integer.parseInt(split_key[1]) != start_node) {
                new_key = split_key[1] + space + split_key[0];
                temp_result_graph.put(Integer.parseInt(split_key[1]), (split_key[0] + space + value));
//                total_weight = total_weight + value;
                bg_value = new BigDecimal(value);
                bg_sum = bg_sum.add( bg_value );
            }
            count++;
        }

//        System.out.println(total_weight);
        lastTime = System.currentTimeMillis();
        System.out.println(bg_sum + space + (lastTime-startTime));
        String split_value[];
        if(count<=50)
        for (Map.Entry<Integer, String> entry : temp_result_graph.entrySet()) {
            String key = entry.getKey() + "";
            split_value = entry.getValue().split(space);
            System.out.println(split_value[0] + space + key + space + split_value[1]);
        }
    }

    /**
     * *************************************************************************
     * Create the incoming adjacency list of the resulting graph.s
     *
     * @param result_graph
     * @return
     */
    private static TreeMap<Integer, String> create_adj_list_incoming(TreeMap<String, Integer> result_graph) {
        TreeMap<Integer, String> temp_adj_list_incoming = new TreeMap();//node list
        String splitStr[];
        int vertex2;
        String vertex1;

        for (Map.Entry<String, Integer> entry : result_graph.entrySet()) {
            String key = entry.getKey();
            Integer value = entry.getValue();
            splitStr = key.split(space);

            vertex1 = splitStr[0];
            vertex2 = Integer.parseInt(splitStr[1]);

            if (!temp_adj_list_incoming.containsKey(vertex2)) {
                temp_adj_list_incoming.put(vertex2, vertex1);
            } else {
                String temp_list = temp_adj_list_incoming.get(vertex2);
                temp_list = temp_list + prev_arrow + vertex1;
                temp_adj_list_incoming.put(vertex2, temp_list);
            }
        }
        return temp_adj_list_incoming;
    }

    /**
     * *************************************************************************
     * Creates a visited boolean array for each node and check if all the nodes
     * are visited.
     *
     * @param adj_list_of_res_graph
     * @return
     */
    private static boolean check_if_all_nodes_are_visited(TreeMap<Integer, String> adj_list_of_res_graph) {
        boolean all_visited;
        //----------------------------------------------------------------------
        //Once the adjacency list is completed then check if all the vertices 
        //are visited.
        boolean visited[] = new boolean[no_of_vertices + 1];
        visited = check_if_all_nodes_are_visited(adj_list_of_res_graph, visited, start_node);
        //----------------------------------------------------------------------

        all_visited = true;
        list_of_unvisited_nodes.clear();
        for (int i = 1; i < visited.length; i++) {

            if (!visited[i]) {
                list_of_unvisited_nodes.add(i);
                all_visited = false;
            }
        }

        return all_visited;
    }

    /**
     * *************************************************************************
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        //Local variable declaration.
        TreeMap<Integer, String> adj_list_of_res_graph;//
        TreeMap<String, Integer> temp_edges;//from to weight
        TreeMap<Integer, String> temp_adj_list_incoming;//node list
        TreeMap<String, Integer> temp_result_graph = new TreeMap();//from to weight
        ArrayList<Integer> nodes_in_cycle = new ArrayList<>();
        boolean all_visited = false;
        int count = 0;

        //----------------------------------------------------------------------
        readInputFromConsole();
        startTime = System.currentTimeMillis();
        result_graph = create_0_wt_graph(orig_graph, adj_list_incoming);

        while (all_visited == false) {//&& count < no_of_edges
            count++;
            temp_adj_list_incoming = create_adj_list_incoming(result_graph);
            if (count > 1) {
                result_graph = create_0_wt_graph(result_graph, temp_adj_list_incoming);
            }
            
            if (result_graph.size() < (no_of_vertices - 1)) {
                result_graph.putAll(temp_result_graph);
            } else {
//                added_edges.clear();
            }
            
            adj_list_of_res_graph = create_adj_for_dfs(result_graph);
            all_visited = check_if_all_nodes_are_visited(adj_list_of_res_graph);
            if (all_visited == true) {
                break;
            }
            //Get nodes in cycle
            for (int i = 0; i < list_of_unvisited_nodes.size(); i++) {
                nodes_in_cycle.clear();

                nodes_in_cycle = check_cycle_from(list_of_unvisited_nodes.get(i), list_of_unvisited_nodes.get(i), nodes_in_cycle, 0, result_graph, adj_list_of_res_graph);
                if (nodes_in_cycle.size() > 1) {
                    break;
                }

            }
            if (nodes_in_cycle.isEmpty() || nodes_in_cycle.size() == 1 || !Objects.equals(nodes_in_cycle.get(0), nodes_in_cycle.get(nodes_in_cycle.size() - 1))) {
                nodes_in_cycle.addAll(list_of_unvisited_nodes);
            }

            //If unvisited nodes are present there is a cycle.Create a list of temp
            //nodes to detect the minimum of all edges which will be added to the 
            //actual resulting graph.
            temp_edges = get_edges_from_univisited_nodes(nodes_in_cycle, result_graph);

            //Get the minimum vertex, add it to the resulting graph and remove the 
            //incoming vertex with on the same node which is added.
            temp_result_graph.putAll(result_graph);
            result_graph = get_min_wt_edge_and_replace(temp_edges, nodes_in_cycle, result_graph);

            adj_list_of_res_graph = create_adj_for_dfs(result_graph);
            all_visited = check_if_all_nodes_are_visited(adj_list_of_res_graph);
            if (all_visited == true) {
                break;
            }

//            if (count == 6000) {
//                break;
//            }
        }//end while.
        //----------------------------------------------------------------------
        //Print for testing purpose.
//        System.out.println("\nRESULTING GRAPH");
        //Print resulting graph as per proper format.
        print_resulting_graph();
    }

}
