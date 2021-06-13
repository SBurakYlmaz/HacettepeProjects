public class Binary_Search_Tree {
    private int value;
    private Binary_Search_Tree right_link,left_link;

    public Binary_Search_Tree(int value) {
        this.value = value;
        right_link=null;
        left_link=null;
    }

    public Binary_Search_Tree() {
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public Binary_Search_Tree getRight_link() {
        return right_link;
    }

    public void setRight_link(Binary_Search_Tree right_link) {
        this.right_link = right_link;
    }

    public Binary_Search_Tree getLeft_link() {
        return left_link;
    }

    public void setLeft_link(Binary_Search_Tree left_link) {
        this.left_link = left_link;
    }
}
