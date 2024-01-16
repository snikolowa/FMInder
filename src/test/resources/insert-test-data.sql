INSERT INTO users (user_id, email, password, first_name, last_name, gender, graduate_year)
VALUES
    (1, 'user1@example.com', 'password1', 'John', 'Doe', 'Male', 2023),
    (2, 'user2@example.com', 'password2', 'Jane', 'Smith', 'Female', 2022),
    (3, 'user3@example.com', 'password3', 'Alice', 'Johnson', 'Female', 2023),
    (4, 'user4@example.com', 'password4', 'Bob', 'Williams', 'Male', 2022),
    (5, 'user5@example.com', 'password5', 'Emily', 'Brown', 'Female', 2024),
    (6, 'user6@example.com', 'password6', 'Michael', 'Jones', 'Male', 2022),
    (7, 'user7@example.com', 'password7', 'Sara', 'Miller', 'Female', 2023),
    (8, 'user8@example.com', 'password8', 'David', 'Davis', 'Male', 2024),
    (9, 'user9@example.com', 'password9', 'Laura', 'Anderson', 'Female', 2022),
    (10, 'user10@example.com', 'password10', 'Chris', 'Taylor', 'Male', 2023);

INSERT INTO chats (chat_id, receiver_id, sender_id, message, timestamp)
VALUES
    (1, 1, 2, 'Hey, how are you?', '2024-01-15 12:30:00'),
    (2, 2, 1, 'Good, thanks! How about you?', '2024-01-15 12:35:00'),
    (3, 3, 1, 'Hi there! What are you up to?', '2024-01-15 13:00:00'),
    (4, 1, 3, 'Not much, just working on some projects.', '2024-01-15 13:10:00'),
    (5, 2, 3, 'Nice! Anything interesting?', '2024-01-15 13:15:00'),
    (6, 3, 2, 'I am exploring new technologies. What about you?', '2024-01-15 13:20:00'),
    (7, 4, 1, 'Hey! Long time no see. How have you been?', '2024-01-15 14:00:00'),
    (8, 1, 4, 'I have been good. How about you?', '2024-01-15 14:05:00'),
    (9, 1, 2, 'Hello! Ready for the upcoming project meeting?', '2024-01-15 15:30:00'),
    (10, 2, 1, 'Absolutely!', '2024-01-15 15:35:00');

INSERT INTO requests (request_id, sender_user_id, receiver_user_id, created_on, updated_on, status)
VALUES
    (1, 3, 1, '2024-01-15 11:00:00', '2024-01-15 11:00:00', 'Pending'),
    (2, 3, 2, '2024-01-15 12:15:00', '2024-01-15 12:15:00', 'Accepted'),
    (3, 1, 4, '2024-01-15 13:15:00', '2024-01-15 13:15:00', 'Accepted'),
    (4, 5, 2, '2024-01-15 14:00:00', '2024-01-15 14:00:00', 'Pending');

