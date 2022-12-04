package test;


import java.util.Collections;
import java.util.HashMap;
import java.util.Map;


public class LFU implements CacheReplacementPolicy {
    Map<String, Node> cacheMap = new HashMap<>();
    Map<Integer, DoublyList> frequencyMap = new HashMap<>();

    public LFU() {
    }

    @Override
    public void add(String word) {
        if (cacheMap.containsKey(word)) {
            updateFrequency(word);
        }
        else {
            initiateWordInCache(word);
        }
    }

    private void updateFrequency(String word) {
        // Get the node and remove from the frequency map
        Node wordNode = cacheMap.get(word);
        frequencyMap.get(wordNode.frequency).removeNode(wordNode);

        // Update the word's frequency
        wordNode.frequency++;

        // Add the word back to the frequency map with the updated frequency
        if (!frequencyMap.containsKey(wordNode.frequency)) {
            frequencyMap.put(wordNode.frequency, new DoublyList(wordNode.frequency));
        }
        frequencyMap.get(wordNode.frequency).addNode(wordNode);
    }

    private void initiateWordInCache(String word) {
        Node wordNode = new Node(word, 1);
        cacheMap.put(word, wordNode);
        if (!frequencyMap.containsKey(wordNode.frequency)) {
            frequencyMap.put(1, new DoublyList(1));
        }
        frequencyMap.get(1).addNode(wordNode);
    }

    @Override
    public String remove() {
        Node leastFrequentlyUsed;
        // Get the minimal frequency, the keys are integers that represent frequency.
        int freq = Collections.min(frequencyMap.keySet());

        // Get the least frequently used, the node closest to the tail, and remove it from both maps
        leastFrequentlyUsed = frequencyMap.get(freq).tail.prev;
        frequencyMap.get(freq).removeNode(leastFrequentlyUsed);
        cacheMap.remove(leastFrequentlyUsed.value);

        return leastFrequentlyUsed.value;
    }


    private static class Node {
        private final String value;
        private int frequency;
        private Node prev;
        private Node next;

        private Node(String value, int frequency) {
            this.value = value;
            this.frequency = frequency;
        }
    }

    private class DoublyList {
        Node head, tail;

        public DoublyList(int frequency) {
            head = new Node("head", frequency);
            tail = new Node("tail", frequency);
            head.next = tail;
            tail.prev = head;
        }

        public void addNode(Node node) {
            head.next.prev = node;
            node.next = head.next;
            head.next = node;
            node.prev = head;
        }

        public void removeNode(Node node) {
            node.prev.next = node.next;
            node.next.prev = node.prev;

            Node head = frequencyMap.get(node.frequency).head;
            Node tail = frequencyMap.get(node.frequency).tail;
            if (head.next == tail)
                frequencyMap.remove(node.frequency);
        }
    }

}
