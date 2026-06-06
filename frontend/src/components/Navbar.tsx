import { Link, useNavigate } from 'react-router-dom'
import { useAuth } from '../contexts/AuthContext'

export default function Navbar() {
  const { usuario, logout } = useAuth()
  const navigate = useNavigate()

  async function handleLogout() {
    await logout()
    navigate('/login')
  }

  return (
    <nav>
      <Link to="/livros">BiblioTech</Link>

      {usuario ? (
        <div>
          <span>Olá, {usuario.nome}</span>
          {usuario.admin && <Link to="/livros/novo">Cadastrar Livro</Link>}
          <Link to="/meus-emprestimos">Meus Empréstimos</Link>
          <button onClick={handleLogout}>Sair</button>
        </div>
      ) : (
        <div>
          <Link to="/login">Entrar</Link>
          <Link to="/cadastro">Cadastrar</Link>
        </div>
      )}
    </nav>
  )
}
