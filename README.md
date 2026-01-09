# 🧾 Jasper Converter

Conversor **100% gratuito e offline** de arquivos **.jasper → .jrxml**, com interface web moderna e empacotamento como **aplicação desktop nativa** para **macOS, Linux e Windows**.

Projeto voltado para **uso pessoal**, aprendizado e produtividade — sem custos, sem SaaS, sem dependência externa.

---

## ✨ Funcionalidades

- Upload de arquivos `.jasper`
- Conversão automática para `.jrxml`
- Logs em tempo real (Server-Sent Events)
- Barra de progresso
- Tema dark
- Funciona 100% offline
- Backend Spring Boot
- Frontend HTML + CSS + JS
- Empacotável como app desktop

---

## 🧠 Arquitetura

```
Frontend (Browser)
   ↓ SSE / HTTP
Spring Boot Backend
   ↓
JasperReports Engine
```

---

## 📦 Requisitos

- Java **17 ou superior**
- Maven 3.9+

Verificar:
```bash
java -version
mvn -version
```

---

## ▶️ Executar em modo desenvolvimento

```bash
mvn spring-boot:run
```

Depois acesse:
```
http://localhost:8080
```

---

## 🏗️ Gerar JAR

```bash
mvn clean package
```

Arquivo gerado:
```
target/jasper-converter-1.0.0.jar
```

---

## 📦 Gerar aplicação desktop (jpackage)

⚠️ Execute **sempre na raiz do projeto**.

Antes:
```bash
mvn clean package
```

---

## 🍎 macOS — Gerar `.app`

```bash
jpackage \
  --type app-image \
  --name JasperConverter \
  --input target \
  --main-jar jasper-converter-1.0.0.jar \
  --resource-dir macos \
  --dest dist \
  --java-options "--add-modules java.desktop"
```

Resultado:
```
dist/JasperConverter.app
```

---

## 🐧 Linux — Gerar App

```bash
jpackage \
  --type app-image \
  --name JasperConverter \
  --input target \
  --main-jar jasper-converter-1.0.0.jar \
  --dest dist \
  --java-options "--add-modules java.desktop"
```

Executar:
```bash
./dist/JasperConverter/bin/JasperConverter
```

### (Opcional) Gerar `.deb`
```bash
jpackage --type deb --name JasperConverter --input target --main-jar jasper-converter-1.0.0.jar --dest dist
```

---

## 🪟 Windows — Gerar `.exe`

Pré-requisito:
- WiX Toolset

```powershell
jpackage ^
  --type exe ^
  --name JasperConverter ^
  --input target ^
  --main-jar jasper-converter-1.0.0.jar ^
  --dest dist ^
  --java-options "--add-modules java.desktop"
```

Resultado:
```
dist/JasperConverter.exe
```

---

## 🧑‍💻 Usuário final (sem Java)

Após gerar o app:
- **macOS**: abrir `.app`
- **Linux**: executar binário
- **Windows**: instalar `.exe`

👉 **Java não é necessário para o usuário final**.

---

## 🧪 Logs em tempo real

O frontend recebe logs via **Server-Sent Events (SSE)**:

- Arquivo recebido
- Conversão iniciada
- Conversão finalizada
- Erros detalhados

---

## 📁 Estrutura do projeto

```
jasper-converter/
├── src/
│   └── main/
│       ├── java/
│       └── resources/
│           └── static/
├── macos/
│   └── launcher.sh
├── pom.xml
├── run.sh
└── README.md
```

---

## 🚀 Releases

Recomendado criar **GitHub Releases**:

- macOS: `.app`
- Linux: `.deb` ou pasta
- Windows: `.exe`

---

## 🧹 Limpeza

```bash
rm -rf dist target
mvn clean
```

---

## 📜 Licença

Uso pessoal e educacional.
Livre para estudar, modificar e aprender.

---

## 🙌 Autor

Desenvolvido por **Igão** ❤️

Projeto criado para aprendizado, automação e evolução técnica.

