INSERT INTO client (nom_client, email_client) VALUES
                                                  ('Alice', 'alice@mail.com'),
                                                  ('Bob', 'bob@mail.com');

INSERT INTO compte (code_compte, date_creation, solde, CODE_CLIENT, TYPE_COMPTE, decouvert, taux)
VALUES
    ('C1', CURRENT_DATE, 1500, 1, 'CC', 500, NULL),
    ('C2', CURRENT_DATE, 2500, 2, 'CE', NULL, 0.04);
