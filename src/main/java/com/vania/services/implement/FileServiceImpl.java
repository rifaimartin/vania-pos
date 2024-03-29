package com.vania.services.implement;

import com.vania.services.FileService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

@Service
public class FileServiceImpl implements FileService {
    @Override
    public void saveFile(MultipartFile image, String id) throws IOException {
        File file = new File("C:/nginx-1.16.1/html/menu-image/"+id+".jpg");
        file.createNewFile();
        try(FileOutputStream fileOutputStream = new FileOutputStream(file)){
            fileOutputStream.write(image.getBytes());
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }
}
