package com.example.argir.tree;

public class BST<Key extends Comparable<Key>, Value> {


    private Node root;

    public int size() {
        return size(root);
    }


    public int size(Node node) {
        if (node == null) {
            return 0;
        }
        return node.getN();
    }


    public Value get(Key key) {
        return get(root,key);
    }

    //命中返回值否则返回Null
    private Value get(Node root, Key key) {
        if (root == null) {
            return null;
        }
        int cmp=key.compareTo(root.getKey());
        if (cmp>0) return get(root.getRight(), key);
        else if(cmp<0) return get(root.getLeft(), key);
        else return root.getValue();
    }

    //非递归
    private Value get2(Key key) {
        Node x=root;
        while (x != null) {
            int cmp=key.compareTo(x.getKey());
            if(cmp>0)x=x.right;
            else if(cmp<0)x=x.left;
            else return x.value;
        }
        return null;
    }

//    如果这个值存在，替换返回旧值；否则返回自己
    public void put(Key key,Value value) {
        root= put(root,  key, value);
    }


    private Node put(Node root, Key key,Value value) {
        if (root == null) {
            return new Node(key,value,1);
        }

        int cmp = key.compareTo(root.getKey());
        if(cmp>0)  root.right=put(root.getRight(), key, value);
        else if(cmp<0)  root.left=put(root.getLeft(), key, value);
        else root.value=value;

        root.N=size(root.left)+size(root.right)+1;
        return root;

    }

    public Node getRoot() {
        return root;
    }

    //root要存在
    public Key min() {
        return min(root).key;
    }

    private Node min(Node node) {
        if (node.left == null) return node;
        return min(node.left);
    }

    public Key floor(Key key) {
        Node x = floor(root, key);
        if (x == null) {
            return null;
        }

        return x.key;
    }

    private Node floor(Node root, Key key) {
        if (root == null) {
            return null;
        }
        int cmp = key.compareTo(root.key);
        if(cmp==0)return root;
        if(cmp<0) return floor(root.left, key);
        Node t = floor(root.right, key);
        if (t != null)  return t;
        else return root;
    }

    public void deleteMin() {
        root = deleteMin(root);
    }


    private Node deleteMin(Node root) {

        if (root.left == null) return root.right;

        root.left = deleteMin(root.left);
        root.N=size(root.left)+size(root.right)+1;
        return root;

    }

    public void delete(Key key) {
        root = delete(root, key);
    }

    private Node delete(Node root, Key key) {

        int cmp = key.compareTo(root.key);
        if(cmp>0) root.right = delete(root.right, key);
        else if(cmp<0) root.left = delete(root.left, key);
        else {
            if(root.right==null)return root.left;
            if(root.left==null)return root.right;
            Node t=root;
            root = min(t.right);
            root.right = deleteMin(t.right);
            root.left=t.left;

        }

        root.N=size(root.left)+size(root.right)+1;
        return root;
    }

    class Node {

        private Node left;
        private Node right;
        private Key key;
        private Value value;
        //以该节点为根的总子节点数
        private int N;

         public Node(Key key, Value value,int N) {
             this.key = key;
             this.value = value;
             this.N=N;
         }

         public Node(Node left, Node right, Key key, Value value, int n) {
            this.left = left;
            this.right = right;
            this.key = key;
            this.value = value;
            N = n;
        }


        public Node getLeft() {
            return left;
        }

        public void setLeft(Node left) {
            this.left = left;
        }

        public Node getRight() {
            return right;
        }

        public void setRight(Node right) {
            this.right = right;
        }

        public Key getKey() {
            return key;
        }

        public void setKey(Key key) {
            this.key = key;
        }

        public Value getValue() {
            return value;
        }

        public void setValue(Value value) {
            this.value = value;
        }

        public int getN() {
            return N;
        }

        public void setN(int n) {
            N = n;
        }
    }
}
