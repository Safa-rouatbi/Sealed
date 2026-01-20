@RestController
@RequestMapping("/api/secrets")
public class SecretController {

    private final SecretService secretService;

    public SecretController(SecretService secretService) {
        this.secretService = secretService;
    }

    @PostMapping
    public Map<String, String> create(@RequestBody CreateSecretRequest request) {
        String id = secretService.createSecret(request);
        return Map.of("id", id);
    }
}