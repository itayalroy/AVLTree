/**
 * AVLTree
 * <p>
 * An implementation of a AVL Tree with
 * distinct integer keys and info
 */

public class AVLTree {
    private IAVLNode min;
    private IAVLNode max;
    private IAVLNode root;
    private int size;

    public AVLTree() {
        this.size = 0;
        this.min = new AVLNode(Integer.MAX_VALUE, null);
        this.max = new AVLNode(Integer.MIN_VALUE, null);
    }

    /**
     * public boolean empty()
     * <p>
     * returns true if and only if the tree is empty
     */
    public boolean empty() {
        return this.size == 0;
    }

    /**
     * public String search(int k)
     * <p>
     * returns the info of an item with key k if it exists in the tree
     * otherwise, returns null
     */
    public String search(int k) {
        IAVLNode temp = this.root;
        while (temp != null) {
            if (temp.getKey() < k) {
                temp = temp.getRight();
            } else if (temp.getKey() > k) {
                temp = temp.getLeft();
            } else {
                return temp.getValue();
            }
        }
        return null;
    }

    /**
     * public int insert(int k, String i)
     * <p>
     * inserts an item with key k and info i to the AVL tree.
     * the tree must remain valid (keep its invariants).
     * returns the number of rebalancing operations, or 0 if no rebalancing operations were necessary.
     * promotion/rotation - counted as one rebalnce operation, double-rotation is counted as 2.
     * returns -1 if an item with key k already exists in the tree.
     */
    public int insert(int k, String i) {
        return 42;    // to be replaced by student code
    }

    private void leftRotation(IAVLNode node){
        IAVLNode tempParent = node.getParent();
        if(tempParent == null)
            return;
        node.setParent(tempParent.getParent());
        tempParent.setParent(node);
        tempParent.setRight(node.getLeft());
        node.setLeft(tempParent);
    }

    private void rightRotation(IAVLNode node){
        IAVLNode tempParent = node.getParent();
        if(tempParent == null)
            return;
        node.setParent(tempParent.getParent());
        tempParent.setParent(node);
        tempParent.setLeft(node.getRight());
        node.setRight(tempParent);
    }

    /**
     * public int delete(int k)
     * <p>
     * deletes an item with key k from the binary tree, if it is there;
     * the tree must remain valid (keep its invariants).
     * returns the number of rebalancing operations, or 0 if no rebalancing operations were needed.
     * demotion/rotation - counted as one rebalnce operation, double-rotation is counted as 2.
     * returns -1 if an item with key k was not found in the tree.
     */
    public int delete(int k) {
        return 42;    // to be replaced by student code
    }

    /**
     * public String min()
     * <p>
     * Returns the info of the item with the smallest key in the tree,
     * or null if the tree is empty
     */
    public String min() {
        return this.min.getValue();
    }

    /**
     * public String max()
     * <p>
     * Returns the info of the item with the largest key in the tree,
     * or null if the tree is empty
     */
    public String max() {
        return this.max.getValue();
    }

    /**
     * public int[] keysToArray()
     * <p>
     * Returns a sorted array which contains all keys in the tree,
     * or an empty array if the tree is empty.
     */
    public int[] keysToArray() {
        int[] arr = new int[this.size];
        infoToArrayRec(this.root, null, arr, new Integer(0), true);
        return arr;
    }

    /**
     * public String[] infoToArray()
     * <p>
     * Returns an array which contains all info in the tree,
     * sorted by their respective keys,
     * or an empty array if the tree is empty.
     */
    public String[] infoToArray() {
        String[] arr = new String[this.size];
        infoToArrayRec(this.root, arr, null, new Integer(0), false);
        return arr;
    }

    private void infoToArrayRec(IAVLNode node, String[] inOrder, int[] inOrderKeys, Integer index, boolean isKeys) {
        if (node.getLeft() != null) {
            infoToArrayRec(node.getLeft(), inOrder, inOrderKeys, index, isKeys);
        }
        if (isKeys)
            inOrderKeys[index] = node.getKey();
        else
            inOrder[index] = node.getValue();
        index += 1;
        if (node.getRight() != null) {
            infoToArrayRec(node.getRight(), inOrder, inOrderKeys, index, isKeys);
        }
    }

    /**
     * public int size()
     * <p>
     * Returns the number of nodes in the tree.
     * <p>
     * precondition: none
     * postcondition: none
     */
    public int size() {
        return this.size; // to be replaced by student code
    }

    /**
     * public int getRoot()
     * <p>
     * Returns the root AVL node, or null if the tree is empty
     * <p>
     * precondition: none
     * postcondition: none
     */
    public IAVLNode getRoot() {
        return this.root;
    }

    /**
     * public string split(int x)
     * <p>
     * splits the tree into 2 trees according to the key x.
     * Returns an array [t1, t2] with two AVL trees. keys(t1) < x < keys(t2).
     * precondition: search(x) != null (i.e. you can also assume that the tree is not empty)
     * postcondition: none
     */
    public AVLTree[] split(int x) {
        return null;
    }

    /**
     * public join(IAVLNode x, AVLTree t)
     * <p>
     * joins t and x with the tree.
     * Returns the complexity of the operation (|tree.rank - t.rank| + 1).
     * precondition: keys(x,t) < keys() or keys(x,t) > keys(). t/tree might be empty (rank = -1).
     * postcondition: none
     */
    public int join(IAVLNode x, AVLTree t) {
        return 0;
    }

    /**
     * public interface IAVLNode
     * ! Do not delete or modify this - otherwise all tests will fail !
     */
    public interface IAVLNode {
        public int getKey(); //returns node's key (for virtuval node return -1)

        public String getValue(); //returns node's value [info] (for virtuval node return null)

        public void setLeft(IAVLNode node); //sets left child

        public IAVLNode getLeft(); //returns left child (if there is no left child return null)

        public void setRight(IAVLNode node); //sets right child

        public IAVLNode getRight(); //returns right child (if there is no right child return null)

        public void setParent(IAVLNode node); //sets parent

        public IAVLNode getParent(); //returns the parent (if there is no parent return null)

        public boolean isRealNode(); // Returns True if this is a non-virtual AVL node

        public void setHeight(int height); // sets the height of the node

        public int getHeight(); // Returns the height of the node (-1 for virtual nodes)
    }

    /**
     * public class AVLNode
     * <p>
     * If you wish to implement classes other than AVLTree
     * (for example AVLNode), do it in this file, not in
     * another file.
     * This class can and must be modified.
     * (It must implement IAVLNode)
     */
    public class AVLNode implements IAVLNode {
        private int key;
        private String val;
        private IAVLNode left;
        private IAVLNode right;
        private int height;
        private IAVLNode parent;

        public AVLNode(int key, String val) {
            this.key = key;
            this.val = val;
            this.left = null;
            this.right = null;
            this.height = -1;
            this.parent = null;
        }


        public int getKey() {
            return key;
        }

        public String getValue() {
            return val;
        }

        public void setLeft(IAVLNode node) {
            this.left = node;
        }

        public IAVLNode getLeft() {
            return this.left;
        }

        public void setRight(IAVLNode node) {
            this.right = node;
        }

        public IAVLNode getRight() {

            return this.right;
        }

        public void setParent(IAVLNode node) {
            this.parent = node;
        }

        public IAVLNode getParent() {
            return this.parent;
        }

        public boolean isRealNode() {
            return this.key != -1;
        }

        public void setHeight(int height) {
            this.height = height;
        }

        public int getHeight() {
            return this.height;
        }
    }

}
  

