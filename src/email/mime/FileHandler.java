package email.mime;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.io.File;
import java.io.IOException;
import java.nio.file.StandardCopyOption;

public class FileHandler {

    // 파일을 첨부하는 메소드
    // filePath로 지정된 파일을 첨부할 수 있는지 확인 후 처리
    public void attachFile(String filePath) {
        File file = new File(filePath);
        if (file.exists()) {
            if (isValidFileType(file)) {
                // 유효한 파일이면 첨부
            } else {
                System.out.println("유효하지가 않잖아."); // 오류 메시지 출력
            }
        } else {
            System.out.println("파일이 없잖아."); // 파일이 없는 경우 메시지 출력
        }
    }

    // 파일의 MIME 타입을 반환하는 메소드
    // 파일 경로를 입력받아 해당 파일의 MIME 타입을 반환
    // javax.activation으로도 구현하는게 있긴한데 이게 기본 jdk라이브러리에 빠져있어서 별도의 라이브러리로 추가해야함.
    // 장단점 비교 mime파트랑 논의 필요할듯
    public String getMimeType(String filePath) {
        Path path = Paths.get(filePath);
        try {
            return Files.probeContentType(path); // MIME 타입 탐지
        } catch (IOException e) {
            e.printStackTrace(); // 오류 발생 시 출력
            return "이상한 타입인데요"; // 알 수 없는 경우 처리
        }
    }

    // 파일 타입 유효성 확인 메소드
    // 이미지, PDF, 텍스트 파일이 유효한 파일 타입으로 간주됨
    public boolean isValidFileType(File file) {
        String mimeType = getMimeType(String.valueOf(file)); // MIME 타입 가져오기
        boolean isValid = false;
        if(mimeType.startsWith("image")) {
            isValid = true;
        } // image만 일단 했는데 뭐 파일 형식 여러개 해야겠쥬
        return isValid;
    }

    // 파일 크기 확인 메소드
    // 주어진 파일 경로에 대한 파일 크기를 반환
    public long getFileSize(String filePath) {
        File file = new File(filePath);
        if (file.exists()) {
            return file.length(); // 파일 크기 반환
        } else {
            System.out.println("파일이 없잖아요"); // 파일을 찾을 수 없는 경우
            return 0;
        }
    }

    // 파일 첨부 후 파일 목록을 관리하는 메소드
    public void manageAttachedFiles() {
        // 리스트에 파일을 추가하거나, 삭제하거나, 출력하는 기능 포함 예정
        // 한 메소드에 넣지 말고 나눌까? 좀 길어질거 같긴 함니다
    }
    // 파일을 로컬 디스크에 저장하는 메소드
    public void saveFileToDisk(File file, String destinationDirectory) throws IOException {
        // 파일이 유효한지 확인 후 저장 예정
        Path destinationPath = Path.of(destinationDirectory, file.getName());
        Files.copy(file.toPath(), destinationPath, StandardCopyOption.REPLACE_EXISTING);
    }
}