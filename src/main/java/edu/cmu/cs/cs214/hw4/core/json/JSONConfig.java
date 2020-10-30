package edu.cmu.cs.cs214.hw4.core.json;

import com.google.gson.Gson;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.nio.charset.StandardCharsets;

/**
 * Parses the tiles.json file located in main/resources/tiles.json
 */
public class JSONConfig {
    // Disabling Checkstyle to prevent errors for public JSON fields.
    // CHECKSTYLE:OFF

    /**
     * Representation of a JSON tile.
     */
    @edu.umd.cs.findbugs.annotations.SuppressFBWarnings("UUF_UNUSED_PUBLIC_OR_PROTECTED_FIELD")
    public static class JSONTile {
        public Character id;
        public String top;
        public String right;
        public String bottom;
        public String left;
        public String center;
        public Boolean shield;
        public String[][] links;
        public Integer amount;
    }

    /**
     * Collection of all tiles.
     */
    public static class JSONDeck {
        public JSONTile[] tiles;
    }

    /**
     * Method to parse the JSON file.
     * @param configFile File to be parsed.
     * @return Class representation of JSON.
     */
    public static JSONDeck parse(String configFile) {
        Gson gson = new Gson();
        try (Reader reader = new FileReader(new File(configFile), StandardCharsets.UTF_8)) {
            return gson.fromJson(reader, JSONDeck.class);
        } catch (IOException e) {
            throw new IllegalArgumentException("Error when reading file: " + configFile, e);
        }
    }
    // CHECKSTYLE:ON
}