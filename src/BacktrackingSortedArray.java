

public class BacktrackingSortedArray implements Array<Integer>, Backtrack {
    private Stack stack;
    public int[] arr; // This field is public for grading purposes. By coding conventions and best practice it should be private.
    private int size;

    // Do not change the constructor's signature
    public BacktrackingSortedArray(Stack stack, int size) {
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
     * This method searches throughout the array for a desired value's index using binary search
     * @param k the desired value
     * @return its index
     */
    @Override
    public Integer search(int k) {
        int start = 0, end = size - 1, middle = (start + end)/2;
        if (k < arr[0] || k > arr[end] || size == 0) return -1;
        while (start <= end) {
            if (arr[middle] == k) {
                return middle;
            }
            else if (arr[middle] < k) {
                start = middle + 1;
                middle = (start + end)/2;
            }
            else {
                end = middle-1;
                middle = (start + end)/2;
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
            int place = 0;
            if (size == 0) {
                arr[0] = x;
            }
            else {
                if (x < arr[0]) {
                    for (int i = size; i > 0; i--) {
                        arr[i] = arr[i-1];
                    }
                    arr[0] = x;
                }
                else if (x > arr[size - 1]) {
                    arr[size] = x;
                    place = size;
                }
                else {
                    for (int i = 0; i < size - 1 && arr[i] < x; i++) {
                        if (arr[i + 1] > x) {
                            for (int j = size; j > i + 1; j--) {
                                arr[j] = arr[j - 1];
                            }
                            arr[i + 1] = x;
                            place = i + 1;
                        }
                    }
                }
            }
            int[] input = {1, place, x};
            stack.push(input);
            size++;
        }
        else {
            throw new IndexOutOfBoundsException("The array is currently full, or is size 0");
        }
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
            int[] input = {0, index, arr[index]};
            for (int i = index; i < size - 1; i++) {
                arr[i] = arr[i+1];
            }
            size--;
            stack.push(input);
        }
        else {
            throw new IndexOutOfBoundsException("The array is empty or the cell you tried to reach is, can't delete something from empty");
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
        return 0;
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
        return size - 1;
    }

    /**
     * This method looks for an integer's successor in an array
     * @param index is the location of said integer
     * @return the index of said integer's successor
     */
    @Override
    public Integer successor(Integer index) {
        if (index >= size - 1) {
            throw new IndexOutOfBoundsException("There isn't a successor to the maximum number in the array by definition");
        }
        return index + 1;
    }

    /**
     * This method looks for an integer's predecessor in an array
     * @param index is the location of said integer
     * @return the index of said integer's predecessor
     */
    @Override
    public Integer predecessor(Integer index) {
        if (index == 0) {
            throw new IndexOutOfBoundsException("There isn't a predecessor to the minimum number in the array by definition");
        }
        return index - 1;
    }

    /**
     * This method backtracks both insert and delete methods
     * The field 'stack' is used here in order to backtrack
     *      insertion backtracking object in stack look as such: {1, int size, int x} - in order to maintain a standard with other files
     *      deletion backtracking object in stack look as such: {0, int index, int deleted}
     * These are in order to know what backtracking to perform
     */
    @Override
    public void backtrack() {
        if (!stack.isEmpty()) {
            int[] output = (int[]) stack.pop();
            if (output[0] == 0) {
                for (int i = size - 1; i > output[1]; i--) {
                    arr[i] = arr[i + 1];
                }
                arr[output[1]] = output[2];
                size++;
            } else if (output[0] == 1) {
                for (int i = output[1]; i < size - 1; i++) {
                    arr[i] = arr[i + 1];
                }
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
