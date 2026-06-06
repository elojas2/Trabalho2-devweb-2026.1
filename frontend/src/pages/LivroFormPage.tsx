import { useState, useEffect, type FormEvent } from 'react'
import { useNavigate, useParams } from 'react-router-dom'
import { get, post } from '../api'

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

    const path = isEditing ? '/livros/editar' : '/livros'
    const body: Record<string, string> = { titulo, autor, ano, disponivel: String(disponivel) }
    if (isEditing) body.id = id!

    try {
      await post(path, body)
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
      await post('/livros/excluir', { id: id! })
      navigate('/livros')
    } catch (err: unknown) {
      const e = err as { message?: string }
      setErro(e.message ?? 'Erro ao excluir livro')
    }
  }

  return (
    <main>
      <h1>{isEditing ? 'Editar Livro' : 'Cadastrar Livro'}</h1>
      <form onSubmit={handleSubmit}>
        <label>
          Título
          <input value={titulo} onChange={(e) => setTitulo(e.target.value)} required />
        </label>
        <label>
          Autor
          <input value={autor} onChange={(e) => setAutor(e.target.value)} required />
        </label>
        <label>
          Ano
          <input type="number" value={ano} onChange={(e) => setAno(e.target.value)} required />
        </label>
        <label>
          <input type="checkbox" checked={disponivel} onChange={(e) => setDisponivel(e.target.checked)} />
          Disponível
        </label>
        {erro && <p role="alert">{erro}</p>}
        <button type="submit" disabled={carregando}>
          {carregando ? 'Salvando...' : 'Salvar'}
        </button>
      </form>
      {isEditing && (
        <button onClick={handleExcluir}>Excluir livro</button>
      )}
    </main>
  )
}
