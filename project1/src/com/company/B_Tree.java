package com.company;

import java.io.*;
import java.util.*;

public class B_Tree<K, V> {
    private int degree = 2;
    private B_Tree_Node<K, V> root;
    private int minSize = degree - 1;
    private int maxSize = 2*degree - 1;
    private Comparator<K> kComparator;

    private static class keyValue<K, V> {
        private K key;
        private V value;

        public void setKey(K key) {
            this.key = key;
        }

        public K getKey() {
            return this.key;
        }

        public void setValue(V value) {
            this.value = value;
        }

        public V getValue() {
            return this.value;
        }

        @Override
        public String toString() {
            return "key: " + this.key + " , "+"value "+this.value+"   ";
        }
    }

    public class B_Tree_Node<K, V> {
        private List<keyValue<K, V>> keyValues;
        private List<B_Tree_Node<K, V>> children;
        private boolean isLeaf;
        private Comparator<K> kComparator;

        public B_Tree_Node() {
            this.keyValues = new LinkedList<>();
            this.children = new LinkedList<>();
            this.isLeaf = false;
        }
        public B_Tree_Node(Comparator<K> kComparator) {
            this();
            this.kComparator = kComparator;
        }
        private int compare(K key1, K key2) {
            if(this.kComparator == null){
                return ((Comparable<K>) key2).compareTo(key1);
            }else {
                return kComparator.compare(key1, key2);
            }
        }

        public void setIsLeaf(boolean isLeaf) {
            this.isLeaf = isLeaf;
        }
        public boolean getIsLeaf() {
            return this.isLeaf;
        }
        public int nodeSize() {
            return this.keyValues.size();
        }
        //二分查找
        public searchValue<V> search(K key) {
            int begin = 0;
            int end = this.nodeSize() - 1;
            int mid = (begin + end) / 2;
            boolean exist = false;
            int index = 0;
            V value = null;
            //二分查找
            while (begin < end) {
                mid = (begin + end) / 2;
                keyValue keyValue = this.keyValues.get(mid);
                int compare = compare((K) keyValue.getKey(), key);
                if (compare == 0) {
                    break;
                } else {
                    if (compare > 0) {
                        begin = mid + 1;
                    } else {
                        end = mid - 1;
                    }
                }
            }
            //只有两个或一个元素属于边界条件
            if (begin < end) {
                exist = true;
                index = mid;
                value = this.keyValues.get(mid).getValue();
            } else if (begin == end) {
                K midKey = this.keyValues.get(begin).getKey();
                int comRe = compare(midKey, key);
                if (comRe == 0) {
                    exist = true;
                    index = begin;
                    value = this.keyValues.get(mid).getValue();
                } else if (comRe > 0) {
                    exist = false;
                    index = begin + 1;
                    value = null;
                } else {
                    exist = false;
                    index = begin;
                    value = null;
                }
            } else {
                exist = false;
                index = begin;
                value = null;
            }
            //递归调用
            return new searchValue<V>(exist, index, value);
        }
        public keyValue<K, V> deleteKeyValue(int index) {
            keyValue<K, V> keyValue = this.keyValues.get(index);
            this.keyValues.remove(index);
            return keyValue;
        }
        public keyValue<K, V> getKeyValue(int index) {
            return this.keyValues.get(index);
        }
        private void insertKeyValue(keyValue<K, V> keyValue, int index) {
            this.keyValues.add(index, keyValue);
        }
        private boolean insertKeyValue(keyValue<K, V> keyValue) {
            searchValue<V> result = search(keyValue.getKey());
            if (result.isExist()) {
                return false;
            } else {
                insertKeyValue(keyValue, result.getIndex());
                return true;
            }
        }

