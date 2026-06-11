import { useState, type FormEvent } from 'react'
import { useNavigate, Link } from 'react-router-dom'
import { useAuth } from '../contexts/AuthContext'

const MOCK = import.meta.env.VITE_MOCK === 'true'

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
    <main className="page">
      <div className="form-card">
        <h1 className="form-card__title">Entrar</h1>
        {MOCK && (
          <p className="alert" style={{ background: '#fef9c3', color: '#92400e', borderRadius: 6, padding: '0.5rem 0.75rem', marginBottom: '1rem', fontSize: '0.875rem' }}>
            Modo demo — use qualquer email e senha para entrar.
          </p>
        )}
        <form onSubmit={handleSubmit}>
          <div className="form-group">
            <label htmlFor="email">E-mail</label>
            <input id="email" name="email" type="email" required />
          </div>
          <div className="form-group">
            <label htmlFor="senha">Senha</label>
            <input id="senha" name="senha" type="password" required />
          </div>
          {erro && <p className="alert alert--error" role="alert">{erro}</p>}
          <button type="submit" className="btn btn--primary btn--full" disabled={carregando}>
            {carregando ? 'Entrando...' : 'Entrar'}
          </button>
        </form>
        <p className="form-footer">
          Não tem conta? <Link to="/cadastro">Cadastre-se</Link>
        </p>
      </div>
    </main>
  )
}
