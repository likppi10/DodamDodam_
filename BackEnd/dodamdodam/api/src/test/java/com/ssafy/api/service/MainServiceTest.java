package com.ssafy.api.service;

import com.ssafy.core.dto.req.CreateSuggestionReqDto;
import com.ssafy.core.dto.res.MainProfileResDto;
import com.ssafy.core.entity.*;
import com.ssafy.core.exception.CustomException;
import com.ssafy.core.repository.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.transaction.Transactional;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.BDDAssertions.catchThrowable;
import static org.assertj.core.api.BDDAssertions.then;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
@Transactional
class MainServiceTest {

    @InjectMocks
    private MainService mainService;

    @Mock
    private ProfileRepository profileRepository;
    @Mock
    private FamilyRepository familyRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private SuggestionRepository suggestionRepository;
    @Mock
    private SuggestionReactionRepository suggestionReactionRepository;
    @Mock
    private AlarmRepository alarmRepository;

    final MainProfileResDto expectMainProfileDto = MainProfileResDto.builder()
            .profileId(2L)
            .comment("test")
            .emotion("smile")
            .imagePath("image.jpg")
            .role("아빠")
            .build();

    final Family expectFamily = Family.builder()
            .id(1L)
            .picture("familyPicture.jpg")
            .code("TEST12345678910")
            .build();

    final CreateSuggestionReqDto expectCreateSuggestionDto = CreateSuggestionReqDto.builder()
            .text("suggestion")
            .build();

    final SuggestionReaction expectSuggestionReaction = SuggestionReaction.builder()
            .id(1L)
            .isLike(true)
            .build();

    final  Suggestion expectSuggestion = Suggestion.builder()
            .id(1L)
            .text("suggestion")
            .likeCount(1L)
            .dislikeCount(0L)
            .build();
    @Test
    void getProfileList_정상동작() {
        //given
        given(familyRepository.findFamilyIdByUserPk(anyLong())).willReturn(1L);
        given(profileRepository.findProfileIdByUserPk(anyLong())).willReturn(1L);
        given(profileRepository.findProfileListByFamilyId(anyLong()))
                .willReturn(List.of(expectMainProfileDto));

        //when
        final List<MainProfileResDto> actualProfileList
                = mainService.getProfileListExceptMe(1L);

        //then
        then(actualProfileList).isEqualTo(List.of(expectMainProfileDto));

    }

    @Test
    void getProfileList_본인_제외_한명도_없을경우() {
        //given
        given(familyRepository.findFamilyIdByUserPk(anyLong())).willReturn(1L);
        given(profileRepository.findProfileIdByUserPk(anyLong())).willReturn(2L);
        given(profileRepository.findProfileListByFamilyId(anyLong()))
                .willReturn(List.of(expectMainProfileDto));

        //when
        final List<MainProfileResDto> actualProfileList
                = mainService.getProfileListExceptMe(1L);

        //then
        then(actualProfileList).isEqualTo(Collections.emptyList());

    }

    @Test
    void getProfileList_family_profile_id가_null일때() {
        //given
        given(familyRepository.findFamilyIdByUserPk(anyLong())).willReturn(null);
        given(profileRepository.findProfileIdByUserPk(anyLong())).willReturn(null);

        //when
        final Throwable throwable = catchThrowable(() -> mainService.getProfileListExceptMe(1L));

        //then
        then(throwable).isExactlyInstanceOf(CustomException.class);

    }

    @Test
    void createSuggestion_정상동작() {
        //given
        given(familyRepository.findFamilyByUserPk(anyLong())).willReturn(expectFamily);

        given(suggestionRepository.countSuggestionByFamily_Id(anyLong()))
                .willReturn(1L);

        given(suggestionRepository.save(any())).willReturn(expectSuggestion);

        //when
        ArgumentCaptor<Suggestion> captor = ArgumentCaptor.forClass(Suggestion.class);
        mainService.createSuggestion(expectCreateSuggestionDto,1L);

        //then
        verify(suggestionRepository, times(1)).save(captor.capture());
        Suggestion actualSuggestion = captor.getValue();
        then(actualSuggestion.getText()).isEqualTo(expectCreateSuggestionDto.getText());

    }

//    @Test
//    void createSuggestion() {
//
//    }
//
//    @Test
//    void deleteSuggestion() {
//    }
//
//    @Test
//    void getSuggestionList() {
//    }
//
//    @Test
//    void manageSuggestionReaction() {
//    }
//
//    @Test
//    void findTodayMission() {
//    }
//
//    @Test
//    void getTargetFcmToken() {
//    }
//
//    @Test
//    void getProfileByUserPk() {
//    }
//
//    @Test
//    void getProfileByProfilePk() {
//    }
//
//    @Test
//    void recordAlarmCount() {
//    }
//
//    @Test
//    void getAlarmList() {
//    }
//
//    @Test
//    void meAndTargetFamilyCheck() {
//    }
}