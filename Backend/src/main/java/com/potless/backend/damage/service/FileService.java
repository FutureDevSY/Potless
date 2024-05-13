package com.potless.backend.damage.service;

import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

@Log4j2
@Service
public class FileService {

    //  임시 경로
    private final Path currentPath = Paths.get("");
    private final String curPath = currentPath.toAbsolutePath().toString();
    private final String uploadDir = curPath + "/file";

    public File convertAndSaveFile(MultipartFile multipartFile) throws IOException {
        // 파일 저장 디렉토리 생성
        File directory = new File(uploadDir);
        if (!directory.exists()) {
            directory.mkdirs();
        }

        if (!multipartFile.isEmpty()) {
            String originalFileName = multipartFile.getOriginalFilename();
            File file = new File(uploadDir, originalFileName);
            multipartFile.transferTo(file);  // 파일 저장
            return file;  // 저장된 파일 객체 반환
        } else {
            log.info("multipartFile이 비어있습니다. 저장 실패");
        }

        return null;
    }

    public void deleteFile(File file) {
        if (file != null && file.exists()) {
            file.delete();
        } else {
            log.info("파일 삭제에 실패하였습니다.");
        }
    }

}
