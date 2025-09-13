package adapters.version;

import org.gudelker.utilities.Version;

public class VersionAdapter {
    public static Version toVersion(String version) {
        return switch (version) {
            case "1.0" -> Version.V1;
            case "1.1" -> Version.V2;
            default -> throw new IllegalArgumentException("Unsupported version: " + version);
        };
    }
}

