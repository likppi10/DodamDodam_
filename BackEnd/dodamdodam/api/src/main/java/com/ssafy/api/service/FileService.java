package com.ssafy.api.service;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.*;
import com.drew.imaging.ImageMetadataReader;
import com.drew.metadata.Directory;
import com.drew.metadata.Metadata;
import com.drew.metadata.exif.ExifIFD0Directory;
import com.ssafy.core.common.FileUtil;
import com.ssafy.core.entity.Family;
import com.ssafy.core.entity.Picture;
import com.ssafy.core.entity.Profile;
import com.ssafy.core.exception.ErrorCode;
import com.ssafy.core.exception.CustomException;
import com.ssafy.core.repository.FamilyRepository;
import com.ssafy.core.repository.PictureRepository;
import com.ssafy.core.repository.ProfileRepository;
import lombok.RequiredArgsConstructor;
import marvin.image.MarvinImage;
import org.imgscalr.Scalr;
import org.marvinproject.image.transform.scale.Scale;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Objects;


@RequiredArgsConstructor
@Service
public class FileService {

    private final AmazonS3Client amazonS3Client;
    private final PictureRepository pictureRepository;
    private final ProfileRepository profileRepository;

    private final FamilyRepository familyRepository;

    @Value("${cloud.aws.s3.bucket}")
    private String bucketName;

    public String uploadFileV1(String category, MultipartFile multipartFile) {
        validateFileExists(multipartFile);

        String fileName = FileUtil.buildFileName(category, multipartFile.getOriginalFilename());

        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentType(multipartFile.getContentType());
        objectMetadata.setContentLength(multipartFile.getSize());
        String url;

        try (InputStream inputStream = multipartFile.getInputStream()) {
            amazonS3Client.putObject(new PutObjectRequest(bucketName, fileName, inputStream, objectMetadata)
                    .withCannedAcl(CannedAccessControlList.PublicRead));
            url = amazonS3Client.getUrl(bucketName, fileName).toString();
        } catch (IOException e) {
            throw new CustomException(ErrorCode.FILE_SIZE_EXCEED);
        }finally{
            amazonS3Client.shutdown();
        }

        return url;
    }
    public String uploadFileV2(String category, MultipartFile multipartFile) {

        validateFileExists(multipartFile);

        String fileName = FileUtil.buildFileName(category, multipartFile.getOriginalFilename());
        String refileName = fileName.substring(category.length()+1);

        checkFileNameExtension(multipartFile);

        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentType(multipartFile.getContentType());
        objectMetadata.setContentLength(multipartFile.getSize());
        String url;

        try (InputStream inputStream = multipartFile.getInputStream()) {
            amazonS3Client.putObject(new PutObjectRequest(bucketName, refileName, inputStream, objectMetadata)
                    .withCannedAcl(CannedAccessControlList.PublicRead));
            url = amazonS3Client.getUrl(bucketName, fileName).toString();
        } catch (IOException e) {
            throw new CustomException(ErrorCode.FILE_SIZE_EXCEED);
        }finally{
            amazonS3Client.shutdown();
        }

        return url;
    }

    private void validateFileExists(MultipartFile multipartFile) {
        if (multipartFile.isEmpty()) {
            throw new CustomException(ErrorCode.FILE_DOES_NOT_EXIST);
        }
    }

    private void checkFileNameExtension(MultipartFile multipartFile) {
        String originFilename = Objects.requireNonNull(multipartFile.getOriginalFilename()).replaceAll(" ", "");
        String formatName = originFilename.substring(originFilename.lastIndexOf(".") + 1).toLowerCase();
        String[] supportFormat = { "bmp", "jpg", "jpeg", "png" };
        if (!Arrays.asList(supportFormat).contains(formatName)) {
            throw new CustomException(ErrorCode.WRONG_FILE_EXTENSION);
        }
    }

