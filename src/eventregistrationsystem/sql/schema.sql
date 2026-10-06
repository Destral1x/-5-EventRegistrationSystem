--  Pasākumu reģistrācijas sistēma — datu bāzes struktūra
--  Skripts tiek izpildīts automātiski TIKAI pirmajā palaišanas reizē.

-- Lietotāji (administratori un dalībnieki)
CREATE TABLE LIETOTAJI (
    id             INT          NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1),
    vards          VARCHAR(50)  NOT NULL,
    uzvards        VARCHAR(50)  NOT NULL,
    lietotajvards  VARCHAR(50)  NOT NULL,
    talrunis       VARCHAR(20),
    parole_hash    VARCHAR(64)  NOT NULL,
    loma           VARCHAR(15)  NOT NULL DEFAULT 'DALIBNIEKS',
    izveidots      TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT pk_lietotaji PRIMARY KEY (id),
    CONSTRAINT uq_lietotajvards UNIQUE (lietotajvards),
    CONSTRAINT ck_loma CHECK (loma IN ('ADMIN', 'DALIBNIEKS'))
);

-- Pasākumi
CREATE TABLE PASAKUMI (
    id                      INT           NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1),
    nosaukums               VARCHAR(100)  NOT NULL,
    apraksts                VARCHAR(500),
    datums                  DATE          NOT NULL,
    laiks                   TIME          NOT NULL,
    vieta                   VARCHAR(100)  NOT NULL,
    maks_dalibnieku_skaits  INT           NOT NULL,
    izveidotajs_id          INT           NOT NULL,
    CONSTRAINT pk_pasakumi PRIMARY KEY (id),
    CONSTRAINT ck_maks_skaits CHECK (maks_dalibnieku_skaits > 0),
    CONSTRAINT fk_pasakumi_izveidotajs FOREIGN KEY (izveidotajs_id) REFERENCES LIETOTAJI (id)
);

-- Dalības pieteikumi (reģistrācijas)
CREATE TABLE REGISTRACIJAS (
    id                    INT          NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1),
    pasakuma_id           INT          NOT NULL,
    lietotaja_id          INT          NOT NULL,
    statuss               VARCHAR(15)  NOT NULL DEFAULT 'GAIDA',
    registracijas_laiks   TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT pk_registracijas PRIMARY KEY (id),
    CONSTRAINT uq_pasakums_lietotajs UNIQUE (pasakuma_id, lietotaja_id),
    CONSTRAINT ck_statuss CHECK (statuss IN ('GAIDA', 'APSTIPRINATS', 'NORAIDITS')),
    CONSTRAINT fk_reg_pasakums FOREIGN KEY (pasakuma_id) REFERENCES PASAKUMI (id) ON DELETE CASCADE,
    CONSTRAINT fk_reg_lietotajs FOREIGN KEY (lietotaja_id) REFERENCES LIETOTAJI (id) ON DELETE CASCADE
);

-- Indeksi biežākajiem vaicājumiem
CREATE INDEX idx_pasakumi_datums ON PASAKUMI (datums);
CREATE INDEX idx_reg_statuss ON REGISTRACIJAS (pasakuma_id, statuss);
