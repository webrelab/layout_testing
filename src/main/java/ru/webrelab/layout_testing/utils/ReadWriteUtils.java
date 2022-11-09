package ru.webrelab.layout_testing.utils;

import lombok.SneakyThrows;
import ru.webrelab.layout_testing.ifaces.IScreenSize;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ReadWriteUtils {
    @SneakyThrows
    public static void write(final String json, final String name, final String browserName, final IScreenSize size, final String... path) {
        final Path fullPath = getPath(name, browserName, size, path);
        if (!Files.exists(fullPath)) {
            Files.createDirectories(fullPath);
        }
        Files.write(fullPath, json.getBytes(StandardCharsets.UTF_8));
    }

    @SneakyThrows
    public static String read(final String name, final String browserName, final IScreenSize size, final String... path) {
        try(Stream<String> lines = Files.lines(getPath(name, browserName, size, path))){
            return lines.collect(Collectors.joining("\n"));
        }
    }

    public static boolean isFileExist(final String name, final String browserName, final IScreenSize size, final String... path) {
        return Files.exists(getPath(name, browserName, size, path));
    }

    @SneakyThrows
    public static Path getPath(final String name, final String browserName, final IScreenSize size, final String... path) {
        Path fullPath = Paths.get("resource", "data", "layouts", browserName, ((Enum<?>) size).name());
        for (final String p : path) {
            fullPath = fullPath.resolve(p);
        }
        return fullPath.resolve(name + ".json");
    }
}
