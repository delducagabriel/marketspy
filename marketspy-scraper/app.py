from flask import Flask, request, jsonify
from scraper_service import scrape_product

app = Flask(__name__)

@app.route('/scrape', methods=['POST'])
def scraper_endpoint():
    """
    Rota que recebe um JSON: {"url": "..."}
    E retorna os dados do produto.
    """
    
    # 1. Recebe os dados enviados na requisição
    data = request.get_json()
    
    # 2. Validação básica: O usuário mandou a URL?
    if not data or 'url' not in data:
        return jsonify({
            "status": "error", 
            "message": "O campo 'url' é obrigatório no JSON."
        }), 400
    
    url_recebida = data['url']
    
    # 3. Chama o nosso 'cérebro' (o scraper_service)
    print(f"Processando URL: {url_recebida}")
    resultado = scrape_product(url_recebida)
    
    # 4. Define o código HTTP de resposta (200 OK ou 500 Erro)
    status_code = 200
    if resultado['status'] == 'error':
        status_code = 500
        
    return jsonify(resultado), status_code

if __name__ == '__main__':
    # Roda o servidor na porta 5000
    app.run(host='0.0.0.0', port=5000, debug=True)
