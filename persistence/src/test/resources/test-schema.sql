DROP TABLE IF EXISTS certificate;

create TABLE certificate
(
    id               INT AUTO_INCREMENT NOT NULL PRIMARY KEY,
    name             VARCHAR(255),
    description      VARCHAR(255),
    price            INT,
    duration         INT,
    create_date      DATE,
    last_update_date DATE
);

DROP TABLE IF EXISTS tag;

create TABLE tag
(
    id   INT AUTO_INCREMENT NOT NULL PRIMARY KEY,
    name VARCHAR(255) NOT NULL UNIQUE
);

DROP TABLE IF EXISTS certificate_tag;

create TABLE certificate_tag
(
    id_tag         INT NOT NULL ,
    id_certificate INT NOT NULL ,

    CONSTRAINT pk_certificate_tag PRIMARY KEY (
        id_tag, id_certificate
    ),
    FOREIGN KEY (id_tag) REFERENCES tag (id) ON DELETE CASCADE,
    FOREIGN KEY (id_certificate) REFERENCES certificate (id) ON DELETE CASCADE
);