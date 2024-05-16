package com.potless.backend.damage.event;

import com.potless.backend.damage.dto.controller.request.DamageSetRequestDTO;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

import java.io.File;

@Getter
public class DamageAsyncEvent extends ApplicationEvent {
    private final DamageSetRequestDTO damageSetRequestDTO;
    private final File imageFile;
    private final String hexagonIndex;

    public DamageAsyncEvent(DamageSetRequestDTO damageSetRequestDTO, File imageFile, String hexagonIndex) {
        super(damageSetRequestDTO);
        this.damageSetRequestDTO = damageSetRequestDTO;
        this.imageFile = imageFile;
        this.hexagonIndex = hexagonIndex;
    }
}
