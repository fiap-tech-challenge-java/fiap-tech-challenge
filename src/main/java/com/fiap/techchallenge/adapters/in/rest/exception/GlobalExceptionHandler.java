package com.fiap.techchallenge.adapters.in.rest.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;

import jakarta.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiErrorResponse> handleValidationExceptions(MethodArgumentNotValidException ex,
            HttpServletRequest request) {

        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });

        ApiErrorResponse errorResponse = new ApiErrorResponse("Erro de validação dos dados", "VALIDATION_ERROR",
                HttpStatus.BAD_REQUEST.value(), request.getRequestURI());
        errorResponse.setFieldErrors(errors);

        logger.warn("Erro de validação: {}", errors);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiErrorResponse> handleResourceNotFoundException(ResourceNotFoundException ex,
            HttpServletRequest request) {

        ApiErrorResponse errorResponse = new ApiErrorResponse(ex.getMessage(), "RESOURCE_NOT_FOUND",
                HttpStatus.NOT_FOUND.value(), request.getRequestURI());

        logger.warn("Recurso não encontrado: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ApiErrorResponse> handleBusinessException(BusinessException ex, HttpServletRequest request) {

        ApiErrorResponse errorResponse = new ApiErrorResponse(ex.getMessage(), ex.getClass().getSimpleName(),
                HttpStatus.BAD_REQUEST.value(), request.getRequestURI());

        logger.warn("Erro de negócio: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<ApiErrorResponse> handleUnauthorizedException(UnauthorizedException ex,
            HttpServletRequest request) {

        ApiErrorResponse errorResponse = new ApiErrorResponse(ex.getMessage(), "UNAUTHORIZED",
                HttpStatus.UNAUTHORIZED.value(), request.getRequestURI());

        logger.warn("Acesso não autorizado: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ApiErrorResponse> handleAccessDeniedException(AccessDeniedException ex,
            HttpServletRequest request) {

        ApiErrorResponse errorResponse = new ApiErrorResponse(
                "Acesso negado. Você não tem permissão para acessar este recurso.", "ACCESS_DENIED",
                HttpStatus.FORBIDDEN.value(), request.getRequestURI());

        logger.warn("Acesso negado para {}: {}", request.getRequestURI(), ex.getMessage());
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(errorResponse);
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ApiErrorResponse> handleAuthenticationException(AuthenticationException ex,
            HttpServletRequest request) {

        ApiErrorResponse errorResponse = new ApiErrorResponse("Falha na autenticação: " + ex.getMessage(),
                "AUTHENTICATION_FAILED", HttpStatus.UNAUTHORIZED.value(), request.getRequestURI());

        logger.warn("Falha na autenticação: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ApiErrorResponse> handleBadCredentialsException(BadCredentialsException ex,
            HttpServletRequest request) {

        ApiErrorResponse errorResponse = new ApiErrorResponse("Credenciais inválidas", "INVALID_CREDENTIALS",
                HttpStatus.UNAUTHORIZED.value(), request.getRequestURI());

        logger.warn("Credenciais inválidas para {}", request.getRequestURI());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiErrorResponse> handleHttpMessageNotReadableException(HttpMessageNotReadableException ex,
            HttpServletRequest request) {

        ApiErrorResponse errorResponse = new ApiErrorResponse("Requisição inválida. Verifique o formato dos dados enviados.", "INVALID_REQUEST_BODY",
                HttpStatus.BAD_REQUEST.value(), request.getRequestURI());

        logger.warn("Erro na leitura do corpo da requisição: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ApiErrorResponse> handleMethodArgumentTypeMismatchException(
            MethodArgumentTypeMismatchException ex, HttpServletRequest request) {

        String message = String.format("O parâmetro '%s' deve ser do tipo %s", ex.getName(),
                ex.getRequiredType().getSimpleName());

        ApiErrorResponse errorResponse = new ApiErrorResponse(message, "INVALID_PARAMETER_TYPE",
                HttpStatus.BAD_REQUEST.value(), request.getRequestURI());

        logger.warn("Tipo de parâmetro inválido: {}", message);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<ApiErrorResponse> handleMissingServletRequestParameterException(
            MissingServletRequestParameterException ex, HttpServletRequest request) {

        String message = String.format("Parâmetro obrigatório '%s' não foi fornecido", ex.getParameterName());

        ApiErrorResponse errorResponse = new ApiErrorResponse(message, "MISSING_PARAMETER",
                HttpStatus.BAD_REQUEST.value(), request.getRequestURI());

        logger.warn("Parâmetro obrigatório ausente: {}", ex.getParameterName());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ApiErrorResponse> handleHttpRequestMethodNotSupportedException(
            HttpRequestMethodNotSupportedException ex, HttpServletRequest request) {

        String message = String.format("Método HTTP '%s' não é suportado para este endpoint", ex.getMethod());

        ApiErrorResponse errorResponse = new ApiErrorResponse(message, "METHOD_NOT_ALLOWED",
                HttpStatus.METHOD_NOT_ALLOWED.value(), request.getRequestURI());

        logger.warn("Método HTTP não suportado: {} {}", ex.getMethod(), request.getRequestURI());
        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).body(errorResponse);
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<ApiErrorResponse> handleNoHandlerFoundException(NoHandlerFoundException ex,
            HttpServletRequest request) {

        ApiErrorResponse errorResponse = new ApiErrorResponse("Endpoint não encontrado", "ENDPOINT_NOT_FOUND",
                HttpStatus.NOT_FOUND.value(), request.getRequestURI());

        logger.warn("Endpoint não encontrado: {} {}", ex.getHttpMethod(), request.getRequestURI());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ApiErrorResponse> handleDataIntegrityViolationException(DataIntegrityViolationException ex,
            HttpServletRequest request) {

        ApiErrorResponse errorResponse = new ApiErrorResponse(
                "Erro de integridade dos dados. Verifique se os dados não violam restrições do banco.",
                "DATA_INTEGRITY_VIOLATION", HttpStatus.CONFLICT.value(), request.getRequestURI());

        logger.error("Erro de integridade dos dados: {}", ex.getMessage(), ex);
        return ResponseEntity.status(HttpStatus.CONFLICT).body(errorResponse);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiErrorResponse> handleGenericException(Exception ex, HttpServletRequest request) {

        ApiErrorResponse errorResponse = new ApiErrorResponse("Erro interno do servidor. Tente novamente mais tarde.",
                "INTERNAL_SERVER_ERROR", HttpStatus.INTERNAL_SERVER_ERROR.value(), request.getRequestURI());

        logger.error("Erro interno não tratado", ex);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
    }
}
