CREATE TABLE IF NOT EXISTS equipamento (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(255) NOT NULL,
    codigo_tag VARCHAR(100) NOT NULL UNIQUE,
    setor VARCHAR(255) NOT NULL,
    status VARCHAR(50) NOT NULL
);

CREATE TABLE IF NOT EXISTS tecnico (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    especialidade VARCHAR(255) NOT NULL,
    ativo BOOLEAN NOT NULL DEFAULT TRUE
);

CREATE TABLE IF NOT EXISTS ordem_servico (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    equipamento_id BIGINT NOT NULL,
    tecnico_id BIGINT,
    descricao_problema TEXT NOT NULL,
    descricao_solucao TEXT,
    prioridade VARCHAR(50) NOT NULL,
    status VARCHAR(50) NOT NULL,
    data_abertura DATETIME NOT NULL,
    data_inicio DATETIME,
    data_conclusao DATETIME,
    CONSTRAINT fk_os_equipamento FOREIGN KEY (equipamento_id) REFERENCES equipamento(id),
    CONSTRAINT fk_os_tecnico FOREIGN KEY (tecnico_id) REFERENCES tecnico(id)
);