import java.util.Arrays;
import java.util.Collections;
import java.util.Random;

public class Tests {
    public static long insertionSort(Integer[] arr) {
        long sum = 0;
        for (int i = 1; i < arr.length; i++) {
            int temp = arr[i];
            for (int j = i - 1; j >= 0; j--) {
                if (temp > arr[j]) {
                    break;
                } else {
                    sum++;
                    arr[j + 1] = arr[j];
                    arr[j] = temp;
                }
            }
        }
        return sum;
    }

    public static void main(String[] args) {
        Random rnd = new Random();
        for (int i = 1; i <= 10; i++) {
            System.out.print("Iteration for i = ");
            System.out.println(i);
            AVLTree tree = new AVLTree();
            int randomKey = 0;
            int num = 10000*i;
            for (int j = 0; j < num; j++) {
                int key = Math.abs(rnd.nextInt());
                tree.insert(key, "a");
                if(j==3232){
                    randomKey = key;
                }
            }
            AVLTree.IAVLNode pred = tree.predecessor(tree.getRoot());
            System.out.println(Arrays.toString(tree.testSplit(randomKey)));
            System.out.println(Arrays.toString(tree.testSplit(pred.getKey())));
        }
    }
}
