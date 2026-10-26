package com.example.fileIO;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.nio.file.StandardOpenOption;
import java.util.List;
import java.util.stream.Stream;

public class Nio2Mastery {

    public static void main(String[] args) {

        // Define base work directory path
        Path baseDir = Paths.get("nio_workspace");
        Path sourceDir = baseDir.resolve("source_folder");
        Path targetDir = baseDir.resolve("target_folder");

        Path sourceFile = sourceDir.resolve("app.log");
        Path copiedFile = targetDir.resolve("app_backup.log");
        Path movedFile = targetDir.resolve("app_archive.log");

        try {
            // =========================================================================
            // 1. Using Path Interface (Path manipulation)
            // =========================================================================
            System.out.println("--- 1. PATH INTERFACE OPERATIONS ---");
            System.out.println("FileName: " + sourceFile.getFileName());
            System.out.println("Parent: " + sourceFile.getParent());
            System.out.println("Root: " + sourceFile.getRoot());
            System.out.println("Absolute Path: " + sourceFile.toAbsolutePath());
            System.out.println("Normalized Path: " + Paths.get("nio_workspace/./source_folder/../source_folder").normalize());
            System.out.println();


            // =========================================================================
            // 2. Using Files Class (Check, Create, Write, Copy, Move, Delete)
            // =========================================================================
            System.out.println("--- 2. FILES CLASS OPERATIONS ---");

            // Create directories if they don't exist
            Files.createDirectories(sourceDir);
            Files.createDirectories(targetDir);

            // Checking file/directory attributes
            System.out.println("Directory exists: " + Files.exists(sourceDir));
            System.out.println("Is Directory: " + Files.isDirectory(sourceDir));

            // Create and write content to source file
            List<String> logEntries = List.of(
                    "2026-10-26 [INFO] Application Started",
                    "2026-10-26 [DEBUG] Loading Configuration",
                    "2026-10-26 [WARN] Low Memory Warning",
                    "2026-10-26 [ERROR] Database Connection Failed",
                    "2026-10-26 [INFO] Application Shutdown"
            );
            Files.write(sourceFile, logEntries, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
            System.out.println("Source file written successfully.");

            // Copy file (overwriting if destination file exists)
            Files.copy(sourceFile, copiedFile, StandardCopyOption.REPLACE_EXISTING);
            System.out.println("Copied file to: " + copiedFile);

            // Move / Rename file
            Files.move(copiedFile, movedFile, StandardCopyOption.REPLACE_EXISTING);
            System.out.println("Moved file to: " + movedFile);

            // Check file attributes
            System.out.println("Moved File Size: " + Files.size(movedFile) + " bytes");
            System.out.println("Is Regular File: " + Files.isRegularFile(movedFile));
            System.out.println();


            // =========================================================================
            // 3. Using Stream API with NIO.2
            // =========================================================================
            System.out.println("--- 3. STREAM API WITH NIO.2 ---");

            // 3a. Reading lines from a file as a Stream (Files.lines)
            System.out.println("-> Filtering ERROR logs using Files.lines():");
            try (Stream<String> linesStream = Files.lines(sourceFile)) {
                linesStream
                        .filter(line -> line.contains("[ERROR]") || line.contains("[WARN]"))
                        .map(String::toUpperCase)
                        .forEach(System.out::println);
            }

            // 3b. Directory Listing (Files.list) - Non-recursive
            System.out.println("\n-> Listing items in workspace using Files.list():");
            try (Stream<Path> listStream = Files.list(baseDir)) {
                listStream.forEach(path -> System.out.println("Item: " + path.getFileName()));
            }

            // 3c. Walking Directory Tree (Files.walk) - Recursive search
            System.out.println("\n-> Finding all .log files recursively using Files.walk():");
            try (Stream<Path> walkStream = Files.walk(baseDir)) {
                walkStream
                        .filter(Files::isRegularFile)
                        .filter(p -> p.toString().endsWith(".log"))
                        .forEach(p -> System.out.println("Found log: " + p));
            }

            // 3d. Finding Files with Attributes (Files.find)
            System.out.println("\n-> Finding files modified recently using Files.find():");
            try (Stream<Path> findStream = Files.find(
                    baseDir,
                    3, // Max depth
                    (path, attr) -> attr.isRegularFile() && attr.size() > 0
            )) {
                findStream.forEach(p -> System.out.println("Matched file: " + p));
            }

            System.out.println();

            // =========================================================================
            // 4. Cleanup Operations (Files.delete / deleteIfExists)
            // =========================================================================
            System.out.println("--- 4. CLEANUP ---");
//            Files.deleteIfExists(movedFile);
//            Files.deleteIfExists(sourceFile);
//            Files.deleteIfExists(sourceDir);
//            Files.deleteIfExists(targetDir);
//            Files.deleteIfExists(baseDir);
//            System.out.println("Cleanup completed successfully.");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}