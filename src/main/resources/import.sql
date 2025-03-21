-- This file allow to write SQL commands that will be emitted in test and dev.
-- The commands are commented as their support depends of the database
-- insert into myentity (id, field) values(1, 'field-1');
-- insert into myentity (id, field) values(2, 'field-2');
-- insert into myentity (id, field) values(3, 'field-3');
-- alter sequence myentity_seq restart with 4;

DROP TABLE IF EXISTS event;

CREATE TABLE event (
    id SERIAL PRIMARY KEY,
    latitude DOUBLE PRECISION NOT NULL,
    longitude DOUBLE PRECISION NOT NULL,
    type VARCHAR(255) NOT NULL,
    name VARCHAR(255) NOT NULL,
    description TEXT,
    status VARCHAR(50) NOT NULL,
    event_date DATE NOT NULL
);

-- INSERT INTO event (latitude, longitude, type, name, description, status, event_date) VALUES
-- (40.7128, -74.0060, 'Music', 'Jazz Night', 'An evening of smooth jazz', 'Scheduled', '2025-04-10'),
-- (34.0522, -118.2437, 'Tech', 'AI Conference', 'Latest trends in AI technology', 'Scheduled', '2025-05-15'),
-- (51.5074, -0.1278, 'Sports', 'London Marathon', 'Annual city marathon event', 'Scheduled', '2025-06-01'),
-- (48.8566, 2.3522, 'Food', 'Paris Food Festival', 'Taste the best French cuisine', 'Scheduled', '2025-07-20'),
-- (35.6895, 139.6917, 'Culture', 'Tokyo Art Expo', 'Showcasing modern art', 'Scheduled', '2025-08-05'),
-- (37.7749, -122.4194, 'Tech', 'Silicon Valley Startups', 'Meet innovative startups', 'Scheduled', '2025-09-12'),
-- (55.7558, 37.6173, 'Music', 'Moscow Orchestra', 'Classical music performance', 'Scheduled', '2025-10-18'),
-- (41.9028, 12.4964, 'Culture', 'Rome History Tour', 'Explore ancient Rome', 'Scheduled', '2025-11-25'),
-- (39.9042, 116.4074, 'Business', 'Beijing Trade Fair', 'International business fair', 'Scheduled', '2025-12-10'),
-- (-33.8688, 151.2093, 'New Year', 'Sydney Fireworks', 'New Year celebration fireworks', 'Scheduled', '2025-12-31');

INSERT INTO event (latitude, longitude, type, name, description, status, event_date) VALUES
(52.2298, 21.0122, 'muzyka', 'Koncert Rockowy', 'Niesamowite wydarzenie rockowe w centrum miasta', 'Zaplanowane', '2025-04-10'),
(50.0647, 19.9450, 'technologia', 'Konferencja AI', 'Spotkanie ekspertów w dziedzinie sztucznej inteligencji', 'Zaplanowane', '2025-05-15'),
(54.3520, 18.6466, 'sport', 'Maraton Gdański', 'Coroczny maraton ulicami Gdańska', 'Zaplanowane', '2025-06-02'),
(51.1079, 17.0385, 'jedzenie', 'Festiwal Kuchni Polskiej', 'Degustacja tradycyjnych polskich potraw', 'Zaplanowane', '2025-07-20'),
(53.1325, 23.1688, 'kultura', 'Wystawa Sztuki Nowoczesnej', 'Najlepsi artyści prezentują swoje dzieła', 'Zaplanowane', '2025-08-05'),
(50.0497, 22.0084, 'technologia', 'Startup Weekend', 'Spotkanie dla innowacyjnych startupów', 'Zaplanowane', '2025-09-12'),
(52.4064, 16.9252, 'muzyka', 'Koncert Filharmonii', 'Wieczór z klasyczną muzyką', 'Zaplanowane', '2025-10-18'),
(51.7592, 19.4550, 'kultura', 'Zwiedzanie Łodzi', 'Odkrywanie historii i architektury miasta', 'Zaplanowane', '2025-11-25'),
(50.2572, 19.0275, 'biznes', 'Targi Przemysłowe', 'Największe targi technologii przemysłowej w regionie', 'Zaplanowane', '2025-12-10'),
(54.5189, 18.5305, 'rozrywka', 'Pokaz Sztucznych Ogni', 'Impreza sylwestrowa na plaży', 'Zaplanowane', '2025-12-30');
