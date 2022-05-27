

import java.util.NoSuchElementException;

public class BacktrackingBST implements Backtrack, ADTSet<BacktrackingBST.Node> {
    private Stack stack;
    private Stack redoStack;
    private BacktrackingBST.Node root = null;

    // Do not change the constructor's signature
    public BacktrackingBST(Stack stack, Stack redoStack) {
        this.stack = stack;
        this.redoStack = redoStack;
    }

    public Node getRoot() {
        if (root == null) {
            throw new NoSuchElementException("empty tree has no root");
        }
        return root;
    }

    /**
     * This method searches for a node with they key 'k' throughout the BST
     * @param k the key of the node we're looking for
     * @return the node we're looking for if found in the BST, null otherwise
     * This method uses a helper method called 'search' in Backtracking.Node
     */
    public Node search(int k) {
        if (root == null) return null; //this exists for the sole purpose of not getting to the exception. (requested to return null if the searched key isn't in the set)
        return getRoot().search(k);
    }

    /**
     * This method inserts a node into the BST
     * @param node the node to be inserted
     *
     *             This method also keeps information regarding the insertion for purposes of future backtracking
     *             This method uses a helper method called 'insert' in Backtracking.Node
     */
    public void insert(Node node) {
        if (root == null) root = node;
        getRoot().insert(node);
        Object[] input = {1,node};
        stack.push(input);
        redoStack.clear();
    }

    /**
     * This method deletes a node from the BST
     * @param node the node to be deleted
     *
     *             This method also keeps information regarding the deletion for purposes of future backtracking
     *             This method uses helper methods called 'deleteHelp' and 'deleteSucc'
     */
    public void delete(Node node) {
        Object[] input = {0,node,node.parent,node.right,node.left};
        stack.push(input);
        if(root == node) {
            if (root.left == null && root.right == null) {
                root = null;
            }
            else {
                if (root.left == null) {
                    root = root.right;
                }
                else if (root.right == null) {
                    root = root.left;
                }
                else {
                    deleteHelp(node);
                }
            }
        }
        else {
            deleteHelp(node);
        }
        redoStack.clear();
    }

    /**
     * This is a helper method to 'delete' and 'deleteHelp'
     * This method deletes {@param node}'s successor in order to replace said node in the BST
     * @param node the node to be deleted
     * @return the successor that was deleted
     */
    private Node deleteSucc(Node node) {
        Node succ = successor(node);
        Object[] input = {42,succ,succ.parent,succ.right,null};
        deleteHelp(succ);
        stack.push(input);
        return succ;
    }

    /**
     * This is a helper method to 'delete' which uses 'deleteSucc' in a certain scenario
     * @param node the node to be deleted
     */
    private void deleteHelp(Node node) {
        if (node.right == null && node.left == null) {  //node is leaf
            if (node.parent.right == node) {
                node.parent.right = null;
            }
            else if (node.parent.left == node) {
                node.parent.left = null;
            }
            node.parent = null;
        }
        else if (node.right == null) {                  //node only has a left child
            if (node.parent.right == node) {
                node.parent.right = node.left;
            }
            else if (node.parent.left == node) {
                node.parent.left = node.left;
            }
            node.left.parent = node.parent;
            node.parent = null;
        }
        else if (node.left == null) {                   //node only has a right child
            if (node.parent.right == node) {
                node.parent.right = node.right;
            }
            else if (node.parent.left == node) {
                node.parent.left = node.right;
            }
            node.right.parent = node.parent;
            node.parent = null;
        }
        else {                                          //node has two children
            Node succ = deleteSucc(node);
            succ.right = node.right;
            if (node.right != null) {
                node.right.parent = succ;
            }
            succ.left = node.left;
            if (node.left != null) {
                node.left.parent = succ;
            }
            if (node.parent != null) {
                if (node.parent.right == node) {
                    node.parent.right = succ;
                } else if (node.parent.left == node) {
                    node.parent.left = succ;
                }
                node.parent = null;
            }
            else {
                root = succ;
            }
        }
    }

    /**
     * This method extracts the minimum node in the BST
     * @return the node with the minimum key
     * This method uses a helper method 'minimum' in Backtracking.Node
     */
    public Node minimum() {
        return getRoot().minimum();
    }

    /**
     * This method extracts the maximum node in the BST
     * @return the node with the maximum key
     * This method uses a helper method 'maximum' in Backtracking.Node
     */
    public Node maximum() {
        return getRoot().maximum();
    }

    /**
     * This method extracts the successor of a certain node
     * @param node the node we want to find the successor of
     * @return the successor of said node
     * This method uses a helper method 'successor' in Backtracking.Node
     */
    public Node successor(Node node) {
        return getRoot().successor(node);
    }

