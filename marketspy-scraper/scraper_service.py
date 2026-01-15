import requests
from bs4 import BeautifulSoup

def scrape_product(url):
    """
    Acessa a URL informada, baixa o HTML e extrai Título e Preço.
    Retorna um dicionário (dict) com os dados.
    """

    # --- 1. Configuração User-Agent
    # Sites como amazon e mercado livre bloqueam acessos que não parecer ser de um navegador real
    # Este header diz ao site: oi, eu sou um google chrome no windows 10
    headers = {
        "User-Agent": "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.124 Safari/537.36"
    }

    try:
    # --- 2. Requisição HTTP 
    # O timeout=10 evita que o script fique travado para sempre se o site não responder
        response = requests.get(url, headers=headers, timeout=10)

        # Se o site devolver o erro 404 ou 500, isso gera uma exceção
        response.raise_for_status()

        # --- 3. Parsing (leitura do HTML)
        soup = BeautifulSoup(response.content, 'lxml')

        # --- 4. Extração de dados
        # Busca o título (geralmente na tag h1)
        title_tag = soup.find('h1')
        title = title_tag.get_text().strip() if title_tag else "Título não identificado"

        # Busca o preço
        # OBS: parte mais frágil de um scraper
        # Classes CSS mudam
        # Usar classe comum do mercado livre
        # Se o site mudar o layout, é necessário atualizar
        price_tag = soup.find('span', class_='andes-money-amount__fraction')

        price = 0.0
        if price_tag: 
            # Remoção pontos de milhar (1.200 -> 1200) e troca de vírgula por ponto se necessário
            price_text = price_tag.get_text().replace('.', '').replace(',', '.')
            try:
                price = float(price_text)
            except ValueError:
                price = 0.0
            
        return {
            "status": "success",
            "url": url,
            "title": title,
            "price": price
        }
    
    except Exception as e:
        # Em caso de qualquer erro (site fora do ar, internet caiu, bloqueio), retornamos o erro.
        return {
            "status": "error",
            "message": str(e)
        }
        
# Teste rápido
# Bloco só roda se executar o arquivo diretamente no terminal
# É ignorado quando o app.py importar o arquivo
if __name__ == "__main__":
    url_teste = "https://www.mercadolivre.com.br/apple-iphone-13-128-gb-meia-noite/p/MLB19615330"
    print("Testando scraper...")
    resultado = scrape_product(url_teste)
    print(resultado)
