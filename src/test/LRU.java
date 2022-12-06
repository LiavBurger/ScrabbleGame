package test;


import java.util.HashMap;
import java.util.Map;

public class LRU implements CacheReplacementPolicy {
    Map<String, Node> cacheMap = new HashMap<>();
    Node leastRecentlyUsed = new Node("leastRecentlyUsed"); // left side of the list
    Node mostRecentlyUsed = new Node("mostRecentlyUsed");  // right side of the list
    // THE LINKED LIST WILL LOOK LIKE:
    // [LRU] <--> [a] <--> [b] <--> [c] <--> [MRU]

    public LRU() {
        this.leastRecentlyUsed.next = mostRecentlyUsed;
        this.mostRecentlyUsed.prev = leastRecentlyUsed;
    }

    @Override
    public void add(String word) {
        // if the word exists in the cache, we update it as the most recently used.
        if (cacheMap.containsKey(word)) {
            Node currentWord = cacheMap.get(word);
            moveWordToRecentlyUsed(currentWord);
            return;
        }

        // if the word doesn't exist, we create the word node and map it in the cache map.
        Node wordNode = new Node(word);
        cacheMap.put(word, wordNode);
        addNodeToList(wordNode);
    }

    private void moveWordToRecentlyUsed(Node word) {
        word.prev.next = word.next;
        word.next.prev = word.prev;
        word.prev = mostRecentlyUsed.prev;
        mostRecentlyUsed.prev.next = word;
        word.next = mostRecentlyUsed;
        mostRecentlyUsed.prev = word;
    }

    /**
     * This function adds a word to doubly linked list on the side of "most recently used" by changing the pointers.
     */
    private void addNodeToList(Node newWordNode) {
        newWordNode.next = mostRecentlyUsed;
        newWordNode.prev = mostRecentlyUsed.prev;
        mostRecentlyUsed.prev.next = newWordNode;
        mostRecentlyUsed.prev = newWordNode;
    }

    @Override
    public String remove() {
        //#TODO check what to remove if empty list / map
        Node removedWord = leastRecentlyUsed.next;
        cacheMap.remove(removedWord.value);
        leastRecentlyUsed.next = removedWord.next;
        removedWord.next.prev = leastRecentlyUsed;
        return removedWord.value;
    }

    /**
     * This is an inner class node which will be used for the doubly linked list.
     */
    private static class Node {
        private final String value;
        private Node prev;
        private Node next;

        public Node(String value) {
            this.value = value;
        }
    }
}
