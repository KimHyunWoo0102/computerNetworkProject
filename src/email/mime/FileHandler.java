package email.mime;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
public class FileHandler {

    public void attachFile(String filePath) {
        File file = new File(filePath);
        if (file.exists()) {
            if (isValidFileType(file)) {
                System.out.println(file.getName() + " 파일이 첨부되었습니다.");
            } else {
                System.out.println("유효하지 않은 파일입니다.");
            }
        } else {
            System.out.println("파일이 존재하지 않습니다.");
        }
    }

    public String getMimeType(String filePath) {
        Path path = Paths.get(filePath);
        try {
            return Files.probeContentType(path);
        } catch (IOException e) {
            System.err.println("MIME 타입 탐지 오류: " + e.getMessage());
            return "알 수 없는 MIME 타입";
        }
    }

    public boolean isValidFileType(File file) {
        String mimeType = getMimeType(file.getAbsolutePath());
        return mimeType.startsWith("image") || mimeType.equals("application/pdf") || mimeType.startsWith("text");
    }

    public long getFileSize(String filePath) {
        File file = new File(filePath);
        if (file.exists()) {
            return file.length();
        } else {
            System.out.println("파일을 찾을 수 없습니다.");
            return 0;
        }
    }

    public void saveFileToDisk(File file, String destinationDirectory) {
        Path destinationPath = Paths.get(destinationDirectory, file.getName());
        try {
            Files.copy(file.toPath(), destinationPath, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            System.err.println("파일 저장 오류: " + e.getMessage());
        }
    }

    public String readFile(String filePath) {
        StringBuilder contentBuilder = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                contentBuilder.append(line).append(System.lineSeparator());
            }
        } catch (IOException e) {
            System.err.println("파일 읽기 오류: " + e.getMessage());
        }
        return contentBuilder.toString();
    }
}