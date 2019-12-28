CREATE TABLE airport(id INTEGER PRIMARY KEY AUTOINCREMENT, 
                        nev VARCHAR(100) NOT NULL UNIQUE,
                        rovidites VARCHAR(3) NOT NULL UNIQUE);
INSERT INTO airport (id, nev, rovidites) VALUES (1, 'Budapest', 'BUD');
INSERT INTO airport (id, nev, rovidites) VALUES (2, 'London', 'LHR');
INSERT INTO airport (id, nev, rovidites) VALUES (3, 'Párizs', 'CDG');

CREATE TABLE foglalas(id INTEGER PRIMARY KEY AUTOINCREMENT, 
                        jarat_id INTEGER NOT NULL REFERENCES jarat ON UPDATE cascade ON DELETE restrict, 
                        user_id INTEGER NOT NULL REFERENCES user ON UPDATE cascade ON DELETE restrict,
                        ules VARCHAR(4) NOT NULL);

CREATE TABLE jarat(id INTEGER PRIMARY KEY AUTOINCREMENT,
                    utvonal_id INTEGER NOT NULL REFERENCES utvonal ON UPDATE cascade ON DELETE restrict,
                    helyek_szama INTEGER NOT NULL,
                    idopont DATETIME NOT NULL);

INSERT INTO jarat (id, utvonal_id, helyek_szama, idopont) VALUES (1, 1, 120, '2020-03-15 08:00:00');
INSERT INTO jarat (id, utvonal_id, helyek_szama, idopont) VALUES (2, 2, 120, '2020-03-15 14:00:00');
INSERT INTO jarat (id, utvonal_id, helyek_szama, idopont) VALUES (3, 3, 120, '2020-04-15 09:00:00');
INSERT INTO jarat (id, utvonal_id, helyek_szama, idopont) VALUES (4, 4, 120, '2020-04-15 15:00:00');
INSERT INTO jarat (id, utvonal_id, helyek_szama, idopont) VALUES (5, 1, 120, '2020-05-15 08:00:00');
INSERT INTO jarat (id, utvonal_id, helyek_szama, idopont) VALUES (6, 2, 120, '2020-05-15 14:00:00');
INSERT INTO jarat (id, utvonal_id, helyek_szama, idopont) VALUES (7, 3, 120, '2020-06-15 09:00:00');
INSERT INTO jarat (id, utvonal_id, helyek_szama, idopont) VALUES (8, 4, 120, '2020-06-15 15:00:00');

CREATE TABLE utvonal(id INTEGER PRIMARY KEY AUTOINCREMENT,
                        indulas_id INTEGER NOT NULL REFERENCES airport ON UPDATE cascade ON DELETE restrict,
                        celallomas_id INTEGER NOT NULL REFERENCES airport ON UPDATE cascade ON DELETE restrict,
                        idotartam time NOT NULL);

INSERT INTO utvonal (id, indulas_id, celallomas_id, idotartam) VALUES (1, 1, 2, '02:45:00');
INSERT INTO utvonal (id, indulas_id, celallomas_id, idotartam) VALUES (2, 2, 1, '02:45:00');
INSERT INTO utvonal (id, indulas_id, celallomas_id, idotartam) VALUES (3, 1, 3, '02:25:00');
INSERT INTO utvonal (id, indulas_id, celallomas_id, idotartam) VALUES (4, 3, 1, '02:25:00');

CREATE TABLE user(id INTEGER PRIMARY KEY autoincrement,
                    username  VARCHAR(255) NOT NULL,
                    email     VARCHAR(255) NOT NULL,
                    firstname VARCHAR(255) NOT NULL,
                    lastname  VARCHAR(255) NOT NULL,
                    birthdate DATE NOT NULL,
                    password  TEXT NOT NULL);
