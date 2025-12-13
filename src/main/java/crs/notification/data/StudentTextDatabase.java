/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package crs.notification.data;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.logging.Logger;

import crs.notification.NotificationLogger;

// Reads student records from multiple team-provided text files with different schemas.
public class StudentTextDatabase {
    private static final Logger LOGGER = NotificationLogger.getLogger();

    private static final String FILE_STUDENTS_CSV = "data/students-abood.txt";
    private static final String FILE_LOGIN = "data/LOGIN.txt";

    // Lazy cache to avoid re-reading flat files on every lookup; refreshed on
    // demand when refresh() is called.
    private volatile List<StudentRecord> cachedRecords;

    // Loads student records and returns a defensive copy; shared cache keeps IO
    // minimal.
    // Public API to obtain all students; callers get a copy to avoid accidental
    // mutation.
    public List<StudentRecord> loadStudents() {
        List<StudentRecord> loaded = ensureLoaded();
        return new ArrayList<>(loaded);
    }

    // Lookup by ID (case-insensitive), logging misses for diagnostics.
    public StudentRecord findById(String id) {
        if (id == null) {
            return null;
        }
        Optional<StudentRecord> match = ensureLoaded().stream()
                .filter(s -> id.equalsIgnoreCase(s.getId()))
                .findFirst();
        match.ifPresent(s -> LOGGER.info(() -> "Found student/user: " + s));
        if (!match.isPresent()) {
            LOGGER.warning(() -> "Student/user not found for id: " + id);
        }
        return match.orElse(null);
    }

    // Lookup by username (case-insensitive), logging misses for diagnostics.
    public StudentRecord findByUsername(String username) {
        if (username == null) {
            return null;
        }
        Optional<StudentRecord> match = ensureLoaded().stream()
                .filter(s -> username.equalsIgnoreCase(s.getUsername()))
                .findFirst();
        match.ifPresent(s -> LOGGER.info(() -> "Found student/user by username: " + s));
        if (!match.isPresent()) {
            LOGGER.warning(() -> "Student/user not found for username: " + username);
        }
        return match.orElse(null);
    }

    // Clears the cache so that the next call reloads from disk (e.g., after
    // external edits).
    // Allows callers to force a reload on the next access (e.g., external file
    // edits).
    public void refresh() {
        cachedRecords = null;
    }

    // Ensures the cache is populated exactly once per refresh; synchronized for
    // thread safety.
    private List<StudentRecord> ensureLoaded() {
        List<StudentRecord> snapshot = cachedRecords;
        if (snapshot != null) {
            return snapshot;
        }
        synchronized (this) {
            if (cachedRecords == null) {
                Map<String, StudentRecord> aggregated = new LinkedHashMap<>();

                loadCsvStudents(aggregated, FILE_STUDENTS_CSV);
                loadLoginUsers(aggregated, FILE_LOGIN);

                cachedRecords = new ArrayList<>(aggregated.values());
                LOGGER.info(() -> "Loaded " + cachedRecords.size() + " records from text databases");
            }
            return cachedRecords;
        }
    }

