package s3bucket.service;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ObjectMetadata;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
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
            log.info("[uploadFileForProfile 시작]" + " userEmail : " + userEmail);

            ObjectMetadata metadata = new ObjectMetadata();

            metadata.setContentType(file.getContentType());
            metadata.setContentLength(file.getSize());

            String fileKey = "profile/" + userEmail;

            amazonS3Client.putObject(bucket, fileKey, file.getInputStream(), metadata);

            log.info("[uploadFileForProfile 완료]" + " userEmail : " + userEmail);
            return amazonS3Client.getUrl(bucket, fileKey).toString();

        } catch (IOException e) {
            e.printStackTrace();
            log.info(e.getMessage());
        }

        return "";
    }

    public String uploadFileForRecipeCookingStep(
            List<MultipartFile> files, String uuidRandom) {

        try {
            log.info("[uploadFileForRecipeCookingStep 시작]" + " UUID : " + uuidRandom);

            ObjectMetadata metadata = new ObjectMetadata();

            StringBuilder stringBuilder = new StringBuilder();

            int count = 1;
            for (MultipartFile file : files) {

                metadata.setContentType(file.getContentType());
                metadata.setContentLength(file.getSize());

                String fileKey = "RecipeCookingStep/" + uuidRandom + "/" + count++;

                amazonS3Client.putObject(bucket, fileKey, file.getInputStream(), metadata);

                stringBuilder.append(amazonS3Client.getUrl(bucket, fileKey).toString())
                        .append("\\");
            }

            log.info("[uploadFileForRecipeCookingStep 완료]" + " UUID : " + uuidRandom);
            return stringBuilder.toString();

        } catch (IOException e) {
            e.printStackTrace();
            log.info(e.getMessage());
        }

        return "";
    }

    public String uploadFileForRecipeThumbnail(
            MultipartFile file, String stringUUID) {

        try {
            log.info("[uploadFileForRecipeThumbnail 시작]" + " UUID : " + stringUUID);

            ObjectMetadata metadata = new ObjectMetadata();

            metadata.setContentType(file.getContentType());
            metadata.setContentLength(file.getSize());

            String fileKey = "thumbnail/" + stringUUID;

            amazonS3Client.putObject(bucket, fileKey, file.getInputStream(), metadata);

            log.info("[uploadFileForRecipeThumbnail 완료]" + " UUID : " + stringUUID);
            return amazonS3Client.getUrl(bucket, fileKey).toString();

        } catch (IOException e) {
            e.printStackTrace();
            log.info(e.getMessage());
        }

        return "";
    }
    //shareboard 사진 한장 올리기
    public String uploadFileForShareBoard(MultipartFile file, Long shareBoardId) {

        try {
            log.info("[uploadFileForShareBoard 시작]" + " shareBoardId : " + shareBoardId);

            ObjectMetadata metadata = new ObjectMetadata();

            metadata.setContentType(file.getContentType());
            metadata.setContentLength(file.getSize());

            String fileKey = "shareBoard/" + shareBoardId;

            amazonS3Client.putObject(bucket, fileKey, file.getInputStream(), metadata);

            log.info("[uploadFileForShareBoard 완료]" + " shareBoardId : " + shareBoardId);
            return amazonS3Client.getUrl(bucket, fileKey).toString();

        } catch (IOException e) {
            e.printStackTrace();
            log.info(e.getMessage());
        }

        return "";
    }

    //shareboard 사진 여러장 올리기
    public String uploadFilesForShareBoard(
            List<MultipartFile> files, Long shareBoardId) {

        try {
            log.info("[uploadFileForShareBoard 시작]" + " shareBoardId : " + shareBoardId);

            ObjectMetadata metadata = new ObjectMetadata();

            StringBuilder stringBuilder = new StringBuilder();

            int count = 1;
            for (MultipartFile file : files) {
                metadata.setContentType(file.getContentType());
                metadata.setContentLength(file.getSize());

                String fileKey = "ShareBoard/" + shareBoardId + "/" + count++;

                amazonS3Client.putObject(bucket, fileKey, file.getInputStream(), metadata);

                stringBuilder.append(amazonS3Client.getUrl(bucket, fileKey).toString())
                        .append("\\");
            }

            log.info("[uploadFileForShareBoard 완료]" + " shareBoardId : " + shareBoardId);
            return stringBuilder.toString();

        } catch (IOException e) {
            e.printStackTrace();
            log.info(e.getMessage());
        }

        return "";
    }

    public String uploadFileForNotice(
            List<MultipartFile> files, Long noticeId) {

        try {
            log.info("[uploadFileForNotice 시작]" + " noticeId : " + noticeId);

            ObjectMetadata metadata = new ObjectMetadata();

            StringBuilder stringBuilder = new StringBuilder();

            int count = 1;
            for (MultipartFile file : files) {
                metadata.setContentType(file.getContentType());
                metadata.setContentLength(file.getSize());

                String fileKey = "Notice/" + noticeId + "/" + count++;

                amazonS3Client.putObject(bucket, fileKey, file.getInputStream(), metadata);

                stringBuilder.append(amazonS3Client.getUrl(bucket, fileKey).toString())
                        .append("\\");
            }

            log.info("[uploadFileForNotice 완료]" + " noticeId : " + noticeId);
            return stringBuilder.toString();

        } catch (IOException e) {
            e.printStackTrace();
            log.info(e.getMessage());
        }

        return "";
    }

    public void updateFile(MultipartFile file, String fileUrl) {

        try {
            log.info("[updateFile 시작]" + " fileUrl : " + fileUrl);

            ObjectMetadata metadata = new ObjectMetadata();

            metadata.setContentType(file.getContentType());
            metadata.setContentLength(file.getSize());

            // Url에서 Key부분만 추출
            String fileKey = fileUrl.substring(55);

            amazonS3Client.putObject(bucket, fileKey, file.getInputStream(), metadata);

            log.info("[updateFile 완료]" + " fileUrl : " + fileUrl);
        } catch (IOException e) {
            e.printStackTrace();
            log.info(e.getMessage());
        }

    }

    public void deleteFile(String fileUrl) {

        log.info("[deleteFile 시작]" + " fileUrl : " + fileUrl);

        // Url에서 Key부분만 추출
        String fileKey = fileUrl.substring(55);

        amazonS3Client.deleteObject(bucket, fileKey);

        log.info("[deleteFile 완료]" + " fileUrl : " + fileUrl);
    }

}
