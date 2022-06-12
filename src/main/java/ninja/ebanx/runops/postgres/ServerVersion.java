package ninja.ebanx.runops.postgres;

public enum ServerVersion {

    INVALID("0.0.0"),
    v8_2("8.2.0"),
    v8_3("8.3.0"),
    v8_4("8.4.0"),
    v9_0("9.0.0"),
    v9_1("9.1.0"),
    v9_2("9.2.0"),
    v9_3("9.3.0"),
    v9_4("9.4.0"),
    v9_5("9.5.0"),
    v9_6("9.6.0"),
    v10("10"),
    v11("11"),
    v12("12"),
    v13("13"),
    v14("14"),
    v15("15"),
    v16("16");

    private final int version;
    public int getVersionNum() {
        return version;
    }

    ServerVersion(String version) {
        this.version = parseServerVersionStr(version);
    }

    static int parseServerVersionStr(String serverVersion) throws NumberFormatException {
        final int limit = 3;
        var parts = serverVersion.split("\\.", limit);
        var pow = new int[] {4, 2, 0};
        int version = 0;
        for (int i = 0; i < limit; i++) {
            version += Integer.parseInt(parts[i]) * pow[i];
        }
        return version;
    }
}