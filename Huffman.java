/***********************************************************************

  FileName: Huffman.java

  Description: The program when run takes as a commandline argument a
  file name.It then constructs a Huffman Tree. A Character and Frequency
  list is printed. The user is then able to encode and decode one string
  each.

  Author: Luigi Charles 


***********************************************************************/
import java.util.ArrayList;
import java.util.Scanner;
import java.util.PriorityQueue;
import java.io.File;
import java.io.*;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Arrays;

public class Huffman{

  public static void main(String[] args)throws FileNotFoundException{

    //Scans through inputfile & hashes characters;

    Scanner inputScan = new Scanner(new File(args[0]));
    inputScan.useDelimiter("|\n");
    Scanner userInput = new Scanner(System.in);
    HuffmanTree tree = new HuffmanTree();
    HashMap<String,Integer> map = new HashMap<String,Integer>();
    String temp = "";
    String currentChar;
    String currentLine;
    char[] currentArray;

    while (inputScan.hasNextLine()){
      currentLine = inputScan.nextLine();
      currentArray = currentLine.toCharArray();
      for (int i = 0; i < currentArray.length; i++){
       temp = Character.toString(currentArray[i]);  // Removed Strip();
        for (int j = 0; j < temp.length(); j++){
          currentChar = temp.charAt(j) + "";
          if (!map.containsKey(currentChar))
            map.put(currentChar,1);
          else
            map.put(currentChar,map.get(currentChar) + 1);
        }
      }

    }

    // Construct Nodes for all elements in HashMap and adds to Queue;
    for (String x: map.keySet())
      tree.queue.add(new HuffmanNode(x,map.get(x),null,null));

    // Tree Construction and print word table;
    tree.construct();
    System.out.println("\nWord/ Frequency List: \n");
    HuffmanNode root = tree.queue.poll();
    tree.recPrint(root,"");

    // -- ENCODE INPUT -- ;

    // Prompts and asks user for encode;
    String userCode = "";
    char[] userCodeArray;
    boolean checked = false;
    while(checked == false){
      System.out.print("\nEnter a code: ");
      checked = true;
      userCode = userInput.next();
      userCodeArray= userCode.toCharArray();
      for (char x : userCodeArray){
        if (x != '0' && x != '1' && x != ' ')
          checked = false;
      }
      if (!checked)
        System.out.println("Invalid Code! Try again");
    }

    // Takes code and passes it through leafFinder();
    try{
      HuffmanNode newRoot = root;
      char[] codeArray = userCode.toCharArray();
      int counter = 0;
      String decoded = "";
      while (codeArray.length - 1 > counter){
        while (newRoot.left != null && newRoot.right != null){
          newRoot = tree.leafFinder(newRoot,codeArray[counter]);
          codeArray[counter] = '*';
          counter++;
        }
        decoded = decoded +  newRoot.word;
        newRoot = root;
      }
      if (!decoded.equals(""))
        System.out.println("Decoded: " + decoded);
      else
        System.out.println("Invalid Code! No Matches!");

    }

    // Exception catching;
    catch (NullPointerException e){
      System.out.println("Error! Invalid code!");
    }

    catch (ArrayIndexOutOfBoundsException r){
      System.out.println("Error! Invalid code!");
    }

    // -- DECODE INPUT -- ;

    String userString;
    String encoded = "";
    boolean characterBreak = false;

    System.out.print("\nEnter a string: ");
    userInput.nextLine();
    userString = userInput.nextLine();
    char[] feedArray = userString.toCharArray();
    System.out.println(userString);

    for (char x : feedArray){
      if (tree.codeMap.containsKey(Character.toString(x)) || x == ' ')
        encoded = encoded + tree.codeMap.get(Character.toString(x));
      else {
        System.out.println("Character Not Found!");
        characterBreak = true;
        break;
      }
    }
    if (!characterBreak)
      System.out.println("Encoded: " + encoded);

    // Close Scanners;
    inputScan.close();
    userInput.close();

  }

  // Inner Class for Huffman Tree;
  private static class HuffmanTree{

    // HuffmanTree variable declarations & initialization;
    private HuffmanNode root;
    private PriorityQueue<HuffmanNode> queue =
     new PriorityQueue<HuffmanNode>();
    public HashMap<String,String> codeMap =
     new HashMap<String,String>();

    //HuffmanTree contructor;
    HuffmanTree(){
      root = null;
    }

    // Generates Binary Tree from PriorityQueue;
    public void construct(){
      String codeString = "";
      while (this.queue.size() > 1){

        // Finds least nodes;
        HuffmanNode leftNode = this.queue.poll();
        HuffmanNode rightNode = this.queue.poll();

        // New Node from sum;
        int sum = leftNode.frequency + rightNode.frequency;
        HuffmanNode newNode =
         new HuffmanNode(null,sum,leftNode,rightNode);
        this.queue.add(newNode);
      }

    }

    // Recursive Print Method;
    public void recPrint(HuffmanNode x, String s){
      String code = s;
      if (x.left == null && x.right == null){
        System.out.println(x + "||  Code: " + code);
        codeMap.put(x.word,code);
        code = code.substring(0,code.length() - 1);
      }
      if (x.word == null){
        recPrint(x.right,code + "1");
      }
      if (x.word == null)
        recPrint(x.left,code + "0");
    }

    // Recursive find leaf method;
    public HuffmanNode leafFinder(HuffmanNode x, char c){
      if (c == '0')
        return x.left;
      if (c == '1')
        return x.right;
      else
        return null;
    }
  }

    // Innerclass for HuffmanNode;
    public static class HuffmanNode implements Comparable<HuffmanNode>{

      // Node data;
      public String word;
      public int frequency;

      // Connected Nodes;
      public HuffmanNode left;
      public HuffmanNode right;

      // Constructor;
      HuffmanNode(String s, int f, HuffmanNode lt, HuffmanNode rt){
        word = s;
        frequency = f;
        left = lt;
        right = rt;
      }

      // Is Leaf Method;
      public boolean isLeaf(){
        if (this.left == null && this.right == null)
          return true;
        return false;
      }

      // Compare Override Method;
      @Override
      public int compareTo(HuffmanNode node){
        if (this.frequency < node.frequency){
          return -1;
        }
        else if (this.frequency > node.frequency)
          return 1;
        else
          return 0;
      }

      // Print Override Method;
      @Override
      public String toString(){
        return "Word: " + word + " || frequency: " + frequency;
      }

    }

}
