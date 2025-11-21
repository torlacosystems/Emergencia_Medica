# Emergência Médica - Notas de Versão

## Versão 1.0 (Release Inicial)

### 🚨 Funcionalidades Principais

#### 1. Armazenamento de Dados Médicos
- Cadastro completo de informações médicas pessoais:
  - Nome completo
  - Idade
  - Tipo sanguíneo
  - Alergias
  - Medicamentos em uso
  - Condições médicas
  - Contatos de emergência

#### 2. QR Code de Emergência
- Geração automática de QR Code com dados médicos
- Acesso rápido aos dados em situações de emergência
- URL único para cada usuário hospedado no GitHub Pages

#### 3. Acesso na Tela de Bloqueio
- **Recurso Principal**: Acesso ao QR Code mesmo com telefone bloqueado
- **Serviço de Acessibilidade**: Ativação por 3 toques no botão VOLUME BAIXO
- Ideal para emergências onde a vítima não consegue desbloquear o telefone
- Tela liga automaticamente mostrando informações médicas críticas

#### 4. Visualização Web
- Página web responsiva para quem escanear o QR Code
- Não requer instalação de app para visualizar
- Download de PDF com informações médicas
- Interface limpa e profissional
- Acesso via: https://torlacosystems.github.io/Emergencia_Medica/

#### 5. Escaneamento de QR Codes
- Scanner integrado para ler QR Codes de outras pessoas
- Geração automática de PDF com os dados escaneados
- Armazenamento local do PDF para acesso offline

### 🔧 Recursos Técnicos

- **Plataforma**: Android (API 23 - Android 6.0 ou superior)
- **Linguagem**: Kotlin 100%
- **Otimização**: ProGuard/R8 habilitado (redução de ~15% no tamanho)
- **Segurança**: APK assinado digitalmente
- **Tamanho**: ~11.8 MB (otimizado)

### 📱 Widgets e Atalhos

- Widget para tela inicial
- Atalho rápido para QR Code de emergência
- Ícone personalizado (cruz vermelha de emergência)

### 🔒 Permissões Necessárias

- **CAMERA**: Para escanear QR Codes
- **VIBRATE**: Feedback tátil
- **WAKE_LOCK**: Manter tela ligada no modo emergência
- **DISABLE_KEYGUARD**: Exibir sobre tela de bloqueio
- **BIND_ACCESSIBILITY_SERVICE**: Detecção de botões de volume

### ⚠️ Requisitos de Instalação

O app requer ativação manual de:
1. **Instalação de Fontes Desconhecidas**: Por não estar na Google Play Store
2. **Serviço de Acessibilidade**: Para funcionalidade de emergência na tela de bloqueio

Ver instruções completas em: `README_INSTALACAO.md`

### 🌍 Idiomas

- Português (Brasil) - interface principal
- Suporte multilíngue do Android Material Design

### 🔐 Privacidade

- Dados armazenados apenas localmente no dispositivo
- Nenhum dado enviado para servidores
- QR Code gera URL pública, mas dados permanecem no seu controle
- Sem rastreamento ou analytics

### 🛠️ Tecnologias Utilizadas

- **ZXing**: Geração e leitura de QR Codes
- **iText7**: Geração de PDF no app
- **jsPDF**: Geração de PDF na página web
- **Material Design**: Interface moderna e intuitiva
- **GitHub Pages**: Hospedagem da página de visualização

### 📝 Notas Importantes

1. **Google Play Protect**: O app será bloqueado por padrão. Isto é normal e esperado para apps fora da Play Store. Siga as instruções em `README_INSTALACAO.md` para instalação segura.

2. **Serviço de Acessibilidade**: Precisa ser ativado manualmente em Configurações > Acessibilidade > Emergência Médica. Sem isso, o recurso de 3 toques no volume não funcionará.

3. **Backup**: Recomendamos fazer backup manual dos seus dados médicos, pois eles são armazenados apenas no dispositivo.

### 🐛 Problemas Conhecidos

Nenhum no momento.

### 🔮 Próximas Versões (Planejadas)

- Múltiplos perfis (família)
- Exportação/importação de dados
- Histórico médico detalhado
- Fotos de documentos médicos
- Sincronização em nuvem (opcional)

---

**Data de Release**: Novembro 2025
**Versão**: 1.0
**Build**: release
**Desenvolvido por**: Torlaco Systems
