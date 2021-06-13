import java.io.*;
import java.lang.*;
import java.util.LinkedList;
import java.util.Queue;

public class Commands {
    private FileWriter fileWriter;
    private Binary_Search_Tree root;
    private Queue<Binary_Search_Tree> queue = new LinkedList<>();


    public Commands(FileWriter file, Binary_Search_Tree root) throws IOException {
        this.fileWriter = file;
        this.root = root;
    }

    /*It reads the input file and decide the correct command and perform the operations*/

    public void reader_command(String path) throws IOException {
        File file = new File(path);
        if(!file.exists()){
            System.out.println("The file you try to read is not exist");
            System.exit(1);
        }
        BufferedReader br = new BufferedReader(new FileReader(file));
        String line = br.readLine();

        while (line != null) {
            /*Splitting each command with delimiters space and comma*/
            String[] tree_values = line.split("[ ,]+");

            if (tree_values[0].equals("CreateBST")) {
                while (root != null) {
                    root = delete(root);
                }
                for (int i = 1; i < tree_values.length; i++) {
                    Binary_Search_Tree node = new Binary_Search_Tree(Integer.parseInt(tree_values[i]));
                    root = insert(node);
                }
                fileWriter.write("BST created with elements:");
                inorder_print(root);
                fileWriter.write("\n");

            } else if (tree_values[0].equals("CreateBSTH")) {
                tree_values = line.split(" ");
                if(Integer.parseInt(tree_values[1]) < 1)
                    fileWriter.write("error\n");
                else{
                    /*First delete the whole tree*/
                    while (root != null) {
                        root = delete(root);
                    }
                    /*And then inserting the new values*/
                    root=Create_Full_Binary_Tree((int) Math.pow(2,Integer.parseInt(tree_values[1])),Integer.parseInt(tree_values[1]));
                    fileWriter.write("A full BST created with elements:");
                    inorder_print(root);
                    fileWriter.write("\n");
                }

            } else if (tree_values[0].equals("DelRoot")) {
                if (root== null)
                    fileWriter.write("error\n");
                else {
                    fileWriter.write("Root Deleted:" + root.getValue());
                    root = delete(root);
                    fileWriter.write("\n");
                }

            } else if (tree_values[0].equals("Preorder")) {
                if (root== null)
                    fileWriter.write("error\n");
                else {
                    fileWriter.write("Preorder:");
                    preorder_print(root);
                    fileWriter.write("\n");
                }

            } else if (tree_values[0].equals("DelRootLc")) {
                if (root == null || root.getLeft_link() == null)
                    fileWriter.write("error\n");
                else {
                    fileWriter.write("Left Child of Root Deleted:" + root.getLeft_link().getValue());
                    root.setLeft_link(delete(root.getLeft_link()));
                    fileWriter.write("\n");
                }

            } else if (tree_values[0].equals("DelRootRc")) {
                if (root == null || root.getRight_link() == null)
                    fileWriter.write("error\n");
                else {
                    fileWriter.write("Right Child of Root Deleted:" + root.getRight_link().getValue());
                    root.setRight_link(delete(root.getRight_link()));
                    fileWriter.write("\n");
                }

            } else if (tree_values[0].equals("FindHeight")) {
                if (root== null)
                    fileWriter.write("error\n");
                else {
                    fileWriter.write("Height:" + find_height(root));
                    fileWriter.write("\n");
                }

            } else if (tree_values[0].equals("FindWidth")) {
                if (root== null)
                    fileWriter.write("error\n");
                else {
                    fileWriter.write("Width:" + find_width(root));
                    fileWriter.write("\n");
                }

            } else if (tree_values[0].equals("LeavesAsc")) {
                if (root== null)
                    fileWriter.write("error\n");
                else {
                    fileWriter.write("LeavesAsc:");
                    LeavesAsc(root);
                    fileWriter.write("\n");
                }
            }
            else /*Invalid command control*/
                fileWriter.write("error\n");
            line = br.readLine();
        }
        br.close();
        fileWriter.close();
    }

    /*It checks the root with the node which is gonna be inserted and find the correct location of that node and then insert
    * rearrange the links and then returns the root*/

    public Binary_Search_Tree insertion_recursively(Binary_Search_Tree root, Binary_Search_Tree current_node) {
        if (root == null) {
            root = current_node;
            return root;
        }
        if (root.getValue() < current_node.getValue())
            root.setRight_link(insertion_recursively(root.getRight_link(), current_node));
        else if (root.getValue() > current_node.getValue())
            root.setLeft_link(insertion_recursively(root.getLeft_link(), current_node));

        return root;

    }

    /*It uses auxiliary function to insert values to binary tree*/

    public Binary_Search_Tree insert(Binary_Search_Tree node) {
        if (root == null) {
            root = new Binary_Search_Tree();
            root = node;
            return root;
        }
        root = insertion_recursively(root, node);
        return root;
    }

    /*It deletes the requested value and Returns the inorder successor of that node with modified links*/
    public Binary_Search_Tree delete(Binary_Search_Tree node) {
        node = deleteRec(node, node.getValue());
        return node;
    }

    /* A recursive function to insert a new key in BST */

