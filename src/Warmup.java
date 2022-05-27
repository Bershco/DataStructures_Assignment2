
public class Warmup {
    /**
     * This method searches an array using linear search whilst backtracking forward and backwards throughout it
     * @param arr the array to be searched
     * @param x the integer we're looking for inside the array
     * @param forward how many steps forwards we will take before starting to go backwards
     * @param back how many steps backwards we will take before starting to go forwards again
     * @param myStack a helper stack in order to make sure we remember the array correctly while going backwards
     * @return the index of {@param x} if found, and -1 otherwise
     */
    public static int backtrackingSearch(int[] arr, int x, int forward, int back, Stack myStack) {
        int counter = 0, ind = 0;
        while (ind < arr.length) {
            if (arr[ind] == x) {
                return ind;
            }
            else if (counter <= forward) {
                myStack.push(arr[ind]);
                counter++;
                ind++;
            }
            else {
                counter = back;
                while (counter > 0) {
                    ind--;
                    if (!myStack.pop().equals(arr[ind])) {
                        throw new IllegalArgumentException("The array has been changed"); //might be a redundant exception throw
                    }
                    counter--;
                }
            }
        }
        return -1;
    }

    /**
     * This method searches an array using binary search whilst backtracking forward and backwards throughout it
     * @param arr the array to be searched
     * @param x the integer we're looking for inside the array
     * @param myStack a helper stack in order to make sure we remember the array correctly while going backwards
     * @return the index of {@param x} if found, and -1 otherwise
     */
    public static int consistentBinSearch(int[] arr, int x, Stack myStack) {
        int start = 0, end = arr.length-1, middle = (end + start)/2;
        while (start <= end) {
            myStack.push(start);
            myStack.push(end);
            if (arr[middle] == x) {
                return middle;
            }
            else if (arr[middle] < x) {
                start = middle+1;
                middle = (end + start)/2;
            }
            else {
                end = middle-1;
                middle = (end + start)/2;
            }
            int inconsistencies = Consistency.isConsistent(arr);
            while(inconsistencies > 0) {
                end = (Integer)myStack.pop();
                start = (Integer)myStack.pop();
                middle = (end + start)/2;
                inconsistencies--;
            }
        }
        return -1;
    }

}
