-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION';

-- -----------------------------------------------------
-- Schema mercado_varejo
-- -----------------------------------------------------
DROP SCHEMA IF EXISTS `mercado_varejo` ;

-- -----------------------------------------------------
-- Schema mercado_varejo
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `mercado_varejo` ;
USE `mercado_varejo` ;

-- -----------------------------------------------------
-- Table `mercado_varejo`.`pessoa`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `mercado_varejo`.`pessoa` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `nome` VARCHAR(1024) NOT NULL,
  `cpf` CHAR(11) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `cpf_UNIQUE` (`cpf` ASC))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `mercado_varejo`.`telefone`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `mercado_varejo`.`telefone` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `numero` VARCHAR(20) NOT NULL,
  `pessoa_id` INT NOT NULL,
  PRIMARY KEY (`id`, `pessoa_id`),
  INDEX `fk_telefone_pessoa_idx` (`pessoa_id` ASC),
  CONSTRAINT `fk_telefone_pessoa`
    FOREIGN KEY (`pessoa_id`)
    REFERENCES `mercado_varejo`.`pessoa` (`id`)
    ON DELETE CASCADE
    ON UPDATE CASCADE)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `mercado_varejo`.`cliente`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `mercado_varejo`.`cliente` (
  `pessoa_id` INT NOT NULL,
  PRIMARY KEY (`pessoa_id`),
  CONSTRAINT `fk_cliente_pessoa1`
    FOREIGN KEY (`pessoa_id`)
    REFERENCES `mercado_varejo`.`pessoa` (`id`)
    ON DELETE CASCADE
    ON UPDATE CASCADE)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `mercado_varejo`.`funcionario`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `mercado_varejo`.`funcionario` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `pessoa_id` INT NOT NULL,
  `salario` DECIMAL(10,2) NOT NULL,
  PRIMARY KEY (`id`, `pessoa_id`),
  INDEX `fk_funcionario_pessoa1_idx` (`pessoa_id` ASC),
  CONSTRAINT `fk_funcionario_pessoa1`
    FOREIGN KEY (`pessoa_id`)
    REFERENCES `mercado_varejo`.`pessoa` (`id`)
    ON DELETE CASCADE
    ON UPDATE CASCADE)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `mercado_varejo`.`venda`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `mercado_varejo`.`venda` (
  `id` INT UNSIGNED NOT NULL AUTO_INCREMENT,
  `cliente_id` INT NULL,
  `funcionario_id` INT NULL,
  `data_hora` DATETIME NOT NULL,
  `comissao` DECIMAL(3,3) UNSIGNED NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `fk_venda_cliente1_idx` (`cliente_id` ASC),
  INDEX `fk_venda_funcionario1_idx` (`funcionario_id` ASC),
  CONSTRAINT `fk_venda_cliente1`
    FOREIGN KEY (`cliente_id`)
    REFERENCES `mercado_varejo`.`cliente` (`pessoa_id`)
    ON DELETE SET NULL
    ON UPDATE CASCADE,
  CONSTRAINT `fk_venda_funcionario1`
    FOREIGN KEY (`funcionario_id`)
    REFERENCES `mercado_varejo`.`funcionario` (`id`)
    ON DELETE SET NULL
    ON UPDATE CASCADE)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `mercado_varejo`.`produto`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `mercado_varejo`.`produto` (
  `id` INT UNSIGNED NOT NULL AUTO_INCREMENT,
  `nome` VARCHAR(1024) NOT NULL,
  `qtd_estoque` INT UNSIGNED NOT NULL,
  `preco_atual` DECIMAL(10,2) NOT NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `mercado_varejo`.`item_produto`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `mercado_varejo`.`item_produto` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `venda_id` INT UNSIGNED NULL,
  `produto_id` INT UNSIGNED NULL,
  `qtd` INT UNSIGNED NOT NULL,
  `preco_praticado` DECIMAL(10,2) UNSIGNED NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `fk_item_produto_venda1_idx` (`venda_id` ASC),
  INDEX `fk_item_produto_produto1_idx` (`produto_id` ASC),
  CONSTRAINT `fk_item_produto_venda1`
    FOREIGN KEY (`venda_id`)
    REFERENCES `mercado_varejo`.`venda` (`id`)
    ON DELETE SET NULL
    ON UPDATE CASCADE,
  CONSTRAINT `fk_item_produto_produto1`
    FOREIGN KEY (`produto_id`)
    REFERENCES `mercado_varejo`.`produto` (`id`)
    ON DELETE SET NULL
    ON UPDATE CASCADE)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `mercado_varejo`.`fornecedor`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `mercado_varejo`.`fornecedor` (
  `id` INT UNSIGNED NOT NULL AUTO_INCREMENT,
  `nome_fantasia` VARCHAR(1024) NOT NULL,
  `cnpj` CHAR(14) NOT NULL,
  `telefone_contato` VARCHAR(20) NOT NULL,
  `telefone_telegram` VARCHAR(20) NULL,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `cnpj_UNIQUE` (`cnpj` ASC))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `mercado_varejo`.`fornecimento`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `mercado_varejo`.`fornecimento` (
  `id` INT UNSIGNED NOT NULL AUTO_INCREMENT,
  `fornecedor_id` INT UNSIGNED NULL,
  `produto_id` INT UNSIGNED NULL,
  `qtd` INT UNSIGNED NOT NULL,
  `preco_custo` DECIMAL(10,2) UNSIGNED NOT NULL,
  `data_hora` DATETIME NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `fk_fornecimento_fornecedor1_idx` (`fornecedor_id` ASC),
  INDEX `fk_fornecimento_produto1_idx` (`produto_id` ASC),
  CONSTRAINT `fk_fornecimento_fornecedor1`
    FOREIGN KEY (`fornecedor_id`)
    REFERENCES `mercado_varejo`.`fornecedor` (`id`)
    ON DELETE SET NULL
    ON UPDATE CASCADE,
  CONSTRAINT `fk_fornecimento_produto1`
    FOREIGN KEY (`produto_id`)
    REFERENCES `mercado_varejo`.`produto` (`id`)
    ON DELETE SET NULL
    ON UPDATE CASCADE)
