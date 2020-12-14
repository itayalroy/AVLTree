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
        int[] arr = {4, 14, 1, 2, 8, 19, 13, 3, 16, 5, 17, 6, 12, 11, 20, 18, 10, 15, 9, 7};
        int res = 0;
        int resdel = 0;
        for (int i = 0; i < arr.length; i++) {
            res += tree.insert(arr[i], "0");
        }
        for (int i = 0; i < arr.length; i++) {
            resdel += tree.delete(arr[i]);
        }
        System.out.println(res);
        System.out.println(resdel);
    }

}
