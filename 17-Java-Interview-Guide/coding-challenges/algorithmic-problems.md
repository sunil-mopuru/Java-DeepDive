# Algorithmic Problems for Senior Java Developers (10+ Years Experience)

## 🧮 **Advanced Algorithm Design**

### **Problem 1: LRU Cache with TTL and Statistics**
**Difficulty**: ⭐⭐⭐⭐
**Scenario**: Implement a thread-safe LRU cache with TTL support and comprehensive statistics.

**Requirements**:
- O(1) get, put, and delete operations
- Automatic expiration based on TTL
- Thread-safe for concurrent access
- Statistics: hit rate, miss rate, evictions, etc.

**Solution**:
```java
public class AdvancedLRUCache<K, V> {
    
    private static class Node<K, V> {
        K key;
        V value;
        long accessTime;
        long expireTime;
        Node<K, V> prev, next;
        
        Node(K key, V value, long ttlMillis) {
            this.key = key;
            this.value = value;
            this.accessTime = System.currentTimeMillis();
            this.expireTime = ttlMillis > 0 ? accessTime + ttlMillis : Long.MAX_VALUE;
        }
        
        boolean isExpired() {
            return expireTime <= System.currentTimeMillis();
        }
    }
    
    private final int capacity;
    private final ConcurrentHashMap<K, Node<K, V>> cache;
    private final ReadWriteLock lock = new ReentrantReadWriteLock();
    
    // Doubly-linked list for LRU ordering
    private final Node<K, V> head = new Node<>(null, null, 0);
    private final Node<K, V> tail = new Node<>(null, null, 0);
    
    // Statistics
    private final AtomicLong hits = new AtomicLong();
    private final AtomicLong misses = new AtomicLong();
    private final AtomicLong evictions = new AtomicLong();
    private final AtomicLong expirations = new AtomicLong();
    
    public AdvancedLRUCache(int capacity) {
        this.capacity = capacity;
        this.cache = new ConcurrentHashMap<>(capacity);
        head.next = tail;
        tail.prev = head;
        
        // Background cleanup thread
        startCleanupThread();
    }
    
    public V get(K key) {
        Node<K, V> node = cache.get(key);
        
        if (node == null) {
            misses.incrementAndGet();
            return null;
        }
        
        if (node.isExpired()) {
            cache.remove(key, node);
            lock.writeLock().lock();
            try {
                removeNode(node);
            } finally {
                lock.writeLock().unlock();
            }
            expirations.incrementAndGet();
            misses.incrementAndGet();
            return null;
        }
        
        // Move to head (most recently used)
        lock.writeLock().lock();
        try {
            moveToHead(node);
        } finally {
            lock.writeLock().unlock();
        }
        
        hits.incrementAndGet();
        return node.value;
    }
    
    public void put(K key, V value, long ttlMillis) {
        Node<K, V> newNode = new Node<>(key, value, ttlMillis);
        Node<K, V> existing = cache.put(key, newNode);
        
        lock.writeLock().lock();
        try {
            if (existing != null) {
                removeNode(existing);
            }
            
            addToHead(newNode);
            
            // Check capacity and evict if necessary
            while (cache.size() > capacity) {
                Node<K, V> tail = removeTail();
                if (tail != null) {
                    cache.remove(tail.key, tail);
                    evictions.incrementAndGet();
                }
            }
        } finally {
            lock.writeLock().unlock();
        }
    }
    
    public CacheStatistics getStatistics() {
        long totalRequests = hits.get() + misses.get();
        return new CacheStatistics(
            cache.size(),
            capacity,
            hits.get(),
            misses.get(),
            evictions.get(),
            expirations.get(),
            totalRequests > 0 ? (double) hits.get() / totalRequests : 0.0
        );
    }
    
    // Time complexity: O(1) for all operations
    private void moveToHead(Node<K, V> node) {
        removeNode(node);
        addToHead(node);
        node.accessTime = System.currentTimeMillis();
    }
    
    private void addToHead(Node<K, V> node) {
        node.prev = head;
        node.next = head.next;
        head.next.prev = node;
        head.next = node;
    }
    
    private void removeNode(Node<K, V> node) {
        node.prev.next = node.next;
        node.next.prev = node.prev;
    }
    
    private Node<K, V> removeTail() {
        Node<K, V> last = tail.prev;
        if (last == head) return null;
        removeNode(last);
        return last;
    }
}
```

