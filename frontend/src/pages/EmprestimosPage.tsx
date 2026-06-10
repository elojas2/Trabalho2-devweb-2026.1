import { useState, useEffect } from 'react'
import { get, post } from '../api'

interface Emprestimo {
  id: number
  livroTitulo?: string 
  livro?: {            
    id: number
    titulo: string
    autor: string
  }
  dataEmprestimo: string
  dataDevolucao: string | null
}

export default function EmprestimosPage() {
  const [emprestimos, setEmprestimos] = useState<Emprestimo[]>([])
  const [carregando, setCarregando] = useState(true)
  const [erro, setErro] = useState('')

  async function carregarEmprestimos() {
    setCarregando(true)
    setErro('')
    try {
      const data = await get<any>('/emprestimos')
      
      if (Array.isArray(data)) {
        setEmprestimos(data)
      } else {
        setErro('O servidor não retornou uma lista válida de empréstimos.')
      }
    } catch (err: unknown) {
      const e = err as { message?: string }
      setErro(e.message ?? 'Erro ao carregar histórico de empréstimos.')
    } finally {
      setCarregando(false)
    }
  }

  useEffect(() => {
    carregarEmprestimos()
  }, [])

  async function handleDevolver(idEmprestimo: number) {
    try {
      await post('/emprestimos', { action: 'devolver', idEmprestimo: String(idEmprestimo) })
      
      alert('Livro devolvido com sucesso!')
      carregarEmprestimos() 
    } catch (err: unknown) {
      const e = err as { message?: string }
      alert(e.message ?? 'Erro ao tentar devolver o livro.')
    }
  }

  return (
    <main className="page">
      <div className="container" style={{ padding: '2rem' }}>
        <h1 style={{ marginBottom: '2rem' }}>Meus Empréstimos</h1>

        {erro && <p className="alert alert--error" style={{ color: 'red', fontWeight: 'bold' }}>{erro}</p>}
        {carregando && <p>Carregando seus empréstimos...</p>}

        {!carregando && !erro && (
          <div className="table-responsive">
            <table style={{ width: '100%', borderCollapse: 'collapse', textAlign: 'left' }}>
              <thead>
                <tr style={{ borderBottom: '2px solid #ccc' }}>
                  <th style={{ padding: '0.75rem' }}>ID</th>
                  <th style={{ padding: '0.75rem' }}>Livro</th>
                  <th style={{ padding: '0.75rem' }}>Data do Empréstimo</th>
                  <th style={{ padding: '0.75rem' }}>Data da Devolução</th>
                  <th style={{ padding: '0.75rem' }}>Status</th>
                  <th style={{ padding: '0.75rem' }}>Ações</th>
                </tr>
              </thead>
              <tbody>
                {emprestimos.length === 0 ? (
                  <tr>
                    <td colSpan={6} style={{ padding: '1rem', textAlign: 'center' }}>
                      Você não possui nenhum empréstimo registrado no momento.
                    </td>
                  </tr>
                ) : (
                  emprestimos.map((emp) => {
                    const tituloLivro = emp.livro?.titulo ?? emp.livroTitulo ?? 'Livro Desconhecido'
                    const estaDevolvido = Boolean(emp.dataDevolucao)

                    return (
                      <tr key={emp.id} style={{ borderBottom: '1px solid #eee' }}>
                        <td style={{ padding: '0.75rem' }}>{emp.id}</td>
                        <td style={{ padding: '0.75rem' }}>{tituloLivro}</td>
                        <td style={{ padding: '0.75rem' }}>{emp.dataEmprestimo}</td>
                        <td style={{ padding: '0.75rem' }}>{emp.dataDevolucao ?? '-'}</td>
                        <td style={{ padding: '0.75rem' }}>
                          {estaDevolvido ? (
                            <span style={{ color: 'green', fontWeight: 'bold' }}>Devolvido</span>
                          ) : (
                            <span style={{ color: 'orange', fontWeight: 'bold' }}>Ativo</span>
                          )}
                        </td>
                        <td style={{ padding: '0.75rem' }}>
                          {}
                          {!estaDevolvido && (
                            <button
                              onClick={() => handleDevolver(emp.id)}
                              className="btn btn--primary"
                              style={{ padding: '0.3rem 0.6rem', fontSize: '0.9rem' }}
                            >
                              Devolver
                            </button>
                          )}
                        </td>
                      </tr>
                    )
                  })
                )}
              </tbody>
            </table>
          </div>
        )}
      </div>
    </main>
  )
}