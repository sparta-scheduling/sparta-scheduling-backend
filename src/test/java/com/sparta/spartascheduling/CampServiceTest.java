package com.sparta.spartascheduling.domain.camp.service;

import com.sparta.spartascheduling.domain.camp.dto.CampRequestDto;
import com.sparta.spartascheduling.domain.camp.dto.CampResponseDto;
import com.sparta.spartascheduling.domain.camp.entity.Camp;
import com.sparta.spartascheduling.domain.camp.enums.CampStatus;
import com.sparta.spartascheduling.domain.camp.repository.CampRepository;
import com.sparta.spartascheduling.domain.manager.entity.Manager;
import com.sparta.spartascheduling.domain.manager.repository.ManagerRepository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class CampServiceTest {

	@Mock
	private CampRepository campRepository;

	@Mock
	private ManagerRepository managerRepository;

	@InjectMocks
	private CampService campService;

	@Test
	public void 캠프생성_성공() {
		// given
		Long managerId = 1L;
		Manager manager = Manager.builder()
			.id(managerId)
			.email("manager@example.com")
			.password("password123")
			.username("매니저")
			.build();

		CampRequestDto requestDto = new CampRequestDto();
		requestDto.setName("스파르타 코딩 캠프");
		requestDto.setContents("캠프 내용");
		requestDto.setOpenDate(LocalDate.now().plusDays(10));
		requestDto.setCloseDate(LocalDate.now().plusDays(20));
		requestDto.setMaxCount(30);

		when(managerRepository.findById(managerId)).thenReturn(Optional.of(manager));
		when(campRepository.existsByNameAndOpenDate(requestDto.getName(), requestDto.getOpenDate())).thenReturn(false);
		when(campRepository.save(any(Camp.class))).thenAnswer(invocation -> {
			Camp camp = invocation.getArgument(0);
			camp.setId(1L); // 임의의 ID 설정
			return camp;
		});

		// when
		CampResponseDto responseDto = campService.createCamp(requestDto, managerId);

		// then
		verify(campRepository, times(1)).save(any(Camp.class));
		assertEquals(requestDto.getName(), responseDto.getName());
		assertEquals(CampStatus.CREATED, responseDto.getStatus());
	}

	@Test
	public void 캠프생성_실패_매니저없음() {
		// given
		Long managerId = 1L;

		CampRequestDto requestDto = new CampRequestDto();
		requestDto.setName("스파르타 코딩 캠프");
		requestDto.setContents("캠프 내용");
		requestDto.setOpenDate(LocalDate.now().plusDays(10));
		requestDto.setCloseDate(LocalDate.now().plusDays(20));
		requestDto.setMaxCount(30);

		when(managerRepository.findById(managerId)).thenReturn(Optional.empty());

		// when & then
		IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
			campService.createCamp(requestDto, managerId);
		});

		assertEquals("매니저를 찾을 수 없습니다.", exception.getMessage());
		verify(campRepository, never()).save(any(Camp.class));
	}

	@Test
	public void 캠프생성_실패_캠프중복() {
		// given
		Long managerId = 1L;
		Manager manager = Manager.builder()
			.id(managerId)
			.email("manager@example.com")
			.password("password123")
			.username("매니저")
			.build();

		CampRequestDto requestDto = new CampRequestDto();
		requestDto.setName("스파르타 코딩 캠프");
		requestDto.setContents("캠프 내용");
		requestDto.setOpenDate(LocalDate.now().plusDays(10));
		requestDto.setCloseDate(LocalDate.now().plusDays(20));
		requestDto.setMaxCount(30);

		when(managerRepository.findById(managerId)).thenReturn(Optional.of(manager));
		when(campRepository.existsByNameAndOpenDate(requestDto.getName(), requestDto.getOpenDate())).thenReturn(true);

		// when & then
		IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
			campService.createCamp(requestDto, managerId);
		});

		assertEquals("같은 이름과 시작일의 캠프가 이미 존재합니다.", exception.getMessage());
		verify(campRepository, never()).save(any(Camp.class));
	}

	@Test
	public void 캠프생성_실패_시작일이_종료일보다_늦음() {
		// given
		Long managerId = 1L;
		Manager manager = Manager.builder()
			.id(managerId)
			.email("manager@example.com")
			.password("password123")
			.username("매니저")
			.build();

		CampRequestDto requestDto = new CampRequestDto();
		requestDto.setName("스파르타 코딩 캠프");
		requestDto.setContents("캠프 내용");
		requestDto.setOpenDate(LocalDate.now().plusDays(20));
		requestDto.setCloseDate(LocalDate.now().plusDays(10));
		requestDto.setMaxCount(30);

		when(managerRepository.findById(managerId)).thenReturn(Optional.of(manager));
		when(campRepository.existsByNameAndOpenDate(requestDto.getName(), requestDto.getOpenDate())).thenReturn(false);

		// when & then
		IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
			campService.createCamp(requestDto, managerId);
		});

		assertEquals("시작일은 종료일보다 빠르거나 같아야 합니다.", exception.getMessage());
		verify(campRepository, never()).save(any(Camp.class));
	}

	@Test
	public void 캠프생성_실패_시작일이_과거() {
		// given
		Long managerId = 1L;
		Manager manager = Manager.builder()
			.id(managerId)
			.email("manager@example.com")
			.password("password123")
			.username("매니저")
			.build();

		CampRequestDto requestDto = new CampRequestDto();
		requestDto.setName("스파르타 코딩 캠프");
		requestDto.setContents("캠프 내용");
		requestDto.setOpenDate(LocalDate.now().minusDays(1));
		requestDto.setCloseDate(LocalDate.now().plusDays(10));
		requestDto.setMaxCount(30);

		when(managerRepository.findById(managerId)).thenReturn(Optional.of(manager));
		when(campRepository.existsByNameAndOpenDate(requestDto.getName(), requestDto.getOpenDate())).thenReturn(false);

		// when & then
		IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
			campService.createCamp(requestDto, managerId);
		});

		assertEquals("시작일은 오늘 이후여야 합니다.", exception.getMessage());
		verify(campRepository, never()).save(any(Camp.class));
	}

	@Test
	public void 캠프생성_실패_최대인원_음수() {
		// given
		Long managerId = 1L;
		Manager manager = Manager.builder()
			.id(managerId)
			.email("manager@example.com")
			.password("password123")
			.username("매니저")
			.build();

		CampRequestDto requestDto = new CampRequestDto();
		requestDto.setName("스파르타 코딩 캠프");
		requestDto.setContents("캠프 내용");
		requestDto.setOpenDate(LocalDate.now().plusDays(10));
		requestDto.setCloseDate(LocalDate.now().plusDays(20));
		requestDto.setMaxCount(-5);

		when(managerRepository.findById(managerId)).thenReturn(Optional.of(manager));
		when(campRepository.existsByNameAndOpenDate(requestDto.getName(), requestDto.getOpenDate())).thenReturn(false);

		// when & then
		IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
			campService.createCamp(requestDto, managerId);
		});

		assertEquals("최대 인원은 1명 이상이어야 합니다.", exception.getMessage());
		verify(campRepository, never()).save(any(Camp.class));
	}
}
