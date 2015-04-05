/* Developed By: Snehal Sutar (svs130130) Swathi Rajamani(sxr132031)
 * Description:  given a directed graph as input, if the graph has uniform 
 * weights (i.e., same positive weights for all edges), then it runs breadth_first_search to find 
 * shorest paths.  Otherwise, if the graph is a directed, acyclic graph (DAG), 
 * then it runs DAG shortest paths.  Otherwise, if the graph has only 
 * nonnegative weights, then it runs Dijkstra's algorithm.  If all these test 
 * fail, then it runs the Bellman-Ford algorithm.  If the graph has negative 
 * cycles, then it prints the message "Booyah!!".
 */

package project7;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;
import java.util.TreeMap;

/**
 *
 * @author SnehalSutar
 */
public class svs130130_Project8{

    //Global variable Declaration.
    static TreeMap<String, Integer> orig_graph = new TreeMap();//from to weight
    static TreeMap<Integer, String> adj_list_incoming = new TreeMap();//node list
    static TreeMap<Integer, String> adj_list_outgoing = new TreeMap();//node list

    static int no_of_vertices;
    static int no_of_edges;
    static int source_node;
    static int destination_node;

    static String prev_arrow = "<--";
    static String next_arrow = "-->";
    static String tab = "\t";
    static String space = " ";
    static String nospace = "";

    static long startTime = 0, lastTime;

    static boolean all_edges_have_same_wt = true;
    static boolean all_edges_have_positive_wt = true;

