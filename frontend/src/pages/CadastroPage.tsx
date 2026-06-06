import { useState, type FormEvent } from 'react'
import { useNavigate, Link } from 'react-router-dom'
import { post } from '../api'

export default function CadastroPage() {
  const navigate = useNavigate()
  const [erro, setErro] = useState('')
  const [carregando, setCarregando] = useState(false)

  async function handleSubmit(e: FormEvent<HTMLFormElement>) {
    e.preventDefault()
    setErro('')
    setCarregando(true)

    const form = e.currentTarget
    const nome = (form.elements.namedItem('nome') as HTMLInputElement).value
    const email = (form.elements.namedItem('email') as HTMLInputElement).value
    const senha = (form.elements.namedItem('senha') as HTMLInputElement).value

    try {
      await post('/cadastroUser', { nome, email, senha })
      navigate('/login')
    } catch (err: unknown) {
      const e = err as { message?: string }
      setErro(e.message ?? 'Erro ao cadastrar')
    } finally {
      setCarregando(false)
    }
  }

  return (
    <main>
      <h1>Cadastro</h1>
      <form onSubmit={handleSubmit}>
        <label>
          Nome
          <input name="nome" type="text" required />
        </label>
        <label>
          E-mail
          <input name="email" type="email" required />
        </label>
        <label>
          Senha
          <input name="senha" type="password" required />
        </label>
        {erro && <p role="alert">{erro}</p>}
        <button type="submit" disabled={carregando}>
          {carregando ? 'Cadastrando...' : 'Cadastrar'}
        </button>
      </form>
      <p>
        Já tem conta? <Link to="/login">Faça login</Link>
      </p>
    </main>
  )
}