    public Binary_Search_Tree deleteRec(Binary_Search_Tree root, int value) {

        /* If the tree is empty it simply returns root*/
        if (root == null) return root;

        /* Otherwise it recursively traverse the tree */

        /*If the value we are gonna delete is less than the root's value it goes from left_link*/
        if (value < root.getValue())
            root.setLeft_link(deleteRec(root.getLeft_link(), value));

        /*If the value we are gonna delete is bigger than the root's value it goes from left_link*/
        else if (value > root.getValue())
            root.setRight_link(deleteRec(root.getRight_link(), value));

            /* If the key is same as the root's key this is the root we are gonna delete
            */
        else {
            /*This statements controls if the root has only one child*/
            if (root.getLeft_link() == null) {
                return root.getRight_link();
            } else if (root.getRight_link() == null) {
                return root.getLeft_link();
            }
            /*If the root has two child we have to find the inorder successor(smallest node in the right sub-tree)*/
            /*Minimum child function is an auxilary function to find inorder successor of the right sub-tree*/
            root.setValue(Minimum_Child(root.getRight_link()));

            /*We are simply deleting the inorder successor*/
            root.setRight_link(deleteRec(root.getRight_link(), root.getValue()));
        }
        return root;
    }

    /*It searches the inorder successor and returns it*/
    int Minimum_Child(Binary_Search_Tree root) {
        int successor_value = root.getValue();
        while (root.getLeft_link() != null) {
            successor_value = root.getLeft_link().getValue();
            root = root.getLeft_link();
        }
        return successor_value;
    }

    /*Printing pre-order traversal of the tree*/

    public void preorder_print(Binary_Search_Tree node) throws IOException {
        if (node == null)
            return;
        fileWriter.write(node.getValue() + " ");
        preorder_print(node.getLeft_link());
        preorder_print(node.getRight_link());
    }

    /*Printing inorder traversal of the tree*/

    public void inorder_print(Binary_Search_Tree node) throws IOException {
        if (node == null)
            return;
        inorder_print(node.getLeft_link());
        fileWriter.write(node.getValue() + " ");
        inorder_print(node.getRight_link());
    }

    /*Finding height of the binary tree*/

    public int find_height(Binary_Search_Tree root) {
        /*Base case it returns -1 we are adding +1 below so a tree with only one node simply returns height of 0*/
        if (root == null)
            return -1;
        else {
            /* We are calculating the each sub-trees height and stores in a variable */
            int left_sub_tree = find_height(root.getLeft_link());
            int right_sub_tree = find_height(root.getRight_link());

            /* We are deciding which one is bigger */
            if (left_sub_tree > right_sub_tree)
                return (left_sub_tree + 1);
            else
                return (right_sub_tree + 1);
        }
    }

    /*This is simply breadth-first-search algorithm in other words level order traversal*/
    public int find_width(Binary_Search_Tree root) {

        /*Add the root to the queue*/
        queue.add(root);

        int width = queue.size();

        int level = 0;/*Root level is 0*/

        int poll_time = 0;/*this is the variable that we can maximum poll element from the queue*/

        while (!queue.isEmpty()) {
            Binary_Search_Tree temporary_node = queue.poll();

            /*Enqueue the left child */
            if (temporary_node.getLeft_link() != null) {
                queue.add(temporary_node.getLeft_link());
                poll_time++;
            }
            else
                poll_time++;

            /*Enqueue the right child */
            if (temporary_node.getRight_link() != null) {
                queue.add(temporary_node.getRight_link());
                poll_time++;
            }
            else
                poll_time++;

            /*In any level of the tree simply we can at most poll 2^level nodes*/
            if (poll_time == Math.pow(2, level+1)) {
                int max_width = queue.size();
                if (max_width > width)
                    width = max_width;
                level++;
                poll_time = 0;

                /*If that level of the tree does not contain the maximum child we assumed that ıt contains that children
                * and increase the poll time*/
                if(queue.size()!=Math.pow(2,level)){
                    poll_time= (int) ((Math.pow(2,level)-queue.size())*2);
                }
            }

        }
        return width;
    }

    /*This is almost a post-order traversal just modified at the end checking
    if the node's both link is null that means it is leaf*/

    public void LeavesAsc(Binary_Search_Tree root) throws IOException {
        if (root == null) {
            return;
        }
        LeavesAsc(root.getLeft_link());
        LeavesAsc(root.getRight_link());
        if(root.getRight_link()==null && root.getLeft_link()==null)
            fileWriter.write(root.getValue()+" ");
    }

    /*Creating a full binary tree with the given parameter.It works recursively and basically considers the depth of the tree
    * The root would be 2^h ,left sub-tree will be rooted at  root - 2^(h-1), right sub-tree will be rooted at root + 2^(h-1)*/

    public Binary_Search_Tree Create_Full_Binary_Tree(int root,int depth){

        /*Root node's depth is zero when depth is zero we found our root */
        if (depth==0){
            return new Binary_Search_Tree(root);
        } else {
            Binary_Search_Tree node = new Binary_Search_Tree(root);
            node.setLeft_link(Create_Full_Binary_Tree((int) (root-Math.pow(2,depth-1)),depth-1));
            node.setRight_link(Create_Full_Binary_Tree((int) (root+Math.pow(2,depth-1)),depth-1));
            return node;
        }
    }
}