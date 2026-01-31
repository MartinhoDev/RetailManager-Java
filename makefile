# Variáveis de Configuração
JAVAC = javac
JAVA = java
SRC_DIR = src
BIN_DIR = bin
LIB_DIR = lib

# O nome exato do seu arquivo .jar
DRIVER_JAR = mysql-connector-j-8.3.0.jar

# Configuração do Classpath
# IMPORTANTE: No Linux usa-se ':' para separar. No Windows usa-se ';'
# Como você usa Linux, mantive ':'
CLASSPATH = $(LIB_DIR)/$(DRIVER_JAR):.

# Lista de arquivos fonte
SOURCES = $(wildcard $(SRC_DIR)/*.java)

# Regra Padrão (Compilar tudo)
all:
	@mkdir -p $(BIN_DIR)
	@echo "Compilando..."
	$(JAVAC) -d $(BIN_DIR) -cp $(CLASSPATH) $(SOURCES)
	@echo "Compilação concluída com sucesso!"

# Regra para Rodar
run: all
	@echo "Executando..."
	@# O Classpath para rodar precisa incluir a pasta bin E o jar
	$(JAVA) -cp $(BIN_DIR):$(CLASSPATH) Main

# Regra para Limpar (apagar os .class)
clean:
	@echo "Limpando arquivos compilados..."
	rm -rf $(BIN_DIR)