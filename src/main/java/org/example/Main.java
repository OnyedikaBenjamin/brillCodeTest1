package org.example;

import io.jsonwebtoken.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Scanner;

public class Main {
    private static final String SECRET_KEY = "4226452948404D635166546A576E5A7234753778214125442A462D4A614E6452";
    private static boolean validateFields(String username, String email, String password, String dateOfBirthString) {
        boolean usernameValid;
        boolean emailValid;
        boolean passwordValid;
        boolean dateOfBirthValid;

        usernameValid = validateUsername(username);
        if (!usernameValid) {
            if (username.isEmpty() || username.isBlank()) {
                System.out.println("Username cannot be empty, it must have a minimum of 4 characters");
            } else {
                System.out.println("Username must have a minimum of 4 characters");
            }
        }

        emailValid = validateEmail(email);
        if (!emailValid) {
            if (email.isEmpty() || email.isBlank()) {
                System.out.println("Email field cannot be empty.");
            } else {
                System.out.println("Please input a valid email address i.e benbillion62@gmail.com");
            }
        }

        passwordValid = validatePassword(password);
        if (!passwordValid) {

            if (password.isEmpty() || password.isBlank()) {
                System.out.println("password cannot be empty.");
            } else {
                System.out.println("A Strong password is required with at least 1 uppercase, " +
                        "" +
                        " 1 special character, 1 number, and minimum of 8 characters");
            }
        }

        dateOfBirthValid = validateDateOfBirth(dateOfBirthString);

        if (!dateOfBirthValid) {
            if(dateOfBirthString.isEmpty()){
                System.out.println("Date of Birth cannot not be empty");
            }
            else {
                System.out.println("Date of birth should not be less than 16 years");
            }
        }
        return usernameValid && emailValid && passwordValid && dateOfBirthValid;
    }

    private static boolean validateUsername(String username) {
        return !username.isEmpty() && username.length() >= 4;
    }

    private static boolean validateEmail(String email) {
        return !email.isEmpty() && email.matches("[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}");
    }


    private static boolean validateDateOfBirth(String dobString) {
        LocalDate dob = LocalDate.parse(dobString, DateTimeFormatter.ISO_LOCAL_DATE);
        LocalDate minValidDate = LocalDate.now().minusYears(16);
        return !dobString.isEmpty() && dob.isBefore(minValidDate);
    }



        public static String generateToken(String username, String email) {
            long currentTimeMillis = System.currentTimeMillis();
            Date now = new Date(currentTimeMillis);

            JwtBuilder builder = Jwts.builder()
                    .setId(username)
                    .setSubject(email)
                    .setIssuedAt(now)
                    .setExpiration(new Date(currentTimeMillis + 86400000))
                    .signWith(SignatureAlgorithm.HS256, SECRET_KEY);

            return builder.compact();
        }


    private static boolean verifyToken(String jwtToken) {
        try {
            Jws<Claims> claimsJws = Jwts.parser()
                    .setSigningKey(SECRET_KEY)
                    .parseClaimsJws(jwtToken);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private static boolean validatePassword(String password) {
        if (password.isEmpty()) {
            return false;
        }
        boolean hasUppercase = false;
        boolean hasSpecialChar = false;
        boolean hasNumber = false;

        for (char ch : password.toCharArray()) {
            if (Character.isUpperCase(ch)) {
                hasUppercase = true;
            } else if (isSpecialCharacter(ch)) {
                hasSpecialChar = true;
            } else if (Character.isDigit(ch)) {
                hasNumber = true;
            }
        }
        return hasUppercase && hasSpecialChar && hasNumber && password.length() >= 8;
    }

    private static boolean isSpecialCharacter(char ch) {
        String specialCharacters = "!@#$%^&*";
        return specialCharacters.indexOf(ch) != -1;
    }


    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Username: ");
        String username = scanner.nextLine();

        System.out.print("Email: ");
        String email = scanner.nextLine();

        System.out.print("Password: ");
        String password = scanner.nextLine();

        System.out.print("Date of Birth (yyyy-mm-dd): ");
        String dateOfBirthString = scanner.nextLine();

        boolean validationPassed = validateFields(username, email, password, dateOfBirthString);

        if (validationPassed) {
            String token = generateToken(username, email);
            System.out.println("Validation successful! JWT token: " + token);

            System.out.print("Enter the JWT token for verification: ");
            String jwtToken = scanner.nextLine();

            boolean verificationPassed = verifyToken(jwtToken);
            if (verificationPassed) {
                System.out.println("Verification passed!");
            } else {
                System.out.println("Verification failed!");
            }
        }
    }

}