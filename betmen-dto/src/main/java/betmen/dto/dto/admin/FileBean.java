package betmen.dto.dto.admin;

import org.springframework.web.multipart.MultipartFile;

public class FileBean {

    private MultipartFile multipartFile;

    public MultipartFile getMultipartFile() {
        return multipartFile;
    }

    public void setMultipartFile(MultipartFile multipartFile) {
        this.multipartFile = multipartFile;
    }
}
