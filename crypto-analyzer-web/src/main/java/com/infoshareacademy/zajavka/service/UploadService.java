package com.infoshareacademy.zajavka.service;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ejb.Stateless;
import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.util.List;

@Stateless
public class UploadService {

    public static final int MEMORY_THRESHOLD = 1024 * 1024;
    public static final int MAX_REQUEST_SIZE = 1024 * 1024 * 5 * 5;
    public static final int MAX_FILE_SIZE = 1024 * 1024 * 10;
    public static final String UPLOAD_PATH = System.getProperty("java.io.tmpdir") + "/zajavka-data";
    private static final Logger LOG = LoggerFactory.getLogger(UploadService.class);

    public String readFileFromRequest(HttpServletRequest request) {

        try {
            LOG.info("Upload path: {}", UPLOAD_PATH);
            File uploadDir = new File(UPLOAD_PATH);
            if (!uploadDir.exists()) {
                uploadDir.mkdir();
            }
            if (ServletFileUpload.isMultipartContent(request)) {
                LOG.info("Received correct multipart data");

                DiskFileItemFactory factory = new DiskFileItemFactory();

                factory.setSizeThreshold(MEMORY_THRESHOLD);
                factory.setRepository(new File(System.getProperty("java.io.tmpdir")));
                ServletFileUpload upload = new ServletFileUpload(factory);
                upload.setFileSizeMax(MAX_FILE_SIZE);
                upload.setSizeMax(MAX_REQUEST_SIZE);

                List<FileItem> formItems = upload.parseRequest(request);
                if (formItems != null && formItems.size() > 0) {
                    for (FileItem item : formItems) {
                        if (!item.isFormField()) {
                            String fileName = new File(item.getName()).getName();
                            String filePath = UPLOAD_PATH + File.separator + fileName;
                            if (fileName.endsWith(".zip")) {

                                LOG.info("Saving file '{}' to '{}'", fileName, filePath);

                                File storeFile = new File(filePath);
                                item.write(storeFile);

                                LOG.info("File saved successfully to '{}'", filePath);
                                return filePath;
                            } else {
                                LOG.warn("Incorrect file type: {}");
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            LOG.error("Error while saving file: {}", e.getMessage());
        }
        return null;
    }
}