### **Problem 2: Rate Limiter with Multiple Strategies**
**Difficulty**: ⭐⭐⭐⭐⭐
**Scenario**: Design a distributed rate limiter supporting multiple algorithms.

**Solution**:
```java
public interface RateLimiter {
    boolean tryAcquire(String clientId, int permits);
    boolean tryAcquire(String clientId, int permits, Duration timeout);
}

// Token bucket implementation
public class TokenBucketRateLimiter implements RateLimiter {
    
    private static class Bucket {
        private final long capacity;
        private final long refillRate; // tokens per second
        private volatile long tokens;
        private volatile long lastRefillTime;
        
        Bucket(long capacity, long refillRate) {
            this.capacity = capacity;
            this.refillRate = refillRate;
            this.tokens = capacity;
            this.lastRefillTime = System.currentTimeMillis();
        }
        
        synchronized boolean tryConsume(int permits) {
            refill();
            
            if (tokens >= permits) {
                tokens -= permits;
                return true;
            }
            return false;
        }
        
        private void refill() {
            long now = System.currentTimeMillis();
            long timePassed = now - lastRefillTime;
            long tokensToAdd = (timePassed * refillRate) / 1000;
            
            if (tokensToAdd > 0) {
                tokens = Math.min(capacity, tokens + tokensToAdd);
                lastRefillTime = now;
            }
        }
    }
    
    private final ConcurrentHashMap<String, Bucket> buckets = new ConcurrentHashMap<>();
    private final long capacity;
    private final long refillRate;
    
    public TokenBucketRateLimiter(long capacity, long refillRate) {
        this.capacity = capacity;
        this.refillRate = refillRate;
    }
    
    @Override
    public boolean tryAcquire(String clientId, int permits) {
        Bucket bucket = buckets.computeIfAbsent(clientId, 
            k -> new Bucket(capacity, refillRate));
        return bucket.tryConsume(permits);
    }
    
    @Override
    public boolean tryAcquire(String clientId, int permits, Duration timeout) {
        long deadline = System.currentTimeMillis() + timeout.toMillis();
        
        while (System.currentTimeMillis() < deadline) {
            if (tryAcquire(clientId, permits)) {
                return true;
            }
            
            try {
                Thread.sleep(10); // Small delay before retry
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return false;
            }
        }
        
        return false;
    }
}
```

### **Problem 3: Consistent Hashing with Virtual Nodes**
**Difficulty**: ⭐⭐⭐⭐
**Scenario**: Implement consistent hashing for distributed caching with automatic rebalancing.

**Solution**:
```java
public class ConsistentHashRing<T> {
    
    private final TreeMap<Long, T> ring = new TreeMap<>();
    private final Map<T, Set<Long>> nodeHashes = new ConcurrentHashMap<>();
    private final int virtualNodes;
    private final MessageDigest md5;
    
    public ConsistentHashRing(int virtualNodes) {
        this.virtualNodes = virtualNodes;
        try {
            this.md5 = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }
    
    public synchronized void addNode(T node) {
        Set<Long> hashes = new HashSet<>();
        
        for (int i = 0; i < virtualNodes; i++) {
            String virtualNodeKey = node.toString() + ":" + i;
            long hash = hash(virtualNodeKey);
            ring.put(hash, node);
            hashes.add(hash);
        }
        
        nodeHashes.put(node, hashes);
    }
    
    public synchronized void removeNode(T node) {
        Set<Long> hashes = nodeHashes.remove(node);
        if (hashes != null) {
            for (Long hash : hashes) {
                ring.remove(hash);
            }
        }
    }
    
    public T getNode(String key) {
        if (ring.isEmpty()) {
            return null;
        }
        
        long hash = hash(key);
        
        // Find the first node clockwise from the hash
        Map.Entry<Long, T> entry = ring.ceilingEntry(hash);
        if (entry == null) {
            // Wrap around to the first node
            entry = ring.firstEntry();
        }
        
        return entry.getValue();
    }
    
    // Get multiple nodes for replication
    public List<T> getNodes(String key, int count) {
        if (ring.isEmpty()) {
            return Collections.emptyList();
        }
        
        List<T> result = new ArrayList<>();
        Set<T> seen = new HashSet<>();
        long hash = hash(key);
        
        NavigableMap<Long, T> tailMap = ring.tailMap(hash, true);
        addUniqueNodes(tailMap.values(), result, seen, count);
        
        if (result.size() < count) {
            addUniqueNodes(ring.values(), result, seen, count);
        }
        
        return result;
    }
    
    private void addUniqueNodes(Collection<T> nodes, List<T> result, 
                               Set<T> seen, int maxCount) {
        for (T node : nodes) {
            if (result.size() >= maxCount) {
                break;
            }
            if (seen.add(node)) {
                result.add(node);
            }
        }
    }
    
    private long hash(String key) {
        synchronized (md5) {
            md5.reset();
            md5.update(key.getBytes());
            byte[] digest = md5.digest();
            
            long hash = 0;
            for (int i = 0; i < 8; i++) {
                hash = (hash << 8) | (digest[i] & 0xFF);
            }
            return hash;
        }
    }
}
```

