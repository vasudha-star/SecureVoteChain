import java.security.MessageDigest;
import java.util.*;

public class VotingSystemDemo {

    private LinkedHashMap<String, HashMap<String, String>> voteMatrix = new LinkedHashMap<>();
    private List<String> candidates = new ArrayList<>();
    private String lastMerkleRoot = "";

    private HashMap<String, Boolean> voterStatus = new HashMap<>();
    private AVLRegistry registry = new AVLRegistry();
    private VoterNode registryRoot = null;
    private HashMap<String, Integer> tally = new HashMap<>();

    static VoteBlockchain blockchain = new VoteBlockchain();
    static MerkleTree merkle = new MerkleTree();
    static SuspiciousVoteHeap suspiciousHeap = new SuspiciousVoteHeap();
    static AuditVerifier audit = new AuditVerifier();




    /* ================= HASH FUNCTION ================= */
    private String generateHash(String data) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] bytes = md.digest(data.getBytes());
            StringBuilder sb = new StringBuilder();
            for (byte b : bytes)
                sb.append(String.format("%02x", b));
            return sb.toString();
        } catch (Exception e) { return ""; }
    }

    /* ================= LOAD FILE ================= */
    private void loadElectionData(String filename) {
        try {
            Scanner file = new Scanner(new java.io.File(filename));
            boolean voter = false, candidate = false;

            while (file.hasNextLine()) {
                String line = file.nextLine().trim();

                if (line.equals("#VOTERS")) { voter = true; candidate = false; continue; }
                if (line.equals("#CANDIDATES")) { voter = false; candidate = true; continue; }
                if (line.isEmpty()) continue;

                if (voter)
                    registryRoot = registry.insert(registryRoot, generateHash(line));

                if (candidate) {
                    candidates.add(line);
                    tally.put(line, 0);
                }
            }
            file.close();
            System.out.println(ConsoleColor.LIME +
                    "                  Election Data Loaded Successfully" + ConsoleColor.RESET);
        } catch (Exception e) {
            System.out.println(ConsoleColor.BRIGHT_RED +
                    "[ERROR] Data file missing!" + ConsoleColor.RESET);
        }
    }

    /* ================= VOTER VALIDATION ================= */
    private boolean validateVoter(String voterID) {

        String hash = generateHash(voterID);

        if (!registry.search(registryRoot, hash)) {
            System.out.println(ConsoleColor.BRIGHT_RED +
                    "[REJECTED] Voter Not Registered" + ConsoleColor.RESET);
            return false;
        }

        if (voterStatus.containsKey(hash)) {
            System.out.println(ConsoleColor.MAROON +
                    "[REJECTED] You have already voted - Multiple voting is not allowed" +
                    ConsoleColor.RESET);
            return false;
        }
        return true;
    }

    /* ================= SHOW CANDIDATES ================= */
    private void showCandidates() {
        System.out.println(ConsoleColor.VIOLET + "\nSelect Candidate:" + ConsoleColor.RESET);
        for (int i = 0; i < candidates.size(); i++)
            System.out.println(ConsoleColor.AQUA + (i + 1) + ". Candidate " +
                    candidates.get(i) + ConsoleColor.RESET);
    }

    /* ================= CAST VOTE ================= */
    private static final String TICK = "\u2713";

    public void castVote(String voterID, String candidate) {

        String voterHash = generateHash(voterID);
        voterStatus.put(voterHash, true);

        voteMatrix.putIfAbsent(voterID, new HashMap<>());
        for (String c : candidates)
            voteMatrix.get(voterID).put(c, "-");

        voteMatrix.get(voterID).put(candidate, TICK);
        tally.put(candidate, tally.get(candidate) + 1);

        blockchain.add(voterHash, candidate);
        audit.add(voterHash, candidate);


        String voteHash = generateHash(voterHash + candidate);
        merkle.addLeaf(voteHash);
        lastMerkleRoot = merkle.getRoot();

        System.out.println(ConsoleColor.BRIGHT_GREEN +
                "\n[ACCEPTED] Vote Recorded Successfully" + ConsoleColor.RESET);

        blockchain.printMinimalLedger();

        System.out.println(ConsoleColor.VIOLET +
        "\n--- MERKLE ROOT ---");
        System.out.println(ConsoleColor.VIOLET +
        "Merkle Root Stored: " + lastMerkleRoot + ConsoleColor.RESET);


        printVoteTable();

    }

    /* ================= DASHBOARD ================= */
    private void printVoteTable() {

    System.out.println(ConsoleColor.BRIGHT_BLUE +
            "\n==================== VOTING DASHBOARD ====================" +
            ConsoleColor.RESET);

    System.out.printf(ConsoleColor.GOLD + "%-12s", "VOTER ID");
    for (String c : candidates)
        System.out.printf("%-8s", c);
    System.out.println(ConsoleColor.RESET);

    for (String voter : voteMatrix.keySet()) {

        System.out.printf(ConsoleColor.AQUA + "%-12s", voter);

        for (String c : candidates) {
            String val = voteMatrix.get(voter).get(c);
            if (val.equals(TICK))
                System.out.printf(ConsoleColor.BRIGHT_GREEN + "%-8s", TICK);
            else
                System.out.printf(ConsoleColor.DARK_RED + "%-8s", "-");
        }
        System.out.println(ConsoleColor.RESET);
    }
    }


    /* ================= VERIFY INTEGRITY ================= */
    public void verifyIntegrity() {
        

        boolean chainValid = blockchain.verify();
        boolean merkleValid = lastMerkleRoot.equals(merkle.getRoot());


        int severity;

        if (!chainValid && !merkleValid) {
            severity = 100;
            System.out.println(ConsoleColor.RED +
                "[CRITICAL] Blockchain & Merkle ledger compromised!" +
                ConsoleColor.RESET);
        }
        else if (chainValid && !merkleValid) {
            severity = 70;
            System.out.println(ConsoleColor.RED +
                "[WARNING] Merkle ledger manipulated!" +
                ConsoleColor.RESET);
        }
        else if (!chainValid && merkleValid) {
            severity = 60;
            System.out.println(ConsoleColor.RED +
                "[ALERT] Blockchain links broken!" +
                ConsoleColor.RESET);
        }
        else {
            System.out.println(ConsoleColor.DARK_GREEN +
                "[SECURE] System Normal - No Irregularity Detected" +
                ConsoleColor.RESET);
            return;
        }

        suspiciousHeap.insert(severity);
        suspiciousHeap.print();
    }

    // Rebuild Merkel

    private void rebuildMerkleFromBlockchain() {
    merkle = new MerkleTree();
    for (VoteBlock b : blockchain.getBlocks()) {
        String voteHash = generateHash(b.voter + b.candidate);
        merkle.addLeaf(voteHash);
    }
}






        /* ================= DISPLAY TALLY ================= */
        private void displayTally() {
            System.out.println(ConsoleColor.PURPLE +
                    "\n--- TOTAL VOTE COUNT ---" + ConsoleColor.RESET);
            for (String c : tally.keySet())
                System.out.println(ConsoleColor.YELLOW +
                        "Candidate " + c + " : " + tally.get(c) + ConsoleColor.RESET);
        }

        /* ================= BANNER ================= */
        private void printBanner() {
            System.out.println(ConsoleColor.DARK_PINK +
                    "\n====================================================================");
            System.out.println("      SecureVoteChain  |  B.Tech DSA Project - Group 15");
            System.out.println("====================================================================" +
                    ConsoleColor.RESET);
        }
        // -------- INTERNAL ANOMALY INJECTION (TESTING ONLY) --------
        private void injectAnomaly(int type) {

            switch(type) {

                // CASE-1: Block Tampering → blockchain , merkle 
                case 1:
                    blockchain.corruptFirstBlock();
                    System.out.println(ConsoleColor.RED+
                        "[TEST MODE] Block data tampered." + ConsoleColor.RESET);
                    break;

                // CASE-2: Ledger Manipulation → blockchain , merkle 
                case 2:
                    merkle.addLeaf("FAKE_VOTE_HASH");
                    System.out.println(ConsoleColor.RED+
                        "[TEST MODE] Merkle ledger manipulated." + ConsoleColor.RESET);
                    break;

                // CASE-3: Chain Reconstruction → blockchain , merkle 
                case 3:
                    blockchain.corruptFirstBlock();
                    System.out.println(ConsoleColor.RED +
                        "[TEST MODE] Blockchain links broken." + ConsoleColor.RESET);
                    break;
            }
        }

        /* ================= DECLARE WINNER ================= */
    private void declareWinner() {

        System.out.println(ConsoleColor.GOLD +
            "\n================== ELECTION RESULT ==================" +
            ConsoleColor.RESET);

        int maxVotes = -1;
        for (int v : tally.values())
            maxVotes = Math.max(maxVotes, v);

        List<String> winners = new ArrayList<>();
        for (String c : tally.keySet()) {
            if (tally.get(c) == maxVotes)
                winners.add(c);
        }

        if (maxVotes == 0) {
            System.out.println(ConsoleColor.BRIGHT_RED +
                    "No votes were cast." + ConsoleColor.RESET);
        }
        else if (winners.size() == 1) {
            System.out.println(ConsoleColor.BRIGHT_GREEN +
                    "WINNER : Candidate " + winners.get(0) +
                    " with " + maxVotes + " votes" +
                    ConsoleColor.RESET);
        }
        else {
            System.out.println(ConsoleColor.BRIGHT_YELLOW +
                    "TIE BETWEEN : " + String.join(" , ", winners) +
                    " with " + maxVotes + " votes each" +
                    ConsoleColor.RESET);
        }

        System.out.println(ConsoleColor.GOLD +
            "====================================================" +
            ConsoleColor.RESET);
    }


    /* ================= MAIN ================= */
    
    public static void main(String[] args) {

        VotingSystemDemo system = new VotingSystemDemo();
        Scanner sc = new Scanner(System.in);

        system.printBanner();
        system.loadElectionData("election_data.txt");

        while (true) {
            System.out.println(ConsoleColor.ORANGE +
                "\n====== SECURE VOTECHAIN MENU ======" +
                "\n1. Cast Vote" +
                "\n2. View Blockchain" +
                "\n3. Verify Integrity" +
                "\n4. Inject Anomaly " +
                "\n5. Exit" + ConsoleColor.RESET);


            System.out.print("Enter your choice: ");
            int ch = sc.nextInt();

            switch (ch) {
                case 1:
                    System.out.print("Enter Voter ID: ");
                    String voter = sc.next().trim();
                    if (!system.validateVoter(voter)) break;

                    system.showCandidates();
                    System.out.print("Enter Candidate Number: ");
                    int choice = sc.nextInt();

                    if (choice < 1 || choice > system.candidates.size()) {
                        System.out.println(ConsoleColor.BRIGHT_RED +
                                "[INVALID] Candidate not in list" + ConsoleColor.RESET);
                        break;
                    }
                    system.castVote(voter, system.candidates.get(choice - 1));
                    break;

                case 2:
                    blockchain.print();
                    break;

                case 3:
                    system.verifyIntegrity();
                    break;

                case 4:   // Inject Anomaly (Demo Mode)
                    System.out.println("SELECT: ");
                    System.out.println("1. Tamper Block");
                    System.out.println("2. Manipulate Merkle Ledger");
                    System.out.println("3. Break Blockchain Links");
                    System.out.println();
                    System.out.print("Inject Anomaly :");
                    System.out.println();

                    int t = sc.nextInt();
                    system.injectAnomaly(t);
                    break;

                case 5:   // Exit
                    system.displayTally();
                    system.printVoteTable();
                    system.declareWinner();
                    // audit.print();

                    System.out.println(ConsoleColor.BRIGHT_WHITE +
                        "\nPress ENTER to close the system..." + ConsoleColor.RESET);

                    sc.nextLine();  // clear buffer
                    sc.nextLine();  // wait for ENTER
                    System.exit(0);
                    

            }
        }
    }
}
