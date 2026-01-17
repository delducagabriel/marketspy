import { useState, useEffect } from 'react'
import axios from 'axios'

function App() {
  // estado para guardar a lista de produtos que vem do java
  const [products, setProducts] = useState([])
  const [loading, setLoading] = useState(true)

  // executa isso assim que a tela carrega
  useEffect(() => {
    fetchProducts()
  }, [])

  const fetchProducts = async () => {
    try {
      // faz a chamada GET para o java (porta 8080)
      const response = await axios.get('http://localhost:8080/api/products')
      setProducts(response.data) // guarda os dados na memória do react
      setLoading(false)
    } catch (error) {
      console.error("Erro ao buscar produtos:", error)
      setLoading(false)
    }
  }

  return (
    <div style={{ maxWidth: '800px', margin: '0 auto', padding: '20px', fontFamily: 'sans-serif' }}>
      
      {/* Cabeçalho */}
      <header style={{ marginBottom: '40px', textAlign: 'center' }}>
        <h1 style={{ color: '#2c3e50' }}>🕵️ MarketSpy</h1>
        <p style={{ color: '#7f8c8d' }}>Monitoramento de Inteligência de Preços</p>
      </header>

      {/* Lista de Produtos */}
      {loading ? (
        <p>Carregando dados do servidor...</p>
      ) : (
        <div style={{ display: 'grid', gap: '20px' }}>
          {products.map((product) => (
            // Card do Produto
            <div key={product.id} style={{ 
              border: '1px solid #ddd', 
              borderRadius: '8px', 
              padding: '20px',
              boxShadow: '0 2px 4px rgba(0,0,0,0.1)' 
            }}>
              <h3 style={{ margin: '0 0 10px 0' }}>{product.name}</h3>
              
              <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center' }}>
                <a href={product.url} target="_blank" style={{ color: '#3498db', textDecoration: 'none' }}>
                  Ver no site original ↗
                </a>
                
                <div style={{ textAlign: 'right' }}>
                  <span style={{ fontSize: '0.9em', color: '#7f8c8d' }}>Preço Atual</span>
                  <div style={{ fontSize: '1.5em', fontWeight: 'bold', color: '#27ae60' }}>
                    R$ {product.lastPrice?.toFixed(2)}
                  </div>
                </div>
              </div>
            </div>
          ))}
        </div>
      )}

      {products.length === 0 && !loading && (
        <p style={{ textAlign: 'center' }}>Nenhum produto cadastrado ainda.</p>
      )}
    </div>
  )
}

export default App