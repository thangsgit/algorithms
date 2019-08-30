import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Comparator;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;


/**
 * File Name: Hauffman.java
 *
 *
 *
 * @author Thangaraju Karuppusamy
 * @year 2019
 */

public class Hauffman {
  private String s; //original string
  private boolean show;
  private String dotfilename;
  private int nodeCnt = 0;
  private StringBuffer sb1;
  private BufferedWriter out;

  private String d; //decoded String
  private String r; //recovered String
  private HashMap<Character,Integer> hm;
  private PriorityQueue<Node> pq; //minHeap for Nodes
  private HashMap<Character,StringBuffer> hc;
  private Node root;

  private class Node {
    private char c ;
    private int freq;
    private Node left;
    private Node right;
    private int nodeNum;

    Node(char c,int freq,Node left,Node right) {
      this.c = c ;
      this.freq = freq;
      this.left = left;
      this.right = right;
      this.nodeNum = ++nodeCnt;
    }

    /**
     * to identify if it is a leaf
     */
    public boolean isLeaf() {
      return left==null && right==null;
    }

  }

  private class NodeComparator implements Comparator<Node> {

    @Override
    public int compare(Node x, Node y) {
      return x.freq-y.freq;
    }
  }

  public Hauffman(String s, boolean show, String dotfilename) {
    this.s = s;
    this.show = show;
    this.dotfilename = dotfilename;

    alg();
  }

  private void alg(){
    System.out.println("");
    System.out.println("+++++++++++++++++++++++++++++++++++++++");
    System.out.println("");
    setup();
    printFreq();
    root = buildTree();
    assignCode(root,new StringBuffer(""));
    printCode();
    createDot();
    System.out.println("===========Original String====================");
    System.out.println(s);
  }

  /*
   * Method to load the Map with chars and their frequencies
   */

  private void setup(){
    hm = new HashMap<Character,Integer>();
    hc = new HashMap<Character,StringBuffer>();
    sb1 = new StringBuffer("");
    for(int i=0; i < s.length(); i++){
      char c = s.charAt(i);

      if(hm.containsKey(c))
        hm.put(c,hm.get(c)+1);
      else
        hm.put(c,1);
    }
  }

  /*
   * Method to build the min heap tree using PriorityQueue
   * Time: O(n)
   * Space: O(n)
   */

  private Node buildTree(){
    pq = new PriorityQueue<Node>(hm.size(),new NodeComparator());

    System.out.println("=========Tree Built in this Order=========");
    //add all chars and frequencies
    for (Map.Entry<Character, Integer> m : hm.entrySet())
    {

      pq.add(new Node(m.getKey(),m.getValue(),null,null));
      System.out.println("Leaf :  Node "+nodeCnt+" Character is "+m.getKey().toString()+" Weight is "+m.getValue().toString());

    }

    //when PQ has more than 1 node, get two minimum frequency nodes and construct parent node
    while(pq.size() > 1){
      Node left = pq.poll(); //1st minimum becomes left
      Node right = pq.poll(); //2nd minimum becomes right
      int f = left.freq+right.freq;
      char c='#';
      pq.add(new Node(c,f,left,right));
      String s = "Left"+Character.toString(left.c)+"("+left.freq+") Right"+Character.toString(right.c)+"("+right.freq+")";

      System.out.println("Internal Node :  Node "+nodeCnt+" "+s+" Weight is "+f);
    }

    return pq.peek(); //Root Node
  }


  /*
   * Recursive method to assign the Hauffman code for each character
   */
  private void assignCode(Node n,StringBuffer sb){

    if ( n.isLeaf() ) {
      if(hm.size()==1)
        hc.put(n.c,new StringBuffer("0"));
      else
        hc.put(n.c, new StringBuffer(sb));
      //System.out.println(Character.toString(n.c) + " " + sb);
    }
    else {
      if ( n.left != null ) {
        assignCode(n.left, sb.append("0"));
        sb.deleteCharAt(sb.length() - 1);
      }
      if ( n.right != null ) {
        assignCode(n.right, sb.append("1"));
        sb.deleteCharAt(sb.length() - 1);
      }
    }
  }

