CREATE TABLE pessoa (
	codigo BIGINT(20) PRIMARY KEY AUTO_INCREMENT,
	nome VARCHAR(50) NOT NULL,
	logradouro VARCHAR(50),
	numero VARCHAR(5),
	complemento VARCHAR(50),
	bairro VARCHAR(20),
	cep VARCHAR(9),
	cidade VARCHAR(20),
	estado VARCHAR(20),
	ativo BOOLEAN NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

INSERT INTO pessoa 
(nome, logradouro, numero, complemento, bairro, cep, cidade, estado, ativo) 
VALUES 
('Ranyery', 'Rua Matilde', '1337', 'Casa 3', 'Jardins', '06777-999', 'São Paulo', 'São Paulo', true),
('Santos', 'Rua Adelaide', '1338', 'Casa 2', 'Vila Creti', '06777-888', 'Osasco', 'São Paulo', true),
('Coutinho', 'Rua Clemilde', '1339', 'Casa 1', 'Jd. Rio Verde', '06777-777', 'Vila Maria', 'Bahia', true);