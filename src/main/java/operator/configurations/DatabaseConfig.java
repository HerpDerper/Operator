package operator.configurations;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DatabaseConfig {

    public static String username = "";

    public static String password = "";

    public static String dbName = "";

    public static String host = "";

    private static String applicationProperties = "src/main/resources/application.properties";

    public static void init() {
        Pattern patternHost = Pattern.compile("\\{MYSQL_HOST:(.+)\\}", Pattern.MULTILINE);
        Pattern patternDBName = Pattern.compile("\\d/(.+)$", Pattern.MULTILINE);

        Matcher hostMatcher = patternHost.matcher(loadProperties("spring.datasource.url"));
        Matcher dbNameMatcher = patternDBName.matcher(loadProperties("spring.datasource.url"));

        hostMatcher.find();
        dbNameMatcher.find();

        String host = hostMatcher.group(1);
        String dbName = dbNameMatcher.group(1);

        String dbUser = loadProperties("spring.datasource.username");
        String dbPass = loadProperties("spring.datasource.password");

        DatabaseConfig.username = dbUser;
        DatabaseConfig.password = dbPass;
        DatabaseConfig.dbName = dbName;
        DatabaseConfig.host = host;
    }

    private static String loadProperties(String name) {
        Properties properties = new Properties();
        try (FileInputStream propStream = new FileInputStream(applicationProperties)) {
            properties.load(propStream);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return properties.getProperty(name);
    }

}