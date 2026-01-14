import java.security.MessageDigest;
import java.util.ArrayList;

public class MerkleTree {

    private ArrayList<String> tree = new ArrayList<>();
    private String root = "";

    private String hash(String data) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] bytes = md.digest(data.getBytes());
            StringBuilder sb = new StringBuilder();
            for (byte b : bytes) sb.append(String.format("%02x", b));
            return sb.toString();
        } catch (Exception e) { return ""; }
    }

    public void addLeaf(String data) {
        tree.add(hash(data));
        buildTree();
    }

    private void buildTree() {

        ArrayList<String> temp = new ArrayList<>(tree);

        while (temp.size() > 1) {
            ArrayList<String> next = new ArrayList<>();

            for (int i = 0; i < temp.size(); i += 2) {
                if (i + 1 < temp.size())
                    next.add(hash(temp.get(i) + temp.get(i + 1)));
                else
                    next.add(hash(temp.get(i) + temp.get(i)));
            }
            temp = next;
        }

        root = temp.get(0);
    }

    public String getRoot() {
        return root;
    }

    public void print() {
        System.out.println("MERKLE ROOT: " + root);
    }
}