ENGINE = InnoDB;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;

USE mercado_varejo;

-- Povoamento

-- ==========================================================
-- 1. PESSOAS (25 Registros)
-- ==========================================================
INSERT INTO pessoa (nome, cpf) VALUES 
('Saitama Careca', '10000000001'),       
('Genos Ciborgue', '10000000002'),       
('Kratos de Esparta', '20000000001'),    
('Atreus Boy', '20000000002'),           
('Johnny Silverhand', '30000000001'),   
('V Male', '30000000002'),               
('Billy Butcher', '40000000001'),        
('Homelander', '40000000002'),           
('Izuku Midoriya', '50000000001'),       
('All Might', '50000000002'),            
('Sonic the Hedgehog', '60000000001'),  
('Dr. Eggman', '60000000002'),           
('Guts Black Swordsman', '70000000001'), 
('Griffith', '70000000002'),             
('Tony Stark', '80000000001'),           
('Peter Parker', '80000000002'),         
('Bruce Wayne', '90000000001'),          
('Clark Kent', '90000000002'),           
('Walter White', '11122233344'),         
('Jesse Pinkman', '11122233355'),        
('Naruto Uzumaki', '12312312301'),       
('Sasuke Uchiha', '12312312302'),        
('Geralt de Rivia', '99988877701'),      
('Yennefer de Vengerberg', '99988877702'), 
('Ciri', '99988877703');                 

-- ==========================================================
-- 2. CLIENTES (15 Registros)
-- ==========================================================
-- Lembrando: cliente.pessoa_id é PK e FK ao mesmo tempo
INSERT INTO cliente (pessoa_id) VALUES 
(1), (3), (5), (7), (9), (11), (13), (15), (17), (19), (21), (23), (25), (2), (4);

-- ==========================================================
-- 3. FUNCIONÁRIOS (10 Registros)
-- ==========================================================
-- pessoa_id liga à tabela pessoa. O ID do funcionário será gerado auto (1 a 10)
INSERT INTO funcionario (pessoa_id, salario) VALUES 
(16, 1500.00),  
(20, 2500.00),  
(2, 3500.00),   
(6, 4000.00),  
(14, 15000.00), 
(8, 20000.00), 
(12, 8500.00),  
(18, 2200.00),  
(24, 6000.00),  
(10, 1000.00);  

-- ==========================================================
-- 4. TELEFONES (12 Registros)
-- ==========================================================
INSERT INTO telefone (numero, pessoa_id) VALUES 
('(11) 90001-0001', 1),
('(11) 90002-0002', 3),
('(21) 90003-0003', 5),
('(55) 90004-0004', 7),
('(81) 90005-0005', 15),
('(11) 90006-0006', 16),
('(31) 90007-0007', 19),
('(31) 90008-0008', 20),
('(41) 90009-0009', 23),
('(11) 91010-1010', 8),
('(11) 91111-1111', 12),
('(21) 91212-1212', 17);

-- ==========================================================
-- 5. FORNECEDORES (10 Registros)
-- ==========================================================
INSERT INTO fornecedor (nome_fantasia, cnpj, telefone_contato, telefone_telegram) VALUES 
('Capsule Corp', '00000001000101', '(11) 3333-4444', '@bulma_capsule'),
('Stark Industries', '00000002000102', '(21) 5555-6666', NULL),
('Wayne Enterprises', '00000003000103', '(11) 7777-8888', '@alfred_wayne'),
('Umbrella Corp', '00000004000104', '(66) 6666-6666', NULL),
('Arasaka Corp', '00000005000105', '(77) 2077-2077', '@arasaka_official'),
('Vought International', '00000006000106', '(11) 1234-5678', '@vought_pr'),
('U.A. High Supply', '00000007000107', '(81) 1000-1000', NULL),
('Shinra Electric', '00000008000108', '(11) 9999-0000', '@rufus_shinra'),
('Cyberdyne Systems', '00000009000109', '(99) 0101-0101', '@skynet'),
('Los Pollos Hermanos', '00000010000110', '(50) 5050-5050', '@gustavo_fring');

