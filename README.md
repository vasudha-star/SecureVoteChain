# SecureVoteChain

SecureVoteChain is a Java-based online voting integrity system that demonstrates secure
voter authentication, tamper-evident vote storage, and post-election verification using
data structures and cryptographic techniques.

> **Project Type:** Academic / Learning Project

---

## Problem Overview

Online voting systems often face trust issues due to concerns about unauthorized access,
duplicate voting, and vote tampering. SecureVoteChain addresses these concerns by providing
a verifiable and integrity-focused voting workflow.

---

## Key Objectives

- Authenticate only registered voters  
- Prevent duplicate and unauthorized voting  
- Store votes securely and immutably  
- Detect any modification of stored voting data  
- Enable post-election audit verification  

---

## System Highlights

- Balanced-tree based voter authentication  
- Hash-based duplicate vote prevention  
- Cryptographic hashing (SHA-256) for vote security  
- Blockchain-style immutable vote storage  
- Merkle Tree based integrity verification  
- Audit verification of voting records  

---

## Data Structures Used

- **AVL Tree** – Voter authentication  
- **HashMap** – Voter status tracking and vote tallying  
- **Merkle Tree** – Vote integrity verification  
- **Blockchain (hash-linked blocks)** – Immutable vote storage  
- **Max Heap** – Suspicious voting activity detection  

---

## Project Structure

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


---
Output

Valid voters can vote once

Invalid or duplicate votes are rejected

Votes are stored immutably

Audit verification detects tampering


Future Enhancements

Encrypted vote transmission

Distributed or decentralized deployment

Advanced fraud detection mechanisms

##Disclaimer

This project is intended for educational purposes only and is not designed for real-world
election deployment.


## How to Run

```bash
javac *.java
java VotingSystemDemo

