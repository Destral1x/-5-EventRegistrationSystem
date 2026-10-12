-- sākuma dati

-- admins, parole admin123
INSERT INTO LIETOTAJI (vards, uzvards, lietotajvards, talrunis, parole_hash, loma)
VALUES ('Sistēmas', 'Administrators', 'admin', NULL,
        '240be518fabd2724ddb6f04eeb1da5967448d7e831c08c8fa822809f74c720a9', 'ADMIN');

-- testa lietotājs, parole admin123
INSERT INTO LIETOTAJI (vards, uzvards, lietotajvards, talrunis, parole_hash, loma)
VALUES ('Jānis', 'Bērziņš', 'janis@example.com', '+37120000000',
        '240be518fabd2724ddb6f04eeb1da5967448d7e831c08c8fa822809f74c720a9', 'DALIBNIEKS');

-- pasākumi
INSERT INTO PASAKUMI (nosaukums, apraksts, datums, laiks, vieta, maks_dalibnieku_skaits, izveidotajs_id)
VALUES ('Java programmēšanas seminārs', 'Ievads Java Swing un JDBC.', '2026-11-20', '10:00:00',
        'DTTT, 205. kabinets', 25, 1);

INSERT INTO PASAKUMI (nosaukums, apraksts, datums, laiks, vieta, maks_dalibnieku_skaits, izveidotajs_id)
VALUES ('Datu bāzu meistarklase', 'Praktiska nodarbība ar Apache Derby.', '2026-12-05', '14:30:00',
        'DTTT, aktu zāle', 15, 1);

INSERT INTO PASAKUMI (nosaukums, apraksts, datums, laiks, vieta, maks_dalibnieku_skaits, izveidotajs_id)
VALUES ('Programmēšanas sacensības', 'Komandu sacensības algoritmos.', '2027-02-14', '09:00:00',
        'Daugavpils, Vienības nams', 3, 1);

-- viens pieteikums
INSERT INTO REGISTRACIJAS (pasakuma_id, lietotaja_id, statuss) VALUES (1, 2, 'GAIDA');
