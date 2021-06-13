public class Binary_tree {
    private Float value;
    private String Country_name;
    private Binary_tree right_link,left_link;

    public Binary_tree(Float new_country,String country_name) {
        this.Country_name= country_name;
        this.value = new_country;
        right_link=null;
        left_link=null;
    }
    public Binary_tree(Binary_tree node){
        this.value=node.getValue();
        this.Country_name=node.getCountry_name();
        right_link=null;
        left_link=null;
    }

    public Binary_tree() {
    }

    public Float getValue() {
        return value;
    }

    public void setValue(Float value) {
        this.value = value;
    }

    public String getCountry_name() { return Country_name; }

    public Binary_tree getRight_link() {
        return right_link;
    }

    public void setRight_link(Binary_tree right_link) {
        this.right_link = right_link;
    }

    public Binary_tree getLeft_link() {
        return left_link;
    }

    public void setLeft_link(Binary_tree left_link) {
        this.left_link = left_link;
    }

    /*Inserting objects to the binary tree with given root and node
    * with using an auxiliary function below*/

    public Binary_tree insert(Binary_tree root, Binary_tree node) {
        /*If the root is null current node becomes the root*/
        if (node == null) {
            node = new Binary_tree();
            return node;
        }
        root = insertion_recursively(node, root);
        return root;
    }

    /*It decides which object is gonna be right_link and which is gonna be left-link by comparing float values of the objects
    also it allows duplicate keys by adding same keys as a left-child of the root.*/

    public Binary_tree insertion_recursively(Binary_tree current_node, Binary_tree root) {
        if (root == null) {
            root = current_node;
            return root;
        }
        if (root.getValue() > current_node.getValue())
            root.setRight_link(insertion_recursively(current_node,root.getRight_link()));
        else
            root.setLeft_link(insertion_recursively(current_node,root.getLeft_link()));

        return root;
    }
}
