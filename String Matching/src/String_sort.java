import java.io.FileWriter;
import java.io.IOException;
import java.util.Stack;

public class String_sort {
    private String country_name;
    private String_sort right_link,left_link;

    public String_sort(String country_name) {
        this.country_name = country_name;
        this.right_link = null;
        this.left_link = null;
    }

    public String_sort() {
    }

    public String_sort getRight_link() {
        return right_link;
    }

    public void setRight_link(String_sort right_link) {
        this.right_link = right_link;
    }

    public String_sort getLeft_link() {
        return left_link;
    }

    public void setLeft_link(String_sort left_link) {
        this.left_link = left_link;
    }

    public String getCountry_name() {
        return country_name;
    }

    /*Inserting new object to tree with using an auxiliary recursive function*/
    public String_sort insert(String_sort root, String_sort node) {
        /*If the root is null current node becomes the root*/
        if(root.getCountry_name()==null){
            root=node;
            return root;
        }
        root = insertion_recursively(node, root);
        return root;
    }
     /*It decides which object is gonna be right_link and which is gonna be left-link by comparing the strings
     * without using comparator class,compare_to function or any library.*/

    public String_sort insertion_recursively(String_sort current_node, String_sort root) {
        if (root == null) {
            root = current_node;
            return root;
        }
        if (String_compare(current_node.getCountry_name().toCharArray(),root.getCountry_name().toCharArray())>0)
            root.setRight_link(insertion_recursively(current_node,root.getRight_link()));
        else if (String_compare(current_node.getCountry_name().toCharArray(),root.getCountry_name().toCharArray())<0)
            root.setLeft_link(insertion_recursively(current_node,root.getLeft_link()));
        return root;
    }

    /*Comparing strings as a char array and it returns 1 if the string1 is alphabetically bigger
    * and it returns -1 if the string1 is alphabetically smaller,and finally  it returns 0 if the string1 is equal string2*/
    public int String_compare(char[] string1,char[] string2){
        int equal=0;
        while (equal<string1.length && equal<string2.length){
            if(string1[equal]>string2[equal]){
                return 1;
            }
            else if(string1[equal]<string2[equal]){
                return -1;
            }
            equal++;
        }
        return 0;
    }

    /*It traverse the tree with the inorder type and write the values to the output-file.When it traverse
    * inorder it basically writes in alphabetic order*/
    public void Tree_Sort_Write_File(String_sort root, FileWriter fw) throws IOException {
        if (root == null)
            return;


        Stack<String_sort> stack_of_countries = new Stack<>();
        String_sort current_root = root;

        // traverse the tree
        while (current_root != null || stack_of_countries.size() > 0)
        {

            /* Reach the left most Node of the
            current_root Node */
            while (current_root !=  null)
            {
                /* place pointer to a tree node on
                   the stack before traversing
                  the node'stack_of_countries left subtree */
                stack_of_countries.push(current_root);
                current_root = current_root.getLeft_link();
            }

            /* Current must be NULL at this point */
            current_root = stack_of_countries.pop();

            fw.write(String.format("%s",current_root.getCountry_name()));

            /* we have visited the node and its
               left subtree.  Now, it'stack_of_countries right
               subtree'stack_of_countries turn */
            current_root = current_root.getRight_link();
            if(stack_of_countries.size()!=0 || current_root!=null)
                fw.write(", ");

            }
        }
    }

