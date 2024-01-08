package com.example.fminder.models;

import com.example.fminder.models.enums.Gender;
import com.example.fminder.models.enums.Major;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

@Data
@NoArgsConstructor
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private int id;

    @NotBlank(message = "First name is required")
    @Length(min = 2, max = 45, message = "First name should be between 2 and 45 characters")
    @Column(name = "first_name")
    private String firstName;

    @NotBlank(message = "Last name is required")
    @Length(min = 2, max = 45, message = "Last name should be between 2 and 45 characters")
    @Column(name = "last_name")
    private String lastName;

    @NotBlank(message = "Email address is required")
    @Column(name = "email")
    private String email;

    @NotBlank(message = "Password is required")
    @Column(name = "password")
    private String password;

    @NotBlank(message = "Gender is required")
    @Enumerated(EnumType.STRING)
    @Column(name = "gender")
    private Gender gender;

    @NotBlank(message = "Graduation year is required")
    @Column(name = "graduate_year")
    private int graduateYear;

    @Column(name = "major")
    private Major major;

    @Column(name = "interests")
    private String interests;

    @Column(name = "profile_picture")
    String profilePicture;

    public User(String firstName, String lastName, Gender gender, String email, String password, int graduateYear, Major major) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.gender = gender;
        this.email = email;
        this.password = password;
        this.graduateYear = graduateYear;
        this.major = major;
    }
}
