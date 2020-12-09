import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class TreePrinter {
    public static <T extends Comparable<?>> void printNode(AVLTree.IAVLNode root) {
        int maxLevel = TreePrinter.maxLevel(root);

        printNodeInternal(Collections.singletonList(root), 1, maxLevel);
    }

    private static <T extends Comparable<?>> void printNodeInternal(List<AVLTree.IAVLNode> nodes, int level, int maxLevel) {
        if (nodes.isEmpty() || TreePrinter.isAllElementsNull(nodes))
            return;

        int floor = maxLevel - level;
        int endgeLines = (int) Math.pow(2, (Math.max(floor - 1, 0)));
        int firstSpaces = (int) Math.pow(2, (floor)) - 1;
        int betweenSpaces = (int) Math.pow(2, (floor + 1)) - 1;

        TreePrinter.printWhitespaces(firstSpaces);

        List<AVLTree.IAVLNode> newNodes = new ArrayList<AVLTree.IAVLNode>();
        for (AVLTree.IAVLNode node : nodes) {
            if (node != null && node.isRealNode()) {
                System.out.print(node.getKey());
                newNodes.add(node.getLeft());
                newNodes.add(node.getRight());
            } else {
                newNodes.add(null);
                newNodes.add(null);
                System.out.print(" ");
            }

            TreePrinter.printWhitespaces(betweenSpaces);
        }
        System.out.println("");

        for (int i = 1; i <= endgeLines; i++) {
            for (int j = 0; j < nodes.size(); j++) {
                TreePrinter.printWhitespaces(firstSpaces - i);
                if (nodes.get(j) == null) {
                    TreePrinter.printWhitespaces(endgeLines + endgeLines + i + 1);
                    continue;
                }

                if (nodes.get(j).getLeft() != null)
                    System.out.print("/");
                else
                    TreePrinter.printWhitespaces(1);

                TreePrinter.printWhitespaces(i + i - 1);

                if (nodes.get(j).getRight() != null)
                    System.out.print("\\");
                else
                    TreePrinter.printWhitespaces(1);

                TreePrinter.printWhitespaces(endgeLines + endgeLines - i);
            }

            System.out.println("");
        }

        printNodeInternal(newNodes, level + 1, maxLevel);
    }

    private static void printWhitespaces(int count) {
        for (int i = 0; i < count; i++)
            System.out.print(" ");
    }

    private static <T extends Comparable<?>> int maxLevel(AVLTree.IAVLNode node) {
        if (node == null)
            return 0;

        return Math.max(TreePrinter.maxLevel(node.getLeft()), TreePrinter.maxLevel(node.getRight())) + 1;
    }

    private static <T> boolean isAllElementsNull(List<T> list) {
        for (Object object : list) {
            if (object != null)
                return false;
        }

        return true;
    }


        public static void main(String[] args) {
            AVLTree tree = new AVLTree();
            tree.insert(18, null);
            tree.insert(19, null);

            printNode(tree.getRoot());
            System.out.println(tree.getRoot().getHeight());
            System.out.println(tree.size());
            AVLTree tree2 = new AVLTree();
            tree2.insert(16, null);

            tree.join(new AVLTree.AVLNode(17, null), tree2);

            printNode(tree.getRoot());
            System.out.println(tree.getRoot().getHeight());
            System.out.println(tree.size());

            int[] values4 = new int[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10};

            int actualOperations = 0;
            int avlOperations = 0;
            AVLTree avlTree = new AVLTree();
            for (int aValues4: values4){
                avlTree.insert(aValues4, ""+aValues4);
            }
            int n = 0;
            for (int aValues4 : values4) {
                avlOperations += avlTree.delete(values4[aValues4 - 1]);
                if (avlTree.size() > 0) {
                    // while avlTree is not empty, checking the min & max values
                    if ((!avlTree.max().equals(avlTree.max())) ||
                            (!avlTree.min().equals(avlTree.min()))) {
                        n++;
                    }
                } else {
                    // if all items were deleted from avlTree, check if RBTree is empty as well
                    if (!avlTree.empty()) {
                        n++;
                    }
                }
            }
            for (int val : values4) {
                // checking that all the values that were supposed to be deleted are not in the RBTree
                if (!(avlTree.search(val) == null)) {
                    n++;
                }
            }

        }
    }