    /**
     * *************************************************************************
     * Read the input file.
     */
    public static void readInputFromConsole() {
        int weight, prev_weight = 0;
        String nodes_from_to;
        Scanner scan = new Scanner(System.in);
        String line = scan.nextLine();
        String splitLine[] = line.split(space);
        no_of_vertices = Integer.parseInt(splitLine[0]);
        no_of_edges = Integer.parseInt(splitLine[1]);
        source_node = Integer.parseInt(splitLine[2]);
        destination_node = Integer.parseInt(splitLine[3]);

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
            if (i == 0) {
                prev_weight = weight;
            }

            if (prev_weight != weight) {
                all_edges_have_same_wt = false;
            }

            if (weight < 0) {
                all_edges_have_positive_wt = false;
            }

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

    public static boolean check_if_cycle_exists() {
        boolean cycle_exists = false;

        boolean dest_node_found = false;
        Queue<Integer> queue_list = new LinkedList<>();
        HashMap<Integer, String> hm_visited = new HashMap<>();
        HashMap<Integer, Integer> hm_prev_node = new HashMap<>();
        int push_node;
        String split_node[], list_of_outgoing_nodes;
        //----------------------------------------------------------------------
        queue_list.add(source_node);
        hm_visited.put(source_node, "true");

        while (!queue_list.isEmpty()) {
            int node = queue_list.remove();
            list_of_outgoing_nodes = adj_list_outgoing.get(node);
            split_node = list_of_outgoing_nodes.split(next_arrow);
            for (String split_node1 : split_node) {
                push_node = Integer.parseInt(split_node1);
                if (!hm_visited.containsKey(push_node)) {
                    queue_list.add(push_node);
                    hm_prev_node.put(push_node, node);
                    hm_visited.put(push_node, "true");

                    if (push_node == destination_node) {
                        dest_node_found = true;
                        break;
                    }
                } else {
                    cycle_exists = true;
                }
            } //end for loop.
            if (dest_node_found) {
                break;
            }
        }//end while loop.

        return cycle_exists;
    }

    /***************************************************************************
     * BREADTH FIRST SEARCH
     **************************************************************************/
    public static void breadth_first_search() {
        Queue<Integer> queue_list = new LinkedList<>();
        HashMap<Integer, String> hm_visited = new HashMap<>();
        HashMap<Integer, Integer> hm_prev_node = new HashMap<>();
        HashMap<Integer, Integer> hm_min_wt = new HashMap<>();
        int push_node, curr_wt;
        String split_node[], list_of_outgoing_nodes;
        //----------------------------------------------------------------------
        for (int temp_dest = 1; temp_dest <= no_of_vertices; temp_dest++) {
            queue_list.removeAll(queue_list);
        }
        queue_list.add(source_node);
        hm_visited.put(source_node, "true");
        hm_min_wt.put(source_node, 0);

        while (!queue_list.isEmpty()) {
            int node = queue_list.remove();
            list_of_outgoing_nodes = adj_list_outgoing.get(node);
            split_node = list_of_outgoing_nodes.split(next_arrow);
            for (String split_node1 : split_node) {
                push_node = Integer.parseInt(split_node1);
                String edge = node + space + push_node;
                //Get the weight of the new edge and weight from source till 
                //the previous edge and store it in current weight. 
                curr_wt = orig_graph.get(edge) + hm_min_wt.get(node);

                if (!hm_visited.containsKey(push_node)) {
                    queue_list.add(push_node);
                    hm_prev_node.put(push_node, node);
                    hm_visited.put(push_node, "true");
                    hm_min_wt.put(push_node, curr_wt);
                }//If the node is already visited, then check the minimum wt
                //stored for the node, if it more than the current node,then
                //update the minimum weight with current weight.
                else if (hm_min_wt.get(push_node) > curr_wt) {
                    hm_min_wt.put(push_node, curr_wt);
                    hm_prev_node.put(push_node, node);
                }
            } //end for loop.
        }//end while loop.

        //PRINT THE OUTPUT
        lastTime = System.currentTimeMillis();
        long total_time = lastTime - startTime;
        System.out.println("BFS " + hm_min_wt.get(destination_node) + " " + total_time);
        if (no_of_vertices <= 100) {
            for (int st_node = 1; st_node <= no_of_vertices; st_node++) {
                String min_wt;
                String prev_node;

                if (hm_min_wt.containsKey(st_node)) {
                    min_wt = hm_min_wt.get(st_node).toString();
                } else {
                    min_wt = "INF";
                }

                if (hm_prev_node.containsKey(st_node)) {
                    prev_node = hm_prev_node.get(st_node).toString();
                } else {
                    prev_node = "-";
                }
                System.out.println(st_node + space + min_wt + space + prev_node);
            }
        }
    }

    /***************************************************************************
     * DIJKISTRA'S ALGORITHM
     **************************************************************************/
    public static void dijkstras_algo() {
        //Local variable declaration.
        Queue<Integer> queue_list = new LinkedList<>();
        HashMap<Integer, String> hm_visited = new HashMap<>();
        HashMap<Integer, Integer> hm_prev_node = new HashMap<>();
        HashMap<Integer, Integer> hm_min_wt = new HashMap<>();
        int push_node, curr_wt;
        String split_node[], list_of_outgoing_nodes;
        //----------------------------------------------------------------------

        //Add the first node in the queue, make it visible and its minimum wight
        //as zero for the initial node. 
        queue_list.add(source_node);
        hm_visited.put(source_node, "true");
        hm_min_wt.put(source_node, 0);
        //Add the neighbours of the source node to the queue and keep on 
        //processing till the queue is empty.
        while (!queue_list.isEmpty()) {
            //Take out the 1st element inserted to the queue and process. 
            int node = queue_list.remove();
            //Get the neighbours of the queue.
            list_of_outgoing_nodes = adj_list_outgoing.get(node);
            //Continue only if the node is having neighbours.
            if (list_of_outgoing_nodes != null) {
                //Store the neighbours in an array.
                split_node = list_of_outgoing_nodes.split(next_arrow);
                //Process for each of its neighbour.
                for (String split_node1 : split_node) {
                    push_node = Integer.parseInt(split_node1);
                    String edge = node + space + push_node;
                    //Get the weight of the new edge and weight from source till 
                    //the previous edge and store it in current weight. 
                    curr_wt = orig_graph.get(edge) + hm_min_wt.get(node);
                    //If we are visiting the node for the first time then store 
                    //the current weeight as the minimum weight. 
                    if (!hm_visited.containsKey(push_node)) {
                        queue_list.add(push_node);
                        hm_prev_node.put(push_node, node);
                        hm_visited.put(push_node, "true");
                        hm_min_wt.put(push_node, curr_wt);
                    } //If the node is already visited, then check the minimum wt
                    //stored for the node, if it more than the current node,then
                    //update the minimum weight with current weight.
                    else if (hm_min_wt.get(push_node) > curr_wt) {
                        queue_list.add(push_node);//to be removed

                        hm_min_wt.put(push_node, curr_wt);
                        hm_prev_node.put(push_node, node);
                    }
                } //end for loop.
            }
        }//end while loop.

        //Print the output.
        lastTime = System.currentTimeMillis();
        long total_time = lastTime - startTime;
        System.out.println("Dij " + hm_min_wt.get(destination_node) + " " + total_time);
        if (no_of_vertices <= 100) {
            for (int st_node = 1; st_node <= no_of_vertices; st_node++) {
                String min_wt;
                String prev_node;

                if (hm_min_wt.containsKey(st_node)) {
                    min_wt = hm_min_wt.get(st_node).toString();
                } else {
                    min_wt = "INF";
                }

                if (hm_prev_node.containsKey(st_node)) {
                    prev_node = hm_prev_node.get(st_node).toString();
                } else {
                    prev_node = "-";
                }
                System.out.println(st_node + space + min_wt + space + prev_node);
            }
        }
    }

    /***************************************************************************
     * DIRECTED ACYCLIC GRAPH..
     **************************************************************************/
    public static void dag() {
        //Local variable declaration.
        Queue<Integer> queue_list = new LinkedList<>();
        HashMap<Integer, String> hm_visited = new HashMap<>();
        HashMap<Integer, Integer> hm_prev_node = new HashMap<>();
        HashMap<Integer, Integer> hm_min_wt = new HashMap<>();
        int push_node, curr_wt;
        String split_node[], list_of_outgoing_nodes;
        //----------------------------------------------------------------------

        //Add the first node in the queue, make it visible and its minimum wight
        //as zero for the initial node. 
        queue_list.add(source_node);
        hm_visited.put(source_node, "true");
        hm_min_wt.put(source_node, 0);
        //Add the neighbours of the source node to the queue and keep on 
        //processing till the queue is empty.
        while (!queue_list.isEmpty()) {
            //Take out the 1st element inserted to the queue and process. 
            int node = queue_list.remove();
            //Get the neighbours of the queue.
            list_of_outgoing_nodes = adj_list_outgoing.get(node);
            //Continue only if the node is having neighbours.
            if (list_of_outgoing_nodes != null) {
                //Store the neighbours in an array.
                split_node = list_of_outgoing_nodes.split(next_arrow);
                //Process for each of its neighbour.
                for (String split_node1 : split_node) {
                    push_node = Integer.parseInt(split_node1);
                    String edge = node + space + push_node;
                    //Get the weight of the new edge and weight from source till 
                    //the previous edge and store it in current weight. 
                    curr_wt = orig_graph.get(edge) + hm_min_wt.get(node);
                    //If we are visiting the node for the first time then store 
                    //the current weeight as the minimum weight. 
                    if (!hm_visited.containsKey(push_node)) {
                        queue_list.add(push_node);
                        hm_prev_node.put(push_node, node);
                        hm_visited.put(push_node, "true");
                        hm_min_wt.put(push_node, curr_wt);
                    } //If the node is already visited, then check the minimum wt
                    //stored for the node, if it more than the current node,then
                    //update the minimum weight with current weight.
                    else if (hm_min_wt.get(push_node) > curr_wt) {
                        queue_list.add(push_node);//to be removed

                        hm_min_wt.put(push_node, curr_wt);
                        hm_prev_node.put(push_node, node);
                    }
                } //end for loop.
            }
        }//end while loop.

        //Print the output
        lastTime = System.currentTimeMillis();
        long total_time = lastTime - startTime;
        System.out.println("DAG " + hm_min_wt.get(destination_node) + " " + total_time);
        if (no_of_vertices <= 100) {
            for (int st_node = 1; st_node <= no_of_vertices; st_node++) {
                String min_wt;
                String prev_node;

                if (hm_min_wt.containsKey(st_node)) {
                    min_wt = hm_min_wt.get(st_node).toString();
                } else {
                    min_wt = "INF";
                }

                if (hm_prev_node.containsKey(st_node)) {
                    prev_node = hm_prev_node.get(st_node).toString();
                } else {
                    prev_node = "-";
                }
                System.out.println(st_node + space + min_wt + space + prev_node);
            }
        }
    }

    public static void dag1() {
        //Local variable declaration.
        Queue<Integer> queue_list = new LinkedList<>();
        HashMap<Integer, String> hm_visited = new HashMap<>();
        HashMap<Integer, Integer> hm_prev_node = new HashMap<>();
        HashMap<Integer, Integer> hm_min_wt = new HashMap<>();
        ArrayList<Integer> sorted_vertex = new ArrayList<>();
        //----------------------------------------------------------------------

        //Order the edges from the source node and store them in a list. 
        queue_list.add(source_node);
        hm_visited.put(source_node, "true");
        sorted_vertex.add(source_node);
        while (!queue_list.isEmpty()) {
            int node = queue_list.remove();
            String edge = adj_list_outgoing.get(node);
            if (edge != null) {
                String vertexes[] = edge.split(next_arrow);
                for (String vertexe : vertexes) {
                    int next_node = Integer.parseInt(vertexe);
                    if (!hm_visited.containsKey(next_node)) {
                        hm_visited.put(next_node, "true");
                        queue_list.add(next_node);
                        sorted_vertex.add(next_node);
                    }
                }
            }
        }

//        for(int i=0;i<no_of_edges;i++){
        for (Integer vertex : sorted_vertex) {
            String outgoing_list = adj_list_outgoing.get(vertex);
            if (outgoing_list != null) {
                String split_edge[] = outgoing_list.split(next_arrow);
                for (String split_edge1 : split_edge) {
                    int next_vertex = Integer.parseInt(split_edge1);
                    String edge = vertex + space + next_vertex;
                    int weight = orig_graph.get(edge);
                    if (hm_min_wt.containsKey(next_vertex) && hm_min_wt.containsKey(vertex)) {
                        if (hm_min_wt.get(next_vertex) > (hm_min_wt.get(vertex) + weight)) {
                            hm_min_wt.put(next_vertex, (hm_min_wt.get(vertex) + weight));
                            hm_prev_node.put(next_vertex, vertex);
                        }
                    } else if (!hm_min_wt.containsKey(next_vertex) && hm_min_wt.containsKey(vertex)) {
                        hm_min_wt.put(next_vertex, (hm_min_wt.get(vertex) + weight));
                        hm_prev_node.put(next_vertex, vertex);
                    } else {
                        hm_min_wt.put(next_vertex, weight);
                        hm_prev_node.put(next_vertex, vertex);
                    }
                }

            }
        }

        lastTime = System.currentTimeMillis();
        long total_time = lastTime - startTime;
        System.out.println("DAG " + hm_min_wt.get(destination_node) + " " + total_time);
        for (int st_node = 1; st_node <= no_of_vertices; st_node++) {
            String min_wt;
            String prev_node;

            if (hm_min_wt.containsKey(st_node)) {
                min_wt = hm_min_wt.get(st_node).toString();
            } else {
                min_wt = "INF";
            }

            if (hm_prev_node.containsKey(st_node)) {
                prev_node = hm_prev_node.get(st_node).toString();
            } else {
                prev_node = "-";
            }
            System.out.println(st_node + space + min_wt + space + prev_node);
        }
    }

    
    /***************************************************************************
     * BELLMAN FORD ALGORITHM.
     **************************************************************************/
    public static void bellman_ford() {
        //Local variable declaration.
        Queue<Integer> queue_list = new LinkedList<>();
        HashMap<Integer, String> hm_visited = new HashMap<>();
        HashMap<Integer, Integer> hm_prev_node = new HashMap<>();
        TreeMap<Integer, Integer> hm_min_wt = new TreeMap<>();
        ArrayList<String> sorted_edges = new ArrayList<>();
        //----------------------------------------------------------------------

        //Order the edges from the source node and store them in a list. 
        queue_list.add(source_node);
        hm_visited.put(source_node, "true");
        while (!queue_list.isEmpty()) {
            int node = queue_list.remove();
            String edge = adj_list_outgoing.get(node);
            if (edge != null) {
                String vertexes[] = edge.split(next_arrow);
                for (String vertexe : vertexes) {
                    int next_node = Integer.parseInt(vertexe);
                    if (!hm_visited.containsKey(next_node)) {
                        hm_visited.put(next_node, "true");
                        queue_list.add(next_node);
                    }
                    sorted_edges.add(node + space + next_node);
                }
            }
        }

        //As per the sorted edges, run bellman ford algorithm.
        for (int vertex = 1; vertex <= no_of_vertices; vertex++) { //no of passes
            //For all edges in the graph.
            for (String edge : sorted_edges) {
                String split_edge[] = edge.split(space);
                int v1 = Integer.parseInt(split_edge[0]);
                int v2 = Integer.parseInt(split_edge[1]);
                int weight = orig_graph.get(edge);
                if (hm_min_wt.containsKey(v2) && hm_min_wt.containsKey(v1)) {
                    if (hm_min_wt.get(v2) > (hm_min_wt.get(v1) + weight)) {
                        hm_min_wt.put(v2, (hm_min_wt.get(v1) + weight));
                        hm_prev_node.put(v2, v1);
                    }
                } else if (!hm_min_wt.containsKey(v2) && hm_min_wt.containsKey(v1)) {
                    hm_min_wt.put(v2, (hm_min_wt.get(v1) + weight));
                    hm_prev_node.put(v2, v1);
                } else {
                    hm_min_wt.put(v2, weight);
                    hm_prev_node.put(v2, v1);
                }
            }
        }

        for (String edge : sorted_edges) {
            String split_edge[] = edge.split(space);
            int v1 = Integer.parseInt(split_edge[0]);
            int v2 = Integer.parseInt(split_edge[1]);
            int weight = orig_graph.get(edge);
            if (hm_min_wt.containsKey(v2) && hm_min_wt.containsKey(v1)) {
                if (hm_min_wt.get(v2) > (hm_min_wt.get(v1) + weight)) {
                    System.out.print("Booyahh!");
                    return;
                }
            }
        }
        //Print the output on the output screen.
        lastTime = System.currentTimeMillis();
        long total_time = lastTime - startTime;
        System.out.println("B-F " + hm_min_wt.get(destination_node) + " " + total_time);
        if (no_of_vertices <= 100) {
            for (int st_node = 1; st_node <= no_of_vertices; st_node++) {
                String min_wt;
                String prev_node;

                if (hm_min_wt.containsKey(st_node)) {
                    min_wt = hm_min_wt.get(st_node).toString();
                } else {
                    min_wt = "INF";
                }

                if (hm_prev_node.containsKey(st_node)) {
                    prev_node = hm_prev_node.get(st_node).toString();
                } else {
                    prev_node = "-";
                }
                System.out.println(st_node + space + min_wt + space + prev_node);
            }
        }
    }

    /***************************************************************************
     * MAIN
     * @param args
     **************************************************************************/
    public static void main(String[] args) {

        //Local Data Declaration
        boolean cycle_present;
        //----------------------------------------------------------------------
        //Get the input and store in the respective graphs and adjacency lists.
        readInputFromConsole();
        startTime = System.currentTimeMillis();
        //Check if there is any cycle present in the graph.
        cycle_present = check_if_cycle_exists();

        if (all_edges_have_same_wt) {
            breadth_first_search();
        } else if (!cycle_present) {
            dag();
//            dijkstras_algo();
//            bellman_ford();
        } else if (all_edges_have_positive_wt) {
            dijkstras_algo();
        } else {
            bellman_ford();
        }
    }

}
