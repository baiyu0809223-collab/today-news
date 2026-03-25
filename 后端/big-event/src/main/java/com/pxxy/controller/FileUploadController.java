package com.pxxy.controller;

import com.pxxy.pojo.Result;
import com.pxxy.pojo.Video;
import com.pxxy.utils.AliOssUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@Tag(name = "文件上传", description = "图片、视频文件上传接口")
@RestController
public class FileUploadController {

    // =========================
    // 图片上传
    // =========================
    @Operation(
            summary = "图片上传",
            description = "上传图片文件到阿里云 OSS，返回图片访问 URL"
    )
    @PostMapping("/upload")
    public Result<String> upload(
            @Parameter(
                    description = "图片文件",
                    required = true,
                    content = @Content(
                            mediaType = "multipart/form-data",
                            schema = @Schema(type = "string", format = "binary")
                    )
            )
            MultipartFile file) throws Exception {

        String originalFilename = file.getOriginalFilename();
        String filename = UUID.randomUUID().toString()
                + originalFilename.substring(originalFilename.lastIndexOf("."));

        String url = AliOssUtil.uploadFile(filename, file.getInputStream());
        return Result.success(url);
    }

    // =========================
    // 视频上传
    // =========================
    @Operation(
            summary = "视频上传",
            description = "上传视频文件到阿里云 OSS，返回视频播放信息"
    )
    @PostMapping("/uploadVideo")
    public Result<Video> uploadVideo(
            @Parameter(
                    description = "视频文件",
                    required = true,
                    content = @Content(
                            mediaType = "multipart/form-data",
                            schema = @Schema(type = "string", format = "binary")
                    )
            )
            MultipartFile file) throws Exception {

        String originalFilename = file.getOriginalFilename();
        String filename = UUID.randomUUID().toString()
                + originalFilename.substring(originalFilename.lastIndexOf("."));

        String url = AliOssUtil.uploadFile(filename, file.getInputStream());
        String poster = "src/main/resources/files/img.png";

        Video v = new Video();
        v.setSrc(url);
        v.setPoster(poster);

        return Result.success(v);
    }
}