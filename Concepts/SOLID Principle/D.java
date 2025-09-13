// D — Dependency Inversion Principle

// High-level modules should depend on abstractions, not concrete implementations. Inject dependencies; don’t new them inside.
import java.util.Optional;

class User { 
    private final String id;
    private final String name;
    User(String id, String name) { this.id = id; this.name = name; }
    public String getId() { return id; }
    public String getName() { return name; }
}

interface UserRepository {
    Optional<User> findById(String id);
    void save(User user);
}

class InMemoryUserRepository implements UserRepository {
    private final java.util.Map<String, User> store = new java.util.HashMap<>();
    public Optional<User> findById(String id) { return Optional.ofNullable(store.get(id)); }
    public void save(User user) { store.put(user.getId(), user); }
}

class UserService {
    private final UserRepository repo;
    UserService(UserRepository repo) { this.repo = repo; }
    public User getOrCreate(String id, String name) {
        return repo.findById(id).orElseGet(() -> {
            User u = new User(id, name);
            repo.save(u);
            return u;
        });
    }
}

public class D {
    public static void main(String[] args) {
        UserRepository repo = new InMemoryUserRepository();
        UserService service = new UserService(repo);
        User u1 = service.getOrCreate("1", "Hitesh");
        User u2 = service.getOrCreate("1", "Duplicate");
        System.out.println(u1.getName());
        System.out.println(u2.getName());
    }
}
