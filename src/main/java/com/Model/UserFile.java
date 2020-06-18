package com.Model;

import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.*;

@Entity
public class UserFile {

    @Id
    @GeneratedValue (strategy = GenerationType.AUTO)
    private long id;

    private String fileName;

    private String fileType;

    @Lob
    private byte[] data;

    @Autowired
    public UserFile (){
        this.fileName = "unknown";
        this.fileType = "";
        this.data = new byte[0];
    }

    public UserFile (String fileName, String fileType, byte[] data){
        this.fileName = fileName;
        this.fileType = fileType;
        this.data = data;
    }

    public String getFileName() {
        return this.fileName;
    }

    public String getFileType (){
        return this.fileType;
    }

    public void setData (byte[] data){
        this.data = data;
    }

    public byte[] getData (){
        return this.data;
    }

    public long getId (){
        return this.id;
    }
}
