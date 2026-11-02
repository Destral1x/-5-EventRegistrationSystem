-- sākuma (testa) dati programmas funkciju pārbaudei

-- lietotāji: administrators (parole admin123) un 10 dalībnieki (parole parole123)
-- ielogoties var gan ar lietotājvārdu (piem., janis), gan ar e-pastu (janis@example.com)
-- dažiem dalībniekiem nav tālruņa - tad to paprasa, piesakoties pasākumam
INSERT INTO LIETOTAJI (vards, uzvards, lietotajvards, epasts, talrunis, parole_hash, loma, izveidots)
VALUES ('Sistēmas', 'Administrators', 'admin', 'admin@example.com', NULL,
        '240be518fabd2724ddb6f04eeb1da5967448d7e831c08c8fa822809f74c720a9', 'ADMIN', '2026-09-01 09:00:00');
INSERT INTO LIETOTAJI (vards, uzvards, lietotajvards, epasts, talrunis, parole_hash, loma, izveidots)
VALUES ('Jānis', 'Bērziņš', 'janis', 'janis@example.com', '+371 20000000',
        '395026a9fa2cc2e48d86461478873f6c15bdbc127986a8b92c4a83526a4d08cd', 'DALIBNIEKS', '2026-09-15 10:12:00');
INSERT INTO LIETOTAJI (vards, uzvards, lietotajvards, epasts, talrunis, parole_hash, loma, izveidots)
VALUES ('Anna', 'Kalniņa', 'anna', 'anna@example.com', '+371 26000000',
        '395026a9fa2cc2e48d86461478873f6c15bdbc127986a8b92c4a83526a4d08cd', 'DALIBNIEKS', '2026-09-18 18:40:00');
INSERT INTO LIETOTAJI (vards, uzvards, lietotajvards, epasts, talrunis, parole_hash, loma, izveidots)
VALUES ('Pēteris', 'Ozols', 'peteris', 'peteris@example.com', NULL,
        '395026a9fa2cc2e48d86461478873f6c15bdbc127986a8b92c4a83526a4d08cd', 'DALIBNIEKS', '2026-09-20 12:05:00');
INSERT INTO LIETOTAJI (vards, uzvards, lietotajvards, epasts, talrunis, parole_hash, loma, izveidots)
VALUES ('Laura', 'Liepiņa', 'laura', 'laura@example.com', '+371 29111222',
        '395026a9fa2cc2e48d86461478873f6c15bdbc127986a8b92c4a83526a4d08cd', 'DALIBNIEKS', '2026-09-25 08:30:00');
INSERT INTO LIETOTAJI (vards, uzvards, lietotajvards, epasts, talrunis, parole_hash, loma, izveidots)
VALUES ('Mārtiņš', 'Kļaviņš', 'martins', 'martins@example.com', '+371 27333444',
        '395026a9fa2cc2e48d86461478873f6c15bdbc127986a8b92c4a83526a4d08cd', 'DALIBNIEKS', '2026-10-01 14:15:00');
INSERT INTO LIETOTAJI (vards, uzvards, lietotajvards, epasts, talrunis, parole_hash, loma, izveidots)
VALUES ('Elīna', 'Zariņa', 'elina', 'elina@example.com', '+371 22555666',
        '395026a9fa2cc2e48d86461478873f6c15bdbc127986a8b92c4a83526a4d08cd', 'DALIBNIEKS', '2026-10-03 19:00:00');
INSERT INTO LIETOTAJI (vards, uzvards, lietotajvards, epasts, talrunis, parole_hash, loma, izveidots)
VALUES ('Kristaps', 'Vītols', 'kristaps', 'kristaps@example.com', NULL,
        '395026a9fa2cc2e48d86461478873f6c15bdbc127986a8b92c4a83526a4d08cd', 'DALIBNIEKS', '2026-10-07 11:45:00');
INSERT INTO LIETOTAJI (vards, uzvards, lietotajvards, epasts, talrunis, parole_hash, loma, izveidots)
VALUES ('Sintija', 'Krūmiņa', 'sintija', 'sintija@example.com', '+371 25777888',
        '395026a9fa2cc2e48d86461478873f6c15bdbc127986a8b92c4a83526a4d08cd', 'DALIBNIEKS', '2026-10-12 16:20:00');