    @Async
    public void resizeImage(String category, MultipartFile file, Picture picture) {
        if (file.getSize() > 1048576) {
            try {
                String filePath = resizeFile(category,file);
                picture.updatePathName(filePath);
                pictureRepository.save(picture);

            } catch (Exception e) {
                throw new CustomException(ErrorCode.INVALID_REQUEST);
            }
        }
    }

    @Async
    public void resizeImage(String category, MultipartFile file, Family family) {
        if (file.getSize() > 1048576) {
            try {
                String filePath = resizeFile(category,file);
                family.setPicture(filePath);
                familyRepository.save(family);

            } catch (Exception e) {
                throw new CustomException(ErrorCode.INVALID_REQUEST);
            }
        }
    }
    @Async
    public void resizeImage(String category, MultipartFile file, Profile profile) {
        if (file.getSize() > 1048576) {
            try {
                String filePath = resizeFile(category,file);
                profile.updateImagePath(filePath);
                profileRepository.save(profile);

            } catch (Exception e) {
                throw new CustomException(ErrorCode.INVALID_REQUEST);
            }
        }
    }

    public String resizeFile(String category, MultipartFile multipartFile){

        try {
            InputStream fileInputStream = multipartFile.getInputStream();

            String fileName = FileUtil.buildFileName(category, multipartFile.getOriginalFilename());
            String fileFormatName = multipartFile.getContentType().substring(multipartFile.getContentType().lastIndexOf("/") + 1);

            System.out.println("여기는 통과 =========");
            int orientation = 1; // 회전정보, 1. 0도, 3. 180도, 6. 270도, 8. 90도 회전한 정보

            Metadata metadata; // 이미지 메타 데이터 객체
            Directory directory; // 이미지의 Exif 데이터를 읽기 위한 객체

            try {
                metadata = ImageMetadataReader.readMetadata(fileInputStream);

                System.out.println("여기서 설마???");
                directory = metadata.getFirstDirectoryOfType(ExifIFD0Directory.class);
                if(directory != null){
                    orientation = directory.getInt(ExifIFD0Directory.TAG_ORIENTATION); // 회전정보
                }
            }catch (Exception e) {
                orientation = 1;
            }

            System.out.println("너무 많이 읽었나??");
            //imageFile
            BufferedImage inputImage = ImageIO.read(fileInputStream);
            System.out.println("또 읽으려니까 안되나?");

            // 회전 시킨다.
            if(orientation == 3){
                System.out.println("3333333");
                inputImage = Scalr.rotate(inputImage, Scalr.Rotation.CW_180, null);
            }else if(orientation == 6){
                System.out.println("6666666");
                inputImage = Scalr.rotate(inputImage, Scalr.Rotation.CW_90, null);
            }else if(orientation == 8){
                System.out.println("8888888");
                inputImage = Scalr.rotate(inputImage, Scalr.Rotation.CW_270, null);
            }

            System.out.println("회전도 잘됨!!");
            System.out.println("회전도 잘됨!!");
            System.out.println("회전도 잘됨!!");
            System.out.println("회전도 잘됨!!");

            int originWidth = inputImage.getWidth();
            int originHeight = inputImage.getHeight();

            MarvinImage imageMarvin = new MarvinImage(inputImage);

            Scale scale = new Scale();
            scale.load();
            scale.setAttribute("newWidth", 712);
            scale.setAttribute("newHeight", 712 * originHeight / originWidth);
            scale.process(imageMarvin.clone(), imageMarvin, null, null, false);

            BufferedImage imageNoAlpha = imageMarvin.getBufferedImageNoAlpha();

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(imageNoAlpha, fileFormatName, baos);
            baos.flush();
            MultipartFile resizedFile = new MockMultipartFile(fileName, fileName, "image/" + fileFormatName, baos.toByteArray());
            String filePath = uploadFileV2(category, resizedFile);

            System.out.println("리사이징 완료");
            System.out.println("리사이징 완료");
            System.out.println("리사이징 완료");
            System.out.println("리사이징 완료");
            return filePath;
        }
        catch (Exception e) {
            throw new CustomException(ErrorCode.INVALID_REQUEST);
        }
    }
}