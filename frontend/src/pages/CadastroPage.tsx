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
    <main className="page">
      <div className="form-card">
        <h1 className="form-card__title">Criar conta</h1>
        <form onSubmit={handleSubmit}>
          <div className="form-group">
            <label htmlFor="nome">Nome</label>
            <input id="nome" name="nome" type="text" required />
          </div>
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
            {carregando ? 'Cadastrando...' : 'Criar conta'}
          </button>
        </form>
        <p className="form-footer">
          Já tem conta? <Link to="/login">Faça login</Link>
        </p>
      </div>
    </main>
  )
}
