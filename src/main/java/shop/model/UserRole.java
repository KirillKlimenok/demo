package shop.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Embedded;
import org.springframework.data.relational.core.mapping.Table;

import java.lang.annotation.Annotation;
import java.util.UUID;

@EqualsAndHashCode(callSuper = true)
@Data
@Table(value = "user_role")
@NoArgsConstructor
@AllArgsConstructor
public class UserRole extends BaseEntity{

    @Column(value = "id_user")
    @Embedded(onEmpty = Embedded.OnEmpty.USE_EMPTY)
    private UUID userId;

    @Column(value = "id_role")
    private UUID roleId;
}