-- ==========================================================
-- 6. PRODUTOS (15 Itens Registros)
-- ==========================================================
INSERT INTO produto (nome, qtd_estoque, preco_atual) VALUES 
('Leite Longa Vida 1L', 500, 5.50),
('Poção de Cura (HP)', 100, 50.00),
('Poção de Mana (MP)', 100, 55.00),
('Ramen Instantâneo Ichiraku', 200, 8.90),
('Composto V (Dose)', 5, 50000.00),
('Nuka-Cola Quantum', 60, 15.00),
('Batarangue de Borracha', 30, 25.00),
('Teia de Aranha (Refil)', 150, 12.00),
('Cristal Kyber (Azul)', 10, 500.00),
('Espada de Prata (Réplica)', 8, 300.00),
('Blue Meth (Doce Azul)', 500, 45.00),
('Braço Mecânico Usado', 2, 1200.00),
('Anel do Poder (Bijuteria)', 50, 19.90),
('Semente dos Deuses', 20, 100.00),
('Chili Dog do Sonic', 80, 12.50);

-- ==========================================================
-- 7. FORNECIMENTOS (12 Registros)
-- ==========================================================
INSERT INTO fornecimento (fornecedor_id, produto_id, qtd, preco_custo, data_hora) VALUES 
(1, 3, 50, 30.00, '2023-11-01 08:00:00'),   
(10, 11, 200, 20.00, '2023-11-02 09:00:00'), 
(6, 5, 10, 30000.00, '2023-11-03 10:00:00'), 
(2, 8, 100, 5.00, '2023-11-04 11:00:00'),    
(7, 4, 100, 4.00, '2023-11-05 12:00:00'),    
(3, 7, 50, 10.00, '2023-11-06 13:00:00'),    
(9, 12, 5, 600.00, '2023-11-07 14:00:00'),
(10, 1, 100, 2.50, '2023-11-08 15:00:00'),  
(5, 12, 2, 550.00, '2023-11-09 16:00:00'), 
(8, 2, 60, 25.00, '2023-11-10 17:00:00'),   
(1, 14, 30, 40.00, '2023-11-11 18:00:00'),   
(4, 2, 50, 22.00, '2023-11-12 19:00:00');    

-- ==========================================================
-- 8. VENDAS (12 Vendas)
-- ==========================================================
-- cliente_id (Pessoa ID) | funcionario_id (Funcionario ID 1-10)
INSERT INTO venda (cliente_id, funcionario_id, data_hora, comissao) VALUES 
(1, 1, '2023-11-20 10:00:00', 0.050),  
(3, 2, '2023-11-20 11:00:00', 0.050), 
(5, 3, '2023-11-20 12:00:00', 0.100),  
(7, 6, '2023-11-21 13:00:00', 0.020),  
(9, 10, '2023-11-21 14:00:00', 0.000),
(11, 7, '2023-11-21 15:00:00', 0.050),
(13, 9, '2023-11-22 16:00:00', 0.050), 
(15, 1, '2023-11-22 17:00:00', 0.050), 
(17, 8, '2023-11-23 18:00:00', 0.010),
(19, 2, '2023-11-23 19:00:00', 0.050), 
(23, 9, '2023-11-24 20:00:00', 0.050),
(25, 4, '2023-11-24 21:00:00', 0.050);

-- ==========================================================
-- 9. ITENS VENDIDOS (Detalhando as vendas acima)
-- ==========================================================
INSERT INTO item_produto (venda_id, produto_id, qtd, preco_praticado) VALUES 
(1, 4, 10, 8.90),
(1, 1, 2, 4.50),

(2, 10, 1, 300.00),
(2, 2, 5, 50.00),

(3, 12, 1, 1200.00),
(3, 6, 2, 15.00),

(4, 5, 1, 50000.00),

(5, 2, 3, 50.00),
(5, 4, 5, 8.90),

(6, 15, 10, 12.50),

(7, 2, 10, 50.00),
(7, 3, 5, 55.00),

(8, 9, 2, 500.00),
(8, 12, 1, 1200.00),

(9, 7, 20, 25.00),
(9, 8, 5, 12.00),

(10, 11, 50, 45.00),
(10, 3, 2, 55.00),

(11, 2, 4, 50.00),
(11, 3, 4, 55.00),

(12, 10, 1, 300.00),
(12, 13, 1, 19.90);


