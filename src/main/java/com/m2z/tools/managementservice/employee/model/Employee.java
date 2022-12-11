package com.m2z.tools.managementservice.employee.model;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;
import java.util.Arrays;

@Entity
@Table(name = "employee", schema = "ms")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Employee {

    public static enum IdentityProvider {
        COGNITO('C');

        public static IdentityProvider of(Character character) {
            return Arrays.stream(IdentityProvider.values()).filter(e -> e.getCode().equals(character)).findFirst().orElseThrow(
                    () -> new IllegalArgumentException("No enum constant with code " + character));
        }

        @Getter
        private final Character code;

        IdentityProvider(char code) {
            this.code = code;
        }
    }

    @Id
    @Column(columnDefinition = "VARCHAR(64)")
    private String id;

    // WebHook to notify email was changed if updatable
    @Column(unique = true, nullable = false, columnDefinition = "VARCHAR(64)")
    private String email;

    @Column(nullable = false, columnDefinition = "BPCHAR(1)")
    // @Enumerated breaks converter
    private IdentityProvider identityProvider;

    @Column(nullable = false, columnDefinition = "VARCHAR(64)")
    private String firstName;

    @Column(nullable = false, columnDefinition = "VARCHAR(64)")
    private String lastName;

    @Column(nullable = false, columnDefinition = "TIMESTAMP without TIME ZONE")
    @CreatedDate
    private LocalDateTime createdAt;
}