INSERT INTO LIETOTAJI (vards, uzvards, lietotajvards, epasts, talrunis, parole_hash, loma, izveidots)
VALUES ('Dmitrijs', 'Ivanovs', 'dmitrijs', 'dmitrijs@example.com', '+371 28999000',
        '395026a9fa2cc2e48d86461478873f6c15bdbc127986a8b92c4a83526a4d08cd', 'DALIBNIEKS', '2026-10-15 09:10:00');
INSERT INTO LIETOTAJI (vards, uzvards, lietotajvards, epasts, talrunis, parole_hash, loma, izveidots)
VALUES ('Olga', 'Petrova', 'olga', 'olga@example.com', '+371 23444555',
        '395026a9fa2cc2e48d86461478873f6c15bdbc127986a8b92c4a83526a4d08cd', 'DALIBNIEKS', '2026-10-20 13:35:00');

-- pasākumi (visus izveidoja administrators, id = 1)
-- "Programmēšanas sacensības" un "Excel padziļināti" - visas vietas aizņemtas
INSERT INTO PASAKUMI (nosaukums, apraksts, datums, laiks, vieta, maks_dalibnieku_skaits, izveidotajs_id)
VALUES ('Java programmēšanas seminārs', 'Ievads Java Swing un JDBC.', '2026-11-20', '10:00:00',
        'DTTT, 205. kabinets', 25, 1);
INSERT INTO PASAKUMI (nosaukums, apraksts, datums, laiks, vieta, maks_dalibnieku_skaits, izveidotajs_id)
VALUES ('Datu bāzu meistarklase', 'Praktiska nodarbība ar Apache Derby.', '2026-12-05', '14:30:00',
        'DTTT, aktu zāle', 15, 1);
INSERT INTO PASAKUMI (nosaukums, apraksts, datums, laiks, vieta, maks_dalibnieku_skaits, izveidotajs_id)
VALUES ('Programmēšanas sacensības', 'Komandu sacensības algoritmos.', '2027-02-14', '09:00:00',
        'Daugavpils, Vienības nams', 3, 1);
INSERT INTO PASAKUMI (nosaukums, apraksts, datums, laiks, vieta, maks_dalibnieku_skaits, izveidotajs_id)
VALUES ('Tīmekļa izstrādes darbnīca', 'HTML, CSS un JavaScript pamati praktiskos uzdevumos.', '2026-11-27', '16:00:00',
        'DTTT, 112. kabinets', 12, 1);
INSERT INTO PASAKUMI (nosaukums, apraksts, datums, laiks, vieta, maks_dalibnieku_skaits, izveidotajs_id)
VALUES ('Kiberdrošības lekcija', 'Kā aizsargāt kontus un datus internetā.', '2026-12-10', '18:00:00',
        'Daugavpils Universitāte, 1. auditorija', 60, 1);
INSERT INTO PASAKUMI (nosaukums, apraksts, datums, laiks, vieta, maks_dalibnieku_skaits, izveidotajs_id)
VALUES ('Ziemassvētku hakatons', '24 stundu komandu projektu maratons.', '2026-12-18', '09:00:00',
        'Daugavpils, Vienības nams', 40, 1);
INSERT INTO PASAKUMI (nosaukums, apraksts, datums, laiks, vieta, maks_dalibnieku_skaits, izveidotajs_id)
VALUES ('Robotikas darbnīca', 'Arduino robota salikšana un programmēšana.', '2027-01-15', '11:00:00',
        'DTTT, 112. kabinets', 10, 1);
INSERT INTO PASAKUMI (nosaukums, apraksts, datums, laiks, vieta, maks_dalibnieku_skaits, izveidotajs_id)
VALUES ('Git un GitHub pamati', 'Versiju kontrole komandas projektos.', '2027-01-22', '15:00:00',
        'DTTT, 205. kabinets', 20, 1);
INSERT INTO PASAKUMI (nosaukums, apraksts, datums, laiks, vieta, maks_dalibnieku_skaits, izveidotajs_id)
VALUES ('Mākslīgā intelekta seminārs', NULL, '2027-03-05', '13:00:00',
        'Rīga, LU Akadēmiskais centrs', 80, 1);
INSERT INTO PASAKUMI (nosaukums, apraksts, datums, laiks, vieta, maks_dalibnieku_skaits, izveidotajs_id)
VALUES ('Excel padziļināti', 'Formulas, rakurstabulas un diagrammas.', '2027-03-19', '10:00:00',
        'DTTT, 301. kabinets', 2, 1);
