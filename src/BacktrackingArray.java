import java.util.ArrayList;

public class BacktrackingArray implements Array<Integer>, Backtrack {
    private Stack stack;
    private int[] arr;
    private int size;

    // Do not change the constructor's signature
    public BacktrackingArray(Stack stack, int size) {
        this.stack = stack;
        arr = new int[size];
        this.size = 0;
    }

    /**
     * a simple index getter
     * @param index in which our desired value is found
     * @return the value in said index
     */
    @Override
    public Integer get(int index){
        if (index < 0 || index > size) throw new IllegalArgumentException();
        return arr[index];
    }

    /**
     * This method searches throughout the entire array for a desired value's index
     * @param k the desired value
     * @return its index
     */
    @Override
    public Integer search(int k) {
        for (int i = 0; i < size; i++) {
            if (arr[i] == k) {
                return i;
            }
        }
        return -1;
    }

    /**
     * This method inserts an integer into the array
     * @param x the integer to insert
     *
     *          This method also keeps information regarding the insertion for purposes of future backtracking
     */
    @Override
    public void insert(Integer x) {
        if (size < arr.length) {
            int[] input = {1, size, x};
            stack.push(input);
            arr[size] = x;
            size++;
        }
        else {throw new IndexOutOfBoundsException("The array is currently full, or is size 0");}
    }

    /**
     * This method deletes an integer from the array
     * @param index in which the integer is located at
     *
     *              This method also keeps information regarding the deletion for purposes of future backtracking
     */
    @Override
    public void delete(Integer index) {
        if (size > 0 && index < size) {
            int[] input = {0, index, arr[index], size};
            arr[index] = arr[size];
            arr[size] = 0;
            size--;
            stack.push(input);
        }
        else{
            throw new IndexOutOfBoundsException("The array is empty, can't delete empty from empty");
        }
    }

    /**
     * This method extracts the minimum value's index in an array
     * @return the minimum value's index
     */
    @Override
    public Integer minimum() {
        if (size == 0) {
            throw new IndexOutOfBoundsException("There's not a minimum number in the array, as it is empty.");
        }
        int min = arr[0];
        int minInd = 0;
        for (int i = 1; i < size; i++) {
            if (arr[i] < min) {
                minInd = i;
            }
        }
        return minInd;
    }

    /**
     * This method extracts the maximum value's index in an array
     * @return the maximum value's index
     */
    @Override
    public Integer maximum() {
        if (size == 0) {
            throw new IndexOutOfBoundsException("There's not a maximum number in the array, as it is empty.");
        }
        int max = arr[0];
        int maxInd = 0;
        for (int i = 1; i < size; i++) {
            if (arr[i] > max) {
                max = arr[i];
                maxInd = i;
            }
        }
        return maxInd;
    }

    /**
     * This method looks for an integer's successor in an array
     * @param index is the location of said integer
     * @return the index of said integer's successor
     */
    @Override
    public Integer successor(Integer index) {
        if (index < 0 || index > size) {
            throw new IndexOutOfBoundsException("You've gone above and beyond.");
        }
        if (arr[index] == arr[maximum()]) {
            throw new IllegalArgumentException("There isn't a successor to the maximum number in the array by definition");
        }
        int curr = arr[index];
        int succ = Integer.MAX_VALUE;
        int succInd = -1;
        for (int i = 0; i < size; i++) {
            int iValue = arr[i];
            if (iValue > curr && iValue < succ) {
                succ = iValue;
                succInd = i;
            }
        }
        return succInd;
    }

    /**
     * This method looks for an integer's predecessor in an array
     * @param index is the location of said integer
     * @return the index of said integer's predecessor
     */
    @Override
    public Integer predecessor(Integer index) {
        if (index < 0 || index > size) {
            throw new IndexOutOfBoundsException("You've gone above and beyond.");
        }
        if (arr[index] == arr[minimum()]) {
            throw new IndexOutOfBoundsException("There isn't a predecessor to the minimum number in the array by definition");
        }
        int curr = arr[index];
        int pred = Integer.MIN_VALUE;
        int predInd = -1;
        for (int i = 0; i < size; i++) {
            int iValue = arr[i];
            if (iValue < curr && iValue > pred) {
                pred = iValue;
                predInd = i;
            }
        }
        return predInd;
    }

    /**
     * This method backtracks both insert and delete methods
     * The field 'stack' is used here in order to backtrack
     *      insertion backtracking object in stack look as such: {1, int size, int x} - in order to maintain a standard with other files
     *      deletion backtracking object in stack look as such: {0, int index, int deleted, int size}
     * These are in order to know what backtracking to perform
     */
    @Override
    public void backtrack() {
        if (!stack.isEmpty()) {
            int[] output = (int[]) stack.pop();
            if (output[0] == 0) {
                arr[output[3]] = arr[output[1]];
                arr[output[1]] = output[2];
                size++;
            }
            else if (output[0] == 1) {
                size--;
            } else
                throw new IllegalArgumentException("You shouldn't have reached this point, you've done something illegal");
        }
    }

    @Override
    public void retrack() {
        /////////////////////////////////////
        // Do not implement anything here! //
        /////////////////////////////////////
    }

    /**
     * This method prints the entire array, using spaces to split values in different indices
     */
    @Override
    public void print() {
        for (int i = 0; i < size - 1; i++) {
            System.out.print(arr[i]+" ");
        }
        System.out.print(arr[size - 1]); //this is in order to make sure there are no spaces after all the printing algorithm
    }

}