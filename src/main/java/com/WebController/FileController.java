package com.WebController;

import com.Model.UserFile;
import com.Repository.UserFileRepository;
import com.Repository.UserRepository;
import com.Service.UserFileService;
import com.payload.FileResponse;
import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import java.io.IOException;
import java.nio.file.attribute.UserPrincipalNotFoundException;
import java.security.Principal;
import java.util.Optional;

@RestController
public class FileController {

    @Autowired
    private UserFileRepository userFileRepository;

    @Autowired
    private UserFileService userFileService;

    @Autowired
    private UserRepository userRepository;

    @PostMapping (value = "/uploadfile", headers=("content-type=multipart/*"))
    public FileResponse uploadFile (@RequestParam("file") MultipartFile uploadFile, Principal principal) throws IOException {
        if (principal == null){ // if not a logged user, return null.
            return null;
        }
        UserFile userFile = this.userFileService.storeFile(uploadFile, principal.getName());
        String fileDownloadUrli = this.userFileService.generateUrl(userFile);
        return new FileResponse(userFile.getFileName(), fileDownloadUrli, uploadFile.getContentType(), uploadFile.getSize());
    }

    @GetMapping (value="/downloadfile")
    public ResponseEntity<Resource> downloadFile (@RequestParam("id")long fileId){
        UserFile userFile = this.userFileService.getFile(fileId);
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(userFile.getFileType()))
                .body(new ByteArrayResource(userFile.getData()));
    }

    @GetMapping (value = "/useruploadedfile")
    public FileResponse userUploadedFile (Principal principal) throws UserPrincipalNotFoundException, NotFoundException {
        if (principal==null){
            throw new UserPrincipalNotFoundException("Please login");
        }else {
            Optional<UserFile> optionalUserFileserFile = this.userRepository.findUserByEmailAddressIgnoreCase(principal.getName()).getUserFile();
            if (optionalUserFileserFile.isPresent()){
                UserFile userFile = optionalUserFileserFile.get();
                String fileDownloadUrli = this.userFileService.generateUrl(userFile);
                return new FileResponse(userFile.getFileName(), fileDownloadUrli);
            }else{
                return null;
            }
        }

    }

}
