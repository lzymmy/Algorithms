package com.company;
import java.util.*;
import sun.misc.Queue;
import java.util.Map;
import java.util.Map.Entry;

//人类思考，上帝发笑，下属思考，上司发火；孩子思考，父母发愁。
public class BeamSearch {
    public static int beamSize = 2;
          public static class Node{
          public Node parent;
          public Node[] children;
          public float rate;
          public String str;
          public Node(Node parent,Node[] children,float rate,String str){
              this.parent = parent;
              this.children = children;
              this.rate = rate;
              this.str = str;
          }
      }
      public static String[] phrases = {"人类","上帝","思考","发笑"};

      public static float[] creatRates(){
          float[] rates = new float[4];
          for(int i = 0;i < phrases.length;i++){
              rates[i] = (float) Math.random();
          }
          return rates;
      }
      public static Node[] creatNodes(Node parent){
          Node[] nodes = new Node[beamSize];
          Map<String,Float> map = new HashMap<String,Float>();
          map.put("人类",(float)Math.random());
          map.put("上帝",(float)Math.random());
          map.put("思考",(float)Math.random());
          map.put("发笑",(float)Math.random());
          System.out.println("root选择:");
          map.forEach((key, value) -> {
              System.out.print(key + ":" + value+" ");
              System.out.println();
          });
          Comparator<Map.Entry<String, Float>> valCmp = new Comparator<Map.Entry<String,Float>>() {
              @Override
              public int compare(Entry<String, Float> o1, Entry<String, Float> o2) {
                  // TODO Auto-generated method stub
                  return (int) (o2.getValue()*10-o1.getValue()*10);  //降序排序，如果想升序就反过来
              }
          };
          //将map转成List，map的一组key，value对应list一个存储空间
          List<Map.Entry<String, Float>> list = new ArrayList<Map.Entry<String,Float>>(map.entrySet());
          Collections.sort(list,valCmp);
          //输出map
          for(int i = 0;i < beamSize;i++){
              nodes[i] = new Node(parent,null,list.get(i).getValue(),list.get(i).getKey());
          }
          //for(int i=0;i<list.size();i++) {
          //    System.out.println(list.get(i).getKey() + " = " + list.get(i).getValue());
          //}
          return nodes;
      }
      public static Node creatNode(Node parent){
          Map<String,Float> map = new HashMap<String,Float>();
          map.put("人类",(float)Math.random());
          map.put("上帝",(float)Math.random());
          map.put("思考",(float)Math.random());
          map.put("发笑",(float)Math.random());
          System.out.println();
          map.forEach((key, value) -> {
              System.out.print(key + ":" + value+" ");
              System.out.println();
          });
          Comparator<Map.Entry<String, Float>> valCmp = new Comparator<Map.Entry<String,Float>>() {
              @Override
              public int compare(Entry<String, Float> o1, Entry<String, Float> o2) {
                  // TODO Auto-generated method stub
                  return (int) (o2.getValue()*10-o1.getValue()*10); // 降序排序，如果想升序就反过来
              }
          };
          //将map转成List，map的一组key，value对应list一个存储空间
          List<Map.Entry<String, Float>> list = new ArrayList<Map.Entry<String,Float>>(map.entrySet());
          Collections.sort(list,valCmp);
          //输出map
          Node node = new Node(parent,null,list.get(0).getValue(),list.get(0).getKey());
          return node;
      }
      public static void main(String[] args) throws InterruptedException {
          Queue<Node> queue = new Queue<>();
          Node root = new Node(null,null,0,null);
          root.children = creatNodes(root);
          queue.enqueue(root.children[0]);
          queue.enqueue(root.children[1]);
          Node node = null;
          for(int i = 0;i < 3;i++){
              Node node1 = queue.dequeue();
              Node child1 = creatNode(node1);
              Node node2 = queue.dequeue();
              Node child2 = creatNode(node2);
              if(i != 2){
                  queue.enqueue(child1);
                  queue.enqueue(child2);
              }else {
                  node = child1;
              }
          }
          System.out.println(node.parent.parent.parent.str+node.parent.parent.str+node.parent.str+node.str);
      }
}
