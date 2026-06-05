import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class TestBCrypt {
    public static void main(String[] args) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        boolean matches = encoder.matches("123456", "$2a$10$7JB720yubVSZvUIV7EqK/.VqGOZTH.ulu33dHOiBE8ByOhJIrdAu2");
        System.out.println("Matches 123456: " + matches);
    }
}
