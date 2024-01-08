package com.example.fminder.models;

import com.example.fminder.models.enums.Gender;
import com.example.fminder.models.enums.Major;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    @NotBlank(message = "First name is required")
    @Length(min = 2, max = 45, message = "First name should be between 2 and 45 characters")
    private int id;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "email")
    private String email;

    @Column(name = "password")
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(name = "gender")
    private Gender gender;

    @Column(name = "graduate_year")
    private int graduateYear;

    @Column(name = "major")
    private Major major;

    @Column(name = "interests")
    private String interests;

    @Column(name = "profile_picture")
    String profilePicture;

}
