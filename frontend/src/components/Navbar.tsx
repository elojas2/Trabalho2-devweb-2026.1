import { Link, useNavigate, useLocation } from 'react-router-dom'
import { useAuth } from '../contexts/AuthContext'

const PUBLIC_ROUTES = ['/login', '/cadastro']

export default function Navbar() {
  const { usuario, logout } = useAuth()
  const navigate = useNavigate()
  const { pathname } = useLocation()
  const isPublic = PUBLIC_ROUTES.includes(pathname)

  async function handleLogout() {
    await logout()
    navigate('/login')
  }

  return (
    <nav className="navbar">
      <div className="navbar__inner">
        <Link to="/livros" className="navbar__brand">BiblioTech</Link>

        {!isPublic && (
          <div className="navbar__links">
            {usuario ? (
              <>
                <span className="navbar__user">Olá, {usuario.nome}</span>
                {usuario.admin && (
                  <Link to="/livros/novo" className="btn btn--outline">Cadastrar Livro</Link>
                )}
                <Link to="/meus-emprestimos" className="btn btn--ghost">Meus Empréstimos</Link>
                <button onClick={handleLogout} className="btn btn--ghost">Sair</button>
              </>
            ) : null}
          </div>
        )}
      </div>
    </nav>
  )
}
