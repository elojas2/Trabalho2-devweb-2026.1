import { useState, useEffect } from 'react'
import { Link } from 'react-router-dom'
import { get, post } from '../api'
import { useAuth } from '../contexts/AuthContext'

// Importando os novos componentes
import SearchBar from '../components/SearchBar'
import FiltroCheckbox from '../components/FiltroCheckbox'
import TabelaLivros, { type Livro } from '../components/TabelaLivros'

// Funções de Cookie
function setCookie(name: string, value: string, days: number) {
  const date = new Date()
  date.setTime(date.getTime() + (days * 24 * 60 * 60 * 1000))
  document.cookie = `${name}=${value};expires=${date.toUTCString()};path=/;SameSite=Lax`
}

function getCookie(name: string): string | null {
  const value = `; ${document.cookie}`
  const parts = value.split(`; ${name}=`)
  if (parts.length === 2) return parts.pop()?.split(';').shift() ?? null
  return null
}

export default function LivrosPage() {
  const { usuario } = useAuth()

  const [livros, setLivros] = useState<Livro[]>([])
  const [busca, setBusca] = useState(() => getCookie('ultimaBusca') || '')
  const [carregando, setCarregando] = useState(true)
  const [erro, setErro] = useState('')
  const [mostrarApenasDisponiveis, setMostrarApenasDisponiveis] = useState<boolean>(() => {
    return getCookie('filtroDisponiveis') === 'true'
  })

  async function carregarLivros(e?: React.FormEvent) {
    if (e) e.preventDefault()
    setCarregando(true)
    setErro('')

    try {
      const data = await get<any>(`/livros?q=${busca}`)
      if (Array.isArray(data)) {
        setLivros(data)
      } else {
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

  function handleBuscaChange(novoValor: string) {
    setBusca(novoValor);
    setCookie('ultimaBusca', novoValor, 7);
  }

  function handleCheckboxChange(checked: boolean) {
    setMostrarApenasDisponiveis(checked)
    setCookie('filtroDisponiveis', String(checked), 30)
  }

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

  const livrosFiltrados = livros.filter((livro: Livro) =>
    mostrarApenasDisponiveis ? livro.disponivel : true
  );

  return (
    <main className="page">
      <div className="container" style={{ padding: '2rem' }}>

        <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: '2rem', flexWrap: 'wrap', gap: '1rem' }}>
          <h1>Catálogo de Livros</h1>

          <div style={{ display: 'flex', gap: '1rem', alignItems: 'center' }}>
            <SearchBar
              busca={busca}
              setBusca={handleBuscaChange}
              onSearch={carregarLivros}
            />

            {usuario?.admin && (
              <Link to="/livros/novo" className="btn btn--accent">
                + Novo Livro
              </Link>
            )}
          </div>
        </div>

        <FiltroCheckbox
          label="Mostrar apenas livros disponíveis"
          checked={mostrarApenasDisponiveis}
          onChange={handleCheckboxChange}
        />

        {erro && <p className="alert alert--error" style={{ color: 'red', fontWeight: 'bold' }}>{erro}</p>}
        {carregando && <p>Carregando catálogo...</p>}

        {!carregando && !erro && (
          <>
            <TabelaLivros
              livros={livrosFiltrados}
              isAdmin={!!usuario?.admin}
              onEmprestar={handleEmprestar}
            />
          </>
        )}
      </div>
    </main>
  )
}