        public void updateKeyValue(keyValue<K, V> keyValue) {
            searchValue<V> re = search(keyValue.getKey());
            if (re.exist) {
                keyValue keyValue1 = this.keyValues.get(re.getIndex());
                V oldValue = (V) keyValue1.getValue();
                keyValue1.setValue(keyValue.getValue());
                return;
            } else {
                insertKeyValue(keyValue);
            }
        }
        public B_Tree_Node getChild(int index) {
            return this.children.get(index);
        }
        public void deleteChild(int index) {
            this.children.remove(index);
        }
        public void insertChild(B_Tree_Node<K, V> node, int index) {
            this.children.add(index, node);
        }
    }
    //二分查找，根据key
    private static class searchValue<V> {
        private boolean exist;
        private V value;
        private int index;

        //构造方法，将查询结果封装入对象
        public searchValue(boolean isExist, int index, V value) {
            this.exist = isExist;
            this.index = index;
            this.value = value;
        }

        public boolean isExist() {
            return exist;
        }

        public V getValue() {
            return value;
        }

        public int getIndex() {
            return index;
        }
    }

    public B_Tree() {
        B_Tree_Node<K, V> root = new B_Tree_Node<K, V>();
        this.root = root;
        root.setIsLeaf(true);
    }
    public B_Tree(int degree) {
        this();
        this.degree = degree;
        minSize = degree - 1;
        maxSize = 2*degree - 1;
    }
    public B_Tree(Comparator<K> com, int t) {
        this(t);
        this.kComparator = com;
    }
    public V search(B_Tree_Node<K, V> root, K key) {
        searchValue<V> re = root.search(key);
        if (re.exist) {
            return re.value;
        } else {
            if (root.isLeaf) {
                return null;
            }
            int index = re.index;
            //递归搜索
            return (V) search(root.getChild(index), key);
        }
    }
    public V search(K key) {
        return search(this.root, key);
    }
    //从中间节点断开，后半部分形成新结点插入父节点。若分裂节点不是叶子节点，将子节点一并分裂到新节点
    //index:待分裂节点在父节点中的索引
    private void split(B_Tree_Node<K, V> parentNode, B_Tree_Node<K, V> node, int index) {
        B_Tree_Node<K, V> node1 = new B_Tree_Node<K, V>(this.kComparator);
        node1.setIsLeaf(node.isLeaf);
        //迁移到新节点
        for (int i = degree; i < this.maxSize; i++) {
            node1.keyValues.add(node.keyValues.get(i));
        }
        keyValue<K, V> keyValue = node.keyValues.get(minSize);
        for (int i = this.maxSize - 1; i >= minSize; i--) {
            node.keyValues.remove(i);
        }
        //子节点一并跟随分裂
        if (!node.getIsLeaf()) {
            for (int i = degree; i < this.maxSize + 1; i++) {
                node1.children.add(node.children.get(i));
            }
            for (int i = this.maxSize; i >= degree; i--) {
                node.children.remove(i);
            }
        }
        //父节点插入分裂的中间元素，分裂出的新节点加入父节点的 sons
        parentNode.insertKeyValue(keyValue);
        parentNode.insertChild(node1, index + 1);
    }
    private boolean insert(B_Tree_Node<K, V> root, keyValue<K, V> keyValue) {
        if (root.getIsLeaf()) {
            return root.insertKeyValue(keyValue);
        }
        searchValue<V> value = root.search(keyValue.getKey());
        if (value.exist) {
            return false;
        }
        int index = value.getIndex();
        B_Tree_Node<K, V> searchChild = root.getChild(index);
        //查询子节点已满，分裂后再判断该搜索哪个子节点
        if (searchChild.nodeSize() == maxSize) {
            split(root, searchChild, index);
            if (root.compare(root.getKeyValue(index).getKey(), keyValue.getKey()) > 0) {
                searchChild = root.getChild(index + 1);
            }
        }
        return insert(searchChild, keyValue);
    }
    //插入一个非满节点：一路向下寻找插入位置。
    //在寻找的路径上，如果碰到大小为2t-1的节点，分裂并向上融合。
    public boolean insert(keyValue<K, V> keyValue) {
        if (root.nodeSize() == maxSize) {
            B_Tree_Node<K, V> node = new B_Tree_Node<K, V>();
            node.setIsLeaf(false);
            node.insertChild(root, 0);
            split(node, root, 0);
            this.root = node;
        }
        return insert(root, keyValue);
    }
    private keyValue<K, V> delete(B_Tree_Node<K, V> root, keyValue<K, V> keyValue) {
        searchValue<V> value = root.search(keyValue.getKey());
        if (value.isExist()) {
            if (root.getIsLeaf()) {
                return root.deleteKeyValue(value.getIndex());
            }
            //不是叶子节点，判断应将待删除节点交换到左子节点还是右子节点
            B_Tree_Node<K, V> leftChild = root.getChild(value.getIndex());
            if (leftChild.nodeSize() >= degree) {
                root.deleteKeyValue(value.getIndex());
                root.insertKeyValue(leftChild.getKeyValue(leftChild.nodeSize() - 1), value.getIndex());
                leftChild.deleteKeyValue(leftChild.nodeSize() - 1);
                leftChild.insertKeyValue(keyValue);
                return delete(leftChild, keyValue);
            }
            //左子节点不可删除项，则同样逻辑检查右子节点
            B_Tree_Node<K, V> rightChild = root.getChild(value.getIndex() + 1);
            if (rightChild.nodeSize() >= degree) {
                root.deleteKeyValue(value.getIndex());
                root.insertKeyValue(rightChild.getKeyValue(0), value.getIndex());
                rightChild.deleteKeyValue(0);
                rightChild.insertKeyValue(keyValue);
                return delete(rightChild, keyValue);
            }
            //左右子节点均不能删除项，将左右子节点合并，并将删除项放到新节点的合并连接处
            keyValue<K, V> keyValue1 = root.deleteKeyValue(value.getIndex());
            leftChild.insertKeyValue(keyValue1);
            root.deleteChild(value.getIndex() + 1);
            //左右子节点合并
            for (int i = 0; i < rightChild.nodeSize(); i++) {
                leftChild.insertKeyValue(rightChild.getKeyValue(i));
            }
            if (!rightChild.getIsLeaf()) {
                for (int i = 0; i < rightChild.children.size(); i++) {
                    leftChild.insertChild(rightChild.getChild(i), leftChild.children.size());
                }
            }
            return delete(leftChild, keyValue);
        } else {
            if (root.getIsLeaf()) {
                for (int i = 0; i < root.nodeSize(); i++) {
                    System.out.print(root.getKeyValue(i).getKey() + "，");
                }
                //throw new RuntimeException(keyValue.key + " is not in this tree!Error:key missing");
            }
            B_Tree_Node<K, V> searchChild = root.getChild(value.index);
            if (searchChild.nodeSize() >= degree) {
                return delete(searchChild, keyValue);
            }
            B_Tree_Node<K, V> siblingNode = null;
            //存在右兄弟
            int siblingIndex = -1;
            if (value.getIndex() < root.nodeSize() - 1) {
                B_Tree_Node<K, V> rightBrother = root.getChild(value.getIndex() + 1);
                if (rightBrother.nodeSize() >= degree) {
                    siblingNode = rightBrother;
                    siblingIndex = value.getIndex() + 1;
                }
            }
            //不存在右兄弟则尝试左兄弟
            if (siblingNode == null) {
                if (value.getIndex() > 0) {
                    B_Tree_Node<K, V> leftBrother = root.getChild(value.getIndex() - 1);
                    if (leftBrother.nodeSize() >= degree) {
                        siblingNode = leftBrother;
                        siblingIndex = value.getIndex() - 1;
                    }
                }
            }
            //至少有一个兄弟可以匀出项来
            if (siblingNode != null) {
                if (siblingIndex < value.getIndex()) {
                    searchChild.insertKeyValue(root.getKeyValue(siblingIndex), 0);
                    root.deleteKeyValue(siblingIndex);
                    root.insertKeyValue(siblingNode.getKeyValue(siblingNode.nodeSize() - 1), siblingIndex);
                    siblingNode.deleteKeyValue(siblingNode.nodeSize() - 1);
                    if (!siblingNode.getIsLeaf()) {
                        searchChild.insertChild(siblingNode.getChild(siblingNode.children.size() - 1), 0);
                        siblingNode.deleteChild(siblingNode.children.size() - 1);
                    }
                } else {
                    searchChild.insertKeyValue(root.getKeyValue(value.getIndex()), searchChild.nodeSize() - 1);
                    root.deleteKeyValue(value.getIndex());
                    root.insertKeyValue(siblingNode.getKeyValue(0), value.getIndex());
                    siblingNode.deleteKeyValue(0);
                    if (!siblingNode.getIsLeaf()) {
                        searchChild.insertChild(siblingNode.getChild(0), searchChild.children.size());
                        siblingNode.deleteChild(0);
                    }
                }
                return delete(searchChild, keyValue);
            }
            //左右兄弟都匀不出项来，直接由左右兄弟节点与父项合并为一个节点
            if (value.getIndex() <= root.nodeSize() - 1) {
                B_Tree_Node<K, V> rightSon = root.getChild(value.getIndex() + 1);
                searchChild.insertKeyValue(root.getKeyValue(value.getIndex()), searchChild.nodeSize());
                root.deleteKeyValue(value.getIndex());
                root.deleteChild(value.getIndex() + 1);
                for (int i = 0; i < rightSon.nodeSize(); i++) {
                    searchChild.insertKeyValue(rightSon.getKeyValue(i));
                }
                if (!rightSon.getIsLeaf()) {
                    for (int j = 0; j < rightSon.children.size(); j++) {
                        searchChild.insertChild(rightSon.getChild(j), searchChild.children.size());
                    }
                }
                if (root == this.root) {
                    this.root = searchChild;
                }
            } else {
                B_Tree_Node<K, V> leftChild = root.getChild(value.getIndex() - 1);
                searchChild.insertKeyValue(root.getKeyValue(value.getIndex() - 1), 0);
                root.deleteChild(value.getIndex() - 1);
                root.deleteKeyValue(value.getIndex() - 1);
                for (int i = 0; i < leftChild.nodeSize(); i++) {
                    searchChild.insertKeyValue(leftChild.getKeyValue(i));
                }
                if (!leftChild.getIsLeaf()) {
                    for (int i = leftChild.children.size() - 1; i >= 0; i--) {
                        searchChild.insertChild(leftChild.getChild(i), 0);
                    }
                }
                if (root == this.root) {
                    this.root = searchChild;
                }
            }
            return delete(searchChild, keyValue);
        }
    }
    public keyValue<K, V> delete(K key) {
        keyValue<K, V> en = new keyValue<K, V>();
        en.setKey(key);
        return delete(root, en);
    }
    //队列打印B树
    public void outPrint() {
        Queue<B_Tree_Node<K, V>> queue = new LinkedList<>();
        queue.offer(this.root);
        int line = 0;
        while (!queue.isEmpty()) {
            B_Tree_Node<K, V> node = queue.poll();
            for (int i = 0; i < node.nodeSize(); ++i) {
                System.out.print(node.getKeyValue(i) + " ");
            }
            System.out.println();
            if (!node.getIsLeaf()) {
                for (int i = 0; i <= node.children.size() - 1; ++i) {
                    queue.offer(node.getChild(i));
                }
            }
        }
    }
    public static void main(String[] args) throws IOException {
        Random random = new Random();
        B_Tree<Integer, Integer> bt = new B_Tree<Integer, Integer>(2);
        List<Integer> save = new ArrayList<Integer>(30);
        for (int i = 0; i < 30; ++i) {
            int r = random.nextInt(10000);
            save.add(r);
            B_Tree.keyValue keyValue = new keyValue<Integer, Integer>();
            keyValue.setKey(r);
            keyValue.setValue(r);
            bt.insert(keyValue);
            bt.outPrint();
            System.out.println("------"+bt.search(5));
        }
        System.out.println("----------------------");

        bt.outPrint();
        System.out.println("----------------------");
        bt.delete(save.get(6));
        bt.outPrint();
        order();
    }
    public static void order() throws IOException {
        String s;
        B_Tree<Integer,String> tree = new B_Tree<Integer,String>();
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
                if(tree.search(Integer.parseInt(key)) != null){
                    System.out.println("Error:key conflict");
                }else {
                    B_Tree.keyValue keyValue = new keyValue<Integer, Integer>();
                    keyValue.setKey(Integer.parseInt(key));
                    keyValue.setValue(Integer.parseInt(value));
                    tree.insert(keyValue);
                }
                tree.outPrint();
            }
            if(choice.equals("put")){
                System.out.println("请输入键：");
                Scanner scanner2 = new Scanner(System.in);
                String key = scanner2.nextLine();
                System.out.println("请输入值：");
                Scanner scanner3 = new Scanner(System.in);
                String value = scanner3.nextLine();
                if(tree.search(Integer.parseInt(key))==null){
                    System.out.println("Error:key missing");
                }else {
                    B_Tree.keyValue keyValue = new keyValue<Integer, Integer>();
                    keyValue.setKey(Integer.parseInt(key));
                    keyValue.setValue(Integer.parseInt(value));
                   tree.root.updateKeyValue(keyValue);
                }
                tree.outPrint();
            }
            if(choice.equals("get")){
                System.out.println("请输入键：");
                Scanner scanner2 = new Scanner(System.in);
                String key = scanner2.nextLine();
                if(tree.search(Integer.parseInt(key))==null){
                    System.out.println("Error:key missing");
                }else {
                    System.out.println("查找到键："+key);
                }
               tree.outPrint();
            }
            if(choice.equals("del")){
                System.out.println("请输入键：");
                Scanner scanner2 = new Scanner(System.in);
                String key = scanner2.nextLine();
                if(tree.search(Integer.parseInt(key))==null){
                    System.out.println("Error:key missing");
                }else {
                    tree.delete(Integer.parseInt(key));
                }
               tree.outPrint();
            }
            if(choice.equals("load")){
                System.out.println("读取结果如下：");
                readFile();

            }
        }while (!s.equals("quit"));
    }
    public static void readFile() throws IOException {
        String path = "E:\\大学课程\\数据结构与算法设计\\project1\\data2.txt";
        FileInputStream fileInputStream = new FileInputStream(path);
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(fileInputStream));
        String line = null;
        B_Tree<Integer, Integer> bt1 = new B_Tree<Integer, Integer>(2);
        List<Integer> save1 = new ArrayList<Integer>(30);
        String str1 = "";
        while ((line = bufferedReader.readLine()) != null) {
            System.out.println(line);
            str1 = str1+line+" ";

        }
        String[] str = str1.split("\\s+");
        int i = 0;
        while (i < str.length){
            save1.add(Integer.parseInt(str[i]));
            B_Tree.keyValue keyValue = new keyValue<Integer, Integer>();
            keyValue.setKey(Integer.parseInt(str[i]));
            keyValue.setValue(Integer.parseInt(str[i+1]));
            bt1.insert(keyValue);
            i = i+2;
        }
        bt1.outPrint();
        fileInputStream.close();
    }
    public static void writeFile(String s) {
        try {
            File writeName = new File("data1.txt");
            try (FileWriter writer = new FileWriter(writeName, true);
                 BufferedWriter out = new BufferedWriter(writer)
            ) {
                out.write(s + " ");
                out.flush();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
