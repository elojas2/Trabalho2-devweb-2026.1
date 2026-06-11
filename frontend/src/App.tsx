import { Routes, Route, Navigate } from 'react-router-dom'
import Navbar from './components/Navbar'
import ProtectedRoute from './components/ProtectedRoute'
import LoginPage from './pages/LoginPage'
import LivrosPage from './pages/LivrosPage'
import EmprestimosPage from './pages/EmprestimosPage'
import CadastroPage from './pages/CadastroPage'
import LivroFormPage from './pages/LivroFormPage'

export default function App() {
  return (
    <>
      <Navbar />
      <Routes>
        <Route path="/" element={<Navigate to="/login" replace />} />
        
        <Route path="/login" element={<LoginPage />} />
        <Route path="/cadastro" element={<CadastroPage />} />
        
        <Route path="/livros" element={<ProtectedRoute><LivrosPage /></ProtectedRoute>} />
        <Route path="/meus-emprestimos" element={<ProtectedRoute><EmprestimosPage /></ProtectedRoute>} />
        <Route path="/livros/novo" element={<ProtectedRoute adminOnly><LivroFormPage /></ProtectedRoute>} />
        <Route path="/livros/editar/:id" element={<ProtectedRoute adminOnly><LivroFormPage /></ProtectedRoute>} />

        <Route path="*" element={<Navigate to="/login" replace />} />
      </Routes>
    </>
  )
}