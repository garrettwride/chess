
CREATE TABLE users (
                       id INT AUTO_INCREMENT PRIMARY KEY,
                       username VARCHAR(20) UNIQUE NOT NULL,
                       password VARCHAR(20) NOT NULL
);

CREATE TABLE auth_tokens (
                             id INT AUTO_INCREMENT PRIMARY KEY,
                             username VARCHAR(20) UNIQUE NOT NULL,
                             auth_token VARCHAR(12) UNIQUE NOT NULL
);

CREATE TABLE games (
                       id INT PRIMARY KEY,
                       game_name VARCHAR(12) NOT NULL,
                       white_player VARCHAR(20),
                       black_player VARCHAR(20)
);
