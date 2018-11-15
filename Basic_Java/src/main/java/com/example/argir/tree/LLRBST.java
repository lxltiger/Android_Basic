package com.example.argir.tree;

/**
 * Left lean Red Black Search Tree
 * 左链接红黑树 只能节点的左边是红色，
 * 右边是红色需要左转
 * 左边连续两个红色需要右转
 *
 * @param <Key>
 * @param <Value>
 */
public class LLRBST<Key extends Comparable<Key>, Value> {


    private Node root;
    private static final boolean RED = true;
    private static final boolean BLACK = false;


    public int size() {
        return size(root);
    }


    public int size(Node node) {
        if (node == null) {
            return 0;
        }
        return node.getN();
    }

    public void listElement() {
        print(root);
        System.out.println();
    }

    private void print(Node h) {
        if (h == null) {
            return;
        }
        printNode(h);
        print(h.left);
        print(h.right);
    }

    private void printNode(Node node) {
        if (node != null) {
            System.out.print(node);
            if (node.left != null) {
                System.out.print(node.left);
            }
            if (node.right != null) {
                System.out.print(node.right);
            }
        }
    }

    public Value get(Key key) {
        return get(root, key);
    }

    //命中返回值否则返回Null
    private Value get(Node root, Key key) {
        if (root == null) {
            return null;
        }
        int cmp = key.compareTo(root.getKey());
        if (cmp > 0) return get(root.getRight(), key);
        else if (cmp < 0) return get(root.getLeft(), key);
        else return root.getValue();
    }

    //非递归
    private Value get2(Key key) {
        Node x = root;
        while (x != null) {
            int cmp = key.compareTo(x.getKey());
            if (cmp > 0) x = x.right;
            else if (cmp < 0) x = x.left;
            else return x.value;
        }
        return null;
    }

    //    如果这个值存在，替换返回旧值
    public void put(Key key, Value value) {
        root = put(root, key, value);
        root.color = BLACK;
    }

    /**
     * 红黑树的插入
     *
     * @param h
     * @param key
     * @param value
     * @return
     */
    private Node put(Node h, Key key, Value value) {
        if (h == null) {
            return new Node(key, value, RED, 1);
        }

        int cmp = key.compareTo(h.getKey());
        if (cmp > 0) h.right = put(h.getRight(), key, value);
        else if (cmp < 0) h.left = put(h.getLeft(), key, value);
        else h.value = value;

        if (isRed(h.right) && !isRed(h.left)) h = rotateLeft(h);
        if (isRed(h.left) && isRed(h.left.left)) h = rotateRight(h);
        if (isRed(h.left) && isRed(h.right)) flip(h);

        h.N = size(h.left) + size(h.right) + 1;
        return h;

    }

    private void flip(Node h) {
        h.left.color = BLACK;
        h.right.color = BLACK;
        h.color = RED;

    }

    private void flipColor(Node h) {
        h.left.color = !h.left.color;
        h.right.color = !h.right.color;
        h.color = !h.color;

    }

    private boolean isRed(Node h) {
        if (h == null) {
            return false;
        }
        return RED == h.color;
    }

    private Node rotateRight(Node h) {
        Node x = h.left;
        h.left = x.right;
        x.right = h;

        x.color = h.color;
        h.color = RED;
        return x;
    }

    private Node rotateLeft(Node h) {
        Node x = h.right;
        h.right = x.left;
        x.left = h;

        x.color = h.color;
        h.color = RED;
        return x;


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


    public void deleteMin() {
        if (!isRed(root.left) && !isRed(root.right)) {
            root.color=RED;
        }
        root = deleteMin(root);
        if (root != null) {
            root.color = BLACK;
        }
    }


    //    一个节点能删除的前提是自己是红色的或父节点是红色的，不满足这个条件就需要提前向下分解红链接
    private Node deleteMin(Node h) {

        if (h.left == null) return null;

        //保证当前节点不是单键
        if (!isRed(h.left) && !isRed(h.left.left)) {
            h = turnRedLeft(h);
        }

        h.left = deleteMin(h.left);

        return fixUp(h);

    }

    private void deleteMax() {
        if (!isRed(root.left) && !isRed(root.right)) {
            root.color=RED;
        }
        root = deleteMax(root);
        if (root != null) {
            root.color = BLACK;
        }
    }

    private Node deleteMax(Node h) {

        if (isRed(h.left)) {
            h = rotateRight(h);
        }

        if (h.right == null) {
            return null;
        }

        if (!isRed(h.right) && !isRed(h.right.left)) {
            h = turnRedRight(h);
        }

        h.right = deleteMax(h.right);


        return fixUp(h);
    }

    private Node turnRedRight(Node h) {
        flipColor(h);
        if (isRed(h.left.left)) {
            h = rotateRight(h);
        }
        return h;
    }

    private Node turnRedLeft(Node h) {
        flipColor(h);
        if (isRed(h.right.left)) {
            h.right = rotateRight(h.right);
            h = rotateLeft(h);
        }

        return h;
    }


    private void delete(Key key) {
        if (!isRed(root.left) && !isRed(root.right)) {
            root.color=RED;
        }
        root = delete(root,key);
        if (root != null) {
            root.color = BLACK;
        }
    }


    private Node delete(Node h, Key key) {
        if (key.compareTo(h.key) < 0) {
            //往左边，按删除最小键逻辑
            if (!isRed(h.left) && !isRed(h.left.left)) {
                h = turnRedLeft(h);
            }
            h.left = delete(h.left, key);
        }else{
//            进入找到或者在右边子树中

            if (isRed(h.left)) {
                h = rotateRight(h);
            }
            //找到了 并且是在底部
            if (key.compareTo(h.key) == 0 && h.right == null) {
                return null;
            }
//            先确保下面的节点满足条件
            if (!isRed(h.right) && !isRed(h.right.left)) {
                h = turnRedRight(h);
            }

            //找到了
            if (key.compareTo(h.key) == 0) {
                h.value=get(h.right,min(h.right).key);
                h.key = min(h.right).key;
                h.right = deleteMin(h.right);
            }else{
                //没找到继续
                h.right = delete(h.right, key);
            }



        }

        return fixUp(h);
    }

    private Node fixUp(Node h) {
        if (isRed(h.right) && !isRed(h.left)) h = rotateLeft(h);
        if (isRed(h.left) && isRed(h.left.left)) h = rotateRight(h);
        if (isRed(h.left) && isRed(h.right)) flipColor(h);
        h.N = size(h.left) + size(h.right) + 1;

        return h;
    }


    class Node {

        private Node left;
        private Node right;
        private Key key;
        private Value value;
        private boolean color;
        //以该节点为根的总子节点数
        private int N;

        public Node(Key key, Value value, boolean color, int N) {
            this.key = key;
            this.value = value;
            this.color = color;
            this.N = N;
        }

        public Node(Node left, Node right, Key key, Value value, int n) {
            this.left = left;
            this.right = right;
            this.key = key;
            this.value = value;
            N = n;
        }

        @Override
        public String toString() {
            return key + "-" + (color ? "R " : "B ");
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
