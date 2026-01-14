# SecureVoteChain
🔐 SecureVoteChain

SecureVoteChain is a Java-based secure online voting system that demonstrates how
data structures and cryptographic techniques can be used to ensure voter
authentication, vote integrity, and tamper detection.

This project is intended as an academic and learning implementation focused on
integrity and transparency in online voting.

❓ Problem Statement

Online voting systems often face trust issues due to concerns about:

Unauthorized voters

Duplicate voting

Tampering of stored votes

Lack of post-election verification

SecureVoteChain addresses these issues by providing a tamper-evident voting workflow
that allows verification of stored votes after the election process.


🎯 Project Goals

Authenticate only registered voters

Prevent duplicate voting

Securely store votes

Detect any modification of voting data

Support post-election audit verification


🧠 How It Works (High-Level)

Load voter and candidate data

Authenticate voter using a balanced tree

Prevent duplicate voting using hash-based checks

Hash and store votes securely

Append votes to a blockchain structure

Verify integrity using Merkle Tree and audit checks


🧩 Data Structures Used

AVL Tree – Efficient voter authentication

HashMap – Duplicate vote prevention and vote tallying

Merkle Tree – Vote integrity verification

Blockchain (hash-linked blocks) – Immutable vote storage

Max Heap – Detection of suspicious voting activity


🔐 Security Features

SHA-256 hashing for vote data

Tamper-evident blockchain storage

Merkle Root verification

Sequential audit verification

📁 Project Structure
SecureVoteChain/
├── AVLRegistry.java
├── AuditVerifier.java
├── MerkleTree.java
├── VoteBlock.java
├── VoteBlockchain.java
├── VoterNode.java
├── SuspiciousVoteHeap.java
├── VotingSystemDemo.java
├── ConsoleColor.java
├── election_data.txt
└── README.md


▶️ How to Run

Make sure Java is installed

Compile the source files:

javac *.java

Run the application:

java VotingSystemDemo


📊 Output

Valid voters can vote once

Invalid or duplicate votes are rejected

Votes are stored immutably

Audit verification detects tampering


🚀 Future Scope

Encrypted vote transmission

Decentralized or distributed deployment

Advanced fraud detection techniques

Improved user interface


🎓 Project Type

Academic / Learning Project
Focused on data structures, algorithms, and security concepts.


📌 Note

This project is a conceptual and educational implementation and is not intended for
production or real-world elections.
