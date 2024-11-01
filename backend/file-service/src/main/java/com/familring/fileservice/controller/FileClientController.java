package com.familring.fileservice.controller;

import com.familring.common_service.dto.BaseResponse;
import com.familring.fileservice.exception.base.ApplicationException;
import com.familring.fileservice.service.FileService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/client/files")
@RequiredArgsConstructor
@Log4j2
public class FileClientController {

    private final FileService fileService;

    @Operation(
            summary = "파일 업로드",
            description = "하나 또는 여러 개의 파일을 S3에 업로드합니다. 폴더 경로를 지정하면 해당 경로에 파일이 저장됩니다."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "파일 업로드 성공",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(type = "array", implementation = String.class),
                            examples = @ExampleObject(
                                    value = """
                                            [
                                                "https://bucket-name.s3.region.amazonaws.com/users/123/profile/image1.jpg",
                                                "https://bucket-name.s3.region.amazonaws.com/users/123/profile/image2.jpg"
                                            ]
                                            """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "잘못된 요청",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ApplicationException.class),
                            examples = @ExampleObject(
                                    value = """
                                            {
                                                "status": "BAD_REQUEST",
                                                "message": "업로드할 파일이 없습니다."
                                            }
                                            """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "서버 오류",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ApplicationException.class),
                            examples = @ExampleObject(
                                    value = """
                                            {
                                                "status": "INTERNAL_SERVER_ERROR",
                                                "message": "파일 업로드 중 오류가 발생했습니다."
                                            }
                                            """
                            )
                    )
            )
    })
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<BaseResponse<List<String>>> uploadFiles(
            @Parameter(
                    description = "업로드할 파일들 (여러 파일 선택 가능)",
                    required = true,
                    content = @Content(mediaType = MediaType.MULTIPART_FORM_DATA_VALUE)
            )
            @RequestParam("files") List<MultipartFile> files,

            @Parameter(
                    description = """
                            저장될 S3 폴더 경로
                            - 값이 없으면 버킷 루트에 저장
                            - 경로 구분자는 '/'
                            - 시작과 끝의 '/'는 자동으로 처리됨
                            """,
                    example = "users/123/profile",
                    schema = @Schema(type = "string", example = "users/123/profile")
            )
            @RequestParam(value = "folderPath", required = false) String folderPath
    ) {
        log.info("파일 업로드 요청 - 파일 수: {}, 폴더 경로: {}", files.size(), folderPath);
        List<String> urls = fileService.uploadFiles(files, folderPath);
        log.info("파일 업로드 완료 - 업로드된 파일 수: {}", urls.size());
        return ResponseEntity.ok(BaseResponse.create(HttpStatus.OK.value(), "파일이 성공적으로 업로드되었습니다.", urls));
    }

    @Operation(
            summary = "파일 삭제",
            description = """
                    S3에 저장된 파일들을 삭제합니다.
                    - 파일의 전체 URL을 제공해야 합니다.
                    - 존재하지 않는 파일은 404 오류가 발생합니다.
                    - URL 목록이 비어있으면 400 오류가 발생합니다.
                    """
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "파일 삭제 성공"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "잘못된 요청",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ApplicationException.class),
                            examples = @ExampleObject(
                                    value = """
                                            {
                                                "status": "BAD_REQUEST",
                                                "message": "삭제할 파일 URL이 없습니다."
                                            }
                                            """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "파일을 찾을 수 없음",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ApplicationException.class),
                            examples = @ExampleObject(
                                    value = """
                                            {
                                                "status": "NOT_FOUND",
                                                "message": "존재하지 않는 파일입니다. 파일 키: example.jpg"
                                            }
                                            """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "서버 오류",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ApplicationException.class),
                            examples = @ExampleObject(
                                    value = """
                                            {
                                                "status": "INTERNAL_SERVER_ERROR",
                                                "message": "파일 삭제 중 오류가 발생했습니다."
                                            }
                                            """
                            )
                    )
            )
    })
    @DeleteMapping
    public ResponseEntity<BaseResponse<Void>> deleteFiles(
            @Parameter(
                    description = "삭제할 파일들의 URL 목록",
                    required = true,
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(type = "array", implementation = String.class),
                            examples = @ExampleObject(
                                    value = """
                                            [
                                                "https://bucket-name.s3.region.amazonaws.com/users/123/profile/image1.jpg",
                                                "https://bucket-name.s3.region.amazonaws.com/users/123/profile/image2.jpg"
                                            ]
                                            """
                            )
                    )
            )
            @RequestBody List<String> fileUrls
    ) {
        log.info("파일 삭제 요청 - URL 수: {}", fileUrls.size());
        fileService.deleteFiles(fileUrls);
        log.info("파일 삭제 완료");
        return ResponseEntity.ok(BaseResponse.create(HttpStatus.OK.value(), "파일이 성공적으로 삭제되었습니다."));
    }
}