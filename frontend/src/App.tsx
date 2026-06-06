import { Routes, Route, Navigate } from 'react-router-dom'
import Navbar from './components/Navbar'
import ProtectedRoute from './components/ProtectedRoute'
import LoginPage from './pages/LoginPage'
import CadastroPage from './pages/CadastroPage'

export default function App() {
  return (
    <>
      <Navbar />
      <Routes>
        <Route path="/" element={<Navigate to="/livros" replace />} />
        <Route path="/login" element={<LoginPage />} />
        <Route path="/cadastro" element={<CadastroPage />} />
        <Route path="/livros" element={<ProtectedRoute><div>Livros (em breve)</div></ProtectedRoute>} />
        <Route path="/meus-emprestimos" element={<ProtectedRoute><div>Empréstimos (em breve)</div></ProtectedRoute>} />
        <Route path="/livros/novo" element={<ProtectedRoute adminOnly><div>Cadastrar livro (em breve)</div></ProtectedRoute>} />
        <Route path="/livros/editar/:id" element={<ProtectedRoute adminOnly><div>Editar livro (em breve)</div></ProtectedRoute>} />
      </Routes>
    </>
  )
}
