drop table if exists db_springboot.category;
drop table if exists db_springboot.beer_category;

CREATE TABLE db_springboot.category
(
    id                 varchar(36) NOT NULL PRIMARY KEY,
    description        varchar(50),
    created_date       timestamp,
    last_modified_date datetime(6)  DEFAULT NULL,
    version            bigint       DEFAULT NULL
) ENGINE = InnoDB;

CREATE TABLE db_springboot.beer_category
(
    beer_id            varchar(36) NOT NULL,
    category_id        varchar(36) NOT NULL,
    PRIMARY KEY (beer_id, category_id),
    CONSTRAINT pc_beer_id_fk FOREIGN KEY (beer_id) REFERENCES db_springboot.beer (id),
    CONSTRAINT pc_category_id_fk FOREIGN KEY (category_id) REFERENCES category (id)
) ENGINE = InnoDB;
