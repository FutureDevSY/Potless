package com.potless.backend.damage.event;

import com.potless.backend.damage.service.AsyncService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Log4j2
@Component
@RequiredArgsConstructor
public class DamageAsyncEventListener {

    private final AsyncService asyncService;

//    @Async
//    @EventListener
//    @Transactional
//    public void handleDamageAsyncEvent(DamageAsyncEvent event) {
//        try {
//            asyncService.setDamageAsyncMethod(event.getDamageSetRequestDTO(), event.getImageFile(), event.getHexagonIndex()).get();
//        } catch (Exception e) {
//            log.error("Error handling DamageAsyncEvent: {}", e.getMessage());
//            throw new RuntimeException(e);
//        }
//    }
}
