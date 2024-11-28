package com.sparta.spartascheduling.domain.camp.controller;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import com.sparta.spartascheduling.common.annotation.Auth;
import com.sparta.spartascheduling.common.dto.AuthUser;
import com.sparta.spartascheduling.domain.camp.dto.ApplyResponseDto;
import com.sparta.spartascheduling.domain.camp.dto.CampRequestDto;
import com.sparta.spartascheduling.domain.camp.dto.CampResponseDto;
import com.sparta.spartascheduling.domain.camp.service.CampLockFacade;
import com.sparta.spartascheduling.domain.camp.service.CampService;
import com.sparta.spartascheduling.domain.userCamp.entity.UserCamp;
import lombok.RequiredArgsConstructor;


@RestController
@RequiredArgsConstructor
public class CampController {

    private final CampService campService;
	private final CampLockFacade campLockFacade;

	// 캠프 생성 API
	@PostMapping("/admin/camps")
	public ResponseEntity<CampResponseDto> createCamp(
		@RequestBody CampRequestDto requestDto,
		@Auth AuthUser authUser
	) {
		CampResponseDto responseDto = campService.createCamp(requestDto, authUser);
		return ResponseEntity.ok(responseDto);
	}

	// 캠프 단건 조회 API
	@GetMapping("/camps/{campId}")
	public ResponseEntity<CampResponseDto> getCampById(@PathVariable Long campId) {
		CampResponseDto responseDto = campService.getCampById(campId);
		return ResponseEntity.ok(responseDto);
	}

	// 캠프 리스트 조회 API
	@GetMapping("/camps")
	public ResponseEntity<List<CampResponseDto>> getAllCamps() {
		List<CampResponseDto> response = campService.getAllCamps();
		return ResponseEntity.ok(response);
	}

	// 캠프 신청 - 동시성 제어 할 곳
	@PostMapping("/camps/{campId}")
	public UserCamp applyForCamp(@PathVariable Long campId, @Auth AuthUser authUser) {
	 	return campService.applyForCamp(campId, authUser);
	}

	// Redisson 적용
	@PostMapping("/camps/{campId}/redisson")
	public ApplyResponseDto applyForCampRedisson(@PathVariable Long campId, @Auth AuthUser authUser){
		return campLockFacade.applyForCampRedisson(campId, authUser);
	}

	// 비관적 락 적용
	@PostMapping("/camps/{campId}/pessimistic")
	public ApplyResponseDto applyForCampPessimistic(@PathVariable Long campId, @Auth AuthUser authUser){
		return campService.applyForCampPessimistic(campId, authUser);
	}
}
