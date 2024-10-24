drop table if exists beer_audit;

CREATE TABLE beer_audit (
    audit_id            VARCHAR(36) NOT NULL PRIMARY KEY,
    id                  VARCHAR(36) NOT NULL,
    version             BIGINT      DEFAULT NULL,
    beer_name           VARCHAR(50) NOT NULL,
    beer_style          TINYINT     NOT NULL CHECK (beer_style BETWEEN 0 AND 9),
    upc                 VARCHAR(255) NOT NULL,
    quantity_on_hand    INTEGER,
    price               DECIMAL(38,2) NOT NULL,
    created_date        DATETIME(6),
    update_date         DATETIME(6),
    created_date_audit  DATETIME(6),
    principal_name      VARCHAR(255),
    audit_event_type    VARCHAR(255)
) ENGINE = InnoDB;