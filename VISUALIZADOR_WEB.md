# 🆘 Visualizador Web de Emergência Médica

## 📄 Como Hospedar a Página HTML

Para que o QR Code funcione corretamente para pessoas **sem o app instalado**, você precisa hospedar o arquivo `emergency_viewer.html` em um servidor web público.

### Opção 1: Netlify (Recomendado - Grátis e Fácil)

1. Acesse [https://www.netlify.com](https://www.netlify.com)
2. Crie uma conta gratuita
3. Arraste o arquivo `emergency_viewer.html` para o Netlify Drop
4. Renomeie o arquivo para `index.html`
5. Copie a URL gerada (exemplo: `https://seu-site.netlify.app`)
6. Atualize o código em `QRCodeActivity.kt`:
   ```kotlin
   val baseUrl = "https://seu-site.netlify.app"
   ```

### Opção 2: GitHub Pages (Grátis)

1. Crie um repositório no GitHub
2. Faça upload do arquivo `emergency_viewer.html` e renomeie para `index.html`
3. Vá em Settings > Pages
4. Ative o GitHub Pages
5. Use a URL: `https://seu-usuario.github.io/nome-repositorio`

### Opção 3: Vercel (Grátis)

1. Acesse [https://vercel.com](https://vercel.com)
2. Crie uma conta
3. Faça upload do arquivo HTML
4. Copie a URL gerada

### Opção 4: Servidor Próprio

Se você tem um servidor web próprio, apenas faça upload do arquivo `emergency_viewer.html`.

## 🔧 Configuração no App

Após hospedar a página, atualize a URL no arquivo `QRCodeActivity.kt`:

```kotlin
private fun generateWebURL(jsonData: String): String {
    val baseUrl = "https://SUA-URL-AQUI.com"
    val encodedData = URLEncoder.encode(jsonData, "UTF-8")
    return "$baseUrl?data=$encodedData"
}
```

## ✨ Como Funciona

1. **App gera QR Code** → Contém URL web + dados médicos codificados
2. **Pessoa escaneia** → Com câmera nativa do celular (sem precisar do app)
3. **Abre navegador** → Página HTML carrega automaticamente
4. **PDF gerado** → Automaticamente após 1 segundo
5. **Dados exibidos** → Interface bonita e profissional

## 🎯 Funcionalidades da Página Web

- ✅ Exibe todos os dados médicos formatados
- ✅ Gera PDF automaticamente
- ✅ Botão para baixar PDF novamente
- ✅ Botão para ligar 192 (SAMU)
- ✅ Botão para compartilhar
- ✅ Funciona em qualquer celular/navegador
- ✅ Não precisa instalar nada

## 📱 Teste

1. Hospede o arquivo HTML
2. Atualize a URL no código
3. Recompile o APK
4. Gere um QR Code no app
5. Escaneie com a câmera nativa do celular
6. O navegador abrirá e o PDF será gerado automaticamente!

## 🌐 URL de Exemplo

Por padrão, o app está configurado para usar:
```
https://emergencia-medica-viewer.netlify.app
```

Você deve substituir por sua própria URL.
