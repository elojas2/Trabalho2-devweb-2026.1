import { useState, useEffect, type FormEvent } from 'react'
import { useNavigate, useParams } from 'react-router-dom'
import { get, post, put, del } from '../api'

interface Livro {
  id: number
  titulo: string
  autor: string
  ano: number
  disponivel: boolean
}

export default function LivroFormPage() {
  const { id } = useParams()
  const navigate = useNavigate()
  const isEditing = Boolean(id)

  const [titulo, setTitulo] = useState('')
  const [autor, setAutor] = useState('')
  const [ano, setAno] = useState('')
  const [disponivel, setDisponivel] = useState(true)
  const [erro, setErro] = useState('')
  const [carregando, setCarregando] = useState(false)

  useEffect(() => {
    if (!isEditing) return
    get<Livro>(`/livros/${id}`).then((livro) => {
      setTitulo(livro.titulo)
      setAutor(livro.autor)
      setAno(String(livro.ano))
      setDisponivel(livro.disponivel)
    }).catch(() => setErro('Livro não encontrado.'))
  }, [id, isEditing])

  async function handleSubmit(e: FormEvent) {
    e.preventDefault()
    setErro('')
    setCarregando(true)

    const body = { titulo, autor, ano: Number(ano), disponivel }

    try {
      if (isEditing) {
        await put(`/livros/${id}`, body)
      } else {
        await post('/livros', body)
      }
      navigate('/livros')
    } catch (err: unknown) {
      const e = err as { message?: string }
      setErro(e.message ?? 'Erro ao salvar livro')
    } finally {
      setCarregando(false)
    }
  }

  async function handleExcluir() {
    if (!confirm('Tem certeza que deseja excluir este livro?')) return
    try {
      await del(`/livros/${id}`)
      navigate('/livros')
    } catch (err: unknown) {
      const e = err as { message?: string }
      setErro(e.message ?? 'Erro ao excluir livro')
    }
  }

  return (
    <main className="page">
      <div className="form-card">
        <h1 className="form-card__title">{isEditing ? 'Editar Livro' : 'Cadastrar Livro'}</h1>
        <form onSubmit={handleSubmit}>
          <div className="form-group">
            <label htmlFor="titulo">Título</label>
            <input id="titulo" value={titulo} onChange={(e) => setTitulo(e.target.value)} required />
          </div>
          <div className="form-group">
            <label htmlFor="autor">Autor</label>
            <input id="autor" value={autor} onChange={(e) => setAutor(e.target.value)} required />
          </div>
          <div className="form-group">
            <label htmlFor="ano">Ano</label>
            <input id="ano" type="number" value={ano} onChange={(e) => setAno(e.target.value)} required />
          </div>
          <label className="form-checkbox">
            <input type="checkbox" checked={disponivel} onChange={(e) => setDisponivel(e.target.checked)} />
            Disponível para empréstimo
          </label>
          {erro && <p className="alert alert--error" role="alert">{erro}</p>}
          <button type="submit" className="btn btn--primary btn--full" disabled={carregando}>
            {carregando ? 'Salvando...' : 'Salvar'}
          </button>
        </form>
        {isEditing && (
          <button onClick={handleExcluir} className="btn btn--danger btn--full" style={{ marginTop: 12 }}>
            Excluir livro
          </button>
        )}
      </div>
    </main>
  )
}
