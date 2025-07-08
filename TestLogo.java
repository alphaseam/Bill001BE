import org.springframework.core.io.ClassPathResource;
import java.io.InputStream;

public class TestLogo {
    public static void main(String[] args) {
        try {
            ClassPathResource resource = new ClassPathResource("static/logo.png");
            if (resource.exists()) {
                try (InputStream is = resource.getInputStream()) {
                    byte[] imageBytes = is.readAllBytes();
                    System.out.println("✅ Logo loaded successfully! Size: " + imageBytes.length + " bytes");
                }
            } else {
                System.out.println("❌ Logo not found at static/logo.png");
                // Try alternate path
                ClassPathResource resource2 = new ClassPathResource("logo.png");
                if (resource2.exists()) {
                    System.out.println("✅ Found logo at logo.png instead");
                } else {
                    System.out.println("❌ Logo not found at logo.png either");
                }
            }
        } catch (Exception e) {
            System.out.println("❌ Error loading logo: " + e.getMessage());
        }
    }
}
