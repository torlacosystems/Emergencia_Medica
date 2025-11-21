# 🚀 GUIA RÁPIDO: Como Ativar a Geração Automática de PDF

## 📋 O Problema
Quando alguém **SEM o app instalado** escaneia o QR Code com a câmera nativa do celular, precisa abrir uma página web que gere o PDF automaticamente.

## ✅ Solução em 3 Passos

### **PASSO 1: Hospedar a Página HTML** (5 minutos)

#### Opção A: Netlify (Mais Fácil - Recomendado)

1. Abra o arquivo: `app\src\main\assets\emergency_viewer.html`
2. Acesse: https://app.netlify.com/drop
3. Arraste o arquivo `emergency_viewer.html` para a área de upload
4. Aguarde o upload completar
5. Copie a URL gerada (exemplo: `https://fantastic-unicorn-123abc.netlify.app`)

#### Opção B: GitHub Pages (Grátis)

1. Crie uma conta no GitHub (se não tiver)
2. Crie um novo repositório público
3. Nomeie: `emergency-medical-viewer`
4. Faça upload do arquivo `emergency_viewer.html`
5. Renomeie para `index.html`
6. Vá em **Settings** → **Pages**
7. Em Source, selecione `main` branch
8. Salve e copie a URL: `https://seu-usuario.github.io/emergency-medical-viewer`

### **PASSO 2: Atualizar o Código do App**

1. Abra o arquivo: `app\src\main\java\com\emergencia\medica\QRCodeActivity.kt`

2. Localize a linha (aproximadamente linha 60):
```kotlin
val baseUrl = "https://emergencia-medica-viewer.netlify.app"
```

3. Substitua pela SUA URL:
```kotlin
val baseUrl = "https://SUA-URL-AQUI.netlify.app"
```

Exemplo se você usou Netlify:
```kotlin
val baseUrl = "https://fantastic-unicorn-123abc.netlify.app"
```

Exemplo se você usou GitHub Pages:
```kotlin
val baseUrl = "https://seu-usuario.github.io/emergency-medical-viewer"
```

### **PASSO 3: Recompilar o APK**

1. Abra o terminal/PowerShell no diretório do projeto
2. Execute:
```powershell
.\gradlew.bat assembleDebug
```

3. O novo APK estará em:
```
app\build\outputs\apk\debug\app-debug.apk
```

## 🎯 Como Funciona Agora

```
1. Usuário gera QR Code no app
   ↓
2. QR Code contém: https://sua-url.com?data={dados_medicos_codificados}
   ↓
3. Alguém sem o app escaneia com câmera nativa
   ↓
4. Abre o navegador automaticamente
   ↓
5. Página HTML carrega os dados
   ↓
6. PDF é gerado AUTOMATICAMENTE após 1 segundo
   ↓
7. Usuário vê interface bonita + pode baixar PDF novamente
```

## 🧪 Testar se Funcionou

1. Instale o novo APK no celular
2. Configure seus dados médicos
3. Gere um QR Code
4. **Teste 1:** Abra a câmera nativa do celular (sem o app)
5. **Teste 2:** Aponte para o QR Code
6. **Resultado esperado:** 
   - Notificação aparece: "Abrir link?"
   - Toque na notificação
   - Navegador abre a página
   - PDF baixa automaticamente
   - Dados aparecem formatados

## 📱 Funcionalidades da Página Web

Quando alguém escaneia o QR Code **sem o app**:

✅ **PDF gerado automaticamente** após 1 segundo  
✅ **Botão "📄 Baixar PDF"** para baixar novamente  
✅ **Botão "🚑 Ligar 192"** para chamar SAMU  
✅ **Botão "📤 Compartilhar"** via WhatsApp, Email, etc  
✅ **Interface bonita** e profissional  
✅ **Todos os dados** exibidos formatados  
✅ **Funciona em qualquer celular** (Android/iPhone)  

## ❓ Perguntas Frequentes

**P: A página HTML precisa estar sempre online?**  
R: Sim, ela precisa estar hospedada em um servidor público (Netlify, GitHub Pages, etc)

**P: É grátis?**  
R: Sim! Tanto Netlify quanto GitHub Pages são 100% gratuitos

**P: Funciona sem internet?**  
R: Não, a pessoa que escaneia precisa estar conectada à internet

**P: Preciso saber programação?**  
R: Não! Basta seguir os 3 passos acima

**P: Os dados ficam salvos no servidor?**  
R: NÃO! Os dados vão codificados na URL e são processados apenas no navegador do usuário

**P: É seguro?**  
R: Sim! Os dados não são enviados para nenhum servidor, tudo acontece localmente no navegador

## 🆘 Ajuda Rápida

Se você NÃO quiser hospedar agora, o app ainda funciona normalmente:
- ✅ Usuários COM o app instalado podem escanear e ver os dados
- ✅ Podem gerar PDF dentro do app
- ❌ Usuários SEM o app não conseguirão abrir (verão apenas o JSON)

**Recomendação:** Hospede a página para ter a funcionalidade completa!

## 📧 Contato

Se tiver dúvidas, revise os arquivos:
- `VISUALIZADOR_WEB.md` - Documentação completa
- `app\src\main\assets\emergency_viewer.html` - A página HTML
