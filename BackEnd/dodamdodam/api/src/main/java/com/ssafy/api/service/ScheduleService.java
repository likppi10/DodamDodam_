package com.ssafy.api.service;

import com.ssafy.core.dto.req.NewScheduleReqDto;
import com.ssafy.core.dto.res.ScheduleDetailResDto;
import com.ssafy.core.entity.Family;
import com.ssafy.core.entity.Profile;
import com.ssafy.core.entity.Schedule;
import com.ssafy.core.entity.User;
import com.ssafy.core.exception.ErrorCode;
import com.ssafy.core.exception.CustomException;
import com.ssafy.core.repository.FamilyRepository;
import com.ssafy.core.repository.ProfileRepository;
import com.ssafy.core.repository.ScheduleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.util.List;

import static com.ssafy.core.exception.ErrorCode.INVALID_REQUEST;

@Service
@Slf4j
@RequiredArgsConstructor
public class ScheduleService {
    private final ScheduleRepository scheduleRepository;
    private final ProfileRepository profileRepository;
    private final FamilyRepository familyRepository;

    @Transactional
    public void createSchedule(NewScheduleReqDto scheduleReq, Family family, User user) {

        Profile profile = profileRepository.findProfileByUserPk(user.getUserPk());

        LocalDate endDate;

        if (scheduleReq.getEndDate() == null) {
            endDate = scheduleReq.getStartDate();
        }else{
            endDate = scheduleReq.getEndDate();
        }

        scheduleRepository.save(Schedule.builder()
                .title(scheduleReq.getTitle())
                .content(scheduleReq.getContent())
                .startDate(scheduleReq.getStartDate())
                .endDate(endDate)
                .family(family)
                .role(profile.getRole())
                .build());
    }


    @Transactional(readOnly = true)
    public Schedule getSchedule (long scheduleId, Family family) {

        Schedule schedule = scheduleRepository.findScheduleById(scheduleId);
        if (schedule == null) {
            throw new CustomException(INVALID_REQUEST, "해당 일정이 없습니다.");
        } else if (schedule.getFamily().getId() != family.getId()) {
            throw new CustomException(ErrorCode.NOT_BELONG_FAMILY);
        }
        return scheduleRepository.findScheduleById(scheduleId);
    }

    @Transactional(readOnly = true)
    public List<ScheduleDetailResDto> getScheduleListByUserPkAndDay (Long userPk, LocalDate day) {

        Family family = familyRepository.findFamilyByUserPk(userPk);
        return scheduleRepository.findScheduleByDay(day, family);
    }

    @Transactional(readOnly = true)
    public List<ScheduleDetailResDto> getScheduleListByMonth (Family family, String month) {

        String[] dayList = month.split("-");
        try {
            if (dayList[0].length() != 4 || dayList[1].length() != 2) {
                throw new CustomException(INVALID_REQUEST, "잘못된 날짜 입력 방식입니다.");
            }
        } catch (NumberFormatException | DateTimeException e) {
            throw new CustomException(INVALID_REQUEST);
        }
        Integer result = Integer.parseInt(dayList[1]);
        return scheduleRepository.findScheduleByMonth(result, family);
    }

    @Transactional
    public void updateSchedule(Schedule schedule, NewScheduleReqDto scheduleReq) {

        LocalDate endDate;

        if (scheduleReq.getEndDate() == null) {
            endDate = scheduleReq.getStartDate();
        }else{
            endDate = scheduleReq.getEndDate();
        }

        schedule.setTitle(scheduleReq.getTitle());
        schedule.setContent(scheduleReq.getContent());
        schedule.setStartDate(scheduleReq.getStartDate());
        schedule.setEndDate(endDate);
        scheduleRepository.save(schedule);
    }

    @Transactional
    public void deleteSchedule(Schedule schedule) {
        scheduleRepository.delete(schedule);
    }
}
