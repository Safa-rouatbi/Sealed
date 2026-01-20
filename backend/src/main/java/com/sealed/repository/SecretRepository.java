@Repository
public class SecretRepository {

    private final StringRedisTemplate redis;

    public SecretRepository(StringRedisTemplate redis) {
        this.redis = redis;
    }

    public void save(String id, String iv, String ciphertext, long ttlSeconds) {
      
        redis.opsForValue().set("secret:" + id, iv + ":" + ciphertext, ttlSeconds, TimeUnit.SECONDS);
    }
}