/**
 * Configuracao de ambiente para o front-end Vanilla (login.html / app.html).
 *
 * Este arquivo e lido antes do app.js e define a URL base do backend Spring Boot.
 *
 * Como funciona por ambiente:
 *
 *  - DESENVOLVIMENTO LOCAL (npm run dev ou abrindo pelo Spring Boot em :8080):
 *    Mantenha API_BASE_URL como "/conteudo" (relativo). Assim o front chama
 *    o backend no mesmo dominio/porta em que esta sendo servido.
 *
 *  - PRODUCAO NA VERCEL (front estatico):
 *    Troque API_BASE_URL pela URL publica do backend na OCI, por exemplo:
 *        window.__NEXT_PUBLIC_API_URL__ = "http://163.176.134.19:8080";
 *    Alternativamente, voce pode definir a env NEXT_PUBLIC_API_URL no painel
 *    da Vercel; porem, como o app.html/login.html sao HTML estatico puro,
 *    a env do painel NAO e injetada automaticamente — por isso este arquivo
 *    e o ponto central de configuracao.
 *
 *  - BACKEND OCI DO DIEGO (temporario / homologacao):
 *    window.__NEXT_PUBLIC_API_URL__ = "http://163.176.134.19:8080";
 *
 * Observacao: nao inclua o "/conteudo" no fim da string; o app.js ja adiciona.
 */
window.__NEXT_PUBLIC_API_URL__ = "";  // Ex.: "http://163.176.134.19:8080"