INSERT INTO PASAKUMI (nosaukums, apraksts, datums, laiks, vieta, maks_dalibnieku_skaits, izveidotajs_id)
VALUES ('Karjeras diena IT nozarē', 'Tikšanās ar IT uzņēmumu pārstāvjiem.', '2027-04-08', '12:00:00',
        'Daugavpils Universitāte, aktu zāle', 100, 1);
INSERT INTO PASAKUMI (nosaukums, apraksts, datums, laiks, vieta, maks_dalibnieku_skaits, izveidotajs_id)
VALUES ('Python iesācējiem', 'Programmēšanas pamati ar Python.', '2027-04-20', '17:00:00',
        'DTTT, 205. kabinets', 15, 1);

-- pieteikumi ar dažādiem statusiem
INSERT INTO REGISTRACIJAS (pasakuma_id, lietotaja_id, statuss) VALUES (1, 2, 'APSTIPRINATS');
INSERT INTO REGISTRACIJAS (pasakuma_id, lietotaja_id, statuss) VALUES (1, 3, 'APSTIPRINATS');
INSERT INTO REGISTRACIJAS (pasakuma_id, lietotaja_id, statuss) VALUES (1, 5, 'GAIDA');
INSERT INTO REGISTRACIJAS (pasakuma_id, lietotaja_id, statuss) VALUES (1, 6, 'GAIDA');
INSERT INTO REGISTRACIJAS (pasakuma_id, lietotaja_id, statuss) VALUES (1, 9, 'NORAIDITS');
INSERT INTO REGISTRACIJAS (pasakuma_id, lietotaja_id, statuss) VALUES (2, 2, 'GAIDA');
INSERT INTO REGISTRACIJAS (pasakuma_id, lietotaja_id, statuss) VALUES (2, 7, 'APSTIPRINATS');
INSERT INTO REGISTRACIJAS (pasakuma_id, lietotaja_id, statuss) VALUES (2, 10, 'APSTIPRINATS');
INSERT INTO REGISTRACIJAS (pasakuma_id, lietotaja_id, statuss) VALUES (3, 5, 'APSTIPRINATS');
INSERT INTO REGISTRACIJAS (pasakuma_id, lietotaja_id, statuss) VALUES (3, 6, 'APSTIPRINATS');
INSERT INTO REGISTRACIJAS (pasakuma_id, lietotaja_id, statuss) VALUES (3, 10, 'APSTIPRINATS');
INSERT INTO REGISTRACIJAS (pasakuma_id, lietotaja_id, statuss) VALUES (3, 11, 'GAIDA');
INSERT INTO REGISTRACIJAS (pasakuma_id, lietotaja_id, statuss) VALUES (4, 3, 'APSTIPRINATS');
INSERT INTO REGISTRACIJAS (pasakuma_id, lietotaja_id, statuss) VALUES (4, 9, 'GAIDA');
INSERT INTO REGISTRACIJAS (pasakuma_id, lietotaja_id, statuss) VALUES (5, 2, 'APSTIPRINATS');
INSERT INTO REGISTRACIJAS (pasakuma_id, lietotaja_id, statuss) VALUES (5, 7, 'APSTIPRINATS');
INSERT INTO REGISTRACIJAS (pasakuma_id, lietotaja_id, statuss) VALUES (5, 11, 'APSTIPRINATS');
INSERT INTO REGISTRACIJAS (pasakuma_id, lietotaja_id, statuss) VALUES (5, 6, 'NORAIDITS');
INSERT INTO REGISTRACIJAS (pasakuma_id, lietotaja_id, statuss) VALUES (6, 5, 'GAIDA');
INSERT INTO REGISTRACIJAS (pasakuma_id, lietotaja_id, statuss) VALUES (6, 10, 'GAIDA');
INSERT INTO REGISTRACIJAS (pasakuma_id, lietotaja_id, statuss) VALUES (7, 9, 'APSTIPRINATS');
INSERT INTO REGISTRACIJAS (pasakuma_id, lietotaja_id, statuss) VALUES (8, 3, 'GAIDA');
INSERT INTO REGISTRACIJAS (pasakuma_id, lietotaja_id, statuss) VALUES (8, 11, 'APSTIPRINATS');
INSERT INTO REGISTRACIJAS (pasakuma_id, lietotaja_id, statuss) VALUES (10, 7, 'APSTIPRINATS');
INSERT INTO REGISTRACIJAS (pasakuma_id, lietotaja_id, statuss) VALUES (10, 2, 'GAIDA');
INSERT INTO REGISTRACIJAS (pasakuma_id, lietotaja_id, statuss) VALUES (11, 6, 'GAIDA');
