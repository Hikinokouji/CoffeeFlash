package sta.cfbe.service.configService;

import io.minio.*;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import sta.cfbe.entity.exeption.resource.ImageUploadException;
import sta.cfbe.properties.MinioProperties;
import sta.cfbe.repository.admins.ProductRepository;
import sta.cfbe.service.admin.ProductService;

import java.io.InputStream;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class ImageService {
    private final MinioClient minioClient;
    private final MinioProperties minioProperties;
    private final ProductRepository productRepository;

    public String upload(String db, Long productId, MultipartFile file) {
        try {
            createBucket();
        } catch (Exception e) {
            log.error("Upload::Error createBucket", e);
            throw new ImageUploadException("Помилка створення баккету");
        }
        if (file.isEmpty() || file.getOriginalFilename() == null) {
            throw new ImageUploadException("Файл з фото пустий");
        }
        String fileName = generateFileName(file, db, productId);
        try (InputStream inputStream = file.getInputStream()) {
            saveImage(inputStream, fileName);
            return fileName;
        } catch (Exception e) {
            throw new ImageUploadException("Виникла якась помилка");
        }
    }

    @SneakyThrows
    private void createBucket() {
        boolean found = minioClient.bucketExists(BucketExistsArgs.builder()
                .bucket(minioProperties.getBucket())
                .build());
        if (!found) {
            minioClient.makeBucket(MakeBucketArgs.builder()
                    .bucket(minioProperties.getBucket())
                    .build());
        }
    }

    private String generateFileName(MultipartFile file, String db, Long productId) {
        String extension = getExtension(file);
        String fileName = productId + "." + extension;
        String fullName = "/" + db + "/" + fileName;
        productRepository.saveImage(fileName, productId, db);
        return fullName;
    }

    private String getExtension(MultipartFile file) {
        return file.getOriginalFilename()
                .substring(file.getOriginalFilename().lastIndexOf(".")+1);
    }

    @SneakyThrows
    private void saveImage(InputStream inputStream, String fileName) {
        minioClient.putObject(PutObjectArgs.builder()
                .stream(inputStream, inputStream.available(), -1)
                .bucket(minioProperties.getBucket())
                .object(fileName)
                .build());
    }

    //Exception
    public InputStream download(String fileName) throws Exception{
        return minioClient.getObject(GetObjectArgs.builder()
                .bucket(minioProperties.getBucket())
                .object(fileName)
                .build());
    }
}
