import java.util.Arrays;

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
     * private void setTreeAs(AVLTree t)
     * set this instance fields as AVLTree t fields.
     * precondition: t != null.
     * postcondition: None.
     */
    private void setTreeAs(AVLTree t) {
        this.size = t.size;
        this.min = t.min;
        this.max = t.max;
        this.root = t.root;
    }

    /**
     * public String search(int k)
     * <p>
     * returns the info of an item with key k if it exists in the tree
     * otherwise, returns null
     */
    public String search(int k) {
        IAVLNode temp = this.root;
        while (temp.isRealNode()) {
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

    public IAVLNode searchNode(int k) {
        IAVLNode temp = this.root;
        while (temp.isRealNode()) {
            if (temp.getKey() < k) {
                temp = temp.getRight();
            } else if (temp.getKey() > k) {
                temp = temp.getLeft();
            } else {
                return temp;
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
        if (this.root == null) {
            this.root = new AVLNode(k, i);
            this.min = this.root;
            this.max = this.root;
            this.size++;
            return 0;
        }
        IAVLNode temp = this.root;
        IAVLNode tempPar = temp;
        while (temp.isRealNode()) {
            tempPar = temp;
            if (temp.getKey() < k) {
                temp = temp.getRight();
            } else if (temp.getKey() > k) {
                temp = temp.getLeft();
            } else {
                return -1;
            }
        }
        IAVLNode newNode = new AVLNode(k, i);
        if (tempPar.getKey() > k)
            tempPar.setLeft(newNode);
        else
            tempPar.setRight(newNode);
        newNode.setParent(tempPar);
        if (newNode.getKey() > this.max.getKey())
            this.max = newNode;
        if (newNode.getKey() < this.min.getKey())
            this.min = newNode;
        this.size++;
        return rebalanceFromNode(newNode);
    }

    private boolean isFixNeeded(IAVLNode node) {
        if (node == null) {
            return false;
        }
        int rightDiff = (node.getHeight() - node.getRight().getHeight());
        int leftDiff = (node.getHeight() - node.getLeft().getHeight());
        return !((rightDiff >= 1) && (rightDiff <= 2) && (leftDiff >= 1) && (leftDiff <= 2) && (leftDiff + rightDiff < 4));
    }

    public boolean isPromotionNeeded(IAVLNode node) {
        return 2 * node.getHeight() - node.getRight().getHeight() - node.getLeft().getHeight() == 1;
    }

    public int promote(IAVLNode node) {
        node.setHeight(node.getHeight() + 1);
        return 1;
    }

    public int rotateInsertion(IAVLNode node) {
        if (node.getHeight() - node.getLeft().getHeight() == 0) {
            if (node.getLeft().getHeight() - node.getLeft().getLeft().getHeight() == 1) {
                rightRotation(node.getLeft(),0);
                return 1;
            } else {
                leftRightRotation(node.getLeft().getRight(),0);
                return 2;
            }
        } else {
            if (node.getRight().getHeight() - node.getRight().getRight().getHeight() == 1) {
                leftRotation(node.getRight(), 0,0);
                return 1;
            } else {
                rightLeftRotation(node.getRight().getLeft(),0);
                return 2;
            }
        }
    }
    private void leftRightRotation(IAVLNode node, int isDeletion) {
        leftRotation(node, 1, isDeletion);
        rightRotation(node, isDeletion);
        if(isDeletion == 1) {
            node.getLeft().setHeight(node.getLeft().getHeight() - 1);
        }
    }

    private void rightLeftRotation(IAVLNode node, int isDeletion) {
        rightRotation(node, isDeletion);
        leftRotation(node, 1, isDeletion);
        if(isDeletion == 1) {
            node.getRight().setHeight(node.getRight().getHeight() - 1);
        }
    }

    private void leftRotation(IAVLNode node, int doubleAddition, int isDeletion) {
        IAVLNode tempParent = node.getParent();
        if (tempParent == null)
            return;
        updateRootForRotation(node, tempParent);
        tempParent.setParent(node);
        node.getLeft().setParent(tempParent);
        tempParent.setRight(node.getLeft());
        node.setLeft(tempParent);
        node.setHeight(node.getHeight() + doubleAddition);
        tempParent.setHeight(tempParent.getHeight() - 1);
        if (isDeletion>0) {
            if(node.getLeft().getHeight() == node.getRight().getHeight()) // node is 1-1 node
                node.setHeight(node.getHeight() + 1);
            else //node is 1-2 or 2-1 node
                tempParent.setHeight(tempParent.getHeight() - 1);
        }

    }

    private void rightRotation(IAVLNode node, int isDeletion) {
        IAVLNode tempParent = node.getParent();
        if (tempParent == null)
            return;
        updateRootForRotation(node, tempParent);
        tempParent.setParent(node);
        node.getRight().setParent(tempParent);
        tempParent.setLeft(node.getRight());
        node.setRight(tempParent);
        tempParent.setHeight(tempParent.getHeight() - 1);
        if (isDeletion>0) {
            if(node.getLeft().getHeight() == node.getRight().getHeight()) // node is 1-1 node
                node.setHeight(node.getHeight() + 1);
            else //node is 1-2 or 2-1 node
                tempParent.setHeight(tempParent.getHeight() - 1);
        }

    }

    private void updateRootForRotation(IAVLNode node, IAVLNode tempParent) {
        if (tempParent.getParent() != null) {
            node.setParent(tempParent.getParent());
            boolean isLeftChild = tempParent.getParent().getLeft() == tempParent;
            if (isLeftChild)
                node.getParent().setLeft(node);
            else
                node.getParent().setRight(node);
        } else {
            this.root = node;
            node.setParent(null);
        }
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

    /*
    @pre nodeToDelete is not a binary node
    @post $ret is where we want to start rebalancing, if null then we deleted the root- no need to rebalance
     */
    public IAVLNode removeUnaryOrLeaf(IAVLNode nodeToDelete) {
        if (nodeToDelete.getRight().isRealNode()) { // unary with right child
            if (nodeToDelete.getParent() != null) {
                if(isLeftChild(nodeToDelete)) {
                    nodeToDelete.getParent().setLeft(nodeToDelete.getRight());
                } else {
                    nodeToDelete.getParent().setRight(nodeToDelete.getRight());
                }
                nodeToDelete.getRight().setParent(nodeToDelete.getParent());
                return nodeToDelete.getParent();
            } else {
                this.root = nodeToDelete.getRight();
                this.root.setParent(null);
                return null;
            }
        } else if (nodeToDelete.getLeft().isRealNode()) { // unary with left child
            if (nodeToDelete.getParent() != null) {
                if(isLeftChild(nodeToDelete)) {
                    nodeToDelete.getParent().setLeft(nodeToDelete.getLeft());
                } else {
                    nodeToDelete.getParent().setRight(nodeToDelete.getLeft());
                }
                nodeToDelete.getLeft().setParent(nodeToDelete.getParent());
                return nodeToDelete.getParent();
            } else {
                this.root = nodeToDelete.getLeft();
                this.root.setParent(null);
                return null;
            }
        } else { //leaf
            IAVLNode continueNode = nodeToDelete.getParent();
            if(isLeftChild(nodeToDelete)) {
                nodeToDelete.getParent().setLeft(AVLNode.virNode);
                nodeToDelete.setParent(null);
            }
            return continueNode;
        }
    }

    public boolean isLeftChild(IAVLNode node) {
        if(node.getParent() != null) {
            if(node.getParent().getLeft() == node) return true;
        }
        return false;
    }

    /*
    @pre nodeToDelete is a binary node
    @post $ret is where we want to start rebalancing, if null then we deleted the root- no need to rebalance
     */
    public IAVLNode removeBinary(IAVLNode nodeToDelete) {
        IAVLNode succ = successor(nodeToDelete);
        boolean isLeftChild = isLeftChild(nodeToDelete);
        removeUnaryOrLeaf(succ);
        succ.setHeight(nodeToDelete.getHeight());
        if (nodeToDelete.getParent() != null) {
            if (!isLeftChild) {
                nodeToDelete.getParent().setRight(succ);
                succ.setParent(nodeToDelete.getParent());
            } else {
                nodeToDelete.getParent().setLeft(succ);
                succ.setParent(nodeToDelete.getParent());
            }
        } else {
            this.root = succ;
            succ.setParent(null);
        }
        if (nodeToDelete.getRight().isRealNode()) {
            nodeToDelete.getRight().setParent(succ);
            succ.setRight(nodeToDelete.getRight());
        }
        if (nodeToDelete.getLeft().isRealNode()) {
            nodeToDelete.getLeft().setParent(succ);
            succ.setLeft(nodeToDelete.getLeft());
        }
        return succ;
    }

    public int delete(int k) {
        IAVLNode nodeToDelete = searchNode(k);
        if (nodeToDelete == null) return -1;
        int stepCount = 0;
        this.size--;
        if(this.min.getKey() == k) {
            this.min = successor(this.min);
        }
        if(this.max.getKey() == k) {
            this.max = predecessor(this.max);
        }
        IAVLNode startRebalanceNode;
        if (nodeToDelete.getRight().isRealNode() && nodeToDelete.getLeft().isRealNode()) { // if binary
            startRebalanceNode = removeBinary(nodeToDelete);
        } else { // unary or leaf
            startRebalanceNode = removeUnaryOrLeaf(nodeToDelete);
        }
        while (isFixNeeded(startRebalanceNode)) {
            if (isDemoteNeeded(startRebalanceNode)) {
                stepCount += demote(startRebalanceNode);
                startRebalanceNode = startRebalanceNode.getParent();
            }
            else {
                stepCount += deletionRotate(startRebalanceNode);
                startRebalanceNode = startRebalanceNode.getParent().getParent();
            }
        }
        return stepCount;
    }


    public boolean isDemoteNeeded(IAVLNode node) {
        if(node.getHeight() - node.getRight().getHeight() == 2 && node.getHeight() - node.getLeft().getHeight() == 2) {
            return true;
        }
        return false;
    }

    public int deletionRotate(IAVLNode node) {
        if(node.getHeight() - node.getLeft().getHeight() == 3) { // node is 3-1
            if (node.getRight().getHeight() - node.getRight().getRight().getHeight() == 1) {
                leftRotation(node.getRight(),0,1);
                return 1;
            }
            else {
                rightLeftRotation(node.getRight().getLeft(), 1);
                return 2;
            }
        } else { // node is a 1-3
            if(node.getLeft().getHeight() - node.getLeft().getLeft().getHeight() == 1) {
                rightRotation(node.getLeft(), 1);
                return 1;
            }
            else {
                leftRightRotation(node.getLeft().getRight(), 1);
                return 2;
            }
        }
    }

    public int demote(IAVLNode node) {
        node.setHeight(node.getHeight() - 1);
        return 1;
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
        infoToArrayRec(this.root, null, arr, new int[]{0}, true);
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
        infoToArrayRec(this.root, arr, null, new int[]{0}, false);
        return arr;
    }

    private void infoToArrayRec(IAVLNode node, String[] inOrder, int[] inOrderKeys, int[] index, boolean isKeys) {
        if (node.getLeft().isRealNode()) {
            infoToArrayRec(node.getLeft(), inOrder, inOrderKeys, index, isKeys);
        }
        if (isKeys)
            inOrderKeys[index[0]] = node.getKey();
        else
            inOrder[index[0]] = node.getValue();
        index[0] += 1;
        if (node.getRight().isRealNode()) {
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

    public IAVLNode successor(IAVLNode node) {
        if (node == max) {
            return null;
        }
        if (node.getRight().isRealNode()) {
            IAVLNode curr = node.getRight();
            while (curr.getLeft().isRealNode()) {
                curr = curr.getLeft();
            }
            return curr;
        } else {
            IAVLNode curr = node;
            while (node.getParent().getRight() == node) {
                curr = curr.getParent();
            }
            return curr.getParent();
        }
    }

    public IAVLNode predecessor(IAVLNode node) {
        if (node == min) return null;
        else {
            if (node.getLeft().isRealNode()) {
                IAVLNode curr = node.getLeft();
                while (curr.getRight().isRealNode()) {
                    curr = curr.getRight();
                }
                return curr;
            } else {
                IAVLNode curr = node;
                while (node.getParent().getLeft() == node) {
                    curr = node.getParent();
                }
                return node.getParent();
            }
        }
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
     * joins t and x with the tree.
     * Returns the complexity of the operation (|tree.rank - t.rank| + 1).
     * precondition: keys(x,t) < keys() or keys(x,t) > keys(). t/tree might be empty (rank = -1).
     * postcondition: none
     */
    public int join(IAVLNode x, AVLTree t) {
        int complexity = Math.abs(getTreeRank() - t.getTreeRank()) + 1;
        if (t.empty() || this.empty()) {
            AVLTree notEmptyTree = getNotEmptyTree(t, this);
            notEmptyTree.insert(x.getKey(), x.getValue());
            notEmptyTree.size = notEmptyTree.size() + 1;
            setTreeAs(notEmptyTree);
            return complexity;
        }
        AVLTree lower = orderTreesByRootVal(t, this)[0];
        AVLTree higher = orderTreesByRootVal(t, this)[1];
        IAVLNode max = higher.max;
        IAVLNode min = lower.min;
        AVLTree joinedTree;
        if (lower.getTreeRank() > higher.getTreeRank()) {
            joinLowerDeeper(x, lower, higher);
            joinedTree = lower;
        } else if (lower.getTreeRank() < higher.getTreeRank()) {
            joinHigherDeeper(x, lower, higher);
            joinedTree = higher;
        } else {
            joinEqualInDepth(x, lower, higher);
            joinedTree = lower;
        }
        joinedTree.max = max;
        joinedTree.min = min;
        setTreeAs(joinedTree);
        return complexity;
    }

    /**
     * private static joinLowerDeeper(IAVLNode x, AVLTree lower, AVLTree higher)
     * joins 2 AVLTrees with the node x between them - giving that lower is a deeper tree then higher
     * The modified tree will be lower
     * precondition: keys(x,higher) > keys(lower) && lower.getTreeRank() > higher.getTreeRank() > -1.
     * postcondition: None.
     */
    private static void joinLowerDeeper(IAVLNode x, AVLTree lower, AVLTree higher){
        int higherRank = higher.getTreeRank();
        IAVLNode tempNode = lower.getRoot();
        while(tempNode.getHeight() > higherRank){
            tempNode = tempNode.getRight();
        }
        joinNodeInPlace(x, higher.getRoot(), tempNode, higherRank + 1, tempNode.getParent(), true);
        lower.size = lower.size + higher.size + 1;
        lower.rebalanceFromNode(x);
    }

    /**
     * private static joinEqualInDepth(IAVLNode x, AVLTree lower, AVLTree higher)
     * joins 2 AVLTrees with the node x between them - giving that the trees are equal in depth.
     * The modified tree will be lower
     * precondition: keys(x,higher) > keys(lower) && lower.getTreeRank() > higher.getTreeRank() > -1.
     * postcondition: None.
     */
    private static void joinEqualInDepth(IAVLNode x, AVLTree lower, AVLTree higher){

        int treesRank = higher.getTreeRank();
        joinNodeInPlace(x, higher.getRoot(), lower.getRoot(), treesRank + 1, null, true);
        lower.size = lower.size + higher.size + 1;
        lower.root = x;
    }

    /**
     * private static joinHigherDeeper(IAVLNode x, AVLTree lower, AVLTree higher)
     * joins 2 AVLTrees with the node x between them - giving that higher is a deeper tree then lower
     * Returns the complexity of the operation (|higher.rank - lower.rank| + 1)
     * The modified tree will be higher
     * precondition: keys(x,higher) > keys(lower) && higher.getTreeRank() > lower.getTreeRank() < -1.
     * postcondition: None.
     */
    private static void joinHigherDeeper(IAVLNode x, AVLTree lower, AVLTree higher){
        int lowerRank = lower.getTreeRank();
        IAVLNode tempNode = higher.getRoot();
        while(tempNode.getHeight() > lowerRank){
            tempNode = tempNode.getLeft();
        }
        joinNodeInPlace(x, tempNode, lower.getRoot(), lowerRank+1, tempNode.getParent(), false);
        higher.size = lower.size + higher.size + 1;
        higher.rebalanceFromNode(x);
    }


    /**
     * private int rebalanceFromNode(IAVLNode x)
     * rebalancing the current tree from node x.
     * returns the amount of rebalancing steps done
     * precondition: search(x.getKey()) != null
     * postcondition: None.
     */
    private int rebalanceFromNode(IAVLNode node){
        int count = 0;
        while (isFixNeeded(node.getParent())) {
            node = node.getParent();
            if (isPromotionNeeded(node)) {
                count = count + promote(node);
            } else {
                count = count + rotateInsertion(node);
            }
        }
        return count;
    }

    private static void joinNodeInPlace(IAVLNode x, IAVLNode rightChild, IAVLNode leftChild, int rank, IAVLNode parent, boolean isRightChild){
        x.setRight(rightChild);
        x.setLeft(leftChild);
        x.setParent(parent);
        if(parent != null && isRightChild){
            x.getParent().setRight(x);
        } else if(parent != null){
            x.getParent().setLeft(x);
        }
        x.setHeight(rank);
        rightChild.setParent(x);
        leftChild.setParent(x);
    }

    /**
     * private static getNotEmptyTree(AVLTree t, AVLTree s)
     * returns a non-empty tree if exists else returns s.
     * precondition: None.
     * postcondition: None.
     */
    private static AVLTree getNotEmptyTree(AVLTree t, AVLTree s) {
        if (t.empty()) {
            return s;
        }
        return t;
    }

    /**
     * private static orderTreesByRootVal(AVLTree t, AVLTree s)
     * Checks which root key is larger.
     * Returns array that in the first item is the tree with the smaller root, and the second is the other tree.
     * precondition: (t.empty() and s.empty()) == false.
     * postcondition: None.
     */
    private static AVLTree[] orderTreesByRootVal(AVLTree t, AVLTree s) {
        if (t.root.getKey() > s.root.getKey()) {
            return new AVLTree[]{s, t};
        }
        return new AVLTree[]{t, s};
    }

    private int getTreeRank() {
        return this.root.getHeight();
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
    public static class AVLNode implements IAVLNode {
        private int key;
        private String val;
        private IAVLNode left;
        private IAVLNode right;
        private int height;
        private IAVLNode parent;
        private int[] rD;
        private static IAVLNode virNode = new AVLNode();

        public AVLNode() {
            this.key = -1;
            this.height = -1;
            this.parent = null;
        }

        public AVLNode(int key, String val) {
            this.key = key;
            this.val = val;
            this.left = virNode;
            this.right = virNode;
            this.height = 0;
            this.parent = null;
        }

        public int getLeftChildRankDiff() {
            return this.rD[0];
        }

        public int getRightChildRankDiff() {
            return this.rD[1];
        }

        public void setLeftChildRankDiff(int diff) {
            this.rD[0] = diff;
        }

        public void setRightChildRankDiff(int diff) {
            this.rD[1] = diff;
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
  

