package test;


import java.io.FileWriter;
import java.io.PrintWriter;

public class MainTrain2 {

    public static void testLRU() {
        CacheReplacementPolicy lru=new LRU();
        lru.add("a");
        lru.add("b");
        lru.add("c");
        // LRU - a - b - c - MRU
        lru.add("a");
        // LRU - b - c - a - MRU
        if(!lru.remove().equals("b"))
            System.out.println("wrong implementation for LRU (-10)");
        // LRU - c - a - MRU
        lru.add("c");
        // LRU - a - c - MRU
        lru.add("d");
        // LRU - a - c - d - MRU
        if(!lru.remove().equals("a"))
            System.out.println("wrong implementation for LRU (-10)");
        // LRU - c - d - MRU
        lru.add("b");
        // LRU - c - d - b - MRU
        lru.add("c");
        // LRU - d - b - c - MRU
        if(!lru.remove().equals("d"))
            System.out.println("wrong implementation for LRU (-10)");
        // LRU - b - c - MRU
        if(!lru.remove().equals("b"))
            System.out.println("wrong implementation for LRU (-10)");
        // LRU - c - MRU
        if(!lru.remove().equals("c"))
            System.out.println("wrong implementation for LRU (-10)");
        // LRU - MRU

        System.out.println("LRU works");
    }

    public static void testLFU() {
        CacheReplacementPolicy lfu=new LFU();
        lfu.add("a");
        lfu.add("b");
        lfu.add("b");
        lfu.add("c");
        lfu.add("a");
        // 1 : c
        // 2 : a - b
        if(!lfu.remove().equals("c"))
            System.out.println("wrong implementation for LFU (-10)");
        // 2 : a - b
        if(!lfu.remove().equals("b"))
            System.out.println("wrong implementation for LFU (-10)");
        // 2: a
        lfu.add("d");
        lfu.add("d");
        lfu.add("d");
        lfu.add("d");
        // 2 : a
        // 4 : d
        if(!lfu.remove().equals("a"))
            System.out.println("wrong implementation for LFU (-10)");
        // 4 : d
        lfu.add("a");
        lfu.add("c");
        lfu.add("d");
        // 1 : c - a
        // 5 : d
        if(!lfu.remove().equals("a"))
            System.out.println("wrong implementation for LFU (-10)");
        // 1 : c
        // 5 : d
        lfu.add("c");
        // 2 : c
        // 5 : d
        if(!lfu.remove().equals("c"))
            System.out.println("wrong implementation for LFU (-10)");
        // 5 : d
        if(!lfu.remove().equals("d"))
            System.out.println("wrong implementation for LFU (-10)");

        lfu.add("a");
        lfu.add("b");
        // 1: b - a
        if(!lfu.remove().equals("a"))
            System.out.println("wrong implementation for LFU (-10)");
        lfu.add("a");
        // 1 : a - b
        lfu.add("b");
        lfu.add("a");
        // 2 : b - a
        if(!lfu.remove().equals("b"))
            System.out.println("wrong implementation for LFU (-10)");

        System.out.println("LFU works");
    }

    public static void testCacheManager() {
        CacheManager exists=new CacheManager(3, new LRU());
        boolean b = exists.query("a");
        b|=exists.query("b");
        b|=exists.query("c");

        if(b)
            System.out.println("wrong result for CacheManager first queries (-5)");

        exists.add("a");
        exists.add("b");
        exists.add("c");

        b=exists.query("a");
        b&=exists.query("b");
        b&=exists.query("c");

        if(!b)
            System.out.println("wrong result for CacheManager second queries (-5)");

        boolean bf = exists.query("d"); // false, LRU is "a"
        exists.add("d");
        boolean bt = exists.query("d"); // true
        bf|= exists.query("a"); // false
        exists.add("a");
        bt &= exists.query("a"); // true, LRU is "b"

        if(bf || ! bt)
            System.out.println("wrong result for CacheManager last queries (-10)");

        System.out.println("CacheManager works");

    }

    public static void testBloomFilter() {
        BloomFilter bf = new BloomFilter(256,"MD5","SHA1");
        String[] words = "the quick brown fox jumps over the lazy dog".split(" ");
        for(String w : words)
            bf.add(w);
        if(!bf.toString().equals("0010010000000000000000000000000000000000000100000000001000000000000000000000010000000001000000000000000100000010100000000010000000000000000000000000000000110000100000000000000000000000000010000000001000000000000000000000000000000000000000000000000000001"))
            System.out.println("problem in the bit vector of the bloom filter (-10)");

        boolean found=true;
        for(String w : words)
            found &= bf.contains(w);

        if(!found)
            System.out.println("problem finding words that should exist in the bloom filter (-15)");

        found=false;
        for(String w : words)
            found |= bf.contains(w+"!");

        if(found)
            System.out.println("problem finding words that should not exist in the bloom filter (-15)");

        System.out.println("BloomFilter works");
    }

    public static void testIOSearch() throws Exception{
        String words1 = "the quick brown fox \n jumps over the lazy dog";
        String words2 = "A Bloom filter is a space efficient probabilistic data structure, \n conceived by Burton Howard Bloom in 1970";
        PrintWriter out = new PrintWriter(new FileWriter("text1.txt"));
        out.println(words1);
        out.close();
        out = new PrintWriter(new FileWriter("text2.txt"));
        out.println(words2);
        out.close();

        if(!IOSearcher.search("is", "text1.txt","text2.txt"))
            System.out.println("your IOsearch did not found a word (-5)");
        if(IOSearcher.search("cat", "text1.txt","text2.txt"))
            System.out.println("your IOsearch found a word that does not exist (-5)");

        System.out.println("IOSearch works");
    }

    public static void testDictionary() {
        Dictionary d = new Dictionary("text1.txt","text2.txt");
        if(!d.query("is"))
            System.out.println("problem with dictionary in query (-5)");
        if(!d.challenge("lazy"))
            System.out.println("problem with dictionary in challenge (-5)");

        System.out.println("Dictionary works");
    }

    public static void main(String[] args) {
        testLRU();
        testLFU();
        testCacheManager();
        testBloomFilter();
        try {
            testIOSearch();
        } catch(Exception e) {
            System.out.println("you got some exception (-10)");
        }
        testDictionary();
        System.out.println("done");
    }
}
