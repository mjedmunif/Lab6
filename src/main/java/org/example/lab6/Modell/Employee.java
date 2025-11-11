package org.example.lab6.Modell;


import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class Employee {

    @Size(min = 2 , message = "id should be 2 number or more")
    @Pattern(regexp = "^[0-9]{2,}$", message = "id number must be 2 digits or more ")
     private String id;

    @Min(value = 25 , message = "age Must be more than 25")
    private int age;


    @Pattern(regexp = "^05[0-9]{8}$", message = "phone number must start with 05 and be 10 digits")
    private String phoneNumber;

    @NotEmpty
    @Size(min = 2 , message = "name Must be more than 2")
    @Pattern(regexp = "^[a-zA-Z]+$" , message = "name just letters please")
    private String name;

    @NotBlank
    @Email(message = "invalid email")
    private String email;

    @NotEmpty
    @Pattern(regexp = "^(supervisor|coordinator)$" , message = "position should be supervisor or coordinator")
    private String position;


    @AssertFalse
    private boolean onLeave;

    @NotNull(message = "hireDate must not be null")
    @PastOrPresent(message = "hireDate must be in the past or present")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime hireDate;

    @PositiveOrZero
    private int annualLeave;

}
