package helpers;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class ConfigProcessor {
    private String currentFilePath;

    public ConfigProcessor() {}

    public void setWorkingFilePath(String currentFilePath) {
        this.currentFilePath = currentFilePath;
    }

    /**
     *
     * @param sectionName Name of the processed section of the currentFilePath
     * @param keyEnumClass Class of the enum that will be returned as Map key
     * @param valueParser Parser that will be used for the Map value
     * @return Hashmap with key K and value V
     * @param <K> Enum class
     * @param <V> Unrestricted class
     */
    public <K extends Enum<K>, V> HashMap<K, V> processSection(
            String sectionName,
            Class<K> keyEnumClass,
            Function<String, V> valueParser) {

        HashMap<K, V> result = new HashMap<>();

        HashMap<String, String> rawData = processSectionToStringValues(sectionName);

        if (rawData == null) return result;

        for (Map.Entry<String, String> entry : rawData.entrySet()) {
            try {
                // Convert the key to enum
                K key = Enum.valueOf(keyEnumClass, entry.getKey().toUpperCase());

                // Convert the value using the gotten parser
                V value = valueParser.apply(entry.getValue());

                result.put(key, value);
            } catch (Exception e) {
                System.err.println("Error processing entry: " + entry.getKey() + " = " + entry.getValue());
            }
        }
        return result;
    }

    /**
     * T es el tipo de Enum que quieras usar.
     * @param sectionName Nombre de la sección [Seccion]
     * @param enumClass La clase del enum (ej: PlayerAction.class)
     */
    public <T extends Enum<T>> HashMap<T, String> processSectionToEnumType(String sectionName, Class<T> enumClass) {
        HashMap<T, String> configs = new HashMap<>();

        // 1. Obtenemos el mapa de Strings (puedes reutilizar tu método actual)
        HashMap<String, String> rawMap = processSectionToStringValues(sectionName);

        if (rawMap == null) return null;

        // 2. Convertimos las llaves String a el Enum tipo T
        for (Map.Entry<String, String> entry : rawMap.entrySet()) {
            try {
                // Normalizamos a mayúsculas para evitar errores de escritura en el config
                T enumValue = Enum.valueOf(enumClass, entry.getKey().toUpperCase());
                configs.put(enumValue, entry.getValue());
            } catch (IllegalArgumentException e) {
                System.err.println("La llave '" + entry.getKey() + "' no existe en el Enum " + enumClass.getSimpleName());
            }
        }

        return configs;
    }

    public HashMap<String, String> processSectionToStringValues(String sectionName) {
        // Try-with-resources ensures the file is closed automatically
        try (BufferedReader br = new BufferedReader(new FileReader(currentFilePath))) {
            String line;
            boolean isInsideSection = false;
            HashMap<String, String> configs = new HashMap<>();

            while ((line = br.readLine()) != null) {
                line = line.trim();

                // Skip empty lines to prevent StringIndexOutOfBoundsException
                if (line.isEmpty()) continue;

                // Skip comments
                if (line.charAt(0) == ';') continue;

                // Handle Section Headers
                if (line.startsWith("[") && line.endsWith("]")) {
                    String currentSection = line.replace("[", "").replace("]", "").trim();

                    if (currentSection.equalsIgnoreCase(sectionName)) {
                        isInsideSection = true;
                        continue;
                    } else {
                        // If we were reading our target section and found a new one, we are done
                        if (isInsideSection) {
                            return configs;
                        }
                        isInsideSection = false;
                    }
                }

                // Read properties only if we are inside the correct section
                if (isInsideSection) {
                    if (line.contains("=")) {
                        String[] vals = line.split("=");

                        if (vals.length >= 2) {
                            // Store key and value as strings
                            configs.put(vals[0].trim(), vals[1].trim());
                        }
                    }
                }
            }

            if (!configs.isEmpty()) return configs;

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public HashMap<String, Integer> processSectionToIntegerValues(String sectionName) {
        // Try-with-resources ensures the file is closed automatically
        try (BufferedReader br = new BufferedReader(new FileReader(currentFilePath))) {
            String line;
            boolean isInsideSection = false;
            HashMap<String, Integer> configs = new HashMap<>();

            while ((line = br.readLine()) != null) {
                line = line.trim();

                // Skip empty lines to prevent StringIndexOutOfBoundsException
                if (line.isEmpty()) continue;

                // Skip comments
                if (line.charAt(0) == ';') continue;

                // Handle Section Headers
                if (line.startsWith("[") && line.endsWith("]")) {
                    String currentSection = line.replace("[", "").replace("]", "").trim();

                    if (currentSection.equalsIgnoreCase(sectionName)) {
                        isInsideSection = true;
                        continue; // Move to the next line to read properties
                    } else {
                        // If we were reading our target section and found a new one, we are done
                        if (isInsideSection) {
                            return configs;
                        }
                        isInsideSection = false;
                    }
                }

                // Read properties only if we are inside the correct section
                if (isInsideSection) {
                    if (line.contains("=")) {
                        String[] vals = line.split("=");

                        if (vals.length >= 2) {
                            String key = vals[0].trim();
                            try {
                                int value = Integer.parseInt(vals[1].trim());
                                configs.put(key, value);
                            } catch (NumberFormatException e) {
                                System.err.println("Invalid number format for key: " + key);
                            }
                        }
                    }
                }
            }

            if (!configs.isEmpty()) return configs;

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public HashMap<String, Double> processSectionToDoubleValues(String sectionName) {
        // Try-with-resources ensures the file is closed automatically
        try (BufferedReader br = new BufferedReader(new FileReader(currentFilePath))) {
            String line;
            boolean isInsideSection = false;
            HashMap<String, Double> configs = new HashMap<>();

            while ((line = br.readLine()) != null) {
                line = line.trim();

                // Skip empty lines to prevent StringIndexOutOfBoundsException
                if (line.isEmpty()) continue;

                // Skip comments
                if (line.charAt(0) == ';') continue;

                // Handle Section Headers
                if (line.startsWith("[") && line.endsWith("]")) {
                    String currentSection = line.replace("[", "").replace("]", "").trim();

                    if (currentSection.equalsIgnoreCase(sectionName)) {
                        isInsideSection = true;
                        continue;
                    } else {
                        // If we were reading our target section and found a new one, we are done
                        if (isInsideSection) {
                            return configs;
                        }
                        isInsideSection = false;
                    }
                }

                // Read properties only if we are inside the correct section
                if (isInsideSection) {
                    if (line.contains("=")) {
                        String[] vals = line.split("=");

                        if (vals.length >= 2) {
                            String key = vals[0].trim();
                            try {
                                // Parse value to Double
                                double value = Double.parseDouble(vals[1].trim());
                                configs.put(key, value);
                            } catch (NumberFormatException e) {
                                System.err.println("Invalid number format for key: " + key);
                            }
                        }
                    }
                }
            }

            if (!configs.isEmpty()) return configs;

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
