package shop.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Persistent;
import org.springframework.data.relational.core.mapping.Column;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public abstract class BaseEntity{
    @Id
    @Column("id")
    protected UUID id;

    @Column("created")
    protected LocalDateTime created;

    @Column("updated")
    protected LocalDateTime updated;

    @Column("status")
    protected Status status;

}
