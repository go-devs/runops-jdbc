package ninja.ebanx.runops.api;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class AuthorizationResolver {
    private static final JWTResolver[] resolvers = {new PropertyResolver(), new ConfigFileResolver()};
    public static String resolve() {
        for (var r : resolvers) {
            var jwt = r.getJWT();
            if (!(jwt == null || jwt.isEmpty()))
                return jwt;
        }
        return "";
    }

    private interface JWTResolver {
        String getJWT();
    }

    private static class PropertyResolver implements JWTResolver {

        @Override
        public String getJWT() {
            return System.getProperty("RUNOPS_JWT");
        }
    }

    private static class ConfigFileResolver implements JWTResolver {

        @Override
        public String getJWT() {
            Path fileName = Path.of(System.getProperty("user.home"),"/.runops/config");
            try {
                return Files.readString(fileName);
            } catch (IOException e) {
                return "";
            }
        }
    }
}
