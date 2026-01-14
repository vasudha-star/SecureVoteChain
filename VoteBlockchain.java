import java.util.*;

public class VoteBlockchain {

    private ArrayList<VoteBlock> blocks = new ArrayList<>();

    // Add new vote block
    public void add(String voter, String candidate) {

        String prevHash = blocks.isEmpty() ? "0" :
                blocks.get(blocks.size() - 1).currHash;

        blocks.add(new VoteBlock(blocks.size() + 1, voter, candidate, prevHash));
    }

    // Print full blockchain ledger
    public void print() {
        System.out.println("\n=========== BLOCKCHAIN LEDGER ===========");
        for (VoteBlock b : blocks) {
            System.out.println("ID: " + b.voteId +
                    " | Voter: " + b.voter +
                    " | Candidate: " + b.candidate);
            System.out.println("PrevHash: " + b.prevHash);
            System.out.println("CurrHash: " + b.currHash);
            System.out.println("----------------------------------------");
        }
    }
    public void printMinimalLedger() {
        System.out.println(ConsoleColor.TEAL + "\n--- BLOCKCHAIN LEDGER ---" + ConsoleColor.RESET);

        int count = 1;
        for (VoteBlock b : blocks) {

            String suffix;
            if (count % 10 == 1 && count != 11) suffix = "st";
            else if (count % 10 == 2 && count != 12) suffix = "nd";
            else if (count % 10 == 3 && count != 13) suffix = "rd";
            else suffix = "th";

            System.out.println(ConsoleColor.TEAL +
                    count + suffix + " CurrHash: " + b.currHash +
                    ConsoleColor.RESET);
            count++;
        }
    }
    public ArrayList<VoteBlock> getBlocks() {
        return blocks;
    }



    // ============ TESTING ANOMALY ============
    public void corruptFirstBlock() {
        if (!blocks.isEmpty())
            blocks.get(0).corruptCandidate();
    }

    public void corruptSecondBlockHash() {
        if (blocks.size() > 1)
            blocks.get(1).prevHash = "FAKE_HASH";
    }

    // ============ REAL VERIFICATION ============
    public boolean verify() {

        for (int i = 0; i < blocks.size(); i++) {

            VoteBlock curr = blocks.get(i);

            // 1. Detect data tampering
            if (!curr.isHashValid())
                return false;

            // 2. Detect broken hash chain
            if (i > 0) {
                VoteBlock prev = blocks.get(i - 1);
                if (!curr.prevHash.equals(prev.currHash))
                    return false;
            }
        }
        return true;
    }
}
