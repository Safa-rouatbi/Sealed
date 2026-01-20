@Service
public class SecretService {

    private final SecretRepository secretRepository;

    public SecretService(SecretRepository secretRepository) {
        this.secretRepository = secretRepository;
    }

    public String createSecret(CreateSecretRequest request) {
        if (request.getTtlSeconds() > 604800) {
            throw new RuntimeException("TTL trop grand");
        }
        String id = UUID.randomUUID().toString();
        secretRepository.save(id, request.getIv(), request.getCiphertext(), request.getTtlSeconds());
        return id;
    }
}