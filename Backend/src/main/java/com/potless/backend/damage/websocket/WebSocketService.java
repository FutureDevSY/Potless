package com.potless.backend.damage.websocket;

import com.potless.backend.damage.entity.area.AreaEntity;
import com.potless.backend.global.exception.member.ManagerNotFoundException;
import com.potless.backend.member.entity.ManagerEntity;
import com.potless.backend.member.entity.MemberEntity;
import com.potless.backend.member.repository.manager.ManagerRepository;
import com.potless.backend.member.service.MemberService;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Log4j2
@Service
@RequiredArgsConstructor
public class WebSocketService {

    private final MemberService memberService;
    private final ManagerRepository managerRepository;
    private final SimpMessagingTemplate messagingTemplate;

    public void sendWebSocket(Authentication authentication, @NotNull(message = "데미지 ID 는 비어있을 수 없습니다") Long damageId) {
        MemberEntity member = memberService.findMember(authentication.getName());
        AreaEntity area = member.getArea();

        ManagerEntity managerEntity = managerRepository.findByAreaEntity(area)
                .orElseThrow(ManagerNotFoundException::new);

        messagingTemplate.convertAndSendToUser(managerEntity.getMemberEntity().getEmail(), "/topic/notifications", "작업 완료 알림: Damage ID " + damageId);
    }
}
