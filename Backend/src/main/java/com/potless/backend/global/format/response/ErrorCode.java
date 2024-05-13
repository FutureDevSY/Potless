package com.potless.backend.global.format.response;

import com.potless.backend.global.exception.hexagon.HexagonNotFoundException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

    // 글로벌 예외 처리
    GLOBAL_UNEXPECTED_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "처리 중 예기치 않은 서버 오류가 발생했습니다."),

    // 요청 관련 예외 처리
    REQUEST_METHOD_NOT_SUPPORTED(HttpStatus.METHOD_NOT_ALLOWED, "지원하지 않는 HTTP 메서드입니다."),
    MEDIA_TYPE_NOT_SUPPORTED(HttpStatus.UNSUPPORTED_MEDIA_TYPE, "지원하지 않는 미디어 타입입니다."),
    MISSING_SERVLET_REQUEST_PARAMETER(HttpStatus.BAD_REQUEST, "필수 요청 파라미터가 누락되었습니다."),
    SERVLET_REQUEST_BINDING_EXCEPTION(HttpStatus.BAD_REQUEST, "요청 바인딩 오류가 발생했습니다."),
    CONVERSION_NOT_SUPPORTED(HttpStatus.INTERNAL_SERVER_ERROR, "지원되지 않는 변환 유형입니다."),
    TYPE_MISMATCH(HttpStatus.BAD_REQUEST, "타입 불일치 오류가 발생했습니다."),
    MESSAGE_NOT_READABLE(HttpStatus.BAD_REQUEST, "메시지를 읽을 수 없습니다."),
    MESSAGE_NOT_WRITABLE(HttpStatus.INTERNAL_SERVER_ERROR, "메시지를 쓸 수 없습니다."),
    METHOD_ARGUMENT_NOT_VALID(HttpStatus.BAD_REQUEST, "메서드 인자가 유효하지 않습니다."),
    ACCESS_DENIED(HttpStatus.FORBIDDEN, "접근이 거부되었습니다."),
    AUTHENTICATION_FAILED(HttpStatus.UNAUTHORIZED, "인증에 실패했습니다."),
    MISSING_PATH_VARIABLE(HttpStatus.BAD_REQUEST, "PathVariable 파라미터가 요청에 포함되지 않았습니다."),

    ACCESS_TOKEN_NOT_FOUND(HttpStatus.UNAUTHORIZED, "액세스 토큰을 찾을 수 없습니다."),
    REFRESH_TOKEN_NOT_FOUND(HttpStatus.UNAUTHORIZED, "새로운 로그인이 필요합니다. 재로그인을 진행해주세요."),
    EXPIRED_TOKEN(HttpStatus.UNAUTHORIZED, "토큰이 존재하지 않습니다."),
    INVALID_TOKEN_TYPE(HttpStatus.UNAUTHORIZED, "로그인 정보 형식이 올바르지 않습니다."),
    MODIFIED_TOKEN_DETECTED(HttpStatus.UNAUTHORIZED, "로그인 정보가 변경되었습니다."),
    INVALID_TOKEN_FORMAT(HttpStatus.UNAUTHORIZED, "토큰 형식이 유효하지 않습니다."),
    UNAUTHORIZED_ACCESS(HttpStatus.UNAUTHORIZED, "서비스 이용을 위해 로그인이 필요합니다."),

    // 회원 관련 예외 처리
    SIGNUP_FAILED(HttpStatus.BAD_REQUEST, "회원 가입에 실패했습니다."),
    LOGIN_FAILED(HttpStatus.UNAUTHORIZED, "이메일 혹은 패스워드 정보가 정확하지 않습니다."),
    PASSWORD_MISMATCH(HttpStatus.BAD_REQUEST, "비밀번호가 일치하지 않습니다."),

    EMAIL_DUPLICATED(HttpStatus.CONFLICT, "이미 사용 중인 이메일입니다."),
    EMAIL_NOT_FOUND(HttpStatus.BAD_REQUEST, "존재하지 않는 이메일입니다."),
    INVALID_NICKNAME(HttpStatus.BAD_REQUEST, "유효하지 않은 닉네임입니다."),
    EMAIL_SEND_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "인증 이메일 전송에 실패했습니다."),
    AUTH_CODE_INVALID(HttpStatus.BAD_REQUEST, "인증 코드가 유효하지 않습니다."),

    PROFILE_UPDATE_FAILED(HttpStatus.BAD_REQUEST, "회원정보를 수정할 수 없습니다."),
    MEMBER_NOT_FOUND(HttpStatus.UNAUTHORIZED, "관련 회원 정보를 찾을 수 없습니다."),
    MANAGER_NOT_FOUND(HttpStatus.UNAUTHORIZED, "관련 매니저 정보를 찾을 수 없습니다."),
    LOGIN_AUTH_UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "잘못된 권한을 가진 계정의 로그인 시도입니다."),
    UNAUTHORIZED_ACCESS_TO_SERVICE(HttpStatus.UNAUTHORIZED, "잘못된 권한을 가진 계정의 요청 시도입니다. 권한을 확인해주세요."),

    // 포트홀 관련 예외 처리
    POTHOLE_NOT_FOUND(HttpStatus.UNAUTHORIZED, "관련 포트홀 정보를 찾을 수 없습니다."),
    POTHOLE_LOCATION_NOT_FOUND(HttpStatus.UNAUTHORIZED, "동 정보를 찾을 수 없습니다."),
    POTHOLE_AREA_NOT_FOUND(HttpStatus.UNAUTHORIZED, "지역 정보를 찾을 수 없습니다."),
    POTHOLE_MINUS_NOT_FOUND(HttpStatus.UNAUTHORIZED, "삭제할 포트홀 정보를 찾을 수 없습니다."),
    AREA_NOT_FOUND(HttpStatus.UNAUTHORIZED, "지역 정보를 찾을 수 없습니다."),
    LOCATION_NOT_FOUND(HttpStatus.UNAUTHORIZED, "동 정보를 찾을 수 없습니다."),
    POTHOLE_DETECTION_FAILED(HttpStatus.METHOD_NOT_ALLOWED, "포트홀 2차 탐지 결과 탐지하지 못했습니다."),
    INVALID_COORDINATE_RANGE(HttpStatus.BAD_REQUEST, "x또는 y 좌표값의 범위가 올바르지 않습니다."),

    // 프로젝트 관련 예외 처리
    PROJECT_NOT_FOUND(HttpStatus.UNAUTHORIZED, "관련 프로젝트 정보를 찾을 수 없습니다."),
    TEAM_NOT_FOUND(HttpStatus.UNAUTHORIZED, "관련 팀 정보를 찾을 수 없습니다."),
    PROJECT_AREA_NOT_FOUND(HttpStatus.UNAUTHORIZED, "관련 지역 정보를 찾을 수 없습니다."),

    // 작업 관련 예외 처리
    TASK_NOT_FOUND(HttpStatus.UNAUTHORIZED, "작업 정보를 찾을 수 없습니다."),
    CREATE_TEAM_FAILED(HttpStatus.UNAUTHORIZED, "관리자 본인 해당 지역구의 팀만 생성할 수 있습니다."),

    // AWS ERROR
    FAILED_TO_UPLOAD(HttpStatus.INTERNAL_SERVER_ERROR, "S3 이미지 저장에 실패했습니다."),

    // Hexagon 예외 처리
    HEXAGON_NOT_FOUND(HttpStatus.UNAUTHORIZED,"Hexagon 정보를 찾을 수 없습니다.");

    private final HttpStatus status;
    private final String message;

}


