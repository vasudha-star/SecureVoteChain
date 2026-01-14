import java.security.MessageDigest;

public class VoteBlock {

    int voteId;
    String voter, candidate, prevHash, currHash;

    VoteBlock(int id, String v, String c, String p) {
        voteId = id;
        voter = v;
        candidate = c;
        prevHash = p;

        // Hash includes previous hash for real blockchain linking
        currHash = hash(voter + candidate + voteId + prevHash);
    }

    // Used only for testing fraud detection
    public void corruptCandidate() {
        this.candidate = "CORRUPTED_DATA";
    }

    // Verifies if current block data was tampered
    public boolean isHashValid() {
        String recalculated = hash(voter + candidate + voteId + prevHash);
        return currHash.equals(recalculated);
    }

    // SHA-256 hashing function
    static String hash(String s) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] b = md.digest(s.getBytes());
            StringBuilder sb = new StringBuilder();
            for (byte x : b)
                sb.append(String.format("%02x", x));
            return sb.toString();
        } catch (Exception e) {
            return "";
        }
    }
}
