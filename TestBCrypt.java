import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class TestBCrypt {
    public static void main(String[] args) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        System.out.println("Matches: " + encoder.matches("123456", "$2a$10$7JB720yubVSZvUIV7EqK/.VqGOZTH.ulu33dHOiBE8ByOhJIrdAu2"));
    }
}