  /*
   * Method to print char and its code assigned
   */
  private void printCode(){
    System.out.println("========Code for each character in "+s+"===========");
    for (Map.Entry<Character, StringBuffer> m : hc.entrySet())
    {
      System.out.println(m.getKey() + "  has Code " +m.getValue());
    }
  }

  /*
   * Method to print char and its freq
   */
  private void printFreq(){
    System.out.println("================="+s+"====================");
    for (Map.Entry<Character, Integer> m : hm.entrySet())
    {
        System.out.println(m.getKey() + "  occurs " +m.getValue()+ "  times");
    }
  }

  /*
   * Method to encode the given string
   */

  public String encode(){
    StringBuilder d1 = new StringBuilder();

    for(int i=0;i < s.length();i++){
      d1.append(hc.get(s.charAt(i)));
    }
    d = d1.toString();

    System.out.println("===========Decoded String====================");
    System.out.println(d);

    return d;
  }

  /*
   * Method to decode the hauffman encrypted string
   */
  public String decode(){
    StringBuilder s = new StringBuilder();


    Node n = root;
      for (int i = 0; i < d.length(); i++) {
        char ch = d.charAt(i);
        if(hm.size()==1)
        {
          s = s.append(n.c);
        }
        else {
          if (ch == '0') {
            n = n.left;
          } else {
            n = n.right;
          }
          if (n.left == null) // n is a leaf
          {
            s = s.append(n.c);
            n = root;
          }
        }
      }
      r = s.toString();
      System.out.println("===========Recovered String====================");
      System.out.println(r);

      return r;
    }

  /*
   * Method to create dot file from the hauffman tree
   */
  private void createDot(){
    try{
      out = new BufferedWriter(new FileWriter(dotfilename));
      out.write("digraph g {");
      out.newLine();
      out.write("label = "+"\""+"Thanga Assignment: "+s+"\"");
      out.newLine();
      dotPrint(root);
      out.write("}");
      out.close();

    }
    catch (IOException e)
    {
      e.printStackTrace();
    }
  }

  /*
   * Recursive method to print the line needed for creating treedot file
   */
  private void dotPrint( Node n ) throws IOException {
    if ( n.isLeaf() ) {
      //nothing to be printed
      return;
    }
    String o;
    String sc;
    if ( n.left != null ) {
      if(n.left.isLeaf()) {
        if(n.left.c==32)
          sc = "blank";
        else
          sc = Character.toString(n.left.c);

        o = "\"" + n.nodeNum + "\\n" + n.freq + "\"" + " -> " + "\"" + n.left.nodeNum + "\\n" + n.left.freq + "\\n" + sc + "\" [color=red]";
      }
      else
         o = "\""+n.nodeNum+"\\n"+n.freq+"\""+" -> "+"\""+n.left.nodeNum+"\\n"+n.left.freq+"\" [color=red]";

      out.write(o);
      out.newLine();
      dotPrint(n.left);
    }
    if ( n.right != null ) {
      if(n.right.isLeaf()) {
        if(n.right.c==32)
          sc = "blank";
        else
          sc = Character.toString(n.right.c);

        o = "\"" + n.nodeNum + "\\n" + n.freq + "\"" + " -> " + "\"" + n.right.nodeNum + "\\n" + n.right.freq + "\\n" + sc + "\" [color=blue]";
      }
      else
        o = "\""+n.nodeNum+"\\n"+n.freq+"\""+" -> "+"\""+n.right.nodeNum+"\\n"+n.right.freq+"\" [color=blue]";

      out.write(o);
      out.newLine();
      dotPrint(n.right);
    }
  }


  public static void main(String[] args) {
    System.out.println("Hauffman problem STARTS");
    System.out.println("Hauffman problem ENDS");
  }
}
