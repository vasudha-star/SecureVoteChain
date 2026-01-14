public class SuspiciousVoteHeap {
    int[] heap=new int[100];
    int size=0;

    void insert(int score){
        heap[size]=score;
        int i=size++;
        while(i>0 && heap[(i-1)/2]<heap[i]){
            int t=heap[i]; heap[i]=heap[(i-1)/2]; heap[(i-1)/2]=t;
            i=(i-1)/2;
        }
    }

    void print(){
        System.out.print(ConsoleColor.RED+"SUSPICIOUS SCORES (MAX HEAP): ");
        for(int i=0;i<size;i++) System.out.print(heap[i]+" ");
        System.out.println(ConsoleColor.RESET);
    }
}
