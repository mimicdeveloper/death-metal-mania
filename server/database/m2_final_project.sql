-- database m2_final_project
BEGIN TRANSACTION;

-- *************************************************************************************************
-- Drop all db objects in the proper order
-- *************************************************************************************************
DROP TABLE IF EXISTS User_favorites, Events, Albums, Bands, Profile, Users CASCADE;

-- *************************************************************************************************
-- Create the tables and constraints
-- *************************************************************************************************

-- Users table
CREATE TABLE Users (
    user_id SERIAL PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password_hash VARCHAR(200) NOT NULL,
    role VARCHAR(50) NOT NULL
);

-- Profile table (references Users with ON DELETE CASCADE)
CREATE TABLE Profile (
    profile_id SERIAL PRIMARY KEY,
    user_id INT NOT NULL,
    first_name VARCHAR(50),
    last_name VARCHAR(50),
    email VARCHAR(50),
    CONSTRAINT profile_user_id_fkey FOREIGN KEY (user_id) REFERENCES Users(user_id) ON DELETE CASCADE
);

-- Bands table
CREATE TABLE Bands (
    band_id SERIAL PRIMARY KEY,
    spotify_id VARCHAR(50) UNIQUE,
    name VARCHAR(100) NOT NULL,
    genre VARCHAR(50),
    country VARCHAR(50) NOT NULL
);

-- Albums table
CREATE TABLE Albums (
    album_id SERIAL PRIMARY KEY,
    band_id INT NOT NULL,
    spotify_id VARCHAR(50) UNIQUE,
    title VARCHAR(100) NOT NULL,
    CONSTRAINT albums_band_id_fkey FOREIGN KEY (band_id) REFERENCES Bands(band_id) ON DELETE CASCADE
);

-- Events table
CREATE TABLE Events (
    event_id SERIAL PRIMARY KEY,
    band_id INT NOT NULL,
    name VARCHAR(100) NOT NULL UNIQUE,
    date DATE NOT NULL CHECK (date >= CURRENT_DATE),
    venue VARCHAR(100) NOT NULL,
    city VARCHAR(100) NOT NULL,
    country_code VARCHAR(2) NOT NULL CHECK (LENGTH(country_code) = 2),
    min_price DECIMAL(10, 2) NOT NULL CHECK (min_price >= 0),
    max_price DECIMAL(10, 2) NOT NULL CHECK (max_price >= min_price),
    info TEXT,
    CONSTRAINT events_band_id_fkey FOREIGN KEY (band_id) REFERENCES Bands(band_id) ON DELETE CASCADE
);

-- User favorites table
CREATE TABLE User_favorites (
    user_id INT NOT NULL,
    band_id INT NOT NULL,
    rating INT CHECK (rating >= 1 AND rating <= 5),
    band_name VARCHAR(50) NOT NULL,
    PRIMARY KEY (user_id, band_id),
    CONSTRAINT user_favorites_user_id_fkey FOREIGN KEY (user_id) REFERENCES Users(user_id) ON DELETE CASCADE,
    CONSTRAINT user_favorites_band_id_fkey FOREIGN KEY (band_id) REFERENCES Bands(band_id) ON DELETE CASCADE
);

-- *************************************************************************************************
-- Insert some sample starting data
-- *************************************************************************************************

-- Users (passwords are BCrypt hashed; all passwords = "password")
INSERT INTO Users (username, password_hash, role) VALUES
    ('user',     '$2a$10$tmxuYYg1f5T0eXsTPlq/V.DJUKmRHyFbJ.o.liI1T35TFbjs2xiem', 'ROLE_USER'),
    ('admin',    '$2a$10$tmxuYYg1f5T0eXsTPlq/V.DJUKmRHyFbJ.o.liI1T35TFbjs2xiem', 'ROLE_ADMIN'),
    ('pkonmann0','$2a$04$O0I7kwvTNJNNXbooU8nEuOTTvyJ0JgZ6suyp8a69aS1gVqa.un28a', 'ROLE_ADMIN'),
    ('bguerri1', '$2a$04$sCl8fiUo0mfz/jPU90BC9OKuAB5lWz7TI4dGgCRTlHTY.JYugy3J6', 'ROLE_USER'),
    ('ltallon2', '$2a$04$m08heuGhs2I3jqaN9Mkjn.f2y5X4euIlIU/mC6QXufCkdepKfoXHy', 'ROLE_USER'),
    ('egraeser3','$2a$04$d7TSauH03QNRxzBGKNWaN.qyK79QxjpKTTscR3/LL9GIvYT/p5sX.', 'ROLE_ADMIN'),
    ('iprivett4','$2a$04$Mi3/0HNCqBMkm6xGXEGr3OrTqzttUBwBKKfjzc7vLpTekMLEgaftW', 'ROLE_GUEST');

-- Profiles
INSERT INTO Profile (user_id, first_name, last_name, email) VALUES
    (5, 'Katrinka', 'Wilding', 'kwilding0@ning.com'),
    (1, 'Amelina',  'Kisbey',  'akisbey1@usatoday.com'),
    (4, 'Arie',     'Bowe',    'abowe2@exblog.jp'),
    (3, 'Dyann',    'Reiners', 'dreiners3@cnbc.com'),
    (2, 'Alphonso', 'Macklin', 'amacklin4@pen.io');

-- Bands
INSERT INTO Bands (spotify_id, name, genre, country) VALUES
    ('6rqhFgbbKwnb9MLmUQDhG6', 'Behemoth', 'Death Metal', 'Poland'),
    ('5lyASzv7wP4OMfUVC3vDqQ', 'Cannibal Corpse', 'Death Metal', 'USA'),
    ('2DaxqgrOhkeH0fpeiQq2f4', 'Death', 'Death Metal', 'USA');

-- Albums
INSERT INTO Albums (band_id, spotify_id, title) VALUES
    (1, '1Bd8P8H6p6wY6Zuk9H7LwH', 'I Loved You at Your Darkest'),
    (2, '2J7BvpXxXETXLlDGd4X8rG', 'Tomb of the Mutilated'),
    (3, '6RODCQVKZqLfZdgDzOz6fo', 'Symbolic');

-- Events
INSERT INTO Events (band_id, name, date, venue, city, country_code, min_price, max_price) VALUES
    (2, 'Maryland Deathfest',     '2025-11-12', 'Ram''s Head',          'Baltimore',  'US', 55, 55),
    (3, 'Killtown Deathfest',     '2025-09-21', 'Newport Music Hall',  'Copenhagen', 'DK', 40, 40),
    (1, 'Fire in the Mountains',  '2025-07-23', 'King of Clubs',       'Kyoto',      'JP', 35, 35);

-- User Favorites
INSERT INTO User_favorites (user_id, band_id, rating, band_name) VALUES
    (5, 1, 5, 'Behemoth'),
    (3, 2, 4, 'Cannibal Corpse'),
    (2, 3, 5, 'Death'),
    (4, 1, 4, 'Behemoth'),
    (1, 2, 4, 'Cannibal Corpse');

COMMIT TRANSACTION;
