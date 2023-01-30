package fr.esgi.ecommerce.service;

import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;
import java.util.stream.Stream;

public interface FilesStorageService {
    public void save(MultipartFile file, Path path);

    public byte[] load(String filename);

    public void deleteAll();

    public Stream<Path> loadAll();
}