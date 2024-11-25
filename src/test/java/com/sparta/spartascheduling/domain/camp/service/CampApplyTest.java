package com.sparta.spartascheduling.domain.camp.service;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;
import com.sparta.spartascheduling.common.dto.AuthUser;
import com.sparta.spartascheduling.domain.camp.entity.Camp;
import com.sparta.spartascheduling.domain.camp.enums.CampStatus;
import com.sparta.spartascheduling.domain.camp.repository.CampRepository;
import com.sparta.spartascheduling.domain.user.entity.User;
import com.sparta.spartascheduling.domain.user.repository.UserRepository;
import com.sparta.spartascheduling.domain.userCamp.entity.UserCamp;
import com.sparta.spartascheduling.domain.userCamp.repository.UserCampRepository;
import com.sparta.spartascheduling.exception.customException.CampException;
import com.sparta.spartascheduling.exception.customException.UserCampException;
import com.sparta.spartascheduling.exception.customException.UserException;
import com.sparta.spartascheduling.exception.enums.ExceptionCode;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.*;


public class CampApplyTest {

    @Mock
    private CampRepository campRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserCampRepository userCampRepository;

    @InjectMocks
    private CampService campService;

    private Camp camp;
    private User user;

    private AuthUser authUser;
    private AuthUser studentUser;
    private AuthUser adminUser;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);

        // Mocking the user data
        studentUser = new AuthUser(1L, "", "학생", "USER");
        adminUser = new AuthUser(22L, "", "매니저", "ADMIN");

        // Mocking the user object
        user = mock(User.class);
        when(user.getId()).thenReturn(1L);

        // Mocking the camp object
        camp = mock(Camp.class);
        when(camp.getId()).thenReturn(3L);
        when(camp.getRemainCount()).thenReturn(10);
    }

    @Test
    @DisplayName("캠프 신청")
    public void testApplyForCampSuccess() {
        // Arrange
        when(campRepository.findById(3L)).thenReturn(Optional.of(camp));
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userCampRepository.findByUserId(1L)).thenReturn(null); // No previous camp application
        when(userCampRepository.existsActiveCampForUser(1L, CampStatus.CLOSED)).thenReturn(false); // No active camp

        UserCamp userCamp = new UserCamp.Builder()
            .user(user)
            .camp(camp)
            .build();
        when(userCampRepository.save(any(UserCamp.class))).thenReturn(userCamp);

        // Act
        UserCamp result = campService.applyForCamp(3L, studentUser);
        System.out.println("결과 : "+result);
        System.out.println("결과 고유번호 : "+result.getCamp().getId());
        System.out.println("결과 남은 인원 : "+result.getCamp().getRemainCount());

        // Assert
        assertNotNull(result);
        verify(campRepository, times(1)).save(camp);
        verify(userCampRepository, times(1)).save(any(UserCamp.class));
    }

    @Test
    @DisplayName("학생외에 신청 예외")
    public void testApplyForCampWhenUserIsNotStudent() {
        // Act & Assert
        UserException exception = assertThrows(UserException.class, () -> {
            campService.applyForCamp(1L, adminUser);
        });
        assertEquals(ExceptionCode.NO_AUTHORIZATION_USER.getMessage(), exception.getExceptionCode().getMessage());
        assertEquals(ExceptionCode.NO_AUTHORIZATION_USER.getHttpStatus(), exception.getExceptionCode().getHttpStatus());
    }

    @Test
    @DisplayName("캠프 미존재 예외")
    public void testApplyForCampWhenCampNotFound() {
        // Arrange
        when(campRepository.findById(1L)).thenReturn(Optional.empty()); // 캠프가 존재하지 않음

        // Act & Assert
        CampException exception = assertThrows(CampException.class, () -> {
            campService.applyForCamp(1L, studentUser);
        });
        assertEquals(ExceptionCode.NOT_FOUND_CAMP.getMessage(), exception.getExceptionCode().getMessage());
        assertEquals(ExceptionCode.NOT_FOUND_CAMP.getHttpStatus(), exception.getExceptionCode().getHttpStatus());
    }

    @Test
    @DisplayName("학생 미존재 예외")
    public void testApplyForCampWhenUserNotFound() {
        // Arrange
        when(campRepository.findById(1L)).thenReturn(Optional.of(camp));
        when(userRepository.findById(22L)).thenReturn(Optional.empty()); // 유저가 존재하지 않음

        // Act & Assert
        UserException exception = assertThrows(UserException.class, () -> {
            campService.applyForCamp(1L, studentUser);
        });
        assertEquals(ExceptionCode.NOT_FOUND_USER.getMessage(), exception.getExceptionCode().getMessage());
        assertEquals(ExceptionCode.NOT_FOUND_USER.getHttpStatus(), exception.getExceptionCode().getHttpStatus());
    }

    @Test
    @DisplayName("현재 참여중인 캠프 예외")
    public void testApplyForCampWhenUserAlreadyInActiveCamp() {
        // Arrange
        when(campRepository.findById(3L)).thenReturn(Optional.of(camp));
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userCampRepository.findByUserId(1L)).thenReturn(new UserCamp()); // 이미 신청한 캠프가 있음
        when(userCampRepository.existsActiveCampForUser(1L, CampStatus.CLOSED)).thenReturn(true); // 이미 참여 중인 캠프 있음

        // Act & Assert
        CampException exception = assertThrows(CampException.class, () -> {
            campService.applyForCamp(3L, studentUser);
        });
        assertEquals(ExceptionCode.ALREADY_JOIN_CAMP.getMessage(), exception.getExceptionCode().getMessage());
        assertEquals(ExceptionCode.ALREADY_JOIN_CAMP.getHttpStatus(), exception.getExceptionCode().getHttpStatus());

    }

    @Test
    @DisplayName("정원초과 예외")
    public void testApplyForCampWhenCampIsFull() {
        // Arrange
        when(campRepository.findById(3L)).thenReturn(Optional.of(camp));
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userCampRepository.findByUserId(1L)).thenReturn(null); // 신청한 캠프가 없음
        when(camp.getRemainCount()).thenReturn(0); // 캠프의 남은 인원이 0명

        // Act & Assert
        UserCampException exception = assertThrows(UserCampException.class, () -> {
            campService.applyForCamp(3L, studentUser);
        });
        assertEquals(ExceptionCode.EXCEEDED_CAMP_CAPACITY.getMessage(), exception.getExceptionCode().getMessage());
        assertEquals(ExceptionCode.EXCEEDED_CAMP_CAPACITY.getHttpStatus(), exception.getExceptionCode().getHttpStatus());
    }

    @Test
    @DisplayName("중복된 캠프 예외")
    public void testApplyForCampWhenCampAlreadyApplied() {
        // Arrange
        UserCamp userCamp = new UserCamp.Builder()
            .user(user)
            .camp(camp)
            .build();
        when(userCampRepository.save(any(UserCamp.class))).thenReturn(userCamp);

        when(campRepository.findById(3L)).thenReturn(Optional.of(camp));
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userCampRepository.findByUserId(1L)).thenReturn(userCamp); // 이미 신청한 캠프가 있음

        // Act & Assert
        CampException exception = assertThrows(CampException.class, () -> {
            campService.applyForCamp(3L, studentUser);
        });
        assertEquals(ExceptionCode.ALREADY_APPLY_CAMP.getMessage(), exception.getExceptionCode().getMessage());
        assertEquals(ExceptionCode.ALREADY_APPLY_CAMP.getHttpStatus(), exception.getExceptionCode().getHttpStatus());
    }
}
