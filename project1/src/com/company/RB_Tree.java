package com.company;

import java.util.Scanner;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class RB_Tree<K extends Comparable<K>,V> {

    private RB_Tree_Node<K,V> root;

    public RB_Tree() {
        root = null;
    }

    public RB_Tree(K key,V value) {
        this.root = new RB_Tree_Node(key,value, null, null, null);
    }
    private static class RB_Tree_Node<K,V> {
        private K key;
        private V value;
        private RB_Tree_Node parent;
        private RB_Tree_Node leftChild;
        private RB_Tree_Node rightChild;
        private boolean black = true;

        public RB_Tree_Node(K key,V value) {
            this.key = key;
            this.value = value;
        }

        public RB_Tree_Node(K key,V value, RB_Tree_Node parent, RB_Tree_Node leftChild, RB_Tree_Node rightChild) {
            this.key = key;
            this.value = value;
            this.parent = parent;
            this.leftChild = leftChild;
            this.rightChild = rightChild;
        }

        public boolean isLeftChild() {
            return this == this.parent.leftChild;
        }

        public boolean isRightChild() {
            return this == this.parent.rightChild;
        }

        public boolean isRoot() {
            return this.parent == null;
        }

        public void setValue(V value){
            this.value = value;
        }
        public V getValue(){
            return this.value;
        }

        @Override
        public String toString() {
            String color;
            if(this.black == true){
                color = "black";
            }else {
                color = "red";
            }
            return "key: "+this.key+","+"value: "+this.value+","+"color: "+color;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj.getClass() == RB_Tree_Node.class) {
                RB_Tree_Node target = (RB_Tree_Node) obj;
                return  leftChild == target.leftChild && rightChild == target.rightChild && parent == target.parent && key.equals(target.key) && black == target.black;
            }
            return false;
        }
    }
    private RB_Tree_Node getParent(RB_Tree_Node node) {
        if(node == null){
            return null;
        }else {
            return node.parent;
        }
    }

    private RB_Tree_Node getGrandfather(RB_Tree_Node node) {
        return getParent(getParent(node));
    }

    private RB_Tree_Node getLeftChild(RB_Tree_Node node) {
        if(node == null){
            return null;
        }else {
            return node.leftChild;
        }
    }

    private RB_Tree_Node getRightChild(RB_Tree_Node node) {
        if(node == null){
            return null;
        }else {
            return node.rightChild;
        }
    }

    private boolean isBlack(RB_Tree_Node node) {
        if(node != null){
            return node.black;
        }else {
            return true;
        }
    }

    private void setBlack(RB_Tree_Node node, boolean isBlack) {
        if (node != null) {
            node.black = isBlack;
        }
    }

    public RB_Tree_Node getNode(K key) {
        RB_Tree_Node node = root;
        while (node != null) {
            int cmp = key.compareTo((K) node.key);
            if (cmp < 0) {
                node = node.leftChild;
            } else if (cmp > 0) {
                node = node.rightChild;
            } else {
                return node;
            }
        }
        return null;
    }

    private void leftRotation(RB_Tree_Node node) {
        if (node != null) {
            RB_Tree_Node rightChild = node.rightChild;
            RB_Tree_Node node1 = rightChild.leftChild;
            node.rightChild = node1;
            if (node1 != null) {
                node1.parent = node;
            }
            rightChild.parent = node.parent;
            if (node.parent == null) {
                root = rightChild;
            } else if (node == node.parent.leftChild) {
                node.parent.leftChild = rightChild;
            } else {
                node.parent.rightChild = rightChild;
            }
            rightChild.leftChild = node;
            node.parent = rightChild;
        }
    }

    private void rightRotation(RB_Tree_Node node) {
        if (node != null) {
            RB_Tree_Node leftChild = node.leftChild;
            RB_Tree_Node node1 = leftChild.rightChild;
            node.leftChild = node1;
            if (node1 != null) {
                node1.parent = node;
            }
            leftChild.parent = node.parent;
            if (node.parent == null) {
                root = leftChild;
            } else if (node == node.parent.leftChild) {
                node.parent.leftChild = leftChild;
            } else {
                node.parent.rightChild = leftChild;
            }
            leftChild.rightChild = node;
            node.parent = leftChild;
        }
    }

    public void insert(K key,V value) {
        if (root == null) {
            this.root = new RB_Tree.RB_Tree_Node(key,value);
        } else {
            RB_Tree.RB_Tree_Node node = root;
            RB_Tree.RB_Tree_Node parent = null;
            int cmp = 0;
            do {
                //二叉树搜索
                parent = node;
                cmp = key.compareTo((K) node.key);
                if (cmp > 0) {
                    node = node.rightChild;
                } else if(cmp == 0){
                    System.out.println("Error:key conflict");
                    return;
                }else if(cmp < 0){
                    node = node.leftChild;
                }
            } while (node != null);
            RB_Tree.RB_Tree_Node node1 = new RB_Tree.RB_Tree_Node(key,value, parent, null, null);
            if (cmp > 0) {
                parent.rightChild = node1;
            } else {
                parent.leftChild = node1;
            }
            insertFix(node1);
        }
    }

    public void delete(K key) {
        RB_Tree_Node node = getNode(key);
        if (node == null) {
            System.out.println("Error:key missing");
            return;
        }
        //前趋
        if (node.leftChild != null && node.rightChild != null) {
            RB_Tree_Node node1 = node.leftChild;
            while (node1.rightChild != null) {
                node1 = node1.rightChild;
            }
            node.key = node1.key;
            node = node1;
        }
        RB_Tree_Node node1;
        if(node.leftChild != null){
            node1 = node.leftChild;
        }else {
            node1 = node.rightChild;
        }
        if (node1 != null) {
            node1.parent = node.parent;
            if (node.isRoot()) {
                root = node1;
            } else if (node.isLeftChild()) {
                node.parent.leftChild = node1;
            } else {
                node.parent.rightChild = node1;
            }
            node.leftChild = node.rightChild = node.parent = null;
            if (isBlack(node) == true) {
                deleteFix(node1);
            }
        } else if (node.isRoot()) {
            root = null;
        } else {
            if (isBlack(node) == true) {
                deleteFix(node);
            }
            if (node.isLeftChild()) {
                node.parent.leftChild = null;
            } else {
                node.parent.rightChild = null;
            }
            node.parent = null;
        }
    }

    private void insertFix(RB_Tree_Node node) {
        //node的父节点和叔叔节点都是红色，这个时候，只需要将x叔、父节点设置为黑色，祖父节点设置为红色
        //然后，将祖父节点设置为node递归处理
        //node的叔叔节点是黑色，这时候就需要进行旋转处理了
        node.black = false;
        while (node != null && node != root && node.parent.black == false) {
            if (getParent(node).isLeftChild()) {
                RB_Tree_Node uncle = getRightChild(getGrandfather(node));
                if (isBlack(uncle) == false) {
                    // 叔、父都是红色
                    setBlack(getParent(node), true);
                    setBlack(uncle, true);
                    setBlack(getGrandfather(node), false);
                    node = getGrandfather(node);
                } else {
                    if (node.isRightChild()) {
                        node = getParent(node);
                        //不同侧
                        leftRotation(node);
                    }
                    setBlack(getParent(node), true);
                    setBlack(getGrandfather(node), false);
                    rightRotation(getGrandfather(node));
                }
            } else {
                RB_Tree_Node uncle = getLeftChild(getGrandfather(node));
                if (isBlack(uncle) == false) {
                    // 叔、父都是红色
                    setBlack(getParent(node), true);
                    setBlack(uncle, true);
                    setBlack(getGrandfather(node), false);
                    node = getGrandfather(node);
                } else {
                    if (node == getLeftChild(getParent(node))) {
                        node = getParent(node);
                        rightRotation(node);
                    }
                    setBlack(getParent(node), true);
                    setBlack(getGrandfather(node), false);
                    //不同侧
                    leftRotation(getGrandfather(node));
                }
            }
        }
        root.black = true;
    }

    public void deleteFix(RB_Tree_Node node) {
        while (node != root && isBlack(node) == true) {
            if (node.isLeftChild()) {
                //是左儿子
                RB_Tree_Node brother = getRightChild(getParent(node));
                if (isBlack(brother) == false) {
                    // sib是红色，则它的父节点原本一定是黑色
                    setBlack(brother, true);
                    setBlack(getParent(brother), false);
                    leftRotation(getParent(node));
                    brother = getRightChild(getParent(node));
                }
                if (isBlack(getLeftChild(brother)) == true && isBlack(getRightChild(brother)) == true) {
                    setBlack(brother, false);
                    node = getParent(node);
                } else {
                    //sib的子节点中只有一个是黑色
                    if (isBlack(getRightChild(brother)) == true) {
                        setBlack(getLeftChild(brother), true);
                        setBlack(brother, false);
                        rightRotation(brother);
                        brother = getRightChild(getParent(node));
                    }
                    setBlack(brother, isBlack(getParent(node)));
                    setBlack(getParent(node), true);
                    setBlack(getRightChild(node), true);
                    leftRotation(getParent(node));
                    node = root;
                }
            } else {
                //是右儿子
                RB_Tree_Node sib = getLeftChild(getParent(node));
                if (isBlack(sib) == false) {
                    setBlack(sib, true);
                    setBlack(getParent(node), false);
                    rightRotation(getParent(node));
                    sib = getLeftChild(getParent(node));
                }
                // sib的两个子节点都是黑色
                if (isBlack(getLeftChild(sib)) == true && isBlack(getRightChild(sib)) == true) {
                    setBlack(sib, false);
                    node = getParent(node);
                } else {
                    if (isBlack(getLeftChild(sib)) == true) {
                        setBlack(getRightChild(sib), true);
                        setBlack(sib, false);
                        leftRotation(sib);
                        sib = getLeftChild(getParent(node));
                    }
                    setBlack(sib, isBlack(getParent(node)));
                    setBlack(getParent(node), true);
                    setBlack(getLeftChild(node), true);
                    rightRotation(getParent(node));
                    node = root;
                }
            }
        }
        setBlack(node, true);
    }

    // 中序遍历
    public List<RB_Tree_Node> inIterator(RB_Tree_Node node) {
        List<RB_Tree_Node> nodes = new ArrayList<RB_Tree_Node>();
        if (node.leftChild != null) {
            nodes.addAll(inIterator(node.leftChild));
        }
        nodes.add(node);
        if (node.rightChild != null) {
            nodes.addAll(inIterator(node.rightChild));
        }
        return nodes;
    }

    public List<RB_Tree_Node> inIterator() {
        return root != null ? inIterator(root) : new ArrayList<RB_Tree_Node>(0);
    }

    public static void main(String[] args) throws IOException {
        RB_Tree<Integer,Integer> tree = new RB_Tree<Integer,Integer>();
        Integer[] integers = {8, 42, 86, 9, 17, 22, 11, 7, 13, 38};
        for (Integer i :
                integers) {
            tree.insert(i,null);
            System.out.println(tree.inIterator());
        }
        for (Integer i :
                integers) {
            tree.delete(i);
            System.out.println(tree.inIterator());
        }
        order();

    }
    public static void order() throws IOException {
        String s;
        RB_Tree<Integer,String> tree = new RB_Tree<Integer,String>();
        do {
            System.out.println("请输入指令：");
            Scanner scanner1 = new Scanner(System.in);
            String choice = scanner1.nextLine();
            s = choice;
            if (choice.equals("insert")) {
                System.out.println("请输入键：");
                Scanner scanner2 = new Scanner(System.in);
                String key = scanner2.nextLine();
                System.out.println("请输入值：");
                Scanner scanner3 = new Scanner(System.in);
                String value = scanner3.nextLine();
                if(tree.getNode(Integer.parseInt(key)) != null){
                    System.out.println("Error:key conflict");
                }else {
                    tree.insert(Integer.parseInt(key),value);
                }
                System.out.println(tree.inIterator());
            }
            if(choice.equals("put")){
                System.out.println("请输入键：");
                Scanner scanner2 = new Scanner(System.in);
                String key = scanner2.nextLine();
                System.out.println("请输入值：");
                Scanner scanner3 = new Scanner(System.in);
                String value = scanner3.nextLine();
                if(tree.getNode(Integer.parseInt(key))==null){
                    System.out.println("Error:key missing");
                }else {
                    tree.getNode(Integer.parseInt(key)).setValue(value);
                }
                System.out.println(tree.inIterator());
            }
            if(choice.equals("get")){
                System.out.println("请输入键：");
                Scanner scanner2 = new Scanner(System.in);
                String key = scanner2.nextLine();
                if(tree.getNode(Integer.parseInt(key))==null){
                    System.out.println("Error:key missing");
                }else {
                    System.out.println("查找到键："+key);
                }
                System.out.println(tree.inIterator());
            }
            if(choice.equals("del")){
                System.out.println("请输入键：");
                Scanner scanner2 = new Scanner(System.in);
                String key = scanner2.nextLine();
                if(tree.getNode(Integer.parseInt(key))==null){
                    System.out.println("Error:key missing");
                }else {
                    tree.delete(Integer.parseInt(key));
                }
                System.out.println(tree.inIterator());
            }
            if(choice.equals("load")){
                System.out.println("读取结果如下：");
                readFile();

            }
        }while (!s.equals("quit"));
    }
    public static void readFile() throws IOException {
        RB_Tree<Integer,Integer> tree = new RB_Tree<Integer,Integer>();
        String path = "E:\\大学课程\\数据结构与算法设计\\project1\\data1.txt";
        FileInputStream fileInputStream = new FileInputStream(path);
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(fileInputStream));
        String line = null;
        String str1 = "";
        RB_Tree<Integer,Integer> tree1 = new RB_Tree<Integer,Integer>();
        while ((line = bufferedReader.readLine()) != null) {
            System.out.println(line);
            str1 = str1+line+" ";
        }
        String[] str = str1.split("\\s+");
        Integer[] integers1 = new Integer[str.length];
        for(int i = 0;i < str.length;i = i+2){
            integers1[i] = Integer.parseInt(str[i]);
            integers1[i+1] = Integer.parseInt(str[i+1]);
        }
        for (int i=0;i<integers1.length;i = i+2)
        {
            tree.insert(integers1[i],integers1[i+1]);
            System.out.println(tree.inIterator());
        }
        for (int i=0;i<integers1.length;i = i+2)
        {
            tree.delete(integers1[i]);
            System.out.println(tree.inIterator());
        }
        System.out.println("----------------------");
        fileInputStream.close();
    }
    public static void writeFile(String s){
        try {
            File writeName = new File("data1.txt");
            try (FileWriter writer = new FileWriter(writeName, true);
                 BufferedWriter out = new BufferedWriter(writer)
            ) {
                out.write(s+" ");
                out.flush();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    }