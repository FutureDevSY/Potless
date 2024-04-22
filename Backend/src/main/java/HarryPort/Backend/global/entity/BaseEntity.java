package HarryPort.Backend.global.entity;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Getter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class BaseEntity {

    @CreatedDate
    private LocalDateTime createdDateTime;

    @LastModifiedDate
    private LocalDateTime modifiedDateTime;

    @Column(name = "is_deleted")
    private boolean deleted = false;

    @Column(name = "deleted_date_time")
    private LocalDateTime deletedDateTime;

    public void softDelete() {
        this.deleted = true;
        this.deletedDateTime = LocalDateTime.now();
    }
}

