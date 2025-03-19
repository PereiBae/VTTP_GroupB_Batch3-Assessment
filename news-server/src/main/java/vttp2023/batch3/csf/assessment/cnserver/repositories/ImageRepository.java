package vttp2023.batch3.csf.assessment.cnserver.repositories;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Repository
public class ImageRepository {
	
	// TODO: Task 1
    @Autowired
    private AmazonS3 s3;

    @Value("${s3.bucket.endpoint}")
    private String endpoint;

    @Value("${s3.bucket}")
    private String bucket;

    public String saveImage(MultipartFile file, String image_id) throws IOException {
        Map<String,String> userData = new HashMap<>();
        userData.put("image_id",image_id);
        userData.put("fileName", file.getOriginalFilename());
        userData.put("fileSize", String.valueOf(file.getSize()));
        userData.put("uploadDateTime", LocalDateTime.now().toString());

        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(file.getSize());
        metadata.setContentType(file.getContentType());
        metadata.setUserMetadata(userData);

        PutObjectRequest putObjectRequest = new PutObjectRequest(
                bucket,
                "picture/%s".formatted(image_id),
                file.getInputStream(),
                metadata);
        putObjectRequest = putObjectRequest.withCannedAcl(CannedAccessControlList.PublicRead);

        s3.putObject(putObjectRequest);

        System.out.printf("https://%s.%s/picture/%s\n".formatted(bucket, endpoint, image_id));

        return "https://%s.%s/picture/%s".formatted(bucket, endpoint, image_id);
    }



}