    /**
     * This method extracts the predecessor of a certain node
     * @param node the node we want to find the predecessor of
     * @return the predecessor of said node
     * This method uses a helper method 'predecessor' in Backtracking.Node
     */
    public Node predecessor(Node node) {
        return getRoot().predecessor(node);
    }

    /**
     * This method backtracks both insert and delete methods
     * The field 'stack' is used here in order to backtrack
     *      insertion backtracking object in stack look as such: {1, Node inserted}
     *      deletion backtracking object in stack look as such: {0 (or 42),Node deleted,Node parent,Node right,Node left}
     * These are in order to know what backtracking to perform
     * This method uses a helper method 'normalDeleteBacktrack' in order to eliminate duplication of code
     */
    @Override
    public void backtrack() {
        if (!stack.isEmpty()) {
            Object[] output = (Object[]) stack.pop();
            Node node = (Node) output[1];
            if (output[0] == (Integer) 1) { //an insertion backtracking
                if (root != null) {
                    deleteHelp(node);
                }
                redoStack.push(output);

            }
            else if (output[0] == (Integer) 0) { //a normally deleted node
                normalDeleteBacktrack(output);
                redoStack.push(output);
            }
            else if (output[0] == (Integer) 42) { //the first index will only be 42 if it's a successor of 'case 3' deleted node
                /*  So, said code is divided by a comment - first part is handling the 'case 3' node itself
                    and the second part of the code - after said comment - is handling its successor
                 */
                Object[] twoSons = (Object[]) stack.pop();
                if (twoSons[0] == (Integer) 0 && twoSons[3] != null && twoSons[4] != null) {
                    node = (Node) twoSons[1];
                    Node p = (Node) twoSons[2];
                    Node r = (Node) twoSons[3];
                    Node l = (Node) twoSons[4];
                    node.parent = p;
                    if (p != null) {
                        if (p.left == output[1]) {
                            p.left = node;
                        } else if (p.right == output[1]) {
                            p.right = node;
                        }
                    }
                    else {
                        root = node;
                    }
                    node.right = r;
                    r.parent = node;
                    node.left = l;
                    l.parent = node;
                    //This is the dividing comment, from now and up until line 268 - is about handling the successor
                    node = (Node) output[1];
                    p = (Node) output[2];
                    r = (Node) output[3];
                    l = (Node) output[4];
                    if (r == null && l == null) {       //the node was a leaf, so it should become one again.
                        node.left = null;
                        node.right = null;
                        node.parent = p;
                        if (p.getKey() > node.getKey()) {
                            p.left = node;
                        }
                        else {
                            p.right = node;
                        }
                    }
                    else if (l == null) {               //the node only had a right child -- if it had a left child - it wouldn't be the successor
                        if (p != null) {
                            if (p.left == r) {
                                p.left = node;
                            }
                            else if (p.right == r) {
                                p.right = node;
                            }
                        }
                        node.right = r;
                        node.parent = p;
                        r.parent = node;
                    }
                    //Just to make sure it's understood - this is line 268.
                }
                else {
                    stack.push(twoSons);
                    normalDeleteBacktrack(output);
                }
                redoStack.push(twoSons);
            }
        }
    }

    /**
     * This is a helper method to 'backtrack' in order to eliminate duplication of code
     * @param output an array from the backtracking stack to help us differentiate between
     *               insertion and deletion backtracking and some helpful parameters
     */
    private void normalDeleteBacktrack(Object[] output) {
        Node node = (Node) output[1];
        Node p = (Node) output[2];
        Node r = (Node) output[3];
        Node l = (Node) output[4];
        if (root == null && r == null && l == null && p == null) {
            root = node;
        }
        if (r == null && l == null) {           //the node was a leaf, so it should become one again.
            node.right = null;
            node.left = null;
            node.parent = p;
        }
        else if (r == null) {                 //the node only had a left child
            if (p != null) {
                if (p.left == l) {
                    p.left = node;
                } else if (p.right == l) {
                    p.right = node;
                }
            }
            else {
                root = node;
            }
            node.left = l;
            node.parent = p;
            l.parent = node;
        }
        else if (l == null) {               //the node only had a right child
            if (p != null) {
                if (p.left == r) {
                    p.left = node;
                } else if (p.right == r) {
                    p.right = node;
                }
            }
            node.right = r;
            node.parent = p;
            r.parent = node;
        }
        output[0] = 1;
    }

