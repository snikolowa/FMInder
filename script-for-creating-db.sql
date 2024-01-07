-- Create FMInder Database
CREATE DATABASE IF NOT EXISTS FMinder;
USE FMinder;

-- Create Users Table
CREATE TABLE IF NOT EXISTS users (
                                     user_id INT PRIMARY KEY AUTO_INCREMENT,
                                     email VARCHAR(255) NOT NULL,
    password VARCHAR(255) NOT NULL,
    first_name VARCHAR(255) NOT NULL,
    last_name VARCHAR(255) NOT NULL,
    gender ENUM('Male', 'Female', 'Other') NOT NULL,
    graduate_year INT,
    major VARCHAR(255),
    profile_pic VARCHAR(255),
    interests TEXT
    );

-- Create Requests Table
CREATE TABLE IF NOT EXISTS requests (
                                        request_id INT PRIMARY KEY AUTO_INCREMENT,
                                        sender_user_id INT,
                                        receiver_user_id INT,
                                        created_on TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                        updated_on TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                                        status ENUM('Pending', 'Accepted', 'Denied') NOT NULL,
    FOREIGN KEY (sender_user_id) REFERENCES users(user_id),
    FOREIGN KEY (receiver_user_id) REFERENCES users(user_id)
    );

-- Create Chats Table
CREATE TABLE IF NOT EXISTS chats (
                                     chat_id INT PRIMARY KEY AUTO_INCREMENT,
                                     receiver_id INT,
                                     sender_id INT,
                                     message TEXT,
                                     timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                     FOREIGN KEY (receiver_id) REFERENCES users(user_id),
    FOREIGN KEY (sender_id) REFERENCES users(user_id)
    );

