import sun.reflect.generics.tree.Tree;

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
        this.min = null;
        this.max = null;
        this.root = null;
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
        if (this.size > 0) {
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
        }
        return null;
    }

    /**
     * public String search(int k)
     * <p>
     * returns the node of an item with key k if it exists in the tree
     * otherwise, returns null
     */
    public IAVLNode searchNode(int k) {
        if (this.size > 0) {
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
        // addressing an edge case if the tree is empty
        if (this.root == null) {
            this.root = new AVLNode(k, i);
            this.min = this.root;
            this.max = this.root;
            this.size++;
            return 0;
        }

        // determining where to insert the new node
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
        // inserting the node at the place we found earlier
        IAVLNode newNode = new AVLNode(k, i);
        if (tempPar.getKey() > k)
            tempPar.setLeft(newNode);
        else
            tempPar.setRight(newNode);
        newNode.setParent(tempPar);
        // updating min/max if necessary
        if (newNode.getKey() > this.max.getKey())
            this.max = newNode;
        if (newNode.getKey() < this.min.getKey())
            this.min = newNode;
        this.size++;
        // starting rebalance process from new node up
        return rebalanceFromNode(newNode);
    }


    /* determines if the node we're currently at needs a balance operation or not */

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
        node.resetSize();
        return 1;
    }


    /*
    * a decision-making function if we detected that some kind of rotation is needed
    * determines which rotation do we make, and returns number of rotations (single/double)
    *  */
    public int rotateInsertion(IAVLNode node) {
        if (node.getHeight() - node.getLeft().getHeight() == 0) {
            if (node.getLeft().getHeight() - node.getLeft().getLeft().getHeight() == 1) {
                rightRotation(node.getLeft());
                return 1;
            } else {
                leftRightRotation(node.getLeft().getRight());
                return 2;
            }
        } else {
            if (node.getRight().getHeight() - node.getRight().getRight().getHeight() == 1) {
                leftRotation(node.getRight());
                return 1;
            } else {
                rightLeftRotation(node.getRight().getLeft());
                return 2;
            }
        }
    }

    private void leftRightRotation(IAVLNode node) {
        leftRotation(node);
        rightRotation(node);

    }

    private void rightLeftRotation(IAVLNode node) {
        rightRotation(node);
        leftRotation(node);

    }
    /*
    * rotates left the edge between node and node.parent
    * updates heights and sizes for the necessary nodess
    *  */
    private void leftRotation(IAVLNode node) {
        IAVLNode tempParent = node.getParent();
        if (tempParent == null)
            return;
        updateRootForRotation(node, tempParent);
        tempParent.setParent(node);
        node.getLeft().setParent(tempParent);
        tempParent.setRight(node.getLeft());
        node.setLeft(tempParent);
        tempParent.fixHeight();
        tempParent.resetSize();
        node.fixHeight();
        node.resetSize();
    }
    /*
     * rotates right the edge between node and node.parent
     * updates heights and sizes for the necessary nodess
     *  */
    private void rightRotation(IAVLNode node) {
        IAVLNode tempParent = node.getParent();
        if (tempParent == null)
            return;
        updateRootForRotation(node, tempParent);
        tempParent.setParent(node);
        node.getRight().setParent(tempParent);
        tempParent.setLeft(node.getRight());
        node.setRight(tempParent);
        tempParent.fixHeight();
        tempParent.resetSize();
        node.fixHeight();
        node.resetSize();
    }

    /*
    * updates the necessary pointers of the root of the subtree changed by a rotation
    */
    private void updateRootForRotation(IAVLNode node, IAVLNode tempParent) {
        if (tempParent.getParent() != null) {
            node.setParent(tempParent.getParent());
            if (tempParent.isLeftChild())
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

    public int delete(int k) {
        IAVLNode nodeToDelete = searchNode(k);
        if (nodeToDelete == null) return -1;
        int stepCount = 0;
        this.size--;
        // addressing a deletion from a 1 sized tree
        if(size == 0) {
            setToEmptyTree();
            return 0;
        }
        // updating min and max if necessary
        if (this.min.getKey() == k) {
            this.min = successor(this.min);
        }
        if (this.max.getKey() == k) {
            this.max = predecessor(this.max);
        }


        IAVLNode startRebalanceNode;
        // addressing a deletion of a binary node
        if (nodeToDelete.getRight().isRealNode() && nodeToDelete.getLeft().isRealNode()) {
            startRebalanceNode = removeBinary(nodeToDelete);
        } else {
            // addressing a deletion of an unary/leaf node
            startRebalanceNode = removeUnaryOrLeaf(nodeToDelete);
        }
        // going up untill the root, rebalancing the tree and updating node sizes
        while (startRebalanceNode != null) {
            if(isFixNeeded(startRebalanceNode)) {
                if (isDemoteNeeded(startRebalanceNode)) {
                    stepCount += demote(startRebalanceNode);
                    startRebalanceNode = startRebalanceNode.getParent();
                } else {
                    stepCount += deletionRotate(startRebalanceNode);
                    startRebalanceNode = startRebalanceNode.getParent().getParent();
                }
            } else {
                startRebalanceNode.resetSize();
                startRebalanceNode = startRebalanceNode.getParent();
            }
        }
        return stepCount;
    }

    /*
    @pre nodeToDelete is not a binary node
    @post $ret is where we want to start rebalancing, if null then we deleted the root- no need to rebalance
     */
    public IAVLNode removeUnaryOrLeaf(IAVLNode nodeToDelete) {
        if (nodeToDelete.getRight().isRealNode()) { // unary with right child
            if (nodeToDelete.getParent() != null) {
                if(nodeToDelete.isLeftChild()) {
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
                if(nodeToDelete.isLeftChild()) {
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
            if(nodeToDelete.isLeftChild()) {
                nodeToDelete.getParent().setLeft(AVLNode.virNode);
            } else {
                nodeToDelete.getParent().setRight(AVLNode.virNode);
            }
            nodeToDelete.setParent(null);
            return continueNode;
        }
    }

    public void setToEmptyTree() {
        this.size = 0;
        this.min = null;
        this.max = null;
        this.root = null;
    }



    /*
    @pre nodeToDelete is a binary node
    @post $ret is where we want to start rebalancing
    deleting a binary node as we learned in class
     */
    public IAVLNode removeBinary(IAVLNode nodeToDelete) {
        IAVLNode succ = successor(nodeToDelete);
        boolean isLeftChild = nodeToDelete.isLeftChild();
        IAVLNode nodeToContinue = removeUnaryOrLeaf(succ);
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
        return nodeToContinue;
    }




    public boolean isDemoteNeeded(IAVLNode node) {
        if (node.getHeight() - node.getRight().getHeight() == 2 && node.getHeight() - node.getLeft().getHeight() == 2) {
            return true;
        }
        return false;
    }
    /*
    * a decision making function that determines which rotate is necessary if we detected that some
    *  kind of rotation is necessary
    */
    public int deletionRotate(IAVLNode node) {
        if (node.getHeight() - node.getLeft().getHeight() == 3) { // node is 3-1
            if (node.getRight().getHeight() - node.getRight().getRight().getHeight() == 1) {
                leftRotation(node.getRight());
                return 1;
            } else {
                rightLeftRotation(node.getRight().getLeft());
                return 2;
            }
        } else { // node is a 1-3
            if (node.getLeft().getHeight() - node.getLeft().getLeft().getHeight() == 1) {
                rightRotation(node.getLeft());
                return 1;
            } else {
                leftRightRotation(node.getLeft().getRight());
                return 2;
            }
        }
    }

    public int demote(IAVLNode node) {
        node.setHeight(node.getHeight() - 1);
        node.resetSize();
        return 1;
    }


    /**
     * public String min()
     * <p>
     * Returns the info of the item with the smallest key in the tree,
     * or null if the tree is empty
     */
    public String min() {
        if (size > 0) {
            return this.min.getValue();
        } else
            return null;
    }

    /**
     * public String max()
     * <p>
     * Returns the info of the item with the largest key in the tree,
     * or null if the tree is empty
     */
    public String max() {
        if (size > 0) {
            return this.max.getValue();
        } else
            return null;
    }

    /**
     * public int[] keysToArray()
     * <p>
     * Returns a sorted array which contains all keys in the tree,
     * or an empty array if the tree is empty.
     */
    public int[] keysToArray() {
        int[] arr = new int[this.size];
        if (size > 0) {
            infoToArrayRec(this.root, null, arr, new int[]{0}, true);
        }
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
        return this.size;
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
    /*
    * finding the successor of a node in O(logn) as we learned in class
    */
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
            while (curr.getParent().getRight() == curr) {
                curr = curr.getParent();
            }
            return curr.getParent();
        }
    }
    /*
     * finding the predecessor of a node in O(logn) as we learned in class
     */
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
                while (curr.getParent().getLeft() == curr) {
                    curr = curr.getParent();
                }
                return curr.getParent();
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
        IAVLNode xNode = searchNode(x);
        AVLTree smallerTree = new AVLTree();
        AVLTree biggerTree = new AVLTree();
        // computing min/max values for both the trees we will be returning
        IAVLNode minOfSmaller = getMinAndMaxForSplit(xNode)[0];
        IAVLNode maxOfBigger = getMinAndMaxForSplit(xNode)[1];
        IAVLNode maxOfSmaller = predecessor(xNode);
        IAVLNode minOfBigger = successor(xNode);
        // adding xNodes' left and right subtrees to smaller/bigger accordingly
        if(xNode.getLeft().isRealNode())
            smallerTree =  seperateSubTree(xNode.getLeft());
        if(xNode.getRight().isRealNode())
            biggerTree = seperateSubTree(xNode.getRight());
        IAVLNode nextNode = xNode.getParent();
        // going up until the root, joining all subtrees into smaller/bigger as learned in class
        while(xNode.getParent() != null) {
            IAVLNode nodeForJoin = new AVLNode(xNode.getParent().getKey(), xNode.getParent().getValue());
            if(!xNode.isLeftChild()) {
                // join smallerTree with xNode and his left subtree
                smallerTree.join(nodeForJoin, seperateSubTree(xNode.getParent().getLeft()));
            } else {
                // join biggerTree with xNode and his right subtree
                biggerTree.join(nodeForJoin,seperateSubTree(xNode.getParent().getRight()));
            }
            xNode = xNode.getParent();
        }
        // updating min/max values for res trees
        smallerTree.min = minOfSmaller;
        smallerTree.max = maxOfSmaller;
        biggerTree.min = minOfBigger;
        biggerTree.max = maxOfBigger;
        AVLTree[] res = {smallerTree, biggerTree};
        return res;
    }

    public IAVLNode[] getMinAndMaxForSplit(IAVLNode xNode) {
        IAVLNode maxOfBigger;
        IAVLNode minOfSmaller;
        if(xNode!= this.max)
            maxOfBigger = this.max;
        else
            maxOfBigger = predecessor(this.max);
        if(xNode!= this.min)
            minOfSmaller = this.min;
        else
            minOfSmaller = successor(this.min);
        return new IAVLNode[]{minOfSmaller, maxOfBigger};
    }

    /*
    *  seperates a subtree which root is node from the tree.
    *  min/max values of the returning subtree are not correct and will be updated once split finishes
    */
    public AVLTree seperateSubTree(IAVLNode node) {
        AVLTree res = new AVLTree();
        res.root = node;
        node.setParent(null);
        res.size = node.getSize();
        res.min = node; // not a true val, will be updated when split finishes
        res.max = node; // not a true val, will be updated when split finishes
        return res;
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
            setTreeAs(notEmptyTree);
            return complexity;
        }
        AVLTree lower = orderTreesByRootVal(t, this)[0];
        AVLTree higher = orderTreesByRootVal(t, this)[1];
        IAVLNode max = higher.max;
        IAVLNode min = lower.min;
        AVLTree joinedTree;
        if (lower.getTreeRank() > higher.getTreeRank() + 1) {
            joinLowerDeeper(x, lower, higher);
            joinedTree = lower;
        } else if (lower.getTreeRank() + 1 < higher.getTreeRank()) {
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
    private static void joinLowerDeeper(IAVLNode x, AVLTree lower, AVLTree higher) {
        int higherRank = higher.getTreeRank();
        IAVLNode tempNode = lower.getRoot();
        while (tempNode.getHeight() > higherRank && tempNode.getRight().isRealNode()) {
            tempNode = tempNode.getRight();
        }
        joinNodeInPlace(x, higher.getRoot(), tempNode, Math.max(tempNode.getHeight(), higherRank) + +1, tempNode.getParent(), true);
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
    private static void joinEqualInDepth(IAVLNode x, AVLTree lower, AVLTree higher) {
        joinNodeInPlace(x, higher.getRoot(), lower.getRoot(), Math.max(higher.getTreeRank(), lower.getTreeRank()) + 1, null, true);
        lower.size = lower.size + higher.size + 1;
        lower.root = x;
        x.resetSize();
    }

    /**
     * private static joinHigherDeeper(IAVLNode x, AVLTree lower, AVLTree higher)
     * joins 2 AVLTrees with the node x between them - giving that higher is a deeper tree then lower
     * Returns the complexity of the operation (|higher.rank - lower.rank| + 1)
     * The modified tree will be higher
     * precondition: keys(x,higher) > keys(lower) && higher.getTreeRank() > lower.getTreeRank() < -1.
     * postcondition: None.
     */
    private static void joinHigherDeeper(IAVLNode x, AVLTree lower, AVLTree higher) {
        int lowerRank = lower.getTreeRank();
        IAVLNode tempNode = higher.getRoot();
        while (tempNode.getHeight() > lowerRank && tempNode.getLeft().isRealNode()) {
            tempNode = tempNode.getLeft();
        }
        joinNodeInPlace(x, tempNode, lower.getRoot(), Math.max(tempNode.getHeight(), lowerRank) + 1, tempNode.getParent(), false);
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
    private int rebalanceFromNode(IAVLNode node) {
        int count = 0;
        node.resetSize();
        while (node.getParent() != null) {
            node = node.getParent();
            if(isFixNeeded(node)) {
                if (isPromotionNeeded(node)) {
                    count = count + promote(node);
                } else {
                    count = count + rotateInsertion(node);
                    node = node.getParent();
                }
            } else {
                node.resetSize();
            }
        }
        return count;
    }

    private static void joinNodeInPlace(IAVLNode x, IAVLNode rightChild, IAVLNode leftChild, int rank, IAVLNode parent, boolean isRightChild) {
        x.setRight(rightChild);
        x.setLeft(leftChild);
        x.setParent(parent);
        if (parent != null && isRightChild) {
            x.getParent().setRight(x);
        } else if (parent != null) {
            x.getParent().setLeft(x);
        }
        x.setHeight(rank);
        rightChild.setParent(x);
        leftChild.setParent(x);
        x.resetSize();
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
        if (this.size > 0) {
            return this.root.getHeight();
        } else return -1;
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

        public void fixHeight(); // sets this's height to max(left.height,right.height) + 1

        public boolean isLeftChild(); // t iff this is a left child of his parent

        public int getSize();

        public void setSize(int size);

        public void resetSize(); // sets the size to left.size + right.size + 1
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
        private int size;
        private static IAVLNode virNode = new AVLNode();

        public AVLNode() {
            this.key = -1;
            this.height = -1;
            this.parent = null;
            this.size = 0;
        }

        public AVLNode(int key, String val) {
            this.key = key;
            this.val = val;
            this.left = virNode;
            this.right = virNode;
            this.height = 0;
            this.parent = null;
            this.size = 1;
        }


        public boolean isLeftChild() {
            if(this.getParent() != null) {
                if(this.getParent().getLeft() == this) return true;
            }
            return false;
        }
        @Override
        public void fixHeight() {
            this.height = Integer.max(this.left.getHeight(), this.right.getHeight()) + 1;
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

        public int getSize() {return this.size;}

        public void setSize(int size) {this.size = size;}

        public void resetSize() {this.size = this.getLeft().getSize() + this.getRight().getSize() + 1;}
    }

}
  