## 🔍 **Complex Data Structures**

### **Problem 4: Trie with Autocomplete and Ranking**
**Expected Answer**:
```java
public class AutocompleTrie {
    
    private static class TrieNode {
        Map<Character, TrieNode> children = new HashMap<>();
        boolean isEndOfWord = false;
        String word = null;
        int frequency = 0;
        
        // Keep top suggestions at each node for fast retrieval
        PriorityQueue<Suggestion> topSuggestions = new PriorityQueue<>(
            (a, b) -> Integer.compare(b.frequency, a.frequency)
        );
    }
    
    private static class Suggestion {
        String word;
        int frequency;
        
        Suggestion(String word, int frequency) {
            this.word = word;
            this.frequency = frequency;
        }
    }
    
    private final TrieNode root = new TrieNode();
    private final int maxSuggestions;
    
    public AutocompleTrie(int maxSuggestions) {
        this.maxSuggestions = maxSuggestions;
    }
    
    public void insert(String word, int frequency) {
        TrieNode current = root;
        
        for (char c : word.toCharArray()) {
            current.children.computeIfAbsent(c, k -> new TrieNode());
            current = current.children.get(c);
            
            // Update suggestions at each level
            updateSuggestions(current, word, frequency);
        }
        
        current.isEndOfWord = true;
        current.word = word;
        current.frequency = frequency;
    }
    
    public List<String> getSuggestions(String prefix) {
        TrieNode node = findNode(prefix);
        if (node == null) {
            return Collections.emptyList();
        }
        
        return node.topSuggestions.stream()
            .map(s -> s.word)
            .collect(Collectors.toList());
    }
    
    private void updateSuggestions(TrieNode node, String word, int frequency) {
        // Remove existing suggestion for the same word
        node.topSuggestions.removeIf(s -> s.word.equals(word));
        
        // Add new suggestion
        node.topSuggestions.offer(new Suggestion(word, frequency));
        
        // Keep only top suggestions
        while (node.topSuggestions.size() > maxSuggestions) {
            node.topSuggestions.poll();
        }
    }
    
    // Time complexity: O(p) where p is prefix length
    private TrieNode findNode(String prefix) {
        TrieNode current = root;
        
        for (char c : prefix.toCharArray()) {
            current = current.children.get(c);
            if (current == null) {
                return null;
            }
        }
        
        return current;
    }
}
```

## 📊 **Performance Analysis**

### **Time Complexity Requirements**:
- **Cache Operations**: O(1) average case
- **Rate Limiting**: O(1) per request
- **Consistent Hashing**: O(log N) for node lookup
- **Autocomplete**: O(P) where P is prefix length

### **Space Complexity Considerations**:
- **Memory efficiency** for large datasets
- **Cache locality** for better performance  
- **Concurrent access patterns**
- **GC pressure minimization**

### **Scalability Factors**:
- **Thread safety** without performance degradation
- **Lock contention** minimization
- **Memory usage** optimization
- **CPU utilization** efficiency