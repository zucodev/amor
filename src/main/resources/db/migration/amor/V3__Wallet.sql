CREATE SEQUENCE seq_wallet
START WITH 1
INCREMENT BY 1
NO MINVALUE
NO MAXVALUE
CACHE 1;


CREATE TABLE wallet (
  id           BIGINT DEFAULT nextval('seq_wallet' :: REGCLASS) NOT NULL,
  version      BIGINT                                           NOT NULL,
  date_created TIMESTAMP WITHOUT TIME ZONE                      NOT NULL,
  last_updated TIMESTAMP WITHOUT TIME ZONE                      NOT NULL,
  user_id      BIGINT,
  address      CHARACTER VARYING(1000)                          NOT NULL,
  test         BOOLEAN                                          NOT NULL,
  attributes   JSON
);

ALTER TABLE ONLY wallet
  ADD CONSTRAINT doc_pkey PRIMARY KEY (id);

ALTER TABLE ONLY wallet
  ADD CONSTRAINT wallet_user_fkey FOREIGN KEY (user_id) REFERENCES app_user (id);

ALTER TABLE ONLY wallet
  ADD CONSTRAINT uk_wallet_address UNIQUE (address);


SELECT pg_catalog.setval('seq_wallet', 1, FALSE);