    // Parses CSV with header:
    // StudentID,FirstName,LastName,Major,Courses,Year,Email.
    private void loadCsvStudents(Map<String, StudentRecord> out, String fileName) {
        Path path = Paths.get(fileName);
        if (!Files.exists(path)) {
            return;
        }
        try (BufferedReader reader = new BufferedReader(new FileReader(path.toFile()))) {
            String line;
            boolean first = true;
            int idxId = 0;
            int idxFirst = 1;
            int idxLast = 2;
            int idxProgram = 3; // "Major"
            int idxYear = 5;
            int idxEmail = 6;
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) {
                    continue;
                }
                if (first && line.toLowerCase().contains("studentid")) {
                    String[] header = line.split(",");
                    idxId = headerIndex(header, "studentid", idxId);
                    idxFirst = headerIndex(header, "firstname", idxFirst);
                    idxLast = headerIndex(header, "lastname", idxLast);
                    idxProgram = headerIndex(header, "major", idxProgram);
                    idxYear = headerIndex(header, "year", idxYear);
                    idxEmail = headerIndex(header, "email", idxEmail);
                    first = false;
                    continue; // skip header row
                }
                first = false;
                String[] parts = line.split(",");
                StudentRecord record = new StudentRecord(
                        safe(parts, idxId),
                        safe(parts, idxFirst),
                        safe(parts, idxLast),
                        safe(parts, idxEmail),
                        safe(parts, idxProgram),
                        safe(parts, idxYear),
                        "STUDENT",
                        null);
                merge(out, record);
            }
            LOGGER.info(() -> "Loaded CSV students from " + fileName);
        } catch (IOException ex) {
            LOGGER.warning(() -> "Failed to read " + fileName + ": " + ex.getMessage());
        }
    }

    // Finds a header index by case-insensitive match with fallback.
    private int headerIndex(String[] header, String name, int fallback) {
        if (header == null || name == null) {
            return fallback;
        }
        for (int i = 0; i < header.length; i++) {
            String h = header[i];
            if (h != null && h.trim().equalsIgnoreCase(name)) {
                return i;
            }
        }
        return fallback;
    }

    // Parses LOGIN.txt style: role,id,first,last,username,...,email,year,program.
    private void loadLoginUsers(Map<String, StudentRecord> out, String fileName) {
        Path path = Paths.get(fileName);
        if (!Files.exists(path)) {
            return;
        }
        try (BufferedReader reader = new BufferedReader(new FileReader(path.toFile()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) {
                    continue;
                }
                String[] parts = line.split(",");
                if (parts.length < 5) {
                    continue;
                }
                String role = safe(parts, 0);
                String id = safe(parts, 1);
                String first = safe(parts, 2);
                String last = safe(parts, 3);
                String username = safe(parts, 4);

                // email: first token containing '@'
                String email = null;
                for (String part : parts) {
                    if (part != null && part.contains("@")) {
                        email = part.trim();
                        break;
                    }
                }

                // year: token containing "YEAR"
                String year = null;
                for (String part : parts) {
                    if (part != null && part.toUpperCase().contains("YEAR")) {
                        year = part.trim();
                        break;
                    }
                }

                // program: the token right before year (if found) else a reasonable middle
                // token
                String program = null;
                if (year != null) {
                    for (int i = 0; i < parts.length; i++) {
                        if (year.equals(parts[i].trim()) && i > 0) {
                            program = safe(parts, i - 1);
                            break;
                        }
                    }
                }
                if (program == null && parts.length >= 7) {
                    program = safe(parts, 5);
                }

                StudentRecord record = new StudentRecord(id, first, last, email, program, year, role, username);
                merge(out, record);
            }
            LOGGER.info(() -> "Loaded users from " + fileName);
        } catch (IOException ex) {
            LOGGER.warning(() -> "Failed to read " + fileName + ": " + ex.getMessage());
        }
    }

    // Consolidates records sharing the same id, preferring already-set fields.
    private void merge(Map<String, StudentRecord> out, StudentRecord incoming) {
        if (incoming.getId() == null || incoming.getId().trim().isEmpty()) {
            return;
        }
        String key = incoming.getId().trim();
        if (!out.containsKey(key)) {
            out.put(key, incoming);
            return;
        }

        StudentRecord current = out.get(key);
        // Fill missing fields from incoming.
        if (isBlank(current.getFirstName()) && !isBlank(incoming.getFirstName())) {
            current.setFirstName(incoming.getFirstName());
        }
        if (isBlank(current.getLastName()) && !isBlank(incoming.getLastName())) {
            current.setLastName(incoming.getLastName());
        }
        if (isBlank(current.getEmail()) && !isBlank(incoming.getEmail())) {
            current.setEmail(incoming.getEmail());
        }
        if (isBlank(current.getProgram()) && !isBlank(incoming.getProgram())) {
            current.setProgram(incoming.getProgram());
        }
        if (isBlank(current.getYear()) && !isBlank(incoming.getYear())) {
            current.setYear(incoming.getYear());
        }
        if (isBlank(current.getRole()) && !isBlank(incoming.getRole())) {
            current.setRole(incoming.getRole());
        }
        if (isBlank(current.getUsername()) && !isBlank(incoming.getUsername())) {
            current.setUsername(incoming.getUsername());
        }
    }

    // Null/whitespace-safe array access.
    private String safe(String[] arr, int idx) {
        if (idx < 0 || idx >= arr.length) {
            return null;
        }
        String val = arr[idx];
        return val == null ? null : val.trim();
    }

    // Reusable blank check for merge logic.
    private boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }
}
