public class AVLRegistry {
    VoterNode root;

    int h(VoterNode n){ return n==null?0:n.height; }

    VoterNode rotateRight(VoterNode y){
        VoterNode x=y.left;
        y.left=x.right; x.right=y;
        y.height=Math.max(h(y.left),h(y.right))+1;
        x.height=Math.max(h(x.left),h(x.right))+1;
        return x;
    }

    VoterNode rotateLeft(VoterNode x){
        VoterNode y=x.right;
        x.right=y.left; y.left=x;
        x.height=Math.max(h(x.left),h(x.right))+1;
        y.height=Math.max(h(y.left),h(y.right))+1;
        return y;
    }

    int balance(VoterNode n){ return n==null?0:h(n.left)-h(n.right); }

    VoterNode insert(VoterNode n,String key){
        if(n==null) return new VoterNode(key);
        if(key.compareTo(n.voterHash)<0) n.left=insert(n.left,key);
        else if(key.compareTo(n.voterHash)>0) n.right=insert(n.right,key);
        else return n;

        n.height=1+Math.max(h(n.left),h(n.right));
        int b=balance(n);

        if(b>1 && key.compareTo(n.left.voterHash)<0) return rotateRight(n);
        if(b<-1 && key.compareTo(n.right.voterHash)>0) return rotateLeft(n);
        if(b>1 && key.compareTo(n.left.voterHash)>0){ n.left=rotateLeft(n.left); return rotateRight(n); }
        if(b<-1 && key.compareTo(n.right.voterHash)<0){ n.right=rotateRight(n.right); return rotateLeft(n); }
        return n;
    }

    public void addVoter(String v){ root=insert(root,v); }

    public boolean search(VoterNode n,String k){
        if(n==null) return false;
        if(n.voterHash.equals(k)) return true;
        return k.compareTo(n.voterHash)<0?search(n.left,k):search(n.right,k);
    }

    public void print(){
        System.out.print(ConsoleColor.GREEN+"VOTER REGISTRY (AVL): ");
        inorder(root);
        System.out.println(ConsoleColor.RESET);
    }

    void inorder(VoterNode n){
        if(n!=null){ inorder(n.left); System.out.print(n.voterHash+" "); inorder(n.right); }
    }
}
