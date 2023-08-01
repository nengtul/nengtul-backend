package s3bucket.service;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ObjectMetadata;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class AmazonS3Service {

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    private final AmazonS3Client amazonS3Client;

    public String uploadFileForProfile(MultipartFile file, String userEmail) {

        try {
            ObjectMetadata metadata = new ObjectMetadata();

            metadata.setContentType(file.getContentType());
            metadata.setContentLength(file.getSize());

            String fileKey = "profile/" + userEmail;

            amazonS3Client.putObject(bucket, fileKey, file.getInputStream(), metadata);

            return amazonS3Client.getUrl(bucket, fileKey).toString();

        } catch (IOException e) {
            e.printStackTrace();
        }

        return "";
    }

    public String uploadFileForRecipeCookingStep(
            List<MultipartFile> files, String title) {

        try {

            ObjectMetadata metadata = new ObjectMetadata();

            StringBuilder stringBuilder = new StringBuilder();

            String now = LocalDateTime.now().toString();

            for (MultipartFile file : files) {
                metadata.setContentType(file.getContentType());
                metadata.setContentLength(file.getSize());

                String fileKey = title + "/" + now + "/" + file.getOriginalFilename();

                amazonS3Client.putObject(bucket, fileKey, file.getInputStream(), metadata);

                stringBuilder.append(amazonS3Client.getUrl(bucket, fileKey).toString())
                        .append("\\");
            }


            return stringBuilder.toString();

        } catch (IOException e) {
            e.printStackTrace();
        }

        return "";
    }

}