    /**
     * This method retracks backtracked insertions and deletions performed previously
     */
    @Override
    public void retrack() {
        Object[] output = (Object[]) redoStack.pop();
        stack.push(output);                                             //in order to make sure we can backtrack the retracks
        Node node = (Node) output[1];
        if (output[0] == (Integer) 0 || output[0] == (Integer) 42) {    //retracking deletion = deleting.
            if(root == node) {
                if (root.left == null && root.right == null) {
                    root = null;
                }
                else {
                    if (root.left == null) {
                        root = root.right;
                    }
                    else if (root.right == null) {
                        root = root.left;
                    }
                    else {
                        deleteHelp(node);
                    }
                }
            }
            else {
                deleteHelp(node);
            }
        }
        if (output[0] == (Integer) 1) {     //retracking insertion = inserting.
            Node p = (Node) output[2];
            Node r = (Node) output[3];
            Node l = (Node) output[4];
            if (root == null && r == null && l == null && p == null) {
                root = node;
            }
            else {
                if (p != null) {
                    if (p.getKey() > node.getKey()) {
                        p.left = node;
                    } else {
                        p.right = node;
                    }
                }
                node.parent = p;
                node.left = null;
                node.right = null;
            }
        }

    }

    /**
     * This method uses a helper method in Backtracking.Node to print the BST in pre-order
     */
    public void printPreOrder() {
        System.out.println(getRoot().printPreOrder());
    }

    @Override
    public void print() {
        printPreOrder();
    }
    public static class Node {
        // These fields are public for grading purposes. By coding conventions and best practice they should be private.
        public BacktrackingBST.Node left;
        public BacktrackingBST.Node right;

        private BacktrackingBST.Node parent;
        private int key;
        private Object value;

        public Node(int key, Object value) {
            this.key = key;
            this.value = value;
        }

        public int getKey() {
            return key;
        }

        public Object getValue() {
            return value;
        }

        /**
         * This is a helper method to 'search' in BacktrackingBST
         * @param k they key of the node we're searching for
         * @return the node with said key if found, null otherwise
         */
        private Node search(int k) {
            if (key == k) {
                return this;
            }
            else if (key > k) {
                if (left != null) {
                    return left.search(k);
                }
                return null;
            }
            else {
                if (right != null) {
                    return right.search(k);
                }
                return null;
            }
        }

        /**
         * This is a recursive helper method to 'insert' in BacktrackingBST
         * @param node the node to be inserted as a leaf
         */
        private void insert(Node node) {
            if (key > node.getKey()) {
                if (left != null) {
                    left.insert(node);
                }
                else {
                    left = node;
                    node.parent = this;
                    node.right = null;
                    node.left = null;
                }
            }
            else if (key < node.getKey()) {
                if (right != null) {
                    right.insert(node);
                }
                else {
                    right = node;
                    node.parent = this;
                    node.right = null;
                    node.left = null;
                }
            }
        }

        /**
         * This is a recursive helper method to 'minimum' in BacktrackingBST
         * @return the BST's minimum valued node if found, and null otherwise
         */
        private Node minimum() {
            if (left != null) return left.minimum();
            return this;
        }

        /**
         * This is a recursive helper method to 'maximum' in BacktrackingBST
         * @return the BST's maximum valued node if found, and null otherwise
         */
        private Node maximum() {
            if (right != null) return right.maximum();
            return this;
        }

        /**
         * This is a helper method to 'successor' in BacktrackingBST, and it finds a node's successor in the BST
         * @param node the node of which we want its successor
         * @return said node's successor
         */
        private Node successor(Node node) {
            if (node == maximum()) throw new NoSuchElementException("There isn't a successor to the maximum number in the array by definition");
            if (node.right != null) return node.right.minimum();
            Node curr = node.parent;
            Node currSon = node;
            while (currSon == curr.right) {
                currSon = curr;
                curr = curr.parent;
            }
            return curr;
        }

        /**
         * This is a helper method to 'predecessor' in BacktrackingBST, and it finds a node's predecessor in the BST
         * @param node the node of which we want its predecessor
         * @return said node's predecessor
         */
        private Node predecessor(Node node) {
            if (node == minimum()) throw new NoSuchElementException("There isn't a predecessor to the minimum number in the array by definition");
            if (node.left != null) return node.left.maximum();
            Node curr = node.parent;
            Node currSon = node;
            while (currSon == curr.left) {
                currSon = curr;
                curr = curr.parent;
            }
            return curr;
        }

        /**
         * This is a helper method to 'printPreOrder' in BacktrackingBST
         * @return the accumulated string in order to print it all together
         */
        private String printPreOrder() {
            String acc = getKey() + " ";
            if (left != null) {
                acc = acc + left.printPreOrder() + " ";
            }
            if (right != null) {
                acc = acc + right.printPreOrder() + " ";
            }
            return acc;
        }
    }
}