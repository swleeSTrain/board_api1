package org.sunbong.board_api1.common.advice;


import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.sunbong.board_api1.common.exception.TaskException;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
@Log4j2
public class CommonControllerAdvice {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String,Object>> handle(MethodArgumentNotValidException ex){

        BindingResult bindingResult = ex.getBindingResult();

        Map<String,Object> map = new HashMap<>();

        ex.getFieldErrors().forEach(fieldError -> {
            log.error("-------------");
            String key = fieldError.getField();

            Object value = fieldError.getDefaultMessage();
            map.put(key,value);
        });


        return ResponseEntity.status(400).body(map);

    }

    @ExceptionHandler(TaskException.class)
    public ResponseEntity<String> handle(TaskException ex){

        return ResponseEntity.status(ex.getCode()).body(ex.getMessage());

    }
//    @ExceptionHandler(AccessDeniedException.class)
//    public ResponseEntity<Map<String,Object>> handle(AccessDeniedException ex){
//        Map<String,Object> map= Map.of("status",403,"message","Access denied");
//        return ResponseEntity.status(403).body(map);
//    }


}