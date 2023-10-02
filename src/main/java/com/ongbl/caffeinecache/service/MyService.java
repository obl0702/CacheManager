package com.ongbl.caffeinecache.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class MyService<T> {

    private final CacheManager cacheManager;

    public MyService(CacheManager cacheManager) {
        this.cacheManager = cacheManager;
    }

    @Cacheable("myCache") // Specify the cache name
    public String getCachedData2(String key) {
        // This method will be cached
        // Implement your logic here to retrieve the data
        return getData(key);
    }

    private String getData(String key){
        log.info("getData: " + key);
        if(key.equals("ZZZ1") || key.equals("ZZZ2"))
            return "ZZZZ";
        else
            return key;
    }

    public T getCachedData(String key, Class<T> valueType) {
        Cache cache = cacheManager.getCache("myCache");
        if (cache != null) {
            Cache.ValueWrapper valueWrapper = cache.get(key);
            if (valueWrapper != null) {
                return valueType.cast(valueWrapper.get());
            }
        }
        return null; // Return null or handle cache miss as appropriate for your use case.
    }

    @CachePut("myCache") // Specify the cache name
    public T updateCachedData(String key, T newData) {

        // Update the cache with the new data
        Cache cache = cacheManager.getCache("myCache");
        if (cache != null) {
            cache.put(key, newData);
        }

        return newData;
    }

    @CacheEvict(value = "myCache", allEntries = true)
    public void evictWholeCache() {
        // Evict a specific entry from the cache
    }

    @CacheEvict(value = "myCache", key = "#key")
    public void evictCache(String key) {
        // This method will evict the entry with the provided key from the cache
    }
}