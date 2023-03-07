package com.yuri.shoppingsite.service;

import lombok.extern.java.Log;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.util.UUID;

@Service
@Log
public class FileService {

    public String uploadFile(String uploadPath, String originalFileName,
                             byte[] fileData) throws Exception{
//        UUID(Universally Unique Identifier)는 서로 다른 개체들을 구별하기 위해 이름을 부여할 때 사용한다.
        //실제 사용시 중복될 가능성이 거의 없기 때문에 파일의 이름으로 사용하면 파일명 중복 문제를 해결할 수 있다.
        UUID uuid = UUID.randomUUID();
        //확장자명 추출
        String extenstion = originalFileName.substring(originalFileName.lastIndexOf("."));
        //UUID로 받은 값과 원래 파일의 이름의 확장자를 조합해서 저장될 파일 이름을 만든다.
        String savedFileName = uuid.toString() + extenstion;
        String fileUploadFullUrl = uploadPath + "/" + savedFileName;
        //FileOutputStream 클래스는 바이트 단위의 출력을 내보내는 클래스이다.
        //생성자로 파일이 저장될 위치와 파일의 이름을 넘겨 파일에 쓸 파일 출력 스트림을 만든다
        FileOutputStream fos = new FileOutputStream(fileUploadFullUrl);
        //fileData를 파일 출력 스트림에 입력한다
        fos.write(fileData);
        fos.close();
        //업로드된 파일의 이름을 반환한다.
        return savedFileName;
    }

    public void deleteFile(String filePath) throws Exception {
        //파일이 저장된 경로를 이용하여 파일 객체를 생성한다.
        File deleteFile = new File(filePath);
        //해당 파일이 존재하면 파일을 삭제한다.
        if(deleteFile.exists()) {
            deleteFile.delete();
            log.info("파일을 삭제하였습니다.");
        } else {
            log.info("파일이 존재하지 않습니다.");
        }
        }

    }

