package com.example.schedulerv2.controller;

import com.example.schedulerv2.dto.request.SaveScheduleRequestDto;
import com.example.schedulerv2.dto.request.UpdateScheduleRequestDto;
import com.example.schedulerv2.dto.response.ScheduleResponseDto;
import com.example.schedulerv2.service.ScheduleService;
import com.example.schedulerv2.util.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/schedules")
@RequiredArgsConstructor
public class ScheduleController {

    private final ScheduleService scheduleService;

    // 스케줄 생성
    @PostMapping
    public ResponseEntity<ScheduleResponseDto> save(@Valid  @RequestBody SaveScheduleRequestDto saveScheduleRequestDto, HttpServletRequest request){
        String currentUserEmail = JwtUtil.getEmailFromRequest(request);

        ScheduleResponseDto scheduleResponseDto = scheduleService.save(saveScheduleRequestDto.getTitle(), saveScheduleRequestDto.getContents(), currentUserEmail);

        return new ResponseEntity<>(scheduleResponseDto, HttpStatus.CREATED);
    }

    // 스케줄 조회
    @GetMapping("/{id}")
    public ResponseEntity<ScheduleResponseDto> findById(@PathVariable Long id){
        ScheduleResponseDto scheduleResponseDto = scheduleService.findById(id);

        return new ResponseEntity<>(scheduleResponseDto, HttpStatus.OK);
    }

    // 스케줄 조회 ( by userId )
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<ScheduleResponseDto>> findByUserId(
            @PathVariable Long userId,
            @RequestParam Integer pageNumber, Integer pageSize
    ){
        List<ScheduleResponseDto> scheduleResponseDtoList = scheduleService.findByUserId(userId, pageNumber, pageSize);

        return new ResponseEntity<>(scheduleResponseDtoList, HttpStatus.OK);
    }

    // 스케줄 전체 조회
    @GetMapping
    public ResponseEntity<List<ScheduleResponseDto>> findAll(@RequestParam Integer pageNumber, Integer pageSize){
        List<ScheduleResponseDto> schedules = scheduleService.findAll(pageNumber, pageSize);

        return new ResponseEntity<>(schedules, HttpStatus.OK);
    }

    // 스케줄 수정
    @PatchMapping("/{id}")
    public ResponseEntity<ScheduleResponseDto> update(
        @PathVariable Long id,
        @Valid @RequestBody UpdateScheduleRequestDto updateScheduleRequestDto,
        HttpServletRequest request
    ){
        String currentUserEmail = JwtUtil.getEmailFromRequest(request);

        ScheduleResponseDto scheduleResponseDto = scheduleService.update(id, updateScheduleRequestDto.getTitle(), updateScheduleRequestDto.getContents(), currentUserEmail);

        return new ResponseEntity<>(scheduleResponseDto, HttpStatus.OK);
    }

    // 스케줄 삭제
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Long id, HttpServletRequest request){
        String currentUserEmail = JwtUtil.getEmailFromRequest(request);

        scheduleService.deleteById(id, currentUserEmail);

        return new ResponseEntity<>(HttpStatus.OK);
    }
}
