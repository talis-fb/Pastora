package br.ufrn.imd.pastora.components;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component
public class PhotoStorageComponent {
  private String uploadDir;

  public PhotoStorageComponent() {
    this.uploadDir = System.getProperty("user.home") + "/pastora-uploads";
    Path uploadPath = Paths.get(uploadDir);

    try {
      if (!Files.exists(uploadPath)) {
        Files.createDirectories(uploadPath);
        System.out.println("Upload directory created at: " + uploadPath.toAbsolutePath());
      }
    } catch (IOException e) {
      throw new RuntimeException("Error creating upload directory: " + uploadPath.toAbsolutePath(), e);
    }
  }

  public String storePhoto(MultipartFile file) {
    if (file.isEmpty()) {
      throw new IllegalArgumentException("The uploaded file is empty");
    }

    try {
      String fileExtension = getFileExtension(file.getOriginalFilename());
      String uniqueFileName = UUID.randomUUID().toString() + fileExtension;

      // Caminho completo do arquivo
      Path filePath = Paths.get(uploadDir, uniqueFileName);

      // Salva o arquivo no diretório
      file.transferTo(filePath.toFile());

      return uniqueFileName; // Retorna o nome do arquivo salvo
    } catch (IOException e) {
      throw new RuntimeException("Erro ao salvar o arquivo", e);
    }
  }

  public void deletePhoto(String fileName) {
    if (fileName == null || fileName.isEmpty()) {
      throw new IllegalArgumentException("The file name cannot be null or empty");
    }

    Path filePath = Paths.get(uploadDir, fileName);

    try {
      Files.deleteIfExists(filePath);
    } catch (IOException e) {
      throw new RuntimeException("Error deleting file: " + fileName, e);
    }
  }

  public String getFileExtension(String fileName) {
    if (fileName == null || !fileName.contains(".")) {
      return ""; // Sem extensão
    }
    return fileName.substring(fileName.lastIndexOf("."));
  }

  public byte[] downloadPhoto(String fileName) {
    if (fileName == null || fileName.isEmpty()) {
      throw new IllegalArgumentException("The file name cannot be null or empty");
    }

    Path filePath = Paths.get(uploadDir, fileName);

    try {
      if (!Files.exists(filePath)) {
        throw new IllegalArgumentException("File not found: " + fileName);
      }

      // Lê o conteúdo do arquivo como bytes
      return Files.readAllBytes(filePath);
    } catch (IOException e) {
      throw new RuntimeException("Error reading file: " + fileName, e);
    }
  }
}
