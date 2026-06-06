import { useState, type FormEvent } from 'react'
import { useNavigate, Link } from 'react-router-dom'
import { useAuth } from '../contexts/AuthContext'

export default function LoginPage() {
  const { login } = useAuth()
  const navigate = useNavigate()
  const [erro, setErro] = useState('')
  const [carregando, setCarregando] = useState(false)

  async function handleSubmit(e: FormEvent<HTMLFormElement>) {
    e.preventDefault()
    setErro('')
    setCarregando(true)

    const form = e.currentTarget
    const email = (form.elements.namedItem('email') as HTMLInputElement).value
    const senha = (form.elements.namedItem('senha') as HTMLInputElement).value

    try {
      await login(email, senha)
      navigate('/livros')
    } catch (err: unknown) {
      const e = err as { message?: string }
      setErro(e.message ?? 'Erro ao fazer login')
    } finally {
      setCarregando(false)
    }
  }

  return (
    <main>
      <h1>Login</h1>
      <form onSubmit={handleSubmit}>
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
          {carregando ? 'Entrando...' : 'Entrar'}
        </button>
      </form>
      <p>
        Não tem conta? <Link to="/cadastro">Cadastre-se</Link>
      </p>
    </main>
  )
}
