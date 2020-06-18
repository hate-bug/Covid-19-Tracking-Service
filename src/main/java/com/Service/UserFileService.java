package com.Service;

import com.Model.User;
import com.Model.UserFile;
import com.Repository.UserFileRepository;
import com.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.IOException;

@Service
public class UserFileService {

    @Autowired
    private UserFileRepository userFileRepository;

    @Autowired
    private UserRepository userRepository;

    public UserFile storeFile (MultipartFile file, String userName) throws IOException {
        String fileName = StringUtils.getFilename(file.getOriginalFilename());
        UserFile userFile = new UserFile(fileName, file.getContentType(), file.getBytes());
        userFile = this.userFileRepository.save(userFile);//assign id to userfile
        User user = this.userRepository.findUserByEmailAddressIgnoreCase(userName);
        user.addUserFile(userFile);
        this.userRepository.save(user);
        return userFile;
    }

    public UserFile getFile (long fileid){
        return this.userFileRepository.findAllById(fileid);
    }

    public String generateUrl (UserFile userFile){
        String fileDownloadUrli = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/downloadfile")
                .toUriString();
        fileDownloadUrli = fileDownloadUrli+ "?id=" + userFile.getId();
        return fileDownloadUrli;
    }
}
