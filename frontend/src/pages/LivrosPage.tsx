import { useState, useEffect } from 'react'
import { Link } from 'react-router-dom'
import { get, post } from '../api'
import { useAuth } from '../contexts/AuthContext'

interface Livro {
  id: number
  titulo: string
  autor: string
  ano: number
  disponivel: boolean
}

export default function LivrosPage() {
  const { usuario } = useAuth()

  const [livros, setLivros] = useState<Livro[]>([])
  const [busca, setBusca] = useState('')
  const [carregando, setCarregando] = useState(true)
  const [erro, setErro] = useState('')
  const [mostrarApenasDisponiveis, setMostrarApenasDisponiveis] = useState(false)

  async function carregarLivros(e?: React.FormEvent) {
    if (e) e.preventDefault()
    
    setCarregando(true)
    setErro('')
    
    try {
      const data = await get<any>(`/livros?q=${busca}`)
      
      if (Array.isArray(data)) {
        setLivros(data)
      } else {
        console.error("A API não retornou uma lista:", data);
        setErro('O servidor não retornou a lista no formato correto.')
        setLivros([])
      }
      
    } catch (err: unknown) {
      const e = err as { message?: string }
      setErro(e.message ?? 'Erro ao carregar o catálogo de livros.')
    } finally {
      setCarregando(false)
    }
  }

  useEffect(() => {
    carregarLivros()
  }, [])

  async function handleEmprestar(idLivro: number) {
    try {
      await post('/emprestimos', { action: 'emprestar', idLivro: String(idLivro) })
      alert('Livro emprestado com sucesso!')
      carregarLivros()
    } catch (err: unknown) {
      const e = err as { message?: string }
      alert(e.message ?? 'Erro ao tentar emprestar o livro.')
    }
  }

  const livrosFiltrados = livros.filter(livro => {
    if (mostrarApenasDisponiveis) {
      return livro.disponivel; 
    }
    return true; 
  });

  return (
    <main className="page">
      <div className="container" style={{ padding: '2rem' }}>
        
        <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: '2rem', flexWrap: 'wrap', gap: '1rem' }}>
          <h1>Catálogo de Livros</h1>
          
          <div style={{ display: 'flex', gap: '1rem', alignItems: 'center' }}>
            <form onSubmit={carregarLivros} style={{ display: 'flex', gap: '0.5rem' }}>
              <input 
                type="text" 
                placeholder="Título ou autor..." 
                value={busca}
                onChange={(e) => setBusca(e.target.value)}
                style={{ padding: '0.5rem', borderRadius: '4px', border: '1px solid #ccc' }}
              />
              <button type="submit" className="btn btn--primary">Buscar</button>
            </form>
            
            {usuario?.admin && (
              <Link to="/livros/novo" className="btn btn--accent">
                + Novo Livro
              </Link>
            )}
          </div>
        </div>

        <div className="filtro-container" style={{ marginBottom: '1rem' }}>
          <label style={{ display: 'flex', alignItems: 'center', gap: '8px', cursor: 'pointer' }}>
            <input
              type="checkbox"
              checked={mostrarApenasDisponiveis}
              onChange={(e) => setMostrarApenasDisponiveis(e.target.checked)}
            />
            Mostrar apenas livros disponíveis
          </label>
        </div>

        {erro && <p className="alert alert--error" style={{ color: 'red', fontWeight: 'bold' }}>{erro}</p>}
        {carregando && <p>Carregando catálogo...</p>}

        {!carregando && !erro && (
          <div className="table-responsive">
            <table style={{ width: '100%', borderCollapse: 'collapse', textAlign: 'left' }}>
              <thead>
                <tr style={{ borderBottom: '2px solid #ccc' }}>
                  <th style={{ padding: '0.75rem' }}>ID</th>
                  <th style={{ padding: '0.75rem' }}>Título</th>
                  <th style={{ padding: '0.75rem' }}>Autor</th>
                  <th style={{ padding: '0.75rem' }}>Ano</th>
                  <th style={{ padding: '0.75rem' }}>Status</th>
                  <th style={{ padding: '0.75rem' }}>Ações</th>
                </tr>
              </thead>
              <tbody>
                {livrosFiltrados.length === 0 ? (
                  <tr>
                    <td colSpan={6} style={{ padding: '1rem', textAlign: 'center' }}>Nenhum livro encontrado.</td>
                  </tr>
                ) : (
                  livrosFiltrados.map((livro) => (
                    <tr key={livro.id} style={{ borderBottom: '1px solid #eee' }}>
                      <td style={{ padding: '0.75rem' }}>{livro.id}</td>
                      <td style={{ padding: '0.75rem' }}>{livro.titulo}</td>
                      <td style={{ padding: '0.75rem' }}>{livro.autor}</td>
                      <td style={{ padding: '0.75rem' }}>{livro.ano}</td>
                      <td style={{ padding: '0.75rem' }}>
                        {livro.disponivel ? (
                          <span style={{ color: 'green', fontWeight: 'bold' }}>Disponível</span>
                        ) : (
                          <span style={{ color: 'red', fontWeight: 'bold' }}>Emprestado</span>
                        )}
                      </td>
                      <td style={{ padding: '0.75rem', display: 'flex', gap: '0.5rem' }}>
                        
                        {livro.disponivel && (
                          <button 
                            onClick={() => handleEmprestar(livro.id)}
                            className="btn btn--primary"
                            style={{ padding: '0.3rem 0.6rem', fontSize: '0.9rem' }}
                          >
                            Emprestar
                          </button>
                        )}
                        
                        {usuario?.admin && (
                          <Link 
                            to={`/livros/editar/${livro.id}`} 
                            className="btn btn--edit"
                            style={{ padding: '0.3rem 0.6rem', fontSize: '0.9rem', backgroundColor: '#e2e8f0', color: 'black', textDecoration: 'none', borderRadius: '4px' }}
                          >
                            Editar
                          </Link>
                        )}
                      </td>
                    </tr>
                  ))
                )}
              </tbody>
            </table>
          </div>
        )}
      </div>
    </main>
  )
}